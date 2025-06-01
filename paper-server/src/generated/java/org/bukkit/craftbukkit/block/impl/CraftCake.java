package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Cake;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftCake extends CraftBlockData implements Cake {
    private static final IntegerProperty BITES = CakeBlock.BITES;

    public CraftCake(BlockState state) {
        super(state);
    }

    @Override
    public int getBites() {
        return this.get(BITES);
    }

    @Override
    public void setBites(final int bites) {
        this.set(BITES, bites);
    }

    @Override
    public int getMaximumBites() {
        return BITES.max;
    }
}
