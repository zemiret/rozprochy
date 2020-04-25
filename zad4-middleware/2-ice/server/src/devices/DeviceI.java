package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Device;
import types.Smarthouse.GenericError;
import types.Smarthouse.State;

public abstract class DeviceI implements Device {
    private final String name;
    private State state;

    public DeviceI(String name) {
        this.name = name;
    }

    @Override
    public String getName(Current current) {
        return name;
    }

    @Override
    public State getState(Current current) {
        return state;
    }

    @Override
    public void turnOn(Current current) throws GenericError {
        this.state = State.Running;
    }

    @Override
    public void turnOff(Current current) throws GenericError {
        this.state = State.Off;
    }
}
