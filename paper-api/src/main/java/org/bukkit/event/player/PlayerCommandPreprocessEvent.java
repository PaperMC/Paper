package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerCommandPreprocessEvent extends PlayerChatEvent {
    public PlayerCommandPreprocessEvent(final Player player, final String message) {
        super(Type.PLAYER_COMMAND_PREPROCESS, player, message);
    }
}
