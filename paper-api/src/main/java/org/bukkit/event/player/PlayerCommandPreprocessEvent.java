package org.bukkit.event.player;

import org.bukkit.entity.Player;

/**
 * Called early in the command handling process. This event is only
 * for very exceptional cases and you should not normally use it.
 */
public class PlayerCommandPreprocessEvent extends PlayerChatEvent {
    public PlayerCommandPreprocessEvent(final Player player, final String message) {
        super(Type.PLAYER_COMMAND_PREPROCESS, player, message);
    }
}
