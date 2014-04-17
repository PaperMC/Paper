package net.minecraft.server;

import java.lang.reflect.Type;

import net.minecraft.util.com.google.gson.JsonDeserializationContext;
import net.minecraft.util.com.google.gson.JsonDeserializer;
import net.minecraft.util.com.google.gson.JsonElement;
import net.minecraft.util.com.google.gson.JsonObject;
import net.minecraft.util.com.google.gson.JsonSerializationContext;
import net.minecraft.util.com.google.gson.JsonSerializer;

class JsonListEntrySerializer implements JsonDeserializer, JsonSerializer {

    final JsonList a;

    private JsonListEntrySerializer(JsonList jsonlist) {
        this.a = jsonlist;
    }

    public JsonElement a(JsonListEntry jsonlistentry, Type type, JsonSerializationContext jsonserializationcontext) {
        JsonObject jsonobject = new JsonObject();

        jsonlistentry.a(jsonobject);
        return jsonobject;
    }

    public JsonListEntry a(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) {
        if (jsonelement.isJsonObject()) {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            JsonListEntry jsonlistentry = this.a.a(jsonobject);

            return jsonlistentry;
        } else {
            return null;
        }
    }

    public JsonElement serialize(Object object, Type type, JsonSerializationContext jsonserializationcontext) {
        return this.a((JsonListEntry) object, type, jsonserializationcontext);
    }

    public Object deserialize(JsonElement jsonelement, Type type, JsonDeserializationContext jsondeserializationcontext) {
        return this.a(jsonelement, type, jsondeserializationcontext);
    }

    JsonListEntrySerializer(JsonList jsonlist, JsonListType jsonlisttype) {
        this(jsonlist);
    }
}
