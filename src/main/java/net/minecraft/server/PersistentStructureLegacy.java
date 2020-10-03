package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nullable;

public class PersistentStructureLegacy {

    private static final Map<String, String> a = (Map) SystemUtils.a(Maps.newHashMap(), (hashmap) -> { // CraftBukkit - decompile error
        hashmap.put("Village", "Village");
        hashmap.put("Mineshaft", "Mineshaft");
        hashmap.put("Mansion", "Mansion");
        hashmap.put("Igloo", "Temple");
        hashmap.put("Desert_Pyramid", "Temple");
        hashmap.put("Jungle_Pyramid", "Temple");
        hashmap.put("Swamp_Hut", "Temple");
        hashmap.put("Stronghold", "Stronghold");
        hashmap.put("Monument", "Monument");
        hashmap.put("Fortress", "Fortress");
        hashmap.put("EndCity", "EndCity");
    });
    private static final Map<String, String> b = (Map) SystemUtils.a(Maps.newHashMap(), (hashmap) -> { // CraftBukkit - decompile error
        hashmap.put("Iglu", "Igloo");
        hashmap.put("TeDP", "Desert_Pyramid");
        hashmap.put("TeJP", "Jungle_Pyramid");
        hashmap.put("TeSH", "Swamp_Hut");
    });
    private final boolean c;
    private final Map<String, Long2ObjectMap<NBTTagCompound>> d = Maps.newHashMap();
    private final Map<String, PersistentIndexed> e = Maps.newHashMap();
    private final List<String> f;
    private final List<String> g;

    public PersistentStructureLegacy(@Nullable WorldPersistentData worldpersistentdata, List<String> list, List<String> list1) {
        this.f = list;
        this.g = list1;
        this.a(worldpersistentdata);
        boolean flag = false;

        String s;

        for (Iterator iterator = this.g.iterator(); iterator.hasNext(); flag |= this.d.get(s) != null) {
            s = (String) iterator.next();
        }

        this.c = flag;
    }

