package org.bukkit.craftbukkit.event.block;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockDispenseArmorEvent;

public class CraftBlockDispenseArmorEvent extends CraftBlockDispenseEvent implements BlockDispenseArmorEvent {

    private final LivingEntity target;

    public CraftBlockDispenseArmorEvent(final BlockSource pointer, final ItemStack dispensed, final net.minecraft.world.entity.LivingEntity target) {
        super(pointer, dispensed, Vec3.ZERO);
        this.target = target.getBukkitEntity();
    }

    @Override
    public LivingEntity getTargetEntity() {
        return this.target;
    }
}
