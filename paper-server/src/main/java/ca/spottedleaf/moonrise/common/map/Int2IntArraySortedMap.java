package ca.spottedleaf.moonrise.common.map;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;

import java.util.Arrays;

public class Int2IntArraySortedMap {

    protected int[] key;
    protected int[] val;
    protected int size;

    public Int2IntArraySortedMap() {
        this.key = new int[8];
        this.val = new int[8];
    }

    public int put(final int key, final int value) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index >= 0) {
            final int current = this.val[index];
            this.val[index] = value;
            return current;
        }
        final int insert = -(index + 1);
        // shift entries down
        if (this.size >= this.val.length) {
            this.key = Arrays.copyOf(this.key, this.key.length * 2);
            this.val = Arrays.copyOf(this.val, this.val.length * 2);
        }
        System.arraycopy(this.key, insert, this.key, insert + 1, this.size - insert);
        System.arraycopy(this.val, insert, this.val, insert + 1, this.size - insert);
        ++this.size;

        this.key[insert] = key;
        this.val[insert] = value;

        return 0;
    }

    public int computeIfAbsent(final int key, final Int2IntFunction producer) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index >= 0) {
            return this.val[index];
        }
        final int insert = -(index + 1);
        // shift entries down
        if (this.size >= this.val.length) {
            this.key = Arrays.copyOf(this.key, this.key.length * 2);
            this.val = Arrays.copyOf(this.val, this.val.length * 2);
        }
        System.arraycopy(this.key, insert, this.key, insert + 1, this.size - insert);
        System.arraycopy(this.val, insert, this.val, insert + 1, this.size - insert);
        ++this.size;

        this.key[insert] = key;

        return this.val[insert] = producer.apply(key);
    }

    public int get(final int key) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index < 0) {
            return 0;
        }
        return this.val[index];
    }

    public int getFloor(final int key) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index < 0) {
            final int insert = -(index + 1) - 1;
            return insert < 0 ? 0 : this.val[insert];
        }
        return this.val[index];
    }
}