    public void a(long i) {
        Iterator iterator = this.f.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            PersistentIndexed persistentindexed = (PersistentIndexed) this.e.get(s);

            if (persistentindexed != null && persistentindexed.c(i)) {
                persistentindexed.d(i);
                persistentindexed.b();
            }
        }

    }

    public NBTTagCompound a(NBTTagCompound nbttagcompound) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Level");
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(nbttagcompound1.getInt("xPos"), nbttagcompound1.getInt("zPos"));

        if (this.a(chunkcoordintpair.x, chunkcoordintpair.z)) {
            nbttagcompound = this.a(nbttagcompound, chunkcoordintpair);
        }

        NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompound("Structures");
        NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompound("References");
        Iterator iterator = this.g.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            StructureGenerator<?> structuregenerator = (StructureGenerator) StructureGenerator.a.get(s.toLowerCase(Locale.ROOT));

            if (!nbttagcompound3.hasKeyOfType(s, 12) && structuregenerator != null) {
                boolean flag = true;
                LongArrayList longarraylist = new LongArrayList();

                for (int i = chunkcoordintpair.x - 8; i <= chunkcoordintpair.x + 8; ++i) {
                    for (int j = chunkcoordintpair.z - 8; j <= chunkcoordintpair.z + 8; ++j) {
                        if (this.a(i, j, s)) {
                            longarraylist.add(ChunkCoordIntPair.pair(i, j));
                        }
                    }
                }

                nbttagcompound3.c(s, (List) longarraylist);
            }
        }

        nbttagcompound2.set("References", nbttagcompound3);
        nbttagcompound1.set("Structures", nbttagcompound2);
        nbttagcompound.set("Level", nbttagcompound1);
        return nbttagcompound;
    }

    private boolean a(int i, int j, String s) {
        return !this.c ? false : this.d.get(s) != null && ((PersistentIndexed) this.e.get(PersistentStructureLegacy.a.get(s))).b(ChunkCoordIntPair.pair(i, j));
    }

    private boolean a(int i, int j) {
        if (!this.c) {
            return false;
        } else {
            Iterator iterator = this.g.iterator();

            String s;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                s = (String) iterator.next();
            } while (this.d.get(s) == null || !((PersistentIndexed) this.e.get(PersistentStructureLegacy.a.get(s))).c(ChunkCoordIntPair.pair(i, j)));

            return true;
        }
    }

    private NBTTagCompound a(NBTTagCompound nbttagcompound, ChunkCoordIntPair chunkcoordintpair) {
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Level");
        NBTTagCompound nbttagcompound2 = nbttagcompound1.getCompound("Structures");
        NBTTagCompound nbttagcompound3 = nbttagcompound2.getCompound("Starts");
        Iterator iterator = this.g.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            Long2ObjectMap<NBTTagCompound> long2objectmap = (Long2ObjectMap) this.d.get(s);

            if (long2objectmap != null) {
                long i = chunkcoordintpair.pair();

                if (((PersistentIndexed) this.e.get(PersistentStructureLegacy.a.get(s))).c(i)) {
                    NBTTagCompound nbttagcompound4 = (NBTTagCompound) long2objectmap.get(i);

                    if (nbttagcompound4 != null) {
                        nbttagcompound3.set(s, nbttagcompound4);
                    }
                }
            }
        }

        nbttagcompound2.set("Starts", nbttagcompound3);
        nbttagcompound1.set("Structures", nbttagcompound2);
        nbttagcompound.set("Level", nbttagcompound1);
        return nbttagcompound;
    }

    private void a(@Nullable WorldPersistentData worldpersistentdata) {
        if (worldpersistentdata != null) {
            Iterator iterator = this.f.iterator();

            while (iterator.hasNext()) {
                String s = (String) iterator.next();
                NBTTagCompound nbttagcompound = new NBTTagCompound();

                try {
                    nbttagcompound = worldpersistentdata.a(s, 1493).getCompound("data").getCompound("Features");
                    if (nbttagcompound.isEmpty()) {
                        continue;
                    }
                } catch (IOException ioexception) {
                    ;
                }

                Iterator iterator1 = nbttagcompound.getKeys().iterator();

                while (iterator1.hasNext()) {
                    String s1 = (String) iterator1.next();
                    NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound(s1);
                    long i = ChunkCoordIntPair.pair(nbttagcompound1.getInt("ChunkX"), nbttagcompound1.getInt("ChunkZ"));
                    NBTTagList nbttaglist = nbttagcompound1.getList("Children", 10);
                    String s2;

                    if (!nbttaglist.isEmpty()) {
                        s2 = nbttaglist.getCompound(0).getString("id");
                        String s3 = (String) PersistentStructureLegacy.b.get(s2);

                        if (s3 != null) {
                            nbttagcompound1.setString("id", s3);
                        }
                    }

                    s2 = nbttagcompound1.getString("id");
                    ((Long2ObjectMap) this.d.computeIfAbsent(s2, (s4) -> {
                        return new Long2ObjectOpenHashMap();
                    })).put(i, nbttagcompound1);
                }

                String s4 = s + "_index";
                PersistentIndexed persistentindexed = (PersistentIndexed) worldpersistentdata.a(() -> {
                    return new PersistentIndexed(s4);
                }, s4);

                if (!persistentindexed.a().isEmpty()) {
                    this.e.put(s, persistentindexed);
                } else {
                    PersistentIndexed persistentindexed1 = new PersistentIndexed(s4);

                    this.e.put(s, persistentindexed1);
                    Iterator iterator2 = nbttagcompound.getKeys().iterator();

                    while (iterator2.hasNext()) {
                        String s5 = (String) iterator2.next();
                        NBTTagCompound nbttagcompound2 = nbttagcompound.getCompound(s5);

                        persistentindexed1.a(ChunkCoordIntPair.pair(nbttagcompound2.getInt("ChunkX"), nbttagcompound2.getInt("ChunkZ")));
                    }

                    persistentindexed1.b();
                }
            }

        }
    }

    public static PersistentStructureLegacy a(ResourceKey<DimensionManager> resourcekey, @Nullable WorldPersistentData worldpersistentdata) { // CraftBukkit
        if (resourcekey == DimensionManager.OVERWORLD) { // CraftBukkit
            return new PersistentStructureLegacy(worldpersistentdata, ImmutableList.of("Monument", "Stronghold", "Village", "Mineshaft", "Temple", "Mansion"), ImmutableList.of("Village", "Mineshaft", "Mansion", "Igloo", "Desert_Pyramid", "Jungle_Pyramid", "Swamp_Hut", "Stronghold", "Monument"));
        } else {
            ImmutableList immutablelist;

            if (resourcekey == DimensionManager.THE_NETHER) { // CraftBukkit
                immutablelist = ImmutableList.of("Fortress");
                return new PersistentStructureLegacy(worldpersistentdata, immutablelist, immutablelist);
            } else if (resourcekey == DimensionManager.THE_END) { // CraftBukkit
                immutablelist = ImmutableList.of("EndCity");
                return new PersistentStructureLegacy(worldpersistentdata, immutablelist, immutablelist);
            } else {
                throw new RuntimeException(String.format("Unknown dimension type : %s", resourcekey));
            }
        }
    }
}
