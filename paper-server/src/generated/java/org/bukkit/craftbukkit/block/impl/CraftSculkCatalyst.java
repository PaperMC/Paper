package org.bukkit.craftbukkit.block.impl;

import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.SculkCatalystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.bukkit.block.data.type.SculkCatalyst;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftSculkCatalyst extends CraftBlockData implements SculkCatalyst {
    private static final BooleanProperty PULSE = SculkCatalystBlock.PULSE;

    public CraftSculkCatalyst(BlockState state) {
        super(state);
    }

    @Override
    public boolean isBloom() {
        return this.get(PULSE);
    }

    @Override
    public void setBloom(final boolean bloom) {
        this.set(PULSE, bloom);
    }
}
