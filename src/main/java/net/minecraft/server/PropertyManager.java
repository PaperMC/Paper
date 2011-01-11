package net.minecraft.server;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import joptsimple.OptionSet; // CraftBukkit

public class PropertyManager {

    public static Logger a = Logger.getLogger("Minecraft");
    private Properties b;
    private File c;

    public PropertyManager(File file) {
        b = new Properties();
        c = file;
        if (file.exists()) {
            try {
                b.load(((java.io.InputStream) (new FileInputStream(file))));
            } catch (Exception exception) {
                a.log(Level.WARNING, (new StringBuilder()).append("Failed to load ").append(((file))).toString(), ((Throwable) (exception)));
                a();
            }
        } else {
            a.log(Level.WARNING, (new StringBuilder()).append(((file))).append(" does not exist").toString());
            a();
        }
    }

    // Craftbukkit start
    private OptionSet options = null;

    public PropertyManager(final OptionSet options) {
        this((File)options.valueOf("config"));

        this.options = options;
    }

    private <T> T getOverride(String name, T value) {
        if ((options != null) && (options.has(name))) {
            return (T)options.valueOf(name);
        }

        return value;
    }
    // Craftbukkit end

    public void a() {
        a.log(Level.INFO, "Generating new properties file");
        b();
    }

    public void b() {
        try {
            b.store(((java.io.OutputStream) (new FileOutputStream(c))), "Minecraft server properties");
        } catch (Exception exception) {
            a.log(Level.WARNING, (new StringBuilder()).append("Failed to save ").append(((c))).toString(), ((Throwable) (exception)));
            a();
        }
    }

    public String a(String s, String s1) {
        if (!b.containsKey(((s)))) {
            b.setProperty(s, getOverride(s, s1)); // Craftbukkit
            b();
        }
        return getOverride(s, b.getProperty(s, s1)); // Craftbukkit
    }

    public int a(String s, int i) {
        try {
            return getOverride(s, Integer.parseInt(a(s, String.valueOf(i)))); // Craftbukkit
        } catch (Exception exception) {
            b.setProperty(s, getOverride(s, i).toString()); // Craftbukkit
        }
        return getOverride(s, i); // Craftbukkit
    }

    public boolean a(String s, boolean flag) {
        try {
            return getOverride(s, Boolean.parseBoolean(a(s, String.valueOf(flag)))); // Craftbukkit
        } catch (Exception exception) {
            b.setProperty(s, getOverride(s, flag).toString()); // Craftbukkit
        }
        return getOverride(s, flag); // Craftbukkit
    }
}
