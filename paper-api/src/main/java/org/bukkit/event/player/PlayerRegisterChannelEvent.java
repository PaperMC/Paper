package org.bukkit.event.player;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This is called immediately after a player registers for a plugin channel.
 */
public class PlayerRegisterChannelEvent extends PlayerChannelEvent {

    public PlayerRegisterChannelEvent(@NotNull final Player player, @NotNull final String channel) {
        super(player, channel);
    }
}
