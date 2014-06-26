package net.minecraft.server;

import net.minecraft.util.com.google.gson.JsonObject;

public class JsonListEntry {

    private final Object a;

    public JsonListEntry(Object object) {
        this.a = object;
    }

    protected JsonListEntry(Object object, JsonObject jsonobject) {
        this.a = object;
    }

    public Object getKey() { // CraftBukkit -> package private -> public
        return this.a;
    }

    boolean hasExpired() {
        return false;
    }

    protected void a(JsonObject jsonobject) {}
}
