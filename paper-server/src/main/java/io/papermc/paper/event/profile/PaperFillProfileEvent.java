package io.papermc.paper.event.profile;

import com.destroystokyo.paper.event.profile.FillProfileEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Set;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperFillProfileEvent extends CraftEvent implements FillProfileEvent {

    private final PlayerProfile profile;

    public PaperFillProfileEvent(final PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public Set<ProfileProperty> getProperties() {
        return this.profile.getProperties();
    }

    @Override
    public HandlerList getHandlers() {
        return FillProfileEvent.getHandlerList();
    }
}
