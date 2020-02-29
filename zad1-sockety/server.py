# Each client - separate thread
# "Broadcast" to clients (keep clients in some iterable structre)
# TCP connection
# UDP connection
# Multicast

import socket
import threading
from concurrent import futures
import signal
import sys

import config
import message


class Server:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.tcp_serv_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.tcp_serv_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.tcp_serv_socket.bind(('', self.port))
        self.tcp_serv_socket.listen()

        self.udp_serv_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.udp_serv_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.udp_serv_socket.bind(('', self.port))

        self.connections = []  # Seems like python lists are thread-safe unless back-referenced
        self.conlock = threading.Lock()
        self.executor = None

        self.stop = False

    def _handle_client_tcp(self, saved_conn):
        conn, addr = saved_conn['conn'], saved_conn['addr']
        nick = addr

        while True:
            data = conn.recv(config.BUF_SIZE)
            if not data:
                # client disconnected
                print('Client {} disconnected'.format(addr))
                self.conlock.acquire()
                self.connections.remove(saved_conn)
                self.conlock.release()
                return

            data = str(data)

            if message.check(message.CHANGE_NICK, data):
                nick = message.extract(data)
            else:
                self.conlock.acquire()
                for c in self.connections:
                    if c['addr'] != addr:
                        c['conn'].sendall('{}: {}'.format(nick, data).encode('utf-8'))

                self.conlock.release()

    def _handle_client_udp(self, saved_conn):
        ip = saved_conn['addr'][0]
        addr = (ip, config.PORT)
        nick = saved_conn['addr']

        while not self.stop:
            data = str(self.udp_serv_socket.recv(config.BUF_SIZE))
            print('UDP Received ', data)

            if message.check(message.CHANGE_NICK, data):
                nick = message.extract(data)
            else:
                self.conlock.acquire()
                for c in self.connections:
                    to_addr = (c['addr'][0], config.PORT)
                    print('UDP addr: ', addr)
                    if addr != to_addr:
                        print('UDP send to: ', to_addr)
                        self.udp_serv_socket.sendto('{}: {}'.format(nick, data).encode('utf-8'), to_addr)

                self.conlock.release()

    def serve(self):
        with futures.ThreadPoolExecutor() as self.executor:
            while True:
                conn, addr = self.tcp_serv_socket.accept()
                saved_conn = {
                    'conn': conn,
                    'addr': addr,
                }
                print('Connecting: ', addr)

                self.conlock.acquire()
                self.connections.append(saved_conn)
                self.conlock.release()

                self.executor.submit(self._handle_client_tcp, saved_conn)
                self.executor.submit(self._handle_client_udp, saved_conn)

    def shutdown(self):
        self.stop = True
        if self.executor is not None:
            self.executor.shutdown(wait=False)

        self.conlock.acquire()
        for c in self.connections:
            print('killing: ', c['addr'])
            c['conn'].shutdown(socket.SHUT_RDWR)
            c['conn'].close()
        self.connections = []
        self.conlock.release()

        self.tcp_serv_socket.shutdown(socket.SHUT_RDWR)
        self.tcp_serv_socket.close()

        if self.executor is not None:
            self.executor._threads.clear()

        try:
            self.udp_serv_socket.shutdown(socket.SHUT_RDWR)
        except OSError:
            pass
        try:
            self.udp_serv_socket.close()
        except OSError:
            pass

        print('Closed connections')


if __name__ == "__main__":
    server = Server('', config.PORT)

    def handler(_=None, __=None):
        print('Shutting down...')
        server.shutdown()
        sys.exit(0)

    print('Starting server')
    signal.signal(signal.SIGINT, handler)
    server.serve()
