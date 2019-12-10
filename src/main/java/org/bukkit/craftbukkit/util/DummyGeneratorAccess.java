package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.server.AxisAlignedBB;
import net.minecraft.server.BiomeBase;
import net.minecraft.server.BiomeManager;
import net.minecraft.server.Block;
import net.minecraft.server.BlockPosition;
import net.minecraft.server.ChunkStatus;
import net.minecraft.server.DifficultyDamageScaler;
import net.minecraft.server.Entity;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.Fluid;
import net.minecraft.server.FluidType;
import net.minecraft.server.GeneratorAccess;
import net.minecraft.server.HeightMap;
import net.minecraft.server.IBlockData;
import net.minecraft.server.IChunkAccess;
import net.minecraft.server.IChunkProvider;
import net.minecraft.server.LightEngine;
import net.minecraft.server.ParticleParam;
import net.minecraft.server.SoundCategory;
import net.minecraft.server.SoundEffect;
import net.minecraft.server.TickList;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;
import net.minecraft.server.WorldBorder;
import net.minecraft.server.WorldData;
import net.minecraft.server.WorldProvider;

public class DummyGeneratorAccess implements GeneratorAccess {

    public static final GeneratorAccess INSTANCE = new DummyGeneratorAccess();

    protected DummyGeneratorAccess() {
    }

    @Override
    public long getSeed() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickList<Block> getBlockTickList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TickList<FluidType> getFluidTickList() {
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
    public Random getRandom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(BlockPosition bp, Block block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void playSound(EntityHuman eh, BlockPosition bp, SoundEffect se, SoundCategory sc, float f, float f1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addParticle(ParticleParam pp, double d, double d1, double d2, double d3, double d4, double d5) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void a(EntityHuman eh, int i, BlockPosition bp, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Entity> getEntities(Entity entity, AxisAlignedBB aabb, Predicate<? super Entity> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T extends Entity> List<T> a(Class<? extends T> type, AxisAlignedBB aabb, Predicate<? super T> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<? extends EntityHuman> getPlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IChunkAccess getChunkAt(int i, int i1, ChunkStatus cs, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int a(HeightMap.Type type, int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int c() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeManager d() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BiomeBase a(int i, int i1, int i2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean p_() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getSeaLevel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldProvider getWorldProvider() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public LightEngine e() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public WorldBorder getWorldBorder() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(BlockPosition bp, Predicate<IBlockData> prdct) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i) {
        return false;
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag, Entity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
