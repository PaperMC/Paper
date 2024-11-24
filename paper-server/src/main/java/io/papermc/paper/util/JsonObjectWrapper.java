package io.papermc.paper.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record JsonObjectWrapper(JsonObject element) implements ConfigurationSerializable {

    private static final String KEY = "value";
    private static final Gson GSON = new Gson();

    static {
        ConfigurationSerialization.registerClass(JsonObjectWrapper.class);
    }

    public JsonObjectWrapper(final JsonElement element) {
        this(element.getAsJsonObject());
    }

    public JsonObjectWrapper(final Map<String, Object> input) {
        this(JsonParser.parseString((String) input.get(KEY)).getAsJsonObject());
    }

    @Override
    public Map<String, Object> serialize() {
        return Map.of(KEY, GSON.toJson(this.element));
    }
}
