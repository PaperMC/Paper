package org.bukkit.craftbukkit.block.data;

import org.bukkit.block.data.Ageable;

public abstract class CraftAgeable extends CraftBlockData implements Ageable {

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger("age");

    @Override
    public int getAge() {
        return this.get(CraftAgeable.AGE);
    }

    @Override
    public void setAge(int age) {
        this.set(CraftAgeable.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftAgeable.AGE);
    }
}
