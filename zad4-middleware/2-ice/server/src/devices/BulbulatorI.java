package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Bulbulator;
import types.Smarthouse.GenericError;

public class BulbulatorI extends DeviceI implements Bulbulator {

    public BulbulatorI(String name) {
        super(name);
    }

    @Override
    public String[] getJoke(Current current) throws GenericError {
        System.out.println("Get joke of: " + this.getName(null));
        checkRunning();

        return new String[]{
                "Co to jest ten bulbulator?",
                "No coś w stylu wihajstra.",
                "Co to jest wihajster?",
                "Taki mały mecykator.",
                "Nie, raczej nie... Zbyt podobne do brumbadła.",
                "Coś ty, raczej do cyngwajsa, nie myl chłopakowi w głowie."
        };
    }
}

