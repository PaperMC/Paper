package io.papermc.paper.util.capture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockStates;
import org.jspecify.annotations.Nullable;

/*
The premise of this storage is to essentially "mock" block placement in the game.
This should attempt to mirror block placement the best it can, in order to provide a read solution of possibly what blocks
are going to be placed in the world.

The reason why we use a custom capture map rather than just storing the blocks placed in the world is to ensure
that blocks are placed in the correct order, and that the vanilla block chain can occur, which includes physics updates.
 */
public final class CaptureRecordMap {

    private final Map<BlockPos, CaptureRecord> recordsByPos = new HashMap<>();

    public void setLatestBlockStateAt(BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
        this.add(new CaptureRecord(pos, state, flags));
    }

    public void setLatestBlockEntityAt(BlockPos pos, boolean clearPreviousBlockEntity, @Nullable BlockEntity add) {
        CaptureRecord oldRecord = this.recordsByPos.get(pos);
        if (oldRecord != null) {
            oldRecord.setBlockEntity(clearPreviousBlockEntity, add);
        }
    }

    private void add(CaptureRecord record) {
        this.recordsByPos.put(record.pos, record);
    }

    public boolean isEmpty() {
        return this.recordsByPos.isEmpty();
    }

    public @Nullable BlockState getLatestBlockStateAt(BlockPos pos) {
        CaptureRecord record = this.recordsByPos.get(pos);
        if (record == null) {
            return null;
        }

        return record.state;
    }

    // Null indicates that it's not present, no override
    // Optional empty indicates its being removed
    public @Nullable Optional<BlockEntity> getLatestBlockEntityAt(BlockPos pos) {
        CaptureRecord record = this.recordsByPos.get(pos);
        if (record == null) {
            return null;
        }

        return Optional.ofNullable(record.blockEntity);
    }

    public void applyBlockEntities(ServerLevel parent) {
        this.recordsByPos.keySet().forEach((pos) -> {
            Optional<BlockEntity> res = this.getLatestBlockEntityAt(pos);
            if (res != null && res.isPresent()) {
                parent.setBlockEntity(res.get());
            }
        });
    }

    public void applyApiPatch(ServerLevel level) {
        this.recordsByPos.keySet().forEach((pos) -> {
            this.recordsByPos.get(pos).applyApiPatch(level);
        });
    }

    // TODO: Clean this up
    public List<org.bukkit.block.BlockState> calculateLatestSnapshots(ServerLevel level) {
        List<org.bukkit.block.BlockState> out = new ArrayList<>();

        for (Map.Entry<BlockPos, CaptureRecord> entry : this.recordsByPos.entrySet()) {
            CaptureRecord captureRecord = entry.getValue();
            out.add(CraftBlockStates.getBlockState(level.getWorld(), entry.getKey(), captureRecord.state, captureRecord.blockEntity));
        }
        return out;
    }

    public Stream<org.bukkit.block.Block> getAffectedBlocks(ServerLevel level) {
        return this.recordsByPos.keySet().stream().map(pos -> CraftBlock.at(level, pos));
    }

    public static class CaptureRecord {

        private final BlockPos pos;

        private BlockState state;
        private @Nullable BlockEntity blockEntity;
        private boolean clearPreviousBlockEntity;
        private @Block.UpdateFlags int flags;

        public CaptureRecord(BlockPos pos, BlockState state, BlockEntity blockEntity) {
            this.pos = pos;
            this.state = state;
            this.blockEntity = blockEntity;
        }

        public CaptureRecord(BlockPos pos, BlockState state, @Block.UpdateFlags int flags) {
            this.pos = pos;
            this.state = state;
            this.flags = flags;
        }

        public CaptureRecord(boolean remove, @Nullable BlockEntity add, BlockPos pos) {
            this.clearPreviousBlockEntity = remove;
            this.blockEntity = add;
            this.pos = pos;
        }

        public void applyApiPatch(ServerLevel level) {
            if (this.clearPreviousBlockEntity) {
                level.removeBlockEntity(this.pos);
            }

            level.setBlock(this.pos, this.state, this.flags);
            if (this.blockEntity != null) {
                level.setBlockEntity(this.blockEntity);
            }
        }

        public void setBlockEntity(boolean clearPreviousBlockEntity, @Nullable BlockEntity add) {
            this.clearPreviousBlockEntity = clearPreviousBlockEntity;
            this.blockEntity = add;
        }
    }
}
