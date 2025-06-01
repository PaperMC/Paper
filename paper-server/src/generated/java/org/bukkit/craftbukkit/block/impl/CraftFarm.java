package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftFarm extends CraftBlockData implements Farmland {
    private static final IntegerProperty MOISTURE = FarmBlock.MOISTURE;

    public CraftFarm(BlockState state) {
        super(state);
    }

    @Override
    public int getMoisture() {
        return this.get(MOISTURE);
    }

    @Override
    public void setMoisture(final int moisture) {
        this.set(MOISTURE, moisture);
    }

    @Override
    public int getMaximumMoisture() {
        return MOISTURE.max;
    }
}
