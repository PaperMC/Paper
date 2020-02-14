package com.destroystokyo.paper.util.set;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class LinkedSortedSet<E> implements Iterable<E> {

    public final Comparator<? super E> comparator;

    protected Link<E> head;
    protected Link<E> tail;

    public LinkedSortedSet() {
        this((Comparator)Comparator.naturalOrder());
    }

    public LinkedSortedSet(final Comparator<? super E> comparator) {
        this.comparator = comparator;
    }

    public void clear() {
        this.head = this.tail = null;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            Link<E> next = LinkedSortedSet.this.head;

            @Override
            public boolean hasNext() {
                return this.next != null;
            }

            @Override
            public E next() {
                final Link<E> next = this.next;
                if (next == null) {
                    throw new NoSuchElementException();
                }
                this.next = next.next;
                return next.element;
            }
        };
    }

    public boolean addLast(final E element) {
        final Comparator<? super E> comparator = this.comparator;

        Link<E> curr = this.tail;
        if (curr != null) {
            int compare;

            while ((compare = comparator.compare(element, curr.element)) < 0) {
                Link<E> prev = curr;
                curr = curr.prev;
                if (curr != null) {
                    continue;
                }
                this.head = prev.prev = new Link<>(element, null, prev);
                return true;
            }

            if (compare != 0) {
                // insert after curr
                final Link<E> next = curr.next;
                final Link<E> insert = new Link<>(element, curr, next);
                curr.next = insert;

                if (next == null) {
                    this.tail = insert;
                } else {
                    next.prev = insert;
                }
                return true;
            }

            return false;
        } else {
            this.head = this.tail = new Link<>(element);
            return true;
        }
    }

    public boolean addFirst(final E element) {
        final Comparator<? super E> comparator = this.comparator;

        Link<E> curr = this.head;
        if (curr != null) {
            int compare;

            while ((compare = comparator.compare(element, curr.element)) > 0) {
                Link<E> prev = curr;
                curr = curr.next;
                if (curr != null) {
                    continue;
                }
                this.tail = prev.next = new Link<>(element, prev, null);
                return true;
            }

            if (compare != 0) {
                // insert before curr
                final Link<E> prev = curr.prev;
                final Link<E> insert = new Link<>(element, prev, curr);
                curr.prev = insert;

                if (prev == null) {
                    this.head = insert;
                } else {
                    prev.next = insert;
                }
                return true;
            }

            return false;
        } else {
            this.head = this.tail = new Link<>(element);
            return true;
        }
    }

    protected static final class Link<E> {
        public E element;
        public Link<E> prev;
        public Link<E> next;

        public Link() {}

        public Link(final E element) {
            this.element = element;
        }

        public Link(final E element, final Link<E> prev, final Link<E> next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }
}
