package net.minecraft.server;

import com.google.gson.JsonObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;

public abstract class ExpirableListEntry<T> extends JsonListEntry<T> {

    public static final SimpleDateFormat a = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    protected final Date b;
    protected final String c;
    protected final Date d;
    protected final String e;

    public ExpirableListEntry(T t0, @Nullable Date date, @Nullable String s, @Nullable Date date1, @Nullable String s1) {
        super(t0);
        this.b = date == null ? new Date() : date;
        this.c = s == null ? "(Unknown)" : s;
        this.d = date1;
        this.e = s1 == null ? "Banned by an operator." : s1;
    }

    protected ExpirableListEntry(T t0, JsonObject jsonobject) {
        super(checkExpiry(t0, jsonobject));

        Date date;

        try {
            date = jsonobject.has("created") ? ExpirableListEntry.a.parse(jsonobject.get("created").getAsString()) : new Date();
        } catch (ParseException parseexception) {
            date = new Date();
        }

        this.b = date;
        this.c = jsonobject.has("source") ? jsonobject.get("source").getAsString() : "(Unknown)";

        Date date1;

        try {
            date1 = jsonobject.has("expires") ? ExpirableListEntry.a.parse(jsonobject.get("expires").getAsString()) : null;
        } catch (ParseException parseexception1) {
            date1 = null;
        }

        this.d = date1;
        this.e = jsonobject.has("reason") ? jsonobject.get("reason").getAsString() : "Banned by an operator.";
    }

    public String getSource() {
        return this.c;
    }

    public Date getExpires() {
        return this.d;
    }

    public String getReason() {
        return this.e;
    }

    public abstract IChatBaseComponent e();

    @Override
    boolean hasExpired() {
        return this.d == null ? false : this.d.before(new Date());
    }

    @Override
    protected void a(JsonObject jsonobject) {
        jsonobject.addProperty("created", ExpirableListEntry.a.format(this.b));
        jsonobject.addProperty("source", this.c);
        jsonobject.addProperty("expires", this.d == null ? "forever" : ExpirableListEntry.a.format(this.d));
        jsonobject.addProperty("reason", this.e);
    }

    // CraftBukkit start
    public Date getCreated() {
        return this.b;
    }

    private static <T> T checkExpiry(T object, JsonObject jsonobject) {
        Date expires = null;

        try {
            expires = jsonobject.has("expires") ? a.parse(jsonobject.get("expires").getAsString()) : null;
        } catch (ParseException ex) {
            // Guess we don't have a date
        }

        if (expires == null || expires.after(new Date())) {
            return object;
        } else {
            return null;
        }
    }
    // CraftBukkit end
}
