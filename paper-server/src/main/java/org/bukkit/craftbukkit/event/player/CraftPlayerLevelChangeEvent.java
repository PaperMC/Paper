package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class CraftPlayerLevelChangeEvent extends CraftPlayerEvent implements PlayerLevelChangeEvent {

    private final int oldLevel;
    private final int newLevel;

    public CraftPlayerLevelChangeEvent(final Player player, final int oldLevel, final int newLevel) {
        super(player);
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    @Override
    public int getOldLevel() {
        return this.oldLevel;
    }

    @Override
    public int getNewLevel() {
        return this.newLevel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerLevelChangeEvent.getHandlerList();
    }
}
