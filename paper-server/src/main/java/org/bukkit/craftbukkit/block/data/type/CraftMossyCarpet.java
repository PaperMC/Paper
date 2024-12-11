package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.MossyCarpet;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftMossyCarpet extends CraftBlockData implements MossyCarpet {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty BOTTOM = getBoolean("bottom");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?>[] HEIGHTS = new net.minecraft.world.level.block.state.properties.EnumProperty[]{
        getEnum("north"), getEnum("east"), getEnum("south"), getEnum("west")
    };

    @Override
    public boolean isBottom() {
        return this.get(CraftMossyCarpet.BOTTOM);
    }

    @Override
    public void setBottom(boolean up) {
        this.set(CraftMossyCarpet.BOTTOM, up);
    }

    @Override
    public org.bukkit.block.data.type.MossyCarpet.Height getHeight(org.bukkit.block.BlockFace face) {
        return this.get(CraftMossyCarpet.HEIGHTS[face.ordinal()], org.bukkit.block.data.type.MossyCarpet.Height.class);
    }

    @Override
    public void setHeight(org.bukkit.block.BlockFace face, org.bukkit.block.data.type.MossyCarpet.Height height) {
        this.set(CraftMossyCarpet.HEIGHTS[face.ordinal()], height);
    }
}
