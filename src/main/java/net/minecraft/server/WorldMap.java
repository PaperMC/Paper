package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap extends WorldMapBase {

    public int b;
    public int c;
    public byte map;
    public byte e;
    public byte[] f = new byte[16384];
    public int g;
    public List h = new ArrayList();
    private Map j = new HashMap();
    public List i = new ArrayList();

    public WorldMap(String s) {
        super(s);
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.map = nbttagcompound.c("dimension");
        this.b = nbttagcompound.e("xCenter");
        this.c = nbttagcompound.e("zCenter");
        this.e = nbttagcompound.c("scale");
        if (this.e < 0) {
            this.e = 0;
        }

        if (this.e > 4) {
            this.e = 4;
        }

        short short1 = nbttagcompound.d("width");
        short short2 = nbttagcompound.d("height");

        if (short1 == 128 && short2 == 128) {
            this.f = nbttagcompound.j("colors");
        } else {
            byte[] abyte = nbttagcompound.j("colors");

            this.f = new byte[16384];
            int i = (128 - short1) / 2;
            int j = (128 - short2) / 2;

            for (int k = 0; k < short2; ++k) {
                int l = k + j;

                if (l >= 0 || l < 128) {
                    for (int i1 = 0; i1 < short1; ++i1) {
                        int j1 = i1 + i;

                        if (j1 >= 0 || j1 < 128) {
                            this.f[j1 + l * 128] = abyte[i1 + k * short1];
                        }
                    }
                }
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("dimension", this.map);
        nbttagcompound.a("xCenter", this.b);
        nbttagcompound.a("zCenter", this.c);
        nbttagcompound.a("scale", this.e);
        nbttagcompound.a("width", (short) 128);
        nbttagcompound.a("height", (short) 128);
        nbttagcompound.a("colors", this.f);
    }

    public void a(EntityHuman entityhuman, ItemStack itemstack) {
        if (!this.j.containsKey(entityhuman)) {
            WorldMapHumanTracker worldmaphumantracker = new WorldMapHumanTracker(this, entityhuman);

            this.j.put(entityhuman, worldmaphumantracker);
            this.h.add(worldmaphumantracker);
        }

        this.i.clear();

        for (int i = 0; i < this.h.size(); ++i) {
            WorldMapHumanTracker worldmaphumantracker1 = (WorldMapHumanTracker) this.h.get(i);

            if (!worldmaphumantracker1.trackee.dead && worldmaphumantracker1.trackee.inventory.c(itemstack)) {
                float f = (float) (worldmaphumantracker1.trackee.locX - (double) this.b) / (float) (1 << this.e);
                float f1 = (float) (worldmaphumantracker1.trackee.locZ - (double) this.c) / (float) (1 << this.e);
                byte b0 = 64;
                byte b1 = 64;

                if (f >= (float) (-b0) && f1 >= (float) (-b1) && f <= (float) b0 && f1 <= (float) b1) {
                    byte b2 = 0;
                    byte b3 = (byte) ((int) ((double) (f * 2.0F) + 0.5D));
                    byte b4 = (byte) ((int) ((double) (f1 * 2.0F) + 0.5D));
                    // CraftBukkit
                    byte b5 = (byte) ((int) ((double) (worldmaphumantracker1.trackee.yaw * 16.0F / 360.0F) + 0.5D));

                    if (this.map < 0) {
                        int j = this.g / 10;

                        b5 = (byte) (j * j * 34187121 + j * 121 >> 15 & 15);
                    }

                    if (worldmaphumantracker1.trackee.dimension == this.map) {
                        this.i.add(new WorldMapOrienter(this, b2, b3, b4, b5));
                    }
                }
            } else {
                this.j.remove(worldmaphumantracker1.trackee);
                this.h.remove(worldmaphumantracker1);
            }
        }
    }

    public byte[] a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        WorldMapHumanTracker worldmaphumantracker = (WorldMapHumanTracker) this.j.get(entityhuman);

        if (worldmaphumantracker == null) {
            return null;
        } else {
            byte[] abyte = worldmaphumantracker.a(itemstack);

            return abyte;
        }
    }

    public void a(int i, int j, int k) {
        super.a();

        for (int l = 0; l < this.h.size(); ++l) {
            WorldMapHumanTracker worldmaphumantracker = (WorldMapHumanTracker) this.h.get(l);

            if (worldmaphumantracker.b[i] < 0 || worldmaphumantracker.b[i] > j) {
                worldmaphumantracker.b[i] = j;
            }

            if (worldmaphumantracker.c[i] < 0 || worldmaphumantracker.c[i] < k) {
                worldmaphumantracker.c[i] = k;
            }
        }
    }
}
