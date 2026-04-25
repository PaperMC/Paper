package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.TickPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.Nullable;

public abstract class DelegatedLevelAccessor implements WorldGenLevel {

    private WorldGenLevel delegate;

    public void setDelegate(WorldGenLevel delegate) {
        this.delegate = delegate;
    }

    public WorldGenLevel getDelegate() {
        return this.delegate;
    }

    @Override
    public long getSeed() {
        return this.delegate.getSeed();
    }

    @Override
    public boolean ensureCanWrite(BlockPos pos) {
        return this.delegate.ensureCanWrite(pos);
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {
        this.delegate.setCurrentlyGenerating(currentlyGenerating);
    }

    @Override
    public ServerLevel getLevel() {
        return this.delegate.getLevel();
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return this.delegate.getMinecraftWorld();
    }

    @Override
    public long nextSubTickCount() {
        return this.delegate.nextSubTickCount();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return this.delegate.getBlockTicks();
    }

    @Override
    public void scheduleTick(BlockPos pos, Block type, int tickDelay, TickPriority priority) {
        this.delegate.scheduleTick(pos, type, tickDelay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Block type, int tickDelay) {
        this.delegate.scheduleTick(pos, type, tickDelay);
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.delegate.getFluidTicks();
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid type, int tickDelay, TickPriority priority) {
        this.delegate.scheduleTick(pos, type, tickDelay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid type, int tickDelay) {
        this.delegate.scheduleTick(pos, type, tickDelay);
    }

    @Override
    public LevelData getLevelData() {
        return this.delegate.getLevelData();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        return this.delegate.getCurrentDifficultyAt(pos);
    }

    @Override
    public MinecraftServer getServer() {
        return this.delegate.getServer();
    }

    @Override
    public Difficulty getDifficulty() {
        return this.delegate.getDifficulty();
    }

    @Override
    public ChunkSource getChunkSource() {
        return this.delegate.getChunkSource();
    }

    @Override
    public boolean hasChunk(int chunkX, int chunkZ) {
        return this.delegate.hasChunk(chunkX, chunkZ);
    }

    @Override
    public RandomSource getRandom() {
        return this.delegate.getRandom();
    }

    @Override
    public void updateNeighborsAt(BlockPos pos, Block sourceBlock) {
        this.delegate.updateNeighborsAt(pos, sourceBlock);
    }

    @Override
    public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        this.delegate.neighborShapeChanged(direction, pos, neighborPos, neighborState, updateFlags, updateLimit);
    }

    @Override
    public void playSound(@Nullable final Entity except, final BlockPos pos, final SoundEvent sound, final SoundSource source, final float volume, final float pitch) {
        this.delegate.playSound(except, pos, sound, source, volume, pitch);
    }

    @Override
    public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        this.delegate.addParticle(particle, x, y, z, xd, yd, zd);
    }

    @Override
    public void levelEvent(Entity source, int type, BlockPos pos, int data) {
        this.delegate.levelEvent(source, type, pos, data);
    }

    @Override
    public void levelEvent(int type, BlockPos pos, int data) {
        this.delegate.levelEvent(type, pos, data);
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
        this.delegate.gameEvent(gameEvent, position, context);
    }

    @Override
    public void gameEvent(Entity sourceEntity, Holder<GameEvent> gameEvent, Vec3 pos) {
        this.delegate.gameEvent(sourceEntity, gameEvent, pos);
    }

    @Override
    public void gameEvent(Entity sourceEntity, Holder<GameEvent> gameEvent, BlockPos pos) {
        this.delegate.gameEvent(sourceEntity, gameEvent, pos);
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {
        this.delegate.gameEvent(gameEvent, pos, context);
    }

    @Override
    public void gameEvent(ResourceKey<GameEvent> gameEvent, BlockPos pos, GameEvent.Context context) {
        this.delegate.gameEvent(gameEvent, pos, context);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return this.delegate.getBlockEntity(pos, type);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity source, AABB testArea) {
        return this.delegate.getEntityCollisions(source, testArea);
    }

    @Override
    public boolean isUnobstructed(Entity source, VoxelShape shape) {
        return this.delegate.isUnobstructed(source, shape);
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
        return this.delegate.getHeightmapPos(type, pos);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
        return this.delegate.getChunk(chunkX, chunkZ, targetStatus, loadOrGenerate);
    }

    @Override
    public int getHeight(Heightmap.Types type, int x, int z) {
        return this.delegate.getHeight(type, x, z);
    }

    @Override
    public int getSkyDarken() {
        return this.delegate.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return this.delegate.getBiomeManager();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos pos) {
        return this.delegate.getBiome(pos);
    }

    @Override
    public Stream<BlockState> getBlockStatesIfLoaded(AABB box) {
        return this.delegate.getBlockStatesIfLoaded(box);
    }


    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
        return this.delegate.getNoiseBiome(quartX, quartY, quartZ);
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
        return this.delegate.getUncachedNoiseBiome(quartX, quartY, quartZ);
    }

    @Override
    public boolean isClientSide() {
        return this.delegate.isClientSide();
    }

    @Override
    public int getSeaLevel() {
        return this.delegate.getSeaLevel();
    }

    @Override
    public DimensionType dimensionType() {
        return this.delegate.dimensionType();
    }

    @Override
    public int getMinY() {
        return this.delegate.getMinY();
    }

    @Override
    public int getHeight() {
        return this.delegate.getHeight();
    }

    @Override
    public boolean isEmptyBlock(BlockPos pos) {
        return this.delegate.isEmptyBlock(pos);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPos pos) {
        return this.delegate.canSeeSkyFromBelowWater(pos);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPos pos) {
        return this.delegate.getPathfindingCostFromLightLevels(pos);
    }

    @Override
    public float getLightLevelDependentMagicValue(BlockPos pos) {
        return this.delegate.getLightLevelDependentMagicValue(pos);
    }

    @Override
    public ChunkAccess getChunk(BlockPos pos) {
        return this.delegate.getChunk(pos);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ) {
        return this.delegate.getChunk(chunkX, chunkZ);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) {
        return this.delegate.getChunk(chunkX, chunkZ, status);
    }

    @Override
    public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return this.delegate.getChunkForCollisions(chunkX, chunkZ);
    }

