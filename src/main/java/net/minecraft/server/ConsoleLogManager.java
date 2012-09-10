package net.minecraft.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File; // CraftBukkit

public class ConsoleLogManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public static Logger global = Logger.getLogger(""); // CraftBukkit

    // CraftBukkit - change of method signature!
    public static void init(MinecraftServer server) {
        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter(server.options.has("log-strip-color")); // CraftBukkit - pass strip color option

        a.setUseParentHandlers(false);
        // CraftBukkit start
        ConsoleHandler consolehandler = new org.bukkit.craftbukkit.util.TerminalConsoleHandler(server.reader);

        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }

        consolehandler.setFormatter(new org.bukkit.craftbukkit.util.ShortConsoleLogFormatter(server));
        global.addHandler(consolehandler);
        // CraftBukkit end

        a.addHandler(consolehandler);

        try {
            // CraftBukkit start
            String pattern = (String) server.options.valueOf("log-pattern");

            // We have to parse the pattern ourself so we can create directories as needed (java #6244047)
            String tmpDir = System.getProperty("java.io.tmpdir");
            String homeDir = System.getProperty("user.home");
            if (tmpDir == null) {
                tmpDir = homeDir;
            }

            // We only care about parsing for directories, FileHandler can do file names by itself
            File parent = new File(pattern).getParentFile();
            StringBuilder fixedPattern = new StringBuilder();
            String parentPath = "";
            if (parent != null) {
                parentPath = parent.getPath();
            }

            int i = 0;
            while (i < parentPath.length()) {
                char ch = parentPath.charAt(i);
                char ch2 = 0;
                if (i + 1 < parentPath.length()) {
                    ch2 = Character.toLowerCase(pattern.charAt(i + 1));
                }

                if (ch == '%') {
                    if (ch2 == 'h') {
                        i += 2;
                        fixedPattern.append(homeDir);
                        continue;
                    } else if (ch2 == 't') {
                        i += 2;
                        fixedPattern.append(tmpDir);
                        continue;
                    } else if (ch2 == '%') {
                        // Even though we don't care about this we have to skip it to avoid matching %%t
                        i += 2;
                        fixedPattern.append("%%");
                        continue;
                    } else if (ch2 != 0) {
                        throw new java.io.IOException("log-pattern can only use %t and %h for directories, got %" + ch2);
                    }
                }

                fixedPattern.append(ch);
                i++;
            }

            // Try to create needed parent directories
            parent = new File(fixedPattern.toString());
            if (parent != null) {
                parent.mkdirs();
            }

            int limit = (Integer) server.options.valueOf("log-limit");
            int count = (Integer) server.options.valueOf("log-count");
            boolean append = (Boolean) server.options.valueOf("log-append");
            FileHandler filehandler = new FileHandler(pattern, limit, count, append);
            // CraftBukkit end

            filehandler.setFormatter(consolelogformatter);
            a.addHandler(filehandler);
            global.addHandler(filehandler); // CraftBukkit
        } catch (Exception exception) {
            a.log(Level.WARNING, "Failed to log to server.log", exception);
        }
    }
}
