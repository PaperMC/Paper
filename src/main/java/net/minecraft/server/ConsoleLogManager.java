package net.minecraft.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.logging.Handler; // CraftBukkit
import jline.ConsoleReader;
import org.bukkit.craftbukkit.util.ShortConsoleLogFormatter;
import org.bukkit.craftbukkit.util.TerminalConsoleHandler;

public class ConsoleLogManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public static Logger global = Logger.getLogger(""); // CraftBukkit

    public ConsoleLogManager() {}

    // Craftbukkit - change of method signature!
    public static void a(ConsoleReader reader) {
        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter();

        a.setUseParentHandlers(false);
        ConsoleHandler consolehandler = new TerminalConsoleHandler(reader);

        // CraftBukkit start
        for (Handler handler: global.getHandlers()) {
            global.removeHandler(handler);
        }
        // CraftBukkit end

        consolehandler.setFormatter(new ShortConsoleLogFormatter()); // Craftbukkit
        a.addHandler(consolehandler);
        global.addHandler(consolehandler); // CraftBukkit

        try {
            FileHandler filehandler = new FileHandler("server.log", true);

            filehandler.setFormatter(consolelogformatter);
            a.addHandler(filehandler);
            global.addHandler(filehandler); // CraftBukkit
        } catch (Exception exception) {
            a.log(Level.WARNING, "Failed to log to server.log", exception);
        }
    }
}
