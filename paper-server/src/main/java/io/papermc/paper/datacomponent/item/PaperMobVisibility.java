package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.EntityType;

public record PaperMobVisibility(
    net.minecraft.world.item.component.MobVisibility impl
) implements MobVisibility, Handleable<net.minecraft.world.item.component.MobVisibility> {

    @Override
    public net.minecraft.world.item.component.MobVisibility getHandle() {
        return this.impl;
    }

    @Override
    public RegistryKeySet<EntityType> targetingEntityTypes() {
        return PaperRegistrySets.convertToApi(RegistryKey.ENTITY_TYPE, this.impl.targetingEntityTypes());
    }

    @Override
    public float visibility() {
        return this.impl.visibility();
    }
}
