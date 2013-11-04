package net.minecraft.server;

import org.bukkit.event.world.PortalCreateEvent; // CraftBukkit

public class PortalCreator {

    private final World a;
    private final int b;
    private final int c;
    private final int d;
    private int e = 0;
    private ChunkCoordinates f;
    private int g;
    private int h;
    java.util.Collection<org.bukkit.block.Block> blocks; // CraftBukkit

    public PortalCreator(World world, int i, int j, int k, int l) {
        this.a = world;
        this.b = l;
        this.d = BlockPortal.a[l][0];
        this.c = BlockPortal.a[l][1];

        for (int i1 = j; j > i1 - 21 && j > 0 && this.a(world.getType(i, j - 1, k)); --j) {
            ;
        }

        int j1 = this.a(i, j, k, this.d) - 1;

        if (j1 >= 0) {
            this.f = new ChunkCoordinates(i + j1 * Direction.a[this.d], j, k + j1 * Direction.b[this.d]);
            this.h = this.a(this.f.x, this.f.y, this.f.z, this.c);
            if (this.h < 2 || this.h > 21) {
                this.f = null;
                this.h = 0;
            }
        }

        if (this.f != null) {
            this.g = this.a();
        }
    }

    protected int a(int i, int j, int k, int l) {
        int i1 = Direction.a[l];
        int j1 = Direction.b[l];

        int k1;
        Block block;

        for (k1 = 0; k1 < 22; ++k1) {
            block = this.a.getType(i + i1 * k1, j, k + j1 * k1);
            if (!this.a(block)) {
                break;
            }

            Block block1 = this.a.getType(i + i1 * k1, j - 1, k + j1 * k1);

            if (block1 != Blocks.OBSIDIAN) {
                break;
            }
        }

        block = this.a.getType(i + i1 * k1, j, k + j1 * k1);
        return block == Blocks.OBSIDIAN ? k1 : 0;
    }

    protected int a() {
        this.blocks = new java.util.HashSet<org.bukkit.block.Block>(); // CraftBukkit
        org.bukkit.World bworld = this.a.getWorld();
        int i;
        int j;
        int k;
        int l;

        label56:
        for (this.g = 0; this.g < 21; ++this.g) {
            i = this.f.y + this.g;

            for (j = 0; j < this.h; ++j) {
                k = this.f.x + j * Direction.a[BlockPortal.a[this.b][1]];
                l = this.f.z + j * Direction.b[BlockPortal.a[this.b][1]];
                Block block = this.a.getType(k, i, l);

                if (!this.a(block)) {
                    break label56;
                }

                if (block == Blocks.PORTAL) {
                    ++this.e;
                }

                if (j == 0) {
                    block = this.a.getType(k + Direction.a[BlockPortal.a[this.b][0]], i, l + Direction.b[BlockPortal.a[this.b][0]]);
                    if (block != Blocks.OBSIDIAN) {
                        break label56;
                        // CraftBukkit start - add the block to our list
                    } else {
                        blocks.add(bworld.getBlockAt(k + Direction.a[BlockPortal.a[this.b][0]], i, l + Direction.b[BlockPortal.a[this.b][0]]));
                        // CraftBukkit end
                    }
                } else if (j == this.h - 1) {
                    block = this.a.getType(k + Direction.a[BlockPortal.a[this.b][1]], i, l + Direction.b[BlockPortal.a[this.b][1]]);
                    if (block != Blocks.OBSIDIAN) {
                        break label56;
                        // CraftBukkit start - add the block to our list
                    } else {
                        blocks.add(bworld.getBlockAt(k + Direction.a[BlockPortal.a[this.b][1]], i, l + Direction.b[BlockPortal.a[this.b][1]]));
                        // CraftBukkit end
                    }
                }
            }
        }

        for (i = 0; i < this.h; ++i) {
            j = this.f.x + i * Direction.a[BlockPortal.a[this.b][1]];
            k = this.f.y + this.g;
            l = this.f.z + i * Direction.b[BlockPortal.a[this.b][1]];
            if (this.a.getType(j, k, l) != Blocks.OBSIDIAN) {
                this.g = 0;
                break;
            }
        }

        if (this.g <= 21 && this.g >= 3) {
            return this.g;
        } else {
            this.f = null;
            this.h = 0;
            this.g = 0;
            return 0;
        }
    }

    protected boolean a(Block block) {
        return block.material == Material.AIR || block == Blocks.FIRE || block == Blocks.PORTAL;
    }

    public boolean b() {
        return this.f != null && this.h >= 2 && this.h <= 21 && this.g >= 3 && this.g <= 21;
    }

    // CraftBukkit start - return boolean
    public boolean c() {
        org.bukkit.World bworld = this.a.getWorld();

        // Copy below for loop
        for (int i = 0; i < this.h; ++i) {
            int j = this.f.x + Direction.a[this.c] * i;
            int k = this.f.z + Direction.b[this.c] * i;

            for (int l = 0; l < this.g; ++l) {
                int i1 = this.f.y + l;

                bworld.getBlockAt(j, i1, k);
            }
        }

        PortalCreateEvent event = new PortalCreateEvent(blocks, bworld, PortalCreateEvent.CreateReason.FIRE);
        this.a.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return false;
        }
        // CraftBukkit end

        for (int i = 0; i < this.h; ++i) {
            int j = this.f.x + Direction.a[this.c] * i;
            int k = this.f.z + Direction.b[this.c] * i;

            for (int l = 0; l < this.g; ++l) {
                int i1 = this.f.y + l;

                this.a.setTypeAndData(j, i1, k, Blocks.PORTAL, this.b, 2);
            }
        }

        return true; // CraftBukkit
    }

    static int a(PortalCreator portalcreator) {
        return portalcreator.e;
    }

    static int b(PortalCreator portalcreator) {
        return portalcreator.h;
    }

    static int c(PortalCreator portalcreator) {
        return portalcreator.g;
    }
}
