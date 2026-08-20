package org.bukkit.craftbukkit.event.player;

import org.bukkit.Input;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInputEvent;

public class CraftPlayerInputEvent extends CraftPlayerEvent implements PlayerInputEvent {

    private final Input input;

    public CraftPlayerInputEvent(final Player player, final Input input) {
        super(player);
        this.input = input;
    }

    @Override
    public Input getInput() {
        return this.input;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerInputEvent.getHandlerList();
    }
}
