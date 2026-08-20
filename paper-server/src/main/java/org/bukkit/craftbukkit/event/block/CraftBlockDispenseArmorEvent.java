package org.bukkit.craftbukkit.event.block;

import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockDispenseArmorEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CraftBlockDispenseArmorEvent extends CraftBlockDispenseEvent implements BlockDispenseArmorEvent {

    private final LivingEntity target;

    public CraftBlockDispenseArmorEvent(final Block block, final ItemStack dispensed, final LivingEntity target) {
        super(block, dispensed, new Vector(0, 0, 0));
        this.target = target;
    }

    public LivingEntity getTargetEntity() {
        return this.target;
    }
}
