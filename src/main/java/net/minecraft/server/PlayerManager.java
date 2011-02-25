package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private List a = new ArrayList();
    private PlayerList b = new PlayerList();
    private List c = new ArrayList();
    private MinecraftServer d;
    private final int[][] e = new int[][] { { 1, 0}, { 0, 1}, { -1, 0}, { 0, -1}};

    // CraftBukkit start
    public WorldServer world;

    // CraftBukkit - change of method signature
    public PlayerManager(MinecraftServer minecraftserver, WorldServer world) {
        this.d = minecraftserver;
        this.world = world;
    }
    // CraftBukkit end

    public void a() {
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

    public void a(int i, int j, int k) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerInstance playerinstance = this.a(l, i1, false);

        if (playerinstance != null) {
            playerinstance.a(i & 15, j, k & 15);
        }
    }

    public void a(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;
        int k = 0;
        byte b0 = 10;
        int l = 0;
        int i1 = 0;

        this.a(i, j, true).a(entityplayer);

        int j1;

        for (j1 = 1; j1 <= b0 * 2; ++j1) {
            for (int k1 = 0; k1 < 2; ++k1) {
                int[] aint = this.e[k++ % 4];

                for (int l1 = 0; l1 < j1; ++l1) {
                    l += aint[0];
                    i1 += aint[1];
                    this.a(i + l, j + i1, true).a(entityplayer);
                }
            }
        }

        k %= 4;

        for (j1 = 0; j1 < b0 * 2; ++j1) {
            l += this.e[k][0];
            i1 += this.e[k][1];
            this.a(i + l, j + i1, true).a(entityplayer);
        }

        this.a.add(entityplayer);
    }

    public void b(EntityPlayer entityplayer) {
        int i = (int) entityplayer.d >> 4;
        int j = (int) entityplayer.e >> 4;

        for (int k = i - 10; k <= i + 10; ++k) {
            for (int l = j - 10; l <= j + 10; ++l) {
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

        return i1 >= -10 && i1 <= 10 ? j1 >= -10 && j1 <= 10 : false;
    }

    public void c(EntityPlayer entityplayer) {
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

            // Craftbukkit start
            if (!this.a(i, j, k, l)) {
                this.a(i, j, true).a(entityplayer);
            }

            if (!this.a(i - i1, j - j1, i, j)) {
                PlayerInstance playerinstance = this.a(i - i1, j - j1, false);

                if (playerinstance != null) {
                    playerinstance.b(entityplayer);
                }
            }
            // Craftbukkit end

            if (i1 != 0 || j1 != 0) {
                for (int k1 = i - 10; k1 <= i + 10; ++k1) {
                    for (int l1 = j - 10; l1 <= j + 10; ++l1) {
                        // Craftbukkit start
                        if ((k1 == i) && (l1 == j)) {
                            continue;
                        }
                        // Craftbukkit end
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
            }
        }
    }

    public int b() {
        return 144;
    }

    static MinecraftServer a(PlayerManager playermanager) {
        return playermanager.d;
    }

    static PlayerList b(PlayerManager playermanager) {
        return playermanager.b;
    }

    static List c(PlayerManager playermanager) {
        return playermanager.c;
    }
}
