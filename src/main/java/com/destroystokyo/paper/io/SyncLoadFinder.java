package com.destroystokyo.paper.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.longs.Long2IntMap;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.server.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SyncLoadFinder {

    public static final boolean ENABLED = Boolean.getBoolean("paper.debug-sync-loads");

    private static final WeakHashMap<World, Object2ObjectOpenHashMap<ThrowableWithEquals, SyncLoadInformation>> SYNC_LOADS = new WeakHashMap<>();

    private static final class SyncLoadInformation {

        public int times;

        public final Long2IntOpenHashMap coordinateTimes = new Long2IntOpenHashMap();
    }

    public static void logSyncLoad(final World world, final int chunkX, final int chunkZ) {
        if (!ENABLED) {
            return;
        }

        final ThrowableWithEquals stacktrace = new ThrowableWithEquals(Thread.currentThread().getStackTrace());

        SYNC_LOADS.compute(world, (final World keyInMap, Object2ObjectOpenHashMap<ThrowableWithEquals, SyncLoadInformation> map) -> {
            if (map == null) {
                map = new Object2ObjectOpenHashMap<>();
            }

            map.compute(stacktrace, (ThrowableWithEquals keyInMap0, SyncLoadInformation valueInMap) -> {
                if (valueInMap == null) {
                    valueInMap = new SyncLoadInformation();
                }

                ++valueInMap.times;

                valueInMap.coordinateTimes.compute(IOUtil.getCoordinateKey(chunkX, chunkZ), (Long keyInMap1, Integer valueInMap1) -> {
                    return valueInMap1 == null ? Integer.valueOf(1) : Integer.valueOf(valueInMap1.intValue() + 1);
                });

                return valueInMap;
            });

            return map;
        });
    }

    public static JsonObject serialize() {
        final JsonObject ret = new JsonObject();

        final JsonArray worldsData = new JsonArray();

        for (final Map.Entry<World, Object2ObjectOpenHashMap<ThrowableWithEquals, SyncLoadInformation>> entry : SYNC_LOADS.entrySet()) {
            final World world = entry.getKey();

            final JsonObject worldData = new JsonObject();

            worldData.addProperty("name", world.getWorld().getName());

            final List<Pair<ThrowableWithEquals, SyncLoadInformation>> data = new ArrayList<>();

            entry.getValue().forEach((ThrowableWithEquals stacktrace, SyncLoadInformation times) -> {
                data.add(new Pair<>(stacktrace, times));
            });

            data.sort((Pair<ThrowableWithEquals, SyncLoadInformation> pair1, Pair<ThrowableWithEquals, SyncLoadInformation> pair2) -> {
                return Integer.compare(pair2.getSecond().times, pair1.getSecond().times); // reverse order
            });

            final JsonArray stacktraces = new JsonArray();

            for (Pair<ThrowableWithEquals, SyncLoadInformation> pair : data) {
                final JsonObject stacktrace = new JsonObject();

                stacktrace.addProperty("times", pair.getSecond().times);

                final JsonArray traces = new JsonArray();

                for (StackTraceElement element : pair.getFirst().stacktrace) {
                    traces.add(String.valueOf(element));
                }

                stacktrace.add("stacktrace", traces);

                final JsonArray coordinates = new JsonArray();

                for (Long2IntMap.Entry coordinate : pair.getSecond().coordinateTimes.long2IntEntrySet()) {
                    final long key = coordinate.getLongKey();
                    final int times = coordinate.getIntValue();
                    coordinates.add("(" + IOUtil.getCoordinateX(key) + "," + IOUtil.getCoordinateZ(key) + "): " + times);
                }

                stacktrace.add("coordinates", coordinates);

                stacktraces.add(stacktrace);
            }


            worldData.add("stacktraces", stacktraces);
            worldsData.add(worldData);
        }

        ret.add("worlds", worldsData);

        return ret;
    }

    static final class ThrowableWithEquals {

        private final StackTraceElement[] stacktrace;
        private final int hash;

        public ThrowableWithEquals(final StackTraceElement[] stacktrace) {
            this.stacktrace = stacktrace;
            this.hash = ThrowableWithEquals.hash(stacktrace);
        }

        public static int hash(final StackTraceElement[] stacktrace) {
            int hash = 0;

            for (int i = 0; i < stacktrace.length; ++i) {
                hash *= 31;
                hash += stacktrace[i].hashCode();
            }

            return hash;
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }

            final ThrowableWithEquals other = (ThrowableWithEquals)obj;
            final StackTraceElement[] otherStackTrace = other.stacktrace;

            if (this.stacktrace.length != otherStackTrace.length || this.hash != other.hash) {
                return false;
            }

            if (this == obj) {
                return true;
            }

            for (int i = 0; i < this.stacktrace.length; ++i) {
                if (!this.stacktrace[i].equals(otherStackTrace[i])) {
                    return false;
                }
            }

            return true;
        }
    }
}
