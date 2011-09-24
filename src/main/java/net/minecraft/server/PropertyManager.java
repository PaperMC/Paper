package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyManager {

    public static Logger a = Logger.getLogger("Minecraft");
    public Properties properties = new Properties(); // CraftBukkit - priv to pub
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
    private joptsimple.OptionSet options = null;

    public PropertyManager(final joptsimple.OptionSet options) {
        this((File) options.valueOf("config"));

        this.options = options;
    }

    private <T> T getOverride(String name, T value) {
        if ((this.options != null) && (this.options.has(name))) {
            return (T) this.options.valueOf(name);
        }

        return value;
    }
    // CraftBukkit end

    public void a() {
        a.log(Level.INFO, "Generating new properties file");
        this.savePropertiesFile();
    }

    public void savePropertiesFile() {
        try {
            this.properties.store(new FileOutputStream(this.c), "Minecraft server properties");
        } catch (Exception exception) {
            a.log(Level.WARNING, "Failed to save " + this.c, exception);
            this.a();
        }
    }

    public String getString(String s, String s1) {
        if (!this.properties.containsKey(s)) {
            s1 = this.getOverride(s, s1); // CraftBukkit
            this.properties.setProperty(s, s1);
            this.savePropertiesFile();
        }

        return this.getOverride(s, this.properties.getProperty(s, s1)); // CraftBukkit
    }

    public int getInt(String s, int i) {
        try {
            return this.getOverride(s, Integer.parseInt(this.getString(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            i = this.getOverride(s, i); // CraftBukkit
            this.properties.setProperty(s, "" + i);
            return i;
        }
    }

    public boolean getBoolean(String s, boolean flag) {
        try {
            return this.getOverride(s, Boolean.parseBoolean(this.getString(s, "" + flag))); // CraftBukkit
        } catch (Exception exception) {
            flag = this.getOverride(s, flag); // CraftBukkit
            this.properties.setProperty(s, "" + flag);
            return flag;
        }
    }

    public void setBoolean(String s, boolean flag) {
        this.properties.setProperty(s, "" + flag);
        this.savePropertiesFile();
    }
}
