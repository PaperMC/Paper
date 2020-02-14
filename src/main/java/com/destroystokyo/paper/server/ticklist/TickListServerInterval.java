package com.destroystokyo.paper.server.ticklist;

import com.destroystokyo.paper.util.set.LinkedSortedSet;
import net.minecraft.server.NextTickListEntry;
import net.minecraft.server.TickListPriority;
import java.util.Comparator;

// represents a set of entries to tick at a specified time
public final class TickListServerInterval<T> {

    public static final int TOTAL_PRIORITIES = TickListPriority.values().length;
    public static final Comparator<NextTickListEntry<?>> ENTRY_COMPARATOR_BY_ID = (entry1, entry2) -> {
        return Long.compare(entry1.getId(), entry2.getId());
    };
    public static final Comparator<NextTickListEntry<?>> ENTRY_COMPARATOR = (Comparator)NextTickListEntry.comparator();

    // we do not record the interval, this class is meant to be used on a ring buffer

    // inlined enum map for TickListPriority
    public final LinkedSortedSet<NextTickListEntry<T>>[] byPriority = new LinkedSortedSet[TOTAL_PRIORITIES];

    {
        for (int i = 0, len = this.byPriority.length; i < len; ++i) {
            this.byPriority[i] = new LinkedSortedSet<>(ENTRY_COMPARATOR_BY_ID);
        }
    }

    public void addEntryLast(final NextTickListEntry<T> entry) {
        this.byPriority[entry.getPriority().ordinal()].addLast(entry);
    }

    public void addEntryFirst(final NextTickListEntry<T> entry) {
        this.byPriority[entry.getPriority().ordinal()].addFirst(entry);
    }

    public void clear() {
        for (int i = 0, len = this.byPriority.length; i < len; ++i) {
            this.byPriority[i].clear(); // O(1) clear
        }
    }
}
