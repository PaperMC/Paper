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
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
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

    private final Map<BlockPos, ArrayDeque<BlockPlacement>> recordsByPos = new HashMap<>();
    private final ArrayDeque<BlockPlacement> orderedRecords = new ArrayDeque<>();

    interface BlockPlacement {
        void applyApiPatch(ServerLevel serverLevel);

        BlockPos pos();
    }


    public record BlockEntityRecord(boolean remove, @Nullable BlockEntity add, BlockPos pos) implements BlockPlacement {

        // If we have a block entity that we change, apply to the server level.
        @Override
        public void applyApiPatch(final ServerLevel level) {
            if (this.remove) {
                level.removeBlockEntity(this.pos);
                return;
            }
            if (this.add != null) {
                level.setBlockEntity(this.add);
            }
        }
    }

    public record BlockStateRecord(BlockState blockState, int flags, BlockPos pos) implements BlockPlacement {

        // If we have a block state that we change, insert in without updating anything else about the world.
        @Override
        public void applyApiPatch(final ServerLevel level) {
            level.setBlock(this.pos, this.blockState, UPDATE_NONE | net.minecraft.world.level.block.Block.UPDATE_SKIP_ON_PLACE);
        }
    }

    public void setLatestBlockStateAt(final BlockPos pos, final BlockState state, final int flags) {
        this.add(new BlockStateRecord(state, flags, pos));
    }

    public void setLatestBlockEntityAt(final BlockPos pos, final boolean remove, @Nullable final BlockEntity add) {
        this.add(new BlockEntityRecord(remove, add, pos));
    }

    private void add(final BlockPlacement record) {
        this.recordsByPos.computeIfAbsent(record.pos(), k -> new ArrayDeque<>()).addLast(record);
        this.orderedRecords.addLast(record);
    }

    public boolean isEmpty() {
        return this.orderedRecords.isEmpty();
    }

    public @Nullable BlockState getLatestBlockStateAt(final BlockPos pos) {
        final Deque<BlockPlacement> dq = this.recordsByPos.get(pos);
        if (dq == null) return null;

        for (BlockPlacement record : this.of(dq.descendingIterator())) {
            if (record instanceof BlockStateRecord bsr) {
                return bsr.blockState();
            }
        }

        return null;
    }

    // Null indicates that its not present, no override
    // Optional empty indicates its being removed
    public @Nullable Optional<@Nullable BlockEntity> getLatestBlockEntityAt(final BlockPos pos) {
        final Deque<BlockPlacement> dq = this.recordsByPos.get(pos);
        if (dq == null) return null;

        for (BlockPlacement record : this.of(dq.descendingIterator())) {
            if (record instanceof BlockEntityRecord ber) {
                return ber.remove() ? Optional.empty() : Optional.of(ber.add());
            }
        }

        return null;
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
        for (final BlockPlacement record : this.orderedRecords) {
            record.applyApiPatch(level);
        }
    }

    // TODO: Clean this up
    public Map<Location, org.bukkit.block.BlockState> calculateLatestBlockStates(final ServerLevel level) {
        final Map<Location, org.bukkit.block.BlockState> out = new HashMap<>();

        for (final Map.Entry<BlockPos, ArrayDeque<BlockPlacement>> entry : this.recordsByPos.entrySet()) {
            final BlockPos pos = entry.getKey();
            final ArrayDeque<BlockPlacement> dq = entry.getValue();

            BlockState latestState = null;
            BlockEntity latestBe = null;
            boolean beKnown = false;

            final Iterator<BlockPlacement> it = dq.descendingIterator();
            while (it.hasNext() && (latestState == null || !beKnown)) {
                final BlockPlacement r = it.next();

                if (latestState == null && r instanceof BlockStateRecord bsr) {
                    latestState = bsr.blockState();
                }

                if (!beKnown && r instanceof BlockEntityRecord ber) {
                    beKnown = true;
                    latestBe = ber.remove() ? null : ber.add();
                }
            }

            if (latestState == null) {
                continue;
            }

            if (!beKnown) {
                latestBe = level.getBlockEntity(pos);
            }

            out.put(CraftLocation.toBukkit(pos), CraftBlockStates.getBlockState(level.getWorld(), CraftLocation.toBlockPosition(CraftLocation.toBukkit(pos)), latestState, latestBe));
        }

        return out;
    }

    private Iterable<BlockPlacement> of(Iterator<BlockPlacement> of) {
        return new Iterable<>() {
            @Override
            public @NotNull Iterator<BlockPlacement> iterator() {
                return of;
            }
        };
    }
}