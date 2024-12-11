package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Door;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftDoor extends CraftBlockData implements Door {

    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> HINGE = getEnum("hinge");

    @Override
    public org.bukkit.block.data.type.Door.Hinge getHinge() {
        return this.get(CraftDoor.HINGE, org.bukkit.block.data.type.Door.Hinge.class);
    }

    @Override
    public void setHinge(org.bukkit.block.data.type.Door.Hinge hinge) {
        this.set(CraftDoor.HINGE, hinge);
    }
}
