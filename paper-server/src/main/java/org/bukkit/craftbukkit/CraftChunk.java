package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.IRegistry;
import net.minecraft.core.SectionPosition;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.thread.ThreadedMailbox;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.biome.WorldChunkManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.BiomeStorage;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.NibbleArray;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.lighting.LightEngine;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class CraftChunk implements Chunk {
    private WeakReference<net.minecraft.world.level.chunk.Chunk> weakChunk;
    private final WorldServer worldServer;
    private final int x;
    private final int z;
    private static final DataPaletteBlock<IBlockData> emptyBlockIDs = new ChunkSection(0).getBlocks();
    private static final byte[] emptyLight = new byte[2048];

    public CraftChunk(net.minecraft.world.level.chunk.Chunk chunk) {
        this.weakChunk = new WeakReference<net.minecraft.world.level.chunk.Chunk>(chunk);

        worldServer = (WorldServer) getHandle().level;
        x = getHandle().getPos().x;
        z = getHandle().getPos().z;
    }

    public CraftChunk(WorldServer worldServer, int x, int z) {
        this.weakChunk = new WeakReference<>(null);
        this.worldServer = worldServer;
        this.x = x;
        this.z = z;
    }

    @Override
    public World getWorld() {
        return worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) getWorld();
    }

    public net.minecraft.world.level.chunk.Chunk getHandle() {
        net.minecraft.world.level.chunk.Chunk c = weakChunk.get();

        if (c == null) {
            c = worldServer.getChunkAt(x, z);

            weakChunk = new WeakReference<net.minecraft.world.level.chunk.Chunk>(c);
        }

        return c;
    }

    void breakLink() {
        weakChunk.clear();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        validateChunkCoordinates(getHandle().getMinBuildHeight(), getHandle().getMaxBuildHeight(), x, y, z);

        return new CraftBlock(worldServer, new BlockPosition((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public boolean isEntitiesLoaded() {
        return getCraftWorld().getHandle().entityManager.a(ChunkCoordIntPair.pair(x, z)); // PAIL rename isEntitiesLoaded
    }

    @Override
    public Entity[] getEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z); // Transient load for this tick
        }

        PersistentEntitySectionManager<net.minecraft.world.entity.Entity> entityManager = getCraftWorld().getHandle().entityManager;
        long pair = ChunkCoordIntPair.pair(x, z);

        if (entityManager.a(pair)) { // PAIL rename isEntitiesLoaded
            return entityManager.getEntities(new ChunkCoordIntPair(x, z)).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .filter(Objects::nonNull).toArray(Entity[]::new);
        }

        entityManager.b(pair); // Start entity loading

        // SPIGOT-6772: Use entity mailbox and re-schedule entities if they get unloaded
        ThreadedMailbox<Runnable> mailbox = ((EntityStorage) entityManager.permanentStorage).entityDeserializerQueue;
        BooleanSupplier supplier = () -> {
            // only execute inbox if our entities are not present
            if (entityManager.a(pair)) {
                return true;
            }

            if (!entityManager.isPending(pair)) {
                // Our entities got unloaded, this should normally not happen.
                entityManager.b(pair); // Re-start entity loading
            }

            // tick loading inbox, which loads the created entities to the world
            // (if present)
            entityManager.tick();
            // check if our entities are loaded
            return entityManager.a(pair);
        };

        // now we wait until the entities are loaded,
        // the converting from NBT to entity object is done on the main Thread which is why we wait
        while (!supplier.getAsBoolean()) {
            if (mailbox.b() != 0) { // PAIL rename size
                mailbox.run();
            } else {
                Thread.yield();
                LockSupport.parkNanos("waiting for entity loading", 100000L);
            }
        }

        return entityManager.getEntities(new ChunkCoordIntPair(x, z)).stream()
                .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                .filter(Objects::nonNull).toArray(Entity[]::new);
    }

    @Override
    public BlockState[] getTileEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z); // Transient load for this tick
        }
        int index = 0;
        net.minecraft.world.level.chunk.Chunk chunk = getHandle();

        BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (Object obj : chunk.blockEntities.keySet().toArray()) {
            if (!(obj instanceof BlockPosition)) {
                continue;
            }

            BlockPosition position = (BlockPosition) obj;
            entities[index++] = worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    @Override
    public boolean isLoaded() {
        return getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
    }

    @Override
    public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        // 987234911L is deterimined in EntitySlime when seeing if a slime can spawn in a chunk
        return SeededRandom.a(getX(), getZ(), getWorld().getSeed(), 987234911L).nextInt(10) == 0;
    }

    @Override
    public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
    }

    @Override
    public boolean isForceLoaded() {
        return getWorld().isChunkForceLoaded(getX(), getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        getWorld().setChunkForceLoaded(getX(), getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(Plugin plugin) {
        return getWorld().addPluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(Plugin plugin) {
        return getWorld().removePluginChunkTicket(getX(), getZ(), plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return getWorld().getPluginChunkTickets(getX(), getZ());
    }

    @Override
    public long getInhabitedTime() {
        return getHandle().getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");

        getHandle().setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<IBlockData> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (ChunkSection section : getHandle().getSections()) {
            if (section != null && section.getBlocks().contains(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return getChunkSnapshot(true, false, false);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        net.minecraft.world.level.chunk.Chunk chunk = getHandle();

        ChunkSection[] cs = chunk.getSections();
        DataPaletteBlock[] sectionBlockIDs = new DataPaletteBlock[cs.length];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];

        for (int i = 0; i < cs.length; i++) {
            if (ChunkSection.a(cs[i])) { // Section is empty? // PAIL rename isEmpty
                sectionBlockIDs[i] = emptyBlockIDs;
                sectionSkyLights[i] = emptyLight;
                sectionEmitLights[i] = emptyLight;
                sectionEmpty[i] = true;
            } else { // Not empty
                NBTTagCompound data = new NBTTagCompound();
                cs[i].getBlocks().a(data, "Palette", "BlockStates");

                DataPaletteBlock blockids = new DataPaletteBlock<>(ChunkSection.GLOBAL_BLOCKSTATE_PALETTE, net.minecraft.world.level.block.Block.BLOCK_STATE_REGISTRY, GameProfileSerializer::c, GameProfileSerializer::a, Blocks.AIR.getBlockData()); // TODO: snapshot whole ChunkSection
                blockids.a(data.getList("Palette", CraftMagicNumbers.NBT.TAG_COMPOUND), data.getLongArray("BlockStates"));

                sectionBlockIDs[i] = blockids;

                LightEngine lightengine = chunk.level.getChunkProvider().getLightEngine();
                NibbleArray skyLightArray = lightengine.a(EnumSkyBlock.SKY).a(SectionPosition.a(x, i, z));
                if (skyLightArray == null) {
                    sectionSkyLights[i] = emptyLight;
                } else {
                    sectionSkyLights[i] = new byte[2048];
                    System.arraycopy(skyLightArray.asBytes(), 0, sectionSkyLights[i], 0, 2048);
                }
                NibbleArray emitLightArray = lightengine.a(EnumSkyBlock.BLOCK).a(SectionPosition.a(x, i, z));
                if (emitLightArray == null) {
                    sectionEmitLights[i] = emptyLight;
                } else {
                    sectionEmitLights[i] = new byte[2048];
                    System.arraycopy(emitLightArray.asBytes(), 0, sectionEmitLights[i], 0, 2048);
                }
            }
        }

        HeightMap hmap = null;

        if (includeMaxBlockY) {
            hmap = new HeightMap(chunk, HeightMap.Type.MOTION_BLOCKING);
            hmap.a(chunk, HeightMap.Type.MOTION_BLOCKING, chunk.heightmaps.get(HeightMap.Type.MOTION_BLOCKING).a());
        }

        BiomeStorage biome = null;

        if (includeBiome || includeBiomeTempRain) {
            biome = chunk.getBiomeIndex();
        }

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), chunk.getMinBuildHeight(), chunk.getMaxBuildHeight(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, biome);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getHandle().persistentDataContainer;
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        BiomeStorage biome = null;

        if (includeBiome || includeBiomeTempRain) {
            WorldChunkManager wcm = world.getHandle().getChunkProvider().getChunkGenerator().getWorldChunkManager();
            biome = new BiomeStorage(world.getHandle().t().d(IRegistry.BIOME_REGISTRY), world.getHandle(), new ChunkCoordIntPair(x, z), wcm);
        }
        IChunkAccess actual = world.getHandle().getChunkAt(x, z, ChunkStatus.EMPTY);

        /* Fill with empty data */
        int hSection = actual.getSectionsCount();
        DataPaletteBlock[] blockIDs = new DataPaletteBlock[hSection];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        boolean[] empty = new boolean[hSection];

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = emptyLight;
            emitLight[i] = emptyLight;
            empty[i] = true;
        }

        return new CraftChunkSnapshot(x, z, world.getMinHeight(), world.getMaxHeight(), world.getName(), world.getFullTime(), blockIDs, skyLight, emitLight, empty, new HeightMap(actual, HeightMap.Type.MOTION_BLOCKING), biome);
    }

    static void validateChunkCoordinates(int minY, int maxY, int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(minY <= y && y <= maxY, "y out of range (expected %s-%s, got %s)", minY, maxY, y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }

    static {
        Arrays.fill(emptyLight, (byte) 0xFF);
    }
}
