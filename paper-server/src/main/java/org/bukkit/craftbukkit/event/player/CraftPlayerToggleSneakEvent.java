package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class CraftPlayerToggleSneakEvent extends CraftPlayerEvent implements PlayerToggleSneakEvent {

    private final boolean sneaking;
    private boolean cancelled;

    public CraftPlayerToggleSneakEvent(final Player player, final boolean sneaking) {
        super(player);
        this.sneaking = sneaking;
    }

    public CraftPlayerToggleSneakEvent(final ServerPlayer player, final boolean sneaking) {
        this(player.getBukkitEntity(), sneaking);
    }

    @Override
    public boolean isSneaking() {
        return this.sneaking;
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
        return PlayerToggleSneakEvent.getHandlerList();
    }
}
