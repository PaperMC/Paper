package com.destroystokyo.paper.util.set;

import java.util.Collection;

/**
 * @author Spottedleaf <Spottedleaf@users.noreply.github.com>
 */
public final class OptimizedSmallEnumSet<E extends Enum<E>> {

    private final Class<E> enumClass;
    private long backingSet;

    public OptimizedSmallEnumSet(final Class<E> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Null class");
        }
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Class must be enum, not " + clazz.getCanonicalName());
        }
        this.enumClass = clazz;
    }

    public boolean addUnchecked(final E element) {
        final int ordinal = element.ordinal();
        final long key = 1L << ordinal;

        final long prev = this.backingSet;
        this.backingSet = prev | key;

        return (prev & key) == 0;
    }

    public boolean removeUnchecked(final E element) {
        final int ordinal = element.ordinal();
        final long key = 1L << ordinal;

        final long prev = this.backingSet;
        this.backingSet = prev & ~key;

        return (prev & key) != 0;
    }

    public void clear() {
        this.backingSet = 0L;
    }

    public int size() {
        return Long.bitCount(this.backingSet);
    }

    public void addAllUnchecked(final Collection<E> enums) {
        for (final E element : enums) {
            if (element == null) {
                throw new NullPointerException("Null element");
            }
            this.backingSet |= (1L << element.ordinal());
        }
    }

    public long getBackingSet() {
        return this.backingSet;
    }

    public boolean hasCommonElements(final OptimizedSmallEnumSet<E> other) {
        return (other.backingSet & this.backingSet) != 0;
    }

    public boolean hasElement(final E element) {
        return (this.backingSet & (1L << element.ordinal())) != 0;
    }
}
