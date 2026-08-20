package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class CraftPlayerPickupArrowEvent extends CraftPlayerPickupItemEvent implements PlayerPickupArrowEvent {

    private final AbstractArrow arrow;

    public CraftPlayerPickupArrowEvent(final Player player, final Item item, final AbstractArrow arrow) {
        super(player, item, 0);
        this.arrow = arrow;
    }

    @Override
    public AbstractArrow getArrow() {
        return this.arrow;
    }
}
