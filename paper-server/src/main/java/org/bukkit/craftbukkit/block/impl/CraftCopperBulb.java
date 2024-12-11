/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftCopperBulb extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.CopperBulb, org.bukkit.block.data.Lightable, org.bukkit.block.data.Powerable {

    public CraftCopperBulb() {
        super();
    }

    public CraftCopperBulb(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean(net.minecraft.world.level.block.CopperBulbBlock.class, "lit");

    @Override
    public boolean isLit() {
        return this.get(CraftCopperBulb.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftCopperBulb.LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.CopperBulbBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftCopperBulb.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftCopperBulb.POWERED, powered);
    }
}
