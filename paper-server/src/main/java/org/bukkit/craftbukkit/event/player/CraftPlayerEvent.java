package org.bukkit.craftbukkit.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

public abstract class CraftPlayerEvent extends CraftEvent implements PlayerEvent {

    protected final Player player;

    protected CraftPlayerEvent(final Player player) {
        this.player = player;
    }

    protected CraftPlayerEvent(final boolean async, final Player player) {
        super(async);
        this.player = player;
    }

    protected CraftPlayerEvent(final ServerPlayer player) {
        this(player.getBukkitEntity());
    }

    protected CraftPlayerEvent(final boolean async, final ServerPlayer player) {
        this(async, player.getBukkitEntity());
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
}
