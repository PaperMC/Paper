package org.bukkit.craftbukkit.block.impl;

import com.google.common.collect.ImmutableSet;
import io.papermc.paper.generated.GeneratedFrom;
import java.util.Set;
import net.minecraft.world.level.block.BrewingStandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.BrewingStand;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftBrewingStand extends CraftBlockData implements BrewingStand {
    private static final BooleanProperty[] HAS_BOTTLE = BrewingStandBlock.HAS_BOTTLE;

    public CraftBrewingStand(BlockState state) {
        super(state);
    }

    @Override
    public boolean hasBottle(final int index) {
        return this.get(HAS_BOTTLE[index]);
    }

    @Override
    public void setBottle(final int index, final boolean bottle) {
        this.set(HAS_BOTTLE[index], bottle);
    }

    @Override
    public Set<Integer> getBottles() {
        ImmutableSet.Builder<Integer> bottles = ImmutableSet.builder();
        for (int index = 0, len = HAS_BOTTLE.length; index < len; index++) {
            if (this.get(HAS_BOTTLE[index])) {
                bottles.add(index);
            }
        }
        return bottles.build();
    }

    @Override
    public int getMaximumBottles() {
        return HAS_BOTTLE.length;
    }
}
