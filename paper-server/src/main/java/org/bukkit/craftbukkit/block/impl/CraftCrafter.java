/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCrafter extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.Crafter {

    public CraftCrafter() {
        super();
    }

    public CraftCrafter(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCrafter

    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean CRAFTING = getBoolean(net.minecraft.world.level.block.CrafterBlock.class, "crafting");
    private static final net.minecraft.world.level.block.state.properties.BlockStateBoolean TRIGGERED = getBoolean(net.minecraft.world.level.block.CrafterBlock.class, "triggered");
    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> ORIENTATION = getEnum(net.minecraft.world.level.block.CrafterBlock.class, "orientation");

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
