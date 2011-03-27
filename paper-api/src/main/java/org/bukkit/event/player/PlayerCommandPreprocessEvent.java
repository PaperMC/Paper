package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerCommandPreprocessEvent extends PlayerChatEvent {
    public PlayerCommandPreprocessEvent(Player player, String message) {
        super(player, message);
    }
}
