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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ColorResolver;
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

public abstract class DelegatedGeneratorAccess implements WorldGenLevel {

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
    public void setCurrentlyGenerating(Supplier<String> structureName) {
        this.delegate.setCurrentlyGenerating(structureName);
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
    public long dayTime() {
        return this.delegate.dayTime();
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
    public void scheduleTick(BlockPos pos, Block block, int delay, TickPriority priority) {
        this.delegate.scheduleTick(pos, block, delay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Block block, int delay) {
        this.delegate.scheduleTick(pos, block, delay);
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.delegate.getFluidTicks();
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid fluid, int delay, TickPriority priority) {
        this.delegate.scheduleTick(pos, fluid, delay, priority);
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid fluid, int delay) {
        this.delegate.scheduleTick(pos, fluid, delay);
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
    public void updateNeighborsAt(BlockPos pos, Block block) {
        this.delegate.updateNeighborsAt(pos, block);
    }

    @Override
    public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, int flags, int recursionLeft) {
        this.delegate.neighborShapeChanged(direction, pos, neighborPos, neighborState, flags, recursionLeft);
    }

    @Override
    public void playSound(@Nullable final Entity entity, final BlockPos pos, final SoundEvent sound, final SoundSource source, final float volume, final float pitch) {
        this.delegate.playSound(entity, pos, sound, source, volume, pitch);
    }

    @Override
    public void addParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.delegate.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ);
    }

    @Override
    public void levelEvent(Entity entity, int eventId, BlockPos pos, int data) {
        this.delegate.levelEvent(entity, eventId, pos, data);
    }

    @Override
    public void levelEvent(int eventId, BlockPos pos, int data) {
        this.delegate.levelEvent(eventId, pos, data);
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, Vec3 pos, GameEvent.Context context) {
        this.delegate.gameEvent(gameEvent, pos, context);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> gameEvent, Vec3 pos) {
        this.delegate.gameEvent(entity, gameEvent, pos);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> gameEvent, BlockPos pos) {
        this.delegate.gameEvent(entity, gameEvent, pos);
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, BlockPos pos, GameEvent.Context emitter) {
        this.delegate.gameEvent(gameEvent, pos, emitter);
    }

    @Override
    public void gameEvent(ResourceKey<GameEvent> gameEvent, BlockPos pos, GameEvent.Context emitter) {
        this.delegate.gameEvent(gameEvent, pos, emitter);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
        return this.delegate.getBlockEntity(pos, type);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity entity, AABB box) {
        return this.delegate.getEntityCollisions(entity, box);
    }

    @Override
    public boolean isUnobstructed(Entity except, VoxelShape shape) {
        return this.delegate.isUnobstructed(except, shape);
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types heightmap, BlockPos pos) {
        return this.delegate.getHeightmapPos(heightmap, pos);
    }

    @Override
    public float getMoonBrightness() {
        return this.delegate.getMoonBrightness();
    }

    @Override
    public float getTimeOfDay(float tickDelta) {
        return this.delegate.getTimeOfDay(tickDelta);
    }

    @Override
    public int getMoonPhase() {
        return this.delegate.getMoonPhase();
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
        return this.delegate.getChunk(chunkX, chunkZ, leastStatus, create);
    }

    @Override
    public int getHeight(Heightmap.Types heightmap, int x, int z) {
        return this.delegate.getHeight(heightmap, x, z);
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
    public int getBlockTint(BlockPos pos, ColorResolver colorResolver) {
        return this.delegate.getBlockTint(pos, colorResolver);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return this.delegate.getNoiseBiome(biomeX, biomeY, biomeZ);
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return this.delegate.getUncachedNoiseBiome(biomeX, biomeY, biomeZ);
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
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus chunkStatus) {
        return this.delegate.getChunk(chunkX, chunkZ, chunkStatus);
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
    public int getMaxLocalRawBrightness(BlockPos pos, int ambientDarkness) {
        return this.delegate.getMaxLocalRawBrightness(pos, ambientDarkness);
    }

    @Override
    public boolean hasChunkAt(int x, int z) {
        return this.delegate.hasChunkAt(x, z);
    }

    @Override
    public boolean hasChunkAt(BlockPos pos) {
        return this.delegate.hasChunkAt(pos);
    }

    @Override
    public boolean hasChunksAt(BlockPos from, BlockPos to) {
        return this.delegate.hasChunksAt(from, to);
    }

    @Override
    public boolean hasChunksAt(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        return this.delegate.hasChunksAt(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean hasChunksAt(int minX, int minZ, int maxX, int maxZ) {
        return this.delegate.hasChunksAt(minX, minZ, maxX, maxZ);
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
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> registryKey) {
        return this.delegate.holderLookup(registryKey);
    }

    @Override
    public float getShade(Direction direction, boolean shade) {
        return this.delegate.getShade(direction, shade);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.delegate.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        return this.delegate.getBrightness(type, pos);
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkness) {
        return this.delegate.getRawBrightness(pos, ambientDarkness);
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
    public boolean isUnobstructed(Entity entity) {
        return this.delegate.isUnobstructed(entity);
    }

    @Override
    public boolean noCollision(AABB collisionBox) {
        return this.delegate.noCollision(collisionBox);
    }

    @Override
    public boolean noCollision(Entity entity) {
        return this.delegate.noCollision(entity);
    }

    @Override
    public boolean noCollision(Entity entity, AABB collisionBox) {
        return this.delegate.noCollision(entity, collisionBox);
    }

    @Override
    public boolean noCollision(Entity entity, AABB collisionBox, boolean checkFluid) {
        return this.delegate.noCollision(entity, collisionBox, checkFluid);
    }

    @Override
    public boolean noBlockCollision(Entity entity, AABB collisionBox) {
        return this.delegate.noBlockCollision(entity, collisionBox);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity entity, AABB collisionBox) {
        return this.delegate.getCollisions(entity, collisionBox);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity entity, AABB collisionBox) {
        return this.delegate.getBlockCollisions(entity, collisionBox);
    }

    @Override
    public Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity entity, AABB collisionBox) {
        return this.delegate.getBlockAndLiquidCollisions(entity, collisionBox);
    }

    @Override
    public BlockHitResult clipIncludingBorder(ClipContext clipContext) {
        return this.delegate.clipIncludingBorder(clipContext);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity entity, AABB box) {
        return this.delegate.collidesWithSuffocatingBlock(entity, box);
    }

    @Override
    public Optional<BlockPos> findSupportingBlock(Entity entity, AABB box) {
        return this.delegate.findSupportingBlock(entity, box);
    }

    @Override
    public Optional<Vec3> findFreePosition(Entity entity, VoxelShape shape, Vec3 pos, double x, double y, double z) {
        return this.delegate.findFreePosition(entity, shape, pos, x, y, z);
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
    public int getControlInputSignal(BlockPos pos, Direction direction, boolean diodesOnly) {
        return this.delegate.getControlInputSignal(pos, direction, diodesOnly);
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
    public boolean hasNeighborSignal(BlockPos pos) {
        return this.delegate.hasNeighborSignal(pos);
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
    public Stream<BlockState> getBlockStates(AABB area) {
        return this.delegate.getBlockStates(area);
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext context) {
        return this.delegate.isBlockInLine(context);
    }

    @Override
    public BlockHitResult clip(ClipContext traverseContext, BlockPos traversePos) {
        return this.delegate.clip(traverseContext, traversePos);
    }

    @Override
    public BlockHitResult clip(ClipContext context) {
        return this.delegate.clip(context);
    }

    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 startVec, Vec3 endVec, BlockPos pos, VoxelShape shape, BlockState state) {
        return this.delegate.clipWithInteractionOverride(startVec, endVec, pos, shape, state);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape shape, Supplier<VoxelShape> belowShapeSupplier) {
        return this.delegate.getBlockFloorHeight(shape, belowShapeSupplier);
    }

    @Override
    public double getBlockFloorHeight(BlockPos pos) {
        return this.delegate.getBlockFloorHeight(pos);
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB area, Predicate<? super Entity> predicate) {
        return this.delegate.getEntities(except, area, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTest, AABB bounds, Predicate<? super T> predicate) {
        return this.delegate.getEntities(entityTypeTest, bounds, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> entityClass, AABB area, Predicate<? super T> filter) {
        return this.delegate.getEntitiesOfClass(entityClass, area, filter);
    }

    @Override
    public List<? extends Player> players() {
        return this.delegate.players();
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB area) {
        return this.delegate.getEntities(except, area);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> entityClass, AABB area) {
        return this.delegate.getEntitiesOfClass(entityClass, area);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double maxDistance, Predicate<Entity> targetPredicate) {
        return this.delegate.getNearestPlayer(x, y, z, maxDistance, targetPredicate);
    }

    @Override
    public Player getNearestPlayer(Entity entity, double maxDistance) {
        return this.delegate.getNearestPlayer(entity, maxDistance);
    }

    @Override
    public Player getNearestPlayer(double x, double y, double z, double maxDistance, boolean ignoreCreative) {
        return this.delegate.getNearestPlayer(x, y, z, maxDistance, ignoreCreative);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double x, double y, double z, double distance) {
        return this.delegate.hasNearbyAlivePlayer(x, y, z, distance);
    }

    @Override
    public Player getPlayerByUUID(UUID uuid) {
        return this.delegate.getPlayerByUUID(uuid);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int recursionLeft) {
        return this.delegate.setBlock(pos, state, flags, recursionLeft);
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags) {
        return this.delegate.setBlock(pos, state, flags);
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean isMoving) {
        return this.delegate.removeBlock(pos, isMoving);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
        return this.delegate.destroyBlock(pos, dropBlock);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity) {
        return this.delegate.destroyBlock(pos, dropBlock, entity);
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropBlock, Entity entity, int recursionLeft) {
        return this.delegate.destroyBlock(pos, dropBlock, entity, recursionLeft);
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return this.delegate.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity entity, @Nullable CreatureSpawnEvent.SpawnReason reason) {
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
    public boolean isInsideBuildHeight(int y) {
        return this.delegate.isInsideBuildHeight(y);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos pos) {
        return this.delegate.isOutsideBuildHeight(pos);
    }

    @Override
    public boolean isOutsideBuildHeight(int y) {
        return this.delegate.isOutsideBuildHeight(y);
    }

    @Override
    public int getSectionIndex(int y) {
        return this.delegate.getSectionIndex(y);
    }

    @Override
    public int getSectionIndexFromSectionY(int sectionIndex) {
        return this.delegate.getSectionIndexFromSectionY(sectionIndex);
    }

    @Override
    public int getSectionYFromSectionIndex(int sectionIndex) {
        return this.delegate.getSectionYFromSectionIndex(sectionIndex);
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state) {
        return this.delegate.isStateAtPosition(pos, state);
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

