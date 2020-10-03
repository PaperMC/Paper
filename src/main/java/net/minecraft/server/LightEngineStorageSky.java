package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Arrays;

public class LightEngineStorageSky extends LightEngineStorage<LightEngineStorageSky.a> {

    private static final EnumDirection[] k = new EnumDirection[]{EnumDirection.NORTH, EnumDirection.SOUTH, EnumDirection.WEST, EnumDirection.EAST};
    private final LongSet l = new LongOpenHashSet();
    private final LongSet m = new LongOpenHashSet();
    private final LongSet n = new LongOpenHashSet();
    private final LongSet o = new LongOpenHashSet();
    private volatile boolean p;

    protected LightEngineStorageSky(ILightAccess ilightaccess) {
        super(EnumSkyBlock.SKY, ilightaccess, new LightEngineStorageSky.a(new Long2ObjectOpenHashMap(), new Long2IntOpenHashMap(), Integer.MAX_VALUE));
    }

    @Override
    protected int d(long i) {
        long j = SectionPosition.e(i);
        int k = SectionPosition.c(j);
        LightEngineStorageSky.a lightenginestoragesky_a = (LightEngineStorageSky.a) this.e;
        int l = lightenginestoragesky_a.c.get(SectionPosition.f(j));

        if (l != lightenginestoragesky_a.b && k < l) {
            NibbleArray nibblearray = this.a((LightEngineStorageArray) lightenginestoragesky_a, j);

            if (nibblearray == null) {
                for (i = BlockPosition.f(i); nibblearray == null; nibblearray = this.a((LightEngineStorageArray) lightenginestoragesky_a, j)) {
                    j = SectionPosition.a(j, EnumDirection.UP);
                    ++k;
                    if (k >= l) {
                        return 15;
                    }

                    i = BlockPosition.a(i, 0, 16, 0);
                }
            }

            return nibblearray.a(SectionPosition.b(BlockPosition.b(i)), SectionPosition.b(BlockPosition.c(i)), SectionPosition.b(BlockPosition.d(i)));
        } else {
            return 15;
        }
    }

    @Override
    protected void k(long i) {
        int j = SectionPosition.c(i);

        if (((LightEngineStorageSky.a) this.f).b > j) {
            ((LightEngineStorageSky.a) this.f).b = j;
            ((LightEngineStorageSky.a) this.f).c.defaultReturnValue(((LightEngineStorageSky.a) this.f).b);
        }

        long k = SectionPosition.f(i);
        int l = ((LightEngineStorageSky.a) this.f).c.get(k);

        if (l < j + 1) {
            ((LightEngineStorageSky.a) this.f).c.put(k, j + 1);
            if (this.o.contains(k)) {
                this.q(i);
                if (l > ((LightEngineStorageSky.a) this.f).b) {
                    long i1 = SectionPosition.b(SectionPosition.b(i), l - 1, SectionPosition.d(i));

                    this.p(i1);
                }

                this.f();
            }
        }

    }

    private void p(long i) {
        this.n.add(i);
        this.m.remove(i);
    }

    private void q(long i) {
        this.m.add(i);
        this.n.remove(i);
    }

    private void f() {
        this.p = !this.m.isEmpty() || !this.n.isEmpty();
    }

    @Override
    protected void l(long i) {
        long j = SectionPosition.f(i);
        boolean flag = this.o.contains(j);

        if (flag) {
            this.p(i);
        }

        int k = SectionPosition.c(i);

        if (((LightEngineStorageSky.a) this.f).c.get(j) == k + 1) {
            long l;

            for (l = i; !this.g(l) && this.a(k); l = SectionPosition.a(l, EnumDirection.DOWN)) {
                --k;
            }

            if (this.g(l)) {
                ((LightEngineStorageSky.a) this.f).c.put(j, k + 1);
                if (flag) {
                    this.q(l);
                }
            } else {
                ((LightEngineStorageSky.a) this.f).c.remove(j);
            }
        }

        if (flag) {
            this.f();
        }

    }

    @Override
    protected void b(long i, boolean flag) {
        this.d();
        if (flag && this.o.add(i)) {
            int j = ((LightEngineStorageSky.a) this.f).c.get(i);

            if (j != ((LightEngineStorageSky.a) this.f).b) {
                long k = SectionPosition.b(SectionPosition.b(i), j - 1, SectionPosition.d(i));

                this.q(k);
                this.f();
            }
        } else if (!flag) {
            this.o.remove(i);
        }

    }

    @Override
    protected boolean a() {
        return super.a() || this.p;
    }

    @Override
    protected NibbleArray j(long i) {
        NibbleArray nibblearray = (NibbleArray) this.i.get(i);

        if (nibblearray != null) {
            return nibblearray;
        } else {
            long j = SectionPosition.a(i, EnumDirection.UP);
            int k = ((LightEngineStorageSky.a) this.f).c.get(SectionPosition.f(i));

            if (k != ((LightEngineStorageSky.a) this.f).b && SectionPosition.c(j) < k) {
                NibbleArray nibblearray1;

                while ((nibblearray1 = this.a(j, true)) == null) {
                    j = SectionPosition.a(j, EnumDirection.UP);
                }

                return new NibbleArray((new NibbleArrayFlat(nibblearray1, 0)).asBytes());
            } else {
                return new NibbleArray();
            }
        }
    }

