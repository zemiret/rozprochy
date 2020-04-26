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
        this.state = State.Running;
    }

    @Override
    public String getName(Current current) {
        return name;
    }

    @Override
    public State getState(Current current) {
        System.out.println("Get state of: " + this.getName(null));
        return this.state;
    }

    @Override
    public void turnOn(Current current) {
        System.out.println("Turning on: " + this.getName(null));
        this.state = State.Running;
    }

    @Override
    public void turnOff(Current current) {
        System.out.println("Turning off: " + this.getName(null));
        this.state = State.Off;
    }

    protected void checkRunning() throws GenericError {
        if (this.getState(null) != State.Running) {
            throw new GenericError("Device must be turned on before the operation");
        }
    }
}
