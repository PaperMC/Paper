package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

// implementation of an ArrayList that offers a getter without range checks
public class UnsafeList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 8683452581112892190L;
    private transient Object[] data;
    private int size;
    private int initialCapacity;

    public UnsafeList(int capacity) {
        super();
        if (capacity < 0) capacity = 128;
        int rounded = Integer.highestOneBit(capacity - 1) << 1;
        data = new Object[rounded];
        initialCapacity = rounded;
    }

    public UnsafeList() {
        this(128);
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
        data = new Object[initialCapacity];
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
        return size != 0;
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
    }

    private void readObject(ObjectInputStream is) throws IOException, ClassNotFoundException {
        is.defaultReadObject();

        size = is.readInt();
        initialCapacity = is.readInt();
        data = new Object[Integer.highestOneBit(size - 1) << 1];
        for (int i = 0; i < size; i++) {
            data[i] = is.readObject();
        }
    }

    public UnsafeList<E> clone() {
        try {
            UnsafeList<E> copy = (UnsafeList<E>) super.clone();
            copy.data = Java15Compat.Arrays_copyOf(data, size);
            copy.size = size;
            copy.initialCapacity = initialCapacity;
            return copy;
        } catch (CloneNotSupportedException ex) {
            // This should never happen
            return null;
        }
    }
}