    @Override
    public boolean isWaterAt(BlockPos pos) {
        return this.delegate.isWaterAt(pos);
    }

    @Override
    public boolean containsAnyLiquid(AABB box) {
        return this.delegate.containsAnyLiquid(box);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos) {
        return this.delegate.getMaxLocalRawBrightness(pos);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos pos, int skyDarkening) {
        return this.delegate.getMaxLocalRawBrightness(pos, skyDarkening);
    }

    @Override
    public boolean hasChunkAt(int blockX, int blockZ) {
        return this.delegate.hasChunkAt(blockX, blockZ);
    }

    @Override
    public boolean hasChunkAt(BlockPos pos) {
        return this.delegate.hasChunkAt(pos);
    }

    @Override
    public boolean hasChunksAt(BlockPos pos0, BlockPos pos1) {
        return this.delegate.hasChunksAt(pos0, pos1);
    }

    @Override
    public boolean hasChunksAt(int x0, int y0, int z0, int x1, int y1, int z1) {
        return this.delegate.hasChunksAt(x0, y0, z0, x1, y1, z1);
    }

    @Override
    public boolean hasChunksAt(int x0, int z0, int x1, int z1) {
        return this.delegate.hasChunksAt(x0, z0, x1, z1);
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.delegate.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.delegate.enabledFeatures();
    }

    @Override
    public EnvironmentAttributeReader environmentAttributes() {
        return this.delegate.environmentAttributes();
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> key) {
        return this.delegate.holderLookup(key);
    }


    @Override
    public LevelLightEngine getLightEngine() {
        return this.delegate.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer layer, BlockPos pos) {
        return this.delegate.getBrightness(layer, pos);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int darkening) {
        return this.delegate.getRawBrightness(pos, darkening);
    }

