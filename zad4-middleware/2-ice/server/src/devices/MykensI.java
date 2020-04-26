package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.GenericError;
import types.Smarthouse.Mykens;
import types.Smarthouse.State;

import java.util.Collections;

public class MykensI extends BulbulatorI implements Mykens {
    private int speed = 0;

    public MykensI(String name) {
        super(name);
    }

    @Override
    public void setSpeed(int speed, Current current) throws GenericError {
        System.out.println("Set speed of: " + this.getName(null));
        checkRunning();
        this.speed = speed;
    }

    @Override
    public String mykensuj(Current current) throws GenericError {
        System.out.println("Mykensuj of: " + this.getName(null));
        checkRunning();
        return String.join(", ", Collections.nCopies(this.speed, "myk"));
    }
}
