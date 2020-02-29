CHANGE_NICK = '!changenick'
IGNORE = '!ignore'


def create(mtype, content):
    return '{}:{}'.format(mtype, content).encode('utf-8')


def check(mtype, msg):
    return mtype in msg


def extract(msg):
    return ':'.join(msg.split(':')[1:])
