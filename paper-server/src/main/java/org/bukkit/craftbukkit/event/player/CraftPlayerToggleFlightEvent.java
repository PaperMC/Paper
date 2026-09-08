package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class CraftPlayerToggleFlightEvent extends CraftPlayerEvent implements PlayerToggleFlightEvent {

    private final boolean flying;
    private boolean cancelled;

    public CraftPlayerToggleFlightEvent(final Player player, final boolean flying) {
        super(player);
        this.flying = flying;
    }

    public CraftPlayerToggleFlightEvent(final ServerPlayer player, final boolean flying) {
        this(player.getBukkitEntity(), flying);
    }

    @Override
    public boolean isFlying() {
        return this.flying;
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
        return PlayerToggleFlightEvent.getHandlerList();
    }
}
