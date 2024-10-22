package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.MossyCarpet;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftMossyCarpet extends CraftBlockData implements MossyCarpet {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean BOTTOM = getBoolean("bottom");
    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?>[] HEIGHTS = new net.minecraft.world.level.block.state.properties.BlockStateEnum[]{
        getEnum("north"), getEnum("east"), getEnum("south"), getEnum("west")
    };

    @Override
    public boolean isBottom() {
        return get(BOTTOM);
    }

    @Override
    public void setBottom(boolean up) {
        set(BOTTOM, up);
    }

    @Override
    public org.bukkit.block.data.type.MossyCarpet.Height getHeight(org.bukkit.block.BlockFace face) {
        return get(HEIGHTS[face.ordinal()], org.bukkit.block.data.type.MossyCarpet.Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, org.bukkit.block.data.type.MossyCarpet.Height height) {
        set(HEIGHTS[face.ordinal()], height);
    }
}
