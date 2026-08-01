package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
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
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.BlackholeTickAccess;
import net.minecraft.world.ticks.LevelTickAccess;

public class DummyLevelAccessor implements WorldGenLevel {

    public static final WorldGenLevel INSTANCE = new DummyLevelAccessor();

    protected DummyLevelAccessor() {
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServerLevel getLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long nextSubTickCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public void scheduleTick(BlockPos pos, Block type, int tickDelay) {
        // Used by ComposterBlock
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return BlackholeTickAccess.emptyLevelList();
    }

    @Override
    public LevelData getLevelData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MinecraftServer getServer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ChunkSource getChunkSource() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RandomSource getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void levelEvent(Entity source, int type, BlockPos pos, int data) {
        // Used by PowderSnowBlock.pickupBlock
    }

    @Override
    public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
        // Used by ComposterBlock
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends Player> players() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(Heightmap.Types type, int x, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSkyDarken() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeManager getBiomeManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DimensionType dimensionType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RegistryAccess registryAccess() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EnvironmentAttributeReader environmentAttributes() {
        return EnvironmentAttributeReader.EMPTY;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return Blocks.AIR.defaultBlockState(); // SPIGOT-6515
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState(); // SPIGOT-6634
    }

    @Override
    public ChunkAccess getChunkIfLoadedImmediately(int x, int z) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockState getBlockStateIfLoaded(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FluidState getFluidIfLoaded(BlockPos pos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState blockState, @Block.UpdateFlags int updateFlags, int updateLimit) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
        return false; // SPIGOT-6515
    }

    @Override
    public void scheduleTick(BlockPos pos, Fluid type, int tickDelay) {}

    @Override
    public void scheduleTick(BlockPos pos, Block type, int tickDelay, net.minecraft.world.ticks.TickPriority priority) {}

    @Override
    public void scheduleTick(BlockPos pos, Fluid type, int tickDelay, net.minecraft.world.ticks.TickPriority priority) {}
}
