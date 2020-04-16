package net.minecraft.server;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArraySetSorted<T> extends AbstractSet<T> {

    private final Comparator<T> a;
    private T[] b; private final T[] getBackingArray() { return this.b; } // Paper - OBFHELPER
    private int c; private final int getSize() { return this.c; } private final void setSize(int value) { this.c = value; } // Paper - OBFHELPER

    private ArraySetSorted(int i, Comparator<T> comparator) {
        this.a = comparator;
        if (i < 0) {
            throw new IllegalArgumentException("Initial capacity (" + i + ") is negative");
        } else {
            this.b = a(new Object[i]);
        }
    }

    // Paper start - optimise removeIf
    @Override
    public boolean removeIf(java.util.function.Predicate<? super T> filter) {
        // prev. impl used an iterator, which could be n^2 and creates garbage
        int i = 0, len = this.getSize();
        T[] backingArray = this.getBackingArray();

        for (;;) {
            if (i >= len) {
                return false;
            }
            if (!filter.test(backingArray[i])) {
                ++i;
                continue;
            }
            break;
        }

        // we only want to write back to backingArray if we really need to

        int lastIndex = i; // this is where new elements are shifted to

        for (; i < len; ++i) {
            T curr = backingArray[i];
            if (!filter.test(curr)) { // if test throws we're screwed
                backingArray[lastIndex++] = curr;
            }
        }

        // cleanup end
        Arrays.fill(backingArray, lastIndex, len, null);
        this.setSize(lastIndex);
        return true;
    }
    // Paper end - optimise removeIf

    public static <T extends Comparable<T>> ArraySetSorted<T> a(int i) {
        return new ArraySetSorted<>(i, (Comparator)Comparator.naturalOrder()); // Paper - decompile fix
    }

    private static <T> T[] a(Object[] aobject) {
        return (T[])aobject; // Paper - decompile fix
    }

    private int c(T t0) {
        return Arrays.binarySearch(this.b, 0, this.c, t0, this.a);
    }

    private static int b(int i) {
        return -i - 1;
    }

    public boolean add(T t0) {
        int i = this.c(t0);

        if (i >= 0) {
            return false;
        } else {
            int j = b(i);

            this.a(t0, j);
            return true;
        }
    }

    private void c(int i) {
        if (i > this.b.length) {
            if (this.b != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
                i = (int) Math.max(Math.min((long) this.b.length + (long) (this.b.length >> 1), 2147483639L), (long) i);
            } else if (i < 10) {
                i = 10;
            }

            Object[] aobject = new Object[i];

            System.arraycopy(this.b, 0, aobject, 0, this.c);
            this.b = a(aobject);
        }
    }

    private void a(T t0, int i) {
        this.c(this.c + 1);
        if (i != this.c) {
            System.arraycopy(this.b, i, this.b, i + 1, this.c - i);
        }

        this.b[i] = t0;
        ++this.c;
    }

    private void d(int i) {
        --this.c;
        if (i != this.c) {
            System.arraycopy(this.b, i + 1, this.b, i, this.c - i);
        }

        this.b[this.c] = null;
    }

    private T e(int i) {
        return this.b[i];
    }

    public T a(T t0) {
        int i = this.c(t0);

        if (i >= 0) {
            return this.e(i);
        } else {
            this.a(t0, b(i));
            return t0;
        }
    }

    public boolean remove(Object object) {
        int i = this.c((T)object); // Paper - decompile fix

        if (i >= 0) {
            this.d(i);
            return true;
        } else {
            return false;
        }
    }

    public T b() {
        return this.e(0);
    }

    public boolean contains(Object object) {
        int i = this.c((T)object); // Paper - decompile fix

        return i >= 0;
    }

    public Iterator<T> iterator() {
        return new ArraySetSorted.a();
    }

    public int size() {
        return this.c;
    }

    public Object[] toArray() {
        return (Object[]) this.b.clone();
    }

    public <U> U[] toArray(U[] au) {
        if (au.length < this.c) {
            return (U[])Arrays.copyOf(this.b, this.c, au.getClass()); // Paper - decompile fix
        } else {
            System.arraycopy(this.b, 0, au, 0, this.c);
            if (au.length > this.c) {
                au[this.c] = null;
            }

            return au;
        }
    }

    public void clear() {
        Arrays.fill(this.b, 0, this.c, (Object) null);
        this.c = 0;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            if (object instanceof ArraySetSorted) {
                ArraySetSorted<?> arraysetsorted = (ArraySetSorted) object;

                if (this.a.equals(arraysetsorted.a)) {
                    return this.c == arraysetsorted.c && Arrays.equals(this.b, arraysetsorted.b);
                }
            }

            return super.equals(object);
        }
    }

    class a implements Iterator<T> {

        private int b;
        private int c;

        private a() {
            this.c = -1;
        }

        public boolean hasNext() {
            return this.b < ArraySetSorted.this.c;
        }

        public T next() {
            if (this.b >= ArraySetSorted.this.c) {
                throw new NoSuchElementException();
            } else {
                this.c = this.b++;
                return ArraySetSorted.this.b[this.c];
            }
        }

        public void remove() {
            if (this.c == -1) {
                throw new IllegalStateException();
            } else {
                ArraySetSorted.this.d(this.c);
                --this.b;
                this.c = -1;
            }
        }
    }
}
