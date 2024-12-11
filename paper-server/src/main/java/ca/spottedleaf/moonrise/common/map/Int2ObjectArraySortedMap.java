package ca.spottedleaf.moonrise.common.map;

import java.util.Arrays;
import java.util.function.IntFunction;

public class Int2ObjectArraySortedMap<V> {

    protected int[] key;
    protected V[] val;
    protected int size;

    public Int2ObjectArraySortedMap() {
        this.key = new int[8];
        this.val = (V[])new Object[8];
    }

    public V put(final int key, final V value) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index >= 0) {
            final V current = this.val[index];
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

        this.key[insert] = key;
        this.val[insert] = value;

        return null;
    }

    public V computeIfAbsent(final int key, final IntFunction<V> producer) {
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

        this.key[insert] = key;

        return this.val[insert] = producer.apply(key);
    }

    public V get(final int key) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index < 0) {
            return null;
        }
        return this.val[index];
    }

    public V getFloor(final int key) {
        final int index = Arrays.binarySearch(this.key, 0, this.size, key);
        if (index < 0) {
            final int insert = -(index + 1);
            return this.val[insert];
        }
        return this.val[index];
    }
}
