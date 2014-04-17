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

    Object f() {
        return this.a;
    }

    boolean e() {
        return false;
    }

    protected void a(JsonObject jsonobject) {}
}
