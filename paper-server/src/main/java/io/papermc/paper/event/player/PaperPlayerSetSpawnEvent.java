package io.papermc.paper.event.player;

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

public class PaperPlayerSetSpawnEvent extends CraftPlayerEvent implements PlayerSetSpawnEvent {

    private final Cause cause;
    private @Nullable Location location;
    private boolean forced;
    private boolean notifyPlayer;
    private @Nullable Component notification;

    private boolean cancelled;

    public PaperPlayerSetSpawnEvent(final Player player, final Cause cause, final @Nullable Location location, final boolean forced, final boolean notifyPlayer, final @Nullable Component notification) {
        super(player);
        this.cause = cause;
        this.location = location;
        this.forced = forced;
        this.notifyPlayer = notifyPlayer;
        this.notification = notification;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    @Override
    public @Nullable Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(final @Nullable Location location) {
        this.location = location != null ? location.clone() : null;
    }

    @Override
    public boolean isForced() {
        return this.forced;
    }

    @Override
    public void setForced(final boolean forced) {
        this.forced = forced;
    }

    @Override
    public boolean willNotifyPlayer() {
        return this.notifyPlayer;
    }

    @Override
    public void setNotifyPlayer(final boolean notifyPlayer) {
        this.notifyPlayer = notifyPlayer;
    }

    @Override
    public @Nullable Component getNotification() {
        return this.notification;
    }

    @Override
    public void setNotification(final @Nullable Component notification) {
        this.notification = notification;
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
        return PlayerSetSpawnEvent.getHandlerList();
    }
}
