package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.tag.TagKey;
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
    public TagKey<DamageType> types() {
        return PaperRegistries.fromNms(this.impl.types());
    }
}
