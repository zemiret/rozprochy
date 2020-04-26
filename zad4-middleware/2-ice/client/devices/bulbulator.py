from devices.device import DEVICE_OPTS
from utils.tools import select_opt


def get_joke(bulbulator_prx):
    joke = bulbulator_prx.getJoke()
    print('Bulbulator joke: ')
    for line in joke:
        print('* {}'.format(line))
    print()


BULBULATOR_OPTS = DEVICE_OPTS + (
    ('get_joke', get_joke),
)


def bulbulator_handler():
    print('Choose bulbulator operation')
    return BULBULATOR_OPTS[select_opt(BULBULATOR_OPTS)][1]

