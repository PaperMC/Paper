package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.inventory.ItemStack;

public class CraftFurnaceExtractEvent extends CraftBlockExpEvent implements FurnaceExtractEvent {

    private final Player player;
    private final ItemStack item;
    private final int itemAmount;

    public CraftFurnaceExtractEvent(final Player player, final Block block, final ItemStack item, final int itemAmount, final int exp) {
        super(block, exp);
        this.player = player;
        this.item = item;
        this.itemAmount = itemAmount;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getItemStack() {
        return this.item.clone();
    }

    @Override
    public int getItemAmount() {
        return this.itemAmount;
    }
}
