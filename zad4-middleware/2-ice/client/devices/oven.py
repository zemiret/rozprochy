import Smarthouse
from devices.device import DEVICE_OPTS
from utils.tools import select_opt


def get_program(oven_prx):
    program = oven_prx.getProgram()
    print('Oven program: ')
    print(program)
    print()


def set_program(oven_prx):
    print('Set new program')
    temperature = float(input('temperature: '))
    hours = int(input('hours: '))
    minutes = int(input('minutes: '))
    seconds = int(input('seconds: '))

    program = Smarthouse.OvenProgram(temperature, hours, minutes, seconds)
    print()
    oven_prx.setProgram(program)


OVEN_OPTS = DEVICE_OPTS + (
    ('get_program', get_program),
    ('set_program', set_program)
)


def oven_handler():
    print('Choose oven operation')
    return OVEN_OPTS[select_opt(OVEN_OPTS)][1]
