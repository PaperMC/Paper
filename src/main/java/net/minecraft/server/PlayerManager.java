package net.minecraft.server;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private List a = new ArrayList();
    private PlayerList b = new PlayerList();
    private List c = new ArrayList();
    private MinecraftServer d;
    public WorldServer world; // Craftbukkit

    // Craftbukkit - change of method signature
    public PlayerManager(MinecraftServer minecraftserver, WorldServer world) {
        this.d = minecraftserver;

        this.world = world; // Craftbukkit
    }

    public void a() {
        for (int i = 0; i < this.c.size(); ++i) {
            ((PlayerInstance) this.c.get(i)).a();
        }

        this.c.clear();
    }

    // Craftbukkit - method signature changed!
    private PlayerInstance a(int i, int j, boolean flag, WorldServer world) {
        long k = (long) i + 2147483647L | (long) j + 2147483647L << 32;
        PlayerInstance playerinstance = (PlayerInstance) this.b.a(k);

        if (playerinstance == null && flag) {
            playerinstance = new PlayerInstance(this, i, j);
            this.b.a(k, playerinstance);
        }

        return playerinstance;
    }

    // Craftbukkit - method signature changed!
    public void a(int i, int j, int k, WorldServer world) {
        int l = i >> 4;
        int i1 = k >> 4;
        PlayerInstance playerinstance = this.a(l, i1, false, world);

        if (playerinstance != null) {
            playerinstance.a(i & 15, j, k & 15);
        }
    }

    // CraftBukkit start
    private final int[][] direction = new int[][] {
        {  1,  0 },
        {  0,  1 },
        { -1,  0 },
        {  0, -1 },
    };
    // CraftBukkit end

    public void a(EntityPlayer entityplayer) {
        int i = (int) entityplayer.locX >> 4;
        int j = (int) entityplayer.locZ >> 4;

        entityplayer.d = entityplayer.locX;
        entityplayer.e = entityplayer.locZ;

        // CraftBukkit start
        int facing = 0;
        int size = 10;
        int dx = 0;
        int dz = 0;

        // Origin
        this.a(i, j, true, ((WorldServer)entityplayer.world)).a(entityplayer);

        // All but the last leg
        for (int legSize = 1; legSize <= size * 2; legSize++) {
            for (int leg = 0; leg < 2; leg++) {
                int[] dir = direction[ facing++ % 4 ];

                for (int k = 0; k < legSize; k++ ) {
                    dx += dir[0];
                    dz += dir[1];
                    this.a(i + dx, j + dz, true, ((WorldServer)entityplayer.world)).a(entityplayer); // Craftbukkit
                }
            }
        }

        // Final leg
        facing %= 4;
        for (int k = 0; k < size * 2; k++) {
            dx += direction[facing][0];
            dz += direction[facing][1];
            this.a(i + dx, j + dz, true, ((WorldServer)entityplayer.world)).a(entityplayer);
        }
        // CraftBukkit end

        this.a.add(entityplayer);
    }

    public void b(EntityPlayer entityplayer) {
        int i = (int) entityplayer.d >> 4;
        int j = (int) entityplayer.e >> 4;

        for (int k = i - 10; k <= i + 10; ++k) {
            for (int l = j - 10; l <= j + 10; ++l) {
                PlayerInstance playerinstance = this.a(k, l, false, ((WorldServer)entityplayer.world)); // Craftbukkit

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

            if (i1 != 0 || j1 != 0) {
                for (int k1 = i - 10; k1 <= i + 10; ++k1) {
                    for (int l1 = j - 10; l1 <= j + 10; ++l1) {
                        if (!this.a(k1, l1, k, l)) {
                            this.a(k1, l1, true, ((WorldServer)entityplayer.world)).a(entityplayer); // Craftbukkit
                        }

                        if (!this.a(k1 - i1, l1 - j1, i, j)) {
                            PlayerInstance playerinstance = this.a(k1 - i1, l1 - j1, false, ((WorldServer)entityplayer.world)); // Craftbukkit

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
