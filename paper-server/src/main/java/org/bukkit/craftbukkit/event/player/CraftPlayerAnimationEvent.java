package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;

public class CraftPlayerAnimationEvent extends CraftPlayerEvent implements PlayerAnimationEvent {

    private final PlayerAnimationType animationType;
    private boolean cancelled;

    public CraftPlayerAnimationEvent(final Player player, final PlayerAnimationType playerAnimationType) {
        super(player);
        this.animationType = playerAnimationType;
    }

    @Override
    public PlayerAnimationType getAnimationType() {
        return this.animationType;
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
        return PlayerAnimationEvent.getHandlerList();
    }
}
