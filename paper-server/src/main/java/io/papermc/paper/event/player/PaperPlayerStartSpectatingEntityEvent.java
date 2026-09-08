package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerStartSpectatingEntityEvent extends CraftPlayerEvent implements PlayerStartSpectatingEntityEvent {

    private final Entity currentSpectatorTarget;
    private final Entity newSpectatorTarget;

    private boolean cancelled;

    public PaperPlayerStartSpectatingEntityEvent(final Player player, final Entity currentSpectatorTarget, final Entity newSpectatorTarget) {
        super(player);
        this.currentSpectatorTarget = currentSpectatorTarget;
        this.newSpectatorTarget = newSpectatorTarget;
    }

    public PaperPlayerStartSpectatingEntityEvent(
        final ServerPlayer player,
        final net.minecraft.world.entity.Entity oldCamera,
        final net.minecraft.world.entity.Entity newCamera
    ) {
        this(player.getBukkitEntity(), oldCamera.getBukkitEntity(), newCamera.getBukkitEntity());
    }

    @Override
    public Entity getCurrentSpectatorTarget() {
        return this.currentSpectatorTarget;
    }

    @Override
    public Entity getNewSpectatorTarget() {
        return this.newSpectatorTarget;
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
        return PlayerStartSpectatingEntityEvent.getHandlerList();
    }
}

