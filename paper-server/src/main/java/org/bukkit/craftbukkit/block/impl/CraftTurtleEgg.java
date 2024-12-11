/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.block.impl;

public final class CraftTurtleEgg extends org.bukkit.craftbukkit.block.data.CraftBlockData implements org.bukkit.block.data.type.TurtleEgg, org.bukkit.block.data.Hatchable {

    public CraftTurtleEgg() {
        super();
    }

    public CraftTurtleEgg(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftTurtleEgg

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty EGGS = getInteger(net.minecraft.world.level.block.TurtleEggBlock.class, "eggs");

    @Override
    public int getEggs() {
        return this.get(CraftTurtleEgg.EGGS);
    }

    @Override
    public void setEggs(int eggs) {
        this.set(CraftTurtleEgg.EGGS, eggs);
    }

    @Override
    public int getMinimumEggs() {
        return getMin(CraftTurtleEgg.EGGS);
    }

    @Override
    public int getMaximumEggs() {
        return getMax(CraftTurtleEgg.EGGS);
    }

    // org.bukkit.craftbukkit.block.data.CraftHatchable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty HATCH = getInteger(net.minecraft.world.level.block.TurtleEggBlock.class, "hatch");

    @Override
    public int getHatch() {
        return this.get(CraftTurtleEgg.HATCH);
    }

    @Override
    public void setHatch(int hatch) {
        this.set(CraftTurtleEgg.HATCH, hatch);
    }

    @Override
    public int getMaximumHatch() {
        return getMax(CraftTurtleEgg.HATCH);
    }
}
