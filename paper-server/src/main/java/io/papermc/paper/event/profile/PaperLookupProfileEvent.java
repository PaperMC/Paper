package io.papermc.paper.event.profile;

import com.destroystokyo.paper.event.profile.LookupProfileEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperLookupProfileEvent extends CraftEvent implements LookupProfileEvent {

    private final PlayerProfile profile;

    public PaperLookupProfileEvent(final PlayerProfile profile) {
        super(!Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    @Override
    public String getName() {
        return Objects.requireNonNull(this.profile.getName(), "profile name");
    }

    @Override
    public UUID getId() {
        return Objects.requireNonNull(this.profile.getId(), "profile id");
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.21.9")
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public HandlerList getHandlers() {
        return LookupProfileEvent.getHandlerList();
    }
}
