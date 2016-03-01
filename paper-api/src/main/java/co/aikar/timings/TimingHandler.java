/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import co.aikar.util.LoadingIntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TimingHandler implements Timing {

    private static AtomicInteger idPool = new AtomicInteger(1);
    private static Deque<TimingHandler> TIMING_STACK = new ArrayDeque<>();
    final int id = idPool.getAndIncrement();

    final TimingIdentifier identifier;
    private final boolean verbose;

    private final Int2ObjectOpenHashMap<TimingData> children = new LoadingIntMap<>(TimingData::new);

    final TimingData record;
    private TimingHandler startParent;
    private final TimingHandler groupHandler;

    private long start = 0;
    private int timingDepth = 0;
    private boolean added;
    private boolean timed;
    private boolean enabled;

    TimingHandler(@NotNull TimingIdentifier id) {
        this.identifier = id;
        this.verbose = id.name.startsWith("##");
        this.record = new TimingData(this.id);
        this.groupHandler = id.groupHandler;

        TimingIdentifier.getGroup(id.group).handlers.add(this);
        checkEnabled();
    }

    final void checkEnabled() {
        enabled = Timings.timingsEnabled && (!verbose || Timings.verboseEnabled);
    }

    void processTick(boolean violated) {
        if (timingDepth != 0 || record.getCurTickCount() == 0) {
            timingDepth = 0;
            start = 0;
            return;
        }

        record.processTick(violated);
        for (TimingData handler : children.values()) {
            handler.processTick(violated);
        }
    }

    @NotNull
    @Override
    public Timing startTimingIfSync() {
        startTiming();
        return this;
    }

    @Override
    public void stopTimingIfSync() {
        stopTiming();
    }

    @NotNull
    public Timing startTiming() {
        if (!enabled || !Bukkit.isPrimaryThread()) {
            return this;
        }
        if (++timingDepth == 1) {
            startParent = TIMING_STACK.peekLast();
            start = System.nanoTime();
        }
        TIMING_STACK.addLast(this);
        return this;
    }

    public void stopTiming() {
        if (!enabled || timingDepth <= 0 || start == 0 || !Bukkit.isPrimaryThread()) {
            return;
        }

        popTimingStack();
        if (--timingDepth == 0) {
            addDiff(System.nanoTime() - start, startParent);
            startParent = null;
            start = 0;
        }
    }

    private void popTimingStack() {
        TimingHandler last;
        while ((last = TIMING_STACK.removeLast()) != this) {
            last.timingDepth = 0;
            if ("Minecraft".equalsIgnoreCase(last.identifier.group)) {
                Logger.getGlobal().log(Level.SEVERE, "TIMING_STACK_CORRUPTION - Look above this for any errors and report this to Paper unless it has a plugin in the stack trace (" + last.identifier + " did not stopTiming)");
            } else {
                Logger.getGlobal().log(Level.SEVERE, "TIMING_STACK_CORRUPTION - Report this to the plugin " + last.identifier.group + " (Look for errors above this in the logs) (" + last.identifier + " did not stopTiming)", new Throwable());
            }

            boolean found = TIMING_STACK.contains(this);
            if (!found) {
                // We aren't even in the stack... Don't pop everything
                TIMING_STACK.addLast(last);
                break;
            }
        }
    }

    @Override
    public final void abort() {

    }

    void addDiff(long diff, @Nullable TimingHandler parent) {
        if (parent != null) {
            parent.children.get(id).add(diff);
        }

        record.add(diff);
        if (!added) {
            added = true;
            timed = true;
            TimingsManager.HANDLERS.add(this);
        }
        if (groupHandler != null) {
            groupHandler.addDiff(diff, parent);
            groupHandler.children.get(id).add(diff);
        }
    }

    /**
     * Reset this timer, setting all values to zero.
     */
    void reset(boolean full) {
        record.reset();
        if (full) {
            timed = false;
        }
        start = 0;
        timingDepth = 0;
        added = false;
        children.clear();
        checkEnabled();
    }

    @NotNull
    @Override
    public TimingHandler getTimingHandler() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * This is simply for the Closeable interface so it can be used with try-with-resources ()
     */
    @Override
    public void close() {
        stopTimingIfSync();
    }

    public boolean isSpecial() {
        return this == TimingsManager.FULL_SERVER_TICK || this == TimingsManager.TIMINGS_TICK;
    }

    boolean isTimed() {
        return timed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @NotNull
    TimingData[] cloneChildren() {
        final TimingData[] clonedChildren = new TimingData[children.size()];
        int i = 0;
        for (TimingData child : children.values()) {
            clonedChildren[i++] = child.clone();
        }
        return clonedChildren;
    }
}
