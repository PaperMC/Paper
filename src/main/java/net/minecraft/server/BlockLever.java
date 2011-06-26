package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

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

    public boolean b() {
        return false;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 1 && world.d(i, j - 1, k) ? true : (l == 2 && world.d(i, j, k + 1) ? true : (l == 3 && world.d(i, j, k - 1) ? true : (l == 4 && world.d(i + 1, j, k) ? true : l == 5 && world.d(i - 1, j, k))));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.d(i - 1, j, k) ? true : (world.d(i + 1, j, k) ? true : (world.d(i, j, k - 1) ? true : (world.d(i, j, k + 1) ? true : world.d(i, j - 1, k))));
    }

    public void postPlace(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);
        int j1 = i1 & 8;

        i1 &= 7;
        i1 = -1;
        if (l == 1 && world.d(i, j - 1, k)) {
            i1 = 5 + world.random.nextInt(2);
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

        if (i1 == -1) {
            this.b_(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
        } else {
            world.setData(i, j, k, i1 + j1);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
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

            if (!world.d(i, j - 1, k) && i1 == 6) {
                flag = true;
            }

            if (flag) {
                this.b_(world, i, j, k, world.getData(i, j, k));
                world.setTypeId(i, j, k, 0);
            }
        }
    }

    private boolean g(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.b_(world, i, j, k, world.getData(i, j, k));
            world.setTypeId(i, j, k, 0);
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
        this.interact(world, i, j, k, entityhuman);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (world.isStatic) {
            return true;
        } else {
            int l = world.getData(i, j, k);
            int i1 = l & 7;
            int j1 = 8 - (l & 8);

            // CraftBukkit start - Interact Lever
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (j1 != 8) ? 1 : 0;
            int current = (j1 == 8) ? 1 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (j1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, i1 + j1);
            world.b(i, j, k, i, j, k);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, j1 > 0 ? 0.6F : 0.5F);
            world.applyPhysics(i, j, k, this.id);
            if (i1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (i1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (i1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (i1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }

            return true;
        }
    }

    public void remove(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        if ((l & 8) > 0) {
            world.applyPhysics(i, j, k, this.id);
            int i1 = l & 7;

            if (i1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (i1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (i1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (i1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }
        }

        super.remove(world, i, j, k);
    }

    public boolean a(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0;
    }

    public boolean c(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return false;
        } else {
            int j1 = i1 & 7;

            return j1 == 6 && l == 1 ? true : (j1 == 5 && l == 1 ? true : (j1 == 4 && l == 2 ? true : (j1 == 3 && l == 3 ? true : (j1 == 2 && l == 4 ? true : j1 == 1 && l == 5))));
        }
    }

    public boolean isPowerSource() {
        return true;
    }
}
