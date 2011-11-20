package net.minecraft.server;

import java.util.Random;

import org.bukkit.event.block.BlockRedstoneEvent; // CraftBukkit

public class BlockDoor extends Block {

    protected BlockDoor(int i, Material material) {
        super(i, material);
        this.textureId = 97;
        if (material == Material.ORE) {
            ++this.textureId;
        }

        float f = 0.5F;
        float f1 = 1.0F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
    }

    public int a(int i, int j) {
        if (i != 0 && i != 1) {
            int k = this.e(j);

            if ((k == 0 || k == 2) ^ i <= 3) {
                return this.textureId;
            } else {
                int l = k / 2 + (i & 1 ^ k);

                l += (j & 4) / 4;
                int i1 = this.textureId - (j & 8) * 2;

                if ((l & 1) != 0) {
                    i1 = -i1;
                }

                return i1;
            }
        } else {
            return this.textureId;
        }
    }

    public boolean a() {
        return false;
    }

    public boolean b() {
        return false;
    }

    public int c() {
        return 7;
    }

    public AxisAlignedBB e(World world, int i, int j, int k) {
        this.a((IBlockAccess)world, i, j, k); // CraftBukkit - Make sure this points to the below method!
        return super.e(world, i, j, k);
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        this.d(this.e(iblockaccess.getData(i, j, k)));
    }

    public void d(int i) {
        float f = 0.1875F;

        this.a(0.0F, 0.0F, 0.0F, 1.0F, 2.0F, 1.0F);
        if (i == 0) {
            this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
        }

        if (i == 1) {
            this.a(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }

        if (i == 2) {
            this.a(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
        }

        if (i == 3) {
            this.a(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
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

            if ((l & 8) != 0) {
                if (world.getTypeId(i, j - 1, k) == this.id) {
                    this.interact(world, i, j - 1, k, entityhuman);
                }

                return true;
            } else {
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setData(i, j + 1, k, (l ^ 4) + 8);
                }

                world.setData(i, j, k, l ^ 4);
                world.b(i, j - 1, k, i, j, k);
                world.a(entityhuman, 1003, i, j, k, 0);
                return true;
            }
        }
    }

    public void setDoor(World world, int i, int j, int k, boolean flag) {
        int l = world.getData(i, j, k);

        if ((l & 8) != 0) {
            if (world.getTypeId(i, j - 1, k) == this.id) {
                this.setDoor(world, i, j - 1, k, flag);
            }
        } else {
            boolean flag1 = (world.getData(i, j, k) & 4) > 0;

            if (flag1 != flag) {
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setData(i, j + 1, k, (l ^ 4) + 8);
                }

                world.setData(i, j, k, l ^ 4);
                world.b(i, j - 1, k, i, j, k);
                world.a((EntityHuman) null, 1003, i, j, k, 0);
            }
        }
    }

    public void doPhysics(World world, int i, int j, int k, int l) {
        int i1 = world.getData(i, j, k);

        if ((i1 & 8) != 0) {
            if (world.getTypeId(i, j - 1, k) != this.id) {
                world.setTypeId(i, j, k, 0);
            }

            if (l > 0 && Block.byId[l].isPowerSource()) {
                this.doPhysics(world, i, j - 1, k, l);
            }
        } else {
            boolean flag = false;

            if (world.getTypeId(i, j + 1, k) != this.id) {
                world.setTypeId(i, j, k, 0);
                flag = true;
            }

            if (!world.e(i, j - 1, k)) {
                world.setTypeId(i, j, k, 0);
                flag = true;
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setTypeId(i, j + 1, k, 0);
                }
            }

            if (flag) {
                if (!world.isStatic) {
                    this.b(world, i, j, k, i1, 0);
                }
            } else if (l > 0 && Block.byId[l].isPowerSource()) {
                // CraftBukkit start
                org.bukkit.World bworld = world.getWorld();
                org.bukkit.block.Block block = bworld.getBlockAt(i, j, k);
                org.bukkit.block.Block blockTop = bworld.getBlockAt(i, j + 1, k);

                int power = block.getBlockPower();
                int powerTop = blockTop.getBlockPower();
                if (powerTop > power) power = powerTop;
                int oldPower = (world.getData(i, j, k) & 4) > 0 ? 15 : 0;

                if (oldPower == 0 ^ power == 0) {
                    BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, oldPower, power);
                    world.getServer().getPluginManager().callEvent(eventRedstone);

                    this.setDoor(world, i, j, k, eventRedstone.getNewCurrent() > 0);
                }
                // CraftBukkit end
            }
        }
    }

    public int a(int i, Random random, int j) {
        return (i & 8) != 0 ? 0 : (this.material == Material.ORE ? Item.IRON_DOOR.id : Item.WOOD_DOOR.id);
    }

    public MovingObjectPosition a(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
        this.a(world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }

    public int e(int i) {
        return (i & 4) == 0 ? i - 1 & 3 : i & 3;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return j >= world.height - 1 ? false : world.e(i, j - 1, k) && super.canPlace(world, i, j, k) && super.canPlace(world, i, j + 1, k);
    }

    public static boolean f(int i) {
        return (i & 4) != 0;
    }

    public int g() {
        return 1;
    }
}
