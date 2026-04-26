package io.papermc.paper.util.capture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import org.jspecify.annotations.Nullable;

public class MinecraftCaptureBridge implements PaperCapturingWorldLevel {

    private final ServerLevel parent;
    private final List<Runnable> queuedTasks = new ArrayList<>();

    // Effective represents plugin set blocks -> predicted blocks -> actual server level
    private final BlockPlacementPredictor effectiveReadLayer;
    private SimpleBlockPlacementPredictor writeLayer;

    // This is the layer that is written to in the server level potentially.
    // Mostly plugin set blocks
    private final SimpleBlockPlacementPredictor serverLevelOverlayLayer;

    private final CapturingTickAccess<Block> blocks;
    private final CapturingTickAccess<Fluid> liquids;

    private Consumer<Runnable> sink = this.queuedTasks::add;

    public MinecraftCaptureBridge(ServerLevel parent, BlockPlacementPredictor baseReadLayer) {
        this.parent = parent;

        this.serverLevelOverlayLayer = new SimpleBlockPlacementPredictor();
        SimpleBlockPlacementPredictor predictedBlocks = new SimpleBlockPlacementPredictor();

        this.effectiveReadLayer = new LayeredBlockPlacementPredictor(
                this.serverLevelOverlayLayer, // The overlay layer represents plugin set blocks!
                predictedBlocks, // Now predicted blocks
                baseReadLayer
        );

        this.writeLayer = predictedBlocks; // Predicting

        this.blocks = new CapturingTickAccess<>(parent.getBlockTicks());
        this.liquids = new CapturingTickAccess<>(parent.getFluidTicks());
    }

    @Override
    public long getSeed() {
        return this.parent.getSeed();
    }

    @Override
    public ServerLevel getLevel() {
        return this.parent;
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        return this.parent.getCurrentDifficultyAt(pos);
    }

    @Override
    public long nextSubTickCount() {
        return this.parent.nextSubTickCount();
    }

    @Override
    public LevelData getLevelData() {
        return this.parent.getLevelData();
    }

    @Override
    public @Nullable MinecraftServer getServer() {
        return this.parent.getServer();
    }

    @Override
    public ServerChunkCache getChunkSource() {
        return this.parent.getChunkSource();
    }

    @Override
    public boolean setBlockAndUpdate(BlockPos pos, BlockState blockState) {
        return this.setBlock(pos, blockState, Block.UPDATE_ALL);
    }

    @Override
    public SimpleBlockCapture forkCaptureSession() {
        return this.parent.capturer.createCaptureSession(new BlockPlacementPredictor() {
            @Override
            public Optional<BlockState> getLatestBlockAt(BlockPos pos) {
                return MinecraftCaptureBridge.this.effectiveReadLayer.getLatestBlockAt(pos);
            }

            @Override
            public Optional<LoadedBlockState> getLatestBlockAtIfLoaded(BlockPos pos) {
                return MinecraftCaptureBridge.this.effectiveReadLayer.getLatestBlockAtIfLoaded(pos);
            }

            @Override
            public Optional<BlockEntityPlacement> getLatestBlockEntityAt(BlockPos pos) {
                return MinecraftCaptureBridge.this.effectiveReadLayer.getLatestBlockEntityAt(pos);
            }
        });
    }

    @Override
    public RandomSource getRandom() {
        // TODO: Support rolling this back?
        return this.parent.getRandom();
    }

    @Override
    public void playSound(@Nullable Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        this.addTask((level) -> level.playSound(except, pos, sound, source, volume, pitch));
    }

