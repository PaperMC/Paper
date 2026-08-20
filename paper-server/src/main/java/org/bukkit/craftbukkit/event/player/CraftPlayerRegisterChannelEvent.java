package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

public class CraftPlayerRegisterChannelEvent extends CraftPlayerChannelEvent implements PlayerRegisterChannelEvent {

    public CraftPlayerRegisterChannelEvent(final Player player, final String channel) {
        super(player, channel);
    }
}
