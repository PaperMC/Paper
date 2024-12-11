/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftWeatheringCopperBulb extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.CopperBulb, org.bukkit.block.data.Lightable, org.bukkit.block.data.Powerable {

    public CraftWeatheringCopperBulb() {
        super();
    }

    public CraftWeatheringCopperBulb(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftLightable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty LIT = getBoolean(net.minecraft.world.level.block.WeatheringCopperBulbBlock.class, "lit");

    @Override
    public boolean isLit() {
        return this.get(CraftWeatheringCopperBulb.LIT);
    }

    @Override
    public void setLit(boolean lit) {
        this.set(CraftWeatheringCopperBulb.LIT, lit);
    }

    // org.bukkit.craftbukkit.block.data.CraftPowerable

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty POWERED = getBoolean(net.minecraft.world.level.block.WeatheringCopperBulbBlock.class, "powered");

    @Override
    public boolean isPowered() {
        return this.get(CraftWeatheringCopperBulb.POWERED);
    }

    @Override
    public void setPowered(boolean powered) {
        this.set(CraftWeatheringCopperBulb.POWERED, powered);
    }
}
