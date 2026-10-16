package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.damage.DamageType;

public record PaperDamageResistant(
    net.minecraft.world.item.component.DamageResistant impl
) implements DamageResistant, Handleable<net.minecraft.world.item.component.DamageResistant> {

    @Override
    public net.minecraft.world.item.component.DamageResistant getHandle() {
        return this.impl;
    }

    @Override
    public RegistryKeySet<DamageType> types() {
        return PaperRegistrySets.convertToApi(RegistryKey.DAMAGE_TYPE, this.impl.types());
    }
}
