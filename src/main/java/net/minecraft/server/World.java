package net.minecraft.server;

import co.aikar.timings.Timing;
import co.aikar.timings.Timings;
import com.destroystokyo.paper.antixray.ChunkPacketBlockController; // Paper - Anti-Xray
import com.destroystokyo.paper.antixray.ChunkPacketBlockControllerAntiXray; // Paper - Anti-Xray
import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerInternalException;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CapturedBlockState;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.event.block.BlockPhysicsEvent;
// CraftBukkit end

public abstract class World implements GeneratorAccess, AutoCloseable {

    protected static final Logger LOGGER = LogManager.getLogger();
    public static final Codec<ResourceKey<World>> f = MinecraftKey.a.xmap(ResourceKey.b(IRegistry.L), ResourceKey::a);
    public static final ResourceKey<World> OVERWORLD = ResourceKey.a(IRegistry.L, new MinecraftKey("overworld"));
    public static final ResourceKey<World> THE_NETHER = ResourceKey.a(IRegistry.L, new MinecraftKey("the_nether"));
    public static final ResourceKey<World> THE_END = ResourceKey.a(IRegistry.L, new MinecraftKey("the_end"));
    private static final EnumDirection[] a = EnumDirection.values();
    //public final List<TileEntity> tileEntityList = Lists.newArrayList(); // Paper - remove unused list
    public final List<TileEntity> tileEntityListTick = Lists.newArrayList();
    protected final List<TileEntity> tileEntityListPending = Lists.newArrayList();
    protected final java.util.Set<TileEntity> tileEntityListUnload = com.google.common.collect.Sets.newHashSet();
    public final Thread serverThread;
    private final boolean debugWorld;
    private int d;
    protected int n = (new Random()).nextInt();
    protected final int o = 1013904223;
    protected float lastRainLevel;
    protected float rainLevel;
    protected float lastThunderLevel;
    protected float thunderLevel;
    public final Random random = new Random();
    private final DimensionManager x;
    public final WorldDataMutable worldData;
    private final Supplier<GameProfilerFiller> methodProfiler;
    public final boolean isClientSide;
    protected boolean tickingTileEntities;
    private final WorldBorder worldBorder;
    private final BiomeManager biomeManager;
    private final ResourceKey<World> dimensionKey;

    // CraftBukkit start Added the following
    private final ResourceKey<DimensionManager> typeKey;
    private final CraftWorld world;
    public boolean pvpMode;
    public boolean keepSpawnInMemory = true;
    public org.bukkit.generator.ChunkGenerator generator;
    public static final boolean DEBUG_ENTITIES = Boolean.getBoolean("debug.entities"); // Paper

    public boolean captureBlockStates = false;
    public boolean captureTreeGeneration = false;
    public Map<BlockPosition, org.bukkit.craftbukkit.block.CraftBlockState> capturedBlockStates = new java.util.LinkedHashMap<>(); // Paper
    public Map<BlockPosition, TileEntity> capturedTileEntities = new HashMap<>();
    public List<EntityItem> captureDrops;
    public long ticksPerAnimalSpawns;
    public long ticksPerMonsterSpawns;
    public long ticksPerWaterSpawns;
    public long ticksPerWaterAmbientSpawns;
    public long ticksPerAmbientSpawns;
    // Paper start
    public int wakeupInactiveRemainingAnimals;
    public int wakeupInactiveRemainingFlying;
    public int wakeupInactiveRemainingMonsters;
    public int wakeupInactiveRemainingVillagers;
    // Paper end
    public boolean populating;
    public final org.spigotmc.SpigotWorldConfig spigotConfig; // Spigot

    public final com.destroystokyo.paper.PaperWorldConfig paperConfig; // Paper
    public final ChunkPacketBlockController chunkPacketBlockController; // Paper - Anti-Xray

    public final co.aikar.timings.WorldTimingsHandler timings; // Paper
    public static BlockPosition lastPhysicsProblem; // Spigot
    private org.spigotmc.TickLimiter entityLimiter;
    private org.spigotmc.TickLimiter tileLimiter;
    private int tileTickPosition;
    public final Map<Explosion.CacheKey, Float> explosionDensityCache = new HashMap<>(); // Paper - Optimize explosions
    public java.util.ArrayDeque<BlockRedstoneTorch.RedstoneUpdateInfo> redstoneUpdateInfos; // Paper - Move from Map in BlockRedstoneTorch to here

    public CraftWorld getWorld() {
        return this.world;
    }

    public CraftServer getServer() {
        return (CraftServer) Bukkit.getServer();
    }

    // Paper start
    @Override
    public boolean isChunkLoaded(int x, int z) {
        return ((WorldServer)this).getChunkIfLoaded(x, z) != null;
    }
    // Paper end

    public ResourceKey<DimensionManager> getTypeKey() {
        return typeKey;
    }

