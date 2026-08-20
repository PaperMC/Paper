package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerShieldDisableEvent extends CraftPlayerEvent implements PlayerShieldDisableEvent {

    private final Entity damager;
    private int cooldown;

    private boolean cancelled;

    public PaperPlayerShieldDisableEvent(final Player player, final Entity damager, final int cooldown) {
        super(player);
        this.damager = damager;
        this.cooldown = cooldown;
    }

    @Override
    public Entity getDamager() {
        return this.damager;
    }

    @Override
    public int getCooldown() {
        return this.cooldown;
    }

    @Override
    public void setCooldown(final int cooldown) {
        Preconditions.checkArgument(cooldown >= 0, "The cooldown has to be equal to or greater than 0!");
        this.cooldown = cooldown;
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
        return PlayerShieldDisableEvent.getHandlerList();
    }
}
