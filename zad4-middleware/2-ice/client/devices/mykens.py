from devices.bulbulator import BULBULATOR_OPTS
from utils.tools import select_opt


def set_speed(mykens_prx):
    speed = int(input('new speed: '))
    print()
    mykens_prx.setSpeed(speed)


def mykensuj(mykens_prx):
    print(mykens_prx.mykensuj())
    print()


MYKENS_OPTS = BULBULATOR_OPTS + (
    ('set_speed', set_speed),
    ('mykensuj', mykensuj)
)


def mykens_handler():
    print('Choose mykens operation')
    return MYKENS_OPTS[select_opt(MYKENS_OPTS)][1]
