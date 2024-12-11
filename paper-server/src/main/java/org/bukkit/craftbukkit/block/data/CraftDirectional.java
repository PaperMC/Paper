package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Directional;

public abstract class CraftDirectional extends CraftBlockData implements Directional {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> FACING = getEnum("facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return this.get(CraftDirectional.FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        this.set(CraftDirectional.FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return this.getValues(CraftDirectional.FACING, org.bukkit.block.BlockFace.class);
    }
}
