package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import joptsimple.OptionSet; // CraftBukkit

public class PropertyManager {

    public static Logger a = Logger.getLogger("Minecraft");
    private Properties properties = new Properties();
    private File c;

    public PropertyManager(File file1) {
        this.c = file1;
        if (file1.exists()) {
            try {
                this.properties.load(new FileInputStream(file1));
            } catch (Exception exception) {
                a.log(Level.WARNING, "Failed to load " + file1, exception);
                this.a();
            }
        } else {
            a.log(Level.WARNING, file1 + " does not exist");
            this.a();
        }
    }

    // CraftBukkit start
    private OptionSet options = null;

    public PropertyManager(final OptionSet options) {
        this((File) options.valueOf("config"));

        this.options = options;
    }

    private <T> T getOverride(String name, T value) {
        if ((options != null) && (options.has(name))) {
            return (T) options.valueOf(name);
        }

        return value;
    }
    // CraftBukkit end

    public void a() {
        a.log(Level.INFO, "Generating new properties file");
        this.b();
    }

    public void b() {
        try {
            this.properties.store(new FileOutputStream(this.c), "Minecraft server properties");
        } catch (Exception exception) {
            a.log(Level.WARNING, "Failed to save " + this.c, exception);
            this.a();
        }
    }

    public String getString(String s, String s1) {
        if (!this.properties.containsKey(s)) {
            s1 = getOverride(s, s1); // CraftBukkit
            this.properties.setProperty(s, s1);
            this.b();
        }

        return getOverride(s, this.properties.getProperty(s, s1)); // CraftBukkit
    }

    public int getInt(String s, int i) {
        try {
            return getOverride(s, Integer.parseInt(this.getString(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            i = getOverride(s, i); // CraftBukkit
            this.properties.setProperty(s, "" + i);
            return i;
        }
    }

    public boolean getBoolean(String s, boolean flag) {
        try {
            return getOverride(s, Boolean.parseBoolean(this.getString(s, "" + flag))); // CraftBukkit
        } catch (Exception exception) {
            flag = getOverride(s, flag); // CraftBukkit
            this.properties.setProperty(s, "" + flag);
            return flag;
        }
    }

    public void b(String s, boolean flag) {
        flag = getOverride(s, flag); // CraftBukkit
        this.properties.setProperty(s, "" + flag);
        this.b();
    }
}
