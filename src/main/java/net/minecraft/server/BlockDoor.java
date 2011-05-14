package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.block.BlockRedstoneEvent;
// CraftBukkit end

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
            int k = this.d(j);

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

    public AxisAlignedBB d(World world, int i, int j, int k) {
        this.a(world, i, j, k);
        return super.d(world, i, j, k);
    }

    public void a(IBlockAccess iblockaccess, int i, int j, int k) {
        this.c(this.d(iblockaccess.getData(i, j, k)));
    }

    public void c(int i) {
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
                if (Math.random() < 0.5D) {
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.door_open", 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                } else {
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.door_close", 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }

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
                if (Math.random() < 0.5D) {
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.door_open", 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                } else {
                    world.makeSound((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D, "random.door_close", 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }
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

            if (!world.d(i, j - 1, k)) {
                world.setTypeId(i, j, k, 0);
                flag = true;
                if (world.getTypeId(i, j + 1, k) == this.id) {
                    world.setTypeId(i, j + 1, k, 0);
                }
            }

            if (flag) {
                if (!world.isStatic) {
                    this.a_(world, i, j, k, i1);
                }
            } else if (l > 0 && Block.byId[l].isPowerSource()) {
                boolean flag1 = world.isBlockIndirectlyPowered(i, j, k) || world.isBlockIndirectlyPowered(i, j + 1, k);

                // CraftBukkit start
                CraftWorld craftWorld = ((WorldServer) world).getWorld();
                CraftServer server = ((WorldServer) world).getServer();
                org.bukkit.block.Block block = craftWorld.getBlockAt(i, j, k);
                org.bukkit.block.Block blockTop = craftWorld.getBlockAt(i, j + 1, k);
                int power = block.getBlockPower();
                int powerTop = blockTop.getBlockPower();
                if (powerTop > power) power = powerTop;

                BlockRedstoneEvent eventRedstone = new BlockRedstoneEvent(block, (world.getData(i, j, k) & 4) > 0 ? 15: 0, power);
                server.getPluginManager().callEvent(eventRedstone);

                flag1 = eventRedstone.getNewCurrent() > 0;
                // CraftBukkit end

                this.setDoor(world, i, j, k, flag1);
            }
        }
    }

    public int a(int i, Random random) {
        return (i & 8) != 0 ? 0 : (this.material == Material.ORE ? Item.IRON_DOOR.id : Item.WOOD_DOOR.id);
    }

    public MovingObjectPosition a(World world, int i, int j, int k, Vec3D vec3d, Vec3D vec3d1) {
        this.a(world, i, j, k);
        return super.a(world, i, j, k, vec3d, vec3d1);
    }

    public int d(int i) {
        return (i & 4) == 0 ? i - 1 & 3 : i & 3;
    }

    public boolean canPlace(World world, int i, int j, int k) {
        return j >= 127 ? false : world.d(i, j - 1, k) && super.canPlace(world, i, j, k) && super.canPlace(world, i, j + 1, k);
    }

    public static boolean e(int i) {
        return (i & 4) != 0;
    }
}
