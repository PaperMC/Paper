package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

public record PaperEnchantable(
    net.minecraft.world.item.enchantment.Enchantable impl
) implements Enchantable, Handleable<net.minecraft.world.item.enchantment.Enchantable> {

    @Override
    public net.minecraft.world.item.enchantment.Enchantable getHandle() {
        return this.impl;
    }

    @Override
    public int value() {
        return this.impl.value();
    }
}
