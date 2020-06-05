package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;

public abstract class LightEngineStorage<M extends LightEngineStorageArray<M>> extends LightEngineGraphSection {

    protected static final NibbleArray a = new NibbleArray();
    private static final EnumDirection[] k = EnumDirection.values();
    private final EnumSkyBlock l;
    private final ILightAccess m;
    protected final LongSet b = new LongOpenHashSet();
    protected final LongSet c = new LongOpenHashSet();
    protected final LongSet d = new LongOpenHashSet();
    protected volatile M e_visible; protected final Object visibleUpdateLock = new Object(); // Paper - diff on change, should be "visible" - force compile fail on usage change
    protected final M f; protected final M updating; // Paper - diff on change, should be "updating"
    protected final LongSet g = new LongOpenHashSet();
    protected final LongSet h = new LongOpenHashSet(); LongSet dirty = h; // Paper - OBFHELPER
    protected final Long2ObjectMap<NibbleArray> i = Long2ObjectMaps.synchronize(new Long2ObjectOpenHashMap());
    private final LongSet n = new LongOpenHashSet();
    private final LongSet o = new LongOpenHashSet();
    private final LongSet p = new LongOpenHashSet();
    protected volatile boolean j;

    protected LightEngineStorage(EnumSkyBlock enumskyblock, ILightAccess ilightaccess, M m0) {
        super(3, 256, 256); // Paper - bump expected size of level sets to improve collisions and reduce rehashing (seen a lot of it)
        this.l = enumskyblock;
        this.m = ilightaccess;
        this.f = m0; updating = m0; // Paper
        this.e_visible = m0.b(); // Paper - avoid copying light data
        this.e_visible.d(); // Paper - avoid copying light data
    }

    protected final boolean g(long i) { // Paper - final to help inlining
        return this.updating.getUpdatingOptimized(i) != null; // Paper - inline to avoid branching
    }

    @Nullable
    protected NibbleArray a(long i, boolean flag) {
        // Paper start - avoid copying light data
        if (flag) {
            return this.updating.getUpdatingOptimized(i);
        } else {
            synchronized (this.visibleUpdateLock) {
                return this.e_visible.lookup.apply(i);
            }
        }
        // Paper end - avoid copying light data
    }

    @Nullable
    protected final NibbleArray a(M m0, long i) { // Paper
        return m0.c(i);
    }

    @Nullable
    public NibbleArray h(long i) {
        NibbleArray nibblearray = (NibbleArray) this.i.get(i);

        return nibblearray != null ? nibblearray : this.a(i, false);
    }

    protected abstract int d(long i);

    protected int i(long i) {
        // Paper start - reuse and inline math, use Optimized Updating path
        final int x = (int) (i >> 38);
        final int y = (int) ((i << 52) >> 52);
        final int z = (int) ((i << 26) >> 38);
        long j = SectionPosition.blockPosAsSectionLong(x, y, z);
        NibbleArray nibblearray = this.updating.getUpdatingOptimized(j);
        //  BUG: Sometimes returns null and crashes, try to recover, but to prevent crash just return no light.
        if (nibblearray == null) {
            nibblearray = this.e_visible.lookup.apply(j);
        }
        if (nibblearray == null) {
            System.err.println("Null nibble, preventing crash " + BlockPosition.fromLong(i));
            return 0;
        }

        return nibblearray.a(x & 15, y & 15, z & 15); // Paper - inline operations
        // Paper end
    }

    protected void b(long i, int j) {
        // Paper start - cache part of the math done in loop below
        int x = (int) (i >> 38);
        int y = (int) ((i << 52) >> 52);
        int z = (int) ((i << 26) >> 38);
        long k = SectionPosition.blockPosAsSectionLong(x, y, z);
        // Paper end

        if (this.g.add(k)) {
            this.f.a(k);
        }

        NibbleArray nibblearray = this.a(k, true);
        nibblearray.a(x & 15, y & 15, z & 15, j); // Paper - use already calculated x/y/z

        // Paper start - credit to JellySquid for a major optimization here:
        /*
         * An extremely important optimization is made here in regards to adding items to the pending notification set. The
         * original implementation attempts to add the coordinate of every chunk which contains a neighboring block position
         * even though a huge number of loop iterations will simply map to block positions within the same updating chunk.
         *
         * Our implementation here avoids this by pre-calculating the min/max chunk coordinates so we can iterate over only
         * the relevant chunk positions once. This reduces what would always be 27 iterations to just 1-8 iterations.
         *
         * @reason Use faster implementation
         * @author JellySquid
         */
        for (int z2 = (z - 1) >> 4; z2 <= (z + 1) >> 4; ++z2) {
            for (int x2 = (x - 1) >> 4; x2 <= (x + 1) >> 4; ++x2) {
                for (int y2 = (y - 1) >> 4; y2 <= (y + 1) >> 4; ++y2) {
                    this.dirty.add(SectionPosition.asLong(x2, y2, z2));
                    // Paper end
                }
            }
        }

    }

