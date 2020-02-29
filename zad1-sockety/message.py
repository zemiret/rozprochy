CHANGE_NICK = '!changenick'


def create(mtype, content):
    return '{}:{}'.format(mtype, content).encode('utf-8')


def check(mtype, msg):
    return mtype in msg


def extract(msg):
    return msg.split(':')[1]