    @Override
    protected void a(LightEngineLayer<LightEngineStorageSky.a, ?> lightenginelayer, boolean flag, boolean flag1) {
        super.a(lightenginelayer, flag, flag1);
        if (flag) {
            LongIterator longiterator;
            long i;
            int j;
            int k;

            if (!this.m.isEmpty()) {
                longiterator = this.m.iterator();

                while (longiterator.hasNext()) {
                    i = (Long) longiterator.next();
                    j = this.c(i);
                    if (j != 2 && !this.n.contains(i) && this.l.add(i)) {
                        int l;

                        if (j == 1) {
                            this.a(lightenginelayer, i);
                            if (this.g.add(i)) {
                                ((LightEngineStorageSky.a) this.f).a(i);
                            }

                            Arrays.fill(this.a(i, true).asBytes(), (byte) -1);
                            k = SectionPosition.c(SectionPosition.b(i));
                            l = SectionPosition.c(SectionPosition.c(i));
                            int i1 = SectionPosition.c(SectionPosition.d(i));
                            EnumDirection[] aenumdirection = LightEngineStorageSky.k;
                            int j1 = aenumdirection.length;

                            long k1;

                            for (int l1 = 0; l1 < j1; ++l1) {
                                EnumDirection enumdirection = aenumdirection[l1];

                                k1 = SectionPosition.a(i, enumdirection);
                                if ((this.n.contains(k1) || !this.l.contains(k1) && !this.m.contains(k1)) && this.g(k1)) {
                                    for (int i2 = 0; i2 < 16; ++i2) {
                                        for (int j2 = 0; j2 < 16; ++j2) {
                                            long k2;
                                            long l2;

                                            switch (enumdirection) {
                                                case NORTH:
                                                    k2 = BlockPosition.a(k + i2, l + j2, i1);
                                                    l2 = BlockPosition.a(k + i2, l + j2, i1 - 1);
                                                    break;
                                                case SOUTH:
                                                    k2 = BlockPosition.a(k + i2, l + j2, i1 + 16 - 1);
                                                    l2 = BlockPosition.a(k + i2, l + j2, i1 + 16);
                                                    break;
                                                case WEST:
                                                    k2 = BlockPosition.a(k, l + i2, i1 + j2);
                                                    l2 = BlockPosition.a(k - 1, l + i2, i1 + j2);
                                                    break;
                                                default:
                                                    k2 = BlockPosition.a(k + 16 - 1, l + i2, i1 + j2);
                                                    l2 = BlockPosition.a(k + 16, l + i2, i1 + j2);
                                            }

                                            lightenginelayer.a(k2, l2, lightenginelayer.b(k2, l2, 0), true);
                                        }
                                    }
                                }
                            }

                            for (int i3 = 0; i3 < 16; ++i3) {
                                for (j1 = 0; j1 < 16; ++j1) {
                                    long j3 = BlockPosition.a(SectionPosition.c(SectionPosition.b(i)) + i3, SectionPosition.c(SectionPosition.c(i)), SectionPosition.c(SectionPosition.d(i)) + j1);

                                    k1 = BlockPosition.a(SectionPosition.c(SectionPosition.b(i)) + i3, SectionPosition.c(SectionPosition.c(i)) - 1, SectionPosition.c(SectionPosition.d(i)) + j1);
                                    lightenginelayer.a(j3, k1, lightenginelayer.b(j3, k1, 0), true);
                                }
                            }
                        } else {
                            for (k = 0; k < 16; ++k) {
                                for (l = 0; l < 16; ++l) {
                                    long k3 = BlockPosition.a(SectionPosition.c(SectionPosition.b(i)) + k, SectionPosition.c(SectionPosition.c(i)) + 16 - 1, SectionPosition.c(SectionPosition.d(i)) + l);

                                    lightenginelayer.a(Long.MAX_VALUE, k3, 0, true);
                                }
                            }
                        }
                    }
                }
            }

            this.m.clear();
            if (!this.n.isEmpty()) {
                longiterator = this.n.iterator();

                while (longiterator.hasNext()) {
                    i = (Long) longiterator.next();
                    if (this.l.remove(i) && this.g(i)) {
                        for (j = 0; j < 16; ++j) {
                            for (k = 0; k < 16; ++k) {
                                long l3 = BlockPosition.a(SectionPosition.c(SectionPosition.b(i)) + j, SectionPosition.c(SectionPosition.c(i)) + 16 - 1, SectionPosition.c(SectionPosition.d(i)) + k);

                                lightenginelayer.a(Long.MAX_VALUE, l3, 15, false);
                            }
                        }
                    }
                }
            }

            this.n.clear();
            this.p = false;
        }
    }

    protected boolean a(int i) {
        return i >= ((LightEngineStorageSky.a) this.f).b;
    }

    protected boolean m(long i) {
        int j = BlockPosition.c(i);

        if ((j & 15) != 15) {
            return false;
        } else {
            long k = SectionPosition.e(i);
            long l = SectionPosition.f(k);

            if (!this.o.contains(l)) {
                return false;
            } else {
                int i1 = ((LightEngineStorageSky.a) this.f).c.get(l);

                return SectionPosition.c(i1) == j + 16;
            }
        }
    }

    protected boolean n(long i) {
        long j = SectionPosition.f(i);
        int k = ((LightEngineStorageSky.a) this.f).c.get(j);

        return k == ((LightEngineStorageSky.a) this.f).b || SectionPosition.c(i) >= k;
    }

    protected boolean o(long i) {
        long j = SectionPosition.f(i);

        return this.o.contains(j);
    }

    public static final class a extends LightEngineStorageArray<LightEngineStorageSky.a> {

        private int b;
        private final Long2IntOpenHashMap c;

        public a(Long2ObjectOpenHashMap<NibbleArray> long2objectopenhashmap, Long2IntOpenHashMap long2intopenhashmap, int i) {
            super(long2objectopenhashmap);
            this.c = long2intopenhashmap;
            long2intopenhashmap.defaultReturnValue(i);
            this.b = i;
        }

        @Override
        public LightEngineStorageSky.a b() {
            return new LightEngineStorageSky.a(this.a.clone(), this.c.clone(), this.b);
        }
    }
}
