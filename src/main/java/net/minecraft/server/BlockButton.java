package net.minecraft.server;

import java.util.Random;

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

public class BlockButton extends Block {

    protected BlockButton(int i, int j) {
        super(i, j, Material.ORIENTABLE);
        this.a(true);
    }

    public AxisAlignedBB d(World world, int i, int j, int k) {
        return null;
    }

    public int b() {
        return 20;
    }

    public boolean a() {
        return false;
    }

    public boolean a(World world, int i, int j, int k) {
        return world.d(i - 1, j, k) ? true : (world.d(i + 1, j, k) ? true : (world.d(i, j, k - 1) ? true : world.d(i, j, k + 1)));
    }

    public void c(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 8;

        i1 &= 7;
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
        int l = iblockaccess.getData(i, j, k);
        int i1 = l & 7;
        boolean flag = (l & 8) > 0;
        float f = 0.375F;
        float f1 = 0.625F;
        float f2 = 0.1875F;
        float f3 = 0.125F;

        if (flag) {
            f3 = 0.0625F;
        }

        if (i1 == 1) {
            this.a(0.0F, f, 0.5F - f2, f3, f1, 0.5F + f2);
        } else if (i1 == 2) {
            this.a(1.0F - f3, f, 0.5F - f2, 1.0F, f1, 0.5F + f2);
        } else if (i1 == 3) {
            this.a(0.5F - f2, f, 0.0F, 0.5F + f2, f1, f3);
        } else if (i1 == 4) {
            this.a(0.5F - f2, f, 1.0F - f3, 0.5F + f2, f1, 1.0F);
        }
    }

    public void b(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.a(world, i, j, k, entityhuman);
    }

    public boolean a(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            // CraftBukkit start - Interact Button
            CraftWorld craftWorld = ((WorldServer) world).getWorld();
            CraftServer server = ((WorldServer) world).getServer();
            Type eventType = Type.BLOCK_INTERACT;
            CraftBlock block = (CraftBlock) craftWorld.getBlockAt(i, j, k);
            LivingEntity who = (entityhuman == null) ? null : (LivingEntity) entityhuman.getBukkitEntity();

            BlockInteractEvent event = new BlockInteractEvent(eventType, block, who);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return true;
            }
            // CraftBukkit end

            int l = world.getData(i, j, k);
            int i1 = l & 7;
            int j1 = 8 - (l & 8);

            if (j1 == 0) {
                return true;
            } else {
                //Allow the lever to change the current
                int old = (j1 != 8) ? 1 : 0;
                int current = (j1 == 8) ? 1 : 0;
                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, BlockFace.SELF, old, current);
                server.getPluginManager().callEvent(eventRedstone);
                if ((eventRedstone.getNewCurrent() > 0) == (j1 == 8)) {
                    world.c(i, j, k, i1 + j1);
                    world.b(i, j, k, i, j, k);
                    world.a((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.6F);
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
                    world.i(i, j, k, this.id);
                }
                return true;
            }
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

    public void a(World world, int i, int j, int k, Random random) {
        if (!world.isStatic) {
            int l = world.getData(i, j, k);

            if ((l & 8) != 0) {
                world.c(i, j, k, l & 7);
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

                world.a((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, 0.5F);
                world.b(i, j, k, i, j, k);
            }
        }
    }
}
