package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerExpCooldownChangeEvent;

public class CraftPlayerExpCooldownChangeEvent extends CraftPlayerEvent implements PlayerExpCooldownChangeEvent {

    private final ChangeReason reason;
    private int newCooldown;

    public CraftPlayerExpCooldownChangeEvent(final Player player, final int newCooldown, final ChangeReason reason) {
        super(player);
        this.newCooldown = newCooldown;
        this.reason = reason;
    }

    public CraftPlayerExpCooldownChangeEvent(final ServerPlayer player, final int newCooldown, final ChangeReason reason) {
        this(player.getBukkitEntity(), newCooldown, reason);
    }

    @Override
    public ChangeReason getReason() {
        return this.reason;
    }

    @Override
    public int getNewCooldown() {
        return this.newCooldown;
    }

    @Override
    public void setNewCooldown(final int newCooldown) {
        this.newCooldown = newCooldown;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerExpCooldownChangeEvent.getHandlerList();
    }
}
