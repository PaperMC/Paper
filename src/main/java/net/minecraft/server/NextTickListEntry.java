package net.minecraft.server;

import java.util.Comparator;

public class NextTickListEntry<T> {

    private static long d;
    private final T e;
    public final BlockPosition a;
    public final long b;
    public final TickListPriority c;
    private final long f;

    public NextTickListEntry(BlockPosition blockposition, T t0) {
        this(blockposition, t0, 0L, TickListPriority.NORMAL);
    }

    public NextTickListEntry(BlockPosition blockposition, T t0, long i, TickListPriority ticklistpriority) {
        this.f = (long) (NextTickListEntry.d++);
        this.a = blockposition.immutableCopy();
        this.e = t0;
        this.b = i;
        this.c = ticklistpriority;
    }

    public boolean equals(Object object) {
        if (!(object instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry<?> nextticklistentry = (NextTickListEntry) object;

            return this.a.equals(nextticklistentry.a) && this.e == nextticklistentry.e;
        }
    }

    public int hashCode() {
        return this.a.hashCode();
    }

    public static <T> Comparator<Object> a() { // Paper - decompile fix
        return Comparator.comparingLong((nextticklistentry) -> {
            return ((NextTickListEntry<T>) nextticklistentry).b; // Paper - decompile fix
        }).thenComparing((nextticklistentry) -> {
            return ((NextTickListEntry<T>) nextticklistentry).c; // Paper - decompile fix
        }).thenComparingLong((nextticklistentry) -> {
            return ((NextTickListEntry<T>) nextticklistentry).f; // Paper - decompile fix
        });
    }

    public String toString() {
        return this.e + ": " + this.a + ", " + this.b + ", " + this.c + ", " + this.f;
    }

    public T b() {
        return this.e;
    }
}
