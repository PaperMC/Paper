package net.minecraft.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import joptsimple.OptionSet; // CraftBukkit

public class PropertyManager {

    private static final Logger loggingAgent = LogManager.getLogger();
    public final Properties properties = new Properties(); // CraftBukkit - private -> public
    private final File c;

    public PropertyManager(File file1) {
        this.c = file1;
        if (file1.exists()) {
            FileInputStream fileinputstream = null;

            try {
                fileinputstream = new FileInputStream(file1);
                this.properties.load(fileinputstream);
            } catch (Exception exception) {
                loggingAgent.warn("Failed to load " + file1, exception);
                this.a();
            } finally {
                if (fileinputstream != null) {
                    try {
                        fileinputstream.close();
                    } catch (IOException ioexception) {
                        ;
                    }
                }
            }
        } else {
            loggingAgent.warn(file1 + " does not exist");
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
        if ((this.options != null) && (this.options.has(name))) {
            return (T) this.options.valueOf(name);
        }

        return value;
    }
    // CraftBukkit end

    public void a() {
        loggingAgent.info("Generating new properties file");
        this.savePropertiesFile();
    }

    public void savePropertiesFile() {
        FileOutputStream fileoutputstream = null;

        try {
            // CraftBukkit start - Don't attempt writing to file if it's read only
            if (this.c.exists() && !this.c.canWrite()) {
                return;
            }
            // CraftBukkit end
            fileoutputstream = new FileOutputStream(this.c);
            this.properties.store(fileoutputstream, "Minecraft server properties");
        } catch (Exception exception) {
            loggingAgent.warn("Failed to save " + this.c, exception);
            this.a();
        } finally {
            if (fileoutputstream != null) {
                try {
                    fileoutputstream.close();
                } catch (IOException ioexception) {
                    ;
                }
            }
        }
    }

    public File c() {
        return this.c;
    }

    public String getString(String s, String s1) {
        if (!this.properties.containsKey(s)) {
            this.properties.setProperty(s, s1);
            this.savePropertiesFile();
            this.savePropertiesFile();
        }

        return this.getOverride(s, this.properties.getProperty(s, s1)); // CraftBukkit
    }

    public int getInt(String s, int i) {
        try {
            return this.getOverride(s, Integer.parseInt(this.getString(s, "" + i))); // CraftBukkit
        } catch (Exception exception) {
            this.properties.setProperty(s, "" + i);
            this.savePropertiesFile();
            return this.getOverride(s, i); // CraftBukkit
        }
    }

    public boolean getBoolean(String s, boolean flag) {
        try {
            return this.getOverride(s, Boolean.parseBoolean(this.getString(s, "" + flag))); // CraftBukkit
        } catch (Exception exception) {
            this.properties.setProperty(s, "" + flag);
            this.savePropertiesFile();
            return this.getOverride(s, flag); // CraftBukkit
        }
    }

    public void a(String s, Object object) {
        this.properties.setProperty(s, "" + object);
    }
}
