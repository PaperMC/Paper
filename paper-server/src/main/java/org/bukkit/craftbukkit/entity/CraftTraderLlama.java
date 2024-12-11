package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.TraderLlama;

public class CraftTraderLlama extends CraftLlama implements TraderLlama {

    public CraftTraderLlama(CraftServer server, net.minecraft.world.entity.animal.horse.TraderLlama entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.animal.horse.TraderLlama getHandle() {
        return (net.minecraft.world.entity.animal.horse.TraderLlama) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTraderLlama";
    }
}
