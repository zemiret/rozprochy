import socket
import struct
import threading
import os

import config
import sys
import message


class TcpRecHandler(threading.Thread):
    def __init__(self, sock, group=None, target=None, name=None, args=(), kwargs=None, daemon=None):
        super().__init__(group, target, name, args, kwargs, daemon=daemon)

        self.socket = sock

    def run(self) -> None:
        while True:
            data = self.socket.recv(config.BUF_SIZE)
            if not data:
                print('Server died. Quitting...')
                os._exit(1)

            data = str(data, 'utf-8')
            print(data)


class UdpResHandler(threading.Thread):
    def __init__(self, sock, group=None, target=None, name=None, args=(), kwargs=None, daemon=None):
        super().__init__(group, target, name, args, kwargs, daemon=daemon)

        self.socket = sock

    def run(self) -> None:
        while True:
            data = str(self.socket.recv(config.BUF_SIZE), 'utf-8')
            print(data)


class MultiResHandler(UdpResHandler):
    def __init__(self, sock, nick=None, group=None, target=None, name=None, args=(), kwargs=None, daemon=None):
        super().__init__(sock, group, target, name, args, kwargs, daemon=daemon)
        self.nick = nick

    def run(self) -> None:
        while True:
            data = str(self.socket.recv(config.BUF_SIZE), 'utf-8')

            if not data.startswith('{}:'.format(self.nick)):
                print(data)


def create_tcp_socket(ip, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    return s


def create_udp_socket():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return s


def create_multicast_server_socket():
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.settimeout(0.2)
    ttl = struct.pack('b', 1)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)
    return sock


def create_multicast_client_socket(ip, port):
    # Hmm. This must be only bind once
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind((ip, port))
    return sock


if __name__ == '__main__':
    server_ip = config.HOST

    tcp_sock = create_tcp_socket(server_ip, config.PORT)
    udp_sock = create_udp_socket()

    tcpRecHandler = TcpRecHandler(tcp_sock, daemon=True)
    tcpRecHandler.start()

    udpRecHandler = UdpResHandler(udp_sock, daemon=True)
    udpRecHandler.start()

    multi_server_sock = create_multicast_server_socket()
    multi_client_sock = create_multicast_client_socket(config.MULTI_IP, config.MULTI_PORT)

    group = socket.inet_aton(config.MULTI_IP)
    mreq = struct.pack('4sL', group, socket.INADDR_ANY)
    multi_client_sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    nickname = input('Nickname: ')
    tcp_sock.sendall(message.create(message.CHANGE_NICK, nickname))
    udp_sock.sendto(message.create(message.CHANGE_NICK, nickname), (server_ip, config.PORT))

    multiResHandler = MultiResHandler(multi_client_sock, nick=nickname, daemon=True)
    multiResHandler.start()

    while True:
        try:
            user_in = input('>')
            if user_in == 'U':
                user_in = input()
                udp_sock.sendto(user_in.encode('utf-8'), (server_ip, config.PORT))
            elif user_in == 'M':
                user_in = input()
                multi_server_sock.sendto('{}: {}'.format(nickname, user_in).encode('utf-8'),
                                         (config.MULTI_IP, config.MULTI_PORT)
                                         )
            else:
                tcp_sock.sendall(user_in.encode('utf-8'))
        except KeyboardInterrupt:
            udp_sock.close()
            tcp_sock.close()
            multi_client_sock.close()
            multi_server_sock.close()
            sys.exit(0)
