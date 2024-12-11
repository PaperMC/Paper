package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Wall;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftWall extends CraftBlockData implements Wall {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty UP = getBoolean("up");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?>[] HEIGHTS = new net.minecraft.world.level.block.state.properties.EnumProperty[]{
        getEnum("north"), getEnum("east"), getEnum("south"), getEnum("west")
    };

    @Override
    public boolean isUp() {
        return this.get(CraftWall.UP);
    }

    @Override
    public void setUp(boolean up) {
        this.set(CraftWall.UP, up);
    }

    @Override
    public org.bukkit.block.data.type.Wall.Height getHeight(org.bukkit.block.BlockFace face) {
        return this.get(CraftWall.HEIGHTS[face.ordinal()], org.bukkit.block.data.type.Wall.Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, org.bukkit.block.data.type.Wall.Height height) {
        this.set(CraftWall.HEIGHTS[face.ordinal()], height);
    }
}
