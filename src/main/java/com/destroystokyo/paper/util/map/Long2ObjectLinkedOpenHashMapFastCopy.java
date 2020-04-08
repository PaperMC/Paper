package com.destroystokyo.paper.util.map;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;

public class Long2ObjectLinkedOpenHashMapFastCopy<V> extends Long2ObjectLinkedOpenHashMap<V> {

    public void copyFrom(Long2ObjectLinkedOpenHashMapFastCopy<V> map) {
        if (key.length != map.key.length) {
            key = null;
            key = new long[map.key.length];
        }
        if (value.length != map.value.length) {
            value = null;
            //noinspection unchecked
            value = (V[]) new Object[map.value.length];
        }
        if (link.length != map.link.length) {
            link = null;
            link = new long[map.link.length];
        }
        System.arraycopy(map.key, 0, this.key, 0, map.key.length);
        System.arraycopy(map.value, 0, this.value, 0, map.value.length);
        System.arraycopy(map.link, 0, this.link, 0, map.link.length);
        this.size = map.size;
        this.mask = map.mask;
        this.first = map.first;
        this.last = map.last;
        this.n = map.n;
        this.maxFill = map.maxFill;
        this.containsNullKey = map.containsNullKey;
    }

    @Override
    public Long2ObjectLinkedOpenHashMapFastCopy<V> clone() {
        Long2ObjectLinkedOpenHashMapFastCopy<V> clone = (Long2ObjectLinkedOpenHashMapFastCopy<V>) super.clone();
        clone.copyFrom(this);
        return clone;
    }
}
