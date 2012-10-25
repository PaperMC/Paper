package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// CraftBukkit start
import java.util.UUID;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.map.CraftMapView;
// CraftBukkit end

public class WorldMap extends WorldMapBase {

    public int centerX;
    public int centerZ;
    public byte map;
    public byte scale;
    public byte[] colors = new byte[16384];
    public List f = new ArrayList();
    private Map i = new HashMap();
    public Map g = new LinkedHashMap();

    // CraftBukkit start
    public final CraftMapView mapView;
    private CraftServer server;
    private UUID uniqueId = null;
    // CraftBukkit end

    public WorldMap(String s) {
        super(s);
        // CraftBukkit start
        mapView = new CraftMapView(this);
        server = (CraftServer) org.bukkit.Bukkit.getServer();
        // CraftBukkit end
    }

    public void a(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        byte dimension = nbttagcompound.getByte("dimension");

        if (dimension >= 10) {
            long least = nbttagcompound.getLong("UUIDLeast");
            long most = nbttagcompound.getLong("UUIDMost");

            if (least != 0L && most != 0L) {
                this.uniqueId = new UUID(most, least);

                CraftWorld world = (CraftWorld) server.getWorld(this.uniqueId);
                // Check if the stored world details are correct.
                if (world == null) {
                    /* All Maps which do not have their valid world loaded are set to a dimension which hopefully won't be reached.
                       This is to prevent them being corrupted with the wrong map data. */
                    dimension = 127;
                } else {
                    dimension = (byte) world.getHandle().dimension;
                }
            }
        }

        this.map = dimension;
        // CraftBukkit end
        this.centerX = nbttagcompound.getInt("xCenter");
        this.centerZ = nbttagcompound.getInt("zCenter");
        this.scale = nbttagcompound.getByte("scale");
        if (this.scale < 0) {
            this.scale = 0;
        }

        if (this.scale > 4) {
            this.scale = 4;
        }

        short short1 = nbttagcompound.getShort("width");
        short short2 = nbttagcompound.getShort("height");

        if (short1 == 128 && short2 == 128) {
            this.colors = nbttagcompound.getByteArray("colors");
        } else {
            byte[] abyte = nbttagcompound.getByteArray("colors");

            this.colors = new byte[16384];
            int i = (128 - short1) / 2;
            int j = (128 - short2) / 2;

            for (int k = 0; k < short2; ++k) {
                int l = k + j;

                if (l >= 0 || l < 128) {
                    for (int i1 = 0; i1 < short1; ++i1) {
                        int j1 = i1 + i;

                        if (j1 >= 0 || j1 < 128) {
                            this.colors[j1 + l * 128] = abyte[i1 + k * short1];
                        }
                    }
                }
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        // CraftBukkit start
        if (this.map >= 10) {
            if (this.uniqueId == null) {
                for (org.bukkit.World world : server.getWorlds()) {
                    CraftWorld cWorld = (CraftWorld) world;
                    if (cWorld.getHandle().dimension == this.map) {
                        this.uniqueId = cWorld.getUID();
                        break;
                    }
                }
            }
            /* Perform a second check to see if a matching world was found, this is a necessary
               change incase Maps are forcefully unlinked from a World and lack a UID.*/
            if (this.uniqueId != null) {
                nbttagcompound.setLong("UUIDLeast", this.uniqueId.getLeastSignificantBits());
                nbttagcompound.setLong("UUIDMost", this.uniqueId.getMostSignificantBits());
            }
        }
        // CraftBukkit end
        nbttagcompound.setByte("dimension", this.map);
        nbttagcompound.setInt("xCenter", this.centerX);
        nbttagcompound.setInt("zCenter", this.centerZ);
        nbttagcompound.setByte("scale", this.scale);
        nbttagcompound.setShort("width", (short) 128);
        nbttagcompound.setShort("height", (short) 128);
        nbttagcompound.setByteArray("colors", this.colors);
    }

    public void a(EntityHuman entityhuman, ItemStack itemstack) {
        if (!this.i.containsKey(entityhuman)) {
            WorldMapHumanTracker worldmaphumantracker = new WorldMapHumanTracker(this, entityhuman);

            this.i.put(entityhuman, worldmaphumantracker);
            this.f.add(worldmaphumantracker);
        }

        if (!entityhuman.inventory.c(itemstack)) {
            this.g.remove(entityhuman.getName());
        }

        for (int i = 0; i < this.f.size(); ++i) {
            WorldMapHumanTracker worldmaphumantracker1 = (WorldMapHumanTracker) this.f.get(i);

            if (!worldmaphumantracker1.trackee.dead && (worldmaphumantracker1.trackee.inventory.c(itemstack) || itemstack.y())) {
                if (!itemstack.y() && worldmaphumantracker1.trackee.dimension == this.map) {
                    this.a(0, worldmaphumantracker1.trackee.world, worldmaphumantracker1.trackee.getName(), worldmaphumantracker1.trackee.locX, worldmaphumantracker1.trackee.locZ, (double) worldmaphumantracker1.trackee.yaw);
                }
            } else {
                this.i.remove(worldmaphumantracker1.trackee);
                this.f.remove(worldmaphumantracker1);
            }
        }

        if (itemstack.y()) {
            this.a(1, entityhuman.world, "frame-" + itemstack.z().id, (double) itemstack.z().x, (double) itemstack.z().z, (double) (itemstack.z().direction * 90));
        }
    }

    private void a(int i, World world, String s, double d0, double d1, double d2) {
        int j = 1 << this.scale;
        float f = (float) (d0 - (double) this.centerX) / (float) j;
        float f1 = (float) (d1 - (double) this.centerZ) / (float) j;
        byte b0 = (byte) ((int) ((double) (f * 2.0F) + 0.5D));
        byte b1 = (byte) ((int) ((double) (f1 * 2.0F) + 0.5D));
        byte b2 = 63;
        byte b3;

        if (f >= (float) (-b2) && f1 >= (float) (-b2) && f <= (float) b2 && f1 <= (float) b2) {
            d2 += d2 < 0.0D ? -8.0D : 8.0D;
            b3 = (byte) ((int) (d2 * 16.0D / 360.0D));
            if (this.map < 0) {
                int k = (int) (world.getWorldData().g() / 10L);

                b3 = (byte) (k * k * 34187121 + k * 121 >> 15 & 15);
            }
        } else {
            if (Math.abs(f) >= 320.0F || Math.abs(f1) >= 320.0F) {
                this.g.remove(s);
                return;
            }

            i = 6;
            b3 = 0;
            if (f <= (float) (-b2)) {
                b0 = (byte) ((int) ((double) (b2 * 2) + 2.5D));
            }

            if (f1 <= (float) (-b2)) {
                b1 = (byte) ((int) ((double) (b2 * 2) + 2.5D));
            }

            if (f >= (float) b2) {
                b0 = (byte) (b2 * 2 + 1);
            }

            if (f1 >= (float) b2) {
                b1 = (byte) (b2 * 2 + 1);
            }
        }

        this.g.put(s, new WorldMapDecoration(this, (byte) i, b0, b1, b3));
    }

    public byte[] getUpdatePacket(ItemStack itemstack, World world, EntityHuman entityhuman) {
        WorldMapHumanTracker worldmaphumantracker = (WorldMapHumanTracker) this.i.get(entityhuman);

        return worldmaphumantracker == null ? null : worldmaphumantracker.a(itemstack);
    }

    public void flagDirty(int i, int j, int k) {
        super.c();

        for (int l = 0; l < this.f.size(); ++l) {
            WorldMapHumanTracker worldmaphumantracker = (WorldMapHumanTracker) this.f.get(l);

            if (worldmaphumantracker.b[i] < 0 || worldmaphumantracker.b[i] > j) {
                worldmaphumantracker.b[i] = j;
            }

            if (worldmaphumantracker.c[i] < 0 || worldmaphumantracker.c[i] < k) {
                worldmaphumantracker.c[i] = k;
            }
        }
    }

    public WorldMapHumanTracker a(EntityHuman entityhuman) {
        WorldMapHumanTracker worldmaphumantracker = (WorldMapHumanTracker) this.i.get(entityhuman);

        if (worldmaphumantracker == null) {
            worldmaphumantracker = new WorldMapHumanTracker(this, entityhuman);
            this.i.put(entityhuman, worldmaphumantracker);
            this.f.add(worldmaphumantracker);
        }

        return worldmaphumantracker;
    }
}
