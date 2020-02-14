package net.minecraft.server;

import java.util.Comparator;

public class NextTickListEntry<T> {

    private static final java.util.concurrent.atomic.AtomicLong COUNTER = new java.util.concurrent.atomic.AtomicLong(); // Paper - async chunk loading
    private final T e; public final T getData() { return this.e; } // Paper - OBFHELPER
    public final BlockPosition a; public final BlockPosition getPosition() { return this.a; } // Paper - OBFHELPER
    public final long b; public final long getTargetTick() { return this.b; } // Paper - OBFHELPER
    public final TickListPriority c; public final TickListPriority getPriority() { return this.c; } // Paper - OBFHELPER
    private final long f; public final long getId() { return this.f; } // Paper - OBFHELPER
    private final int hash; // Paper
    public int tickState; // Paper

    public NextTickListEntry(BlockPosition blockposition, T t0) {
        this(blockposition, t0, 0L, TickListPriority.NORMAL);
    }

    public NextTickListEntry(BlockPosition blockposition, T t0, long i, TickListPriority ticklistpriority) {
        this.f = (long) (NextTickListEntry.COUNTER.getAndIncrement()); // Paper - async chunk loading
        this.a = blockposition.immutableCopy();
        this.e = t0;
        this.b = i;
        this.c = ticklistpriority;
        this.hash = this.computeHash(); // Paper
    }

    public boolean equals(Object object) {
        if (!(object instanceof NextTickListEntry)) {
            return false;
        } else {
            NextTickListEntry<?> nextticklistentry = (NextTickListEntry) object;

            return this.a.equals(nextticklistentry.a) && this.e == nextticklistentry.e;
        }
    }

    // Paper start - optimize hashcode
    @Override
    public int hashCode() {
        return this.hash;
    }
    public final int computeHash() {
        // Paper end - optimize hashcode
        return this.a.hashCode();
    }

    // Paper start - let's not use more functional code for no reason.
    public static <T> Comparator<Object> comparator() { return NextTickListEntry.a(); } // Paper - OBFHELPER
    public static <T> Comparator<Object> a() {
        return (Comparator)(Comparator<NextTickListEntry>)(NextTickListEntry nextticklistentry, NextTickListEntry nextticklistentry1) -> {
            int i = Long.compare(nextticklistentry.getTargetTick(), nextticklistentry1.getTargetTick());

            if (i != 0) {
                return i;
            } else {
                i = nextticklistentry.getPriority().compareTo(nextticklistentry1.getPriority());
                return i != 0 ? i : Long.compare(nextticklistentry.getId(), nextticklistentry1.getId());
            }
        };
    }
    // Paper end - let's not use more functional code for no reason.

    public String toString() {
        return this.e + ": " + this.a + ", " + this.b + ", " + this.c + ", " + this.f;
    }

    public T b() {
        return this.e;
    }
}
