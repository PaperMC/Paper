package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    public List managedPlayers = new ArrayList();
    private PlayerList b = new PlayerList();
    private List c = new ArrayList();
    private MinecraftServer server;
    private int e;
    private int f;
    private final int[][] g = new int[][] { { 1, 0}, { 0, 1}, { -1, 0}, { 0, -1}};

    public PlayerManager(MinecraftServer minecraftserver, int i, int j) {
        // CraftBukkit start - no longer need to track view distance here, defers to the player.
        this.server = minecraftserver;
        this.e = i;
        // CraftBukkit end
    }

    public WorldServer a() {
        return this.server.getWorldServer(this.e);
    }

    public void flush() {
        for (int i = 0; i < this.c.size(); ++i) {
            ((PlayerInstance) this.c.get(i)).a();
        }

        this.c.clear();
    }

    private PlayerInstance a(int i, int j, boolean flag) {
        long k = (long) i + 2147483647L | (long) j + 2147483647L << 32;
        PlayerInstance playerinstance = (PlayerInstance) this.b.a(k);

        if (playerinstance == null && flag) {
            playerinstance = new PlayerInstance(this, i, j);
            this.b.a(k, playerinstance);
        }

        return playerinstance;
    }

    public void flagDirty(int i, int j, int k) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerInstance playerinstance = this.a(l, i1, false);

        if (playerinstance != null) {
            playerinstance.a(i & 15, j, k & 15);
        }
    }

    public void addPlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;
        int k = 0;
        int l = entityplayer.getViewDistance(); // CraftBukkit - use per-player view distance rather than this.f;
        int i1 = 0;
        int j1 = 0;

        this.a(i, j, true).a(entityplayer);

        int k1;

        for (k1 = 1; k1 <= l * 2; ++k1) {
            for (int l1 = 0; l1 < 2; ++l1) {
                int[] aint = this.g[k++ % 4];

                for (int i2 = 0; i2 < k1; ++i2) {
                    i1 += aint[0];
                    j1 += aint[1];
                    this.a(i + i1, j + j1, true).a(entityplayer);
                }
            }
        }

        k %= 4;

        for (k1 = 0; k1 < l * 2; ++k1) {
            i1 += this.g[k][0];
            j1 += this.g[k][1];
            this.a(i + i1, j + j1, true).a(entityplayer);
        }

        this.managedPlayers.add(entityplayer);
    }

    public void removePlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.d >> 4;
        int j = (int) entityplayer.e >> 4;

        // CraftBukkit start - use per-player view distance instead of this.f
        int viewDistance = entityplayer.getViewDistance();
        for (int k = i - viewDistance; k <= i + viewDistance; ++k) {
            for (int l = j - viewDistance; l <= j + viewDistance; ++l) {
                // CraftBukkit end
                PlayerInstance playerinstance = this.a(k, l, false);

                if (playerinstance != null) {
                    playerinstance.b(entityplayer);
                }
            }
        }

        this.managedPlayers.remove(entityplayer);
    }

    // CraftBukkit start - changed signature to take a reference to a player. Allows for per-player view distance checks
    private boolean a(int viewDistance, int i, int j, int k, int l) {
        int i1 = i - k;
        int j1 = j - l;

        return i1 >= -viewDistance && i1 <= viewDistance ? j1 >= -viewDistance && j1 <= viewDistance : false; // CraftBukkit - use per-player view distance
    }
    // CraftBukkit end

    public void movePlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;
        double d0 = entityplayer.d - entityplayer.locX;
        double d1 = entityplayer.e - entityplayer.locZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D) {
            int k = (int) entityplayer.d >> 4;
            int l = (int) entityplayer.e >> 4;
            int i1 = i - k;
            int j1 = j - l;

            if (i1 != 0 || j1 != 0) {
                // CraftBukkit start - use per-player view distance instead of this.f
                int viewDistance = entityplayer.getViewDistance();
                for (int k1 = i - viewDistance; k1 <= i + viewDistance; ++k1) {
                    for (int l1 = j - viewDistance; l1 <= j + viewDistance; ++l1) {
                        if (!this.a(viewDistance, k1, l1, k, l)) { // CraftBukkit - use per-player view distance
                            this.a(k1, l1, true).a(entityplayer);
                        }

                        if (!this.a(viewDistance, k1 - i1, l1 - j1, i, j)) { // CraftBukkit - use per-player view distance
                            // CraftBukkit end
                            PlayerInstance playerinstance = this.a(k1 - i1, l1 - j1, false);

                            if (playerinstance != null) {
                                playerinstance.b(entityplayer);
                            }
                        }
                    }
                }

                entityplayer.d = entityplayer.locX;
                entityplayer.e = entityplayer.locZ;

                // CraftBukkit start - send nearest chunks first
                if (i1 > 1 || i1 < -1 || j1 > 1 || j1 < -1) {
                    final int x = i;
                    final int z = j;
                    List<ChunkCoordIntPair> chunksToSend = entityplayer.chunkCoordIntPairQueue;

                    java.util.Collections.sort(chunksToSend, new java.util.Comparator<ChunkCoordIntPair>() {
                        public int compare(ChunkCoordIntPair a, ChunkCoordIntPair b) {
                            return Math.max(Math.abs(a.x - x), Math.abs(a.z - z)) - Math.max(Math.abs(b.x - x), Math.abs(b.z - z));
                        }
                    });
                }
                // CraftBukkit end
            }
        }
    }

    public int getFurthestViewableBlock() {
        return this.f * 16 - 16;
    }

    static PlayerList a(PlayerManager playermanager) {
        return playermanager.b;
    }

    static List b(PlayerManager playermanager) {
        return playermanager.c;
    }

    // CraftBukkit start
    /**
     * This method will update references of the EntityPlayer to ensure they are being sent all and only those chunks they can see.
     * Note that no attempt is made in this method to track the distance viewable. As such, care should be taken to ensure the
     * EntityPlayer could indeed see as far previously as you have specified.
     *
     * If the chunks which the EntityPlayer can see changes, chunks will be added or removed in a spiral fashion.
     * @param entityPlayer the EntityPlayer to update
     * @param oldViewDistance the previous distance they could see
     * @param newViewDistance the new distance they can see
     */
    public void updatePlayerViewDistance(EntityPlayer entityPlayer, int oldViewDistance, int newViewDistance) {
        if (oldViewDistance == newViewDistance) {
            return;
        }
        int chunkX = (int) entityPlayer.locX >> 4;
        int chunkZ = (int) entityPlayer.locZ >> 4;

        entityPlayer.d = entityPlayer.locX; // set the 'last known' position
        entityPlayer.e = entityPlayer.locZ;

        // Going to add/remove players from player-chunk maps in a spiral fashion
        // This will send players new chunks they don't have, as well as stop sending chunks they shouldn't have
        // We move in an anticlockwise fashion, and can start at any of the four corners
        // 0 is [-1,-1]; 1 is [1,-1]; 2 is [1,1]; 3 is [-1,1];
        int corner = 2; // TODO use the direction the player is facing to determine best start corner
        int xStartOffset = this.g[(corner+3)%4][(corner+1)%2]; // calculate which offset to use based on corner we start in
        int zStartOffset = this.g[(corner+2)%4][corner%2];
        int deltaX;
        int deltaZ;
        int loop;
        int loopStart;

        if (newViewDistance < oldViewDistance) {
            // Remove player from outer chunk loops in player-chunk map
            loopStart = oldViewDistance;

            for (loop = loopStart, deltaX = xStartOffset*loopStart, deltaZ = zStartOffset*loopStart;
                 loop > newViewDistance;
                 --loop, deltaX-=xStartOffset, deltaZ-=zStartOffset) {
                for (int edge = 0; edge < 4; ++edge) {
                    int[] direction = this.g[corner++ % 4];

                    for (int i2 = 0; i2 < loop*2; ++i2) {
                        deltaX += direction[0];
                        deltaZ += direction[1];
                        this.removePlayerFromChunk(entityPlayer, chunkX + deltaX, chunkZ + deltaZ);
                    }
                }
            }
        } else if (newViewDistance > oldViewDistance) {
            // Add player to outer chunk loops in player-chunk map
            loopStart = oldViewDistance + 1; // start adding outside the current outer loop

            for (loop = loopStart, deltaX = xStartOffset*loopStart, deltaZ = zStartOffset*loopStart;
                 loop <= newViewDistance;
                 ++loop, deltaX+=xStartOffset, deltaZ+=zStartOffset) {
                for (int edge = 0; edge < 4; ++edge) {
                    int[] direction = this.g[corner++ % 4];

                    for (int i2 = 0; i2 < loop*2; ++i2) {
                        deltaX += direction[0];
                        deltaZ += direction[1];
                        this.addPlayerToChunk(entityPlayer, chunkX + deltaX, chunkZ + deltaZ);
                    }
                }
            }
        }
    }

    private void removePlayerFromChunk(EntityPlayer entityPlayer, int chunkX, int chunkZ) {
        PlayerInstance chunkPlayerMap = this.a(chunkX, chunkZ, false); // get the chunk-player map for this chunk, don't create it if it doesn't exist yet
        if (chunkPlayerMap != null) {
            chunkPlayerMap.b(entityPlayer); // if the chunk-player map exists, remove the player from it.
        }
    }

    private void addPlayerToChunk(EntityPlayer entityPlayer, int chunkX, int chunkZ) {
        PlayerInstance chunkPlayerMap = this.a(chunkX, chunkZ, true); // get the chunk-player map for this chunk, create it if it doesn't exist yet
        chunkPlayerMap.a(entityPlayer); // add the player to the chunk-player map
    }
    // CraftBukkit end
}
