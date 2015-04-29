/*
  Based on CompactHashSet Copyright 2011 Ontopia Project

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/

package org.bukkit.craftbukkit.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class LongHashSet {
    private final static int INITIAL_SIZE = 3;
    private final static double LOAD_FACTOR = 0.75;

    private final static long FREE = 0;
    private final static long REMOVED = Long.MIN_VALUE;

    private int freeEntries;
    private int elements;
    private long[] values;
    private int modCount;

    public LongHashSet() {
        this(INITIAL_SIZE);
    }

    public LongHashSet(int size) {
        values = new long[(size==0 ? 1 : size)];
        elements = 0;
        freeEntries = values.length;
        modCount = 0;
    }

    public Iterator iterator() {
        return new Itr();
    }

    public int size() {
        return elements;
    }

    public boolean isEmpty() {
        return elements == 0;
    }

    public boolean contains(int msw, int lsw) {
        return contains(LongHash.toLong(msw, lsw));
    }

    public boolean contains(long value) {
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        return values[index] != FREE;
    }

    public boolean add(int msw, int lsw) {
        return add(LongHash.toLong(msw, lsw));
    }

    public boolean add(long value) {
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;
        int deletedix = -1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            // if there's a deleted object here we can put this object here,
            // provided it's not in here somewhere else already
            if (values[index] == REMOVED) {
                deletedix = index;
            }

            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        if (values[index] == FREE) {
            if (deletedix != -1) { // reusing a deleted cell
                index = deletedix;
            } else {
                freeEntries--;
            }

            modCount++;
            elements++;
            values[index] = value;

            if (1 - (freeEntries / (double) values.length) > LOAD_FACTOR) {
                rehash();
            }

            return true;
        } else {
            return false;
        }
    }

    public void remove(int msw, int lsw) {
        remove(LongHash.toLong(msw, lsw));
    }

    public boolean remove(long value) {
        int hash = hash(value);
        int index = (hash & 0x7FFFFFFF) % values.length;
        int offset = 1;

        // search for the object (continue while !null and !this object)
        while(values[index] != FREE && !(hash(values[index]) == hash && values[index] == value)) {
            index = ((index + offset) & 0x7FFFFFFF) % values.length;
            offset = offset * 2 + 1;

            if (offset == -1) {
                offset = 2;
            }
        }

        if (values[index] != FREE) {
            values[index] = REMOVED;
            modCount++;
            elements--;
            return true;
        } else {
            return false;
        }
    }

    public void clear() {
        elements = 0;
        for (int ix = 0; ix < values.length; ix++) {
            values[ix] = FREE;
        }

        freeEntries = values.length;
        modCount++;
    }

    public long[] toArray() {
        long[] result = new long[elements];
        long[] values = Arrays.copyOf(this.values, this.values.length);
        int pos = 0;

        for (long value : values) {
            if (value != FREE && value != REMOVED) {
                result[pos++] = value;
            }
        }

        return result;
    }

    public long popFirst() {
        for (long value : values) {
            if (value != FREE && value != REMOVED) {
                remove(value);
                return value;
            }
        }

        return 0;
    }

    public long[] popAll() {
        long[] ret = toArray();
        clear();
        return ret;
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

    private void rehash() {
        int gargagecells = values.length - (elements + freeEntries);
        if (gargagecells / (double) values.length > 0.05) {
            rehash(values.length);
        } else {
            rehash(values.length * 2 + 1);
        }
    }

    private void rehash(int newCapacity) {
        long[] newValues = new long[newCapacity];

        for (long value : values) {
            if (value == FREE || value == REMOVED) {
                continue;
            }

            int hash = hash(value);
            int index = (hash & 0x7FFFFFFF) % newCapacity;
            int offset = 1;

            // search for the object
            while (newValues[index] != FREE) {
                index = ((index + offset) & 0x7FFFFFFF) % newCapacity;
                offset = offset * 2 + 1;

                if (offset == -1) {
                    offset = 2;
                }
            }

            newValues[index] = value;
        }

        values = newValues;
        freeEntries = values.length - elements;
    }

    private class Itr implements Iterator {
        private int index;
        private int lastReturned = -1;
        private int expectedModCount;

        public Itr() {
            for (index = 0; index < values.length && (values[index] == FREE || values[index] == REMOVED); index++) {
                // This is just to drive the index forward to the first valid entry
            }
            expectedModCount = modCount;
        }

        public boolean hasNext() {
            return index != values.length;
        }

        public Long next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            int length = values.length;
            if (index >= length) {
                lastReturned = -2;
                throw new NoSuchElementException();
            }

            lastReturned = index;
            for (index += 1; index < length && (values[index] == FREE || values[index] == REMOVED); index++) {
                // This is just to drive the index forward to the next valid entry
            }

            if (values[lastReturned] == FREE) {
                return FREE;
            } else {
                return values[lastReturned];
            }
        }

        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            if (lastReturned == -1 || lastReturned == -2) {
                throw new IllegalStateException();
            }

            if (values[lastReturned] != FREE && values[lastReturned] != REMOVED) {
                values[lastReturned] = REMOVED;
                elements--;
                modCount++;
                expectedModCount = modCount;
            }
        }
    }
}
