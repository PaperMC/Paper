package com.destroystokyo.paper.util.maplist;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.shorts.Short2LongOpenHashMap;
import net.minecraft.server.ChunkSection;
import net.minecraft.server.DataPaletteGlobal;
import net.minecraft.server.IBlockData;
import java.util.Arrays;

/**
 * @author Spottedleaf
 */
public final class IBlockDataList {

    static final DataPaletteGlobal<IBlockData> GLOBAL_PALETTE = (DataPaletteGlobal)ChunkSection.GLOBAL_PALETTE;

    // map of location -> (index | (location << 16) | (palette id << 32))
    private final Short2LongOpenHashMap map = new Short2LongOpenHashMap(2, 0.8f);
    {
        this.map.defaultReturnValue(Long.MAX_VALUE);
    }

    private static final long[] EMPTY_LIST = new long[0];

    private long[] byIndex = EMPTY_LIST;
    private int size;

    public static int getLocationKey(final int x, final int y, final int z) {
        return (x & 15) | (((z & 15) << 4)) | ((y & 255) << (4 + 4));
    }

    public static IBlockData getBlockDataFromRaw(final long raw) {
        return GLOBAL_PALETTE.getObject((int)(raw >>> 32));
    }

    public static int getIndexFromRaw(final long raw) {
        return (int)(raw & 0xFFFF);
    }

    public static int getLocationFromRaw(final long raw) {
        return (int)((raw >>> 16) & 0xFFFF);
    }

    public static long getRawFromValues(final int index, final int location, final IBlockData data) {
        return (long)index | ((long)location << 16) | (((long)GLOBAL_PALETTE.getOrCreateIdFor(data)) << 32);
    }

    public static long setIndexRawValues(final long value, final int index) {
        return value & ~(0xFFFF) | (index);
    }

    public long add(final int x, final int y, final int z, final IBlockData data) {
        return this.add(getLocationKey(x, y, z), data);
    }

    public long add(final int location, final IBlockData data) {
        final long curr = this.map.get((short)location);

        if (curr == Long.MAX_VALUE) {
            final int index = this.size++;
            final long raw = getRawFromValues(index, location, data);
            this.map.put((short)location, raw);

            if (index >= this.byIndex.length) {
                this.byIndex = Arrays.copyOf(this.byIndex, (int)Math.max(4L, this.byIndex.length * 2L));
            }

            this.byIndex[index] = raw;
            return raw;
        } else {
            final int index = getIndexFromRaw(curr);
            final long raw = this.byIndex[index] = getRawFromValues(index, location, data);

            this.map.put((short)location, raw);

            return raw;
        }
    }

    public long remove(final int x, final int y, final int z) {
        return this.remove(getLocationKey(x, y, z));
    }

    public long remove(final int location) {
        final long ret = this.map.remove((short)location);
        final int index = getIndexFromRaw(ret);
        if (ret == Long.MAX_VALUE) {
            return ret;
        }

        // move the entry at the end to this index
        final int endIndex = --this.size;
        final long end = this.byIndex[endIndex];
        if (index != endIndex) {
            // not empty after this call
            this.map.put((short)getLocationFromRaw(end), setIndexRawValues(end, index));
        }
        this.byIndex[index] = end;
        this.byIndex[endIndex] = 0L;

        return ret;
    }

    public int size() {
        return this.size;
    }

    public long getRaw(final int index) {
        return this.byIndex[index];
    }

    public int getLocation(final int index) {
        return getLocationFromRaw(this.getRaw(index));
    }

    public IBlockData getData(final int index) {
        return getBlockDataFromRaw(this.getRaw(index));
    }

    public void clear() {
        this.size = 0;
        this.map.clear();
    }

    public LongIterator getRawIterator() {
        return this.map.values().iterator();
    }
}
