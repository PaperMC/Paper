package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSprintEvent;

public class CraftPlayerToggleSprintEvent extends CraftPlayerEvent implements PlayerToggleSprintEvent {

    private final boolean sprinting;
    private boolean cancelled;

    public CraftPlayerToggleSprintEvent(final Player player, final boolean sprinting) {
        super(player);
        this.sprinting = sprinting;
    }

    public CraftPlayerToggleSprintEvent(final ServerPlayer player, final boolean sprinting) {
        this(player.getBukkitEntity(), sprinting);
    }

    @Override
    public boolean isSprinting() {
        return this.sprinting;
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
        return PlayerToggleSprintEvent.getHandlerList();
    }
}
