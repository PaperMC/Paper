package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Bamboo;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftBamboo extends CraftBlockData implements Bamboo {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> LEAVES = getEnum("leaves");

    @Override
    public org.bukkit.block.data.type.Bamboo.Leaves getLeaves() {
        return this.get(CraftBamboo.LEAVES, org.bukkit.block.data.type.Bamboo.Leaves.class);
    }

    @Override
    public void setLeaves(org.bukkit.block.data.type.Bamboo.Leaves leaves) {
        this.set(CraftBamboo.LEAVES, leaves);
    }
}
