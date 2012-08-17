/*
Based on OpenLongObjectHashMap from colt. Original copyright follows:

Copyright © 1999 CERN - European Organization for Nuclear Research.
Permission to use, copy, modify, distribute and sell this software and its documentation for any purpose
is hereby granted without fee, provided that the above copyright notice appear in all copies and
that both that copyright notice and this permission notice appear in supporting documentation.
CERN makes no representations about the suitability of this software for any purpose.
It is provided "as is" without expressed or implied warranty.
*/

package org.bukkit.craftbukkit.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongObjectHashMap<V> implements Cloneable {
    private long keys[];
    private V values[];

    private int freeEntries;
    private int elements;

    private int highWaterMark;
    private int lowWaterMark;
    private final double minLoadFactor;
    private final double maxLoadFactor;

    private final long FREE = 0;
    private final long REMOVED = Long.MIN_VALUE;

    private static final int defaultCapacity = 277;
    private static final double defaultMinLoadFactor = 0.2;
    private static final double defaultMaxLoadFactor = 0.5;

    private static final int largestPrime = Integer.MAX_VALUE;
    private static final int[] primeCapacities = {
            largestPrime,

            5, 11, 23, 47, 97, 197, 397, 797, 1597, 3203, 6421, 12853, 25717, 51437, 102877, 205759,
            411527, 823117, 1646237, 3292489, 6584983, 13169977, 26339969, 52679969, 105359939,
            210719881, 421439783, 842879579, 1685759167,

            433, 877, 1759, 3527, 7057, 14143, 28289, 56591, 113189, 226379, 452759, 905551, 1811107,
            3622219, 7244441, 14488931, 28977863, 57955739, 115911563, 231823147, 463646329, 927292699,
            1854585413,

            953, 1907, 3821, 7643, 15287, 30577, 61169, 122347, 244703, 489407, 978821, 1957651, 3915341,
            7830701, 15661423, 31322867, 62645741, 125291483, 250582987, 501165979, 1002331963,
            2004663929,

            1039, 2081, 4177, 8363, 16729, 33461, 66923, 133853, 267713, 535481, 1070981, 2141977, 4283963,
            8567929, 17135863, 34271747, 68543509, 137087021, 274174111, 548348231, 1096696463,

            31, 67, 137, 277, 557, 1117, 2237, 4481, 8963, 17929, 35863, 71741, 143483, 286973, 573953,
            1147921, 2295859, 4591721, 9183457, 18366923, 36733847, 73467739, 146935499, 293871013,
            587742049, 1175484103,

            599, 1201, 2411, 4831, 9677, 19373, 38747, 77509, 155027, 310081, 620171, 1240361, 2480729,
            4961459, 9922933, 19845871, 39691759, 79383533, 158767069, 317534141, 635068283, 1270136683,

            311, 631, 1277, 2557, 5119, 10243, 20507, 41017, 82037, 164089, 328213, 656429, 1312867,
            2625761, 5251529, 10503061, 21006137, 42012281, 84024581, 168049163, 336098327, 672196673,
            1344393353,

            3, 7, 17, 37, 79, 163, 331, 673, 1361, 2729, 5471, 10949, 21911, 43853, 87719, 175447, 350899,
            701819, 1403641, 2807303, 5614657, 11229331, 22458671, 44917381, 89834777, 179669557,
            359339171, 718678369, 1437356741,

            43, 89, 179, 359, 719, 1439, 2879, 5779, 11579, 23159, 46327, 92657, 185323, 370661, 741337,
            1482707, 2965421, 5930887, 11861791, 23723597, 47447201, 94894427, 189788857, 379577741,
            759155483, 1518310967,

            379, 761, 1523, 3049, 6101, 12203, 24407, 48817, 97649, 195311, 390647, 781301, 1562611,
            3125257, 6250537, 12501169, 25002389, 50004791, 100009607, 200019221, 400038451, 800076929,
            1600153859
    };

    static {
        java.util.Arrays.sort(primeCapacities);
    }

    public LongObjectHashMap() {
        this(defaultCapacity);
    }

    public LongObjectHashMap(int initialCapacity) {
        this(initialCapacity, defaultMinLoadFactor, defaultMaxLoadFactor);
    }

    @SuppressWarnings("unchecked")
    public LongObjectHashMap(int initialCapacity, double minLoadFactor, double maxLoadFactor) {
        int capacity = initialCapacity;
        capacity = nextPrime(capacity);
        if (capacity == 0) capacity = 1;

        keys = new long[capacity];
        values = (V[]) new Object[capacity];

        this.minLoadFactor = minLoadFactor;
        if (maxLoadFactor == largestPrime) {
            this.maxLoadFactor = 1.0;
        } else {
            this.maxLoadFactor = maxLoadFactor;
        }

        elements = 0;
        freeEntries = capacity;

        lowWaterMark = 0;
        highWaterMark = chooseHighWaterMark(capacity, this.maxLoadFactor);
    }

    public void clear() {
        Arrays.fill(keys, FREE);
        Arrays.fill(values, null);

        elements = 0;
        freeEntries = keys.length;
        trimToSize();
    }

    @SuppressWarnings("unchecked")
    public Set<Long> keySet() {
        return new KeySet();
    }

    @SuppressWarnings("unchecked")
    public Collection<V> values() {
        return new ValueCollection();
    }

    /**
     * Returns a Set of Entry objects for the HashMap. This is not how the internal
     * implementation is laid out so this constructs the entire Set when called. For
     * this reason it should be avoided if at all possible.
     * @deprecated
     * @return Set of Entry objects
     */
    @Deprecated
    public Set<Entry<Long, V>> entrySet() {
        HashSet<Entry<Long, V>> set = new HashSet<Entry<Long, V>>();

        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != FREE && keys[i] != REMOVED) {
                set.add(new LongObjectEntry(keys[i], values[i]));
            }
        }

        return set;
    }

    public boolean containsKey(int msw, int lsw) {
        return containsKey(LongHash.toLong(msw, lsw));
    }

    public boolean containsKey(long key) {
        return indexOfKey(key) >= 0;
    }

    public boolean containsValue(V value) {
        return indexOfValue(value) >= 0;
    }

    public int size() {
        return elements;
    }

    public boolean isEmpty() {
        return elements == 0;
    }

    public V get(int msw, int lsw) {
        return get(LongHash.toLong(msw, lsw));
    }

    public V get(long key) {
        int i = indexOfKey(key);
        if (i < 0) {
            return null;
        }
        return values[i];
    }

    public V put(int msw, int lsw, V value) {
        return put(LongHash.toLong(msw, lsw), value);
    }

    public V put(long key, V value) {
        int i = indexOfInsertion(key);
        if (i < 0) {
            i = -i - 1;
            V oldValue = values[i];
            values[i] = value;
            return oldValue;
        }

        if (elements > highWaterMark) {
            int newCapacity = chooseGrowCapacity(elements + 1, minLoadFactor, maxLoadFactor);
            rehash(newCapacity);
            return put(key, value);
        }

        if (keys[i] == FREE) {
            freeEntries--;
        }

        keys[i] = key;
        values[i] = value;
        elements++;

        if (freeEntries < 1) {
            int newCapacity = chooseGrowCapacity(elements + 1, minLoadFactor, maxLoadFactor);
            rehash(newCapacity);
        }

        return null;
    }

    public V remove(int msw, int lsw) {
        return remove(LongHash.toLong(msw, lsw));
    }

    public V remove(long key) {
        int i = indexOfKey(key);
        if (i < 0) {
            return null;
        }

        V oldValue = values[i];
        keys[i] = REMOVED;
        values[i] = null;
        elements--;

        if (elements < lowWaterMark) {
            int newCapacity = chooseShrinkCapacity(elements, minLoadFactor, maxLoadFactor);
            rehash(newCapacity);
        }

        return oldValue;
    }

    public void ensureCapacity(int minCapacity) {
        if (keys.length < minCapacity) {
            int newCapacity = nextPrime(minCapacity);
            rehash(newCapacity);
        }
    }

    public void trimToSize() {
        int newCapacity = nextPrime((int) (1 + 1.2 * size()));
        if (keys.length > newCapacity) {
            rehash(newCapacity);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        LongObjectHashMap copy = (LongObjectHashMap) super.clone();
        copy.keys = keys.clone();
        copy.values = values.clone();
        return copy;
    }

    /**
     * @param key the key to be added to the receiver.
     * @return the index where the key would need to be inserted, if it is not already contained.
     *         Returns -index-1 if the key is already contained at slot index.
     *         Therefore, if the returned index < 0, then it is already contained at slot -index-1.
     *         If the returned index >= 0, then it is NOT already contained and should be inserted at slot index.
     */
    private int indexOfInsertion(long key) {
        final int length = keys.length;

        final int hash = hash(key) & 0x7FFFFFFF;
        int i = hash % length;
        int decrement = hash % (length - 2);
        if (decrement == 0) {
            decrement = 1;
        }

        while ((keys[i] != FREE && keys[i] != REMOVED) && keys[i] != key) {
            i -= decrement;
            if (i < 0) {
                i += length;
            }
        }

        if (keys[i] == REMOVED) {
            int j = i;
            while (keys[i] != FREE && (keys[i] == REMOVED || keys[i] != key)) {
                i -= decrement;
                if (i < 0) {
                    i += length;
                }
            }
            if (keys[i] == FREE) {
                i = j;
            }
        }


        if (keys[i] != FREE && keys[i] != REMOVED) {
            // key already contained at slot i.
            // return a negative number identifying the slot.
            return -i - 1;
        }
        // not already contained, should be inserted at slot i.
        // return a number >= 0 identifying the slot.
        return i;
    }

    private int indexOfKey(long key) {
        final int length = keys.length;

        final int hash = hash(key) & 0x7FFFFFFF;
        int i = hash % length;
        int decrement = hash % (length - 2);
        if (decrement == 0) decrement = 1;

        while (keys[i] != FREE && (keys[i] == REMOVED || keys[i] != key)) {
            i -= decrement;
            if (i < 0) i += length;
        }

        if (keys[i] == FREE) {
            return -1;
        }

        return i;
    }

    private int indexOfValue(Object value) {
        for (int i = keys.length; --i >= 0; ) {
            if ((keys[i] != FREE && keys[i] != REMOVED) && values[i] == value) {
                return i;
            }
        }

        return -1;
    }

    @SuppressWarnings("unchecked")
    private void rehash(int newCapacity) {
        int oldCapacity = keys.length;

        long oldKeys[] = keys;
        V oldValues[] = values;

        long newKeys[] = new long[newCapacity];
        V newValues[] = (V[]) new Object[newCapacity];

        lowWaterMark = chooseLowWaterMark(newCapacity, minLoadFactor);
        highWaterMark = chooseHighWaterMark(newCapacity, maxLoadFactor);

        keys = newKeys;
        values = newValues;
        freeEntries = newCapacity - elements;

        for (int i = oldCapacity; i-- > 0; ) {
            if (oldKeys[i] != FREE && oldKeys[i] != REMOVED) {
                long element = oldKeys[i];
                int index = indexOfInsertion(element);
                newKeys[index] = element;
                newValues[index] = oldValues[i];
            }
        }
    }

    private static int nextPrime(int desiredCapacity) {
        int i = Arrays.binarySearch(primeCapacities, desiredCapacity);
        if (i < 0) {
            i = -i - 1;
        }

        return primeCapacities[i];
    }

    private int chooseGrowCapacity(int size, double minLoad, double maxLoad) {
        return nextPrime(Math.max(size + 1, (int) ((4 * size / (3 * minLoad + maxLoad)))));
    }

    private int chooseShrinkCapacity(int size, double minLoad, double maxLoad) {
        return nextPrime(Math.max(size + 1, (int) ((4 * size / (minLoad + 3 * maxLoad)))));
    }

    private int chooseHighWaterMark(int capacity, double maxLoad) {
        return Math.min(capacity - 2, (int) (capacity * maxLoad));
    }

    private int chooseLowWaterMark(int capacity, double minLoad) {
        return (int) (capacity * minLoad);
    }

    // This method copied from Murmur3, written by Austin Appleby released under Public Domain
    private int hash(long value) {
        value ^= value >>> 33;
        value *= 0xff51afd7ed558ccdL;
        value ^= value >>> 33;
        value *= 0xc4ceb9fe1a85ec53L;
        value ^= value >>> 33;
        return (int) value;
    }

    private class LongObjectEntry implements Entry<Long, V> {
        private final long key;
        private final V value;

        LongObjectEntry(long k, V v) {
            key = k;
            value = v;
        }

        public Long getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V v) {
            // Changes are not reflected in the main HashMap
            return value;
        }
    }

    private class KeySet extends AbstractSet {
        public int size() {
            return elements;
        }

        public boolean contains(long key) {
            return containsKey(key);
        }

        @SuppressWarnings("unchecked")
        public Iterator<Long> iterator() {
            return new KeyIterator();
        }
    }

    private class KeyIterator implements Iterator {
        private int ix;

        private KeyIterator() {
            for (ix = 0; ix < keys.length; ix++) {
                if (values[ix] != null && keys[ix] != REMOVED) {
                    break;
                }
            }
        }

        public boolean hasNext() {
            return ix < keys.length;
        }

        public void remove() {
            throw new UnsupportedOperationException("Collection is read-only");
        }

        public Long next() {
            if (ix >= keys.length) {
                throw new NoSuchElementException();
            }

            long key = keys[ix++];

            for (; ix < keys.length; ix++) {
                if (keys[ix] != FREE && keys[ix] != REMOVED) {
                    break;
                }
            }

            return key;
        }
    }

    private class ValueCollection extends AbstractCollection<V> {
        public int size() {
            return elements;
        }

        @SuppressWarnings("unchecked")
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @SuppressWarnings("unchecked")
        public boolean contains(Object value) {
            return containsValue((V) value);
        }
    }

    private class ValueIterator<V> implements Iterator<V> {
        private int ix;

        private ValueIterator() {
            for (ix = 0; ix < values.length; ix++) {
                if (values[ix] != null) {
                    break;
                }
            }
        }

        public boolean hasNext() {
            return ix < values.length;
        }

        public void remove() {
            throw new UnsupportedOperationException("Collection is read-only");
        }

        @SuppressWarnings("unchecked")
        public V next() {
            if (ix >= values.length) {
                throw new NoSuchElementException();
            }

            V value = (V) values[ix++];

            for (; ix < values.length; ix++) {
                if (values[ix] != null) {
                    break;
                }
            }

            return value;
        }
    }
}
