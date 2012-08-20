package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

// implementation of an ArrayList that offers a getter without range checks
@SuppressWarnings("unchecked")
public class UnsafeList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 8683452581112892191L;

    private transient Object[] data;
    private int size;
    private int initialCapacity;

    private Iterator[] iterPool = new Iterator[1];
    private int maxPool;
    private int poolCounter;

    public UnsafeList(int capacity, int maxIterPool) {
        super();
        if (capacity < 0) capacity = 32;
        int rounded = Integer.highestOneBit(capacity - 1) << 1;
        data = new Object[rounded];
        initialCapacity = rounded;
        maxPool = maxIterPool;
        iterPool[0] = new Itr();
    }

    public UnsafeList(int capacity) {
        this(capacity, 5);
    }

    public UnsafeList() {
        this(32);
    }

    public E get(int index) {
        rangeCheck(index);

        return (E) data[index];
    }

    public E unsafeGet(int index) {
        return (E) data[index];
    }

    public E set(int index, E element) {
        rangeCheck(index);

        E old = (E) data[index];
        data[index] = element;
        return old;
    }

    public boolean add(E element) {
        growIfNeeded();
        data[size++] = element;
        return true;
    }

    public void add(int index, E element) {
        growIfNeeded();
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    public E remove(int index) {
        rangeCheck(index);

        E old = (E) data[index];
        int movedCount = size - index - 1;
        if (movedCount > 0) {
            System.arraycopy(data, index + 1, data, index, movedCount);
        }
        data[--size] = null;

        return old;
    }

    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }

        return false;
    }

    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == data[i] || o.equals(data[i])) {
                return i;
            }
        }

        return -1;
    }

    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    public void clear() {
        // Create new array to reset memory usage to initial capacity
        size = 0;

        // If array has grown too large create new one, otherwise just clear it
        if (data.length > initialCapacity << 3) {
            data = new Object[initialCapacity];
        } else {
            for (int i = 0; i < data.length; i++) {
                data[i] = null;
            }
        }
    }

    // actually rounds up to nearest power of two
    public void trimToSize() {
        int old = data.length;
        int rounded = Integer.highestOneBit(size - 1) << 1;
        if (rounded < old) {
            data = Java15Compat.Arrays_copyOf(data, rounded);
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object clone() throws CloneNotSupportedException {
        UnsafeList<E> copy = (UnsafeList<E>) super.clone();
        copy.data = Java15Compat.Arrays_copyOf(data, size);
        copy.size = size;
        copy.initialCapacity = initialCapacity;
        copy.iterPool = new Iterator[1];
        copy.iterPool[0] = new Itr();
        copy.maxPool = maxPool;
        copy.poolCounter = 0;
        return copy;
    }

    public Iterator<E> iterator() {
        // Try to find an iterator that isn't in use
        for (Iterator iter : iterPool) {
            if (!((Itr) iter).valid) {
                Itr iterator = (Itr) iter;
                iterator.reset();
                return iterator;
            }
        }

        // Couldn't find one, see if we can grow our pool size
        if (iterPool.length < maxPool) {
            Iterator[] newPool = new Iterator[iterPool.length + 1];
            System.arraycopy(iterPool, 0, newPool, 0, iterPool.length);
            iterPool = newPool;

            iterPool[iterPool.length - 1] = new Itr();
            return iterPool[iterPool.length - 1];
        }

        // Still couldn't find a free one, round robin replace one with a new iterator
        // This is done in the hope that the new one finishes so can be reused
        poolCounter = ++poolCounter % iterPool.length;
        iterPool[poolCounter] = new Itr();
        return iterPool[poolCounter];
    }

    private void rangeCheck(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void growIfNeeded() {
        if (size == data.length) {
            Object[] newData = new Object[data.length << 1];
            System.arraycopy(data, 0, newData, 0, size);
            data = newData;
        }
    }

    private void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();

        os.writeInt(size);
        os.writeInt(initialCapacity);
        for (int i = 0; i < size; i++) {
            os.writeObject(data[i]);
        }
        os.writeInt(maxPool);
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();

        size = is.readInt();
        initialCapacity = is.readInt();
        data = new Object[Integer.highestOneBit(size - 1) << 1];
        for (int i = 0; i < size; i++) {
            data[i] = is.readObject();
        }
        maxPool = is.readInt();
        iterPool = new Iterator[1];
        iterPool[0] = new Itr();
    }

    public class Itr implements Iterator<E> {
        int index;
        int lastRet = -1;
        int expectedModCount = modCount;
        public boolean valid = true;

        public void reset() {
            index = 0;
            lastRet = -1;
            expectedModCount = modCount;
            valid = true;
        }

        public boolean hasNext() {
            valid = index != size;
            return valid;
        }

        public E next() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            int i = index;
            if (i >= size) {
                throw new NoSuchElementException();
            }

            if (i >= data.length) {
                throw new ConcurrentModificationException();
            }

            index = i + 1;
            return (E) data[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }

            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            try {
                UnsafeList.this.remove(lastRet);
                index = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
