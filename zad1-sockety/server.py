import socket
import threading
import uuid
import os
from concurrent import futures

import config
import message


class TCPServer(threading.Thread):

    def __init__(self, ip, port, group=None, target=None, name=None,
                 args=(), kwargs=None, daemon=None):
        super().__init__(group, target, name, args, kwargs, daemon=daemon)

        self.ip = ip
        self.port = port
        self.tcp_serv_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.tcp_serv_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.tcp_serv_socket.bind(('', self.port))
        self.tcp_serv_socket.listen()

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
                print('TCP: Client {} disconnected'.format(addr))
                self.conlock.acquire()
                self.connections.remove(saved_conn)
                self.conlock.release()
                return

            data = str(data, 'utf-8')

            if message.check(message.CHANGE_NICK, data):
                nick = message.extract(data)
            else:
                self.conlock.acquire()

                msg = '{}: {}'.format(nick, data).encode('utf-8')
                for c in self.connections:
                    if c['addr'] != addr:
                        c['conn'].sendall(msg)
                self.conlock.release()

    def run(self):
        with futures.ThreadPoolExecutor() as self.executor:
            while True:
                try:
                    conn, addr = self.tcp_serv_socket.accept()
                    saved_conn = {
                        'conn': conn,
                        'addr': addr,
                    }
                    print('TCP: Connecting ', addr)

                    self.conlock.acquire()
                    self.connections.append(saved_conn)
                    self.conlock.release()

                    self.executor.submit(self._handle_client_tcp, saved_conn)
                except OSError:
                    # can happen when we killed the thread, and sock is closing
                    break

    def shutdown(self):
        if self.executor is not None:
            self.executor.shutdown(wait=False)

        self.conlock.acquire()
        for c in self.connections:
            print('TCP: killing ', c['addr'])
            c['conn'].shutdown(socket.SHUT_RDWR)
            c['conn'].close()
        self.connections = []
        self.conlock.release()

        self.tcp_serv_socket.shutdown(socket.SHUT_RDWR)
        self.tcp_serv_socket.close()

        print('TCP: Closed connections')


class UDPServer(threading.Thread):

    def __init__(self, ip, port, group=None, target=None, name=None,
                 args=(), kwargs=None, daemon=None):
        super().__init__(group, target, name, args, kwargs, daemon=daemon)

        self.ip = ip
        self.port = port

        self.udp_serv_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.udp_serv_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        self.udp_serv_socket.bind(('', self.port))

        self.connections = {}
        self.conlock = threading.Lock()
        self.magic = str.encode(str(uuid.uuid4()))

    def run(self):
        while True:
            msg, addr = self.udp_serv_socket.recvfrom(config.UDP_BUF_SIZE)
            if msg == self.magic:
                break

            msg = str(msg, 'utf-8')

            if message.check(message.CHANGE_NICK, msg):
                print('UDP: changenick ', addr)
                nick = message.extract(msg)

                self.conlock.acquire()
                self.connections[addr] = nick
                self.conlock.release()
            else:
                self.conlock.acquire()
                nick = self.connections[addr]
                for toaddr, _ in self.connections.items():
                    if addr != toaddr:
                        self.udp_serv_socket.sendto('{}:'.format(nick).encode('utf-8'), toaddr)
                        self.udp_serv_socket.sendto(msg.encode('utf-8'), toaddr)

                self.conlock.release()

    def shutdown(self):
        self.conlock.acquire()
        self.connections = {}
        self.conlock.release()

        self.udp_serv_socket.sendto(self.magic, (self.ip, self.port))
        self.udp_serv_socket.close()
        print('UDP: Closed connections')


if __name__ == "__main__":
    tcpServer = TCPServer('', config.PORT, daemon=True)
    udpServer = UDPServer('', config.PORT, daemon=True)

    print('Starting servers')
    tcpServer.start()
    udpServer.start()

    while True:
        try:
            user_in = input('')
        except KeyboardInterrupt:
            print('Shutting down...')
            udpServer.shutdown()

            tcpServer.shutdown()
            os._exit(0)

