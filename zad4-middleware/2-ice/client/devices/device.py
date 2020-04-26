import Smarthouse
from utils.tools import select_opt

DEVICES = (
    ('piekarnik', Smarthouse.OvenPrx),
    ('bulbulator', Smarthouse.BulbulatorPrx),
    ('wihajster', Smarthouse.WihajsterPrx),
    ('mykens', Smarthouse.MykensPrx)
)


def turn_on(device_prx):
    device_prx.turnOn()


def turn_off(device_prx):
    device_prx.turnOff()


def get_name(device_prx):
    print("Name: {}".format(device_prx.getName()))
    print()


def get_state(device_prx):
    print("State: {}".format(device_prx.getState()))
    print()


DEVICE_OPTS = (
    ('get_name', get_name),
    ('get_state', get_state),
    ('turn_on', turn_on),
    ('turn_off', turn_off),
)


def select_device_type():
    print('Choose device type')
    return DEVICES[select_opt(DEVICES)][1]
