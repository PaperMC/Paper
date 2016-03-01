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

import co.aikar.util.LoadingMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>Used as a basis for fast HashMap key comparisons for the Timing Map.</p>
 *
 * This class uses interned strings giving us the ability to do an identity check instead of equals() on the strings
 */
final class TimingIdentifier {
    /**
     * Holds all groups. Autoloads on request for a group by name.
     */
    static final Map<String, TimingGroup> GROUP_MAP = LoadingMap.of(new ConcurrentHashMap<>(64, .5F), TimingGroup::new);
    private static final TimingGroup DEFAULT_GROUP = getGroup("Minecraft");
    final String group;
    final String name;
    final TimingHandler groupHandler;
    private final int hashCode;

    TimingIdentifier(@Nullable String group, @NotNull String name, @Nullable Timing groupHandler) {
        this.group = group != null ? group: DEFAULT_GROUP.name;
        this.name = name;
        this.groupHandler = groupHandler != null ? groupHandler.getTimingHandler() : null;
        this.hashCode = (31 * this.group.hashCode()) + this.name.hashCode();
    }

    @NotNull
    static TimingGroup getGroup(@Nullable String groupName) {
        if (groupName == null) {
            //noinspection ConstantConditions
            return DEFAULT_GROUP;
        }

        return GROUP_MAP.get(groupName);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        TimingIdentifier that = (TimingIdentifier) o;
        return Objects.equals(group, that.group) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "TimingIdentifier{id=" + group + ":" + name +'}';
    }

    static class TimingGroup {

        private static AtomicInteger idPool = new AtomicInteger(1);
        final int id = idPool.getAndIncrement();

        final String name;
        final List<TimingHandler> handlers = Collections.synchronizedList(new ArrayList<>(64));

        private TimingGroup(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TimingGroup that = (TimingGroup) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return id;
        }
    }
}
