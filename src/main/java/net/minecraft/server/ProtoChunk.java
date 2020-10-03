package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.shorts.ShortList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProtoChunk implements IChunkAccess {

    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkCoordIntPair b;
    private volatile boolean c;
    @Nullable
    private BiomeStorage d;
    @Nullable
    private volatile LightEngine e;
    private final Map<HeightMap.Type, HeightMap> f;
    private volatile ChunkStatus g;
    private final Map<BlockPosition, TileEntity> h;
    private final Map<BlockPosition, NBTTagCompound> i;
    private final ChunkSection[] j;
    private final List<NBTTagCompound> k;
    private final List<BlockPosition> l;
    private final ShortList[] m;
    private final Map<StructureGenerator<?>, StructureStart<?>> n;
    private final Map<StructureGenerator<?>, LongSet> o;
    private final ChunkConverter p;
    private final ProtoChunkTickList<Block> q;
    private final ProtoChunkTickList<FluidType> r;
    private long s;
    private final Map<WorldGenStage.Features, BitSet> t;
    private volatile boolean u;

    public ProtoChunk(ChunkCoordIntPair chunkcoordintpair, ChunkConverter chunkconverter) {
        this(chunkcoordintpair, chunkconverter, (ChunkSection[]) null, new ProtoChunkTickList<>((block) -> {
            return block == null || block.getBlockData().isAir();
        }, chunkcoordintpair), new ProtoChunkTickList<>((fluidtype) -> {
            return fluidtype == null || fluidtype == FluidTypes.EMPTY;
        }, chunkcoordintpair));
    }

    public ProtoChunk(ChunkCoordIntPair chunkcoordintpair, ChunkConverter chunkconverter, @Nullable ChunkSection[] achunksection, ProtoChunkTickList<Block> protochunkticklist, ProtoChunkTickList<FluidType> protochunkticklist1) {
        this.f = Maps.newEnumMap(HeightMap.Type.class);
        this.g = ChunkStatus.EMPTY;
        this.h = Maps.newHashMap();
        this.i = Maps.newHashMap();
        this.j = new ChunkSection[16];
        this.k = Lists.newArrayList();
        this.l = Lists.newArrayList();
        this.m = new ShortList[16];
        this.n = Maps.newHashMap();
        this.o = Maps.newHashMap();
        this.t = new Object2ObjectArrayMap();
        this.b = chunkcoordintpair;
        this.p = chunkconverter;
        this.q = protochunkticklist;
        this.r = protochunkticklist1;
        if (achunksection != null) {
            if (this.j.length == achunksection.length) {
                System.arraycopy(achunksection, 0, this.j, 0, this.j.length);
            } else {
                ProtoChunk.LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", achunksection.length, this.j.length);
            }
        }

    }

    @Override
    public IBlockData getType(BlockPosition blockposition) {
        int i = blockposition.getY();

        if (World.b(i)) {
            return Blocks.VOID_AIR.getBlockData();
        } else {
            ChunkSection chunksection = this.getSections()[i >> 4];

            return ChunkSection.a(chunksection) ? Blocks.AIR.getBlockData() : chunksection.getType(blockposition.getX() & 15, i & 15, blockposition.getZ() & 15);
        }
    }

    @Override
    public Fluid getFluid(BlockPosition blockposition) {
        int i = blockposition.getY();

        if (World.b(i)) {
            return FluidTypes.EMPTY.h();
        } else {
            ChunkSection chunksection = this.getSections()[i >> 4];

            return ChunkSection.a(chunksection) ? FluidTypes.EMPTY.h() : chunksection.b(blockposition.getX() & 15, i & 15, blockposition.getZ() & 15);
        }
    }

    @Override
    public Stream<BlockPosition> m() {
        return this.l.stream();
    }

    public ShortList[] w() {
        ShortList[] ashortlist = new ShortList[16];
        Iterator iterator = this.l.iterator();

        while (iterator.hasNext()) {
            BlockPosition blockposition = (BlockPosition) iterator.next();

            IChunkAccess.a(ashortlist, blockposition.getY() >> 4).add(l(blockposition));
        }

        return ashortlist;
    }

    public void b(short short0, int i) {
        this.k(a(short0, i, this.b));
    }

    public void k(BlockPosition blockposition) {
        this.l.add(blockposition.immutableCopy());
    }

    @Nullable
    @Override
    public IBlockData setType(BlockPosition blockposition, IBlockData iblockdata, boolean flag) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();

        if (j >= 0 && j < 256) {
            if (this.j[j >> 4] == Chunk.a && iblockdata.a(Blocks.AIR)) {
                return iblockdata;
            } else {
                if (iblockdata.f() > 0) {
                    this.l.add(new BlockPosition((i & 15) + this.getPos().d(), j, (k & 15) + this.getPos().e()));
                }

                ChunkSection chunksection = this.a(j >> 4);
                IBlockData iblockdata1 = chunksection.setType(i & 15, j & 15, k & 15, iblockdata);

                if (this.g.b(ChunkStatus.FEATURES) && iblockdata != iblockdata1 && (iblockdata.b((IBlockAccess) this, blockposition) != iblockdata1.b((IBlockAccess) this, blockposition) || iblockdata.f() != iblockdata1.f() || iblockdata.e() || iblockdata1.e())) {
                    LightEngine lightengine = this.e();

                    lightengine.a(blockposition);
                }

                EnumSet<HeightMap.Type> enumset = this.getChunkStatus().h();
                EnumSet<HeightMap.Type> enumset1 = null;
                Iterator iterator = enumset.iterator();

                HeightMap.Type heightmap_type;

                while (iterator.hasNext()) {
                    heightmap_type = (HeightMap.Type) iterator.next();
                    HeightMap heightmap = (HeightMap) this.f.get(heightmap_type);

                    if (heightmap == null) {
                        if (enumset1 == null) {
                            enumset1 = EnumSet.noneOf(HeightMap.Type.class);
                        }

                        enumset1.add(heightmap_type);
                    }
                }

                if (enumset1 != null) {
                    HeightMap.a(this, enumset1);
                }

                iterator = enumset.iterator();

                while (iterator.hasNext()) {
                    heightmap_type = (HeightMap.Type) iterator.next();
                    ((HeightMap) this.f.get(heightmap_type)).a(i & 15, j, k & 15, iblockdata);
                }

                return iblockdata1;
            }
        } else {
            return Blocks.VOID_AIR.getBlockData();
        }
    }

    public ChunkSection a(int i) {
        if (this.j[i] == Chunk.a) {
            this.j[i] = new ChunkSection(i << 4);
        }

        return this.j[i];
    }

    @Override
    public void setTileEntity(BlockPosition blockposition, TileEntity tileentity) {
        tileentity.setPosition(blockposition);
        this.h.put(blockposition, tileentity);
    }

    @Override
    public Set<BlockPosition> c() {
        Set<BlockPosition> set = Sets.newHashSet(this.i.keySet());

        set.addAll(this.h.keySet());
        return set;
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPosition blockposition) {
        return (TileEntity) this.h.get(blockposition);
    }

    public Map<BlockPosition, TileEntity> x() {
        return this.h;
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.k.add(nbttagcompound);
    }

    @Override
    public void a(Entity entity) {
        if (!entity.isPassenger()) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            entity.d(nbttagcompound);
            this.b(nbttagcompound);
        }
    }

    public List<NBTTagCompound> y() {
        return this.k;
    }

    public void a(BiomeStorage biomestorage) {
        this.d = biomestorage;
    }

    @Nullable
    @Override
    public BiomeStorage getBiomeIndex() {
        return this.d;
    }

    @Override
    public void setNeedsSaving(boolean flag) {
        this.c = flag;
    }

    @Override
    public boolean isNeedsSaving() {
        return this.c;
    }

    @Override
    public ChunkStatus getChunkStatus() {
        return this.g;
    }

    public void a(ChunkStatus chunkstatus) {
        this.g = chunkstatus;
        this.setNeedsSaving(true);
    }

    @Override
    public ChunkSection[] getSections() {
        return this.j;
    }

    @Nullable
    public LightEngine e() {
        return this.e;
    }

    @Override
    public Collection<Entry<HeightMap.Type, HeightMap>> f() {
        return Collections.unmodifiableSet(this.f.entrySet());
    }

    @Override
    public void a(HeightMap.Type heightmap_type, long[] along) {
        this.a(heightmap_type).a(along);
    }

    @Override
    public HeightMap a(HeightMap.Type heightmap_type) {
        return (HeightMap) this.f.computeIfAbsent(heightmap_type, (heightmap_type1) -> {
            return new HeightMap(this, heightmap_type1);
        });
    }

    @Override
    public int getHighestBlock(HeightMap.Type heightmap_type, int i, int j) {
        HeightMap heightmap = (HeightMap) this.f.get(heightmap_type);

        if (heightmap == null) {
            HeightMap.a(this, EnumSet.of(heightmap_type));
            heightmap = (HeightMap) this.f.get(heightmap_type);
        }

        return heightmap.a(i & 15, j & 15) - 1;
    }

    @Override
    public ChunkCoordIntPair getPos() {
        return this.b;
    }

    @Override
    public void setLastSaved(long i) {}

    @Nullable
    @Override
    public StructureStart<?> a(StructureGenerator<?> structuregenerator) {
        return (StructureStart) this.n.get(structuregenerator);
    }

    @Override
    public void a(StructureGenerator<?> structuregenerator, StructureStart<?> structurestart) {
        this.n.put(structuregenerator, structurestart);
        this.c = true;
    }

    @Override
    public Map<StructureGenerator<?>, StructureStart<?>> h() {
        return Collections.unmodifiableMap(this.n);
    }

    @Override
    public void a(Map<StructureGenerator<?>, StructureStart<?>> map) {
        this.n.clear();
        this.n.putAll(map);
        this.c = true;
    }

    @Override
    public LongSet b(StructureGenerator<?> structuregenerator) {
        return (LongSet) this.o.computeIfAbsent(structuregenerator, (structuregenerator1) -> {
            return new LongOpenHashSet();
        });
    }

    @Override
    public void a(StructureGenerator<?> structuregenerator, long i) {
        ((LongSet) this.o.computeIfAbsent(structuregenerator, (structuregenerator1) -> {
            return new LongOpenHashSet();
        })).add(i);
        this.c = true;
    }

    @Override
    public Map<StructureGenerator<?>, LongSet> v() {
        return Collections.unmodifiableMap(this.o);
    }

    @Override
    public void b(Map<StructureGenerator<?>, LongSet> map) {
        this.o.clear();
        this.o.putAll(map);
        this.c = true;
    }

    public static short l(BlockPosition blockposition) {
        int i = blockposition.getX();
        int j = blockposition.getY();
        int k = blockposition.getZ();
        int l = i & 15;
        int i1 = j & 15;
        int j1 = k & 15;

        return (short) (l | i1 << 4 | j1 << 8);
    }

    public static BlockPosition a(short short0, int i, ChunkCoordIntPair chunkcoordintpair) {
        int j = (short0 & 15) + (chunkcoordintpair.x << 4);
        int k = (short0 >>> 4 & 15) + (i << 4);
        int l = (short0 >>> 8 & 15) + (chunkcoordintpair.z << 4);

        return new BlockPosition(j, k, l);
    }

    @Override
    public void e(BlockPosition blockposition) {
        if (!World.isOutsideWorld(blockposition)) {
            IChunkAccess.a(this.m, blockposition.getY() >> 4).add(l(blockposition));
        }

    }

    @Override
    public ShortList[] l() {
        return this.m;
    }

    @Override
    public void a(short short0, int i) {
        IChunkAccess.a(this.m, i).add(short0);
    }

    @Override
    public ProtoChunkTickList<Block> n() {
        return this.q;
    }

    @Override
    public ProtoChunkTickList<FluidType> o() {
        return this.r;
    }

    @Override
    public ChunkConverter p() {
        return this.p;
    }

    @Override
    public void setInhabitedTime(long i) {
        this.s = i;
    }

    @Override
    public long getInhabitedTime() {
        return this.s;
    }

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        this.i.put(new BlockPosition(nbttagcompound.getInt("x"), nbttagcompound.getInt("y"), nbttagcompound.getInt("z")), nbttagcompound);
    }

    public Map<BlockPosition, NBTTagCompound> z() {
        return Collections.unmodifiableMap(this.i);
    }

    @Override
    public NBTTagCompound i(BlockPosition blockposition) {
        return (NBTTagCompound) this.i.get(blockposition);
    }

    @Nullable
    @Override
    public NBTTagCompound j(BlockPosition blockposition) {
        TileEntity tileentity = this.getTileEntity(blockposition);

        return tileentity != null ? tileentity.save(new NBTTagCompound()) : (NBTTagCompound) this.i.get(blockposition);
    }

    @Override
    public void removeTileEntity(BlockPosition blockposition) {
        this.h.remove(blockposition);
        this.i.remove(blockposition);
    }

    @Nullable
    public BitSet a(WorldGenStage.Features worldgenstage_features) {
        return (BitSet) this.t.get(worldgenstage_features);
    }

    public BitSet b(WorldGenStage.Features worldgenstage_features) {
        return (BitSet) this.t.computeIfAbsent(worldgenstage_features, (worldgenstage_features1) -> {
            return new BitSet(65536);
        });
    }

    public void a(WorldGenStage.Features worldgenstage_features, BitSet bitset) {
        this.t.put(worldgenstage_features, bitset);
    }

    public void a(LightEngine lightengine) {
        this.e = lightengine;
    }

    @Override
    public boolean r() {
        return this.u;
    }

    @Override
    public void b(boolean flag) {
        this.u = flag;
        this.setNeedsSaving(true);
    }
}