    protected World(WorldDataMutable worlddatamutable, ResourceKey<World> resourcekey, final DimensionManager dimensionmanager, Supplier<GameProfilerFiller> supplier, boolean flag, boolean flag1, long i, org.bukkit.generator.ChunkGenerator gen, org.bukkit.World.Environment env, java.util.concurrent.Executor executor) { // Paper
        this.spigotConfig = new org.spigotmc.SpigotWorldConfig(((WorldDataServer) worlddatamutable).getName()); // Spigot
        this.paperConfig = new com.destroystokyo.paper.PaperWorldConfig((((WorldDataServer)worlddatamutable).getName()), this.spigotConfig); // Paper
        this.chunkPacketBlockController = this.paperConfig.antiXray ? new ChunkPacketBlockControllerAntiXray(this, executor) : ChunkPacketBlockController.NO_OPERATION_INSTANCE; // Paper - Anti-Xray
        this.generator = gen;
        this.world = new CraftWorld((WorldServer) this, gen, env);
        this.ticksPerAnimalSpawns = this.getServer().getTicksPerAnimalSpawns(); // CraftBukkit
        this.ticksPerMonsterSpawns = this.getServer().getTicksPerMonsterSpawns(); // CraftBukkit
        this.ticksPerWaterSpawns = this.getServer().getTicksPerWaterSpawns(); // CraftBukkit
        this.ticksPerWaterAmbientSpawns = this.getServer().getTicksPerWaterAmbientSpawns(); // CraftBukkit
        this.ticksPerAmbientSpawns = this.getServer().getTicksPerAmbientSpawns(); // CraftBukkit
        this.typeKey = (ResourceKey) this.getServer().getHandle().getServer().customRegistry.a().c(dimensionmanager).orElseThrow(() -> {
            return new IllegalStateException("Unregistered dimension type: " + dimensionmanager);
        });
        // CraftBukkit end
        this.methodProfiler = supplier;
        this.worldData = worlddatamutable;
        this.x = dimensionmanager;
        this.dimensionKey = resourcekey;
        this.isClientSide = flag;
        if (dimensionmanager.getCoordinateScale() != 1.0D) {
            this.worldBorder = new WorldBorder() {
                @Override
                public double getCenterX() {
                    return super.getCenterX(); // CraftBukkit
                }

                @Override
                public double getCenterZ() {
                    return super.getCenterZ(); // CraftBukkit
                }
            };
        } else {
            this.worldBorder = new WorldBorder();
        }

        this.serverThread = Thread.currentThread();
        this.biomeManager = new BiomeManager(this, i, dimensionmanager.getGenLayerZoomer());
        this.debugWorld = flag1;
        // CraftBukkit start
        getWorldBorder().world = (WorldServer) this;
        // From PlayerList.setPlayerFileData
        getWorldBorder().a(new IWorldBorderListener() {
            public void a(WorldBorder worldborder, double d0) {
                getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_SIZE), worldborder.world);
            }

            public void a(WorldBorder worldborder, double d0, double d1, long i) {
                getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.LERP_SIZE), worldborder.world);
            }

            public void a(WorldBorder worldborder, double d0, double d1) {
                getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_CENTER), worldborder.world);
            }

            public void a(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_TIME), worldborder.world);
            }

            public void b(WorldBorder worldborder, int i) {
                getServer().getHandle().sendAll(new PacketPlayOutWorldBorder(worldborder, PacketPlayOutWorldBorder.EnumWorldBorderAction.SET_WARNING_BLOCKS), worldborder.world);
            }

            public void b(WorldBorder worldborder, double d0) {}

            public void c(WorldBorder worldborder, double d0) {}
        });
        // CraftBukkit end
        timings = new co.aikar.timings.WorldTimingsHandler(this); // Paper - code below can generate new world and access timings
        this.keepSpawnInMemory = this.paperConfig.keepSpawnInMemory; // Paper
        this.entityLimiter = new org.spigotmc.TickLimiter(spigotConfig.entityMaxTickTime);
        this.tileLimiter = new org.spigotmc.TickLimiter(spigotConfig.tileMaxTickTime);
    }

    // Paper start
    // ret true if no collision
    public final boolean checkEntityCollision(IBlockData data, Entity source, VoxelShapeCollision voxelshapedcollision,
                                              BlockPosition position, boolean checkCanSee) {
        // Copied from IWorldReader#a(IBlockData, BlockPosition, VoxelShapeCollision) & EntityAccess#a(Entity, VoxelShape)
        VoxelShape voxelshape = data.getCollisionShape(this, position, voxelshapedcollision);
        if (voxelshape.isEmpty()) {
            return true;
        }

        voxelshape = voxelshape.offset((double) position.getX(), (double) position.getY(), (double) position.getZ());
        if (voxelshape.isEmpty()) {
            return true;
        }

        List<Entity> entities = this.getEntities(null, voxelshape.getBoundingBox());
        for (int i = 0, len = entities.size(); i < len; ++i) {
            Entity entity = entities.get(i);

            if (checkCanSee && source instanceof EntityPlayer && entity instanceof EntityPlayer
                && !((EntityPlayer) source).getBukkitEntity().canSee(((EntityPlayer) entity).getBukkitEntity())) {
                continue;
            }

            // !entity1.dead && entity1.i && (entity == null || !entity1.x(entity));
            // elide the last check since vanilla calls with entity = null
            // only we care about the source for the canSee check
            if (entity.dead || !entity.blocksEntitySpawning()) {
                continue;
            }

            if (VoxelShapes.applyOperation(voxelshape, VoxelShapes.of(entity.getBoundingBox()), OperatorBoolean.AND)) {
                return false;
            }
        }

        return true;
    }
    // Paper end

    // Paper start - moved up from WorldServer
    public BlockPosition getSpawn() {
        BlockPosition blockposition = new BlockPosition(this.worldData.a(), this.worldData.b(), this.worldData.c());

        if (!this.getWorldBorder().a(blockposition)) {
            blockposition = this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, new BlockPosition(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
        }

        return blockposition;
    }
    // Paper end
    @Override
    public boolean s_() {
        return this.isClientSide;
    }

    @Nullable
    public MinecraftServer getMinecraftServer() {
        return null;
    }

    public static boolean isValidLocation(BlockPosition blockposition) {
        return blockposition.isValidLocation(); // Paper - use better/optimized check
    }

    public static boolean l(BlockPosition blockposition) {
        return !d(blockposition.getY()) && D(blockposition);
    }

    private static boolean D(BlockPosition blockposition) {
        return blockposition.getX() >= -30000000 && blockposition.getZ() >= -30000000 && blockposition.getX() < 30000000 && blockposition.getZ() < 30000000;
    }

    private static boolean d(int i) {
        return i < -20000000 || i >= 20000000;
    }

    public static boolean isOutsideWorld(BlockPosition blockposition) {
        return b(blockposition.getY());
    }

    public static boolean b(int i) {
        return i < 0 || i >= 256;
    }

    public final Chunk getChunkAtWorldCoords(BlockPosition blockposition) { // Paper - help inline
        return this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    @Override
    public final Chunk getChunkAt(int i, int j) { // Paper - final to help inline
        return (Chunk) this.getChunkAt(i, j, ChunkStatus.FULL, true); // Paper - avoid a method jump
    }

    // Paper start - if loaded
    @Nullable
    @Override
    public final IChunkAccess getChunkIfLoadedImmediately(int x, int z) {
        return ((WorldServer)this).chunkProvider.getChunkAtIfLoadedImmediately(x, z);
    }

    @Override
    public final IBlockData getTypeIfLoaded(BlockPosition blockposition) {
        // CraftBukkit start - tree generation
        if (captureTreeGeneration) {
            CraftBlockState previous = capturedBlockStates.get(blockposition);
            if (previous != null) {
                return previous.getHandle();
            }
        }
        // CraftBukkit end
        if (!isValidLocation(blockposition)) {
            return Blocks.AIR.getBlockData();
        }
        IChunkAccess chunk = this.getChunkIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4);

        return chunk == null ? null : chunk.getType(blockposition);
    }

    @Override
    public final Fluid getFluidIfLoaded(BlockPosition blockposition) {
        IChunkAccess chunk = this.getChunkIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4);

        return chunk == null ? null : chunk.getFluid(blockposition);
    }

    public final boolean isLoaded(BlockPosition blockposition) {
        return getChunkIfLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4) != null; // Paper
    }

    public final boolean isLoadedAndInBounds(BlockPosition blockposition) { // Paper - final for inline
        return getWorldBorder().isInBounds(blockposition) && getChunkIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4) != null;
    }

    public Chunk getChunkIfLoaded(int x, int z) { // Overridden in WorldServer for ABI compat which has final
        return ((WorldServer) this).getChunkProvider().getChunkAtIfLoadedImmediately(x, z);
    }
    public final Chunk getChunkIfLoaded(BlockPosition blockposition) {
        return ((WorldServer) this).getChunkProvider().getChunkAtIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4);
    }

    //  reduces need to do isLoaded before getType
    public final IBlockData getTypeIfLoadedAndInBounds(BlockPosition blockposition) {
        return getWorldBorder().isInBounds(blockposition) ? getTypeIfLoaded(blockposition) : null;
    }
    // Paper end

    @Override
    public final IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus, boolean flag) { // Paper - final for inline
        IChunkAccess ichunkaccess = this.getChunkProvider().getChunkAt(i, j, chunkstatus, flag);

        if (ichunkaccess == null && flag) {
            throw new IllegalStateException("Should always be able to create a chunk!");
        } else {
            return ichunkaccess;
        }
    }

    @Override
    public final boolean setTypeAndData(BlockPosition blockposition, IBlockData iblockdata, int i) { // Paper - final for inline
        return this.a(blockposition, iblockdata, i, 512);
    }

    @Override
    public boolean a(BlockPosition blockposition, IBlockData iblockdata, int i, int j) {
        // CraftBukkit start - tree generation
        if (this.captureTreeGeneration) {
            CraftBlockState blockstate = capturedBlockStates.get(blockposition);
            if (blockstate == null) {
                blockstate = CapturedBlockState.getTreeBlockState(this, blockposition, i);
                this.capturedBlockStates.put(blockposition.immutableCopy(), blockstate);
            }
            blockstate.setData(iblockdata);
            return true;
        }
        // CraftBukkit end
        if (isOutsideWorld(blockposition)) {
            return false;
        } else if (!this.isClientSide && this.isDebugWorld()) {
            return false;
        } else {
            Chunk chunk = this.getChunkAtWorldCoords(blockposition);
            Block block = iblockdata.getBlock();

            // CraftBukkit start - capture blockstates
            boolean captured = false;
            if (this.captureBlockStates && !this.capturedBlockStates.containsKey(blockposition)) {
                CraftBlockState blockstate = (CraftBlockState) world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()).getState(); // Paper - use CB getState to get a suitable snapshot
                this.capturedBlockStates.put(blockposition.immutableCopy(), blockstate);
                captured = true;
            }
            // CraftBukkit end

            IBlockData iblockdata1 = chunk.setType(blockposition, iblockdata, (i & 64) != 0, (i & 1024) == 0); // CraftBukkit custom NO_PLACE flag
            this.chunkPacketBlockController.onBlockChange(this, blockposition, iblockdata, iblockdata1, i); // Paper - Anti-Xray

            if (iblockdata1 == null) {
                // CraftBukkit start - remove blockstate if failed (or the same)
                if (this.captureBlockStates && captured) {
                    this.capturedBlockStates.remove(blockposition);
                }
                // CraftBukkit end
                return false;
            } else {
                IBlockData iblockdata2 = this.getType(blockposition);

                if ((i & 128) == 0 && iblockdata2 != iblockdata1 && (iblockdata2.b((IBlockAccess) this, blockposition) != iblockdata1.b((IBlockAccess) this, blockposition) || iblockdata2.f() != iblockdata1.f() || iblockdata2.e() || iblockdata1.e())) {
                    this.getMethodProfiler().enter("queueCheckLight");
                    this.getChunkProvider().getLightEngine().a(blockposition);
                    this.getMethodProfiler().exit();
                }

                /*
                if (iblockdata2 == iblockdata) {
                    if (iblockdata1 != iblockdata2) {
                        this.b(blockposition, iblockdata1, iblockdata2);
                    }

                    if ((i & 2) != 0 && (!this.isClientSide || (i & 4) == 0) && (this.isClientSide || chunk.getState() != null && chunk.getState().isAtLeast(PlayerChunk.State.TICKING))) {
                        this.notify(blockposition, iblockdata1, iblockdata, i);
                    }

                    if ((i & 1) != 0) {
                        this.update(blockposition, iblockdata1.getBlock());
                        if (!this.isClientSide && iblockdata.isComplexRedstone()) {
                            this.updateAdjacentComparators(blockposition, block);
                        }
                    }

                    if ((i & 16) == 0 && j > 0) {
                        int k = i & -34;

                        iblockdata1.b(this, blockposition, k, j - 1);
                        iblockdata.a((GeneratorAccess) this, blockposition, k, j - 1);
                        iblockdata.b(this, blockposition, k, j - 1);
                    }

                    this.a(blockposition, iblockdata1, iblockdata2);
                }
                */

                // CraftBukkit start
                if (!this.captureBlockStates) { // Don't notify clients or update physics while capturing blockstates
                    // Modularize client and physic updates
                    // Spigot start
                    try {
                        notifyAndUpdatePhysics(blockposition, chunk, iblockdata1, iblockdata, iblockdata2, i, j);
                    } catch (StackOverflowError ex) {
                        lastPhysicsProblem = new BlockPosition(blockposition);
                    }
                    // Spigot end
                }
                // CraftBukkit end

                return true;
            }
        }
    }

    // CraftBukkit start - Split off from above in order to directly send client and physic updates
    public void notifyAndUpdatePhysics(BlockPosition blockposition, Chunk chunk, IBlockData oldBlock, IBlockData newBlock, IBlockData actualBlock, int i, int j) {
        IBlockData iblockdata = newBlock;
        IBlockData iblockdata1 = oldBlock;
        IBlockData iblockdata2 = actualBlock;
        if (iblockdata2 == iblockdata) {
            if (iblockdata1 != iblockdata2) {
                this.b(blockposition, iblockdata1, iblockdata2);
            }

            if ((i & 2) != 0 && (!this.isClientSide || (i & 4) == 0) && (this.isClientSide || chunk == null || (chunk.getState() != null && chunk.getState().isAtLeast(PlayerChunk.State.TICKING)))) { // allow chunk to be null here as chunk.isReady() is false when we send our notification during block placement
                this.notify(blockposition, iblockdata1, iblockdata, i);
            }

            if ((i & 1) != 0) {
                this.update(blockposition, iblockdata1.getBlock());
                if (!this.isClientSide && iblockdata.isComplexRedstone()) {
                    this.updateAdjacentComparators(blockposition, newBlock.getBlock());
                }
            }

            if ((i & 16) == 0 && j > 0) {
                int k = i & -34;

                // CraftBukkit start
                iblockdata1.b(this, blockposition, k, j - 1); // Don't call an event for the old block to limit event spam
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null && ((WorldServer)this).hasPhysicsEvent) { // Paper
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftBlockData.fromData(iblockdata));
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end
                iblockdata.a((GeneratorAccess) this, blockposition, k, j - 1);
                iblockdata.b(this, blockposition, k, j - 1);
            }

            this.a(blockposition, iblockdata1, iblockdata2);
        }
    }
    // CraftBukkit end

    public void a(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1) {}

    public boolean setAir(BlockPosition blockposition) { return this.a(blockposition, false); } // Paper - OBFHELPER
    public boolean setAir(BlockPosition blockposition, boolean moved) { return this.a(blockposition, moved); } // Paper - OBFHELPER
    @Override public boolean a(BlockPosition blockposition, boolean flag) { // Paper - OBFHELPER
        Fluid fluid = this.getFluid(blockposition);

        return this.setTypeAndData(blockposition, fluid.getBlockData(), 3 | (flag ? 64 : 0));
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag, @Nullable Entity entity, int i) {
        IBlockData iblockdata = this.getType(blockposition);

        if (iblockdata.isAir()) {
            return false;
        } else {
            Fluid fluid = this.getFluid(blockposition);
            // Paper start - while the above setAir method is named same and looks very similar
            // they are NOT used with same intent and the above should not fire this event. The above method is more of a BlockSetToAirEvent,
            // it doesn't imply destruction of a block that plays a sound effect / drops an item.
            boolean playEffect = true;
            if (com.destroystokyo.paper.event.block.BlockDestroyEvent.getHandlerList().getRegisteredListeners().length > 0) {
                com.destroystokyo.paper.event.block.BlockDestroyEvent event = new com.destroystokyo.paper.event.block.BlockDestroyEvent(MCUtil.toBukkitBlock(this, blockposition), fluid.getBlockData().createCraftBlockData(), flag);
                if (!event.callEvent()) {
                    return false;
                }
                playEffect = event.playEffect();
            }
            // Paper end

            if (playEffect && !(iblockdata.getBlock() instanceof BlockFireAbstract)) { // Paper
                this.triggerEffect(2001, blockposition, Block.getCombinedId(iblockdata));
            }

            if (flag) {
                TileEntity tileentity = iblockdata.getBlock().isTileEntity() ? this.getTileEntity(blockposition) : null;

                Block.dropItems(iblockdata, this, blockposition, tileentity, entity, ItemStack.b);
            }

            return this.a(blockposition, fluid.getBlockData(), 3, i);
        }
    }

    public boolean setTypeUpdate(BlockPosition blockposition, IBlockData iblockdata) {
        return this.setTypeAndData(blockposition, iblockdata, 3);
    }

    public abstract void notify(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, int i);

    public void b(BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1) {}

    public void applyPhysics(BlockPosition blockposition, Block block) {
        if (captureBlockStates) { return; } // Paper - Cancel all physics during placement
        this.a(blockposition.west(), block, blockposition);
        this.a(blockposition.east(), block, blockposition);
        this.a(blockposition.down(), block, blockposition);
        this.a(blockposition.up(), block, blockposition);
        this.a(blockposition.north(), block, blockposition);
        this.a(blockposition.south(), block, blockposition);
    }

    public void a(BlockPosition blockposition, Block block, EnumDirection enumdirection) {
        if (enumdirection != EnumDirection.WEST) {
            this.a(blockposition.west(), block, blockposition);
        }

        if (enumdirection != EnumDirection.EAST) {
            this.a(blockposition.east(), block, blockposition);
        }

        if (enumdirection != EnumDirection.DOWN) {
            this.a(blockposition.down(), block, blockposition);
        }

        if (enumdirection != EnumDirection.UP) {
            this.a(blockposition.up(), block, blockposition);
        }

        if (enumdirection != EnumDirection.NORTH) {
            this.a(blockposition.north(), block, blockposition);
        }

        if (enumdirection != EnumDirection.SOUTH) {
            this.a(blockposition.south(), block, blockposition);
        }

    }

    public void a(BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        if (!this.isClientSide) {
            IBlockData iblockdata = this.getType(blockposition);

            try {
                // CraftBukkit start
                CraftWorld world = ((WorldServer) this).getWorld();
                if (world != null && ((WorldServer)this).hasPhysicsEvent) { // Paper
                    BlockPhysicsEvent event = new BlockPhysicsEvent(world.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()), CraftBlockData.fromData(iblockdata), world.getBlockAt(blockposition1.getX(), blockposition1.getY(), blockposition1.getZ()));
                    this.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return;
                    }
                }
                // CraftBukkit end
                iblockdata.doPhysics(this, blockposition, block, blockposition1, false);
            // Spigot Start
            } catch (StackOverflowError ex) {
                lastPhysicsProblem = new BlockPosition(blockposition);
                // Spigot End
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.a(throwable, "Exception while updating neighbours");
                CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Block being updated");

                crashreportsystemdetails.a("Source block type", () -> {
                    try {
                        return String.format("ID #%s (%s // %s)", IRegistry.BLOCK.getKey(block), block.i(), block.getClass().getCanonicalName());
                    } catch (Throwable throwable1) {
                        return "ID #" + IRegistry.BLOCK.getKey(block);
                    }
                });
                CrashReportSystemDetails.a(crashreportsystemdetails, blockposition, iblockdata);
                throw new ReportedException(crashreport);
            }
        }
    }

    public final int getHighestBlockY(final HeightMap.Type heightmap, final int x, final int z) { return this.a(heightmap, x, z); } // Paper - OBFHELPER
    @Override public int a(HeightMap.Type heightmap_type, int i, int j) { // Paper - OBFHELPER
        int k;

        if (i >= -30000000 && j >= -30000000 && i < 30000000 && j < 30000000) {
            if (this.isChunkLoaded(i >> 4, j >> 4)) {
                k = this.getChunkAt(i >> 4, j >> 4).getHighestBlock(heightmap_type, i & 15, j & 15) + 1;
            } else {
                k = 0;
            }
        } else {
            k = this.getSeaLevel() + 1;
        }

        return k;
    }

    @Override
    public LightEngine e() {
        return this.getChunkProvider().getLightEngine();
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        // CraftBukkit start - tree generation
        if (captureTreeGeneration) {
            CraftBlockState previous = capturedBlockStates.get(blockposition); // Paper
            if (previous != null) {
                return previous.getHandle();
            }
        }
        // CraftBukkit end
        if (isOutsideWorld(blockposition)) {
            return Blocks.VOID_AIR.getBlockData();
        } else {
            Chunk chunk = this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4);

            return chunk.getType(blockposition);
        }
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        if (isOutsideWorld(blockposition)) {
            return FluidTypes.EMPTY.h();
        } else {
            Chunk chunk = this.getChunkAtWorldCoords(blockposition);

            return chunk.getFluid(blockposition);
        }
    }

    public boolean isDay() {
        return !this.getDimensionManager().isFixedTime() && this.d < 4;
    }

    public boolean isNight() {
        return !this.getDimensionManager().isFixedTime() && !this.isDay();
    }

    @Override
    public void playSound(@Nullable EntityHuman entityhuman, BlockPosition blockposition, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {
        this.playSound(entityhuman, (double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, soundeffect, soundcategory, f, f1);
    }

    public abstract void playSound(@Nullable EntityHuman entityhuman, double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1);

    public abstract void playSound(@Nullable EntityHuman entityhuman, Entity entity, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1);

    public void a(double d0, double d1, double d2, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1, boolean flag) {}

    @Override
    public void addParticle(ParticleParam particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public void b(ParticleParam particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public void b(ParticleParam particleparam, boolean flag, double d0, double d1, double d2, double d3, double d4, double d5) {}

    public float a(float f) {
        float f1 = this.f(f);

        return f1 * 6.2831855F;
    }

    public boolean a(TileEntity tileentity) {
        if (this.tickingTileEntities) {
            World.LOGGER.error("Adding block entity while ticking: {} @ {}", new org.apache.logging.log4j.util.Supplier[]{() -> {
                        return IRegistry.BLOCK_ENTITY_TYPE.getKey(tileentity.getTileType());
                    }, tileentity::getPosition});
        }

        boolean flag = true; // Paper - remove unused list

        if (flag && tileentity instanceof ITickable && !this.tileEntityListTick.contains(tileentity)) { // Paper
            this.tileEntityListTick.add(tileentity);
        }

        if (this.isClientSide) {
            BlockPosition blockposition = tileentity.getPosition();
            IBlockData iblockdata = this.getType(blockposition);

            this.notify(blockposition, iblockdata, iblockdata, 2);
        }

        return flag;
    }

    public void a(Collection<TileEntity> collection) {
        if (this.tickingTileEntities) {
            this.tileEntityListPending.addAll(collection);
        } else {
            Iterator iterator = collection.iterator();

            while (iterator.hasNext()) {
                TileEntity tileentity = (TileEntity) iterator.next();

                this.a(tileentity);
            }
        }

    }

    public void tickBlockEntities() {
        GameProfilerFiller gameprofilerfiller = this.getMethodProfiler();

        gameprofilerfiller.enter("blockEntities");
        timings.tileEntityTick.startTiming(); // Spigot
        if (!this.tileEntityListUnload.isEmpty()) {
            // Paper start - Use alternate implementation with faster contains
            java.util.Set<TileEntity> toRemove = java.util.Collections.newSetFromMap(new java.util.IdentityHashMap<>());
            toRemove.addAll(tileEntityListUnload);
            this.tileEntityListTick.removeAll(toRemove);
            // Paper end
            //this.tileEntityList.removeAll(this.tileEntityListUnload); // Paper - remove unused list
            this.tileEntityListUnload.clear();
        }

        this.tickingTileEntities = true;
        // Spigot start
        // Iterator iterator = this.tileEntityListTick.iterator();
        int tilesThisCycle = 0;
        for (tileTickPosition = 0; tileTickPosition < tileEntityListTick.size(); tileTickPosition++) { // Paper - Disable tick limiters
            tileTickPosition = (tileTickPosition < tileEntityListTick.size()) ? tileTickPosition : 0;
            TileEntity tileentity = (TileEntity) this.tileEntityListTick.get(tileTickPosition);
            // Spigot start
            if (tileentity == null) {
                getServer().getLogger().severe("Spigot has detected a null entity and has removed it, preventing a crash");
                tilesThisCycle--;
                this.tileEntityListTick.remove(tileTickPosition--);
                continue;
            }
            // Spigot end

            if (!tileentity.isRemoved() && tileentity.hasWorld()) {
                BlockPosition blockposition = tileentity.getPosition();

                if (this.getChunkProvider().a(blockposition) && this.getWorldBorder().a(blockposition)) {
                    try {
                        gameprofilerfiller.a(() -> {
                            return String.valueOf(TileEntityTypes.a(tileentity.getTileType()));
                        });
                        tileentity.tickTimer.startTiming(); // Spigot
                        if (tileentity.getTileType().isValidBlock(this.getType(blockposition).getBlock())) {
                            ((ITickable) tileentity).tick();
                        } else {
                            tileentity.w();
                        }

                        gameprofilerfiller.exit();
                    } catch (Throwable throwable) {
                        // Paper start - Prevent tile entity and entity crashes
                        String msg = "TileEntity threw exception at " + tileentity.world.getWorld().getName() + ":" + tileentity.position.getX() + "," + tileentity.position.getY() + "," + tileentity.position.getZ();
                        System.err.println(msg);
                        throwable.printStackTrace();
                        getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable)));
                        // Paper end
                        tilesThisCycle--;
                        this.tileEntityListTick.remove(tileTickPosition--);
                        continue;
                        // Paper end
                    }
                    // Spigot start
                    finally {
                        tileentity.tickTimer.stopTiming();
                    }
                    // Spigot end
                }
            }

            if (tileentity.isRemoved()) {
                // Spigot start
                tilesThisCycle--;
                this.tileEntityListTick.remove(tileTickPosition--);
                // Spigot end
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                if (this.isLoaded(tileentity.getPosition())) {
                    this.getChunkAtWorldCoords(tileentity.getPosition()).removeTileEntity(tileentity.getPosition());
                }
            }
        }

        timings.tileEntityTick.stopTiming(); // Spigot
        timings.tileEntityPending.startTiming(); // Spigot
        this.tickingTileEntities = false;
        gameprofilerfiller.exitEnter("pendingBlockEntities");
        if (!this.tileEntityListPending.isEmpty()) {
            for (int i = 0; i < this.tileEntityListPending.size(); ++i) {
                TileEntity tileentity1 = (TileEntity) this.tileEntityListPending.get(i);

                if (!tileentity1.isRemoved()) {
                    /* CraftBukkit start - Order matters, moved down
                    if (!this.tileEntityList.contains(tileentity1)) {
                        this.a(tileentity1);
                    }
                    // CraftBukkit end */

                    if (this.isLoaded(tileentity1.getPosition())) {
                        Chunk chunk = this.getChunkAtWorldCoords(tileentity1.getPosition());
                        IBlockData iblockdata = chunk.getType(tileentity1.getPosition());

                        chunk.setTileEntity(tileentity1.getPosition(), tileentity1);
                        this.notify(tileentity1.getPosition(), iblockdata, iblockdata, 3);
                        // CraftBukkit start
                        // From above, don't screw this up - SPIGOT-1746
                        if (true) { // Paper - remove unused list
                            this.a(tileentity1);
                        }
                        // CraftBukkit end
                    }
                }
            }

            this.tileEntityListPending.clear();
        }

        timings.tileEntityPending.stopTiming(); // Spigot
        co.aikar.timings.TimingHistory.tileEntityTicks += this.tileEntityListTick.size(); // Paper
        gameprofilerfiller.exit();
        spigotConfig.currentPrimedTnt = 0; // Spigot
    }

    public void a(Consumer<Entity> consumer, Entity entity) {
        try {
            consumer.accept(entity);
        } catch (Throwable throwable) {
            // Paper start - Prevent tile entity and entity crashes
            String msg = "Entity threw exception at " + entity.world.getWorld().getName() + ":" + entity.locX() + "," + entity.locY() + "," + entity.locZ();
            System.err.println(msg);
            throwable.printStackTrace();
            getServer().getPluginManager().callEvent(new ServerExceptionEvent(new ServerInternalException(msg, throwable)));
            entity.dead = true;
            return;
            // Paper end
        }
    }
    // Paper start - Prevent armor stands from doing entity lookups
    @Override
    public boolean getCubes(@Nullable Entity entity, AxisAlignedBB axisAlignedBB) {
        if (entity instanceof EntityArmorStand && !entity.world.paperConfig.armorStandEntityLookups) return false;
        return GeneratorAccess.super.getCubes(entity, axisAlignedBB);
    }
    // Paper end

    public Explosion explode(@Nullable Entity entity, double d0, double d1, double d2, float f, Explosion.Effect explosion_effect) {
        return this.createExplosion(entity, (DamageSource) null, (ExplosionDamageCalculator) null, d0, d1, d2, f, false, explosion_effect);
    }

    public Explosion createExplosion(@Nullable Entity entity, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
        return this.createExplosion(entity, (DamageSource) null, (ExplosionDamageCalculator) null, d0, d1, d2, f, flag, explosion_effect);
    }

    public Explosion createExplosion(@Nullable Entity entity, @Nullable DamageSource damagesource, @Nullable ExplosionDamageCalculator explosiondamagecalculator, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
        Explosion explosion = new Explosion(this, entity, damagesource, explosiondamagecalculator, d0, d1, d2, f, flag, explosion_effect);

        explosion.a();
        explosion.a(true);
        return explosion;
    }

    @Nullable
    @Override
    // CraftBukkit start
    public TileEntity getTileEntity(BlockPosition blockposition) {
        return getTileEntity(blockposition, true);
    }

    @Nullable
    protected TileEntity getTileEntity(BlockPosition blockposition, boolean validate) {
        // CraftBukkit end
        if (isOutsideWorld(blockposition)) {
            return null;
        } else if (!this.isClientSide && Thread.currentThread() != this.serverThread) {
            return null;
        } else {
            // CraftBukkit start
            TileEntity tileentity = null; // Paper
            if (!capturedTileEntities.isEmpty() && (tileentity = capturedTileEntities.get(blockposition)) != null) { // Paper
                return tileentity; // Paper
            }
            // CraftBukkit end

            //TileEntity tileentity = null; // Paper - move up

            if (this.tickingTileEntities) {
                tileentity = this.E(blockposition);
            }

            if (tileentity == null) {
                tileentity = this.getChunkAtWorldCoords(blockposition).a(blockposition, Chunk.EnumTileEntityState.IMMEDIATE);
            }

            if (tileentity == null) {
                tileentity = this.E(blockposition);
            }

            return tileentity;
        }
    }

    @Nullable
    private TileEntity E(BlockPosition blockposition) {
        for (int i = 0; i < this.tileEntityListPending.size(); ++i) {
            TileEntity tileentity = (TileEntity) this.tileEntityListPending.get(i);

            if (!tileentity.isRemoved() && tileentity.getPosition().equals(blockposition)) {
                return tileentity;
            }
        }

        return null;
    }

    public void setTileEntity(BlockPosition blockposition, @Nullable TileEntity tileentity) {
        if (!isOutsideWorld(blockposition)) {
            if (tileentity != null && !tileentity.isRemoved()) {
                // CraftBukkit start
                if (captureBlockStates) {
                    tileentity.setLocation(this, blockposition);
                    capturedTileEntities.put(blockposition.immutableCopy(), tileentity);
                    return;
                }
                // CraftBukkit end
                if (this.tickingTileEntities) {
                    tileentity.setLocation(this, blockposition);
                    Iterator iterator = this.tileEntityListPending.iterator();

                    while (iterator.hasNext()) {
                        TileEntity tileentity1 = (TileEntity) iterator.next();

                        if (tileentity1.getPosition().equals(blockposition)) {
                            tileentity1.al_();
                            iterator.remove();
                        }
                    }

                    this.tileEntityListPending.add(tileentity);
                } else {
                    this.getChunkAtWorldCoords(blockposition).setTileEntity(blockposition, tileentity);
                    this.a(tileentity);
                }
            }

        }
    }

    public void removeTileEntity(BlockPosition blockposition) {
        TileEntity tileentity = this.getTileEntity(blockposition, false); // CraftBukkit

        if (tileentity != null && this.tickingTileEntities) {
            tileentity.al_();
            this.tileEntityListPending.remove(tileentity);
        } else {
            if (tileentity != null) {
                this.tileEntityListPending.remove(tileentity);
                //this.tileEntityList.remove(tileentity); // Paper - remove unused list
                this.tileEntityListTick.remove(tileentity);
            }

            this.getChunkAtWorldCoords(blockposition).removeTileEntity(blockposition);
        }

    }

    public boolean p(BlockPosition blockposition) {
        return isOutsideWorld(blockposition) ? false : isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4); // Paper
    }

    public boolean a(BlockPosition blockposition, Entity entity, EnumDirection enumdirection) {
        if (isOutsideWorld(blockposition)) {
            return false;
        } else {
            IChunkAccess ichunkaccess = this.getChunkIfLoadedImmediately(blockposition.getX() >> 4, blockposition.getZ() >> 4); // Paper

            return ichunkaccess == null ? false : ichunkaccess.getType(blockposition).a((IBlockAccess) this, blockposition, entity, enumdirection);
        }
    }

    public boolean a(BlockPosition blockposition, Entity entity) {
        return this.a(blockposition, entity, EnumDirection.UP);
    }

    public void P() {
        double d0 = 1.0D - (double) (this.d(1.0F) * 5.0F) / 16.0D;
        double d1 = 1.0D - (double) (this.b(1.0F) * 5.0F) / 16.0D;
        double d2 = 0.5D + 2.0D * MathHelper.a((double) MathHelper.cos(this.f(1.0F) * 6.2831855F), -0.25D, 0.25D);

        this.d = (int) ((1.0D - d2 * d0 * d1) * 11.0D);
    }

    public void setSpawnFlags(boolean flag, boolean flag1) {
        this.getChunkProvider().a(flag, flag1);
    }

    protected void Q() {
        if (this.worldData.hasStorm()) {
            this.rainLevel = 1.0F;
            if (this.worldData.isThundering()) {
                this.thunderLevel = 1.0F;
            }
        }

    }

    public void close() throws IOException {
        this.getChunkProvider().close();
    }

    @Nullable
    @Override
    public IBlockAccess c(int i, int j) {
        return this.getChunkAt(i, j, ChunkStatus.FULL, false);
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate) {
        this.getMethodProfiler().c("getEntities");
        List<Entity> list = Lists.newArrayList();
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.floor((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.floor((axisalignedbb.maxZ + 2.0D) / 16.0D);
        IChunkProvider ichunkprovider = this.getChunkProvider();

        for (int i1 = i; i1 <= j; ++i1) {
            for (int j1 = k; j1 <= l; ++j1) {
                Chunk chunk = (Chunk)this.getChunkIfLoadedImmediately(i1, j1); // Paper

                if (chunk != null) {
                    chunk.a(entity, axisalignedbb, list, predicate);
                }
            }
        }

        return list;
    }

    public <T extends Entity> List<T> a(@Nullable EntityTypes<T> entitytypes, AxisAlignedBB axisalignedbb, Predicate<? super T> predicate) {
        this.getMethodProfiler().c("getEntities");
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.f((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.f((axisalignedbb.maxZ + 2.0D) / 16.0D);
        List<T> list = Lists.newArrayList();

        for (int i1 = i; i1 < j; ++i1) {
            for (int j1 = k; j1 < l; ++j1) {
                Chunk chunk = (Chunk)this.getChunkIfLoadedImmediately(i1, j1); // Paper

                if (chunk != null) {
                    chunk.a(entitytypes, axisalignedbb, list, predicate);
                }
            }
        }

        return list;
    }

    public <T extends Entity> List<T> getEntities(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) { return a(oclass, axisalignedbb, predicate); } // Paper - OBFHELPER
    @Override public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        this.getMethodProfiler().c("getEntities");
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.f((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.f((axisalignedbb.maxZ + 2.0D) / 16.0D);
        List<T> list = Lists.newArrayList();
        IChunkProvider ichunkprovider = this.getChunkProvider();

        for (int i1 = i; i1 < j; ++i1) {
            for (int j1 = k; j1 < l; ++j1) {
                Chunk chunk = (Chunk)this.getChunkIfLoadedImmediately(i1, j1); // Paper

                if (chunk != null) {
                    chunk.a(oclass, axisalignedbb, list, predicate);
                }
            }
        }

        return list;
    }

    @Override
    public <T extends Entity> List<T> b(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        this.getMethodProfiler().c("getLoadedEntities");
        int i = MathHelper.floor((axisalignedbb.minX - 2.0D) / 16.0D);
        int j = MathHelper.f((axisalignedbb.maxX + 2.0D) / 16.0D);
        int k = MathHelper.floor((axisalignedbb.minZ - 2.0D) / 16.0D);
        int l = MathHelper.f((axisalignedbb.maxZ + 2.0D) / 16.0D);
        List<T> list = Lists.newArrayList();
        IChunkProvider ichunkprovider = this.getChunkProvider();

        for (int i1 = i; i1 < j; ++i1) {
            for (int j1 = k; j1 < l; ++j1) {
                Chunk chunk = (Chunk)this.getChunkIfLoadedImmediately(i1, j1); // Paper

                if (chunk != null) {
                    chunk.a(oclass, axisalignedbb, list, predicate);
                }
            }
        }

        return list;
    }

    @Nullable
    public abstract Entity getEntity(int i);

    public void b(BlockPosition blockposition, TileEntity tileentity) {
        if (this.isLoaded(blockposition)) {
            this.getChunkAtWorldCoords(blockposition).markDirty();
        }

    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    public int getBlockPower(BlockPosition blockposition) {
        byte b0 = 0;
        int i = Math.max(b0, this.c(blockposition.down(), EnumDirection.DOWN));

        if (i >= 15) {
            return i;
        } else {
            i = Math.max(i, this.c(blockposition.up(), EnumDirection.UP));
            if (i >= 15) {
                return i;
            } else {
                i = Math.max(i, this.c(blockposition.north(), EnumDirection.NORTH));
                if (i >= 15) {
                    return i;
                } else {
                    i = Math.max(i, this.c(blockposition.south(), EnumDirection.SOUTH));
                    if (i >= 15) {
                        return i;
                    } else {
                        i = Math.max(i, this.c(blockposition.west(), EnumDirection.WEST));
                        if (i >= 15) {
                            return i;
                        } else {
                            i = Math.max(i, this.c(blockposition.east(), EnumDirection.EAST));
                            return i >= 15 ? i : i;
                        }
                    }
                }
            }
        }
    }

    public boolean isBlockFacePowered(BlockPosition blockposition, EnumDirection enumdirection) {
        return this.getBlockFacePower(blockposition, enumdirection) > 0;
    }

    public int getBlockFacePower(BlockPosition blockposition, EnumDirection enumdirection) {
        IBlockData iblockdata = this.getType(blockposition);
        int i = iblockdata.b((IBlockAccess) this, blockposition, enumdirection);

        return iblockdata.isOccluding(this, blockposition) ? Math.max(i, this.getBlockPower(blockposition)) : i;
    }

    public boolean isBlockIndirectlyPowered(BlockPosition blockposition) {
        return this.getBlockFacePower(blockposition.down(), EnumDirection.DOWN) > 0 ? true : (this.getBlockFacePower(blockposition.up(), EnumDirection.UP) > 0 ? true : (this.getBlockFacePower(blockposition.north(), EnumDirection.NORTH) > 0 ? true : (this.getBlockFacePower(blockposition.south(), EnumDirection.SOUTH) > 0 ? true : (this.getBlockFacePower(blockposition.west(), EnumDirection.WEST) > 0 ? true : this.getBlockFacePower(blockposition.east(), EnumDirection.EAST) > 0))));
    }

    public int s(BlockPosition blockposition) {
        int i = 0;
        EnumDirection[] aenumdirection = World.a;
        int j = aenumdirection.length;

        for (int k = 0; k < j; ++k) {
            EnumDirection enumdirection = aenumdirection[k];
            int l = this.getBlockFacePower(blockposition.shift(enumdirection), enumdirection);

            if (l >= 15) {
                return 15;
            }

            if (l > i) {
                i = l;
            }
        }

        return i;
    }

    public long getTime() {
        return this.worldData.getTime();
    }

    public long getDayTime() {
        return this.worldData.getDayTime();
    }

    public boolean a(EntityHuman entityhuman, BlockPosition blockposition) {
        return true;
    }

    public void broadcastEntityEffect(Entity entity, byte b0) {}

    public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
        this.getType(blockposition).a(this, blockposition, i, j);
    }

    @Override
    public WorldData getWorldData() {
        return this.worldData;
    }

    public GameRules getGameRules() {
        return this.worldData.q();
    }

    public float b(float f) {
        return MathHelper.g(f, this.lastThunderLevel, this.thunderLevel) * this.d(f);
    }

    public float d(float f) {
        return MathHelper.g(f, this.lastRainLevel, this.rainLevel);
    }

    public boolean V() {
        return this.getDimensionManager().hasSkyLight() && !this.getDimensionManager().hasCeiling() ? (double) this.b(1.0F) > 0.9D : false;
    }

    public boolean isRaining() {
        return (double) this.d(1.0F) > 0.2D;
    }

    public boolean isRainingAt(BlockPosition blockposition) {
        if (!this.isRaining()) {
            return false;
        } else if (!this.e(blockposition)) {
            return false;
        } else if (this.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, blockposition).getY() > blockposition.getY()) {
            return false;
        } else {
            BiomeBase biomebase = this.getBiome(blockposition);

            return biomebase.c() == BiomeBase.Precipitation.RAIN && biomebase.getAdjustedTemperature(blockposition) >= 0.15F;
        }
    }

    public boolean u(BlockPosition blockposition) {
        BiomeBase biomebase = this.getBiome(blockposition);

        return biomebase.d();
    }

    @Nullable
    public abstract WorldMap a(String s);

    public abstract void a(WorldMap worldmap);

    public abstract int getWorldMapCount();

    public void b(int i, BlockPosition blockposition, int j) {}

    public CrashReportSystemDetails a(CrashReport crashreport) {
        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Affected level", 1);

        crashreportsystemdetails.a("All players", () -> {
            return this.getPlayers().size() + " total; " + this.getPlayers();
        });
        IChunkProvider ichunkprovider = this.getChunkProvider();

        crashreportsystemdetails.a("Chunk stats", ichunkprovider::getName);
        crashreportsystemdetails.a("Level dimension", () -> {
            return this.getDimensionKey().a().toString();
        });

        try {
            this.worldData.a(crashreportsystemdetails);
        } catch (Throwable throwable) {
            crashreportsystemdetails.a("Level Data Unobtainable", throwable);
        }

        return crashreportsystemdetails;
    }

    public abstract void a(int i, BlockPosition blockposition, int j);

    public abstract Scoreboard getScoreboard();

    public void updateAdjacentComparators(BlockPosition blockposition, Block block) {
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);

            if (this.isLoaded(blockposition1)) {
                IBlockData iblockdata = this.getType(blockposition1);

                if (iblockdata.a(Blocks.COMPARATOR)) {
                    iblockdata.doPhysics(this, blockposition1, block, blockposition, false);
                } else if (iblockdata.isOccluding(this, blockposition1)) {
                    blockposition1 = blockposition1.shift(enumdirection);
                    iblockdata = this.getType(blockposition1);
                    if (iblockdata.a(Blocks.COMPARATOR)) {
                        iblockdata.doPhysics(this, blockposition1, block, blockposition, false);
                    }
                }
            }
        }

    }

    @Override
    public DifficultyDamageScaler getDamageScaler(BlockPosition blockposition) {
        long i = 0L;
        float f = 0.0F;

        if (this.isLoaded(blockposition)) {
            f = this.ae();
            i = this.getChunkAtWorldCoords(blockposition).getInhabitedTime();
        }

        return new DifficultyDamageScaler(this.getDifficulty(), this.getDayTime(), i, f);
    }

    @Override
    public int c() {
        return this.d;
    }

    public void c(int i) {}

    @Override
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    public void a(Packet<?> packet) {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }

    @Override
    public DimensionManager getDimensionManager() {
        return this.x;
    }

    public ResourceKey<World> getDimensionKey() {
        return this.dimensionKey;
    }

    @Override
    public Random getRandom() {
        return this.random;
    }

    @Override
    public boolean a(BlockPosition blockposition, Predicate<IBlockData> predicate) {
        return predicate.test(this.getType(blockposition));
    }

    public abstract CraftingManager getCraftingManager();

    public abstract ITagRegistry p();

    public BlockPosition a(int i, int j, int k, int l) {
        // Paper start - allow use of mutable pos
        BlockPosition.MutableBlockPosition ret = new BlockPosition.MutableBlockPosition();
        this.getRandomBlockPosition(i, j, k, l, ret);
        return ret.immutableCopy();
    }
    public final BlockPosition.MutableBlockPosition getRandomBlockPosition(int i, int j, int k, int l, BlockPosition.MutableBlockPosition out) {
        // Paper end
        this.n = this.n * 3 + 1013904223;
        int i1 = this.n >> 2;

        out.setValues(i + (i1 & 15), j + (i1 >> 16 & l), k + (i1 >> 8 & 15)); // Paper - change to setValues call
        return out; // Paper
    }

    public boolean isSavingDisabled() {
        return false;
    }

    public GameProfilerFiller getMethodProfiler() {
        return (GameProfilerFiller) this.methodProfiler.get();
    }

    public Supplier<GameProfilerFiller> getMethodProfilerSupplier() {
        return this.methodProfiler;
    }

    @Override
    public BiomeManager d() {
        return this.biomeManager;
    }

    public final boolean isDebugWorld() {
        return this.debugWorld;
    }
}
