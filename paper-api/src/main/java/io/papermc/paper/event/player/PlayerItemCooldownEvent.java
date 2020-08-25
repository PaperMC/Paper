package io.papermc.paper.event.player;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired when a player receives an item cooldown when using an item.
 *
 * @see PlayerItemGroupCooldownEvent for a more general event when applied to a group of items
 */
@NullMarked
public class PlayerItemCooldownEvent extends PlayerItemGroupCooldownEvent {

    private final Material type;

    @ApiStatus.Internal
    public PlayerItemCooldownEvent(final Player player, final Material type, final NamespacedKey cooldownGroup, final int cooldown) {
        super(player, cooldownGroup, cooldown);
        this.type = type;
    }

    /**
     * Get the material of the item affected by the cooldown.
     *
     * @return material affected by the cooldown
     */
    public Material getType() {
        return this.type;
    }
}
