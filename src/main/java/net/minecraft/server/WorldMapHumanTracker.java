package net.minecraft.server;

// CraftBukkit start
import org.bukkit.map.MapCursor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.map.RenderData;
// CraftBukkit end

public class WorldMapHumanTracker {

    public final EntityHuman trackee;
    public int[] b;
    public int[] c;
    private int e;
    private int f;
    private byte[] g;

    final WorldMap d;

    public WorldMapHumanTracker(WorldMap worldmap, EntityHuman entityhuman) {
        this.d = worldmap;
        this.b = new int[128];
        this.c = new int[128];
        this.e = 0;
        this.f = 0;
        this.trackee = entityhuman;

        for (int i = 0; i < this.b.length; ++i) {
            this.b[i] = 0;
            this.c[i] = 127;
        }
    }

    public byte[] a(ItemStack itemstack) {
        int i;
        int j;
        
        RenderData render = this.d.mapView.render((CraftPlayer) trackee.getBukkitEntity()); // CraftBukkit

        if (--this.f < 0) {
            this.f = 4;
            byte[] abyte = new byte[render.cursors.size() * 3 + 1]; // CraftBukkit

            abyte[0] = 1;

            // CraftBukkit start
            for (i = 0; i < render.cursors.size(); ++i) {
                MapCursor cursor = render.cursors.get(i);
                if (!cursor.isVisible()) continue;
                
                byte value = (byte) (((cursor.getRawType() == 0 || cursor.getDirection() < 8 ? cursor.getDirection() : cursor.getDirection() - 1) & 15) * 16);
                abyte[i * 3 + 1] = (byte) (value | (cursor.getRawType() != 0 && value < 0 ? 16 - cursor.getRawType() : cursor.getRawType()));
                abyte[i * 3 + 2] = (byte) cursor.getX();
                abyte[i * 3 + 3] = (byte) cursor.getY();
            }
            // CraftBukkit end

            boolean flag = true;

            if (this.g != null && this.g.length == abyte.length) {
                for (j = 0; j < abyte.length; ++j) {
                    if (abyte[j] != this.g[j]) {
                        flag = false;
                        break;
                    }
                }
            } else {
                flag = false;
            }

            if (!flag) {
                this.g = abyte;
                return abyte;
            }
        }

        for (int k = 0; k < 10; ++k) {
            i = this.e * 11 % 128;
            ++this.e;
            if (this.b[i] >= 0) {
                j = this.c[i] - this.b[i] + 1;
                int l = this.b[i];
                byte[] abyte1 = new byte[j + 3];

                abyte1[0] = 0;
                abyte1[1] = (byte) i;
                abyte1[2] = (byte) l;

                for (int i1 = 0; i1 < abyte1.length - 3; ++i1) {
                    abyte1[i1 + 3] = render.buffer[(i1 + l) * 128 + i]; // CraftBukkit
                }

                this.c[i] = -1;
                this.b[i] = -1;
                return abyte1;
            }
        }

        return null;
    }
}
