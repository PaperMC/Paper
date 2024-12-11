/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftBrewingStand extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.BrewingStand {

    public CraftBrewingStand() {
        super();
    }

    public CraftBrewingStand(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftBrewingStand

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty[] HAS_BOTTLE = new net.minecraft.world.level.block.state.properties.BooleanProperty[]{
        getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_0"), getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_1"), getBoolean(net.minecraft.world.level.block.BrewingStandBlock.class, "has_bottle_2")
    };

    @Override
    public boolean hasBottle(int bottle) {
        return this.get(CraftBrewingStand.HAS_BOTTLE[bottle]);
    }

    @Override
    public void setBottle(int bottle, boolean has) {
        this.set(CraftBrewingStand.HAS_BOTTLE[bottle], has);
    }

    @Override
    public java.util.Set<Integer> getBottles() {
        com.google.common.collect.ImmutableSet.Builder<Integer> bottles = com.google.common.collect.ImmutableSet.builder();

        for (int index = 0; index < this.getMaximumBottles(); index++) {
            if (this.hasBottle(index)) {
                bottles.add(index);
            }
        }

        return bottles.build();
    }

    @Override
    public int getMaximumBottles() {
        return CraftBrewingStand.HAS_BOTTLE.length;
    }
}