    @Override
    protected int c(long i) {
        return i == Long.MAX_VALUE ? 2 : (this.b.contains(i) ? 0 : (!this.p.contains(i) && this.f.b(i) ? 1 : 2));
    }

    @Override
    protected int b(long i) {
        return this.c.contains(i) ? 2 : (!this.b.contains(i) && !this.d.contains(i) ? 2 : 0);
    }

    @Override
    protected void a(long i, int j) {
        int k = this.c(i);

        if (k != 0 && j == 0) {
            this.b.add(i);
            this.d.remove(i);
        }

        if (k == 0 && j != 0) {
            this.b.remove(i);
            this.c.remove(i);
        }

        if (k >= 2 && j != 2) {
            if (!this.p.remove(i)) { // Paper - remove useless contains - credit to JellySquid
                //this.p.remove(i); // Paper
            //} else { // Paper
                this.f.a(i, this.j(i));
                this.g.add(i);
                this.k(i);

                // Paper start - reuse x/y/z and only notify valid chunks - Credit to JellySquid (See above method for notes)
                int x = (int) (i >> 38);
                int y = (int) ((i << 52) >> 52);
                int z = (int) ((i << 26) >> 38);

                for (int z2 = (z - 1) >> 4; z2 <= (z + 1) >> 4; ++z2) {
                    for (int x2 = (x - 1) >> 4; x2 <= (x + 1) >> 4; ++x2) {
                        for (int y2 = (y - 1) >> 4; y2 <= (y + 1) >> 4; ++y2) {
                            this.dirty.add(SectionPosition.asLong(x2, y2, z2));
                            // Paper end
                        }
                    }
                }
            }
        }

        if (k != 2 && j >= 2) {
            this.p.add(i);
        }

        this.j = !this.p.isEmpty();
    }

    protected NibbleArray j(long i) {
        NibbleArray nibblearray = (NibbleArray) this.i.get(i);

        return nibblearray != null ? nibblearray : new NibbleArray().markPoolSafe(); // Paper
    }

    protected void a(LightEngineLayer<?, ?> lightenginelayer, long i) {
        if (lightenginelayer.c() < 8192) {
            lightenginelayer.a((j) -> {
                return SectionPosition.e(j) == i;
            });
        } else {
            int j = (int) (i >> 42) << 4; // Paper - inline
            int k = (int) (i << 44 >> 44) << 4; // Paper - inline
            int l = (int) (i << 22 >> 42) << 4; // Paper - inline

            for (int i1 = 0; i1 < 16; ++i1) {
                for (int j1 = 0; j1 < 16; ++j1) {
                    for (int k1 = 0; k1 < 16; ++k1) {
                        long l1 = BlockPosition.a(j + i1, k + j1, l + k1);

                        lightenginelayer.e(l1);
                    }
                }
            }

        }
    }

    protected boolean a() {
        return this.j;
    }

    protected void a(LightEngineLayer<M, ?> lightenginelayer, boolean flag, boolean flag1) {
        if (this.a() || !this.i.isEmpty()) {
            LongIterator longiterator = this.p.iterator();

            long i;
            NibbleArray nibblearray;

            while (longiterator.hasNext()) {
                i = longiterator.nextLong(); // Paper
                this.a(lightenginelayer, i);
                NibbleArray nibblearray1 = (NibbleArray) this.i.remove(i);

                nibblearray = this.f.d(i);
                if (this.o.contains(SectionPosition.f(i))) {
                    if (nibblearray1 != null) {
                        this.i.put(i, nibblearray1);
                    } else if (nibblearray != null) {
                        this.i.put(i, nibblearray);
                    }
                }
            }

            this.f.c();
            longiterator = this.p.iterator();

            while (longiterator.hasNext()) {
                i = longiterator.nextLong(); // Paper
                this.l(i);
            }

            this.p.clear();
            this.j = false;
            ObjectIterator objectiterator = this.i.long2ObjectEntrySet().iterator();

            Entry entry;
            long j;

            NibbleArray test = null; // Paper
            while (objectiterator.hasNext()) {
                entry = (Entry) objectiterator.next();
                j = entry.getLongKey();
                if ((test = this.updating.getUpdatingOptimized(j)) != null) { // Paper - dont look up nibble twice
                    nibblearray = (NibbleArray) entry.getValue();
                    if (test != nibblearray) { // Paper
                        this.a(lightenginelayer, j);
                        this.f.a(j, nibblearray);
                        this.g.add(j);
                    }
                }
            }

            this.f.c();
            if (!flag1) {
                longiterator = this.i.keySet().iterator();

                while (longiterator.hasNext()) {
                    i = longiterator.nextLong(); // Paper
                    this.b(lightenginelayer, i);
                }
            } else {
                longiterator = this.n.iterator();

                while (longiterator.hasNext()) {
                    i = longiterator.nextLong(); // Paper
                    this.b(lightenginelayer, i);
                }
            }

            this.n.clear();
            objectiterator = this.i.long2ObjectEntrySet().iterator();

            while (objectiterator.hasNext()) {
                entry = (Entry) objectiterator.next();
                j = entry.getLongKey();
                if (this.g(j)) {
                    objectiterator.remove();
                }
            }

        }
    }

