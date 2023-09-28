package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.particles.ParticleParam;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.IBlockAccess;
import net.minecraft.world.level.RayTrace;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.TileEntity;
import net.minecraft.world.level.block.entity.TileEntityTypes;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.dimension.DimensionManager;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidType;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.MovingObjectPositionBlock;
import net.minecraft.world.phys.Vec3D;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.VoxelShapeCollision;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.NextTickListEntry;
import net.minecraft.world.ticks.TickListPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;

public abstract class DelegatedGeneratorAccess implements GeneratorAccessSeed {

    private GeneratorAccessSeed handle;

    public void setHandle(GeneratorAccessSeed worldAccess) {
        this.handle = worldAccess;
    }

    public GeneratorAccessSeed getHandle() {
        return handle;
    }

    @Override
    public long getSeed() {
        return handle.getSeed();
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> arg0) {
        handle.setCurrentlyGenerating(arg0);
    }

    @Override
    public boolean ensureCanWrite(BlockPosition arg0) {
        return handle.ensureCanWrite(arg0);
    }

    @Override
    public WorldServer getLevel() {
        return handle.getLevel();
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        handle.addFreshEntityWithPassengers(arg0, arg1);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity arg0) {
        handle.addFreshEntityWithPassengers(arg0);
    }

    @Override
    public WorldServer getMinecraftWorld() {
        return handle.getMinecraftWorld();
    }

    @Override
    public DifficultyDamageScaler getCurrentDifficultyAt(BlockPosition arg0) {
        return handle.getCurrentDifficultyAt(arg0);
    }

