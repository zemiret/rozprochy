package devices;


import com.zeroc.Ice.Current;
import types.Smarthouse.Wihajster;

public class WihajsterI extends BulbulatorI implements Wihajster {
    private String target = "";

    public WihajsterI(String name) {
        super(name);
    }

    @Override
    public void setTarget(String target, Current current) {
        this.target = target;
    }

    @Override
    public String describeTarget(Current current) {
        return this.target + " to taki wihajster.";
    }
}
