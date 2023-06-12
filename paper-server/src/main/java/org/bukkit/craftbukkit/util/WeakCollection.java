package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class WeakCollection<T> implements Collection<T> {
    static final Object NO_VALUE = new Object();
    private final Collection<WeakReference<T>> collection;

    public WeakCollection() {
        collection = new ArrayList<>();
    }

    @Override
    public boolean add(T value) {
        Preconditions.checkArgument(value != null, "Cannot add null value");
        return collection.add(new WeakReference<T>(value));
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        Collection<WeakReference<T>> values = this.collection;
        boolean ret = false;
        for (T value : collection) {
            Preconditions.checkArgument(value != null, "Cannot add null value");
            ret |= values.add(new WeakReference<T>(value));
        }
        return ret;
    }

    @Override
    public void clear() {
        collection.clear();
    }

    @Override
    public boolean contains(Object object) {
        if (object == null) {
            return false;
        }
        for (T compare : this) {
            if (object.equals(compare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return toCollection().containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return !iterator().hasNext();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Iterator<WeakReference<T>> it = collection.iterator();
            Object value = NO_VALUE;

            @Override
            public boolean hasNext() {
                Object value = this.value;
                if (value != null && value != NO_VALUE) {
                    return true;
                }

                Iterator<WeakReference<T>> it = this.it;
                value = null;

                while (it.hasNext()) {
                    WeakReference<T> ref = it.next();
                    value = ref.get();
                    if (value == null) {
                        it.remove();
                    } else {
                        this.value = value;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more elements");
                }

                @SuppressWarnings("unchecked")
                T value = (T) this.value;
                this.value = NO_VALUE;
                return value;
            }

            @Override
            public void remove() throws IllegalStateException {
                Preconditions.checkState(value == NO_VALUE, "No last element");

                value = null;
                it.remove();
            }
        };
    }

    @Override
    public boolean remove(Object object) {
        if (object == null) {
            return false;
        }

        Iterator<T> it = this.iterator();
        while (it.hasNext()) {
            if (object.equals(it.next())) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        Iterator<T> it = this.iterator();
        boolean ret = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                ret = true;
                it.remove();
            }
        }
        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        Iterator<T> it = this.iterator();
        boolean ret = false;
        while (it.hasNext()) {
            if (!collection.contains(it.next())) {
                ret = true;
                it.remove();
            }
        }
        return ret;
    }

    @Override
    public int size() {
        int s = 0;
        for (T value : this) {
            s++;
        }
        return s;
    }

    @Override
    public Object[] toArray() {
        return this.toArray(new Object[0]);
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return toCollection().toArray(array);
    }

    private Collection<T> toCollection() {
        ArrayList<T> collection = new ArrayList<T>();
        for (T value : this) {
            collection.add(value);
        }
        return collection;
    }
}
