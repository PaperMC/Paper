package io.papermc.paper.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Modified version of the Guava class with the same name to support add operations.
 *
 * @param <F> backing list element type
 * @param <T> transformed list element type
 */
@NullMarked
@ApiStatus.Internal
public final class TransformingRandomAccessList<F, T> extends AbstractList<T> implements RandomAccess {

    final List<F> fromList;
    final Function<? super F, ? extends T> toFunction;
    final Function<? super T, ? extends F> fromFunction;

    /**
     * Create a new {@link TransformingRandomAccessList}.
     *
     * @param fromList backing list
     * @param toFunction function mapping backing list element type to transformed list element type
     * @param fromFunction function mapping transformed list element type to backing list element type
     */
    public TransformingRandomAccessList(
        final List<F> fromList,
        final Function<? super F, ? extends T> toFunction,
        final Function<? super T, ? extends F> fromFunction
    ) {
        this.fromList = checkNotNull(fromList);
        this.toFunction = checkNotNull(toFunction);
        this.fromFunction = checkNotNull(fromFunction);
    }

    @Override
    public void clear() {
        this.fromList.clear();
    }

    @Override
    public T get(final int index) {
        return this.toFunction.apply(this.fromList.get(index));
    }

    @Override
    public Iterator<T> iterator() {
        return this.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return new TransformedListIterator<>(this.fromList.listIterator(index)) {
            @Override
            T transform(final F from) {
                return TransformingRandomAccessList.this.toFunction.apply(from);
            }

            @Override
            F transformBack(final T from) {
                return TransformingRandomAccessList.this.fromFunction.apply(from);
            }
        };
    }

    @Override
    public boolean isEmpty() {
        return this.fromList.isEmpty();
    }

    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        checkNotNull(filter);
        return this.fromList.removeIf(element -> filter.test(this.toFunction.apply(element)));
    }

    @Override
    public T remove(final int index) {
        return this.toFunction.apply(this.fromList.remove(index));
    }

    @Override
    public int size() {
        return this.fromList.size();
    }

    @Override
    public T set(final int i, final T t) {
        return this.toFunction.apply(this.fromList.set(i, this.fromFunction.apply(t)));
    }

    @Override
    public void add(final int i, final T t) {
        this.fromList.add(i, this.fromFunction.apply(t));
    }

    abstract static class TransformedListIterator<F, T> implements ListIterator<T>, Iterator<T> {

        final Iterator<F> backingIterator;

        TransformedListIterator(final ListIterator<F> backingIterator) {
            this.backingIterator = checkNotNull((Iterator<F>) backingIterator);
        }

        private ListIterator<F> backingIterator() {
            return cast(this.backingIterator);
        }

        static <A> ListIterator<A> cast(final Iterator<A> iterator) {
            return (ListIterator<A>) iterator;
        }

        @Override
        public final boolean hasPrevious() {
            return this.backingIterator().hasPrevious();
        }

        @Override
        public final T previous() {
            return this.transform(this.backingIterator().previous());
        }

        @Override
        public final int nextIndex() {
            return this.backingIterator().nextIndex();
        }

        @Override
        public final int previousIndex() {
            return this.backingIterator().previousIndex();
        }

        @Override
        public void set(final T element) {
            this.backingIterator().set(this.transformBack(element));
        }

        @Override
        public void add(final T element) {
            this.backingIterator().add(this.transformBack(element));
        }

        abstract T transform(F from);

        abstract F transformBack(T to);

        @Override
        public final boolean hasNext() {
            return this.backingIterator.hasNext();
        }

        @Override
        public final T next() {
            return this.transform(this.backingIterator.next());
        }

        @Override
        public final void remove() {
            this.backingIterator.remove();
        }
    }
}
