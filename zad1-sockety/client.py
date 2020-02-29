import socket
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

            print(str(data))


class UdpResHandler(threading.Thread):
    def __init__(self, sock, group=None, target=None, name=None, args=(), kwargs=None, daemon=None):
        super().__init__(group, target, name, args, kwargs, daemon=daemon)

        self.socket = sock

    def run(self) -> None:
        while True:
            data = str(self.socket.recv(config.BUF_SIZE))
            print(data)


def create_tcp_socket(ip, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    return s


def create_udp_socket():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    return s


if __name__ == '__main__':
    server_ip = config.HOST

    tcp_sock = create_tcp_socket(server_ip, config.PORT)
    udp_sock = create_udp_socket()

    tcpRecHandler = TcpRecHandler(tcp_sock, daemon=True)
    tcpRecHandler.start()

    udpRecHandler = UdpResHandler(udp_sock, daemon=True)
    udpRecHandler.start()

    nickname = input('Nickname: ')
    tcp_sock.sendall(message.create(message.CHANGE_NICK, nickname))
    udp_sock.sendto(message.create(message.CHANGE_NICK, nickname), (server_ip, config.PORT))

    while True:
        try:
            user_in = input('>')
            if user_in == 'U':
                user_in = input()
                udp_sock.sendto(user_in.encode('utf-8'), (server_ip, config.PORT))
            else:
                tcp_sock.sendall(user_in.encode('utf-8'))
        except KeyboardInterrupt:
            udp_sock.close()
            tcp_sock.close()
            sys.exit(0)
