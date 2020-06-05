package net.minecraft.server;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class LightEngineStorageBlock extends LightEngineStorage<LightEngineStorageBlock.a> {

    protected LightEngineStorageBlock(ILightAccess ilightaccess) {
        super(EnumSkyBlock.BLOCK, ilightaccess, new LightEngineStorageBlock.a(new com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<>(), false)); // Paper - avoid copying light data
    }

    @Override
    protected int d(long i) {
        // Paper start
        int baseX = (int) (i >> 38);
        int baseY = (int) ((i << 52) >> 52);
        int baseZ = (int) ((i << 26) >> 38);
        long j = (((long) (baseX >> 4) & 4194303L) << 42) | (((long) (baseY >> 4) & 1048575L)) | (((long) (baseZ >> 4) & 4194303L) << 20);
        NibbleArray nibblearray = this.e_visible.lookup.apply(j);
        return nibblearray == null ? 0 : nibblearray.a(baseX & 15, baseY & 15, baseZ & 15);
        // Paper end
    }

    public static final class a extends LightEngineStorageArray<LightEngineStorageBlock.a> {

        public a(com.destroystokyo.paper.util.map.QueuedChangesMapLong2Object<NibbleArray> long2objectopenhashmap, boolean isVisible) { // Paper - avoid copying light data
            super(long2objectopenhashmap, isVisible); // Paper - avoid copying light data
        }

        @Override
        public LightEngineStorageBlock.a b() {
            return new a(this.data, true); // Paper - avoid copying light data
        }
    }
}
