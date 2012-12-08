package org.bukkit.event.inventory;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockExpEvent;

/**
 * This event is called when a player takes items out of the furnace
 */
public class FurnaceExtractEvent extends BlockExpEvent {
    private final Player player;
    private final Material itemType;
    private final int itemAmount;

    public FurnaceExtractEvent(Player player, Block block, Material itemType, int itemAmount, int exp) {
        super(block, exp);
        this.player = player;
        this.itemType = itemType;
        this.itemAmount = itemAmount;
    }

    /**
     * Get the player that triggered the event
     *
     * @return the relevant player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the Material of the item being retrieved
     *
     * @return the material of the item
     */
    public Material getItemType() {
        return itemType;
    }

    /**
     * Get the item count being retrieved
     *
     * @return the amount of the item
     */
    public int getItemAmount() {
        return itemAmount;
    }
}
