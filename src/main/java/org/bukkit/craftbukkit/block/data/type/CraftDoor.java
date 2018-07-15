package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Door;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftDoor extends CraftBlockData implements Door {

    private static final net.minecraft.server.BlockStateEnum<?> HINGE = getEnum("hinge");

    @Override
    public Hinge getHinge() {
        return get(HINGE, Hinge.class);
    }

    @Override
    public void setHinge(Hinge hinge) {
        set(HINGE, hinge);
    }
}
