package org.bukkit.craftbukkit.block.data.type;

import org.bukkit.block.data.type.Crafter;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public abstract class CraftCrafter extends CraftBlockData implements Crafter {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty CRAFTING = getBoolean("crafting");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty TRIGGERED = getBoolean("triggered");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ORIENTATION = getEnum("orientation");

    @Override
    public boolean isCrafting() {
        return this.get(CraftCrafter.CRAFTING);
    }

    @Override
    public void setCrafting(boolean crafting) {
        this.set(CraftCrafter.CRAFTING, crafting);
    }

    @Override
    public boolean isTriggered() {
        return this.get(CraftCrafter.TRIGGERED);
    }

    @Override
    public void setTriggered(boolean triggered) {
        this.set(CraftCrafter.TRIGGERED, triggered);
    }

    @Override
    public org.bukkit.block.data.type.Crafter.Orientation getOrientation() {
        return this.get(CraftCrafter.ORIENTATION, org.bukkit.block.data.type.Crafter.Orientation.class);
    }

    @Override
    public void setOrientation(org.bukkit.block.data.type.Crafter.Orientation orientation) {
        this.set(CraftCrafter.ORIENTATION, orientation);
    }
}
