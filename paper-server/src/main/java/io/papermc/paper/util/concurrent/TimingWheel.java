package io.papermc.paper.util.concurrent;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/**
 * This class schedules tasks in ticks and executes them efficiently using a circular array (the wheel).
 * Each slot in the wheel represents a specific tick modulo the wheel size.
 * Tasks are placed into slots based on their target execution tick.
 * On each tick, the wheel checks the current slot and runs any tasks whose execute tick has been reached.
 *
 * O(1) task scheduling and retrieval within a single wheel rotation.
 * We are using power of 2 for faster operations than modulo.
 *
 */
public class TimingWheel<T extends TickBoundTask> implements Iterable<T> {
    private final int wheelSize;
    private final long mask;
    private final LinkedList<T>[] wheel;

    @SuppressWarnings("unchecked")
    public TimingWheel(int exponent) {
        this.wheelSize = 1 << exponent;
        this.mask = wheelSize - 1L;

        this.wheel = (LinkedList<T>[]) new LinkedList[wheelSize];
        for (int i = 0; i < wheelSize; i++) {
            wheel[i] = new LinkedList<>();
        }
    }

    public void add(T task, int currentTick) {
        long nextRun = task.getNextRun();

        if (nextRun <= currentTick) {
            nextRun = currentTick;
            task.setNextRun(nextRun);
        }

        int slot = (int) (nextRun & mask);
        wheel[slot].addLast(task);
    }

    public void addAll(Collection<? extends T> tasks, int currentTick) {
        for (T task : tasks) {
            this.add(task, currentTick);
        }
    }

    public @NotNull List<T> popValid(int currentTick) {
        int slot = (int) (currentTick & mask);
        LinkedList<T> bucket = wheel[slot];
        if (bucket.isEmpty()) return Collections.emptyList();

        Iterator<T> iter = bucket.iterator();
        List<T> list = new ArrayList<>();
        while (iter.hasNext()) {
            T task = iter.next();

            if (task.getNextRun() <= currentTick) {
                iter.remove();
                list.add(task);
            }
        }

        return list;
    }

    public boolean isReady(int currentTick) {
        int slot = (int) (currentTick & mask);
        LinkedList<T> bucket = wheel[slot];
        if (bucket.isEmpty()) return false;

        for (final T task : bucket) {
            if (task.getNextRun() <= currentTick) {
                return true;
            }
        }

        return false;
    }

    public void removeIf(Predicate<T> apply) {
        Iterator<T> itr = iterator();
        while (itr.hasNext()) {
            T next = itr.next();
            if (apply.test(next)) {
                itr.remove();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private class Itr implements Iterator<T> {
        private int index = 0;
        private Iterator<T> current = Collections.emptyIterator();
        private Iterator<T> lastIterator = null;

        @Override
        public boolean hasNext() {
            if (current.hasNext()) {
                return true;
            }

            for (int i = index; i < wheelSize; i++) {
                if (!wheel[i].isEmpty()) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public T next() {
            while (true) {
                if (current.hasNext()) {
                    lastIterator = current;
                    return current.next();
                }

                if (index >= wheelSize) {
                    throw new NoSuchElementException();
                }

                current = wheel[index++].iterator();
            }
        }

        @Override
        public void remove() {
            if (lastIterator == null) {
                throw new NoSuchElementException();
            }

            lastIterator.remove();
            lastIterator = null;
        }
    }


    @Override
    public @NotNull Iterator<T> iterator() {
        return new Itr();
    }
}
