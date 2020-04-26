import sys, Ice
import Smarthouse
from config import CLOUD_NAME
from devices.bulbulator import bulbulator_handler
from devices.device import select_device_type
from devices.mykens import mykens_handler
from devices.oven import oven_handler
from devices.wihajster import wihajster_handler
from utils.tools import connection_string


def operation_menu():
    print("Choose operation type")
    print("0: list devices")
    print("1: talk to device")

    which = int(input('> '))
    if not 0 <= which <= 1:
        raise RuntimeError("Input not in range")

    return which


def list_devices(communicator):
    base = communicator.stringToProxy(connection_string(CLOUD_NAME))
    cloud = Smarthouse.DeviceCloudPrx.checkedCast(base)
    device_list = cloud.listDevices()

    print('Available devices')
    for device in device_list:
        print(device)

    print()


def handle_device(communicator, DevicePrxCls, device_name):
    base = communicator.stringToProxy(connection_string(device_name))
    device_prx = DevicePrxCls.checkedCast(base)

    if not device_prx:
        raise RuntimeError("Invalid proxy!")

    handler = None
    if DevicePrxCls == Smarthouse.OvenPrx:
        handler = oven_handler()
    if DevicePrxCls == Smarthouse.BulbulatorPrx:
        handler = bulbulator_handler()
    if DevicePrxCls == Smarthouse.WihajsterPrx:
        handler = wihajster_handler()
    if DevicePrxCls == Smarthouse.MykensPrx:
        handler = mykens_handler()

    if handler is None:
        raise RuntimeError("Did not locate handler for the operation!")

    handler(device_prx)


def main():
    with Ice.initialize(sys.argv) as communicator:
        while True:
            try:
                operation = operation_menu()
                if operation == 0:
                    list_devices(communicator)
                else:
                    DevicePrxCls = select_device_type()
                    device_name = input("device name: > ")
                    handle_device(communicator, DevicePrxCls, device_name)

            except Smarthouse.GenericError as e:
                print('Invalid operation: ', e.reason)
                print()
            except Exception as e:
                print(e)
                print()


if __name__ == "__main__":
    main()
