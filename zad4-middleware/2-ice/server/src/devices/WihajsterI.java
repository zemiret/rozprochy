package devices;


import com.zeroc.Ice.Current;
import types.Smarthouse.GenericError;
import types.Smarthouse.State;
import types.Smarthouse.Wihajster;

public class WihajsterI extends BulbulatorI implements Wihajster {
    private String target = "";

    public WihajsterI(String name) {
        super(name);
    }

    @Override
    public void setTarget(String target, Current current) throws GenericError {
        System.out.println("Set target of: " + this.getName(null));
        checkRunning();
        this.target = target;
    }

    @Override
    public String describeTarget(Current current) throws GenericError {
        System.out.println("Describe target of: " + this.getName(null));
        checkRunning();
        return this.target + " to taki wihajster.";
    }
}
