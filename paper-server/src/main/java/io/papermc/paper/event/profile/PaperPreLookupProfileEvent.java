package io.papermc.paper.event.profile;

import com.destroystokyo.paper.event.profile.PreLookupProfileEvent;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPreLookupProfileEvent extends CraftEvent implements PreLookupProfileEvent {

    private final String name;

    private @Nullable UUID uuid;
    private Set<ProfileProperty> properties = new HashSet<>();

    public PaperPreLookupProfileEvent(final String name) {
        super(!Bukkit.isPrimaryThread());
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public @Nullable UUID getUUID() {
        return this.uuid;
    }

    @Override
    public void setUUID(final @Nullable UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.21.9")
    public Set<ProfileProperty> getProfileProperties() {
        return this.properties;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.21.9")
    public void setProfileProperties(final Set<ProfileProperty> properties) {
        this.properties = new HashSet<>();
        this.properties.addAll(properties);
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.21.9")
    public void addProfileProperties(final Set<ProfileProperty> properties) {
        this.properties.addAll(properties);
    }

    @Override
    public HandlerList getHandlers() {
        return PreLookupProfileEvent.getHandlerList();
    }
}
