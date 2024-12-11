package org.bukkit.craftbukkit.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public abstract class LazyHashSet<E> implements Set<E> {
    Set<E> reference = null;

    @Override
    public int size() {
        return this.getReference().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getReference().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.getReference().contains(o);
    }

    @Override
    public Iterator<E> iterator() {
        return this.getReference().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.getReference().toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.getReference().toArray(a);
    }

    @Override
    public boolean add(E o) {
        return this.getReference().add(o);
    }

    @Override
    public boolean remove(Object o) {
        return this.getReference().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.getReference().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return this.getReference().addAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.getReference().retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.getReference().removeAll(c);
    }

    @Override
    public void clear() {
        this.getReference().clear();
    }

    public Set<E> getReference() {
        Set<E> reference = this.reference;
        if (reference != null) {
            return reference;
        }
        return this.reference = this.makeReference();
    }

    protected abstract Set<E> makeReference(); // Paper - protected

    public boolean isLazy() {
        return this.reference == null;
    }

    @Override
    public int hashCode() {
        return 157 * this.getReference().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        LazyHashSet<?> that = (LazyHashSet<?>) obj;
        return (this.isLazy() && that.isLazy()) || this.getReference().equals(that.getReference());
    }

    @Override
    public String toString() {
        return this.getReference().toString();
    }
}
