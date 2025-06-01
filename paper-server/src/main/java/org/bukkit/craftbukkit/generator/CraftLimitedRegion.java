package org.bukkit.craftbukkit.generator;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.storage.TagValueInput;
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

public class CraftLimitedRegion extends CraftRegionAccessor implements LimitedRegion {

    private final WeakReference<WorldGenLevel> weakAccess;
    private final int centerChunkX;
    private final int centerChunkZ;
    // Buffer is one chunk (16 blocks), can be seen in ChunkStatus#q
    // there the order is {..., FEATURES, LIQUID_CARVERS, STRUCTURE_STARTS, ...}
    private final int buffer = 16;
    private final BoundingBox region;
    boolean entitiesLoaded = false;
    // Minecraft saves the entities as CompoundTag during chunk generation. This causes that
    // changes made to the returned bukkit entity are not saved. To combat this we keep them and
    // save them when the population is finished.
    private final List<net.minecraft.world.entity.Entity> entities = new ArrayList<>();
    // SPIGOT-6891: Save outside Entities extra, since they are not part of the region.
    // Prevents crash for chunks which are converting from 1.17 to 1.18
    private final List<net.minecraft.world.entity.Entity> outsideEntities = new ArrayList<>();

    public CraftLimitedRegion(WorldGenLevel access, ChunkPos center) {
        this.weakAccess = new WeakReference<>(access);
        this.centerChunkX = center.x;
        this.centerChunkZ = center.z;

        World world = access.getMinecraftWorld().getWorld();
        int xCenter = this.centerChunkX << 4;
        int zCenter = this.centerChunkZ << 4;
        int xMin = xCenter - this.getBuffer();
        int zMin = zCenter - this.getBuffer();
        int xMax = xCenter + this.getBuffer() + 16;
        int zMax = zCenter + this.getBuffer() + 16;

        this.region = new BoundingBox(xMin, world.getMinHeight(), zMin, xMax, world.getMaxHeight(), zMax);
    }

    public WorldGenLevel getHandle() {
        WorldGenLevel handle = this.weakAccess.get();

        Preconditions.checkState(handle != null, "GeneratorAccessSeed no longer present, are you using it in a different tick?");

        return handle;
    }

    public void loadEntities() {
        if (this.entitiesLoaded) {
            return;
        }

        WorldGenLevel access = this.getHandle();
        // load entities which are already present
        for (int x = -(this.buffer >> 4); x <= (this.buffer >> 4); x++) {
            for (int z = -(this.buffer >> 4); z <= (this.buffer >> 4); z++) {
                ProtoChunk chunk = (ProtoChunk) access.getChunk(this.centerChunkX + x, this.centerChunkZ + z);
                for (CompoundTag compound : chunk.getEntities()) {
                    EntityType.loadEntityRecursive(compound, access.getMinecraftWorld(), EntitySpawnReason.LOAD, (entity) -> {
                        if (this.region.contains(entity.getX(), entity.getY(), entity.getZ())) {
                            entity.generation = true;
                            this.entities.add(entity);
                        } else {
                            this.outsideEntities.add(entity);
                        }
                        return entity;
                    });
                }
            }
        }

        this.entitiesLoaded = true;
    }

    public void saveEntities() {
        WorldGenLevel access = this.getHandle();
        // We don't clear existing entities when they are not loaded and therefore not modified
        if (this.entitiesLoaded) {
            for (int x = -(this.buffer >> 4); x <= (this.buffer >> 4); x++) {
                for (int z = -(this.buffer >> 4); z <= (this.buffer >> 4); z++) {
                    ProtoChunk chunk = (ProtoChunk) access.getChunk(this.centerChunkX + x, this.centerChunkZ + z);
                    chunk.getEntities().clear();
                }
            }
        }

        for (net.minecraft.world.entity.Entity entity : this.entities) {
            if (entity.isAlive()) {
                // check if entity is still in region or if it got teleported outside it
                Preconditions.checkState(this.region.contains(entity.getX(), entity.getY(), entity.getZ()), "Entity %s is not in the region", entity);
                access.addFreshEntityWithPassengers(entity);
            }
        }

        for (net.minecraft.world.entity.Entity entity : this.outsideEntities) {
            access.addFreshEntityWithPassengers(entity);
        }
    }

    public void breakLink() {
        this.weakAccess.clear();
    }

    @Override
    public int getBuffer() {
        return this.buffer;
    }

