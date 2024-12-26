package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.mojang.serialization.Codec;
import io.papermc.paper.FeatureHooks;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.thread.ConsecutiveExecutor;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.DataLayer;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.chunk.PalettedContainerRO;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.storage.EntityStorage;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
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
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.GeneratedStructure;
import org.bukkit.generator.structure.Structure;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class CraftChunk implements Chunk {
    private final ServerLevel worldServer;
    private final int x;
    private final int z;
    private static final PalettedContainer<net.minecraft.world.level.block.state.BlockState> emptyBlockIDs = FeatureHooks.emptyPalettedBlockContainer();
    private static final byte[] FULL_LIGHT = new byte[2048];
    private static final byte[] EMPTY_LIGHT = new byte[2048];

    public CraftChunk(net.minecraft.world.level.chunk.LevelChunk chunk) {
        this.worldServer = chunk.level;
        this.x = chunk.getPos().x;
        this.z = chunk.getPos().z;
    }

    public CraftChunk(ServerLevel worldServer, int x, int z) {
        this.worldServer = worldServer;
        this.x = x;
        this.z = z;
    }

    @Override
    public World getWorld() {
        return this.worldServer.getWorld();
    }

    public CraftWorld getCraftWorld() {
        return (CraftWorld) this.getWorld();
    }

    public ChunkAccess getHandle(ChunkStatus chunkStatus) {
        // Paper start - chunk system
        net.minecraft.world.level.chunk.LevelChunk full = this.worldServer.getChunkIfLoaded(this.x, this.z);
        if (full != null) {
            return full;
        }
        // Paper end - chunk system
        ChunkAccess chunkAccess = this.worldServer.getChunk(this.x, this.z, chunkStatus);

        // SPIGOT-7332: Get unwrapped extension
        if (chunkAccess instanceof ImposterProtoChunk extension) {
            return extension.getWrapped();
        }

        return chunkAccess;
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getZ() {
        return this.z;
    }

    @Override
    public String toString() {
        return "CraftChunk{" + "x=" + this.getX() + "z=" + this.getZ() + '}';
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        CraftChunk.validateChunkCoordinates(this.worldServer.getMinY(), this.worldServer.getMaxY(), x, y, z);

        return new CraftBlock(this.worldServer, new BlockPos((this.x << 4) | x, y, (this.z << 4) | z));
    }

    @Override
    public boolean isEntitiesLoaded() {
        return this.getCraftWorld().getHandle().areEntitiesLoaded(ChunkPos.asLong(this.x, this.z)); // Paper - chunk system
    }

    @Override
    public Entity[] getEntities() {
        return FeatureHooks.getChunkEntities(this.worldServer, this.x, this.z); // Paper - chunk system
    }

    @Override
    public BlockState[] getTileEntities() {
        // Paper start
        return getTileEntities(true);
    }

    @Override
    public BlockState[] getTileEntities(boolean useSnapshot) {
        // Paper end
        if (!this.isLoaded()) {
            this.getWorld().getChunkAt(this.x, this.z); // Transient load for this tick
        }
        int index = 0;
        ChunkAccess chunk = this.getHandle(ChunkStatus.FULL);

        BlockState[] entities = new BlockState[chunk.blockEntities.size()];

        for (BlockPos position : chunk.blockEntities.keySet()) {
            // Paper start
            entities[index++] = this.worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ()).getState(useSnapshot);
        }

        return entities;
    }

    @Override
    public Collection<BlockState> getTileEntities(Predicate<? super Block> blockPredicate, boolean useSnapshot) {
        Preconditions.checkNotNull(blockPredicate, "blockPredicate");
        if (!this.isLoaded()) {
            this.getWorld().getChunkAt(this.x, this.z); // Transient load for this tick
        }
        ChunkAccess chunk = this.getHandle(ChunkStatus.FULL);

        java.util.List<BlockState> entities = new java.util.ArrayList<>();

        for (BlockPos position : chunk.blockEntities.keySet()) {
            Block block = this.worldServer.getWorld().getBlockAt(position.getX(), position.getY(), position.getZ());
            if (blockPredicate.test(block)) {
                entities.add(block.getState(useSnapshot));
            }
            // Paper end
        }

        return entities;
    }

    @Override
    public boolean isGenerated() {
        ChunkAccess chunk = this.getHandle(ChunkStatus.EMPTY);
        return chunk.getPersistedStatus().isOrAfter(ChunkStatus.FULL);
    }

    @Override
    public boolean isLoaded() {
        return this.getWorld().isChunkLoaded(this);
    }

    @Override
    public boolean load() {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), true);
    }

    @Override
    public boolean load(boolean generate) {
        return this.getWorld().loadChunk(this.getX(), this.getZ(), generate);
    }

    @Override
    public boolean unload() {
        return this.getWorld().unloadChunk(this.getX(), this.getZ());
    }

    @Override
    public boolean isSlimeChunk() {
        // 987234911L is deterimined in EntitySlime when seeing if a slime can spawn in a chunk
        return this.worldServer.paperConfig().entities.spawning.allChunksAreSlimeChunks || WorldgenRandom.seedSlimeChunk(this.getX(), this.getZ(), this.getWorld().getSeed(), worldServer.spigotConfig.slimeSeed).nextInt(10) == 0; // Paper
    }

    @Override
    public boolean unload(boolean save) {
        return this.getWorld().unloadChunk(this.getX(), this.getZ(), save);
    }

    @Override
    public boolean isForceLoaded() {
        return this.getWorld().isChunkForceLoaded(this.getX(), this.getZ());
    }

    @Override
    public void setForceLoaded(boolean forced) {
        this.getWorld().setChunkForceLoaded(this.getX(), this.getZ(), forced);
    }

    @Override
    public boolean addPluginChunkTicket(Plugin plugin) {
        return this.getWorld().addPluginChunkTicket(this.getX(), this.getZ(), plugin);
    }

    @Override
    public boolean removePluginChunkTicket(Plugin plugin) {
        return this.getWorld().removePluginChunkTicket(this.getX(), this.getZ(), plugin);
    }

    @Override
    public Collection<Plugin> getPluginChunkTickets() {
        return this.getWorld().getPluginChunkTickets(this.getX(), this.getZ());
    }

    @Override
    public long getInhabitedTime() {
        return this.getHandle(ChunkStatus.EMPTY).getInhabitedTime();
    }

    @Override
    public void setInhabitedTime(long ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be negative");

        this.getHandle(ChunkStatus.STRUCTURE_STARTS).setInhabitedTime(ticks);
    }

    @Override
    public boolean contains(BlockData block) {
        Preconditions.checkArgument(block != null, "Block cannot be null");

        Predicate<net.minecraft.world.level.block.state.BlockState> nms = Predicates.equalTo(((CraftBlockData) block).getState());
        for (LevelChunkSection section : this.getHandle(ChunkStatus.FULL).getSections()) {
            if (section != null && section.getStates().maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Biome biome) {
        Preconditions.checkArgument(biome != null, "Biome cannot be null");

        ChunkAccess chunk = this.getHandle(ChunkStatus.BIOMES);
        Predicate<Holder<net.minecraft.world.level.biome.Biome>> nms = Predicates.equalTo(CraftBiome.bukkitToMinecraftHolder(biome));
        for (LevelChunkSection section : chunk.getSections()) {
            if (section != null && section.getBiomes().maybeHas(nms)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ChunkSnapshot getChunkSnapshot() {
        return this.getChunkSnapshot(true, false, false);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain) {
        // Paper start - Add getChunkSnapshot includeLightData parameter
        return getChunkSnapshot(includeMaxBlockY, includeBiome, includeBiomeTempRain, true);
    }

    @Override
    public ChunkSnapshot getChunkSnapshot(boolean includeMaxBlockY, boolean includeBiome, boolean includeBiomeTempRain, boolean includeLightData) {
        // Paper end - Add getChunkSnapshot includeLightData parameter
        ChunkAccess chunk = this.getHandle(ChunkStatus.FULL);

        LevelChunkSection[] cs = chunk.getSections();
        PalettedContainer[] sectionBlockIDs = new PalettedContainer[cs.length];
        // Paper start - Add getChunkSnapshot includeLightData parameter
        byte[][] sectionSkyLights = includeLightData ? new byte[cs.length][] : null;
        byte[][] sectionEmitLights = includeLightData ? new byte[cs.length][] : null;
        // Paper end - Add getChunkSnapshot includeLightData parameter
        boolean[] sectionEmpty = new boolean[cs.length];
        PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome = (includeBiome || includeBiomeTempRain) ? new PalettedContainer[cs.length] : null;

        Registry<net.minecraft.world.level.biome.Biome> iregistry = this.worldServer.registryAccess().lookupOrThrow(Registries.BIOME);

        for (int i = 0; i < cs.length; i++) {

            // Paper start - Fix ChunkSnapshot#isSectionEmpty(int); and remove codec usage
            sectionEmpty[i] = cs[i].hasOnlyAir(); // fix sectionEmpty array not being filled
            if (!sectionEmpty[i]) {
                sectionBlockIDs[i] = cs[i].getStates().copy(); // use copy instead of round tripping with codecs
            } else {
                sectionBlockIDs[i] = CraftChunk.emptyBlockIDs; // use cached instance for empty block sections
            }
            // Paper end - Fix ChunkSnapshot#isSectionEmpty(int)

            if (includeLightData) { // Paper - Add getChunkSnapshot includeLightData parameter
            LevelLightEngine lightengine = this.worldServer.getLightEngine();
            DataLayer skyLightArray = lightengine.getLayerListener(LightLayer.SKY).getDataLayerData(SectionPos.of(this.x, chunk.getSectionYFromSectionIndex(i), this.z)); // SPIGOT-7498: Convert section index
            if (skyLightArray == null) {
                sectionSkyLights[i] = this.worldServer.dimensionType().hasSkyLight() ? CraftChunk.FULL_LIGHT : CraftChunk.EMPTY_LIGHT;
            } else {
                sectionSkyLights[i] = new byte[2048];
                System.arraycopy(skyLightArray.getData(), 0, sectionSkyLights[i], 0, 2048);
            }
            DataLayer emitLightArray = lightengine.getLayerListener(LightLayer.BLOCK).getDataLayerData(SectionPos.of(this.x, chunk.getSectionYFromSectionIndex(i), this.z)); // SPIGOT-7498: Convert section index
            if (emitLightArray == null) {
                sectionEmitLights[i] = CraftChunk.EMPTY_LIGHT;
            } else {
                sectionEmitLights[i] = new byte[2048];
                System.arraycopy(emitLightArray.getData(), 0, sectionEmitLights[i], 0, 2048);
            }
            } // Paper - Add getChunkSnapshot includeLightData parameter

            if (biome != null) {
                biome[i] = ((PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>) cs[i].getBiomes()).copy(); // Paper - Perf: use copy instead of round tripping with codecs
            }
        }

        Heightmap hmap = null;

        if (includeMaxBlockY) {
            hmap = new Heightmap(chunk, Heightmap.Types.MOTION_BLOCKING);
            hmap.setRawData(chunk, Heightmap.Types.MOTION_BLOCKING, chunk.heightmaps.get(Heightmap.Types.MOTION_BLOCKING).getRawData());
        }

        World world = this.getWorld();
        return new CraftChunkSnapshot(this.getX(), this.getZ(), chunk.getMinY(), chunk.getMaxY(), world.getSeaLevel(), world.getName(), world.getFullTime(), sectionBlockIDs, sectionSkyLights, sectionEmitLights, sectionEmpty, hmap, iregistry, biome);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.getHandle(ChunkStatus.STRUCTURE_STARTS).persistentDataContainer;
    }

    @Override
    public LoadLevel getLoadLevel() {
        net.minecraft.world.level.chunk.LevelChunk chunk = this.worldServer.getChunkIfLoaded(this.getX(), this.getZ());
        if (chunk == null) {
            return LoadLevel.UNLOADED;
        }
        return LoadLevel.values()[chunk.getFullStatus().ordinal()];
    }

    @Override
    public Collection<GeneratedStructure> getStructures() {
        return this.getCraftWorld().getStructures(this.getX(), this.getZ());
    }

    @Override
    public Collection<GeneratedStructure> getStructures(Structure structure) {
        return this.getCraftWorld().getStructures(this.getX(), this.getZ(), structure);
    }

    @Override
    public Collection<Player> getPlayersSeeingChunk() {
        return this.getWorld().getPlayersSeeingChunk(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        CraftChunk that = (CraftChunk) o;

        if (this.x != that.x) return false;
        if (this.z != that.z) return false;
        return this.worldServer.equals(that.worldServer);
    }

    @Override
    public int hashCode() {
        int result = this.worldServer.hashCode();
        result = 31 * result + this.x;
        result = 31 * result + this.z;
        return result;
    }

    public static ChunkSnapshot getEmptyChunkSnapshot(int x, int z, CraftWorld world, boolean includeBiome, boolean includeBiomeTempRain) {
        ChunkAccess actual = world.getHandle().getChunk(x, z, (includeBiome || includeBiomeTempRain) ? ChunkStatus.BIOMES : ChunkStatus.EMPTY);

        /* Fill with empty data */
        int hSection = actual.getSectionsCount();
        PalettedContainer[] blockIDs = new PalettedContainer[hSection];
        byte[][] skyLight = new byte[hSection][];
        byte[][] emitLight = new byte[hSection][];
        boolean[] empty = new boolean[hSection];
        Registry<net.minecraft.world.level.biome.Biome> iregistry = world.getHandle().registryAccess().lookupOrThrow(Registries.BIOME);
        PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>[] biome = (includeBiome || includeBiomeTempRain) ? new PalettedContainer[hSection] : null;
        Codec<PalettedContainerRO<Holder<net.minecraft.world.level.biome.Biome>>> biomeCodec = PalettedContainer.codecRO(iregistry.asHolderIdMap(), iregistry.holderByNameCodec(), PalettedContainer.Strategy.SECTION_BIOMES, iregistry.getOrThrow(Biomes.PLAINS));

        for (int i = 0; i < hSection; i++) {
            blockIDs[i] = CraftChunk.emptyBlockIDs;
            skyLight[i] = world.getHandle().dimensionType().hasSkyLight() ? CraftChunk.FULL_LIGHT : CraftChunk.EMPTY_LIGHT;
            emitLight[i] = CraftChunk.EMPTY_LIGHT;
            empty[i] = true;

            if (biome != null) {
                biome[i] = (PalettedContainer<Holder<net.minecraft.world.level.biome.Biome>>) biomeCodec.parse(NbtOps.INSTANCE, biomeCodec.encodeStart(NbtOps.INSTANCE, actual.getSection(i).getBiomes()).getOrThrow()).getOrThrow(SerializableChunkData.ChunkReadException::new);
            }
        }

        return new CraftChunkSnapshot(x, z, world.getMinHeight(), world.getMaxY(), world.getSeaLevel(), world.getName(), world.getFullTime(), blockIDs, skyLight, emitLight, empty, new Heightmap(actual, Heightmap.Types.MOTION_BLOCKING), iregistry, biome);
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
