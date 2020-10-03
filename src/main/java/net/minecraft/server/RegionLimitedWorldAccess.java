package net.minecraft.server;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RegionLimitedWorldAccess implements GeneratorAccessSeed {

    private static final Logger LOGGER = LogManager.getLogger();
    private final List<IChunkAccess> b;
    private final int c;
    private final int d;
    private final int e;
    private final WorldServer f;
    private final long g;
    private final WorldData h;
    private final Random i;
    private final DimensionManager j;
    private final TickList<Block> k = new TickListWorldGen<>((blockposition) -> {
        return this.z(blockposition).n();
    });
    private final TickList<FluidType> l = new TickListWorldGen<>((blockposition) -> {
        return this.z(blockposition).o();
    });
    private final BiomeManager m;
    private final ChunkCoordIntPair n;
    private final ChunkCoordIntPair o;

    public RegionLimitedWorldAccess(WorldServer worldserver, List<IChunkAccess> list) {
        int i = MathHelper.floor(Math.sqrt((double) list.size()));

        if (i * i != list.size()) {
            throw (IllegalStateException) SystemUtils.c((Throwable) (new IllegalStateException("Cache size is not a square.")));
        } else {
            ChunkCoordIntPair chunkcoordintpair = ((IChunkAccess) list.get(list.size() / 2)).getPos();

            this.b = list;
            this.c = chunkcoordintpair.x;
            this.d = chunkcoordintpair.z;
            this.e = i;
            this.f = worldserver;
            this.g = worldserver.getSeed();
            this.h = worldserver.getWorldData();
            this.i = worldserver.getRandom();
            this.j = worldserver.getDimensionManager();
            this.m = new BiomeManager(this, BiomeManager.a(this.g), worldserver.getDimensionManager().getGenLayerZoomer());
            this.n = ((IChunkAccess) list.get(0)).getPos();
            this.o = ((IChunkAccess) list.get(list.size() - 1)).getPos();
        }
    }

    public int a() {
        return this.c;
    }

    public int b() {
        return this.d;
    }

    @Override
    public IChunkAccess getChunkAt(int i, int j) {
        return this.getChunkAt(i, j, ChunkStatus.EMPTY);
    }

    @Nullable
    @Override
    public IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus, boolean flag) {
        IChunkAccess ichunkaccess;

        if (this.isChunkLoaded(i, j)) {
            int k = i - this.n.x;
            int l = j - this.n.z;

            ichunkaccess = (IChunkAccess) this.b.get(k + l * this.e);
            if (ichunkaccess.getChunkStatus().b(chunkstatus)) {
                return ichunkaccess;
            }
        } else {
            ichunkaccess = null;
        }

        if (!flag) {
            return null;
        } else {
            RegionLimitedWorldAccess.LOGGER.error("Requested chunk : {} {}", i, j);
            RegionLimitedWorldAccess.LOGGER.error("Region bounds : {} {} | {} {}", this.n.x, this.n.z, this.o.x, this.o.z);
            if (ichunkaccess != null) {
                throw (RuntimeException) SystemUtils.c((Throwable) (new RuntimeException(String.format("Chunk is not of correct status. Expecting %s, got %s | %s %s", chunkstatus, ichunkaccess.getChunkStatus(), i, j))));
            } else {
                throw (RuntimeException) SystemUtils.c((Throwable) (new RuntimeException(String.format("We are asking a region for a chunk out of bound | %s %s", i, j))));
            }
        }
    }

    @Override
    public boolean isChunkLoaded(int i, int j) {
        return i >= this.n.x && i <= this.o.x && j >= this.n.z && j <= this.o.z;
    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        return this.getChunkAt(blockposition.getX() >> 4, blockposition.getZ() >> 4).getType(blockposition);
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        return this.z(blockposition).getFluid(blockposition);
    }

    @Nullable
    @Override
    public EntityHuman a(double d0, double d1, double d2, double d3, Predicate<Entity> predicate) {
        return null;
    }

    @Override
    public int c() {
        return 0;
    }

    @Override
    public BiomeManager d() {
        return this.m;
    }

    @Override
    public BiomeBase a(int i, int j, int k) {
        return this.f.a(i, j, k);
    }

    @Override
    public LightEngine e() {
        return this.f.e();
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag, @Nullable Entity entity, int i) {
        IBlockData iblockdata = this.getType(blockposition);

        if (iblockdata.isAir()) {
            return false;
        } else {
            if (flag) {
                TileEntity tileentity = iblockdata.getBlock().isTileEntity() ? this.getTileEntity(blockposition) : null;

                Block.dropItems(iblockdata, this.f, blockposition, tileentity, entity, ItemStack.b);
            }

            return this.a(blockposition, Blocks.AIR.getBlockData(), 3, i);
        }
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        IChunkAccess ichunkaccess = this.z(blockposition);
        TileEntity tileentity = ichunkaccess.getTileEntity(blockposition);

        if (tileentity != null) {
            return tileentity;
        } else {
            NBTTagCompound nbttagcompound = ichunkaccess.i(blockposition);
            IBlockData iblockdata = ichunkaccess.getType(blockposition);

            if (nbttagcompound != null) {
                if ("DUMMY".equals(nbttagcompound.getString("id"))) {
                    Block block = iblockdata.getBlock();

                    if (!(block instanceof ITileEntity)) {
                        return null;
                    }

                    tileentity = ((ITileEntity) block).createTile(this.f);
                } else {
                    tileentity = TileEntity.create(iblockdata, nbttagcompound);
                }

                if (tileentity != null) {
                    ichunkaccess.setTileEntity(blockposition, tileentity);
                    return tileentity;
                }
            }

            if (iblockdata.getBlock() instanceof ITileEntity) {
                RegionLimitedWorldAccess.LOGGER.warn("Tried to access a block entity before it was created. {}", blockposition);
            }

            return null;
        }
    }

    @Override
    public boolean a(BlockPosition blockposition, IBlockData iblockdata, int i, int j) {
        IChunkAccess ichunkaccess = this.z(blockposition);
        IBlockData iblockdata1 = ichunkaccess.setType(blockposition, iblockdata, false);

        if (iblockdata1 != null) {
            this.f.a(blockposition, iblockdata1, iblockdata);
        }

        Block block = iblockdata.getBlock();

        if (block.isTileEntity()) {
            if (ichunkaccess.getChunkStatus().getType() == ChunkStatus.Type.LEVELCHUNK) {
                ichunkaccess.setTileEntity(blockposition, ((ITileEntity) block).createTile(this));
            } else {
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                nbttagcompound.setInt("x", blockposition.getX());
                nbttagcompound.setInt("y", blockposition.getY());
                nbttagcompound.setInt("z", blockposition.getZ());
                nbttagcompound.setString("id", "DUMMY");
                ichunkaccess.a(nbttagcompound);
            }
        } else if (iblockdata1 != null && iblockdata1.getBlock().isTileEntity()) {
            ichunkaccess.removeTileEntity(blockposition);
        }

        if (iblockdata.q(this, blockposition)) {
            this.j(blockposition);
        }

        return true;
    }

    private void j(BlockPosition blockposition) {
        this.z(blockposition).e(blockposition);
    }

    @Override
    public boolean addEntity(Entity entity) {
        int i = MathHelper.floor(entity.locX() / 16.0D);
        int j = MathHelper.floor(entity.locZ() / 16.0D);

        this.getChunkAt(i, j).a(entity);
        return true;
    }

    @Override
    public boolean a(BlockPosition blockposition, boolean flag) {
        return this.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.f.getWorldBorder();
    }

    @Override
    public boolean s_() {
        return false;
    }

    @Deprecated
    @Override
    public WorldServer getMinecraftWorld() {
        return this.f;
    }

    @Override
    public IRegistryCustom r() {
        return this.f.r();
    }

    @Override
    public WorldData getWorldData() {
        return this.h;
    }

    @Override
    public DifficultyDamageScaler getDamageScaler(BlockPosition blockposition) {
        if (!this.isChunkLoaded(blockposition.getX() >> 4, blockposition.getZ() >> 4)) {
            throw new RuntimeException("We are asking a region for a chunk out of bound");
        } else {
            return new DifficultyDamageScaler(this.f.getDifficulty(), this.f.getDayTime(), 0L, this.f.ae());
        }
    }

    @Override
    public IChunkProvider getChunkProvider() {
        return this.f.getChunkProvider();
    }

    @Override
    public long getSeed() {
        return this.g;
    }

    @Override
    public TickList<Block> getBlockTickList() {
        return this.k;
    }

    @Override
    public TickList<FluidType> getFluidTickList() {
        return this.l;
    }

    @Override
    public int getSeaLevel() {
        return this.f.getSeaLevel();
    }

    @Override
    public Random getRandom() {
        return this.i;
    }

    @Override
    public int a(HeightMap.Type heightmap_type, int i, int j) {
        return this.getChunkAt(i >> 4, j >> 4).getHighestBlock(heightmap_type, i & 15, j & 15) + 1;
    }

    @Override
    public void playSound(@Nullable EntityHuman entityhuman, BlockPosition blockposition, SoundEffect soundeffect, SoundCategory soundcategory, float f, float f1) {}

    @Override
    public void addParticle(ParticleParam particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {}

    @Override
    public void a(@Nullable EntityHuman entityhuman, int i, BlockPosition blockposition, int j) {}

    @Override
    public DimensionManager getDimensionManager() {
        return this.j;
    }

    @Override
    public boolean a(BlockPosition blockposition, Predicate<IBlockData> predicate) {
        return predicate.test(this.getType(blockposition));
    }

    @Override
    public <T extends Entity> List<T> a(Class<? extends T> oclass, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super T> predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity, AxisAlignedBB axisalignedbb, @Nullable Predicate<? super Entity> predicate) {
        return Collections.emptyList();
    }

    @Override
    public List<EntityHuman> getPlayers() {
        return Collections.emptyList();
    }

    @Override
    public Stream<? extends StructureStart<?>> a(SectionPosition sectionposition, StructureGenerator<?> structuregenerator) {
        return this.f.a(sectionposition, structuregenerator);
    }
}