    @Override
    public void neighborShapeChanged(EnumDirection arg0, IBlockData arg1, BlockPosition arg2, BlockPosition arg3, int arg4, int arg5) {
        handle.neighborShapeChanged(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public long dayTime() {
        return handle.dayTime();
    }

    @Override
    public WorldData getLevelData() {
        return handle.getLevelData();
    }

    @Override
    public boolean hasChunk(int arg0, int arg1) {
        return handle.hasChunk(arg0, arg1);
    }

    @Override
    public IChunkProvider getChunkSource() {
        return handle.getChunkSource();
    }

    @Override
    public void scheduleTick(BlockPosition arg0, Block arg1, int arg2, TickListPriority arg3) {
        handle.scheduleTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public void scheduleTick(BlockPosition arg0, Block arg1, int arg2) {
        handle.scheduleTick(arg0, arg1, arg2);
    }

    @Override
    public void scheduleTick(BlockPosition arg0, FluidType arg1, int arg2, TickListPriority arg3) {
        handle.scheduleTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public void scheduleTick(BlockPosition arg0, FluidType arg1, int arg2) {
        handle.scheduleTick(arg0, arg1, arg2);
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return handle.getDifficulty();
    }

    @Override
    public void blockUpdated(BlockPosition arg0, Block arg1) {
        handle.blockUpdated(arg0, arg1);
    }

    @Override
    public MinecraftServer getServer() {
        return handle.getServer();
    }

    @Override
    public RandomSource getRandom() {
        return handle.getRandom();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return handle.getBlockTicks();
    }

    @Override
    public long nextSubTickCount() {
        return handle.nextSubTickCount();
    }

    @Override
    public <T> NextTickListEntry<T> createTick(BlockPosition arg0, T arg1, int arg2) {
        return handle.createTick(arg0, arg1, arg2);
    }

    @Override
    public <T> NextTickListEntry<T> createTick(BlockPosition arg0, T arg1, int arg2, TickListPriority arg3) {
        return handle.createTick(arg0, arg1, arg2, arg3);
    }

    @Override
    public LevelTickAccess<FluidType> getFluidTicks() {
        return handle.getFluidTicks();
    }

    @Override
    public void playSound(EntityHuman arg0, BlockPosition arg1, SoundEffect arg2, SoundCategory arg3) {
        handle.playSound(arg0, arg1, arg2, arg3);
    }

    @Override
    public void playSound(EntityHuman arg0, BlockPosition arg1, SoundEffect arg2, SoundCategory arg3, float arg4, float arg5) {
        handle.playSound(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public void levelEvent(int arg0, BlockPosition arg1, int arg2) {
        handle.levelEvent(arg0, arg1, arg2);
    }

    @Override
    public void levelEvent(EntityHuman arg0, int arg1, BlockPosition arg2, int arg3) {
        handle.levelEvent(arg0, arg1, arg2, arg3);
    }

    @Override
    public void addParticle(ParticleParam arg0, double arg1, double arg2, double arg3, double arg4, double arg5, double arg6) {
        handle.addParticle(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public void gameEvent(GameEvent arg0, Vec3D arg1, GameEvent.a arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(GameEvent arg0, BlockPosition arg1, GameEvent.a arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(Entity arg0, GameEvent arg1, BlockPosition arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public void gameEvent(Entity arg0, GameEvent arg1, Vec3D arg2) {
        handle.gameEvent(arg0, arg1, arg2);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity arg0, AxisAlignedBB arg1) {
        return handle.getEntityCollisions(arg0, arg1);
    }

    @Override
    public <T extends TileEntity> Optional<T> getBlockEntity(BlockPosition arg0, TileEntityTypes<T> arg1) {
        return handle.getBlockEntity(arg0, arg1);
    }

    @Override
    public BlockPosition getHeightmapPos(HeightMap.Type arg0, BlockPosition arg1) {
        return handle.getHeightmapPos(arg0, arg1);
    }

    @Override
    public boolean isUnobstructed(Entity arg0, VoxelShape arg1) {
        return handle.isUnobstructed(arg0, arg1);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double arg0, double arg1, double arg2, double arg3) {
        return handle.hasNearbyAlivePlayer(arg0, arg1, arg2, arg3);
    }

    @Override
    public List<? extends EntityHuman> players() {
        return handle.players();
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AxisAlignedBB arg1, Predicate<? super Entity> arg2) {
        return handle.getEntities(arg0, arg1, arg2);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> arg0, AxisAlignedBB arg1, Predicate<? super T> arg2) {
        return handle.getEntities(arg0, arg1, arg2);
    }

    @Override
    public List<Entity> getEntities(Entity arg0, AxisAlignedBB arg1) {
        return handle.getEntities(arg0, arg1);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AxisAlignedBB arg1) {
        return handle.getEntitiesOfClass(arg0, arg1);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> arg0, AxisAlignedBB arg1, Predicate<? super T> arg2) {
        return handle.getEntitiesOfClass(arg0, arg1, arg2);
    }

    @Override
    public EntityHuman getNearestPlayer(PathfinderTargetCondition arg0, EntityLiving arg1, double arg2, double arg3, double arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public EntityHuman getNearestPlayer(PathfinderTargetCondition arg0, double arg1, double arg2, double arg3) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3);
    }

    @Override
    public EntityHuman getNearestPlayer(Entity arg0, double arg1) {
        return handle.getNearestPlayer(arg0, arg1);
    }

    @Override
    public EntityHuman getNearestPlayer(double arg0, double arg1, double arg2, double arg3, Predicate<Entity> arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public EntityHuman getNearestPlayer(double arg0, double arg1, double arg2, double arg3, boolean arg4) {
        return handle.getNearestPlayer(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public EntityHuman getNearestPlayer(PathfinderTargetCondition arg0, EntityLiving arg1) {
        return handle.getNearestPlayer(arg0, arg1);
    }

    @Override
    public <T extends EntityLiving> T getNearestEntity(Class<? extends T> arg0, PathfinderTargetCondition arg1, EntityLiving arg2, double arg3, double arg4, double arg5, AxisAlignedBB arg6) {
        return handle.getNearestEntity(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    @Override
    public <T extends EntityLiving> T getNearestEntity(List<? extends T> arg0, PathfinderTargetCondition arg1, EntityLiving arg2, double arg3, double arg4, double arg5) {
        return handle.getNearestEntity(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public EntityHuman getPlayerByUUID(UUID arg0) {
        return handle.getPlayerByUUID(arg0);
    }

    @Override
    public List<EntityHuman> getNearbyPlayers(PathfinderTargetCondition arg0, EntityLiving arg1, AxisAlignedBB arg2) {
        return handle.getNearbyPlayers(arg0, arg1, arg2);
    }

    @Override
    public <T extends EntityLiving> List<T> getNearbyEntities(Class<T> arg0, PathfinderTargetCondition arg1, EntityLiving arg2, AxisAlignedBB arg3) {
        return handle.getNearbyEntities(arg0, arg1, arg2, arg3);
    }

    @Override
    @Deprecated
    public float getLightLevelDependentMagicValue(BlockPosition arg0) {
        return handle.getLightLevelDependentMagicValue(arg0);
    }

    @Override
    public IBlockAccess getChunkForCollisions(int arg0, int arg1) {
        return handle.getChunkForCollisions(arg0, arg1);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPosition arg0) {
        return handle.getMaxLocalRawBrightness(arg0);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPosition arg0, int arg1) {
        return handle.getMaxLocalRawBrightness(arg0, arg1);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPosition arg0) {
        return handle.canSeeSkyFromBelowWater(arg0);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPosition arg0) {
        return handle.getPathfindingCostFromLightLevels(arg0);
    }

    @Override
    public Stream<IBlockData> getBlockStatesIfLoaded(AxisAlignedBB arg0) {
        return handle.getBlockStatesIfLoaded(arg0);
    }

    @Override
    public Holder<BiomeBase> getUncachedNoiseBiome(int arg0, int arg1, int arg2) {
        return handle.getUncachedNoiseBiome(arg0, arg1, arg2);
    }

    @Override
    @Deprecated
    public int getSeaLevel() {
        return handle.getSeaLevel();
    }

    @Override
    public boolean containsAnyLiquid(AxisAlignedBB arg0) {
        return handle.containsAnyLiquid(arg0);
    }

    @Override
    public int getMinBuildHeight() {
        return handle.getMinBuildHeight();
    }

    @Override
    public boolean isWaterAt(BlockPosition arg0) {
        return handle.isWaterAt(arg0);
    }

    @Override
    public boolean isEmptyBlock(BlockPosition arg0) {
        return handle.isEmptyBlock(arg0);
    }

    @Override
    public boolean isClientSide() {
        return handle.isClientSide();
    }

    @Override
    public DimensionManager dimensionType() {
        return handle.dimensionType();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return handle.enabledFeatures();
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(int arg0, int arg1) {
        return handle.hasChunkAt(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunkAt(BlockPosition arg0) {
        return handle.hasChunkAt(arg0);
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends IRegistry<? extends T>> arg0) {
        return handle.holderLookup(arg0);
    }

    @Override
    public IRegistryCustom registryAccess() {
        return handle.registryAccess();
    }

    @Override
    public Holder<BiomeBase> getNoiseBiome(int arg0, int arg1, int arg2) {
        return handle.getNoiseBiome(arg0, arg1, arg2);
    }

    @Override
    public int getBlockTint(BlockPosition arg0, ColorResolver arg1) {
        return handle.getBlockTint(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(BlockPosition arg0, BlockPosition arg1) {
        return handle.hasChunksAt(arg0, arg1);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) {
        return handle.hasChunksAt(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    @Deprecated
    public boolean hasChunksAt(int arg0, int arg1, int arg2, int arg3) {
        return handle.hasChunksAt(arg0, arg1, arg2, arg3);
    }

    @Override
    public IChunkAccess getChunk(int arg0, int arg1, ChunkStatus arg2, boolean arg3) {
        return handle.getChunk(arg0, arg1, arg2, arg3);
    }

    @Override
    public IChunkAccess getChunk(int arg0, int arg1, ChunkStatus arg2) {
        return handle.getChunk(arg0, arg1, arg2);
    }

    @Override
    public IChunkAccess getChunk(BlockPosition arg0) {
        return handle.getChunk(arg0);
    }

    @Override
    public IChunkAccess getChunk(int arg0, int arg1) {
        return handle.getChunk(arg0, arg1);
    }

    @Override
    public int getHeight(HeightMap.Type arg0, int arg1, int arg2) {
        return handle.getHeight(arg0, arg1, arg2);
    }

    @Override
    public int getHeight() {
        return handle.getHeight();
    }

    @Override
    public Holder<BiomeBase> getBiome(BlockPosition arg0) {
        return handle.getBiome(arg0);
    }

    @Override
    public int getSkyDarken() {
        return handle.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return handle.getBiomeManager();
    }

    @Override
    public boolean canSeeSky(BlockPosition arg0) {
        return handle.canSeeSky(arg0);
    }

    @Override
    public int getRawBrightness(BlockPosition arg0, int arg1) {
        return handle.getRawBrightness(arg0, arg1);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return handle.getLightEngine();
    }

    @Override
    public int getBrightness(EnumSkyBlock arg0, BlockPosition arg1) {
        return handle.getBrightness(arg0, arg1);
    }

    @Override
    public float getShade(EnumDirection arg0, boolean arg1) {
        return handle.getShade(arg0, arg1);
    }

    @Override
    public TileEntity getBlockEntity(BlockPosition arg0) {
        return handle.getBlockEntity(arg0);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape arg0, Supplier<VoxelShape> arg1) {
        return handle.getBlockFloorHeight(arg0, arg1);
    }

    @Override
    public double getBlockFloorHeight(BlockPosition arg0) {
        return handle.getBlockFloorHeight(arg0);
    }

    @Override
    public MovingObjectPositionBlock clipWithInteractionOverride(Vec3D arg0, Vec3D arg1, BlockPosition arg2, VoxelShape arg3, IBlockData arg4) {
        return handle.clipWithInteractionOverride(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public IBlockData getBlockState(BlockPosition arg0) {
        return handle.getBlockState(arg0);
    }

    @Override
    public Fluid getFluidState(BlockPosition arg0) {
        return handle.getFluidState(arg0);
    }

    @Override
    public int getLightEmission(BlockPosition arg0) {
        return handle.getLightEmission(arg0);
    }

    @Override
    public MovingObjectPositionBlock clip(RayTrace arg0) {
        return handle.clip(arg0);
    }

    @Override
    public MovingObjectPositionBlock clip(RayTrace arg0, BlockPosition arg1) {
        return handle.clip(arg0, arg1);
    }

    @Override
    public int getMaxLightLevel() {
        return handle.getMaxLightLevel();
    }

    @Override
    public MovingObjectPositionBlock isBlockInLine(ClipBlockStateContext arg0) {
        return handle.isBlockInLine(arg0);
    }

    @Override
    public Stream<IBlockData> getBlockStates(AxisAlignedBB arg0) {
        return handle.getBlockStates(arg0);
    }

    @Override
    public boolean isOutsideBuildHeight(int arg0) {
        return handle.isOutsideBuildHeight(arg0);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPosition arg0) {
        return handle.isOutsideBuildHeight(arg0);
    }

    @Override
    public int getSectionIndexFromSectionY(int arg0) {
        return handle.getSectionIndexFromSectionY(arg0);
    }

    @Override
    public int getSectionYFromSectionIndex(int arg0) {
        return handle.getSectionYFromSectionIndex(arg0);
    }

    @Override
    public int getMaxSection() {
        return handle.getMaxSection();
    }

    @Override
    public int getMinSection() {
        return handle.getMinSection();
    }

    @Override
    public int getSectionIndex(int arg0) {
        return handle.getSectionIndex(arg0);
    }

    @Override
    public int getSectionsCount() {
        return handle.getSectionsCount();
    }

    @Override
    public int getMaxBuildHeight() {
        return handle.getMaxBuildHeight();
    }

    @Override
    public boolean isUnobstructed(IBlockData arg0, BlockPosition arg1, VoxelShapeCollision arg2) {
        return handle.isUnobstructed(arg0, arg1, arg2);
    }

    @Override
    public boolean isUnobstructed(Entity arg0) {
        return handle.isUnobstructed(arg0);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return handle.getWorldBorder();
    }

    @Override
    public Optional<Vec3D> findFreePosition(Entity arg0, VoxelShape arg1, Vec3D arg2, double arg3, double arg4, double arg5) {
        return handle.findFreePosition(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity arg0, AxisAlignedBB arg1) {
        return handle.getCollisions(arg0, arg1);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity arg0, AxisAlignedBB arg1) {
        return handle.getBlockCollisions(arg0, arg1);
    }

    @Override
    public boolean noCollision(AxisAlignedBB arg0) {
        return handle.noCollision(arg0);
    }

    @Override
    public boolean noCollision(Entity arg0) {
        return handle.noCollision(arg0);
    }

    @Override
    public boolean noCollision(Entity arg0, AxisAlignedBB arg1) {
        return handle.noCollision(arg0, arg1);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity arg0, AxisAlignedBB arg1) {
        return handle.collidesWithSuffocatingBlock(arg0, arg1);
    }

    @Override
    public Optional<BlockPosition> findSupportingBlock(Entity arg0, AxisAlignedBB arg1) {
        return handle.findSupportingBlock(arg0, arg1);
    }

    @Override
    public int getBestNeighborSignal(BlockPosition arg0) {
        return handle.getBestNeighborSignal(arg0);
    }

    @Override
    public int getControlInputSignal(BlockPosition arg0, EnumDirection arg1, boolean arg2) {
        return handle.getControlInputSignal(arg0, arg1, arg2);
    }

    @Override
    public int getDirectSignal(BlockPosition arg0, EnumDirection arg1) {
        return handle.getDirectSignal(arg0, arg1);
    }

    @Override
    public int getDirectSignalTo(BlockPosition arg0) {
        return handle.getDirectSignalTo(arg0);
    }

    @Override
    public boolean hasNeighborSignal(BlockPosition arg0) {
        return handle.hasNeighborSignal(arg0);
    }

    @Override
    public boolean hasSignal(BlockPosition arg0, EnumDirection arg1) {
        return handle.hasSignal(arg0, arg1);
    }

    @Override
    public int getSignal(BlockPosition arg0, EnumDirection arg1) {
        return handle.getSignal(arg0, arg1);
    }

    @Override
    public boolean isStateAtPosition(BlockPosition arg0, Predicate<IBlockData> arg1) {
        return handle.isStateAtPosition(arg0, arg1);
    }

    @Override
    public boolean isFluidAtPosition(BlockPosition arg0, Predicate<Fluid> arg1) {
        return handle.isFluidAtPosition(arg0, arg1);
    }

    @Override
    public boolean addFreshEntity(Entity arg0, CreatureSpawnEvent.SpawnReason arg1) {
        return handle.addFreshEntity(arg0, arg1);
    }

    @Override
    public boolean addFreshEntity(Entity arg0) {
        return handle.addFreshEntity(arg0);
    }

    @Override
    public boolean removeBlock(BlockPosition arg0, boolean arg1) {
        return handle.removeBlock(arg0, arg1);
    }

    @Override
    public boolean destroyBlock(BlockPosition arg0, boolean arg1, Entity arg2, int arg3) {
        return handle.destroyBlock(arg0, arg1, arg2, arg3);
    }

    @Override
    public boolean destroyBlock(BlockPosition arg0, boolean arg1, Entity arg2) {
        return handle.destroyBlock(arg0, arg1, arg2);
    }

    @Override
    public boolean destroyBlock(BlockPosition arg0, boolean arg1) {
        return handle.destroyBlock(arg0, arg1);
    }

    @Override
    public boolean setBlock(BlockPosition arg0, IBlockData arg1, int arg2) {
        return handle.setBlock(arg0, arg1, arg2);
    }

    @Override
    public boolean setBlock(BlockPosition arg0, IBlockData arg1, int arg2, int arg3) {
        return handle.setBlock(arg0, arg1, arg2, arg3);
    }

    @Override
    public float getTimeOfDay(float arg0) {
        return handle.getTimeOfDay(arg0);
    }

    @Override
    public float getMoonBrightness() {
        return handle.getMoonBrightness();
    }

    @Override
    public int getMoonPhase() {
        return handle.getMoonPhase();
    }
}
