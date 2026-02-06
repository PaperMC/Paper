package io.papermc.paper.util.capture;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.level.block.Block.UPDATE_NONE;

/*
The premise of this storage is to essentially "mock" block placement in the game.
This should attempt to mirror block placement the best it can, in order to provide a read solution of possibly what blocks
are going to be placed in the world.

The reason why we use a custom capture map rather than just storing the blocks placed in the world is to ensure
that blocks are placed in the correct order, and that the vanilla block chain can occur, which includes physics updates.
 */
public final class CaptureRecordMap {

    private final Map<BlockPos, CaptureRecord> recordsByPos = new HashMap<>();

    public void setLatestBlockStateAt(final BlockPos pos, final BlockState state, final int flags) {
        this.add(new CaptureRecord(state, pos));
    }

    public void setLatestBlockEntityAt(final BlockPos pos, final boolean remove, @Nullable final BlockEntity add) {
        this.add(new CaptureRecord(remove, add, pos));
    }

    private void add(final CaptureRecord record) {
        this.recordsByPos.put(record.pos, record);
    }

    public boolean isEmpty() {
        return this.recordsByPos.isEmpty();
    }

    public @Nullable BlockState getLatestBlockStateAt(final BlockPos pos) {
        CaptureRecord record = this.recordsByPos.get(pos);
        if (record == null) {
            return null;
        }

        return record.state;
    }

    // Null indicates that its not present, no override
    // Optional empty indicates its being removed
    public @Nullable Optional<@Nullable BlockEntity> getLatestBlockEntityAt(final BlockPos pos) {
        CaptureRecord record = this.recordsByPos.get(pos);
        if (record == null) {
            return null;
        }

        return Optional.ofNullable(record.blockEntity);
    }

    public void applyBlockEntities(ServerLevel parent) {
        this.recordsByPos.keySet().forEach((pos) -> {
            var res = getLatestBlockEntityAt(pos);
            if (res != null && res.isPresent()) {
                parent.setBlockEntity(res.get());
            }
        });
    }

    public void applyApiPatch(final ServerLevel level) {
        this.recordsByPos.keySet().forEach((pos) -> {
            this.recordsByPos.get(pos).applyApiPatch(level);
        });
    }

    // TODO: Clean this up
    public Map<Location, org.bukkit.block.BlockState> calculateLatestBlockStates(PaperCapturingWorldLevel predictor, ServerLevel level) {
        final Map<Location, org.bukkit.block.BlockState> out = new HashMap<>();

        this.recordsByPos.keySet().forEach((pos) -> {

            CaptureRecord captureRecord = this.recordsByPos.get(pos);
            out.put(CraftLocation.toBukkit(pos), CraftBlockStates.getBlockState(level.getWorld(), CraftLocation.toBlockPosition(CraftLocation.toBukkit(pos)), captureRecord.state, captureRecord.blockEntity));
        });

        return out;
    }



    public class CaptureRecord {

        private final BlockPos pos;

        private BlockState state;
        private BlockEntity blockEntity;
        private boolean removeBe;

        public CaptureRecord(BlockPos pos, BlockState state, BlockEntity blockEntity) {
            this.pos = pos;
            this.state = state;
            this.blockEntity = blockEntity;
        }

        public CaptureRecord(BlockState state, BlockPos pos) {
            this.pos = pos;
            this.state = state;
        }

        public CaptureRecord(boolean remove, @Nullable BlockEntity add, BlockPos pos) {
            this.removeBe = remove;
            this.blockEntity = add;
            this.pos = pos;
        }

        public void applyApiPatch(ServerLevel level) {
            if (this.removeBe) {
                level.removeBlockEntity(this.pos);
            }

            level.setBlock(this.pos, this.state, UPDATE_NONE | net.minecraft.world.level.block.Block.UPDATE_SKIP_ON_PLACE);
            if (this.blockEntity != null) {
                level.setBlockEntity(this.blockEntity);
            }
        }
    }
}