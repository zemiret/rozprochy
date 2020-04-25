package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Device;
import types.Smarthouse.DeviceCloud;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DeviceCloudI implements DeviceCloud {
    private final Map<String, Device> devices = new HashMap<>();

    @Override
    public String[] listDevices(Current current) {
        List<String> deviceNames = new LinkedList<>();

        for (Device d: this.devices.values()) {
            deviceNames.add(d.getName(null));
        }

        return deviceNames.toArray(new String[0]);
    }

    public void addDevice(Device device) {
        this.devices.put(device.getName(null), device);
    }

    public Device getByName(String name) {
        return this.devices.get(name);
    }
}
