package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Rail;

public abstract class CraftRail extends CraftBlockData implements Rail {

    private static final net.minecraft.server.BlockStateEnum<?> SHAPE = getEnum("shape");

    @Override
    public Shape getShape() {
        return get(SHAPE, Shape.class);
    }

    @Override
    public void setShape(Shape shape) {
        set(SHAPE, shape);
    }

    @Override
    public java.util.Set<Shape> getShapes() {
        return getValues(SHAPE, Shape.class);
    }
}
