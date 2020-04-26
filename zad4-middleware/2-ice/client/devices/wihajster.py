from devices.bulbulator import BULBULATOR_OPTS
from utils.tools import select_opt


def set_target(wihajster_prx):
    print('Set wihajster target')
    target = input('new target: ')
    print()
    wihajster_prx.setTarget(target)


def describe_target(wihajster_prx):
    print(wihajster_prx.describeTarget())
    print()


WIHAJSTER_OPTS = BULBULATOR_OPTS + (
    ('set_target', set_target),
    ('describe_target', describe_target),
)


def wihajster_handler():
    print('Choose wihajster operation')
    return WIHAJSTER_OPTS[select_opt(WIHAJSTER_OPTS)][1]
