package org.bukkit.craftbukkit.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import org.apache.commons.io.IOUtils;

public class MojangNameLookup {
    private static final Logger logger = LogManager.getFormatterLogger(MojangNameLookup.class);

    public static String lookupName(UUID id) {
        if (id == null) {
            return null;
        }

        InputStream inputStream = null;
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.setUseCaches(false);
            inputStream = connection.getInputStream();
            String result = IOUtils.toString(inputStream, Charsets.UTF_8);
            Gson gson = new Gson();
            Response response = gson.fromJson(result, Response.class);
            if (response == null || response.name == null) {
                logger.warn("Failed to lookup name from UUID");
                return null;
            }

            if (response.cause != null && response.cause.length() > 0) {
                logger.warn("Failed to lookup name from UUID: %s", response.errorMessage);
                return null;
            }

            return response.name;
        } catch (MalformedURLException ex) {
            logger.warn("Malformed URL in UUID lookup");
            return null;
        } catch (IOException ex) {
            IOUtils.closeQuietly(inputStream);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return null;
    }

    private class Response {
        String errorMessage;
        String cause;
        String name;
    }
}
