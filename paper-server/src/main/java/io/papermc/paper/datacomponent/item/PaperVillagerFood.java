package io.papermc.paper.datacomponent.item;

import org.bukkit.craftbukkit.util.Handleable;

public record PaperVillagerFood(
    net.minecraft.world.food.VillagerFood impl
) implements VillagerFood, Handleable<net.minecraft.world.food.VillagerFood> {

    @Override
    public net.minecraft.world.food.VillagerFood getHandle() {
        return this.impl;
    }

    @Override
    public int nutrition() {
        return this.impl.nutrition();
    }
}
