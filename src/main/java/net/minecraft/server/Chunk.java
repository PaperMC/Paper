package net.minecraft.server;

import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.shorts.ShortList;
import it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk implements IChunkAccess {

    private static final Logger LOGGER = LogManager.getLogger();
    @Nullable
    public static final ChunkSection a = null; public static final ChunkSection EMPTY_CHUNK_SECTION = a; // Paper - OBFHELPER
    private final ChunkSection[] sections;
    private BiomeStorage d;
    private final Map<BlockPosition, NBTTagCompound> e;
    public boolean loaded;
    public final WorldServer world; // CraftBukkit - type
    public final Map<HeightMap.Type, HeightMap> heightMap;
    private final ChunkConverter i;
    public final Map<BlockPosition, TileEntity> tileEntities;
    public final List<Entity>[] entitySlices; // Spigot
    private final Map<StructureGenerator<?>, StructureStart<?>> l;
    private final Map<StructureGenerator<?>, LongSet> m;
    private final ShortList[] n;
    private TickList<Block> o;
    private TickList<FluidType> p;
    private boolean q;
    public long lastSaved; // Paper
    private volatile boolean s;
    private long inhabitedTime;
    @Nullable
    private Supplier<PlayerChunk.State> u;
    @Nullable
    private Consumer<Chunk> v;
    private final ChunkCoordIntPair loc; public final long coordinateKey; // Paper - cache coordinate key
    private volatile boolean x;

    public Chunk(World world, ChunkCoordIntPair chunkcoordintpair, BiomeStorage biomestorage) {
        this(world, chunkcoordintpair, biomestorage, ChunkConverter.a, TickListEmpty.b(), TickListEmpty.b(), 0L, (ChunkSection[]) null, (Consumer) null);
    }

    // Paper start
    public final co.aikar.util.Counter<String> entityCounts = new co.aikar.util.Counter<>();
    public final co.aikar.util.Counter<String> tileEntityCounts = new co.aikar.util.Counter<>();
    private class TileEntityHashMap extends java.util.HashMap<BlockPosition, TileEntity> {
        @Override
        public TileEntity put(BlockPosition key, TileEntity value) {
            TileEntity replaced = super.put(key, value);
            if (replaced != null) {
                replaced.setCurrentChunk(null);
                tileEntityCounts.decrement(replaced.getMinecraftKeyString());
            }
            if (value != null) {
                value.setCurrentChunk(Chunk.this);
                tileEntityCounts.increment(value.getMinecraftKeyString());
            }
            return replaced;
        }

        @Override
        public TileEntity remove(Object key) {
            TileEntity removed = super.remove(key);
            if (removed != null) {
                removed.setCurrentChunk(null);
                tileEntityCounts.decrement(removed.getMinecraftKeyString());
            }
            return removed;
        }
    }
    // Track the number of minecarts and items
    // Keep this synced with entitySlices.add() and entitySlices.remove()
    private final int[] itemCounts = new int[16];
    private final int[] inventoryEntityCounts = new int[16];
    // Paper end

    public Chunk(World world, ChunkCoordIntPair chunkcoordintpair, BiomeStorage biomestorage, ChunkConverter chunkconverter, TickList<Block> ticklist, TickList<FluidType> ticklist1, long i, @Nullable ChunkSection[] achunksection, @Nullable Consumer<Chunk> consumer) {
        this.sections = new ChunkSection[16];
        this.e = Maps.newHashMap();
        this.heightMap = Maps.newEnumMap(HeightMap.Type.class);
        this.tileEntities = new TileEntityHashMap(); // Paper
        this.l = Maps.newHashMap();
        this.m = Maps.newHashMap();
        this.n = new ShortList[16];
        this.entitySlices = (List[]) (new List[16]); // Spigot
        this.world = (WorldServer) world; // CraftBukkit - type
        this.loc = chunkcoordintpair; this.coordinateKey = MCUtil.getCoordinateKey(chunkcoordintpair); // Paper - cache coordinate key
        this.i = chunkconverter;
        HeightMap.Type[] aheightmap_type = HeightMap.Type.values();
        int j = aheightmap_type.length;

        for (int k = 0; k < j; ++k) {
            HeightMap.Type heightmap_type = aheightmap_type[k];

            if (ChunkStatus.FULL.h().contains(heightmap_type)) {
                this.heightMap.put(heightmap_type, new HeightMap(this, heightmap_type));
            }
        }

        for (int l = 0; l < this.entitySlices.length; ++l) {
            this.entitySlices[l] = new org.bukkit.craftbukkit.util.UnsafeList(); // Spigot
        }

        this.d = biomestorage;
        this.o = ticklist;
        this.p = ticklist1;
        this.inhabitedTime = i;
        this.v = consumer;
        if (achunksection != null) {
            if (this.sections.length == achunksection.length) {
                System.arraycopy(achunksection, 0, this.sections, 0, this.sections.length);
            } else {
                Chunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", achunksection.length, this.sections.length);
            }
        }

        // CraftBukkit start
        this.bukkitChunk = new org.bukkit.craftbukkit.CraftChunk(this);
    }

    public org.bukkit.Chunk bukkitChunk;
    public org.bukkit.Chunk getBukkitChunk() {
        return bukkitChunk;
    }

    public boolean mustNotSave;
    public boolean needsDecoration;
    // CraftBukkit end

    // Paper start
    public final com.destroystokyo.paper.util.maplist.EntityList entities = new com.destroystokyo.paper.util.maplist.EntityList();
    public PlayerChunk playerChunk;

    static final int NEIGHBOUR_CACHE_RADIUS = 3;
    public static int getNeighbourCacheRadius() {
        return NEIGHBOUR_CACHE_RADIUS;
    }

    boolean loadedTicketLevel;
    private long neighbourChunksLoadedBitset;
    private final Chunk[] loadedNeighbourChunks = new Chunk[(NEIGHBOUR_CACHE_RADIUS * 2 + 1) * (NEIGHBOUR_CACHE_RADIUS * 2 + 1)];

    private static int getNeighbourIndex(final int relativeX, final int relativeZ) {
        // index = (relativeX + NEIGHBOUR_CACHE_RADIUS) + (relativeZ + NEIGHBOUR_CACHE_RADIUS) * (NEIGHBOUR_CACHE_RADIUS * 2 + 1)
        // optimised variant of the above by moving some of the ops to compile time
        return relativeX + (relativeZ * (NEIGHBOUR_CACHE_RADIUS * 2 + 1)) + (NEIGHBOUR_CACHE_RADIUS + NEIGHBOUR_CACHE_RADIUS * ((NEIGHBOUR_CACHE_RADIUS * 2 + 1)));
    }

    public final Chunk getRelativeNeighbourIfLoaded(final int relativeX, final int relativeZ) {
        return this.loadedNeighbourChunks[getNeighbourIndex(relativeX, relativeZ)];
    }

    public final boolean isNeighbourLoaded(final int relativeX, final int relativeZ) {
        return (this.neighbourChunksLoadedBitset & (1L << getNeighbourIndex(relativeX, relativeZ))) != 0;
    }

    public final void setNeighbourLoaded(final int relativeX, final int relativeZ, final Chunk chunk) {
        if (chunk == null) {
            throw new IllegalArgumentException("Chunk must be non-null, neighbour: (" + relativeX + "," + relativeZ + "), chunk: " + this.loc);
        }
        final long before = this.neighbourChunksLoadedBitset;
        final int index = getNeighbourIndex(relativeX, relativeZ);
        this.loadedNeighbourChunks[index] = chunk;
        this.neighbourChunksLoadedBitset |= (1L << index);
        this.onNeighbourChange(before, this.neighbourChunksLoadedBitset);
    }

    public final void setNeighbourUnloaded(final int relativeX, final int relativeZ) {
        final long before = this.neighbourChunksLoadedBitset;
        final int index = getNeighbourIndex(relativeX, relativeZ);
        this.loadedNeighbourChunks[index] = null;
        this.neighbourChunksLoadedBitset &= ~(1L << index);
        this.onNeighbourChange(before, this.neighbourChunksLoadedBitset);
    }

    public final void resetNeighbours() {
        final long before = this.neighbourChunksLoadedBitset;
        this.neighbourChunksLoadedBitset = 0L;
        java.util.Arrays.fill(this.loadedNeighbourChunks, null);
        this.onNeighbourChange(before, 0L);
    }

    protected void onNeighbourChange(final long bitsetBefore, final long bitsetAfter) {
        // Paper start - no-tick view distance
        ChunkProviderServer chunkProviderServer = ((WorldServer)this.world).getChunkProvider();
        PlayerChunkMap chunkMap = chunkProviderServer.playerChunkMap;
        // this code handles the addition of ticking tickets - the distance map handles the removal
        if (!areNeighboursLoaded(bitsetBefore, 2) && areNeighboursLoaded(bitsetAfter, 2)) {
            if (chunkMap.playerViewDistanceTickMap.getObjectsInRange(this.coordinateKey) != null) {
                // now we're ready for entity ticking
                chunkProviderServer.serverThreadQueue.execute(() -> {
                    // double check that this condition still holds.
                    if (Chunk.this.areNeighboursLoaded(2) && chunkMap.playerViewDistanceTickMap.getObjectsInRange(Chunk.this.coordinateKey) != null) {
                        chunkProviderServer.addTicketAtLevel(TicketType.PLAYER, Chunk.this.loc, 31, Chunk.this.loc); // 31 -> entity ticking, TODO check on update
                    }
                });
            }
        }

        // this code handles the chunk sending
        if (!areNeighboursLoaded(bitsetBefore, 1) && areNeighboursLoaded(bitsetAfter, 1)) {
            if (chunkMap.playerViewDistanceBroadcastMap.getObjectsInRange(this.coordinateKey) != null) {
                // now we're ready to send
                chunkMap.mailboxMain.a(ChunkTaskQueueSorter.a(chunkMap.getUpdatingChunk(this.coordinateKey), (() -> { // Copied frm PlayerChunkMap
                    // double check that this condition still holds.
                    if (!Chunk.this.areNeighboursLoaded(1)) {
                        return;
                    }
                    com.destroystokyo.paper.util.misc.PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> inRange = chunkMap.playerViewDistanceBroadcastMap.getObjectsInRange(Chunk.this.coordinateKey);
                    if (inRange == null) {
                        return;
                    }

                    // broadcast
                    Object[] backingSet = inRange.getBackingSet();
                    Packet[] chunkPackets = new Packet[2];
                    for (int index = 0, len = backingSet.length; index < len; ++index) {
                        Object temp = backingSet[index];
                        if (!(temp instanceof EntityPlayer)) {
                            continue;
                        }
                        EntityPlayer player = (EntityPlayer)temp;
                        chunkMap.sendChunk(player, chunkPackets, Chunk.this);
                    }
                })));
            }
        }
        // Paper end - no-tick view distance
    }

    public final boolean isAnyNeighborsLoaded() {
        return neighbourChunksLoadedBitset != 0;
    }
    public final boolean areNeighboursLoaded(final int radius) {
        return Chunk.areNeighboursLoaded(this.neighbourChunksLoadedBitset, radius);
    }

    public static boolean areNeighboursLoaded(final long bitset, final int radius) {
        // index = relativeX + (relativeZ * (NEIGHBOUR_CACHE_RADIUS * 2 + 1)) + (NEIGHBOUR_CACHE_RADIUS + NEIGHBOUR_CACHE_RADIUS * ((NEIGHBOUR_CACHE_RADIUS * 2 + 1)))
        switch (radius) {
            case 0: {
                return (bitset & (1L << getNeighbourIndex(0, 0))) != 0;
            }
            case 1: {
                long mask = 0L;
                for (int dx = -1; dx <= 1; ++dx) {
                    for (int dz = -1; dz <= 1; ++dz) {
                        mask |= (1L << getNeighbourIndex(dx, dz));
                    }
                }
                return (bitset & mask) == mask;
            }
            case 2: {
                long mask = 0L;
                for (int dx = -2; dx <= 2; ++dx) {
                    for (int dz = -2; dz <= 2; ++dz) {
                        mask |= (1L << getNeighbourIndex(dx, dz));
                    }
                }
                return (bitset & mask) == mask;
            }
            case 3: {
                long mask = 0L;
                for (int dx = -3; dx <= 3; ++dx) {
                    for (int dz = -3; dz <= 3; ++dz) {
                        mask |= (1L << getNeighbourIndex(dx, dz));
                    }
                }
                return (bitset & mask) == mask;
            }

            default:
                throw new IllegalArgumentException("Radius not recognized: " + radius);
        }
    }
    // Paper end

    public Chunk(World world, ProtoChunk protochunk) {
        this(world, protochunk.getPos(), protochunk.getBiomeIndex(), protochunk.p(), protochunk.n(), protochunk.o(), protochunk.getInhabitedTime(), protochunk.getSections(), (Consumer) null);
        Iterator iterator = protochunk.y().iterator();

        while (iterator.hasNext()) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) iterator.next();

            EntityTypes.a(nbttagcompound, world, (entity) -> {
                this.a(entity);
                return entity;
            });
        }

        iterator = protochunk.x().values().iterator();

        while (iterator.hasNext()) {
            TileEntity tileentity = (TileEntity) iterator.next();

            this.a(tileentity);
        }

        this.e.putAll(protochunk.z());

        for (int i = 0; i < protochunk.l().length; ++i) {
            this.n[i] = protochunk.l()[i];
        }

        this.a(protochunk.h());
        this.b(protochunk.v());
        iterator = protochunk.f().iterator();

        while (iterator.hasNext()) {
            Entry<HeightMap.Type, HeightMap> entry = (Entry) iterator.next();

            if (ChunkStatus.FULL.h().contains(entry.getKey())) {
                this.a((HeightMap.Type) entry.getKey()).a(((HeightMap) entry.getValue()).a());
            }
        }

        this.b(protochunk.r());
        this.s = true;
        this.needsDecoration = true; // CraftBukkit
    }

    @Override
    public HeightMap a(HeightMap.Type heightmap_type) {
        return (HeightMap) this.heightMap.computeIfAbsent(heightmap_type, (heightmap_type1) -> {
            return new HeightMap(this, heightmap_type1);
        });
    }

    @Override
    public Set<BlockPosition> c() {
        Set<BlockPosition> set = Sets.newHashSet(this.e.keySet());

        set.addAll(this.tileEntities.keySet());
        return set;
    }

    @Override
    public ChunkSection[] getSections() {
        return this.sections;
    }

    // Paper start - Optimize getBlockData to reduce instructions
    public final IBlockData getBlockData(BlockPosition pos) { return getBlockData(pos.getX(), pos.getY(), pos.getZ()); } // Paper
    public IBlockData getType(BlockPosition blockposition) {
        return this.getBlockData(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    public IBlockData getType(final int x, final int y, final int z) {
        return getBlockData(x, y, z);
    }
    public final IBlockData getBlockData(final int x, final int y, final int z) {
        // Method body / logic copied from below
        final int i = y >> 4;
        if (y < 0 || i >= this.sections.length || this.sections[i] == null || this.sections[i].nonEmptyBlockCount == 0) {
            return Blocks.AIR.getBlockData();
        }
        // Inlined ChunkSection.getType() and DataPaletteBlock.a(int,int,int)
        return this.sections[i].blockIds.a((y & 15) << 8 | (z & 15) << 4 | x & 15);
    }

    public IBlockData getBlockData_unused(int i, int j, int k) {
        // Paper end
        if (this.world.isDebugWorld()) {
            IBlockData iblockdata = null;

            if (j == 60) {
                iblockdata = Blocks.BARRIER.getBlockData();
            }

            if (j == 70) {
                iblockdata = ChunkProviderDebug.b(i, k);
            }

            return iblockdata == null ? Blocks.AIR.getBlockData() : iblockdata;
        } else {
            try {
                if (j >= 0 && j >> 4 < this.sections.length) {
                    ChunkSection chunksection = this.sections[j >> 4];

                    if (!ChunkSection.a(chunksection)) {
                        return chunksection.getType(i & 15, j & 15, k & 15);
                    }
                }

                return Blocks.AIR.getBlockData();
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Getting block state");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");

                crashreportsystemdetails.a("Location", () -> {
                    return CrashReportSystemDetails.a(i, j, k);
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    // Paper start - If loaded util
    @Override
    public Fluid getFluidIfLoaded(BlockPosition blockposition) {
        return this.getFluid(blockposition);
    }

    @Override
    public IBlockData getTypeIfLoaded(BlockPosition blockposition) {
        return this.getType(blockposition);
    }
    // Paper end

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return this.a(blockposition.getX(), blockposition.getY(), blockposition.getZ());
    }

    public Fluid a(int i, int j, int k) {
        //try {  // Paper - remove try catch
        // Paper start - reduce the number of ops in this call
        int index = j >> 4;
            if (index >= 0 && index < this.sections.length) {
                ChunkSection chunksection = this.sections[index];

                if (chunksection != null) {
                    return chunksection.blockIds.a((j & 15) << 8 | (k & 15) << 4 | i & 15).getFluid();
                }
                // Paper end
            }

            return FluidTypes.EMPTY.h();
        /*} catch (Throwable throwable) { // Paper - remove try catch
            CrashReport crashreport = CrashReport.a(throwable, "Getting fluid state");
            CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being got");

            crashreportsystemdetails.a("Location", () -> {
                return CrashReportSystemDetails.a(i, j, k);
            });
            throw new ReportedException(crashreport);
        }
         */  // Paper - remove try catch
    }

    // CraftBukkit start
    @Nullable
    @Override
    public IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        return this.setType(blockposition, iblockdata, flag, true);
    }

    @Nullable
    public IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean flag, boolean doPlace) {
        // CraftBukkit end
        int i = blockposition.getX() & 15;
        int j = blockposition.getY();
        int k = blockposition.getZ() & 15;
        ChunkSection chunksection = this.sections[j >> 4];

        if (chunksection == Chunk.a) {
            if (iblockdata.isAir()) {
                return null;
            }

            chunksection = new ChunkSection(j >> 4 << 4, this, this.world, true); // Paper - Anti-Xray - Add parameters
            this.sections[j >> 4] = chunksection;
        }

        boolean flag1 = chunksection.c();
        IBlockData iblockdata1 = chunksection.setType(i, j & 15, k, iblockdata);

        if (iblockdata1 == iblockdata) {
            return null;
        } else {
            Block block = iblockdata.getBlock();
            Block block1 = iblockdata1.getBlock();

            ((HeightMap) this.heightMap.get(HeightMap.Type.MOTION_BLOCKING)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.MOTION_BLOCKING_NO_LEAVES)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.OCEAN_FLOOR)).a(i, j, k, iblockdata);
            ((HeightMap) this.heightMap.get(HeightMap.Type.WORLD_SURFACE)).a(i, j, k, iblockdata);
            boolean flag2 = chunksection.c();

            if (flag1 != flag2) {
                this.world.getChunkProvider().getLightEngine().a(blockposition, flag2);
            }

            if (!this.world.isClientSide) {
                iblockdata1.remove(this.world, blockposition, iblockdata, flag);
            } else if (block1 != block && block1 instanceof ITileEntity) {
                this.world.removeTileEntity(blockposition);
            }

            if (!chunksection.getType(i, j & 15, k).a(block)) {
                return null;
            } else {
                TileEntity tileentity;

                if (block1 instanceof ITileEntity) {
                    tileentity = this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity != null) {
                        tileentity.invalidateBlockCache();
                    }
                }

                // CraftBukkit - Don't place while processing the BlockPlaceEvent, unless it's a BlockContainer. Prevents blocks such as TNT from activating when cancelled.
                if (!this.world.isClientSide && doPlace && (!this.world.captureBlockStates || block instanceof BlockTileEntity)) {
                    iblockdata.onPlace(this.world, blockposition, iblockdata1, flag);
                }

                if (block instanceof ITileEntity) {
                    tileentity = this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
                    if (tileentity == null) {
                        tileentity = ((ITileEntity) block).createTile(this.world);
                        this.world.setTileEntity(blockposition, tileentity);
                    } else {
                        tileentity.invalidateBlockCache();
                    }
                }

                this.s = true;
                return iblockdata1;
            }
        }
    }

    @Nullable
    public LightEngine e() {
        return this.world.getChunkProvider().getLightEngine();
    }

    @Override
    public void a(Entity entity) {
        this.q = true;
        int i = MathHelper.floor(entity.locX() / 16.0D);
        int j = MathHelper.floor(entity.locZ() / 16.0D);

        if (i != this.loc.x || j != this.loc.z) {
            Chunk.LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", i, j, this.loc.x, this.loc.z, entity);
            entity.dead = true;
            return; // Paper
        }

        int k = MathHelper.floor(entity.locY() / 16.0D);

        if (k < 0) {
            k = 0;
        }

        if (k >= this.entitySlices.length) {
            k = this.entitySlices.length - 1;
        }
        // Paper - remove from any old list if its in one
        List<Entity> nextSlice = this.entitySlices[k]; // the next list to be added to
        List<Entity> currentSlice = entity.entitySlice;
        if (nextSlice == currentSlice) {
            if (World.DEBUG_ENTITIES) MinecraftServer.LOGGER.warn("Entity was already in this chunk!" + entity, new Throwable());
            return; // ??? silly plugins
        }
        if (currentSlice != null && currentSlice.contains(entity)) {
            // Still in an old chunk...
            if (World.DEBUG_ENTITIES) MinecraftServer.LOGGER.warn("Entity is still in another chunk!" + entity, new Throwable());
            Chunk chunk = entity.getCurrentChunk();
            if (chunk != null) {
                chunk.removeEntity(entity);
            } else {
                removeEntity(entity);
            }
            currentSlice.remove(entity); // Just incase the above did not remove from the previous slice
        }
        // Paper end

        if (!entity.inChunk || entity.getCurrentChunk() != this) entityCounts.increment(entity.getMinecraftKeyString()); // Paper
        entity.inChunk = true;
        entity.setCurrentChunk(this); // Paper
        entity.chunkX = this.loc.x;
        entity.chunkY = k;
        entity.chunkZ = this.loc.z;
        this.entities.add(entity); // Paper - per chunk entity list
        this.entitySlices[k].add(entity);
        // Paper start
        if (entity instanceof EntityItem) {
            itemCounts[k]++;
        } else if (entity instanceof IInventory) {
            inventoryEntityCounts[k]++;
        }
        // Paper end
        entity.entitySlice = this.entitySlices[k]; // Paper
        this.markDirty(); // Paper
    }

    @Override
    public void a(HeightMap.Type heightmap_type, long[] along) {
        ((HeightMap) this.heightMap.get(heightmap_type)).a(along);
    }

    public final void removeEntity(Entity entity) { this.b(entity); } // Paper - OBFHELPER
    public void b(Entity entity) {
        this.a(entity, entity.chunkY);
    }

    public void a(Entity entity, int i) {
        if (i < 0) {
            i = 0;
        }

        if (i >= this.entitySlices.length) {
            i = this.entitySlices.length - 1;
        }

        // Paper start
        if (entity.currentChunk != null && entity.currentChunk.get() == this) entity.setCurrentChunk(null);
        if (entitySlices[i] == entity.entitySlice) {
            entity.entitySlice = null;
            entity.inChunk = false;
        }
        if (!this.entitySlices[i].remove(entity)) {
            return;
        }
        if (entity instanceof EntityItem) {
            itemCounts[i]--;
        } else if (entity instanceof IInventory) {
            inventoryEntityCounts[i]--;
        }
        entityCounts.decrement(entity.getMinecraftKeyString());
        this.markDirty(); // Paper
        // Paper end
        this.entities.remove(entity); // Paper
    }

    public final int getHighestBlockY(HeightMap.Type heightmap_type, int i, int j) { return this.getHighestBlock(heightmap_type, i, j) + 1; } // Paper - sort of an obfhelper, but without -1
    @Override public int getHighestBlock(HeightMap.Type heightmap_type, int i, int j) { // Paper
        return ((HeightMap) this.heightMap.get(heightmap_type)).a(i & 15, j & 15) - 1;
    }

    @Nullable
    private TileEntity k(BlockPosition blockposition) {
        IBlockData iblockdata = this.getType(blockposition);
        Block block = iblockdata.getBlock();

        return !block.isTileEntity() ? null : ((ITileEntity) block).createTile(this.world);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        return this.a(blockposition, Chunk.EnumTileEntityState.CHECK);
    }

    @Nullable public final TileEntity getTileEntityImmediately(BlockPosition pos) { return this.a(pos, EnumTileEntityState.IMMEDIATE); } // Paper - OBFHELPER
    @Nullable
    public TileEntity a(BlockPosition blockposition, Chunk.EnumTileEntityState chunk_enumtileentitystate) {
        // CraftBukkit start
        TileEntity tileentity = world.capturedTileEntities.get(blockposition);
        if (tileentity == null) {
            tileentity = (TileEntity) this.tileEntities.get(blockposition);
        }
        // CraftBukkit end

        if (tileentity == null) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) this.e.remove(blockposition);

            if (nbttagcompound != null) {
                TileEntity tileentity1 = this.a(blockposition, nbttagcompound);

                if (tileentity1 != null) {
                    return tileentity1;
                }
            }
        }

        if (tileentity == null) {
            if (chunk_enumtileentitystate == Chunk.EnumTileEntityState.IMMEDIATE) {
                tileentity = this.k(blockposition);
                this.world.setTileEntity(blockposition, tileentity);
            }
        } else if (tileentity.isRemoved()) {
            this.tileEntities.remove(blockposition);
            return null;
        }

        return tileentity;
    }

    public void a(TileEntity tileentity) {
        this.setTileEntity(tileentity.getPosition(), tileentity);
        if (this.loaded || this.world.s_()) {
            this.world.setTileEntity(tileentity.getPosition(), tileentity);
        }

    }

    @Override
    public void setTileEntity(BlockPosition blockposition, TileEntity tileentity) {
        if (this.getType(blockposition).getBlock() instanceof ITileEntity) {
            tileentity.setLocation(this.world, blockposition);
            tileentity.r();
            TileEntity tileentity1 = (TileEntity) this.tileEntities.put(blockposition.immutableCopy(), tileentity);

            if (tileentity1 != null && tileentity1 != tileentity) {
                tileentity1.al_();
            }

            // CraftBukkit start
            // Paper start - Remove invalid mob spawner tile entities
        } else if (tileentity instanceof TileEntityMobSpawner && !(getBlockData(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getBlock() instanceof BlockMobSpawner)) {
            this.tileEntities.remove(blockposition);
            // Paper end
        } else {
            // Paper start
            ServerInternalException e = new ServerInternalException(
                    "Attempted to place a tile entity (" + tileentity + ") at " + tileentity.position.getX() + ","
                            + tileentity.position.getY() + "," + tileentity.position.getZ()
                            + " (" + getType(blockposition) + ") where there was no entity tile!\n" +
                            "Chunk coordinates: " + (this.loc.x * 16) + "," + (this.loc.z * 16));
            e.printStackTrace();
            ServerInternalException.reportInternalException(e);

            if (this.world.paperConfig.removeCorruptTEs) {
                this.removeTileEntity(tileentity.getPosition());
                this.markDirty();
                org.bukkit.Bukkit.getLogger().info("Removing corrupt tile entity");
            }
            // Paper end
            // CraftBukkit end
        }
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        this.e.put(new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z")), nbttagcompound);
    }

    @Nullable
    @Override
    public NBTTagCompound j(BlockPosition blockposition) {
        TileEntity tileentity = this.getTileEntity(blockposition);
        NBTTagCompound nbttagcompound;

        if (tileentity != null && !tileentity.isRemoved()) {
            nbttagcompound = tileentity.save(new NBTTagCompound());
            nbttagcompound.setBoolean("keepPacked", false);
            return nbttagcompound;
        } else {
            nbttagcompound = (NBTTagCompound) this.e.get(blockposition);
            if (nbttagcompound != null) {
                nbttagcompound = nbttagcompound.clone();
                nbttagcompound.setBoolean("keepPacked", true);
            }

            return nbttagcompound;
        }
    }

    @Override
    public void removeTileEntity(BlockPosition blockposition) {
        if (this.loaded || this.world.s_()) {
            TileEntity tileentity = (TileEntity) this.tileEntities.remove(blockposition);

            if (tileentity != null) {
                tileentity.al_();
            }
        }

    }

    public void addEntities() {
        if (this.v != null) {
            this.v.accept(this);
            this.v = null;
        }

    }

    // CraftBukkit start
    public void loadCallback() {
        // Paper start - neighbour cache
        int chunkX = this.loc.x;
        int chunkZ = this.loc.z;
        ChunkProviderServer chunkProvider = ((WorldServer)this.world).getChunkProvider();
        for (int dx = -NEIGHBOUR_CACHE_RADIUS; dx <= NEIGHBOUR_CACHE_RADIUS; ++dx) {
            for (int dz = -NEIGHBOUR_CACHE_RADIUS; dz <= NEIGHBOUR_CACHE_RADIUS; ++dz) {
                Chunk neighbour = chunkProvider.getChunkAtIfLoadedMainThreadNoCache(chunkX + dx, chunkZ + dz);
                if (neighbour != null) {
                    neighbour.setNeighbourLoaded(-dx, -dz, this);
                    // should be in cached already
                    this.setNeighbourLoaded(dx, dz, neighbour);
                }
            }
        }
        this.setNeighbourLoaded(0, 0, this);
        this.loadedTicketLevel = true;
        // Paper end - neighbour cache
        org.bukkit.Server server = this.world.getServer();
        ((WorldServer)this.world).getChunkProvider().addLoadedChunk(this); // Paper
        if (server != null) {
            /*
             * If it's a new world, the first few chunks are generated inside
             * the World constructor. We can't reliably alter that, so we have
             * no way of creating a CraftWorld/CraftServer at that point.
             */
            server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkLoadEvent(this.bukkitChunk, this.needsDecoration));

            if (this.needsDecoration) {
                try (co.aikar.timings.Timing ignored = this.world.timings.chunkLoadPopulate.startTiming()) { // Paper
                this.needsDecoration = false;
                java.util.Random random = new java.util.Random();
                random.setSeed(world.getSeed());
                long xRand = random.nextLong() / 2L * 2L + 1L;
                long zRand = random.nextLong() / 2L * 2L + 1L;
                random.setSeed((long) this.loc.x * xRand + (long) this.loc.z * zRand ^ world.getSeed());

                org.bukkit.World world = this.world.getWorld();
                if (world != null) {
                    this.world.populating = true;
                    try {
                        for (org.bukkit.generator.BlockPopulator populator : world.getPopulators()) {
                            populator.populate(world, random, bukkitChunk);
                        }
                    } finally {
                        this.world.populating = false;
                    }
                }
                server.getPluginManager().callEvent(new org.bukkit.event.world.ChunkPopulateEvent(bukkitChunk));
                } // Paper
            }
        }
    }

    public void unloadCallback() {
        org.bukkit.Server server = this.world.getServer();
        org.bukkit.event.world.ChunkUnloadEvent unloadEvent = new org.bukkit.event.world.ChunkUnloadEvent(this.bukkitChunk, this.isNeedsSaving());
        server.getPluginManager().callEvent(unloadEvent);
        // note: saving can be prevented, but not forced if no saving is actually required
        this.mustNotSave = !unloadEvent.isSaveChunk();
        ((WorldServer)this.world).getChunkProvider().removeLoadedChunk(this); // Paper
        // Paper start - neighbour cache
        int chunkX = this.loc.x;
        int chunkZ = this.loc.z;
        ChunkProviderServer chunkProvider = ((WorldServer)this.world).getChunkProvider();
        for (int dx = -NEIGHBOUR_CACHE_RADIUS; dx <= NEIGHBOUR_CACHE_RADIUS; ++dx) {
            for (int dz = -NEIGHBOUR_CACHE_RADIUS; dz <= NEIGHBOUR_CACHE_RADIUS; ++dz) {
                Chunk neighbour = chunkProvider.getChunkAtIfLoadedMainThreadNoCache(chunkX + dx, chunkZ + dz);
                if (neighbour != null) {
                    neighbour.setNeighbourUnloaded(-dx, -dz);
                }
            }
        }
        this.loadedTicketLevel = false;
        this.resetNeighbours();
        // Paper end
    }
    // CraftBukkit end

    public void markDirty() {
        this.s = true;
    }

    public void a(@Nullable Entity entity, AxisAlignedBB axisalignedbb, List<Entity> list, @Nullable Predicate<? super Entity> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            List<Entity> entityslice = this.entitySlices[k]; // Spigot
            List<Entity> list1 = entityslice; // Spigot
            int l = list1.size();

            for (int i1 = 0; i1 < l; ++i1) {
                Entity entity1 = (Entity) list1.get(i1);
                if (entity1.shouldBeRemoved) continue; // Paper

                if (entity1.getBoundingBox().c(axisalignedbb) && entity1 != entity) {
                    if (predicate == null || predicate.test(entity1)) {
                        list.add(entity1);
                    }

                    if (entity1 instanceof EntityEnderDragon) {
                        EntityComplexPart[] aentitycomplexpart = ((EntityEnderDragon) entity1).eJ();
                        int j1 = aentitycomplexpart.length;

                        for (int k1 = 0; k1 < j1; ++k1) {
                            EntityComplexPart entitycomplexpart = aentitycomplexpart[k1];

                            if (entitycomplexpart != entity && entitycomplexpart.getBoundingBox().c(axisalignedbb) && (predicate == null || predicate.test(entitycomplexpart))) {
                                list.add(entitycomplexpart);
                            }
                        }
                    }
                }
            }
        }

    }

    public <T extends Entity> void a(@Nullable EntityTypes<?> entitytypes, AxisAlignedBB axisalignedbb, List<? super T> list, Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        for (int k = i; k <= j; ++k) {
            Iterator iterator = this.entitySlices[k].iterator(); // Spigot

            // Paper start - Don't search for inventories if we have none, and that is all we want
            /*
             * We check if they want inventories by seeing if it is the static `IEntitySelector.d`
             *
             * Make sure the inventory selector stays in sync.
             * It should be the one that checks `var1 instanceof IInventory && var1.isAlive()`
             */
            if (predicate == IEntitySelector.isInventory() && inventoryEntityCounts[k] <= 0) continue;
            while (iterator.hasNext()) {
                T entity = (T) iterator.next(); // CraftBukkit - decompile error
                if (entity.shouldBeRemoved) continue; // Paper

                if ((entitytypes == null || entity.getEntityType() == entitytypes) && entity.getBoundingBox().c(axisalignedbb) && predicate.test(entity)) {
                    list.add(entity);
                }
            }
        }

    }

    public <T extends Entity> void a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, List<T> list, @Nullable Predicate<? super T> predicate) {
        int i = MathHelper.floor((axisalignedbb.minY - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxY + 2.0D) / 16.0D);

        i = MathHelper.clamp(i, 0, this.entitySlices.length - 1);
        j = MathHelper.clamp(j, 0, this.entitySlices.length - 1);

        // Paper start
        int[] counts;
        if (EntityItem.class.isAssignableFrom(oclass)) {
            counts = itemCounts;
        } else if (IInventory.class.isAssignableFrom(oclass)) {
            counts = inventoryEntityCounts;
        } else {
            counts = null;
        }
        // Paper end
        for (int k = i; k <= j; ++k) {
            if (counts != null && counts[k] <= 0) continue; // Paper - Don't check a chunk if it doesn't have the type we are looking for
            Iterator iterator = this.entitySlices[k].iterator(); // Spigot

            // Paper start - Don't search for inventories if we have none, and that is all we want
            /*
             * We check if they want inventories by seeing if it is the static `IEntitySelector.d`
             *
             * Make sure the inventory selector stays in sync.
             * It should be the one that checks `var1 instanceof IInventory && var1.isAlive()`
             */
            if (predicate == IEntitySelector.isInventory() && inventoryEntityCounts[k] <= 0) continue;
            // Paper end
            while (iterator.hasNext()) {
                T t0 = (T) iterator.next(); // CraftBukkit - decompile error
                if (t0.shouldBeRemoved) continue; // Paper

                if (oclass.isInstance(t0) && t0.getBoundingBox().c(axisalignedbb) && (predicate == null || predicate.test(t0))) { // Spigot - instance check
                    list.add(t0);
                }
            }
        }

    }

    public boolean isEmpty() {
        return false;
    }

    @Override
    public ChunkCoordIntPair getPos() {
        return this.loc;
    }

    @Override
    public BiomeStorage getBiomeIndex() {
        return this.d;
    }

    public void setLoaded(boolean flag) {
        this.loaded = flag;
    }

    public World getWorld() {
        return this.world;
    }

    @Override
    public Collection<Entry<HeightMap.Type, HeightMap>> f() {
        return Collections.unmodifiableSet(this.heightMap.entrySet());
    }

    public Map<BlockPosition, TileEntity> getTileEntities() {
        return this.tileEntities;
    }

    public List<Entity>[] getEntitySlices() { // Spigot
        return this.entitySlices;
    }

    @Override
    public NBTTagCompound i(BlockPosition blockposition) {
        return (NBTTagCompound) this.e.get(blockposition);
    }

    @Override
    public Stream<BlockPosition> m() {
        return StreamSupport.stream(BlockPosition.b(this.loc.d(), 0, this.loc.e(), this.loc.f(), 255, this.loc.g()).spliterator(), false).filter((blockposition) -> {
            return this.getType(blockposition).f() != 0;
        });
    }

    @Override
    public TickList<Block> n() {
        return this.o;
    }

    @Override
    public TickList<FluidType> o() {
        return this.p;
    }

    @Override
    public void setNeedsSaving(boolean flag) {
        this.s = flag;
    }

    @Override
    public boolean isNeedsSaving() {
        return (this.s || this.q && this.world.getTime() != this.lastSaved) && !this.mustNotSave; // CraftBukkit
    }

    public void d(boolean flag) {
        this.q = flag;
    }

    @Override
    public void setLastSaved(long i) {
        this.lastSaved = i;
    }

    @Nullable
    @Override
    public StructureStart<?> a(StructureGenerator<?> structuregenerator) {
        return (StructureStart) this.l.get(structuregenerator);
    }

    @Override
    public void a(StructureGenerator<?> structuregenerator, StructureStart<?> structurestart) {
        this.l.put(structuregenerator, structurestart);
    }

    @Override
    public Map<StructureGenerator<?>, StructureStart<?>> h() {
        return this.l;
    }

    @Override
    public void a(Map<StructureGenerator<?>, StructureStart<?>> map) {
        this.l.clear();
        this.l.putAll(map);
    }

    @Override
    public LongSet b(StructureGenerator<?> structuregenerator) {
        return (LongSet) this.m.computeIfAbsent(structuregenerator, (structuregenerator1) -> {
            return new LongOpenHashSet();
        });
    }

    @Override
    public void a(StructureGenerator<?> structuregenerator, long i) {
        ((LongSet) this.m.computeIfAbsent(structuregenerator, (structuregenerator1) -> {
            return new LongOpenHashSet();
        })).add(i);
    }

    @Override
    public Map<StructureGenerator<?>, LongSet> v() {
        return this.m;
    }

    @Override
    public void b(Map<StructureGenerator<?>, LongSet> map) {
        this.m.clear();
        this.m.putAll(map);
    }

    @Override
    public long getInhabitedTime() {
        return world.paperConfig.fixedInhabitedTime < 0 ? this.inhabitedTime : world.paperConfig.fixedInhabitedTime; // Paper
    }

    @Override
    public void setInhabitedTime(long i) {
        this.inhabitedTime = i;
    }

    public void A() {
        ChunkCoordIntPair chunkcoordintpair = this.getPos();

        for (int i = 0; i < this.n.length; ++i) {
            if (this.n[i] != null) {
                ShortListIterator shortlistiterator = this.n[i].iterator();

                while (shortlistiterator.hasNext()) {
                    Short oshort = (Short) shortlistiterator.next();
                    BlockPosition blockposition = ProtoChunk.a(oshort, i, chunkcoordintpair);
                    IBlockData iblockdata = this.getType(blockposition);
                    IBlockData iblockdata1 = Block.b(iblockdata, (GeneratorAccess) this.world, blockposition);

                    this.world.setTypeAndData(blockposition, iblockdata1, 20);
                }

                this.n[i].clear();
            }
        }

        this.B();
        Iterator iterator = Sets.newHashSet(this.e.keySet()).iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition1 = (BlockPosition) iterator.next();

            this.getTileEntity(blockposition1);
        }

        this.e.clear();
        this.i.a(this);
    }

    @Nullable
    private TileEntity a(BlockPosition blockposition, NBTTagCompound nbttagcompound) {
        IBlockData iblockdata = this.getType(blockposition);
        TileEntity tileentity;

        if ("DUMMY".equals(nbttagcompound.getString("id"))) {
            Block block = iblockdata.getBlock();

            if (block instanceof ITileEntity) {
                tileentity = ((ITileEntity) block).createTile(this.world);
            } else {
                tileentity = null;
                Chunk.LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", blockposition, iblockdata);
            }
        } else {
            tileentity = TileEntity.create(iblockdata, nbttagcompound);
        }

        if (tileentity != null) {
            tileentity.setLocation(this.world, blockposition);
            this.a(tileentity);
        } else {
            Chunk.LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", iblockdata, blockposition);
        }

        return tileentity;
    }

    @Override
    public ChunkConverter p() {
        return this.i;
    }

    @Override
    public ShortList[] l() {
        return this.n;
    }

    public void B() {
        if (this.o instanceof ProtoChunkTickList) {
            ((ProtoChunkTickList<Block>) this.o).a(this.world.getBlockTickList(), (blockposition) -> { // CraftBukkit - decompile error
                return this.getType(blockposition).getBlock();
            });
            this.o = TickListEmpty.b();
        } else if (this.o instanceof TickListChunk) {
            ((TickListChunk) this.o).a(this.world.getBlockTickList());
            this.o = TickListEmpty.b();
        }

        if (this.p instanceof ProtoChunkTickList) {
            ((ProtoChunkTickList<FluidType>) this.p).a(this.world.getFluidTickList(), (blockposition) -> { // CraftBukkit - decompile error
                return this.getFluid(blockposition).getType();
            });
            this.p = TickListEmpty.b();
        } else if (this.p instanceof TickListChunk) {
            ((TickListChunk) this.p).a(this.world.getFluidTickList());
            this.p = TickListEmpty.b();
        }

    }

    public void a(WorldServer worldserver) {
        if (this.o == TickListEmpty.<Block>b()) { // CraftBukkit - decompile error
            this.o = new TickListChunk<>(IRegistry.BLOCK::getKey, worldserver.getBlockTickList().a(this.loc, true, false), worldserver.getTime());
            this.setNeedsSaving(true);
        }

        if (this.p == TickListEmpty.<FluidType>b()) { // CraftBukkit - decompile error
            this.p = new TickListChunk<>(IRegistry.FLUID::getKey, worldserver.getFluidTickList().a(this.loc, true, false), worldserver.getTime());
            this.setNeedsSaving(true);
        }

    }

    @Override
    public ChunkStatus getChunkStatus() {
        return ChunkStatus.FULL;
    }

    public PlayerChunk.State getState() {
        return this.u == null ? PlayerChunk.State.BORDER : (PlayerChunk.State) this.u.get();
    }

    public void a(Supplier<PlayerChunk.State> supplier) {
        this.u = supplier;
    }

    @Override
    public boolean r() {
        return this.x;
    }

    @Override
    public void b(boolean flag) {
        this.x = flag;
        this.setNeedsSaving(true);
    }

    public static enum EnumTileEntityState {

        IMMEDIATE, QUEUED, CHECK;

        private EnumTileEntityState() {}
    }
}
