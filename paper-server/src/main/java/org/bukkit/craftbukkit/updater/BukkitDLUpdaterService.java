package org.bukkit.craftbukkit.updater;

import com.google.gson.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitDLUpdaterService {
    private static final String API_PREFIX = "/api/1.0/downloads/projects/craftbukkit/view/";
    private static final DateDeserializer dateDeserializer = new DateDeserializer();
    private final String host;

    public BukkitDLUpdaterService(String host) {
        this.host = host;
    }

    public ArtifactDetails getArtifact(String slug) {
        try {
            return fetchArtifact(slug);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BukkitDLUpdaterService.class.getName()).log(Level.WARNING, "Could not get Artifact details for the auto-updater", ex);
        } catch (IOException ex) {
            Logger.getLogger(BukkitDLUpdaterService.class.getName()).log(Level.WARNING, "Could not get Artifact details for the auto-updater", ex);
        }

        return null;
    }

    public ArtifactDetails fetchArtifact(String slug) throws UnsupportedEncodingException, IOException {
        URL url = new URL("http", host, API_PREFIX + slug);
        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(url.openStream());
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, dateDeserializer).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
            ArtifactDetails fromJson = gson.fromJson(reader, ArtifactDetails.class);

            return fromJson;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    static class DateDeserializer implements JsonDeserializer<Date> {
        private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public Date deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            try {
                return format.parse(je.getAsString());
            } catch (ParseException ex) {
                throw new JsonParseException("Date is not formatted correctly", ex);
            }
        }
    }
}
