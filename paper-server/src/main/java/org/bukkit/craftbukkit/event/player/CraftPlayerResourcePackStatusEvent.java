package org.bukkit.craftbukkit.event.player;

import java.util.UUID;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class CraftPlayerResourcePackStatusEvent extends CraftPlayerEvent implements PlayerResourcePackStatusEvent {

    private final UUID id;
    private final Status status;

    public CraftPlayerResourcePackStatusEvent(final Player player, final UUID id, final Status status) {
        super(player);
        this.id = id;
        this.status = status;
    }

    public CraftPlayerResourcePackStatusEvent(final ServerPlayer player, final UUID id, final Status status) {
        this(player.getBukkitEntity(), id, status);
    }

    @Override
    public UUID getID() {
        return this.id;
    }

    @Override
    public Status getStatus() {
        return this.status;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerResourcePackStatusEvent.getHandlerList();
    }
}
