package org.bukkit.craftbukkit.event.player;

import org.bukkit.craftbukkit.event.CraftEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEventNew;

public abstract class CraftPlayerEvent extends CraftEvent implements PlayerEventNew {

    protected final Player player;

    protected CraftPlayerEvent(final Player player) {
        this.player = player;
    }

    protected CraftPlayerEvent(final Player player, boolean async) {
        super(async);
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }
}
