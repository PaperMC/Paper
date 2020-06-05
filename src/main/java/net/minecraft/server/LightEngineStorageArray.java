package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;

public abstract class LightEngineStorageArray<M extends LightEngineStorageArray<M>> {

    // private final long[] b = new long[2]; // Paper - unused
    private final NibbleArray[] c = new NibbleArray[]{NibbleArray.EMPTY_NIBBLE_ARRAY, NibbleArray.EMPTY_NIBBLE_ARRAY}; private final NibbleArray[] cache = c; // Paper - OBFHELPER
    private boolean d;
    protected final com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> data; // Paper - avoid copying light data
    protected final boolean isVisible; // Paper - avoid copying light data

    // Paper start - faster lookups with less branching, use interface to avoid boxing instead of Function
    public final NibbleArrayAccess lookup;
    public interface NibbleArrayAccess {
        NibbleArray apply(long id);
    }
    // Paper end
    // Paper start - avoid copying light data
    protected LightEngineStorageArray(com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> data, boolean isVisible) {
        if (isVisible) {
            data.performUpdatesLockMap();
        }
        this.data = data;
        this.isVisible = isVisible;
        // Paper end - avoid copying light data
        // Paper start - faster lookups with less branching
        if (isVisible) {
            lookup = data::getVisibleAsync;
        } else {
            lookup = data.getUpdatingMap()::get; // jump straight the sub map
        }
        // Paper end
        this.c();
        this.d = true;
    }

    public abstract M b();

    public void a(long i) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        NibbleArray updating = this.data.getUpdating(i); // Paper - pool nibbles
        NibbleArray nibblearray = new NibbleArray().markPoolSafe(updating.getCloneIfSet()); // Paper
        nibblearray.lightCacheKey = i; // Paper
        this.data.queueUpdate(i, nibblearray); // Paper - avoid copying light data - pool safe clone
        if (updating.cleaner != null) MCUtil.scheduleTask(2, updating.cleaner, "Light Engine Release"); // Paper - delay clean incase anything holding ref was still using it
        this.c();
    }

    public boolean b(long i) {
        return lookup.apply(i) != null; // Paper - avoid copying light data
    }

    // Paper start - less branching as we know we are using cache and updating
    public final NibbleArray getUpdatingOptimized(final long i) { // Paper - final
        final NibbleArray[] cache = this.cache;
        if (cache[0].lightCacheKey == i) return cache[0];
        if (cache[1].lightCacheKey == i) return cache[1];

        final NibbleArray nibblearray = this.lookup.apply(i); // Paper - avoid copying light data
        if (nibblearray == null) {
            return null;
        } else {
            cache[1] = cache[0];
            cache[0] = nibblearray;
            return nibblearray;
        }
    }
    // Paper end

    @Nullable
    public final NibbleArray c(final long i) { // Paper - final
        // Paper start - optimize visible case or missed updating cases
        if (this.d) {
            // short circuit to optimized
            return getUpdatingOptimized(i);
        }

        return this.lookup.apply(i);
        // Paper end
    }

    @Nullable
    public NibbleArray d(long i) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        return (NibbleArray) this.data.queueRemove(i); // Paper - avoid copying light data
    }

    public void a(long i, NibbleArray nibblearray) {
        if (this.isVisible) { throw new IllegalStateException("writing to visible data"); } // Paper - avoid copying light data
        nibblearray.lightCacheKey = i; // Paper
        this.data.queueUpdate(i, nibblearray); // Paper - avoid copying light data
    }

    public void c() {
        for (int i = 0; i < 2; ++i) {
            // this.b[i] = Long.MAX_VALUE; // Paper - Unused
            this.c[i] = NibbleArray.EMPTY_NIBBLE_ARRAY; // Paper
        }
    }

    public void d() {
        this.d = false;
    }
}
