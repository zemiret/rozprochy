package main;

import devices.*;
import types.Smarthouse.Bulbulator;
import types.Smarthouse.Mykens;
import types.Smarthouse.Oven;
import types.Smarthouse.Wihajster;

public class Server {
    public static void main(String[] args) {
        DeviceCloudI deviceCloud = new DeviceCloudI("cloud");

        Oven oven = new OvenI("piekarnik");

        Bulbulator bulbulator = new BulbulatorI("bulbulator");

        Wihajster wihajster1 = new WihajsterI("wihajster1");
        Wihajster wihajster2 = new WihajsterI("wihajster2");

        Mykens mykens1 = new MykensI("mykens1");
        Mykens mykens2 = new MykensI("mykens2");

        deviceCloud.addDevice(oven);
        deviceCloud.addDevice(bulbulator);
        deviceCloud.addDevice(wihajster1);
        deviceCloud.addDevice(wihajster2);
        deviceCloud.addDevice(mykens1);
        deviceCloud.addDevice(mykens2);


        Log.info("Starting server...");
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args)) {
            StaticServantLocator staticServantLocator = new StaticServantLocator(deviceCloud);

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("SmarthouseAdapter", "default -p 10000");
            adapter.addServantLocator(staticServantLocator, "");

            adapter.activate();

            Log.info("Server listening on :10000");
            communicator.waitForShutdown();
        }
    }
}
