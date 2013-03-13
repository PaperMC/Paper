package net.minecraft.server;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File; // CraftBukkit

public class ConsoleLogManager implements IConsoleLogManager {

    private final Logger a;
    private final String b;
    private final String c;
    private final String d;
    public static Logger global = Logger.getLogger(""); // CraftBukkit

    public ConsoleLogManager(String s, String s1, String s2) {
        this.a = Logger.getLogger(s);
        this.c = s;
        this.d = s1;
        this.b = s2;
        this.b();
    }

    private void b() {
        this.a.setUseParentHandlers(false);
        Handler[] ahandler = this.a.getHandlers();
        int i = ahandler.length;

        for (int j = 0; j < i; ++j) {
            Handler handler = ahandler[j];

            this.a.removeHandler(handler);
        }

        ConsoleLogFormatter consolelogformatter = new ConsoleLogFormatter(this, (EmptyClass3) null);

        // CraftBukkit start
        MinecraftServer server = MinecraftServer.getServer();
        ConsoleHandler consolehandler = new org.bukkit.craftbukkit.util.TerminalConsoleHandler(server.reader);
        // CraftBukkit end

        consolehandler.setFormatter(consolelogformatter);
        this.a.addHandler(consolehandler);

        // CraftBukkit start
        for (java.util.logging.Handler handler : global.getHandlers()) {
            global.removeHandler(handler);
        }

        consolehandler.setFormatter(new org.bukkit.craftbukkit.util.ShortConsoleLogFormatter(server));
        global.addHandler(consolehandler);
        // CraftBukkit end

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

            int j = 0;
            while (j < parentPath.length()) {
                char ch = parentPath.charAt(j);
                char ch2 = 0;
                if (j + 1 < parentPath.length()) {
                    ch2 = Character.toLowerCase(pattern.charAt(j + 1));
                }

                if (ch == '%') {
                    if (ch2 == 'h') {
                        j += 2;
                        fixedPattern.append(homeDir);
                        continue;
                    } else if (ch2 == 't') {
                        j += 2;
                        fixedPattern.append(tmpDir);
                        continue;
                    } else if (ch2 == '%') {
                        // Even though we don't care about this we have to skip it to avoid matching %%t
                        j += 2;
                        fixedPattern.append("%%");
                        continue;
                    } else if (ch2 != 0) {
                        throw new java.io.IOException("log-pattern can only use %t and %h for directories, got %" + ch2);
                    }
                }

                fixedPattern.append(ch);
                j++;
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
            this.a.addHandler(filehandler);
            global.addHandler(filehandler); // CraftBukkit
        } catch (Exception exception) {
            this.a.log(Level.WARNING, "Failed to log " + this.c + " to " + this.b, exception);
        }
    }

    public Logger getLogger() {
        return this.a;
    }

    public void info(String s) {
        this.a.log(Level.INFO, s);
    }

    public void warning(String s) {
        this.a.log(Level.WARNING, s);
    }

    public void warning(String s, Object... aobject) {
        this.a.log(Level.WARNING, s, aobject);
    }

    public void warning(String s, Throwable throwable) {
        this.a.log(Level.WARNING, s, throwable);
    }

    public void severe(String s) {
        this.a.log(Level.SEVERE, s);
    }

    public void severe(String s, Throwable throwable) {
        this.a.log(Level.SEVERE, s, throwable);
    }

    static String a(ConsoleLogManager consolelogmanager) {
        return consolelogmanager.d;
    }
}
