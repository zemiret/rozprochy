package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Bulbulator;

public class BulbulatorI extends DeviceI implements Bulbulator {

    public BulbulatorI(String name) {
        super(name);
    }

    @Override
    public String[] getJoke(Current current) {
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

