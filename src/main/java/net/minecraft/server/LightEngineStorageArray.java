package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;

public abstract class LightEngineStorageArray<M extends LightEngineStorageArray<M>> {

    private final long[] b = new long[2];
    private final NibbleArray[] c = new NibbleArray[2];
    private boolean d;
    protected final com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> data; // Paper - avoid copying light data
    protected final boolean isVisible; // Paper - avoid copying light data
    java.util.function.Function<Long, NibbleArray> lookup; // Paper - faster branchless lookup

    // Paper start - avoid copying light data
    protected LightEngineStorageArray(com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> data, boolean isVisible) {
        if (isVisible) {
            data.performUpdatesLockMap();
        }
        this.data = data;
        this.isVisible = isVisible;
        if (isVisible) {
            lookup = data::getVisibleAsync;
        } else {
            lookup = data::getUpdating;
        }
        // Paper end - avoid copying light data
        this.c();
        this.d = true;
    }

    public abstract M b();

    public void a(long i) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        this.data.queueUpdate(i, ((NibbleArray) this.data.getUpdating(i)).b()); // Paper - avoid copying light data
        this.c();
    }

    public boolean b(long i) {
        return lookup.apply(i) != null; // Paper - avoid copying light data
    }

    @Nullable
    public final NibbleArray c(long i) { // Paper - final
        if (this.d) {
            for (int j = 0; j < 2; ++j) {
                if (i == this.b[j]) {
                    return this.c[j];
                }
            }
        }

        NibbleArray nibblearray = lookup.apply(i); // Paper - avoid copying light data

        if (nibblearray == null) {
            return null;
        } else {
            if (this.d) {
                for (int k = 1; k > 0; --k) {
                    this.b[k] = this.b[k - 1];
                    this.c[k] = this.c[k - 1];
                }

                this.b[0] = i;
                this.c[0] = nibblearray;
            }

            return nibblearray;
        }
    }

    @Nullable
    public NibbleArray d(long i) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        return (NibbleArray) this.data.queueRemove(i); // Paper - avoid copying light data
    }

    public void a(long i, NibbleArray nibblearray) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        this.data.queueUpdate(i, nibblearray); // Paper - avoid copying light data
    }

    public void c() {
        for (int i = 0; i < 2; ++i) {
            this.b[i] = Long.MAX_VALUE;
            this.c[i] = null;
        }
    }

    public void d() {
        this.d = false;
    }
}
