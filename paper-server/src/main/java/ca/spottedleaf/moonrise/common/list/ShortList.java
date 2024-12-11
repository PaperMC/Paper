package ca.spottedleaf.moonrise.common.list;

import it.unimi.dsi.fastutil.shorts.Short2ShortOpenHashMap;
import java.util.Arrays;

public final class ShortList {

    private final Short2ShortOpenHashMap map = new Short2ShortOpenHashMap();
    {
        this.map.defaultReturnValue(Short.MIN_VALUE);
    }

    private static final short[] EMPTY_LIST = new short[0];

    private short[] byIndex = EMPTY_LIST;
    private short count;

    public int size() {
        return (int)this.count;
    }

    public short getRaw(final int index) {
        return this.byIndex[index];
    }

    public void setMinCapacity(final int len) {
        final short[] byIndex = this.byIndex;
        if (byIndex.length < len) {
            this.byIndex = Arrays.copyOf(byIndex, len);
        }
    }

    public boolean add(final short value) {
        final int count = (int)this.count;
        final short currIndex = this.map.putIfAbsent(value, (short)count);

        if (currIndex != Short.MIN_VALUE) {
            return false; // already in this list
        }

        short[] list = this.byIndex;

        if (list.length == count) {
            // resize required
            list = this.byIndex = Arrays.copyOf(list, (int)Math.max(4L, count * 2L)); // overflow results in negative
        }

        list[count] = value;
        this.count = (short)(count + 1);

        return true;
    }

    public boolean remove(final short value) {
        final short index = this.map.remove(value);
        if (index == Short.MIN_VALUE) {
            return false;
        }

        // move the entry at the end to this index
        final short endIndex = --this.count;
        final short end = this.byIndex[endIndex];
        if (index != endIndex) {
            // not empty after this call
            this.map.put(end, index);
        }
        this.byIndex[(int)index] = end;
        this.byIndex[(int)endIndex] = (short)0;

        return true;
    }

    public void clear() {
        this.count = (short)0;
        this.map.clear();
    }
}
