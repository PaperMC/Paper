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
        super(EnumSkyBlock.SKY, ilightaccess, new LightEngineStorageSky.a(new com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<>(), new com.destroystokyo.paper.util.map.QueuedChangesMapLong2Int(), Integer.MAX_VALUE, false)); // Paper - avoid copying light data
    }

    @Override
    protected int d(long i) {
        // Paper start
        int baseX = (int) (i >> 38);
        int baseY = (int) ((i << 52) >> 52);
        int baseZ = (int) ((i << 26) >> 38);
        long j = SectionPosition.blockPosAsSectionLong(baseX, baseY, baseZ);
        // Paper end
        int k = SectionPosition.c(j);
        synchronized (this.visibleUpdateLock) { // Paper - avoid copying light data
        LightEngineStorageSky.a lightenginestoragesky_a = (LightEngineStorageSky.a) this.e_visible; // Paper - avoid copying light data - must be after lock acquire
        int l = lightenginestoragesky_a.otherData.getVisibleAsync(SectionPosition.f(j)); // Paper - avoid copying light data

        if (l != lightenginestoragesky_a.b && k < l) {
            NibbleArray nibblearray = this.a(lightenginestoragesky_a, j); // Paper - decompile fix

            if (nibblearray == null) {
                for (i = BlockPosition.f(i); nibblearray == null; nibblearray = this.a(lightenginestoragesky_a, j)) { // Paper - decompile fix
                    j = SectionPosition.a(j, EnumDirection.UP);
                    ++k;
                    if (k >= l) {
                        return 15;
                    }

                    i = BlockPosition.a(i, 0, 16, 0);
                }
            }

            return nibblearray.a(baseX & 15, (int) ((i << 52) >> 52) & 15, (int) baseZ & 15); // Paper - y changed above
        } else {
            return 15;
        }
        } // Paper - avoid copying light data
    }

    @Override
    protected void k(long i) {
        int j = SectionPosition.c(i);

        if (((LightEngineStorageSky.a) this.f).b > j) {
            ((LightEngineStorageSky.a) this.f).b = j;
            ((LightEngineStorageSky.a) this.f).otherData.queueDefaultReturnValue(((LightEngineStorageSky.a) this.f).b); // Paper - avoid copying light data
        }

        long k = SectionPosition.f(i);
        int l = ((LightEngineStorageSky.a) this.f).otherData.getUpdating(k); // Paper - avoid copying light data

        if (l < j + 1) {
            ((LightEngineStorageSky.a) this.f).otherData.queueUpdate(k, j + 1); // Paper - avoid copying light data
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

        if (((LightEngineStorageSky.a) this.f).otherData.getUpdating(j) == k + 1) { // Paper - avoid copying light data
            long l;

            for (l = i; !this.g(l) && this.a(k); l = SectionPosition.a(l, EnumDirection.DOWN)) {
                --k;
            }

            if (this.g(l)) {
                ((LightEngineStorageSky.a) this.f).otherData.queueUpdate(j, k + 1); // Paper - avoid copying light data
                if (flag) {
                    this.q(l);
                }
            } else {
                ((LightEngineStorageSky.a) this.f).otherData.queueRemove(j); // Paper - avoid copying light data
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
            int j = ((LightEngineStorageSky.a) this.f).otherData.getUpdating(i); // Paper - avoid copying light data

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
            int k = ((LightEngineStorageSky.a) this.f).otherData.getUpdating(SectionPosition.f(i)); // Paper - avoid copying light data

            if (k != ((LightEngineStorageSky.a) this.f).b && SectionPosition.c(j) < k) {
                NibbleArray nibblearray1;

                while ((nibblearray1 = this.updating.getUpdatingOptimized(j)) == null) { // Paper
                    j = SectionPosition.a(j, EnumDirection.UP);
                }

                return new NibbleArray().markPoolSafe(new NibbleArrayFlat(nibblearray1, 0).asBytes()); // Paper - mark pool use as safe (no auto cleaner)
            } else {
                return new NibbleArray().markPoolSafe(); // Paper - mark pool use as safe (no auto cleaner)
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
                    i = longiterator.nextLong(); // Paper
                    int baseX = (int) (i >> 42) << 4; // Paper
                    int baseY = (int) (i << 44 >> 44) << 4; // Paper
                    int baseZ = (int) (i << 22 >> 42) << 4; // Paper
                    j = this.c(i);
                    if (j != 2 && !this.n.contains(i) && this.l.add(i)) {
                        int l;

                        if (j == 1) {
                            this.a(lightenginelayer, i);
                            if (this.g.add(i)) {
                                ((LightEngineStorageSky.a) this.f).a(i);
                            }

                            Arrays.fill(this.updating.getUpdatingOptimized(i).asBytesPoolSafe(), (byte) -1); // Paper - use optimized
                            k = baseX; // Paper
                            l = baseY; // Paper
                            int i1 = baseZ; // Paper
                            EnumDirection[] aenumdirection = LightEngineStorageSky.k;
                            int j1 = aenumdirection.length;

                            long k1;

                            for (int l1 = 0; l1 < j1; ++l1) {
                                EnumDirection enumdirection = aenumdirection[l1];

                                k1 = SectionPosition.getAdjacentFromBlockPos(baseX, baseY, baseZ, enumdirection); // Paper
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
                                    long j3 = BlockPosition.a(baseX + i3, baseY, baseZ + j1); // Paper

                                    k1 = BlockPosition.a(baseX + i3, baseY - 1, baseZ + j1); // Paper
                                    lightenginelayer.a(j3, k1, lightenginelayer.b(j3, k1, 0), true);
                                }
                            }
                        } else {
                            for (k = 0; k < 16; ++k) {
                                for (l = 0; l < 16; ++l) {
                                    long k3 = BlockPosition.a(baseX + k, baseY + 16 - 1, baseZ + l); // Paper

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
                    i = longiterator.nextLong(); // Paper
                    int baseX = (int) (i >> 42) << 4; // Paper
                    int baseY = (int) (i << 44 >> 44) << 4; // Paper
                    int baseZ = (int) (i << 22 >> 42) << 4; // Paper
                    if (this.l.remove(i) && this.g(i)) {
                        for (j = 0; j < 16; ++j) {
                            for (k = 0; k < 16; ++k) {
                                long l3 = BlockPosition.a(baseX + j, baseY + 16 - 1, baseZ + k); // Paper

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
                int i1 = ((LightEngineStorageSky.a) this.f).otherData.getUpdating(l); // Paper - avoid copying light data

                return SectionPosition.c(i1) == j + 16;
            }
        }
    }

    protected boolean n(long i) {
        long j = SectionPosition.f(i);
        int k = ((LightEngineStorageSky.a) this.f).otherData.getUpdating(j); // Paper - avoid copying light data

        return k == ((LightEngineStorageSky.a) this.f).b || SectionPosition.c(i) >= k;
    }

    protected boolean o(long i) {
        long j = SectionPosition.f(i);

        return this.o.contains(j);
    }

    public static final class a extends LightEngineStorageArray<LightEngineStorageSky.a> {

        private int b;
        private final com.destroystokyo.paper.util.map.QueuedChangesMapLong2Int otherData; // Paper - avoid copying light data

        // Paper start - avoid copying light data
        public a(com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> data, com.destroystokyo.paper.util.map.QueuedChangesMapLong2Int otherData, int i, boolean isVisible) {
            super(data, isVisible);
            this.otherData = otherData;
            otherData.queueDefaultReturnValue(i);
            // Paper end - avoid copying light data
            this.b = i;
        }

        @Override
        public LightEngineStorageSky.a b() {
            this.otherData.performUpdatesLockMap(); // Paper - avoid copying light data
            return new LightEngineStorageSky.a(this.data, this.otherData, this.b, true); // Paper - avoid copying light data
        }
    }
}
