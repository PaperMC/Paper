package io.papermc.paper.event.profile;

import com.destroystokyo.paper.event.profile.PreFillProfileEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import java.util.Collection;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.event.HandlerList;

public class PaperPreFillProfileEvent extends CraftEvent implements PreFillProfileEvent {

    private final PlayerProfile profile;

    public PaperPreFillProfileEvent(final PlayerProfile profile) {
        super(!org.bukkit.Bukkit.isPrimaryThread());
        this.profile = profile;
    }

    @Override
    public PlayerProfile getPlayerProfile() {
        return this.profile;
    }

    @Override
    public void setProperties(final Collection<ProfileProperty> properties) {
        this.profile.setProperties(properties);
    }

    @Override
    public HandlerList getHandlers() {
        return PreFillProfileEvent.getHandlerList();
    }
}