    @Override
    public boolean canSeeSky(BlockPos pos) {
        return this.delegate.canSeeSky(pos);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.delegate.getWorldBorder();
    }

    @Override
    public boolean isUnobstructed(BlockState state, BlockPos pos, CollisionContext context) {
        return this.delegate.isUnobstructed(state, pos, context);
    }

    @Override
    public boolean isUnobstructed(Entity ignore) {
        return this.delegate.isUnobstructed(ignore);
    }

    @Override
    public boolean noCollision(AABB aabb) {
        return this.delegate.noCollision(aabb);
    }

    @Override
    public boolean noCollision(Entity source) {
        return this.delegate.noCollision(source);
    }

    @Override
    public boolean noCollision(Entity entity, AABB aabb) {
        return this.delegate.noCollision(entity, aabb);
    }

    @Override
    public boolean noCollision(Entity entity, AABB aabb, boolean alwaysCollideWithFluids) {
        return this.delegate.noCollision(entity, aabb, alwaysCollideWithFluids);
    }

    @Override
    public boolean noBlockCollision(Entity entity, AABB aabb) {
        return this.delegate.noBlockCollision(entity, aabb);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity source, AABB box) {
        return this.delegate.getCollisions(source, box);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity source, AABB box) {
        return this.delegate.getBlockCollisions(source, box);
    }

