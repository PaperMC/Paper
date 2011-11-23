package net.minecraft.server;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockTrapdoor extends Block {

    protected BlockTrapdoor(int i, Material material) {
        super(i, material);
        this.textureId = 84;
        if (material == Material.ORE) {
            ++this.textureId;
        }

        float f = 0.5F;
        float f1 = 1.0F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int c() {
        return 0;
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        this.a((IBlockAccess)world, i, j, k); // CraftBukkit - Make sure this points to the below method!
        return super.e(world, i, j, k);
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        this.d(iblockaccess.getData(i, j, k));
    }

    public void f() {
        float f = 0.1875F;

        this.a(0.0F, 0.5F - f / 2.0F, 0.0F, 1.0F, 0.5F + f / 2.0F, 1.0F);
    }

    public void d(int i) {
        float f = 0.1875F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
        if (e(i)) {
            if ((i & 3) == 0) {
                this.a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
            }

            if ((i & 3) == 1) {
                this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
            }

            if ((i & 3) == 2) {
                this.a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            }

            if ((i & 3) == 3) {
                this.a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
            }
        }
    }

    public void b(World world, int i, int j, int k, EntityHuman entityhuman) {
        this.interact(world, i, j, k, entityhuman);
    }

    public boolean interact(World world, int i, int j, int k, EntityHuman entityhuman) {
        if (this.material == Material.ORE) {
            return true;
        } else {
            int l = world.getData(i, j, k);

            world.setData(i, j, k, l ^ 4);
            world.a(entityhuman, 1003, i, j, k, 0);
            return true;
        }
    }

    public void a(World world, int i, int j, int k, boolean flag) {
        int l = world.getData(i, j, k);
        boolean flag1 = (l & 4) > 0;

        if (flag1 != flag) {
            world.setData(i, j, k, l ^ 4);
            world.a((EntityHuman) null, 1003, i, j, k, 0);
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            int i1 = world.getData(i, j, k);
            int j1 = i;
            int k1 = k;

            if ((i1 & 3) == 0) {
                k1 = k + 1;
            }

            if ((i1 & 3) == 1) {
                --k1;
            }

            if ((i1 & 3) == 2) {
                j1 = i + 1;
            }

            if ((i1 & 3) == 3) {
                --j1;
            }

            if (!f(world.getTypeId(j1, j, k1))) {
                world.setTypeId(i, j, k, 0);
                this.b(world, i, j, k, i1, 0);
            }

            // CraftBukkit start
            if (l > 0) {
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);

                int power = block.getBlockPower();
                int oldPower = (world.getData(i, j, k) & 4) > 0 ? 15 : 0;

                if (oldPower == 0 ^ power == 0 || (Block.byId[l] != null && Block.byId[l].isPowerSource())) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    this.a(world, i, j, k, eventRedstone.getNewCurrent() > 0);
                }
                // CraftBukkit end
            }
        }
    }

    public MovingObjectPosition a(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
        this.a(world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }

    public void postPlace(World world, int i, int j, int k, int l) {
        byte b0 = 0;

        if (l == 2) {
            b0 = 0;
        }

        if (l == 3) {
            b0 = 1;
        }

        if (l == 4) {
            b0 = 2;
        }

        if (l == 5) {
            b0 = 3;
        }

        world.setData(i, j, k, b0);
        doPhysics(world, i, j, k, Block.REDSTONE_WIRE.id); // CraftBukkit
    }

    public boolean canPlace(World world, int i, int j, int k, int l) {
        if (l == 0) {
            return false;
        } else if (l == 1) {
            return false;
        } else {
            if (l == 2) {
                ++k;
            }

            if (l == 3) {
                --k;
            }

            if (l == 4) {
                ++i;
            }

            if (l == 5) {
                --i;
            }

            return f(world.getTypeId(i, j, k));
        }
    }

    public static boolean e(int i) {
        return (i & 4) != 0;
    }

    private static boolean f(int i) {
        if (i <= 0) {
            return false;
        } else {
            Block block = Block.byId[i];

            return block != null && block.material.j() && block.b() || block == Block.GLOWSTONE;
        }
    }
}
