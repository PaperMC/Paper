package org.bukkit.craftbukkit.block.impl;

import com.google.common.base.Preconditions;
import io.papermc.paper.generated.GeneratedFrom;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.bukkit.block.data.type.TrialSpawner;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

@GeneratedFrom("1.21.6-pre1")
public class CraftTrialSpawner extends CraftBlockData implements TrialSpawner {
    private static final BooleanProperty OMINOUS = TrialSpawnerBlock.OMINOUS;

    private static final EnumProperty<TrialSpawnerState> STATE = TrialSpawnerBlock.STATE;

    public CraftTrialSpawner(BlockState state) {
        super(state);
    }

    @Override
    public boolean isOminous() {
        return this.get(OMINOUS);
    }

    @Override
    public void setOminous(final boolean ominous) {
        this.set(OMINOUS, ominous);
    }

    @Override
    public TrialSpawner.State getTrialSpawnerState() {
        return this.get(STATE, TrialSpawner.State.class);
    }

    @Override
    public void setTrialSpawnerState(final TrialSpawner.State state) {
        Preconditions.checkArgument(state != null, "state cannot be null!");
        this.set(STATE, state);
    }
}
