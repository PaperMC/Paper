package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockLever extends Block {

    protected BlockLever(int i) {
        super(i, Material.ORIENTABLE);
        this.a(CreativeModeTab.d);
    }

    public AxisAlignedBB b(World world, int i, int j, int k) {
        return null;
    }

    public boolean c() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int d() {
        return 12;
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        return l == 0 && world.u(i, j + 1, k) ? true : (l == 1 && world.w(i, j - 1, k) ? true : (l == 2 && world.u(i, j, k + 1) ? true : (l == 3 && world.u(i, j, k - 1) ? true : (l == 4 && world.u(i + 1, j, k) ? true : l == 5 && world.u(i - 1, j, k)))));
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return world.u(i - 1, j, k) ? true : (world.u(i + 1, j, k) ? true : (world.u(i, j, k - 1) ? true : (world.u(i, j, k + 1) ? true : (world.w(i, j - 1, k) ? true : world.u(i, j + 1, k)))));
    }

    public int getPlacedData(World world, int i, int j, int k, int l, float f, float f1, float f2, int i1) {
        int j1 = i1 & 8;
        int k1 = i1 & 7;
        byte b0 = -1;

        if (l == 0 && world.u(i, j + 1, k)) {
            b0 = 0;
        }

        if (l == 1 && world.w(i, j - 1, k)) {
            b0 = 5;
        }

        if (l == 2 && world.u(i, j, k + 1)) {
            b0 = 4;
        }

        if (l == 3 && world.u(i, j, k - 1)) {
            b0 = 3;
        }

        if (l == 4 && world.u(i + 1, j, k)) {
            b0 = 2;
        }

        if (l == 5 && world.u(i - 1, j, k)) {
            b0 = 1;
        }

        return b0 + j1;
    }

    public void postPlace(World world, int i, int j, int k, EntityLiving entityliving, ItemStack itemstack) {
        int l = world.getData(i, j, k);
        int i1 = l & 7;
        int j1 = l & 8;

        if (i1 == d(1)) {
            if ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 1) == 0) {
                world.setData(i, j, k, 5 | j1, 2);
            } else {
                world.setData(i, j, k, 6 | j1, 2);
            }
        } else if (i1 == d(0)) {
            if ((MathHelper.floor((double) (entityliving.yaw * 4.0F / 360.0F) + 0.5D) & 1) == 0) {
                world.setData(i, j, k, 7 | j1, 2);
            } else {
                world.setData(i, j, k, 0 | j1, 2);
            }
        }
    }

    public static int d(int i) {
        switch (i) {
        case 0:
            return 0;

        case 1:
            return 5;

        case 2:
            return 4;

        case 3:
            return 3;

        case 4:
            return 2;

        case 5:
            return 1;

        default:
            return -1;
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (this.k(world, i, j, k)) {
            int i1 = world.getData(i, j, k) & 7;
            boolean flag = false;

            if (!world.u(i - 1, j, k) && i1 == 1) {
                flag = true;
            }

            if (!world.u(i + 1, j, k) && i1 == 2) {
                flag = true;
            }

            if (!world.u(i, j, k - 1) && i1 == 3) {
                flag = true;
            }

            if (!world.u(i, j, k + 1) && i1 == 4) {
                flag = true;
            }

            if (!world.w(i, j - 1, k) && i1 == 5) {
                flag = true;
            }

            if (!world.w(i, j - 1, k) && i1 == 6) {
                flag = true;
            }

            if (!world.u(i, j + 1, k) && i1 == 0) {
                flag = true;
            }

            if (!world.u(i, j + 1, k) && i1 == 7) {
                flag = true;
            }

            if (flag) {
                this.c(world, i, j, k, world.getData(i, j, k), 0);
                world.setAir(i, j, k);
            }
        }
    }

    private boolean k(World world, int i, int j, int k) {
        if (!this.canPlace(world, i, j, k)) {
            this.c(world, i, j, k, world.getData(i, j, k), 0);
            world.setAir(i, j, k);
            return false;
        } else {
            return true;
        }
    }

    public void updateShape(IBlockAccess iblockaccess, int i, int j, int k) {
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
        } else if (l != 5 && l != 6) {
            if (l == 0 || l == 7) {
                f = 0.25F;
                this.a(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
            }
        } else {
            f = 0.25F;
            this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
        }
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman, int l, float f, float f1, float f2) {
        if (world.isStatic) {
            return true;
        } else {
            int i1 = world.getData(i, j, k);
            int j1 = i1 & 7;
            int k1 = 8 - (i1 & 8);

            // CraftBukkit start - Interact Lever
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            int old = (k1 != 8) ? 15 : 0;
            int current = (k1 == 8) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, old, current);
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (k1 == 8)) {
                return true;
            }
            // CraftBukkit end

            world.setData(i, j, k, j1 + k1, 3);
            world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.click", 0.3F, k1 > 0 ? 0.6F : 0.5F);
            world.applyPhysics(i, j, k, this.id);
            if (j1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (j1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (j1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (j1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else if (j1 != 5 && j1 != 6) {
                if (j1 == 0 || j1 == 7) {
                    world.applyPhysics(i, j + 1, k, this.id);
                }
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }

            return true;
        }
    }

    public void remove(World world, int i, int j, int k, int l, int i1) {
        if ((i1 & 8) > 0) {
            world.applyPhysics(i, j, k, this.id);
            int j1 = i1 & 7;

            if (j1 == 1) {
                world.applyPhysics(i - 1, j, k, this.id);
            } else if (j1 == 2) {
                world.applyPhysics(i + 1, j, k, this.id);
            } else if (j1 == 3) {
                world.applyPhysics(i, j, k - 1, this.id);
            } else if (j1 == 4) {
                world.applyPhysics(i, j, k + 1, this.id);
            } else if (j1 != 5 && j1 != 6) {
                if (j1 == 0 || j1 == 7) {
                    world.applyPhysics(i, j + 1, k, this.id);
                }
            } else {
                world.applyPhysics(i, j - 1, k, this.id);
            }
        }

        super.remove(world, i, j, k, l, i1);
    }

    public int b(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        return (iblockaccess.getData(i, j, k) & 8) > 0 ? 15 : 0;
    }

    public int c(IBlockAccess iblockaccess, int i, int j, int k, int l) {
        int i1 = iblockaccess.getData(i, j, k);

        if ((i1 & 8) == 0) {
            return 0;
        } else {
            int j1 = i1 & 7;

            return j1 == 0 && l == 0 ? 15 : (j1 == 7 && l == 0 ? 15 : (j1 == 6 && l == 1 ? 15 : (j1 == 5 && l == 1 ? 15 : (j1 == 4 && l == 2 ? 15 : (j1 == 3 && l == 3 ? 15 : (j1 == 2 && l == 4 ? 15 : (j1 == 1 && l == 5 ? 15 : 0)))))));
        }
    }

    public boolean isPowerSource() {
        return true;
    }
}
