package io.papermc.paper.util;

import com.google.common.collect.ForwardingSet;
import java.util.Collection;
import java.util.Set;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class SizeLimitedSet<E> extends ForwardingSet<E> {

    private final Set<E> delegate;
    private final int maxSize;

    public SizeLimitedSet(final Set<E> delegate, final int maxSize) {
        this.delegate = delegate;
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(final E element) {
        if (this.size() >= this.maxSize) {
            return false;
        }
        return super.add(element);
    }

    @Override
    public boolean addAll(final Collection<? extends @Nullable E> collection) {
        if ((collection.size() + this.size()) >= this.maxSize) {
            return false;
        }
        boolean edited = false;

        for (final E element : collection) {
            edited |= super.add(element);
        }
        return edited;
    }

    @Override
    protected Set<E> delegate() {
        return this.delegate;
    }
}
