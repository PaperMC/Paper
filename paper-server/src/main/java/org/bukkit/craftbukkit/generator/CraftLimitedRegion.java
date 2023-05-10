package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.Holder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.ChunkCoordIntPair;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.IChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.CraftRegionAccessor;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Consumer;

public class CraftLimitedRegion extends CraftRegionAccessor implements LimitedRegion {

    private final WeakReference<GeneratorAccessSeed> weakAccess;
    private final int centerChunkX;
    private final int centerChunkZ;
    // Buffer is one chunk (16 blocks), can be seen in ChunkStatus#q
    // there the order is {..., FEATURES, LIQUID_CARVERS, STRUCTURE_STARTS, ...}
    private final int buffer = 16;
    private final BoundingBox region;
    boolean entitiesLoaded = false;
    // Minecraft saves the entities as NBTTagCompound during chunk generation. This causes that
    // changes made to the returned bukkit entity are not saved. To combat this we keep them and
    // save them when the population is finished.
    private final List<net.minecraft.world.entity.Entity> entities = new ArrayList<>();
    // SPIGOT-6891: Save outside Entities extra, since they are not part of the region.
    // Prevents crash for chunks which are converting from 1.17 to 1.18
    private final List<net.minecraft.world.entity.Entity> outsideEntities = new ArrayList<>();

    public CraftLimitedRegion(GeneratorAccessSeed access, ChunkCoordIntPair center) {
        this.weakAccess = new WeakReference<>(access);
        centerChunkX = center.x;
        centerChunkZ = center.z;

        World world = access.getMinecraftWorld().getWorld();
        int xCenter = centerChunkX << 4;
        int zCenter = centerChunkZ << 4;
        int xMin = xCenter - getBuffer();
        int zMin = zCenter - getBuffer();
        int xMax = xCenter + getBuffer() + 16;
        int zMax = zCenter + getBuffer() + 16;

        this.region = new BoundingBox(xMin, world.getMinHeight(), zMin, xMax, world.getMaxHeight(), zMax);
    }

    public GeneratorAccessSeed getHandle() {
        GeneratorAccessSeed handle = weakAccess.get();

        if (handle == null) {
            throw new IllegalStateException("GeneratorAccessSeed no longer present, are you using it in a different tick?");
        }

        return handle;
    }

    public void loadEntities() {
        if (entitiesLoaded) {
            return;
        }

        GeneratorAccessSeed access = getHandle();
        // load entities which are already present
        for (int x = -(buffer >> 4); x <= (buffer >> 4); x++) {
            for (int z = -(buffer >> 4); z <= (buffer >> 4); z++) {
                ProtoChunk chunk = (ProtoChunk) access.getChunk(centerChunkX + x, centerChunkZ + z);
                for (NBTTagCompound compound : chunk.getEntities()) {
                    EntityTypes.loadEntityRecursive(compound, access.getMinecraftWorld(), (entity) -> {
                        if (region.contains(entity.getX(), entity.getY(), entity.getZ())) {
                            entity.generation = true;
                            entities.add(entity);
                        } else {
                            outsideEntities.add(entity);
                        }
                        return entity;
                    });
                }
            }
        }

        entitiesLoaded = true;
    }

    public void saveEntities() {
        GeneratorAccessSeed access = getHandle();
        // We don't clear existing entities when they are not loaded and therefore not modified
        if (entitiesLoaded) {
            for (int x = -(buffer >> 4); x <= (buffer >> 4); x++) {
                for (int z = -(buffer >> 4); z <= (buffer >> 4); z++) {
                    ProtoChunk chunk = (ProtoChunk) access.getChunk(centerChunkX + x, centerChunkZ + z);
                    chunk.getEntities().clear();
                }
            }
        }

        for (net.minecraft.world.entity.Entity entity : entities) {
            if (entity.isAlive()) {
                // check if entity is still in region or if it got teleported outside it
                Preconditions.checkState(region.contains(entity.getX(), entity.getY(), entity.getZ()), "Entity %s is not in the region", entity);
                access.addFreshEntity(entity);
            }
        }

        for (net.minecraft.world.entity.Entity entity : outsideEntities) {
            access.addFreshEntity(entity);
        }
    }

    public void breakLink() {
        weakAccess.clear();
    }

    @Override
    public int getBuffer() {
        return buffer;
    }

    @Override
    public boolean isInRegion(Location location) {
        return region.contains(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean isInRegion(int x, int y, int z) {
        return region.contains(x, y, z);
    }

    @Override
    public List<BlockState> getTileEntities() {
        List<BlockState> blockStates = new ArrayList<>();

        for (int x = -(buffer >> 4); x <= (buffer >> 4); x++) {
            for (int z = -(buffer >> 4); z <= (buffer >> 4); z++) {
                ProtoChunk chunk = (ProtoChunk) getHandle().getChunk(centerChunkX + x, centerChunkZ + z);
                for (BlockPosition position : chunk.getBlockEntitiesPos()) {
                    blockStates.add(getBlockState(position.getX(), position.getY(), position.getZ()));
                }
            }
        }

        return blockStates;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBiome(x, y, z);
    }

    @Override
    public void setBiome(int x, int y, int z, Holder<BiomeBase> biomeBase) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        IChunkAccess chunk = getHandle().getChunk(x >> 4, z >> 4, ChunkStatus.EMPTY);
        chunk.setBiome(x >> 2, y >> 2, z >> 2, biomeBase);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBlockState(x, y, z);
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBlockData(x, y, z);
    }

    @Override
    public Material getType(int x, int y, int z) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getType(x, y, z);
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        Preconditions.checkArgument(isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        super.setBlockData(x, y, z, blockData);
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        Preconditions.checkArgument(isInRegion(x, region.getCenter().getBlockY(), z), "Coordinates %s, %s are not in the region", x, z);
        return super.getHighestBlockYAt(x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        Preconditions.checkArgument(isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.getHighestBlockYAt(location);
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        Preconditions.checkArgument(isInRegion(x, region.getCenter().getBlockY(), z), "Coordinates %s, %s are not in the region", x, z);
        return super.getHighestBlockYAt(x, z, heightMap);
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        Preconditions.checkArgument(isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.getHighestBlockYAt(location, heightMap);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType) {
        Preconditions.checkArgument(isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.generateTree(location, random, treeType);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Consumer<BlockState> consumer) {
        Preconditions.checkArgument(isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.generateTree(location, random, treeType, consumer);
    }

    @Override
    public Collection<net.minecraft.world.entity.Entity> getNMSEntities() {
        // Only load entities if we need them
        loadEntities();
        return new ArrayList<>(entities);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.spawn(location, clazz, function, reason);
    }

    @Override
    public void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        entities.add(entity);
    }
}
