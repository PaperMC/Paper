package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.CaveVinesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.CaveVines;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftCaveVines extends CraftBlockData implements CaveVines {
    private static final IntegerProperty AGE = CaveVinesBlock.AGE;

    private static final BooleanProperty BERRIES = CaveVinesBlock.BERRIES;

    public CraftCaveVines(BlockState state) {
        super(state);
    }

    @Override
    public int getAge() {
        return this.get(AGE);
    }

    @Override
    public void setAge(final int age) {
        this.set(AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return AGE.max;
    }

    @Override
    public boolean hasBerries() {
        return this.get(BERRIES);
    }

    @Override
    public void setBerries(final boolean berries) {
        this.set(BERRIES, berries);
    }
}
