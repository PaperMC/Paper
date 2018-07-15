package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Stairs;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftStairs extends CraftBlockData implements Stairs {

    private static final net.minecraft.server.BlockStateEnum<?> SHAPE = getEnum("shape");

    @Override
    public Shape getShape() {
        return get(SHAPE, Shape.class);
    }

    @Override
    public void setShape(Shape shape) {
        set(SHAPE, shape);
    }
}
