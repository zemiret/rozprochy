# Each client - separate thread
# "Broadcast" to clients (keep clients in some iterable structre)
# TCP connection
# UDP connection
# Multicast

import socket
import threading
from concurrent import futures
import signal

import config
import sys


class TCPServer:
    def __init__(self, ip, port):
        self.ip = ip
        self.port = port
        self.serv_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.serv_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.serv_socket.bind((self.ip, self.port))
        self.serv_socket.listen()

        self.connections = []  # Seems like python lists are thread-safe unless back-referenced
        self.conlock = threading.Lock()
        # self.stop = False
        self.executor = None

    def _handle_client(self, saved_conn):
        conn, addr = saved_conn['conn'], saved_conn['addr']

        while True:
            data = conn.recv(config.BUF_SIZE)
            if not data:
                # client disconnected
                print('Client {} disconnected'.format(addr))
                self.conlock.acquire()
                self.connections.remove(saved_conn)
                self.conlock.release()
                return

            self.conlock.acquire()
            for c in self.connections:
                if c['addr'] != addr:
                    c['conn'].sendall('{}: {}'.format(addr, str(data)).encode('utf-8'))

            self.conlock.release()

        # self.connections.remove(saved_conn)

    def serve(self):
        with futures.ThreadPoolExecutor() as self.executor:
            while True:
                conn, addr = self.serv_socket.accept()
                saved_conn = {
                    'conn': conn,
                    'addr': addr,
                }
                print('Connecting: ', addr)

                self.conlock.acquire()
                self.connections.append(saved_conn)
                self.conlock.release()
                self.executor.submit(self._handle_client, saved_conn)

    def shutdown(self):
        # self.stop = True

        if self.executor is not None:
            self.executor.shutdown(wait=False)
            # self.executor._threads.clear()

        self.conlock.acquire()
        for c in self.connections:
            print('killing: ', c['addr'])
            c['conn'].shutdown(socket.SHUT_RDWR)
            c['conn'].close()
        self.conlock.release()

        self.serv_socket.shutdown(socket.SHUT_RDWR)
        self.serv_socket.close()


if __name__ == "__main__":
    server = TCPServer('', config.TCP_PORT)

    def handler(_=None, __=None):
        print('Shutting down...')
        server.shutdown()
        sys.exit(0)

    print('Starting server')
    signal.signal(signal.SIGINT, handler)
    server.serve()
