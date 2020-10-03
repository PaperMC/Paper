package net.minecraft.server;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2ByteMap;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class VillagePlace extends RegionFileSection<VillagePlaceSection> {

    private final VillagePlace.a a = new VillagePlace.a();
    private final LongSet b = new LongOpenHashSet();

    public VillagePlace(File file, DataFixer datafixer, boolean flag) {
        super(file, VillagePlaceSection::a, VillagePlaceSection::new, datafixer, DataFixTypes.POI_CHUNK, flag);
    }

    public void a(BlockPosition blockposition, VillagePlaceType villageplacetype) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).a(blockposition, villageplacetype);
    }

    public void a(BlockPosition blockposition) {
        ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).a(blockposition);
    }

    public long a(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.c(predicate, blockposition, i, villageplace_occupancy).count();
    }

    public boolean a(VillagePlaceType villageplacetype, BlockPosition blockposition) {
        Optional<VillagePlaceType> optional = ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).d(blockposition);

        return optional.isPresent() && ((VillagePlaceType) optional.get()).equals(villageplacetype);
    }

    public Stream<VillagePlaceRecord> b(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        int j = Math.floorDiv(i, 16) + 1;

        return ChunkCoordIntPair.a(new ChunkCoordIntPair(blockposition), j).flatMap((chunkcoordintpair) -> {
            return this.a(predicate, chunkcoordintpair, villageplace_occupancy);
        }).filter((villageplacerecord) -> {
            BlockPosition blockposition1 = villageplacerecord.f();

            return Math.abs(blockposition1.getX() - blockposition.getX()) <= i && Math.abs(blockposition1.getZ() - blockposition.getZ()) <= i;
        });
    }

    public Stream<VillagePlaceRecord> c(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        int j = i * i;

        return this.b(predicate, blockposition, i, villageplace_occupancy).filter((villageplacerecord) -> {
            return villageplacerecord.f().j(blockposition) <= (double) j;
        });
    }

    public Stream<VillagePlaceRecord> a(Predicate<VillagePlaceType> predicate, ChunkCoordIntPair chunkcoordintpair, VillagePlace.Occupancy villageplace_occupancy) {
        return IntStream.range(0, 16).boxed().map((integer) -> {
            return this.d(SectionPosition.a(chunkcoordintpair, integer).s());
        }).filter(Optional::isPresent).flatMap((optional) -> {
            return ((VillagePlaceSection) optional.get()).a(predicate, villageplace_occupancy);
        });
    }

    public Stream<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.c(predicate, blockposition, i, villageplace_occupancy).map(VillagePlaceRecord::f).filter(predicate1);
    }

    public Stream<BlockPosition> b(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.a(predicate, predicate1, blockposition, i, villageplace_occupancy).sorted(Comparator.comparingDouble((blockposition1) -> {
            return blockposition1.j(blockposition);
        }));
    }

    public Optional<BlockPosition> c(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.a(predicate, predicate1, blockposition, i, villageplace_occupancy).findFirst();
    }

    public Optional<BlockPosition> d(Predicate<VillagePlaceType> predicate, BlockPosition blockposition, int i, VillagePlace.Occupancy villageplace_occupancy) {
        return this.c(predicate, blockposition, i, villageplace_occupancy).map(VillagePlaceRecord::f).min(Comparator.comparingDouble((blockposition1) -> {
            return blockposition1.j(blockposition);
        }));
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, BlockPosition blockposition, int i) {
        return this.c(predicate, blockposition, i, VillagePlace.Occupancy.HAS_SPACE).filter((villageplacerecord) -> {
            return predicate1.test(villageplacerecord.f());
        }).findFirst().map((villageplacerecord) -> {
            villageplacerecord.b();
            return villageplacerecord.f();
        });
    }

    public Optional<BlockPosition> a(Predicate<VillagePlaceType> predicate, Predicate<BlockPosition> predicate1, VillagePlace.Occupancy villageplace_occupancy, BlockPosition blockposition, int i, Random random) {
        List<VillagePlaceRecord> list = (List) this.c(predicate, blockposition, i, villageplace_occupancy).collect(Collectors.toList());

        Collections.shuffle(list, random);
        return list.stream().filter((villageplacerecord) -> {
            return predicate1.test(villageplacerecord.f());
        }).findFirst().map(VillagePlaceRecord::f);
    }

    public boolean b(BlockPosition blockposition) {
        return ((VillagePlaceSection) this.e(SectionPosition.a(blockposition).s())).c(blockposition);
    }

    public boolean a(BlockPosition blockposition, Predicate<VillagePlaceType> predicate) {
        return (Boolean) this.d(SectionPosition.a(blockposition).s()).map((villageplacesection) -> {
            return villageplacesection.a(blockposition, predicate);
        }).orElse(false);
    }

    public Optional<VillagePlaceType> c(BlockPosition blockposition) {
        VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(SectionPosition.a(blockposition).s());

        return villageplacesection.d(blockposition);
    }

    public int a(SectionPosition sectionposition) {
        this.a.a();
        return this.a.c(sectionposition.s());
    }

    private boolean f(long i) {
        Optional<VillagePlaceSection> optional = this.c(i);

        return optional == null ? false : (Boolean) optional.map((villageplacesection) -> {
            return villageplacesection.a(VillagePlaceType.b, VillagePlace.Occupancy.IS_OCCUPIED).count() > 0L;
        }).orElse(false);
    }

    @Override
    public void a(BooleanSupplier booleansupplier) {
        super.a(booleansupplier);
        this.a.a();
    }

    @Override
    protected void a(long i) {
        super.a(i);
        this.a.b(i, this.a.b(i), false);
    }

    @Override
    protected void b(long i) {
        this.a.b(i, this.a.b(i), false);
    }

    public void a(ChunkCoordIntPair chunkcoordintpair, ChunkSection chunksection) {
        SectionPosition sectionposition = SectionPosition.a(chunkcoordintpair, chunksection.getYPosition() >> 4);

        SystemUtils.a(this.d(sectionposition.s()), (villageplacesection) -> {
            villageplacesection.a((biconsumer) -> {
                if (a(chunksection)) {
                    this.a(chunksection, sectionposition, biconsumer);
                }

            });
        }, () -> {
            if (a(chunksection)) {
                VillagePlaceSection villageplacesection = (VillagePlaceSection) this.e(sectionposition.s());

                this.a(chunksection, sectionposition, villageplacesection::a);
            }

        });
    }

    private static boolean a(ChunkSection chunksection) {
        Set set = VillagePlaceType.x;

        set.getClass();
        return chunksection.a(set::contains);
    }

    private void a(ChunkSection chunksection, SectionPosition sectionposition, BiConsumer<BlockPosition, VillagePlaceType> biconsumer) {
        sectionposition.t().forEach((blockposition) -> {
            IBlockData iblockdata = chunksection.getType(SectionPosition.b(blockposition.getX()), SectionPosition.b(blockposition.getY()), SectionPosition.b(blockposition.getZ()));

            VillagePlaceType.b(iblockdata).ifPresent((villageplacetype) -> {
                biconsumer.accept(blockposition, villageplacetype);
            });
        });
    }

    public void a(IWorldReader iworldreader, BlockPosition blockposition, int i) {
        SectionPosition.b(new ChunkCoordIntPair(blockposition), Math.floorDiv(i, 16)).map((sectionposition) -> {
            return Pair.of(sectionposition, this.d(sectionposition.s()));
        }).filter((pair) -> {
            return !(Boolean) ((Optional) pair.getSecond()).map(VillagePlaceSection::a).orElse(false);
        }).map((pair) -> {
            return ((SectionPosition) pair.getFirst()).r();
        }).filter((chunkcoordintpair) -> {
            return this.b.add(chunkcoordintpair.pair());
        }).forEach((chunkcoordintpair) -> {
            iworldreader.getChunkAt(chunkcoordintpair.x, chunkcoordintpair.z, ChunkStatus.EMPTY);
        });
    }

    final class a extends LightEngineGraphSection {

        private final Long2ByteMap b = new Long2ByteOpenHashMap();

        protected a() {
            super(7, 16, 256);
            this.b.defaultReturnValue((byte) 7);
        }

        @Override
        protected int b(long i) {
            return VillagePlace.this.f(i) ? 0 : 7;
        }

        @Override
        protected int c(long i) {
            return this.b.get(i);
        }

        @Override
        protected void a(long i, int j) {
            if (j > 6) {
                this.b.remove(i);
            } else {
                this.b.put(i, (byte) j);
            }

        }

        public void a() {
            super.b(Integer.MAX_VALUE);
        }
    }

    public static enum Occupancy {

        HAS_SPACE(VillagePlaceRecord::d), IS_OCCUPIED(VillagePlaceRecord::e), ANY((villageplacerecord) -> {
            return true;
        });

        private final Predicate<? super VillagePlaceRecord> d;

        private Occupancy(Predicate predicate) {
            this.d = predicate;
        }

        public Predicate<? super VillagePlaceRecord> a() {
            return this.d;
        }
    }
}
