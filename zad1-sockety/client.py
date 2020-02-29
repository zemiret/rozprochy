# Console handling
# Multicast

import socket
import threading
import os

import config
import sys


class ReceiveHandler(threading.Thread):
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


def create_socket(ip, port):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect((ip, port))
    return s


if __name__ == '__main__':
    sock = create_socket(config.TCP_HOST, config.TCP_PORT)

    recHandler = ReceiveHandler(sock, daemon=True)
    recHandler.start()

    while True:
        try:
            user_in = input('>')
            sock.sendall(user_in.encode('utf-8'))
        except KeyboardInterrupt:
            sys.exit(0)
