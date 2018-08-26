package org.bukkit.craftbukkit.util;

import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EnumDirection;
import net.minecraft.server.EnumSkyBlock;
import net.minecraft.server.Fluid;
import net.minecraft.server.FluidType;
import net.minecraft.server.GeneratorAccess;
import net.minecraft.server.HeightMap;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.IDataManager;
import net.minecraft.server.ParticleParam;
import net.minecraft.server.PersistentCollection;
import net.minecraft.server.SoundCategory;
import net.minecraft.server.SoundEffect;
import net.minecraft.server.TickList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.VoxelShape;
import net.minecraft.server.World;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldProvider;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class DummyGeneratorAccess implements GeneratorAccess {

    public static final GeneratorAccess INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickList<Block> J() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickList<FluidType> I() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IChunkAccess b(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public World getMinecraftWorld() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldData getWorldData() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DifficultyDamageScaler getDamageScaler(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IChunkProvider getChunkProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDataManager getDataManager() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Random m() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(BlockPosition bp, Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BlockPosition getSpawn() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void a(EntityHuman eh, BlockPosition bp, SoundEffect se, SoundCategory sc, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleParam pp, double d, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isEmpty(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeBase getBiome(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBrightness(EnumSkyBlock esb, BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLightLevel(BlockPosition bp, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isChunkLoaded(int i, int i1, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean e(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int a(HeightMap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EntityHuman a(double d, double d1, double d2, double d3, Predicate<Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int c() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(Entity entity, VoxelShape vs) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int a(BlockPosition bp, EnumDirection ed) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean e() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldProvider o() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TileEntity getTileEntity(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IBlockData getType(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Fluid b(BlockPosition bp) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PersistentCollection h() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i) {
        return false;
    }

    @Override
    public boolean addEntity(Entity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean addEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setAir(BlockPosition blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setAir(BlockPosition blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
