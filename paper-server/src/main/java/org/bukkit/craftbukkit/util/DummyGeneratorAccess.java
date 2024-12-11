package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
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

public class DummyGeneratorAccess implements WorldGenLevel {

    public static final WorldGenLevel INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
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
    public void scheduleTick(BlockPos pos, Block block, int delay) {
        // Used by BlockComposter
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
    public void playSound(Player source, BlockPos pos, SoundEvent sound, SoundSource category, float volume, float pitch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void levelEvent(Player player, int eventId, BlockPos pos, int data) {
        // Used by PowderSnowBlock.removeFluid
    }

    @Override
    public void gameEvent(Holder<GameEvent> event, Vec3 emitterPos, GameEvent.Context emitter) {
        // Used by BlockComposter
    }

    @Override
    public List<Entity> getEntities(Entity except, AABB box, Predicate<? super Entity> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> filter, AABB box, Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends Player> players() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus leastStatus, boolean create) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getHeight(Heightmap.Types heightmap, int x, int z) {
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
    public Holder<Biome> getUncachedNoiseBiome(int biomeX, int biomeY, int biomeZ) {
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
    public float getShade(Direction direction, boolean shaded) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> state) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState state, int flags, int maxUpdateDepth) {
        return false;
    }

    @Override
    public boolean removeBlock(BlockPos pos, boolean move) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean destroyBlock(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth) {
        return false; // SPIGOT-6515
    }
}
