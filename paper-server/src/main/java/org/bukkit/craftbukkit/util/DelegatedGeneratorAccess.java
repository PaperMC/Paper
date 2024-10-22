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
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.IChunkProvider;
import net.minecraft.world.level.chunk.status.ChunkStatus;
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
    public boolean ensureCanWrite(BlockPosition var0) {
        return handle.ensureCanWrite(var0);
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> var0) {
        handle.setCurrentlyGenerating(var0);
    }

    @Override
    public WorldServer getLevel() {
        return handle.getLevel();
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity) {
        handle.addFreshEntityWithPassengers(entity);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        handle.addFreshEntityWithPassengers(entity, reason);
    }

    @Override
    public WorldServer getMinecraftWorld() {
        return handle.getMinecraftWorld();
    }

    @Override
    public long dayTime() {
        return handle.dayTime();
    }

    @Override
    public long nextSubTickCount() {
        return handle.nextSubTickCount();
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return handle.getBlockTicks();
    }

    @Override
    public void scheduleTick(BlockPosition blockposition, Block block, int i, TickListPriority ticklistpriority) {
        handle.scheduleTick(blockposition, block, i, ticklistpriority);
    }

    @Override
    public void scheduleTick(BlockPosition blockposition, Block block, int i) {
        handle.scheduleTick(blockposition, block, i);
    }

    @Override
    public LevelTickAccess<FluidType> getFluidTicks() {
        return handle.getFluidTicks();
    }

    @Override
    public void scheduleTick(BlockPosition blockposition, FluidType fluidtype, int i, TickListPriority ticklistpriority) {
        handle.scheduleTick(blockposition, fluidtype, i, ticklistpriority);
    }

    @Override
    public void scheduleTick(BlockPosition blockposition, FluidType fluidtype, int i) {
        handle.scheduleTick(blockposition, fluidtype, i);
    }

    @Override
    public WorldData getLevelData() {
        return handle.getLevelData();
    }

    @Override
    public DifficultyDamageScaler getCurrentDifficultyAt(BlockPosition blockposition) {
        return handle.getCurrentDifficultyAt(blockposition);
    }

    @Override
    public MinecraftServer getServer() {
        return handle.getServer();
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return handle.getDifficulty();
    }

    @Override
    public IChunkProvider getChunkSource() {
        return handle.getChunkSource();
    }

    @Override
    public boolean hasChunk(int i, int j) {
        return handle.hasChunk(i, j);
    }

    @Override
    public RandomSource getRandom() {
        return handle.getRandom();
    }

    @Override
    public void blockUpdated(BlockPosition blockposition, Block block) {
        handle.blockUpdated(blockposition, block);
    }

    @Override
    public void neighborShapeChanged(EnumDirection enumdirection, BlockPosition blockposition, BlockPosition blockposition1, IBlockData iblockdata, int i, int j) {
        handle.neighborShapeChanged(enumdirection, blockposition, blockposition1, iblockdata, i, j);
    }

    @Override
    public void playSound(EntityHuman entityhuman, BlockPosition blockposition, SoundEffect soundeffect, SoundCategory soundcategory) {
        handle.playSound(entityhuman, blockposition, soundeffect, soundcategory);
    }

    @Override
    public void playSound(EntityHuman entityhuman, BlockPosition blockposition, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        handle.playSound(entityhuman, blockposition, soundeffect, soundcategory, f, f1);
    }

    @Override
    public void addParticle(ParticleParam particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {
        handle.addParticle(particleparam, d0, d1, d2, d3, d4, d5);
    }

    @Override
    public void levelEvent(EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {
        handle.levelEvent(entityhuman, i, blockposition, j);
    }

    @Override
    public void levelEvent(int i, BlockPosition blockposition, int j) {
        handle.levelEvent(i, blockposition, j);
    }

    @Override
    public void gameEvent(Holder<GameEvent> holder, Vec3D vec3d, GameEvent.a gameevent_a) {
        handle.gameEvent(holder, vec3d, gameevent_a);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> holder, Vec3D vec3d) {
        handle.gameEvent(entity, holder, vec3d);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> holder, BlockPosition blockposition) {
        handle.gameEvent(entity, holder, blockposition);
    }

    @Override
    public void gameEvent(Holder<GameEvent> holder, BlockPosition blockposition, GameEvent.a gameevent_a) {
        handle.gameEvent(holder, blockposition, gameevent_a);
    }

    @Override
    public void gameEvent(ResourceKey<GameEvent> resourcekey, BlockPosition blockposition, GameEvent.a gameevent_a) {
        handle.gameEvent(resourcekey, blockposition, gameevent_a);
    }

    @Override
    public <T extends TileEntity> Optional<T> getBlockEntity(BlockPosition var0, TileEntityTypes<T> var1) {
        return handle.getBlockEntity(var0, var1);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity var0, AxisAlignedBB var1) {
        return handle.getEntityCollisions(var0, var1);
    }

    @Override
    public boolean isUnobstructed(Entity var0, VoxelShape var1) {
        return handle.isUnobstructed(var0, var1);
    }

    @Override
    public BlockPosition getHeightmapPos(HeightMap.Type var0, BlockPosition var1) {
        return handle.getHeightmapPos(var0, var1);
    }

    @Override
    public float getMoonBrightness() {
        return handle.getMoonBrightness();
    }

    @Override
    public float getTimeOfDay(float var0) {
        return handle.getTimeOfDay(var0);
    }

    @Override
    public int getMoonPhase() {
        return handle.getMoonPhase();
    }

    @Override
    public IChunkAccess getChunk(int i, int i1, ChunkStatus cs, boolean bln) {
        return handle.getChunk(i, i1, cs, bln);
    }

    @Override
    public int getHeight(HeightMap.Type type, int i, int i1) {
        return handle.getHeight(type, i, i1);
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
    public Holder<BiomeBase> getBiome(BlockPosition var0) {
        return handle.getBiome(var0);
    }

    @Override
    public Stream<IBlockData> getBlockStatesIfLoaded(AxisAlignedBB var0) {
        return handle.getBlockStatesIfLoaded(var0);
    }

    @Override
    public int getBlockTint(BlockPosition var0, ColorResolver var1) {
        return handle.getBlockTint(var0, var1);
    }

    @Override
    public Holder<BiomeBase> getNoiseBiome(int var0, int var1, int var2) {
        return handle.getNoiseBiome(var0, var1, var2);
    }

    @Override
    public Holder<BiomeBase> getUncachedNoiseBiome(int i, int i1, int i2) {
        return handle.getUncachedNoiseBiome(i, i1, i2);
    }

    @Override
    public boolean isClientSide() {
        return handle.isClientSide();
    }

    @Override
    public int getSeaLevel() {
        return handle.getSeaLevel();
    }

    @Override
    public DimensionManager dimensionType() {
        return handle.dimensionType();
    }

    @Override
    public int getMinY() {
        return handle.getMinY();
    }

    @Override
    public int getHeight() {
        return handle.getHeight();
    }

    @Override
    public boolean isEmptyBlock(BlockPosition var0) {
        return handle.isEmptyBlock(var0);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPosition var0) {
        return handle.canSeeSkyFromBelowWater(var0);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPosition var0) {
        return handle.getPathfindingCostFromLightLevels(var0);
    }

    @Override
    public float getLightLevelDependentMagicValue(BlockPosition var0) {
        return handle.getLightLevelDependentMagicValue(var0);
    }

    @Override
    public IChunkAccess getChunk(BlockPosition var0) {
        return handle.getChunk(var0);
    }

    @Override
    public IChunkAccess getChunk(int var0, int var1) {
        return handle.getChunk(var0, var1);
    }

    @Override
    public IChunkAccess getChunk(int var0, int var1, ChunkStatus var2) {
        return handle.getChunk(var0, var1, var2);
    }

    @Override
    public IBlockAccess getChunkForCollisions(int var0, int var1) {
        return handle.getChunkForCollisions(var0, var1);
    }

    @Override
    public boolean isWaterAt(BlockPosition var0) {
        return handle.isWaterAt(var0);
    }

    @Override
    public boolean containsAnyLiquid(AxisAlignedBB var0) {
        return handle.containsAnyLiquid(var0);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPosition var0) {
        return handle.getMaxLocalRawBrightness(var0);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPosition var0, int var1) {
        return handle.getMaxLocalRawBrightness(var0, var1);
    }

    @Override
    public boolean hasChunkAt(int var0, int var1) {
        return handle.hasChunkAt(var0, var1);
    }

    @Override
    public boolean hasChunkAt(BlockPosition var0) {
        return handle.hasChunkAt(var0);
    }

    @Override
    public boolean hasChunksAt(BlockPosition var0, BlockPosition var1) {
        return handle.hasChunksAt(var0, var1);
    }

    @Override
    public boolean hasChunksAt(int var0, int var1, int var2, int var3, int var4, int var5) {
        return handle.hasChunksAt(var0, var1, var2, var3, var4, var5);
    }

    @Override
    public boolean hasChunksAt(int var0, int var1, int var2, int var3) {
        return handle.hasChunksAt(var0, var1, var2, var3);
    }

    @Override
    public IRegistryCustom registryAccess() {
        return handle.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return handle.enabledFeatures();
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends IRegistry<? extends T>> var0) {
        return handle.holderLookup(var0);
    }

    @Override
    public float getShade(EnumDirection ed, boolean bln) {
        return handle.getShade(ed, bln);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return handle.getLightEngine();
    }

    @Override
    public int getBrightness(EnumSkyBlock var0, BlockPosition var1) {
        return handle.getBrightness(var0, var1);
    }

    @Override
    public int getRawBrightness(BlockPosition var0, int var1) {
        return handle.getRawBrightness(var0, var1);
    }

    @Override
    public boolean canSeeSky(BlockPosition var0) {
        return handle.canSeeSky(var0);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return handle.getWorldBorder();
    }

    @Override
    public boolean isUnobstructed(IBlockData var0, BlockPosition var1, VoxelShapeCollision var2) {
        return handle.isUnobstructed(var0, var1, var2);
    }

    @Override
    public boolean isUnobstructed(Entity var0) {
        return handle.isUnobstructed(var0);
    }

    @Override
    public boolean noCollision(AxisAlignedBB var0) {
        return handle.noCollision(var0);
    }

    @Override
    public boolean noCollision(Entity var0) {
        return handle.noCollision(var0);
    }

    @Override
    public boolean noCollision(Entity var0, AxisAlignedBB var1) {
        return handle.noCollision(var0, var1);
    }

    @Override
    public boolean noCollision(Entity var0, AxisAlignedBB var1, boolean var2) {
        return handle.noCollision(var0, var1, var2);
    }

    @Override
    public boolean noBlockCollision(Entity var0, AxisAlignedBB var1) {
        return handle.noBlockCollision(var0, var1);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity var0, AxisAlignedBB var1) {
        return handle.getCollisions(var0, var1);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity var0, AxisAlignedBB var1) {
        return handle.getBlockCollisions(var0, var1);
    }

    @Override
    public Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity var0, AxisAlignedBB var1) {
        return handle.getBlockAndLiquidCollisions(var0, var1);
    }

    @Override
    public MovingObjectPositionBlock clipIncludingBorder(RayTrace var0) {
        return handle.clipIncludingBorder(var0);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity var0, AxisAlignedBB var1) {
        return handle.collidesWithSuffocatingBlock(var0, var1);
    }

    @Override
    public Optional<BlockPosition> findSupportingBlock(Entity var0, AxisAlignedBB var1) {
        return handle.findSupportingBlock(var0, var1);
    }

    @Override
    public Optional<Vec3D> findFreePosition(Entity var0, VoxelShape var1, Vec3D var2, double var3, double var5, double var7) {
        return handle.findFreePosition(var0, var1, var2, var3, var5, var7);
    }

    @Override
    public int getDirectSignal(BlockPosition var0, EnumDirection var1) {
        return handle.getDirectSignal(var0, var1);
    }

    @Override
    public int getDirectSignalTo(BlockPosition var0) {
        return handle.getDirectSignalTo(var0);
    }

    @Override
    public int getControlInputSignal(BlockPosition var0, EnumDirection var1, boolean var2) {
        return handle.getControlInputSignal(var0, var1, var2);
    }

    @Override
    public boolean hasSignal(BlockPosition var0, EnumDirection var1) {
        return handle.hasSignal(var0, var1);
    }

    @Override
    public int getSignal(BlockPosition var0, EnumDirection var1) {
        return handle.getSignal(var0, var1);
    }

    @Override
    public boolean hasNeighborSignal(BlockPosition var0) {
        return handle.hasNeighborSignal(var0);
    }

    @Override
    public int getBestNeighborSignal(BlockPosition var0) {
        return handle.getBestNeighborSignal(var0);
    }

    @Override
    public TileEntity getBlockEntity(BlockPosition blockposition) {
        return handle.getBlockEntity(blockposition);
    }

    @Override
    public IBlockData getBlockState(BlockPosition blockposition) {
        return handle.getBlockState(blockposition);
    }

    @Override
    public Fluid getFluidState(BlockPosition blockposition) {
        return handle.getFluidState(blockposition);
    }

    @Override
    public int getLightEmission(BlockPosition blockposition) {
        return handle.getLightEmission(blockposition);
    }

    @Override
    public Stream<IBlockData> getBlockStates(AxisAlignedBB axisalignedbb) {
        return handle.getBlockStates(axisalignedbb);
    }

    @Override
    public MovingObjectPositionBlock isBlockInLine(ClipBlockStateContext clipblockstatecontext) {
        return handle.isBlockInLine(clipblockstatecontext);
    }

    @Override
    public MovingObjectPositionBlock clip(RayTrace raytrace1, BlockPosition blockposition) {
        return handle.clip(raytrace1, blockposition);
    }

    @Override
    public MovingObjectPositionBlock clip(RayTrace raytrace) {
        return handle.clip(raytrace);
    }

    @Override
    public MovingObjectPositionBlock clipWithInteractionOverride(Vec3D vec3d, Vec3D vec3d1, BlockPosition blockposition, VoxelShape voxelshape, IBlockData iblockdata) {
        return handle.clipWithInteractionOverride(vec3d, vec3d1, blockposition, voxelshape, iblockdata);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape voxelshape, Supplier<VoxelShape> supplier) {
        return handle.getBlockFloorHeight(voxelshape, supplier);
    }

    @Override
    public double getBlockFloorHeight(BlockPosition blockposition) {
        return handle.getBlockFloorHeight(blockposition);
    }

    @Override
    public List<Entity> getEntities(Entity entity, AxisAlignedBB aabb, Predicate<? super Entity> prdct) {
        return handle.getEntities(entity, aabb, prdct);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> ett, AxisAlignedBB aabb, Predicate<? super T> prdct) {
        return handle.getEntities(ett, aabb, prdct);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> var0, AxisAlignedBB var1, Predicate<? super T> var2) {
        return handle.getEntitiesOfClass(var0, var1, var2);
    }

    @Override
    public List<? extends EntityHuman> players() {
        return handle.players();
    }

    @Override
    public List<Entity> getEntities(Entity var0, AxisAlignedBB var1) {
        return handle.getEntities(var0, var1);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> var0, AxisAlignedBB var1) {
        return handle.getEntitiesOfClass(var0, var1);
    }

    @Override
    public EntityHuman getNearestPlayer(double var0, double var2, double var4, double var6, Predicate<Entity> var8) {
        return handle.getNearestPlayer(var0, var2, var4, var6, var8);
    }

    @Override
    public EntityHuman getNearestPlayer(Entity var0, double var1) {
        return handle.getNearestPlayer(var0, var1);
    }

    @Override
    public EntityHuman getNearestPlayer(double var0, double var2, double var4, double var6, boolean var8) {
        return handle.getNearestPlayer(var0, var2, var4, var6, var8);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double var0, double var2, double var4, double var6) {
        return handle.hasNearbyAlivePlayer(var0, var2, var4, var6);
    }

    @Override
    public EntityHuman getPlayerByUUID(UUID var0) {
        return handle.getPlayerByUUID(var0);
    }

    @Override
    public boolean setBlock(BlockPosition blockposition, IBlockData iblockdata, int i, int j) {
        return handle.setBlock(blockposition, iblockdata, i, j);
    }

    @Override
    public boolean setBlock(BlockPosition blockposition, IBlockData iblockdata, int i) {
        return handle.setBlock(blockposition, iblockdata, i);
    }

    @Override
    public boolean removeBlock(BlockPosition blockposition, boolean flag) {
        return handle.removeBlock(blockposition, flag);
    }

    @Override
    public boolean destroyBlock(BlockPosition blockposition, boolean flag) {
        return handle.destroyBlock(blockposition, flag);
    }

    @Override
    public boolean destroyBlock(BlockPosition blockposition, boolean flag, Entity entity) {
        return handle.destroyBlock(blockposition, flag, entity);
    }

    @Override
    public boolean destroyBlock(BlockPosition blockposition, boolean flag, Entity entity, int i) {
        return handle.destroyBlock(blockposition, flag, entity, i);
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return handle.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return handle.addFreshEntity(entity, reason);
    }

    @Override
    public int getMaxY() {
        return handle.getMaxY();
    }

    @Override
    public int getSectionsCount() {
        return handle.getSectionsCount();
    }

    @Override
    public int getMinSectionY() {
        return handle.getMinSectionY();
    }

    @Override
    public int getMaxSectionY() {
        return handle.getMaxSectionY();
    }

    @Override
    public boolean isInsideBuildHeight(int var0) {
        return handle.isInsideBuildHeight(var0);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPosition var0) {
        return handle.isOutsideBuildHeight(var0);
    }

    @Override
    public boolean isOutsideBuildHeight(int var0) {
        return handle.isOutsideBuildHeight(var0);
    }

    @Override
    public int getSectionIndex(int var0) {
        return handle.getSectionIndex(var0);
    }

    @Override
    public int getSectionIndexFromSectionY(int var0) {
        return handle.getSectionIndexFromSectionY(var0);
    }

    @Override
    public int getSectionYFromSectionIndex(int var0) {
        return handle.getSectionYFromSectionIndex(var0);
    }

    @Override
    public boolean isStateAtPosition(BlockPosition bp, Predicate<IBlockData> prdct) {
        return handle.isStateAtPosition(bp, prdct);
    }

    @Override
    public boolean isFluidAtPosition(BlockPosition bp, Predicate<Fluid> prdct) {
        return handle.isFluidAtPosition(bp, prdct);
    }
}
