package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event.Type;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
// CraftBukkit end

public class BlockLever extends Block {

    protected BlockLever(int i, int j) {
        super(i, j, Material.ORIENTABLE);
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        return world.d(i - 1, j, k) ? true : (world.d(i + 1, j, k) ? true : (world.d(i, j, k - 1) ? true : (world.d(i, j, k + 1) ? true : world.d(i, j - 1, k))));
    }

    public void c(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 8;

        i1 &= 7;
        if (l == 1 && world.d(i, j - 1, k)) {
            i1 = 5 + world.l.nextInt(2);
        }

        if (l == 2 && world.d(i, j, k + 1)) {
            i1 = 4;
        }

        if (l == 3 && world.d(i, j, k - 1)) {
            i1 = 3;
        }

        if (l == 4 && world.d(i + 1, j, k)) {
            i1 = 2;
        }

        if (l == 5 && world.d(i - 1, j, k)) {
            i1 = 1;
        }

        world.c(i, j, k, i1 + j1);
    }

    public void e(World world, int i, int j, int k) {
        if (world.d(i - 1, j, k)) {
            world.c(i, j, k, 1);
        } else if (world.d(i + 1, j, k)) {
            world.c(i, j, k, 2);
        } else if (world.d(i, j, k - 1)) {
            world.c(i, j, k, 3);
        } else if (world.d(i, j, k + 1)) {
            world.c(i, j, k, 4);
        } else if (world.d(i, j - 1, k)) {
            world.c(i, j, k, 5 + world.l.nextInt(2));
        }

        this.g(world, i, j, k);
    }

    public void b(World world, int i, int j, int k, int l) {
        if (this.g(world, i, j, k)) {
            int i1 = world.getData(i, j, k) & 7;
            boolean flag = false;

            if (!world.d(i - 1, j, k) && i1 == 1) {
                flag = true;
            }

            if (!world.d(i + 1, j, k) && i1 == 2) {
                flag = true;
            }

            if (!world.d(i, j, k - 1) && i1 == 3) {
                flag = true;
            }

            if (!world.d(i, j, k + 1) && i1 == 4) {
                flag = true;
            }

            if (!world.d(i, j - 1, k) && i1 == 5) {
                flag = true;
            }

            if (flag) {
                this.a_(world, i, j, k, world.getData(i, j, k));
                world.e(i, j, k, 0);
            }
        }
    }

    private boolean g(World world, int i, int j, int k) {
        if (!this.a(world, i, j, k)) {
            this.a_(world, i, j, k, world.getData(i, j, k));
            world.e(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        int l = iblockaccess.getData(i, j, k) & 7;
        float f = 0.1875F;

        if (l == 1) {
            this.a(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        } else if (l == 2) {
            this.a(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        } else if (l == 3) {
            this.a(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        } else if (l == 4) {
            this.a(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        } else {
            f = 0.25F;
            this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }
    }

    public void b(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.a(world, i, j, k, entityhuman);
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            // CraftBukkit start - Interact Lever
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

            BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(event);

            // CraftBukkit the client updates the doors before the server does it's thing.
            // Forcibly send correct data.
            if (event.isCancelled()) {
                ((EntityPlayer) entityhuman).a.b(new Packet53BlockChange(i, j, k, (WorldServer) world));
                return true;
            }
            // CraftBukkit end

            int l = world.getData(i, j, k);
            int i1 = l & 7;
            int j1 = 8 - (l & 8);

            // CraftBukkit start
            int old = (j1 != 8) ? 1 : 0;
            int current = (j1 == 8) ? 1 : 0;
            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, BlockFace.SELF, old, current);
            server.getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) == (j1 == 8)) {
                // CraftBukkit end

                world.c(i, j, k, i1 + j1);
                world.b(i, j, k, i, j, k);
                world.a((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, j1 > 0 ? 0.6F : 0.5F);
                world.h(i, j, k, this.id);
                if (i1 == 1) {
                    world.h(i - 1, j, k, this.id);
                } else if (i1 == 2) {
                    world.h(i + 1, j, k, this.id);
                } else if (i1 == 3) {
                    world.h(i, j, k - 1, this.id);
                } else if (i1 == 4) {
                    world.h(i, j, k + 1, this.id);
                } else {
                    world.h(i, j - 1, k, this.id);
                }
            } // CraftBukkit
            return true;
        }
    }

    public void b(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        if ((l & 8) > 0) {
            world.h(i, j, k, this.id);
            int i1 = l & 7;

            if (i1 == 1) {
                world.h(i - 1, j, k, this.id);
            } else if (i1 == 2) {
                world.h(i + 1, j, k, this.id);
            } else if (i1 == 3) {
                world.h(i, j, k - 1, this.id);
            } else if (i1 == 4) {
                world.h(i, j, k + 1, this.id);
            } else {
                world.h(i, j - 1, k, this.id);
            }
        }

        super.b(world, i, j, k);
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0;
    }

    public boolean d(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return false;
        } else {
            int j1 = i1 & 7;

            return j1 == 5 && l == 1 ? true : (j1 == 4 && l == 2 ? true : (j1 == 3 && l == 3 ? true : (j1 == 2 && l == 4 ? true : j1 == 1 && l == 5)));
        }
    }

    public boolean c() {
        return true;
    }
}
