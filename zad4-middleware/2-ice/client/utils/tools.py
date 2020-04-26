from config import PORT_NUM


def connection_string(device):
    return "{}:default -p {}".format(device, PORT_NUM)


def select_opt(opts):
    for i, d in enumerate(opts):
        print('{}: {}'.format(i, d[0]))

    which = int(input('>'))
    print()

    if not 0 <= which <= len(opts):
        raise RuntimeError("Input not in range")

    return which
