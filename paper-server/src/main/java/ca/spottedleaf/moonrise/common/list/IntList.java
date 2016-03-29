package ca.spottedleaf.moonrise.common.list;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.util.Arrays;

public final class IntList {

    private final Int2IntOpenHashMap map = new Int2IntOpenHashMap();
    {
        this.map.defaultReturnValue(Integer.MIN_VALUE);
    }

    private static final int[] EMPTY_LIST = new int[0];

    private int[] byIndex = EMPTY_LIST;
    private int count;

    public int size() {
        return this.count;
    }

    public void setMinCapacity(final int len) {
        final int[] byIndex = this.byIndex;
        if (byIndex.length < len) {
            this.byIndex = Arrays.copyOf(byIndex, len);
        }
    }

    public int getRaw(final int index) {
        return this.byIndex[index];
    }

    public boolean add(final int value) {
        final int count = this.count;
        final int currIndex = this.map.putIfAbsent(value, count);

        if (currIndex != Integer.MIN_VALUE) {
            return false; // already in this list
        }

        int[] list = this.byIndex;

        if (list.length == count) {
            // resize required
            list = this.byIndex = Arrays.copyOf(list, (int)Math.max(4L, count * 2L)); // overflow results in negative
        }

        list[count] = value;
        this.count = count + 1;

        return true;
    }

    public boolean remove(final int value) {
        final int index = this.map.remove(value);
        if (index == Integer.MIN_VALUE) {
            return false;
        }

        // move the entry at the end to this index
        final int endIndex = --this.count;
        final int end = this.byIndex[endIndex];
        if (index != endIndex) {
            // not empty after this call
            this.map.put(end, index);
        }
        this.byIndex[index] = end;
        this.byIndex[endIndex] = 0;

        return true;
    }

    public void clear() {
        this.count = 0;
        this.map.clear();
    }
}
