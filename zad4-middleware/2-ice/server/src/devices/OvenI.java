package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Oven;
import types.Smarthouse.OvenProgram;
import types.Smarthouse.GenericError;
import types.Smarthouse.State;

public class OvenI extends DeviceI implements Oven {
    private OvenProgram currentProgram = new OvenProgram();

    public OvenI(String name) {
        super(name);
    }

    @Override
    public OvenProgram getProgram(Current current) {
        return currentProgram;
    }

    @Override
    public void setProgram(OvenProgram program, Current current) throws GenericError {
        if (this.getState(null) != State.Running) {
            throw new GenericError("Oven must be turned on before setting program");
        }

        this.currentProgram = program;
    }
}
