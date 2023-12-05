package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Crafter;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCrafter extends CraftBlockData implements Crafter {

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean CRAFTING = getBoolean("crafting");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean TRIGGERED = getBoolean("triggered");
    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> ORIENTATION = getEnum("orientation");

    @Override
    public boolean isCrafting() {
        return get(CRAFTING);
    }

    @Override
    public void setCrafting(boolean crafting) {
        set(CRAFTING, crafting);
    }

    @Override
    public boolean isTriggered() {
        return get(TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        set(TRIGGERED, triggered);
    }

    @Override
    public org.bukkit.block.data.type.Crafter.Orientation getOrientation() {
        return get(ORIENTATION, org.bukkit.block.data.type.Crafter.Orientation.class);
    }

    @Override
    public void setOrientation(org.bukkit.block.data.type.Crafter.Orientation orientation) {
        set(ORIENTATION, orientation);
    }
}
