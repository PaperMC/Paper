package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.core.IRegistry;
import net.minecraft.core.SectionPosition;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.level.WorldServer;
import net.minecraft.util.thread.ThreadedMailbox;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.NibbleArray;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.ProtoChunkExtension;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.storage.ChunkRegionLoader;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.levelgen.HeightMap;
import net.minecraft.world.level.levelgen.SeededRandom;
import net.minecraft.world.level.lighting.LevelLightEngine;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Entity;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class CraftChunk implements Chunk {
    private final WorldServer worldServer;
    private final int x;
    private final int z;
    private static final DataPaletteBlock<IBlockData> emptyBlockIDs = new DataPaletteBlock<>(net.minecraft.world.level.block.Block.BLOCK_STATE_REGISTRY, Blocks.AIR.defaultBlockState(), DataPaletteBlock.d.SECTION_STATES);
    private static final byte[] FULL_LIGHT = new byte[2048];
    private static final byte[] EMPTY_LIGHT = new byte[2048];

    public CraftChunk(net.minecraft.world.level.chunk.Chunk chunk) {
        worldServer = chunk.level;
        x = chunk.getPos().x;
        z = chunk.getPos().z;
    }

    public CraftChunk(WorldServer worldServer, int x, int z) {
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

    public IChunkAccess getHandle(ChunkStatus chunkStatus) {
        IChunkAccess chunkAccess = worldServer.getChunk(x, z, chunkStatus);

        // SPIGOT-7332: Get unwrapped extension
        if (chunkAccess instanceof ProtoChunkExtension extension) {
            return extension.getWrapped();
        }

        return chunkAccess;
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
        validateChunkCoordinates(worldServer.getMinBuildHeight(), worldServer.getMaxBuildHeight(), x, y, z);

        return new CraftBlock(worldServer, new BlockPosition((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public boolean isEntitiesLoaded() {
        return getCraftWorld().getHandle().entityManager.areEntitiesLoaded(ChunkCoordIntPair.asLong(x, z));
    }

    @Override
    public Entity[] getEntities() {
        if (!isLoaded()) {
            getWorld().getChunkAt(x, z); // Transient load for this tick
        }

        PersistentEntitySectionManager<net.minecraft.world.entity.Entity> entityManager = getCraftWorld().getHandle().entityManager;
        long pair = ChunkCoordIntPair.asLong(x, z);

        if (entityManager.areEntitiesLoaded(pair)) {
            return entityManager.getEntities(new ChunkCoordIntPair(x, z)).stream()
                    .map(net.minecraft.world.entity.Entity::getBukkitEntity)
                    .filter(Objects::nonNull).toArray(Entity[]::new);
        }

        entityManager.ensureChunkQueuedForLoad(pair); // Start entity loading

        // SPIGOT-6772: Use entity mailbox and re-schedule entities if they get unloaded
        ThreadedMailbox<Runnable> mailbox = ((EntityStorage) entityManager.permanentStorage).entityDeserializerQueue;
        BooleanSupplier supplier = () -> {
            // only execute inbox if our entities are not present
            if (entityManager.areEntitiesLoaded(pair)) {
                return true;
            }

            if (!entityManager.isPending(pair)) {
                // Our entities got unloaded, this should normally not happen.
                entityManager.ensureChunkQueuedForLoad(pair); // Re-start entity loading
            }

            // tick loading inbox, which loads the created entities to the world
            // (if present)
            entityManager.tick();
            // check if our entities are loaded
            return entityManager.areEntitiesLoaded(pair);
        };

        // now we wait until the entities are loaded,
        // the converting from NBT to entity object is done on the main Thread which is why we wait
        while (!supplier.getAsBoolean()) {
            if (mailbox.size() != 0) {
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
        IChunkAccess chunk = getHandle(ChunkStatus.FULL);

        BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (BlockPosition position : chunk.blockEntities.keySet()) {
            entities[index++] = worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState();
        }

        return entities;
    }

    @Override
    public boolean isGenerated() {
        IChunkAccess chunk = getHandle(ChunkStatus.EMPTY);
        return chunk.getStatus().isOrAfter(ChunkStatus.FULL);
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
        return SeededRandom.seedSlimeChunk(getX(), getZ(), getWorld().getSeed(), 987234911L).nextInt(10) == 0;
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
        return getHandle(ChunkStatus.EMPTY).getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");

        getHandle(ChunkStatus.STRUCTURE_STARTS).setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<IBlockData> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (ChunkSection section : getHandle(ChunkStatus.FULL).getSections()) {
            if (section != null && section.getStates().maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Biome biome) {
        Preconditions.checkArgument(biome != null, "Biome cannot be null");

        IChunkAccess chunk = getHandle(ChunkStatus.BIOMES);
        Predicate<Holder<BiomeBase>> nms = Predicates.equalTo(CraftBiome.bukkitToMinecraftHolder(biome));
        for (ChunkSection section : chunk.getSections()) {
            if (section != null && section.getBiomes().maybeHas(nms)) {
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
        IChunkAccess chunk = getHandle(ChunkStatus.FULL);

        ChunkSection[] cs = chunk.getSections();
        DataPaletteBlock[] sectionBlockIDs = new DataPaletteBlock[cs.length];
        byte[][] sectionSkyLights = new byte[cs.length][];
        byte[][] sectionEmitLights = new byte[cs.length][];
        boolean[] sectionEmpty = new boolean[cs.length];
        PalettedContainerRO<Holder<BiomeBase>>[] biome = (includeBiome || includeBiomeTempRain) ? new DataPaletteBlock[cs.length] : null;

        IRegistry<BiomeBase> iregistry = worldServer.registryAccess().registryOrThrow(Registries.BIOME);
        Codec<PalettedContainerRO<Holder<BiomeBase>>> biomeCodec = DataPaletteBlock.codecRO(iregistry.asHolderIdMap(), iregistry.holderByNameCodec(), DataPaletteBlock.d.SECTION_BIOMES, iregistry.getHolderOrThrow(Biomes.PLAINS));

        for (int i = 0; i < cs.length; i++) {
            NBTTagCompound data = new NBTTagCompound();

            data.put("block_states", ChunkRegionLoader.BLOCK_STATE_CODEC.encodeStart(DynamicOpsNBT.INSTANCE, cs[i].getStates()).getOrThrow());
            sectionBlockIDs[i] = ChunkRegionLoader.BLOCK_STATE_CODEC.parse(DynamicOpsNBT.INSTANCE, data.getCompound("block_states")).getOrThrow(ChunkRegionLoader.a::new);

            LevelLightEngine lightengine = worldServer.getLightEngine();
            NibbleArray skyLightArray = lightengine.getLayerListener(EnumSkyBlock.SKY).getDataLayerData(SectionPosition.of(x, chunk.getSectionYFromSectionIndex(i), z)); // SPIGOT-7498: Convert section index
            if (skyLightArray == null) {
                sectionSkyLights[i] = worldServer.dimensionType().hasSkyLight() ? FULL_LIGHT : EMPTY_LIGHT;
            } else {
                sectionSkyLights[i] = new byte[2048];
                System.arraycopy(skyLightArray.getData(), 0, sectionSkyLights[i], 0, 2048);
            }
            NibbleArray emitLightArray = lightengine.getLayerListener(EnumSkyBlock.BLOCK).getDataLayerData(SectionPosition.of(x, chunk.getSectionYFromSectionIndex(i), z)); // SPIGOT-7498: Convert section index
            if (emitLightArray == null) {
                sectionEmitLights[i] = EMPTY_LIGHT;
            } else {
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(emitLightArray.getData(), 0, sectionEmitLights[i], 0, 2048);
            }

            if (biome != null) {
                data.put("biomes", biomeCodec.encodeStart(DynamicOpsNBT.INSTANCE, cs[i].getBiomes()).getOrThrow());
                biome[i] = biomeCodec.parse(DynamicOpsNBT.INSTANCE, data.getCompound("biomes")).getOrThrow(ChunkRegionLoader.a::new);
            }
        }

        HeightMap hmap = null;

        if (includeMaxBlockY) {
            hmap = new HeightMap(chunk, HeightMap.Type.MOTION_BLOCKING);
            hmap.setRawData(chunk, HeightMap.Type.MOTION_BLOCKING, chunk.heightmaps.get(HeightMap.Type.MOTION_BLOCKING).getRawData());
        }

        World world = getWorld();
        return new CraftChunkSnapshot(getX(), getZ(), chunk.getMinBuildHeight(), chunk.getMaxBuildHeight(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, iregistry, biome);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getHandle(ChunkStatus.STRUCTURE_STARTS).persistentDataContainer;
    }

    @Override
    public LoadLevel getLoadLevel() {
        net.minecraft.world.level.chunk.Chunk chunk = worldServer.getChunkIfLoaded(getX(), getZ());
        if (chunk == null) {
            return LoadLevel.UNLOADED;
        }
        return LoadLevel.values()[chunk.getFullStatus().ordinal()];
    }

    @Override
    public Collection<GeneratedStructure> getStructures() {
        return getCraftWorld().getStructures(getX(), getZ());
    }

    @Override
    public Collection<GeneratedStructure> getStructures(Structure structure) {
        return getCraftWorld().getStructures(getX(), getZ(), structure);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CraftChunk that = (CraftChunk) o;

        if (x != that.x) return false;
        if (z != that.z) return false;
        return worldServer.equals(that.worldServer);
    }

    @Override
    public int hashCode() {
        int result = worldServer.hashCode();
        result = 31 * result + x;
        result = 31 * result + z;
        return result;
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        IChunkAccess actual = world.getHandle().getChunk(x, z, (includeBiome || includeBiomeTempRain) ? ChunkStatus.BIOMES : ChunkStatus.EMPTY);

        /* Fill with empty data */
        int hSection = actual.getSectionsCount();
        DataPaletteBlock[] blockIDs = new DataPaletteBlock[hSection];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        boolean[] empty = new boolean[hSection];
        IRegistry<BiomeBase> iregistry = world.getHandle().registryAccess().registryOrThrow(Registries.BIOME);
        DataPaletteBlock<Holder<BiomeBase>>[] biome = (includeBiome || includeBiomeTempRain) ? new DataPaletteBlock[hSection] : null;
        Codec<PalettedContainerRO<Holder<BiomeBase>>> biomeCodec = DataPaletteBlock.codecRO(iregistry.asHolderIdMap(), iregistry.holderByNameCodec(), DataPaletteBlock.d.SECTION_BIOMES, iregistry.getHolderOrThrow(Biomes.PLAINS));

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = emptyBlockIDs;
            skyLight[i] = world.getHandle().dimensionType().hasSkyLight() ? FULL_LIGHT : EMPTY_LIGHT;
            emitLight[i] = EMPTY_LIGHT;
            empty[i] = true;

            if (biome != null) {
                biome[i] = (DataPaletteBlock<Holder<BiomeBase>>) biomeCodec.parse(DynamicOpsNBT.INSTANCE, biomeCodec.encodeStart(DynamicOpsNBT.INSTANCE, actual.getSection(i).getBiomes()).getOrThrow()).getOrThrow(ChunkRegionLoader.a::new);
            }
        }

        return new CraftChunkSnapshot(x, z, world.getMinHeight(), world.getMaxHeight(), world.getName(), world.getFullTime(), blockIDs, skyLight, emitLight, empty, new HeightMap(actual, HeightMap.Type.MOTION_BLOCKING), iregistry, biome);
    }

    static void validateChunkCoordinates(int minY, int maxY, int x, int y, int z) {
        Preconditions.checkArgument(0 <= x && x <= 15, "x out of range (expected 0-15, got %s)", x);
        Preconditions.checkArgument(minY <= y && y <= maxY, "y out of range (expected %s-%s, got %s)", minY, maxY, y);
        Preconditions.checkArgument(0 <= z && z <= 15, "z out of range (expected 0-15, got %s)", z);
    }

    static {
        Arrays.fill(FULL_LIGHT, (byte) 0xFF);
    }
}
