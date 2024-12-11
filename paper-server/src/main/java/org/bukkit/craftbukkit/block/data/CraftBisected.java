package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Bisected;

public class CraftBisected extends CraftBlockData implements Bisected {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HALF = getEnum("half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return this.get(CraftBisected.HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        this.set(CraftBisected.HALF, half);
    }
}
