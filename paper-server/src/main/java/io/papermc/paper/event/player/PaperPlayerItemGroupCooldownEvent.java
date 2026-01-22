package io.papermc.paper.event.player;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerItemGroupCooldownEvent extends CraftPlayerEvent implements PlayerItemGroupCooldownEvent {

    private final NamespacedKey cooldownGroup;
    private int cooldown;

    private boolean cancelled;

    public PaperPlayerItemGroupCooldownEvent(final Player player, final NamespacedKey cooldownGroup, final int cooldown) {
        super(player);
        this.cooldownGroup = cooldownGroup;
        this.cooldown = cooldown;
    }

    @Override
    public NamespacedKey getCooldownGroup() {
        return this.cooldownGroup;
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
        return PlayerItemGroupCooldownEvent.getHandlerList();
    }
}
