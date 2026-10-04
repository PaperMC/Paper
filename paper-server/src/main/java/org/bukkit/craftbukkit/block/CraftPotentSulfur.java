package org.bukkit.craftbukkit.block;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PotentSulfurBlock;
import net.minecraft.world.level.block.entity.PotentSulfurBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PotentSulfurState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.PotentSulfur;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;

public class CraftPotentSulfur extends CraftBlockEntityState<PotentSulfurBlockEntity> implements PotentSulfur {

    private static final Logger LOGGER = LogUtils.getLogger();

    public CraftPotentSulfur(World world, PotentSulfurBlockEntity blockEntity) {
        super(world, blockEntity);
    }

    protected CraftPotentSulfur(CraftPotentSulfur state, Location location) {
        super(state, location);
    }

    @Override
    public EruptionMode getEruptionMode() {
        final EruptionMode override = this.getEruptionModeOverride();
        if (override != null) {
            return override;
        }

        this.requirePlaced();
        return vanillaEruptionMode(this.getWorldHandle(), this.getPosition());
    }

    @Override
    public @Nullable EruptionMode getEruptionModeOverride() {
        return this.getSnapshot().eruptionModeOverride;
    }

    @Override
    public void setEruptionModeOverride(@Nullable EruptionMode mode) {
        this.getSnapshot().eruptionModeOverride = mode;
    }

    @Override
    public boolean erupt() {
        this.requirePlaced();
        final Level level = this.world.getHandle();
        final BlockPos pos = this.getPosition();
        final BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof PotentSulfurBlock) || !(level.getBlockEntity(pos) instanceof PotentSulfurBlockEntity blockEntity)) {
            return false;
        }

        final PotentSulfurState currentState = state.getValue(PotentSulfurBlock.STATE);
        if (currentState != PotentSulfurState.WET && currentState != PotentSulfurState.DORMANT) {
            return false;
        }

        // without a water column the countdown never runs, which would leave the geyser erupting forever
        if (PotentSulfurBlockEntity.findNoxiousGasSourceBlock(level, pos) == null) {
            return false;
        }

        blockEntity.forcedEruption = true;
        blockEntity.resetCountdown(); // eruption duration is calculated on the next countdown update
        level.setBlockAndUpdate(pos, state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.ERUPTING));
        return true;
    }

    @Override
    public boolean update(boolean force, boolean applyPhysics) {
        boolean result = super.update(force, applyPhysics);

        if (result && this.isPlaced()) {
            // the eruption mode determines the block state, so apply it to the block in the world
            final Level level = this.world.getHandle();
            final BlockPos pos = this.getPosition();
            final BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof PotentSulfurBlock) {
                final BlockState newState = PotentSulfurBlock.validBlockState(state, level, pos);
                if (newState != state) {
                    level.setBlockAndUpdate(pos, newState);
                    if (state.getValue(PotentSulfurBlock.STATE) == PotentSulfurState.ERUPTING && newState.getValue(PotentSulfurBlock.STATE) != PotentSulfurState.CONTINUOUS) {
                        level.gameEvent(GameEvent.BLOCK_DEACTIVATE, pos, GameEvent.Context.of(state));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public CraftPotentSulfur copy() {
        return new CraftPotentSulfur(this, null);
    }

    @Override
    public CraftPotentSulfur copy(Location location) {
        return new CraftPotentSulfur(this, location);
    }

    /**
     * Called from {@link PotentSulfurBlock#validBlockState} once a water source is known to be above the block.
     *
     * @return the state dictated by the eruption mode, or {@code null} to use the vanilla state
     */
    public static @Nullable BlockState applyEruptionMode(final BlockState state, final LevelReader level, final BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof PotentSulfurBlockEntity blockEntity)) {
            return null;
        }

        final EruptionMode mode = blockEntity.eruptionModeOverride;
        if (mode == EruptionMode.CONTINUOUS) {
            blockEntity.forcedEruption = false;
            return state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.CONTINUOUS);
        }

        final PotentSulfurState currentState = state.getValue(PotentSulfurBlock.STATE);
        if (blockEntity.forcedEruption && currentState == PotentSulfurState.ERUPTING) {
            return state;
        }

        if (mode == null) {
            return null;
        }

        return switch (mode) {
            case NEVER -> state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.WET);
            case PERIODIC -> {
                // same as the vanilla magma block behavior
                if (currentState != PotentSulfurState.ERUPTING && currentState != PotentSulfurState.DORMANT) {
                    blockEntity.resetCountdown();
                }
                yield currentState == PotentSulfurState.ERUPTING ? state : state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.DORMANT);
            }
            default -> null;
        };
    }

    /**
     * Called from the countdown ticker when the geyser switches state, to end forced eruptions in the eruption mode's state.
     */
    public static PotentSulfurState stateAfterEruption(final PotentSulfurBlockEntity blockEntity, final PotentSulfurState stateToSet, final BlockState state, final Level level, final BlockPos pos) {
        if (stateToSet != PotentSulfurState.DORMANT || !blockEntity.forcedEruption) {
            return stateToSet;
        }

        blockEntity.forcedEruption = false;
        return PotentSulfurBlock.validBlockState(state.setValue(PotentSulfurBlock.STATE, PotentSulfurState.DORMANT), level, pos).getValue(PotentSulfurBlock.STATE);
    }

    public static @Nullable EruptionMode parseEruptionMode(final @Nullable String mode, final BlockPos pos) {
        if (mode == null) {
            return null;
        }

        try {
            return EruptionMode.valueOf(mode);
        } catch (final IllegalArgumentException ignored) {
            LOGGER.error("Unknown eruption mode {} for potent sulfur at {}", mode, pos);
            return null;
        }
    }

    // mirrors the block below checks in PotentSulfurBlock#validBlockState
    private static EruptionMode vanillaEruptionMode(final LevelReader level, final BlockPos pos) {
        final BlockState belowState = level.getBlockState(pos.below());
        final FluidState belowFluidState = belowState.getFluidState();
        if (!belowFluidState.isEmpty() && !belowFluidState.isSource()) {
            return EruptionMode.NEVER;
        }

        if (belowState.is(BlockTags.CAUSES_CONTINUOUS_GEYSER_ERUPTIONS)) {
            return EruptionMode.CONTINUOUS;
        }

        if (belowState.is(BlockTags.CAUSES_PERIODIC_GEYSER_ERUPTIONS)) {
            return EruptionMode.PERIODIC;
        }

        return EruptionMode.NEVER;
    }
}
