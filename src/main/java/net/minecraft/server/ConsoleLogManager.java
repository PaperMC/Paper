package net.minecraft.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLogManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public static Logger global = Logger.getLogger(""); // Craftbukkit

    public ConsoleLogManager() {}

    public static void a() {
        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter();

        a.setUseParentHandlers(false);
        ConsoleHandler consolehandler = new ConsoleHandler();

        // Craftbukkit start
        for (Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }
        // Craftbukkit end

        consolehandler.setFormatter(consolelogformatter);
        a.addHandler(consolehandler);
        global.addHandler(consolehandler); // Craftbukkit

        try {
            FileHandler filehandler = new FileHandler("server.log", true);

            filehandler.setFormatter(consolelogformatter);
            a.addHandler(filehandler);
            global.addHandler(filehandler); // Craftbukkit
        } catch (Exception exception) {
            a.log(Level.WARNING, "Failed to log to server.log", exception);
        }
    }
}