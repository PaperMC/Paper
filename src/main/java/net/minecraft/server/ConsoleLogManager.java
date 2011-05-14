package net.minecraft.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

// CraftBukkit start
import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import org.bukkit.craftbukkit.util.ShortConsoleLogFormatter;
import org.bukkit.craftbukkit.util.TerminalConsoleHandler;
// CraftBukkit end

public class ConsoleLogManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public static Logger global = Logger.getLogger(""); // CraftBukkit

    public ConsoleLogManager() {}

    // CraftBukkit - change of method signature!
    public static void init(MinecraftServer server) {
        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter();

        a.setUseParentHandlers(false);
        // CraftBukkit start
        ConsoleHandler consolehandler = new TerminalConsoleHandler(server.reader);

        for (Handler handler: global.getHandlers()) {
            global.removeHandler(handler);
        }

        consolehandler.setFormatter(new ShortConsoleLogFormatter(server));
        global.addHandler(consolehandler);
        // CraftBukkit end

        a.addHandler(consolehandler);

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
