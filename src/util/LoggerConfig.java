package util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic logging configuration for the app (can be extended).
 */
public class LoggerConfig {
    public static void configure() {
        Logger root = Logger.getLogger("");
        root.setLevel(Level.INFO);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);
        root.addHandler(handler);
    }
}
