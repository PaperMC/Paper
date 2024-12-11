package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Rail;

public abstract class CraftRail extends CraftBlockData implements Rail {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> SHAPE = getEnum("shape");

    @Override
    public org.bukkit.block.data.Rail.Shape getShape() {
        return this.get(CraftRail.SHAPE, org.bukkit.block.data.Rail.Shape.class);
    }

    @Override
    public void setShape(org.bukkit.block.data.Rail.Shape shape) {
        this.set(CraftRail.SHAPE, shape);
    }

    @Override
    public java.util.Set<org.bukkit.block.data.Rail.Shape> getShapes() {
        return this.getValues(CraftRail.SHAPE, org.bukkit.block.data.Rail.Shape.class);
    }
}
