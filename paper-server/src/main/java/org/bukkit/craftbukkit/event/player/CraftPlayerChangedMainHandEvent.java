package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.inventory.MainHand;

public class CraftPlayerChangedMainHandEvent extends CraftPlayerEvent implements PlayerChangedMainHandEvent {

    private final MainHand newMainHand;

    public CraftPlayerChangedMainHandEvent(final Player player, final MainHand newMainHand) {
        super(player);
        this.newMainHand = newMainHand;
    }

    @Override
    public MainHand getMainHand() {
        return this.newMainHand == MainHand.LEFT ? MainHand.RIGHT : MainHand.LEFT;
    }

    @Override
    public MainHand getNewMainHand() {
        return this.newMainHand;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerChangedMainHandEvent.getHandlerList();
    }
}