    @Override
    public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        this.addTask((level) -> level.addParticle(particle, x, y, z, xd, yd, zd));
    }

    @Override
    public void levelEvent(@Nullable Entity source, int type, BlockPos pos, int data) {
        this.addTask((level) -> level.levelEvent(source, type, pos, data));
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
        this.addTask((level) -> level.gameEvent(gameEvent, position, context));
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.parent.getLightEngine();
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.parent.getWorldBorder();
    }

    @Override
    public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
        return this.effectiveReadLayer.getLatestBlockEntityAt(pos)
                .map(BlockPlacementPredictor.BlockEntityPlacement::blockEntity)
                .orElse(null);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.effectiveReadLayer.getLatestBlockAt(pos).orElseThrow(); // Should not ever be null, parent should pass value
    }

    @Override
    public @Nullable BlockState getBlockStateIfLoaded(BlockPos pos) {
        return this.effectiveReadLayer.getLatestBlockAtIfLoaded(pos).map(BlockPlacementPredictor.LoadedBlockState::state).orElse(null);
    }

    @Override
    public @Nullable FluidState getFluidIfLoaded(BlockPos pos) {
        return this.getBlockStateIfLoaded(pos).getFluidState();
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).getFluidState();
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity except, AABB bb, Predicate<? super Entity> selector) {
        return this.parent.getEntities(except, bb, selector);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTest, AABB bb, Predicate<? super T> selector) {
        return this.parent.getEntities(entityTypeTest, bb, selector);
    }

    @Override
    public List<? extends Player> players() {
        return this.parent.players();
    }

    @Override
    public @Nullable ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status, boolean loadOrGenerate) {
        return this.parent.getChunk(chunkX, chunkZ, status, loadOrGenerate);
    }

    @Override
    public @Nullable ChunkAccess getChunkIfLoadedImmediately(int x, int z) {
        return this.parent.getChunkIfLoadedImmediately(x, z);
    }

    @Override
    public int getHeight(Heightmap.Types type, int x, int z) {
        return this.parent.getHeight(type, x, z); // TODO?
    }

    @Override
    public int getSkyDarken() {
        return this.parent.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return this.parent.getBiomeManager();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
        return this.parent.getUncachedNoiseBiome(quartX, quartY, quartZ);
    }

    @Override
    public boolean isClientSide() {
        return this.parent.isClientSide();
    }

    @Override
    public int getSeaLevel() {
        return this.parent.getSeaLevel();
    }

    @Override
    public DimensionType dimensionType() {
        return this.parent.dimensionType();
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.parent.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.parent.enabledFeatures();
    }

    @Override
    public EnvironmentAttributeReader environmentAttributes() {
        return this.parent.environmentAttributes();
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
        return predicate.test(this.getBlockState(pos));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
        return predicate.test(this.getFluidState(pos));
    }

    public boolean silentSet(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
        return this.writeLayer.setBlockState(this.effectiveReadLayer, pos, blockState, updateFlags);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        BlockPos copy = pos.immutable();
        this.addTask((level) -> level.setBlock(copy, blockState, updateFlags, updateLimit));

        return this.writeLayer.setBlockState(this.effectiveReadLayer, copy, blockState, updateFlags);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
        BlockPos copy = pos.immutable();
        this.addTask((level) -> level.removeBlock(copy, movedByPiston));

        FluidState fluidState = this.getFluidState(copy);
        return this.silentSet(copy, fluidState.createLegacyBlock(), Block.UPDATE_ALL | (movedByPiston ? Block.UPDATE_MOVE_BY_PISTON : 0));
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropResources, @Nullable Entity breaker, int updateLimit) {
        BlockPos copy = pos.immutable();
        this.addTask((level) -> level.destroyBlock(copy, dropResources, breaker, updateLimit));

        BlockState blockState = this.getBlockState(copy);
        if (blockState.isAir()) {
            return false;
        } else {
            FluidState fluidState = this.getFluidState(copy);

            return this.silentSet(copy, fluidState.createLegacyBlock(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, @Block.UpdateFlags int updateFlags) {
        BlockPos copy = pos.immutable();
        this.addTask((level) -> level.sendBlockUpdated(copy, old, current, updateFlags));
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity) {
        this.writeLayer.getRecordMap().setLatestBlockEntityAt(blockEntity.getBlockPos().immutable(), false, blockEntity);
    }

    @Override
    public boolean setBlockSilent(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        BlockPos copy = pos.immutable();
        return this.silentSet(copy, blockState, updateFlags);
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return this.blocks;
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.liquids;
    }

    @Override
    public GameRules getGameRules() {
        return this.parent.getGameRules();
    }

    @Override
    public void addTask(Consumer<ServerLevel> level) {
        this.sink.accept(() -> level.accept(this.parent));
    }

    public @Nullable BlockState getLatestBlockState(BlockPos pos) {
        return this.effectiveReadLayer.getLatestBlockAt(pos).orElse(null);
    }

    public @Nullable Optional<BlockEntity> getLatestBlockEntity(BlockPos pos) {
        Optional<BlockPlacementPredictor.BlockEntityPlacement> placement = this.effectiveReadLayer.getLatestBlockEntityAt(pos);
        if (placement.isEmpty() || (placement.get().blockEntity() == null && !placement.get().removed())) {
            return null;
        }

        return placement.map(BlockPlacementPredictor.BlockEntityPlacement::blockEntity);
    }

    public List<org.bukkit.block.BlockState> calculateLatestSnapshots(ServerLevel level) {
        return this.writeLayer.getRecordMap().calculateLatestSnapshots(level);
    }

    public Stream<org.bukkit.block.Block> getAffectedBlocks(ServerLevel level) {
        return this.writeLayer.getRecordMap().getAffectedBlocks(level);
    }

    public void applyTasks() {
        this.sink = Runnable::run;
        for (Runnable runnable : this.queuedTasks) {
            runnable.run();
        }
        this.blocks.apply();
        this.liquids.apply();

        // If we have changes that the plugin applied on top of the already existing changes, we know that we can apply them.
        // So, do that!
        if (!this.serverLevelOverlayLayer.isEmpty()) {
            this.serverLevelOverlayLayer.getRecordMap().applyApiPatch(this.parent);
        }

        // Apply block entities, those may have been written in our estimation. So just apply them.
        // I don't really like this, as I in general don't really want to apply things from the prediction layer.
        // But, these may be mutated by anything.
        if (!this.writeLayer.getRecordMap().isEmpty()) {
            this.writeLayer.getRecordMap().applyBlockEntities(this.parent);
        }
    }

    public void allowWriteOnLevel() {
        this.writeLayer = this.serverLevelOverlayLayer;
    }

    public static class CapturingTickAccess<T> implements LevelTickAccess<T> {

        private final LevelTickAccess<T> wrapped;
        private final Set<BlockPos> scheduled = new HashSet<>();
        private final List<ScheduledTick<T>> ticks = new ArrayList<>();

        public CapturingTickAccess(LevelTickAccess<T> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean willTickThisTick(BlockPos pos, T type) {
            return this.wrapped.willTickThisTick(pos, type);
        }

        @Override
        public void schedule(ScheduledTick<T> tick) {
            this.scheduled.add(tick.pos());
            this.ticks.add(tick);
        }

        @Override
        public boolean hasScheduledTick(BlockPos pos, T type) {
            return this.wrapped.hasScheduledTick(pos, type) || this.scheduled.contains(pos);
        }

        @Override
        public int count() {
            return this.wrapped.count() + this.scheduled.size();
        }

        public void apply() {
            this.ticks.forEach(this.wrapped::schedule);
        }
    }
}
