package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.annotation.GeneratedClass;
import net.minecraft.world.level.block.PotentSulfurBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PotentSulfurState;
import org.bukkit.block.data.type.PotentSulfur;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.jspecify.annotations.NullMarked;

@NullMarked
@GeneratedClass
public class CraftPotentSulfur extends CraftBlockData implements PotentSulfur {
    private static final EnumProperty<PotentSulfurState> STATE = PotentSulfurBlock.STATE;

    public CraftPotentSulfur(BlockState state) {
        super(state);
    }

    @Override
    public PotentSulfur.State getPotentSulfurState() {
        return this.get(STATE, PotentSulfur.State.class);
    }

    @Override
    public void setPotentSulfurState(final PotentSulfur.State state) {
        Preconditions.checkArgument(state != null, "state cannot be null!");
        this.set(STATE, state);
    }
}
