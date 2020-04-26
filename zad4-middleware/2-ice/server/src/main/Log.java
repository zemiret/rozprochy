package main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private static final Logger logger = Logger.getLogger(Log.class.getName());

    public static void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    public static void warning(String msg) {
        logger.log(Level.WARNING, msg);
    }
}
