package org.bukkit.craftbukkit.util;

import java.util.AbstractList;
import java.util.List;

/**
 * An aggregated unmodifiable view of two lists.
 */
public class CombinedList<E> extends AbstractList<E> {

    private final List<E> lhs;
    private final List<E> rhs;

    public CombinedList(final List<E> lhs, final List<E> rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public int size() {
        return this.lhs.size() + this.rhs.size();
    }

    @Override
    public E get(final int index) {
        if (index >= this.size() || index < 0) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        final int middleIndex = this.lhs.size();
        if (index < middleIndex) {
            return this.lhs.get(index);
        } else {
            return this.rhs.get(index - middleIndex);
        }
    }
}
