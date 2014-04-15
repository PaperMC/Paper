package net.minecraft.server;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;

public class GameProfileBanEntry extends ExpirableListEntry<GameProfile> {

    public GameProfileBanEntry(GameProfile gameprofile) {
        this(gameprofile, (Date) null, (String) null, (Date) null, (String) null);
    }

    public GameProfileBanEntry(GameProfile gameprofile, @Nullable Date date, @Nullable String s, @Nullable Date date1, @Nullable String s1) {
        super(gameprofile, date, s, date1, s1);
    }

    public GameProfileBanEntry(JsonObject jsonobject) {
        super(b(jsonobject), jsonobject);
    }

    @Override
    protected void a(JsonObject jsonobject) {
        if (this.getKey() != null) {
            jsonobject.addProperty("uuid", ((GameProfile) this.getKey()).getId() == null ? "" : ((GameProfile) this.getKey()).getId().toString());
            jsonobject.addProperty("name", ((GameProfile) this.getKey()).getName());
            super.a(jsonobject);
        }
    }

    @Override
    public IChatBaseComponent e() {
        GameProfile gameprofile = (GameProfile) this.getKey();

        return new ChatComponentText(gameprofile.getName() != null ? gameprofile.getName() : Objects.toString(gameprofile.getId(), "(Unknown)"));
    }

    private static GameProfile b(JsonObject jsonobject) {
        // Spigot start
        // this whole method has to be reworked to account for the fact Bukkit only accepts UUID bans and gives no way for usernames to be stored!
        UUID uuid = null;
        String name = null;
        if (jsonobject.has("uuid")) {
            String s = jsonobject.get("uuid").getAsString();

            try {
                uuid = UUID.fromString(s);
            } catch (Throwable throwable) {
            }

        }
        if ( jsonobject.has("name"))
        {
            name = jsonobject.get("name").getAsString();
        }
        if ( uuid != null || name != null )
        {
            return new GameProfile( uuid, name );
        } else {
            return null;
        }
        // Spigot End
    }
}