    @Override
    public boolean isInRegion(Location location) {
        return this.region.contains(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public boolean isInRegion(int x, int y, int z) {
        return this.region.contains(x, y, z);
    }

    @Override
    public List<BlockState> getTileEntities() {
        List<BlockState> blockStates = new ArrayList<>();

        for (int x = -(this.buffer >> 4); x <= (this.buffer >> 4); x++) {
            for (int z = -(this.buffer >> 4); z <= (this.buffer >> 4); z++) {
                ProtoChunk chunk = (ProtoChunk) this.getHandle().getChunk(this.centerChunkX + x, this.centerChunkZ + z);
                for (BlockPos position : chunk.getBlockEntitiesPos()) {
                    blockStates.add(this.getBlockState(position.getX(), position.getY(), position.getZ()));
                }
            }
        }

        return blockStates;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBiome(x, y, z);
    }

    // Paper start
    @Override
    public Biome getComputedBiome(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getComputedBiome(x, y, z);
    }
    // Paper end

    @Override
    public void setBiome(int x, int y, int z, Holder<net.minecraft.world.level.biome.Biome> biomeBase) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        ChunkAccess chunk = this.getHandle().getChunk(x >> 4, z >> 4, ChunkStatus.EMPTY);
        chunk.setBiome(x >> 2, y >> 2, z >> 2, biomeBase);
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBlockState(x, y, z);
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getBlockData(x, y, z);
    }

    @Override
    public Material getType(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getType(x, y, z);
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        super.setBlockData(x, y, z, blockData);
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        Preconditions.checkArgument(this.isInRegion(x, this.region.getCenter().getBlockY(), z), "Coordinates %s, %s are not in the region", x, z);
        return super.getHighestBlockYAt(x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        Preconditions.checkArgument(this.isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.getHighestBlockYAt(location);
    }

    @Override
    public int getHighestBlockYAt(int x, int z, HeightMap heightMap) {
        Preconditions.checkArgument(this.isInRegion(x, this.region.getCenter().getBlockY(), z), "Coordinates %s, %s are not in the region", x, z);
        return super.getHighestBlockYAt(x, z, heightMap);
    }

    @Override
    public int getHighestBlockYAt(Location location, HeightMap heightMap) {
        Preconditions.checkArgument(this.isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.getHighestBlockYAt(location, heightMap);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType) {
        Preconditions.checkArgument(this.isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.generateTree(location, random, treeType);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Consumer<? super BlockState> consumer) {
        Preconditions.checkArgument(this.isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.generateTree(location, random, treeType, consumer);
    }

    @Override
    public Collection<net.minecraft.world.entity.Entity> getNMSEntities() {
        // Only load entities if we need them
        this.loadEntities();
        return new ArrayList<>(this.entities);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        Preconditions.checkArgument(this.isInRegion(location), "Coordinates %s, %s, %s are not in the region", location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return super.spawn(location, clazz, function, reason);
    }

    @Override
    public void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        this.entities.add(entity);
    }

    @Override
    public void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        this.entities.add(entity);
    }

    // Paper start - Add more LimitedRegion API
    @Override
    public void setBlockState(int x, int y, int z, BlockState state) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!state.getBlockData().matches(getHandle().getBlockState(pos).createCraftBlockData())) {
            throw new IllegalArgumentException("BlockData does not match! Expected " + state.getBlockData().getAsString(false) + ", got " + getHandle().getBlockState(pos).createCraftBlockData().getAsString(false));
        }
        getHandle().getBlockEntity(pos).loadWithComponents(TagValueInput.createDiscarding(
                this.getHandle().registryAccess(), ((org.bukkit.craftbukkit.block.CraftBlockEntityState<?>) state).getSnapshotNBT()
        ));
    }

    @Override
    public void scheduleBlockUpdate(int x, int y, int z) {
        BlockPos position = new BlockPos(x, y, z);
        getHandle().scheduleTick(position, getHandle().getBlockState(position).getBlock(), 0);
    }

    @Override
    public void scheduleFluidUpdate(int x, int y, int z) {
        BlockPos position = new BlockPos(x, y, z);
        getHandle().scheduleTick(position, getHandle().getFluidState(position).getType(), 0);
    }

    @Override
    public World getWorld() {
        // reading/writing the returned Minecraft world causes a deadlock.
        // By implementing this, and covering it in warnings, we're assuming people won't be stupid, and
        // if they are stupid, they'll figure it out pretty fast.
        return getHandle().getMinecraftWorld().getWorld();
    }

    @Override
    public int getCenterChunkX() {
        return centerChunkX;
    }

    @Override
    public int getCenterChunkZ() {
        return centerChunkZ;
    }
    // Paper end - Add more LimitedRegion API
    // Paper start - Fluid API
    @Override
    public io.papermc.paper.block.fluid.FluidData getFluidData(int x, int y, int z) {
        Preconditions.checkArgument(this.isInRegion(x, y, z), "Coordinates %s, %s, %s are not in the region", x, y, z);
        return super.getFluidData(x, y, z);
    }
    // Paper end
}
