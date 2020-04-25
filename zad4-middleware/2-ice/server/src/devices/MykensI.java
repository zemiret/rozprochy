package devices;

import com.zeroc.Ice.Current;
import types.Smarthouse.Mykens;

import java.util.Collections;

public class MykensI extends BulbulatorI implements Mykens {
    private int speed = 0;

    public MykensI(String name) {
        super(name);
    }

    @Override
    public void setSpeed(int speed, Current current) {
        this.speed = speed;
    }

    @Override
    public String mykensuj(Current current) {
        return "Mykensuję: " + String.join(", ", Collections.nCopies(this.speed, "myk"));
    }
}
