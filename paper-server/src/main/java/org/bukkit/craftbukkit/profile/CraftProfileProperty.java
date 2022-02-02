package org.bukkit.craftbukkit.profile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.bukkit.craftbukkit.configuration.ConfigSerializationUtil;

final class CraftProfileProperty {

    /**
     * Different JSON formatting styles to use for encoded property values.
     */
    public interface JsonFormatter {

        /**
         * A {@link JsonFormatter} that uses a compact formatting style.
         */
        public static final JsonFormatter COMPACT = new JsonFormatter() {

            private final Gson gson = new GsonBuilder().create();

            @Override
            public String format(JsonElement jsonElement) {
                return gson.toJson(jsonElement);
            }
        };

        public String format(JsonElement jsonElement);
    }

    private static final PublicKey PUBLIC_KEY;

    static {
        try {
            X509EncodedKeySpec spec = new X509EncodedKeySpec(IOUtils.toByteArray(YggdrasilMinecraftSessionService.class.getResourceAsStream("/yggdrasil_session_pubkey.der")));
            PUBLIC_KEY = KeyFactory.getInstance("RSA").generatePublic(spec);
        } catch (Exception e) {
            throw new Error("Could not find yggdrasil_session_pubkey.der! This indicates a bug.");
        }
    }

    public static boolean hasValidSignature(@Nonnull Property property) {
        return property.hasSignature() && property.isSignatureValid(PUBLIC_KEY);
    }

    @Nullable
    private static String decodeBase64(@Nonnull String encoded) {
        try {
            return new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null; // Invalid input
        }
    }

    @Nullable
    public static JsonObject decodePropertyValue(@Nonnull String encodedPropertyValue) {
        String json = decodeBase64(encodedPropertyValue);
        if (json == null) return null;
        try {
            JsonElement jsonElement = JsonParser.parseString(json);
            if (!jsonElement.isJsonObject()) return null;
            return jsonElement.getAsJsonObject();
        } catch (JsonParseException e) {
            return null; // Invalid input
        }
    }

    @Nonnull
    public static String encodePropertyValue(@Nonnull JsonObject propertyValue, @Nonnull JsonFormatter formatter) {
        String json = formatter.format(propertyValue);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    @Nonnull
    public static String toString(@Nonnull Property property) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("name=");
        builder.append(property.getName());
        builder.append(", value=");
        builder.append(property.getValue());
        builder.append(", signature=");
        builder.append(property.getSignature());
        builder.append("}");
        return builder.toString();
    }

    public static int hashCode(@Nonnull Property property) {
        int result = 1;
        result = 31 * result + Objects.hashCode(property.getName());
        result = 31 * result + Objects.hashCode(property.getValue());
        result = 31 * result + Objects.hashCode(property.getSignature());
        return result;
    }

    public static boolean equals(@Nullable Property property, @Nullable Property other) {
        if (property == null || other == null) return (property == other);
        if (!Objects.equals(property.getValue(), other.getValue())) return false;
        if (!Objects.equals(property.getName(), other.getName())) return false;
        if (!Objects.equals(property.getSignature(), other.getSignature())) return false;
        return true;
    }

    public static Map<String, Object> serialize(@Nonnull Property property) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("name", property.getName());
        map.put("value", property.getValue());
        if (property.hasSignature()) {
            map.put("signature", property.getSignature());
        }
        return map;
    }

    public static Property deserialize(@Nonnull Map<?, ?> map) {
        String name = ConfigSerializationUtil.getString(map, "name", false);
        String value = ConfigSerializationUtil.getString(map, "value", false);
        String signature = ConfigSerializationUtil.getString(map, "signature", true);
        return new Property(name, value, signature);
    }

    private CraftProfileProperty() {
    }
}
