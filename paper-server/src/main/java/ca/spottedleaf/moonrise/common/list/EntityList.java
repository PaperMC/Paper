package ca.spottedleaf.moonrise.common.list;

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.world.entity.Entity;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

// list with O(1) remove & contains

/**
 * @author Spottedleaf
 */
public final class EntityList implements Iterable<Entity> {

    private final Int2IntOpenHashMap entityToIndex = new Int2IntOpenHashMap(2, 0.8f);
    {
        this.entityToIndex.defaultReturnValue(Integer.MIN_VALUE);
    }

    private static final Entity[] EMPTY_LIST = new Entity[0];

    private Entity[] entities = EMPTY_LIST;
    private int count;

    public int size() {
        return this.count;
    }

    public boolean contains(final Entity entity) {
        return this.entityToIndex.containsKey(entity.getId());
    }

    public boolean remove(final Entity entity) {
        final int index = this.entityToIndex.remove(entity.getId());
        if (index == Integer.MIN_VALUE) {
            return false;
        }

        // move the entity at the end to this index
        final int endIndex = --this.count;
        final Entity end = this.entities[endIndex];
        if (index != endIndex) {
            // not empty after this call
            this.entityToIndex.put(end.getId(), index); // update index
        }
        this.entities[index] = end;
        this.entities[endIndex] = null;

        return true;
    }

    public boolean add(final Entity entity) {
        final int count = this.count;
        final int currIndex = this.entityToIndex.putIfAbsent(entity.getId(), count);

        if (currIndex != Integer.MIN_VALUE) {
            return false; // already in this list
        }

        Entity[] list = this.entities;

        if (list.length == count) {
            // resize required
            list = this.entities = Arrays.copyOf(list, (int)Math.max(4L, count * 2L)); // overflow results in negative
        }

        list[count] = entity;
        this.count = count + 1;

        return true;
    }

    public Entity getChecked(final int index) {
        if (index < 0 || index >= this.count) {
            throw new IndexOutOfBoundsException("Index: " + index + " is out of bounds, size: " + this.count);
        }
        return this.entities[index];
    }

    public Entity getUnchecked(final int index) {
        return this.entities[index];
    }

    public Entity[] getRawData() {
        return this.entities;
    }

    public void clear() {
        this.entityToIndex.clear();
        Arrays.fill(this.entities, 0, this.count, null);
        this.count = 0;
    }

    @Override
    public Iterator<Entity> iterator() {
        return new Iterator<>() {
            private Entity lastRet;
            private int current;

            @Override
            public boolean hasNext() {
                return this.current < EntityList.this.count;
            }

            @Override
            public Entity next() {
                if (this.current >= EntityList.this.count) {
                    throw new NoSuchElementException();
                }
                return this.lastRet = EntityList.this.entities[this.current++];
            }

            @Override
            public void remove() {
                final Entity lastRet = this.lastRet;

                if (lastRet == null) {
                    throw new IllegalStateException();
                }
                this.lastRet = null;

                EntityList.this.remove(lastRet);
                --this.current;
            }
        };
    }
}
