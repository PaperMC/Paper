package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Wall;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftWall extends CraftBlockData implements Wall {

    private static final net.minecraft.server.BlockStateBoolean UP = getBoolean("up");
    private static final net.minecraft.server.BlockStateEnum<?>[] HEIGHTS = new net.minecraft.server.BlockStateEnum[]{
        getEnum("north"), getEnum("east"), getEnum("south"), getEnum("west")
    };

    @Override
    public boolean isUp() {
        return get(UP);
    }

    @Override
    public void setUp(boolean up) {
        set(UP, up);
    }

    @Override
    public Height getHeight(org.bukkit.block.BlockFace face) {
        return get(HEIGHTS[face.ordinal()], Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, Height height) {
        set(HEIGHTS[face.ordinal()], height);
    }
}
