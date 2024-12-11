package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

public record PaperOminousBottleAmplifier(
    net.minecraft.world.item.component.OminousBottleAmplifier impl
) implements OminousBottleAmplifier, Handleable<net.minecraft.world.item.component.OminousBottleAmplifier> {

    @Override
    public net.minecraft.world.item.component.OminousBottleAmplifier getHandle() {
        return this.impl;
    }

    @Override
    public int amplifier() {
        return this.impl.value();
    }
}
