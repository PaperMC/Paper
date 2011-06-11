package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

// CraftBukkit start
import java.util.Collections;
import java.util.Comparator;
// CraftBukkit end

public class PlayerManager {

    public List a = new ArrayList();
    private PlayerList b = new PlayerList();
    private List c = new ArrayList();
    private MinecraftServer server;
    private int e;
    private int f;
    private final int[][] g = new int[][] { { 1, 0}, { 0, 1}, { -1, 0}, { 0, -1}};

    public PlayerManager(MinecraftServer minecraftserver, int i, int j) {
        if (j > 15) {
            throw new IllegalArgumentException("Too big view radius!");
        } else if (j < 3) {
            throw new IllegalArgumentException("Too small view radius!");
        } else {
            this.f = j;
            this.server = minecraftserver;
            this.e = i;
        }
    }

    public WorldServer a() {
        return this.server.a(this.e);
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
        int l = this.f;
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

        this.a.add(entityplayer);
    }

    public void removePlayer(EntityPlayer entityplayer) {
        int i = (int) entityplayer.d >> 4;
        int j = (int) entityplayer.e >> 4;

        for (int k = i - this.f; k <= i + this.f; ++k) {
            for (int l = j - this.f; l <= j + this.f; ++l) {
                PlayerInstance playerinstance = this.a(k, l, false);

                if (playerinstance != null) {
                    playerinstance.b(entityplayer);
                }
            }
        }

        this.a.remove(entityplayer);
    }

    private boolean a(int i, int j, int k, int l) {
        int i1 = i - k;
        int j1 = j - l;

        return i1 >= -this.f && i1 <= this.f ? j1 >= -this.f && j1 <= this.f : false;
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
            int i1 = i - k;
            int j1 = j - l;

            if (i1 != 0 || j1 != 0) {
                for (int k1 = i - this.f; k1 <= i + this.f; ++k1) {
                    for (int l1 = j - this.f; l1 <= j + this.f; ++l1) {
                        if (!this.a(k1, l1, k, l)) {
                            this.a(k1, l1, true).a(entityplayer);
                        }

                        if (!this.a(k1 - i1, l1 - j1, i, j)) {
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
                    List<ChunkCoordIntPair> chunksToSend = entityplayer.f;
                    Collections.sort(chunksToSend, new Comparator<ChunkCoordIntPair>() {
                        public int compare(ChunkCoordIntPair a, ChunkCoordIntPair b) {
                            return Math.max(Math.abs(a.x - x), Math.abs(a.z - z)) - Math.max(Math.abs(b.x - x), Math.abs(b.z - z));
                        }
                    });
                }
                // CraftBukkit end
            }
        }
    }

    public int c() {
        return this.f * 16 - 16;
    }

    static PlayerList a(PlayerManager playermanager) {
        return playermanager.b;
    }

    static List b(PlayerManager playermanager) {
        return playermanager.c;
    }
}
