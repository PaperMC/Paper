package org.bukkit.craftbukkit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.SharedConstants;
import org.bukkit.Bukkit;

public final class Versioning {
    private static final String BUKKIT_VERSION;
    private static final String API_VERSION;

    static {
        String bukkitVersion = "Unknown-Version";
        String apiVersion = null;
        try (final InputStream stream = Bukkit.class.getClassLoader().getResourceAsStream("apiVersioning.json")) {
            if (stream == null) {
                throw new IOException("apiVersioning.json not found in classpath");
            }

            final JsonObject jsonObject = new Gson()
                .fromJson(new BufferedReader(new InputStreamReader(stream)), JsonObject.class);

            if (jsonObject == null) {
                throw new IOException("apiVersioning.json is not a valid JSON file");
            }

            bukkitVersion = jsonObject.get("version").getAsString();
            apiVersion = jsonObject.get("currentApiVersion").getAsString();
        } catch (final IOException ex) {
            Logger.getLogger(Versioning.class.getName()).log(Level.SEVERE, "Could not get Bukkit version!", ex);
        }
        BUKKIT_VERSION = bukkitVersion;
        if (apiVersion == null) {
            apiVersion = SharedConstants.getCurrentVersion().id();
        }
        API_VERSION = apiVersion;
    }

    public static String getBukkitVersion() {
        return BUKKIT_VERSION;
    }

    public static String getCurrentApiVersion() {
        return API_VERSION;
    }
}
