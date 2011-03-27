package org.bukkit.event.player;

import org.bukkit.entity.Player;

public class PlayerQuitEvent extends PlayerEvent {
    public PlayerQuitEvent(Player who) {
        super(Type.PLAYER_QUIT, who);
    }
}