    private void b(LightEngineLayer<M, ?> lightenginelayer, long i) {
        if (this.g(i)) {
            // Paper start
            int secX = (int) (i >> 42);
            int secY = (int) (i << 44 >> 44);
            int secZ = (int) (i << 22 >> 42);
            int j = secX << 4; // baseX
            int k = secY << 4; // baseY
            int l = secZ << 4; // baseZ
            // Paper end
            EnumDirection[] aenumdirection = LightEngineStorage.k;
            int i1 = aenumdirection.length;

            for (int j1 = 0; j1 < i1; ++j1) {
                EnumDirection enumdirection = aenumdirection[j1];
                long k1 = SectionPosition.getAdjacentFromSectionPos(secX, secY, secZ, enumdirection); // Paper - avoid extra unpacking

                if (!this.i.containsKey(k1) && this.g(k1)) {
                    for (int l1 = 0; l1 < 16; ++l1) {
                        for (int i2 = 0; i2 < 16; ++i2) {
                            long j2;
                            long k2;

                            switch (enumdirection) {
                                case DOWN:
                                    j2 = BlockPosition.a(j + i2, k, l + l1);
                                    k2 = BlockPosition.a(j + i2, k - 1, l + l1);
                                    break;
                                case UP:
                                    j2 = BlockPosition.a(j + i2, k + 16 - 1, l + l1);
                                    k2 = BlockPosition.a(j + i2, k + 16, l + l1);
                                    break;
                                case NORTH:
                                    j2 = BlockPosition.a(j + l1, k + i2, l);
                                    k2 = BlockPosition.a(j + l1, k + i2, l - 1);
                                    break;
                                case SOUTH:
                                    j2 = BlockPosition.a(j + l1, k + i2, l + 16 - 1);
                                    k2 = BlockPosition.a(j + l1, k + i2, l + 16);
                                    break;
                                case WEST:
                                    j2 = BlockPosition.a(j, k + l1, l + i2);
                                    k2 = BlockPosition.a(j - 1, k + l1, l + i2);
                                    break;
                                default:
                                    j2 = BlockPosition.a(j + 16 - 1, k + l1, l + i2);
                                    k2 = BlockPosition.a(j + 16, k + l1, l + i2);
                            }

                            lightenginelayer.a(j2, k2, lightenginelayer.b(j2, k2, lightenginelayer.c(j2)), false);
                            lightenginelayer.a(k2, j2, lightenginelayer.b(k2, j2, lightenginelayer.c(k2)), false);
                        }
                    }
                }
            }

        }
    }

    protected void k(long i) {}

    protected void l(long i) {}

    protected void b(long i, boolean flag) {}

    public void c(long i, boolean flag) {
        if (flag) {
            this.o.add(i);
        } else {
            this.o.remove(i);
        }

    }

    protected void a(long i, @Nullable NibbleArray nibblearray, boolean flag) {
        if (nibblearray != null) {
            NibbleArray remove = this.i.put(i, nibblearray); if (remove != null && remove.cleaner != null) remove.cleaner.run(); // Paper - clean up when removed
            if (!flag) {
                this.n.add(i);
            }
        } else {
            NibbleArray remove = this.i.remove(i); if (remove != null && remove.cleaner != null) remove.cleaner.run(); // Paper - clean up when removed
        }

    }

    protected void d(long i, boolean flag) {
        boolean flag1 = this.b.contains(i);

        if (!flag1 && !flag) {
            this.d.add(i);
            this.a(Long.MAX_VALUE, i, 0, true);
        }

        if (flag1 && flag) {
            this.c.add(i);
            this.a(Long.MAX_VALUE, i, 2, false);
        }

    }

    protected void d() {
        if (this.b()) {
            this.b(Integer.MAX_VALUE);
        }

    }

    protected void e() {
        if (!this.g.isEmpty()) {
            synchronized (this.visibleUpdateLock) { // Paper - avoid copying light data
            M m0 = this.f.b();

            m0.d();
            this.e_visible = m0; // Paper - avoid copying light data
            } // Paper - avoid copying light data
            this.g.clear();
        }

        if (!this.h.isEmpty()) {
            LongIterator longiterator = this.h.iterator();

            while (longiterator.hasNext()) {
                long i = longiterator.nextLong();

                this.m.a(this.l, SectionPosition.a(i));
            }

            this.h.clear();
        }

    }
}
