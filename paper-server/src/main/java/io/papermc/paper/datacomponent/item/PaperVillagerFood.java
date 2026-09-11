package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;
import org.checkerframework.checker.index.qual.Positive;

public record PaperVillagerFood(
    net.minecraft.world.food.VillagerFood impl
) implements VillagerFood, Handleable<net.minecraft.world.food.VillagerFood> {

    @Override
    public net.minecraft.world.food.VillagerFood getHandle() {
        return this.impl;
    }

    @Override
    public @Positive int nutrition() {
        return this.impl.nutrition();
    }
}
