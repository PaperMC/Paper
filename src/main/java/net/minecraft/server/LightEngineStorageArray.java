package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;

public abstract class LightEngineStorageArray<M extends LightEngineStorageArray<M>> {

    private final long[] b = new long[2];
    private final NibbleArray[] c = new NibbleArray[2];
    private boolean d;
    protected final Long2ObjectOpenHashMap<NibbleArray> a;

    protected LightEngineStorageArray(Long2ObjectOpenHashMap<NibbleArray> long2objectopenhashmap) {
        this.a = long2objectopenhashmap;
        this.c();
        this.d = true;
    }

    public abstract M b();

    public void a(long i) {
        this.a.put(i, ((NibbleArray) this.a.get(i)).b());
        this.c();
    }

    public boolean b(long i) {
        return this.a.containsKey(i);
    }

    @Nullable
    public NibbleArray c(long i) {
        if (this.d) {
            for (int j = 0; j < 2; ++j) {
                if (i == this.b[j]) {
                    return this.c[j];
                }
            }
        }

        NibbleArray nibblearray = (NibbleArray) this.a.get(i);

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
        return (NibbleArray) this.a.remove(i);
    }

    public void a(long i, NibbleArray nibblearray) {
        this.a.put(i, nibblearray);
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
