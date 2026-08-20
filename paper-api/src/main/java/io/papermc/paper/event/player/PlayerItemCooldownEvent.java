package io.papermc.paper.event.player;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a player receives an item cooldown when using an item.
 *
 * @see PlayerItemGroupCooldownEvent for a more general event when applied to a group of items
 */
@NullMarked
public interface PlayerItemCooldownEvent extends PlayerItemGroupCooldownEvent {

    /**
     * Get the material of the item affected by the cooldown.
     *
     * @return material affected by the cooldown
     */
    Material getType();
}
