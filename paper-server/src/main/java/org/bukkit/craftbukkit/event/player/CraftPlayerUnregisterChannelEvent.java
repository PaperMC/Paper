package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;

public class CraftPlayerUnregisterChannelEvent extends CraftPlayerChannelEvent implements PlayerUnregisterChannelEvent {

    public CraftPlayerUnregisterChannelEvent(final Player player, final String channel) {
        super(player, channel);
    }
}
