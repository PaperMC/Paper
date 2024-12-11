/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCrafter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Crafter {

    public CraftCrafter() {
        super();
    }

    public CraftCrafter(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCrafter

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty CRAFTING = getBoolean(net.minecraft.world.level.block.CrafterBlock.class, "crafting");
    private static final net.minecraft.world.level.block.state.properties.BooleanProperty TRIGGERED = getBoolean(net.minecraft.world.level.block.CrafterBlock.class, "triggered");
    private static final net.minecraft.world.level.block.state.properties.EnumProperty<?> ORIENTATION = getEnum(net.minecraft.world.level.block.CrafterBlock.class, "orientation");

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
