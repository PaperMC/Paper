package org.bukkit.craftbukkit.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.ServicesKeyType;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.bukkit.craftbukkit.configuration.ConfigSerializationUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CraftProfileProperty {

    /**
     * Different JSON formatting styles to use for encoded property values.
     */
    public interface JsonFormatter {

        /**
         * A {@link JsonFormatter} that uses a compact formatting style.
         */
        JsonFormatter COMPACT = new JsonFormatter() {

            private final Gson gson = new GsonBuilder().create();

            @Override
            public String format(JsonElement jsonElement) {
                return this.gson.toJson(jsonElement);
            }
        };

        String format(JsonElement jsonElement);
    }

    private static final ServicesKeySet PUBLIC_KEYS;

    static {
        try {
            PUBLIC_KEYS = new YggdrasilAuthenticationService(Proxy.NO_PROXY).getServicesKeySet();
        } catch (Exception e) {
            throw new Error("Could not load yggdrasil_session_pubkey.der! This indicates a bug.");
        }
    }

    public static boolean hasValidSignature(Property property) {
        return property.hasSignature() && CraftProfileProperty.PUBLIC_KEYS.keys(ServicesKeyType.PROFILE_PROPERTY).stream().anyMatch((key) -> key.validateProperty(property));
    }

    private static @Nullable String decodeBase64(String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null; // Invalid input
        }
    }

    public static @Nullable JsonObject decodePropertyValue(String encodedPropertyValue) {
        String json = CraftProfileProperty.decodeBase64(encodedPropertyValue);
        if (json == null) return null;
        try {
            JsonElement jsonElement = JsonParser.parseString(json);
            if (!jsonElement.isJsonObject()) return null;
            return jsonElement.getAsJsonObject();
        } catch (JsonParseException e) {
            return null; // Invalid input
        }
    }

    public static String encodePropertyValue(JsonObject propertyValue, JsonFormatter formatter) {
        String json = formatter.format(propertyValue);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    public static String toString(Property property) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("name=");
        builder.append(property.name());
        builder.append(", value=");
        builder.append(property.value());
        builder.append(", signature=");
        builder.append(property.signature());
        builder.append("}");
        return builder.toString();
    }

    public static int hashCode(Property property) {
        int result = 1;
        result = 31 * result + Objects.hashCode(property.name());
        result = 31 * result + Objects.hashCode(property.value());
        result = 31 * result + Objects.hashCode(property.signature());
        return result;
    }

    public static boolean equals(@Nullable Property property, @Nullable Property other) {
        if (property == null || other == null) return (property == other);
        if (!Objects.equals(property.value(), other.value())) return false;
        if (!Objects.equals(property.name(), other.name())) return false;
        if (!Objects.equals(property.signature(), other.signature())) return false;
        return true;
    }

    public static Map<String, Object> serialize(Property property) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", property.name());
        map.put("value", property.value());
        if (property.hasSignature()) {
            map.put("signature", property.signature());
        }
        return map;
    }

    public static Property deserialize(Map<?, ?> map) {
        String name = ConfigSerializationUtil.getString(map, "name", false);
        String value = ConfigSerializationUtil.getString(map, "value", false);
        String signature = ConfigSerializationUtil.getString(map, "signature", true);
        return new Property(name, value, signature);
    }

    private CraftProfileProperty() {
    }
}
