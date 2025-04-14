package org.bukkit.craftbukkit.util;

import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class WeakCollection<E> implements Collection<E> {
    static final Object NO_VALUE = new Object();
    private final Collection<WeakReference<E>> collection;

    public WeakCollection() {
        this.collection = new ArrayList<>();
    }

    @Override
    public boolean add(E value) {
        Preconditions.checkArgument(value != null, "Cannot add null value");
        return this.collection.add(new WeakReference<>(value));
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        Collection<WeakReference<E>> values = this.collection;
        boolean ret = false;
        for (E value : collection) {
            Preconditions.checkArgument(value != null, "Cannot add null value");
            ret |= values.add(new WeakReference<E>(value));
        }
        return ret;
    }

    @Override
    public void clear() {
        this.collection.clear();
    }

    @Override
    public boolean contains(Object object) {
        if (object == null) {
            return false;
        }
        for (E compare : this) {
            if (object.equals(compare)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return this.toCollection().containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            final Iterator<WeakReference<E>> it = WeakCollection.this.collection.iterator();
            Object value = WeakCollection.NO_VALUE;

            @Override
            public boolean hasNext() {
                Object value = this.value;
                if (value != null && value != WeakCollection.NO_VALUE) {
                    return true;
                }

                Iterator<WeakReference<E>> it = this.it;
                value = null;

                while (it.hasNext()) {
                    WeakReference<E> ref = it.next();
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
            public E next() throws NoSuchElementException {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("No more elements");
                }

                @SuppressWarnings("unchecked")
                E value = (E) this.value;
                this.value = WeakCollection.NO_VALUE;
                return value;
            }

            @Override
            public void remove() throws IllegalStateException {
                Preconditions.checkState(this.value == WeakCollection.NO_VALUE, "No last element");

                this.value = null;
                this.it.remove();
            }
        };
    }

    @Override
    public boolean remove(Object object) {
        if (object == null) {
            return false;
        }

        Iterator<E> it = this.iterator();
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
        Iterator<E> it = this.iterator();
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
        Iterator<E> it = this.iterator();
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
        for (E value : this) {
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
        return this.toCollection().toArray(array);
    }

    private Collection<E> toCollection() {
        ArrayList<E> collection = new ArrayList<>();
        for (E value : this) {
            collection.add(value);
        }
        return collection;
    }
}
