package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerPickupExperienceEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerPickupExperienceEvent extends CraftPlayerEvent implements PlayerPickupExperienceEvent {

    private final ExperienceOrb experienceOrb;
    private boolean cancelled;

    public PaperPlayerPickupExperienceEvent(final Player player, final ExperienceOrb experienceOrb) {
        super(player);
        this.experienceOrb = experienceOrb;
    }

    @Override
    public ExperienceOrb getExperienceOrb() {
        return this.experienceOrb;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPickupExperienceEvent.getHandlerList();
    }
}
