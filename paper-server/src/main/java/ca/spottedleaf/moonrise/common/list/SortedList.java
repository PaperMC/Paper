package ca.spottedleaf.moonrise.common.list;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

public final class SortedList<E> {

    private static final Object[] EMPTY_LIST = new Object[0];

    private Comparator<? super E> comparator;
    private E[] elements;
    private int count;

    public SortedList(final Comparator<? super E> comparator) {
        this((E[])EMPTY_LIST, comparator);
    }

    public SortedList(final E[] elements, final Comparator<? super E> comparator) {
        this.elements = elements;
        this.comparator = comparator;
    }

    // start, end are inclusive
    private static <E> int insertIdx(final E[] elements, final E element, final Comparator<E> comparator,
                                     int start, int end) {
        while (start <= end) {
            final int middle = (start + end) >>> 1;

            final E middleVal = elements[middle];

            final int cmp = comparator.compare(element, middleVal);

            if (cmp < 0) {
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }

        return start;
    }

    public int size() {
        return this.count;
    }

    public boolean isEmpty() {
        return this.count == 0;
    }

    public int add(final E element) {
        E[] elements = this.elements;
        final int count = this.count;
        this.count = count + 1;
        final Comparator<? super E> comparator = this.comparator;

        final int idx = insertIdx(elements, element, comparator, 0, count - 1);

        if (count >= elements.length) {
            // copy and insert at the same time
            if (idx == count) {
                this.elements = elements = Arrays.copyOf(elements, (int)Math.max(4L, count * 2L)); // overflow results in negative
                elements[count] = element;
                return idx;
            } else {
                final E[] newElements = (E[])Array.newInstance(elements.getClass().getComponentType(), (int)Math.max(4L, count * 2L));
                System.arraycopy(elements, 0, newElements, 0, idx);
                newElements[idx] = element;
                System.arraycopy(elements, idx, newElements, idx + 1, count - idx);
                this.elements = newElements;
                return idx;
            }
        } else {
            if (idx == count) {
                // no copy needed
                elements[idx] = element;
                return idx;
            } else {
                // shift elements down
                System.arraycopy(elements, idx, elements, idx + 1, count - idx);
                elements[idx] = element;
                return idx;
            }
        }
    }

    public E get(final int idx) {
        if (idx < 0 || idx >= this.count) {
            throw new IndexOutOfBoundsException(idx);
        }
        return this.elements[idx];
    }


    public E remove(final E element) {
        E[] elements = this.elements;
        final int count = this.count;
        final Comparator<? super E> comparator = this.comparator;

        final int idx = Arrays.binarySearch(elements, 0, count, element, comparator);
        if (idx < 0) {
            return null;
        }

        final int last = this.count - 1;
        this.count = last;

        final E ret = elements[idx];

        System.arraycopy(elements, idx + 1, elements, idx, last - idx);

        elements[last] = null;

        return ret;
    }
}
