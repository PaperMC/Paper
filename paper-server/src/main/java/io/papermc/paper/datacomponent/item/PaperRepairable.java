package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.ItemType;

public record PaperRepairable(
    net.minecraft.world.item.enchantment.Repairable impl
) implements Repairable, Handleable<net.minecraft.world.item.enchantment.Repairable> {

    @Override
    public net.minecraft.world.item.enchantment.Repairable getHandle() {
        return this.impl;
    }

    @Override
    public RegistryKeySet<ItemType> types() {
        return PaperRegistrySets.convertToApi(RegistryKey.ITEM, this.impl.items());
    }
}
