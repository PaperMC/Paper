package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import java.util.Collections;
import java.util.Queue;
import java.util.LinkedList;
// CraftBukkit end

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerChunkMap {

    private static final Logger a = LogManager.getLogger();
    private final WorldServer world;
    private final List managedPlayers = new ArrayList();
    private final LongHashMap d = new LongHashMap();
    private final Queue e = new java.util.concurrent.ConcurrentLinkedQueue(); // CraftBukkit ArrayList -> ConcurrentLinkedQueue
    private final Queue f = new java.util.concurrent.ConcurrentLinkedQueue(); // CraftBukkit ArrayList -> ConcurrentLinkedQueue
    private int g;
    private long h;
    private final int[][] i = new int[][] { { 1, 0}, { 0, 1}, { -1, 0}, { 0, -1}};
    private boolean wasNotEmpty; // CraftBukkit - add field

    public PlayerChunkMap(WorldServer worldserver) {
        this.world = worldserver;
        this.a(worldserver.getMinecraftServer().getPlayerList().o());
    }

    public WorldServer a() {
        return this.world;
    }

    public void flush() {
        long i = this.world.getTime();
        int j;
        PlayerChunk playerchunk;

        if (i - this.h > 8000L) {
            this.h = i;

            // CraftBukkit start - Use iterator
            java.util.Iterator iterator = this.f.iterator();
            while (iterator.hasNext()) {
                playerchunk = (PlayerChunk) iterator.next();
                playerchunk.b();
                playerchunk.a();
            }
        } else {
            java.util.Iterator iterator = this.e.iterator();

            while (iterator.hasNext()) {
                playerchunk = (PlayerChunk) iterator.next();
                playerchunk.b();
                iterator.remove();
                // CraftBukkit end
            }
        }

        // this.e.clear(); // CraftBukkit - Removals are already covered
        if (this.managedPlayers.isEmpty()) {
            if (!wasNotEmpty) return; // CraftBukkit - Only do unload when we go from non-empty to empty
            WorldProvider worldprovider = this.world.worldProvider;

            if (!worldprovider.e()) {
                this.world.chunkProviderServer.b();
            }
            // CraftBukkit start
            wasNotEmpty = false;
        } else {
            wasNotEmpty = true;
        }
        // CraftBukkit end
    }

    public boolean a(int i, int j) {
        long k = (long) i + 2147483647L | (long) j + 2147483647L << 32;

        return this.d.getEntry(k) != null;
    }

    private PlayerChunk a(int i, int j, boolean flag) {
        long k = (long) i + 2147483647L | (long) j + 2147483647L << 32;
        PlayerChunk playerchunk = (PlayerChunk) this.d.getEntry(k);

        if (playerchunk == null && flag) {
            playerchunk = new PlayerChunk(this, i, j);
            this.d.put(k, playerchunk);
            this.f.add(playerchunk);
        }

        return playerchunk;
    }
    // CraftBukkit start - add method
    public final boolean isChunkInUse(int x, int z) {
        PlayerChunk pi = a(x, z, false);
        if (pi != null) {
            return (PlayerChunk.b(pi).size() > 0);
        }
        return false;
    }
    // CraftBukkit end

    public void flagDirty(int i, int j, int k) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerChunk playerchunk = this.a(l, i1, false);

        if (playerchunk != null) {
            playerchunk.a(i & 15, j, k & 15);
        }
    }

    public void addPlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;

        // CraftBukkit start - Load nearby chunks first
        List<ChunkCoordIntPair> chunkList = new LinkedList<ChunkCoordIntPair>();
        for (int k = i - this.g; k <= i + this.g; ++k) {
            for (int l = j - this.g; l <= j + this.g; ++l) {
                chunkList.add(new ChunkCoordIntPair(k, l));
            }
        }

        Collections.sort(chunkList, new ChunkCoordComparator(entityplayer));
        for (ChunkCoordIntPair pair : chunkList) {
            this.a(pair.x, pair.z, true).a(entityplayer);
        }
        // CraftBukkit end

        this.managedPlayers.add(entityplayer);
        this.b(entityplayer);
    }

    public void b(EntityPlayer entityplayer) {
        ArrayList arraylist = new ArrayList(entityplayer.chunkCoordIntPairQueue);
        int i = 0;
        int j = this.g;
        int k = (int) entityplayer.locX >> 4;
        int l = (int) entityplayer.locZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = PlayerChunk.a(this.a(k, l, true));

        entityplayer.chunkCoordIntPairQueue.clear();
        if (arraylist.contains(chunkcoordintpair)) {
            entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
        }

        int k1;

        for (k1 = 1; k1 <= j * 2; ++k1) {
            for (int l1 = 0; l1 < 2; ++l1) {
                int[] aint = this.i[i++ % 4];

                for (int i2 = 0; i2 < k1; ++i2) {
                    i1 += aint[0];
                    j1 += aint[1];
                    chunkcoordintpair = PlayerChunk.a(this.a(k + i1, l + j1, true));
                    if (arraylist.contains(chunkcoordintpair)) {
                        entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
                    }
                }
            }
        }

        i %= 4;

        for (k1 = 0; k1 < j * 2; ++k1) {
            i1 += this.i[i][0];
            j1 += this.i[i][1];
            chunkcoordintpair = PlayerChunk.a(this.a(k + i1, l + j1, true));
            if (arraylist.contains(chunkcoordintpair)) {
                entityplayer.chunkCoordIntPairQueue.add(chunkcoordintpair);
            }
        }
    }

    public void removePlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.d >> 4;
        int j = (int) entityplayer.e >> 4;

        for (int k = i - this.g; k <= i + this.g; ++k) {
            for (int l = j - this.g; l <= j + this.g; ++l) {
                PlayerChunk playerchunk = this.a(k, l, false);

                if (playerchunk != null) {
                    playerchunk.b(entityplayer);
                }
            }
        }

        this.managedPlayers.remove(entityplayer);
    }

    private boolean a(int i, int j, int k, int l, int i1) {
        int j1 = i - k;
        int k1 = j - l;

        return j1 >= -i1 && j1 <= i1 ? k1 >= -i1 && k1 <= i1 : false;
    }

    public void movePlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;
        double d0 = entityplayer.d - entityplayer.locX;
        double d1 = entityplayer.e - entityplayer.locZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D) {
            int k = (int) entityplayer.d >> 4;
            int l = (int) entityplayer.e >> 4;
            int i1 = this.g;
            int j1 = i - k;
            int k1 = j - l;
            List<ChunkCoordIntPair> chunksToLoad = new LinkedList<ChunkCoordIntPair>(); // CraftBukkit

            if (j1 != 0 || k1 != 0) {
                for (int l1 = i - i1; l1 <= i + i1; ++l1) {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2) {
                        if (!this.a(l1, i2, k, l, i1)) {
                            chunksToLoad.add(new ChunkCoordIntPair(l1, i2)); // CraftBukkit
                        }

                        if (!this.a(l1 - j1, i2 - k1, i, j, i1)) {
                            PlayerChunk playerchunk = this.a(l1 - j1, i2 - k1, false);

                            if (playerchunk != null) {
                                playerchunk.b(entityplayer);
                            }
                        }
                    }
                }

                this.b(entityplayer);
                entityplayer.d = entityplayer.locX;
                entityplayer.e = entityplayer.locZ;

                // CraftBukkit start - send nearest chunks first
                Collections.sort(chunksToLoad, new ChunkCoordComparator(entityplayer));
                for (ChunkCoordIntPair pair : chunksToLoad) {
                    this.a(pair.x, pair.z, true).a(entityplayer);
                }

                if (i1 > 1 || i1 < -1 || j1 > 1 || j1 < -1) {
                    Collections.sort(entityplayer.chunkCoordIntPairQueue, new ChunkCoordComparator(entityplayer));
                }
                // CraftBukkit end
            }
        }
    }

    public boolean a(EntityPlayer entityplayer, int i, int j) {
        PlayerChunk playerchunk = this.a(i, j, false);

        return playerchunk != null && PlayerChunk.b(playerchunk).contains(entityplayer) && !entityplayer.chunkCoordIntPairQueue.contains(PlayerChunk.a(playerchunk));
    }

    public void a(int i) {
        i = MathHelper.a(i, 3, 20);
        if (i != this.g) {
            int j = i - this.g;
            Iterator iterator = this.managedPlayers.iterator();

            while (iterator.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer) iterator.next();
                int k = (int) entityplayer.locX >> 4;
                int l = (int) entityplayer.locZ >> 4;
                int i1;
                int j1;

                if (j > 0) {
                    for (i1 = k - i; i1 <= k + i; ++i1) {
                        for (j1 = l - i; j1 <= l + i; ++j1) {
                            PlayerChunk playerchunk = this.a(i1, j1, true);

                            if (!PlayerChunk.b(playerchunk).contains(entityplayer)) {
                                playerchunk.a(entityplayer);
                            }
                        }
                    }
                } else {
                    for (i1 = k - this.g; i1 <= k + this.g; ++i1) {
                        for (j1 = l - this.g; j1 <= l + this.g; ++j1) {
                            if (!this.a(i1, j1, k, l, i)) {
                                this.a(i1, j1, true).b(entityplayer);
                            }
                        }
                    }
                }
            }

            this.g = i;
        }
    }

    public static int getFurthestViewableBlock(int i) {
        return i * 16 - 16;
    }

    static Logger c() {
        return a;
    }

    static WorldServer a(PlayerChunkMap playerchunkmap) {
        return playerchunkmap.world;
    }

    static LongHashMap b(PlayerChunkMap playerchunkmap) {
        return playerchunkmap.d;
    }

    static Queue c(PlayerChunkMap playermanager) { // CraftBukkit List -> Queue
        return playermanager.f;
    }

    static Queue d(PlayerChunkMap playermanager) { // CraftBukkit List -> Queue
        return playermanager.e;
    }

    // CraftBukkit start - Sorter to load nearby chunks first
    private static class ChunkCoordComparator implements java.util.Comparator<ChunkCoordIntPair> {
        private int x;
        private int z;

        public ChunkCoordComparator (EntityPlayer entityplayer) {
            x = (int) entityplayer.locX >> 4;
            z = (int) entityplayer.locZ >> 4;
        }

        public int compare(ChunkCoordIntPair a, ChunkCoordIntPair b) {
            if (a.equals(b)) {
                return 0;
            }

            // Subtract current position to set center point
            int ax = a.x - this.x;
            int az = a.z - this.z;
            int bx = b.x - this.x;
            int bz = b.z - this.z;

            int result = ((ax - bx) * (ax + bx)) + ((az - bz) * (az + bz));
            if (result != 0) {
                return result;
            }

            if (ax < 0) {
                if (bx < 0) {
                    return bz - az;
                } else {
                    return -1;
                }
            } else {
                if (bx < 0) {
                    return 1;
                } else {
                    return az - bz;
                }
            }
        }
    }
    // CraftBukkit end
}
