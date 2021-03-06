package main;


import com.zeroc.Ice.Object;
import com.zeroc.Ice.*;
import devices.DeviceCloudI;
import types.Smarthouse.Device;

public class StaticServantLocator implements ServantLocator {
    private final DeviceCloudI deviceCloud;

    public StaticServantLocator(DeviceCloudI deviceCloud) {
        this.deviceCloud = deviceCloud;
    }

    @Override
    public LocateResult locate(Current curr) throws UserException {
        String name = curr.id.name;

        Log.info("Instantiating servant for: " + name);

        if (name.equals(this.deviceCloud.getName())) {
            return new ServantLocator.LocateResult(this.deviceCloud, null);
        }

        Device device = deviceCloud.getByName(name);

        if (device == null) {
            Log.warning("Device not found for: " + name);
            throw new ObjectNotFoundException();
        }

        return new ServantLocator.LocateResult(device, null);
    }

    @Override
    public void deactivate(String category) {

    }

    @Override
    public void finished(Current curr, Object servant, java.lang.Object cookie) throws UserException {

    }
}
