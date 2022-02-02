package org.bukkit.craftbukkit.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class JsonHelper {

    @Nullable
    public static JsonObject getObjectOrNull(@Nonnull JsonObject parent, @Nonnull String key) {
        JsonElement element = parent.get(key);
        return (element instanceof JsonObject) ? (JsonObject) element : null;
    }

    @Nonnull
    public static JsonObject getOrCreateObject(@Nonnull JsonObject parent, @Nonnull String key) {
        JsonObject jsonObject = getObjectOrNull(parent, key);
        if (jsonObject == null) {
            jsonObject = new JsonObject();
            parent.add(key, jsonObject);
        }
        return jsonObject;
    }

    @Nullable
    public static JsonPrimitive getPrimitiveOrNull(@Nonnull JsonObject parent, @Nonnull String key) {
        JsonElement element = parent.get(key);
        return (element instanceof JsonPrimitive) ? (JsonPrimitive) element : null;
    }

    @Nullable
    public static String getStringOrNull(JsonObject parent, String key) {
        JsonPrimitive primitive = getPrimitiveOrNull(parent, key);
        return (primitive != null) ? primitive.getAsString() : null;
    }

    public static void setOrRemove(@Nonnull JsonObject parent, @Nonnull String key, @Nullable JsonElement value) {
        if (value == null) {
            parent.remove(key);
        } else {
            parent.add(key, value);
        }
    }

    private JsonHelper() {
    }
}