    @Override
    public Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity source, AABB box) {
        return this.delegate.getBlockAndLiquidCollisions(source, box);
    }

    @Override
    public BlockHitResult clipIncludingBorder(ClipContext c) {
        return this.delegate.clipIncludingBorder(c);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity source, AABB box) {
        return this.delegate.collidesWithSuffocatingBlock(source, box);
    }

    @Override
    public Optional<BlockPos> findSupportingBlock(Entity source, AABB box) {
        return this.delegate.findSupportingBlock(source, box);
    }

    @Override
    public Optional<Vec3> findFreePosition(Entity source, VoxelShape allowedCenters, Vec3 preferredCenter, double sizeX, double sizeY, double sizeZ) {
        return this.delegate.findFreePosition(source, allowedCenters, preferredCenter, sizeX, sizeY, sizeZ);
    }

    @Override
    public int getDirectSignal(BlockPos pos, Direction direction) {
        return this.delegate.getDirectSignal(pos, direction);
    }

    @Override
    public int getDirectSignalTo(BlockPos pos) {
        return this.delegate.getDirectSignalTo(pos);
    }

    @Override
    public int getControlInputSignal(BlockPos pos, Direction direction, boolean onlyDiodes) {
        return this.delegate.getControlInputSignal(pos, direction, onlyDiodes);
    }

    @Override
    public boolean hasSignal(BlockPos pos, Direction direction) {
        return this.delegate.hasSignal(pos, direction);
    }

    @Override
    public int getSignal(BlockPos pos, Direction direction) {
        return this.delegate.getSignal(pos, direction);
    }

    @Override
    public boolean hasNeighborSignal(BlockPos blockPos) {
        return this.delegate.hasNeighborSignal(blockPos);
    }

    @Override
    public int getBestNeighborSignal(BlockPos pos) {
        return this.delegate.getBestNeighborSignal(pos);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.delegate.getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.delegate.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.delegate.getFluidState(pos);
    }

    @Override
    public int getLightEmission(BlockPos pos) {
        return this.delegate.getLightEmission(pos);
    }

    @Override
    public Stream<BlockState> getBlockStates(AABB box) {
        return this.delegate.getBlockStates(box);
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext c) {
        return this.delegate.isBlockInLine(c);
    }

    @Override
    public BlockHitResult clip(ClipContext c, BlockPos pos) {
        return this.delegate.clip(c, pos);
    }

    @Override
    public BlockHitResult clip(ClipContext c) {
        return this.delegate.clip(c);
    }

    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 from, Vec3 to, BlockPos pos, VoxelShape blockShape, BlockState blockState) {
        return this.delegate.clipWithInteractionOverride(from, to, pos, blockShape, blockState);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape blockShape, Supplier<VoxelShape> belowBlockShape) {
        return this.delegate.getBlockFloorHeight(blockShape, belowBlockShape);
    }

    @Override
    public double getBlockFloorHeight(BlockPos pos) {
        return this.delegate.getBlockFloorHeight(pos);
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
        return this.delegate.getEntities(except, bb, selector);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
        return this.delegate.getEntities(type, bb, selector);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb, Predicate<? super T> selector) {
        return this.delegate.getEntitiesOfClass(baseClass, bb, selector);
    }

    @Override
    public List<? extends Player> players() {
        return this.delegate.players();
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB bb) {
        return this.delegate.getEntities(except, bb);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb) {
        return this.delegate.getEntitiesOfClass(baseClass, bb);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double range, Predicate<Entity> predicate) {
        return this.delegate.getNearestPlayer(x, y, z, range, predicate);
    }

    @Override
    public Player getNearestPlayer(Entity source, double maxDist) {
        return this.delegate.getNearestPlayer(source, maxDist);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double maxDist, boolean filterOutCreative) {
        return this.delegate.getNearestPlayer(x, y, z, maxDist, filterOutCreative);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double x, double y, double z, double range) {
        return this.delegate.hasNearbyAlivePlayer(x, y, z, range);
    }

    @Override
    public Player getPlayerByUUID(UUID uuid) {
        return this.delegate.getPlayerByUUID(uuid);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        return this.delegate.setBlock(pos, blockState, updateFlags, updateLimit);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags) {
        return this.delegate.setBlock(pos, blockState, updateFlags);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
        return this.delegate.removeBlock(pos, movedByPiston);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropResources) {
        return this.delegate.destroyBlock(pos, dropResources);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker) {
        return this.delegate.destroyBlock(pos, dropResources, breaker);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
        return this.delegate.destroyBlock(pos, dropResources, breaker, updateLimit);
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return this.delegate.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity entity, CreatureSpawnEvent.@Nullable SpawnReason reason) {
        return this.delegate.addFreshEntity(entity, reason);
    }

    @Override
    public int getMaxY() {
        return this.delegate.getMaxY();
    }

    @Override
    public int getSectionsCount() {
        return this.delegate.getSectionsCount();
    }

    @Override
    public int getMinSectionY() {
        return this.delegate.getMinSectionY();
    }

    @Override
    public int getMaxSectionY() {
        return this.delegate.getMaxSectionY();
    }

    @Override
    public boolean isInsideBuildHeight(int blockY) {
        return this.delegate.isInsideBuildHeight(blockY);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.delegate.isOutsideBuildHeight(pos);
    }

    @Override
    public boolean isOutsideBuildHeight(int blockY) {
        return this.delegate.isOutsideBuildHeight(blockY);
    }

    @Override
    public int getSectionIndex(int blockY) {
        return this.delegate.getSectionIndex(blockY);
    }

    @Override
    public int getSectionIndexFromSectionY(int sectionY) {
        return this.delegate.getSectionIndexFromSectionY(sectionY);
    }

    @Override
    public int getSectionYFromSectionIndex(int sectionIndex) {
        return this.delegate.getSectionYFromSectionIndex(sectionIndex);
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
        return this.delegate.isStateAtPosition(pos, predicate);
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
        return this.delegate.isFluidAtPosition(pos, predicate);
    }

    @Nullable
    @Override
    public BlockState getBlockStateIfLoaded(final BlockPos pos) {
        return this.delegate.getBlockStateIfLoaded(pos);
    }

    @Nullable
    @Override
    public FluidState getFluidIfLoaded(final BlockPos pos) {
        return this.delegate.getFluidIfLoaded(pos);
    }

    @Nullable
    @Override
    public ChunkAccess getChunkIfLoadedImmediately(final int x, final int z) {
        return this.delegate.getChunkIfLoadedImmediately(x, z);
    }
}
