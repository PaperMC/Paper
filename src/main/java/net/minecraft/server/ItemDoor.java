package net.minecraft.server;

public class ItemDoor extends Item {

    private Material a;

    public ItemDoor(int i, Material material) {
        super(i);
        this.a = material;
        this.maxStackSize = 1;
        this.a(CreativeModeTab.d);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        final int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
        if (l != 1) {
            return false;
        } else {
            ++j;
            Block block;

            if (this.a == Material.WOOD) {
                block = Block.WOODEN_DOOR;
            } else {
                block = Block.IRON_DOOR_BLOCK;
            }

            if (entityhuman.a(i, j, k, l, itemstack) && entityhuman.a(i, j + 1, k, l, itemstack)) {
                if (!block.canPlace(world, i, j, k)) {
                    return false;
                } else {
                    int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;

                    // CraftBukkit start
                    if (!place(world, i, j, k, i1, block, entityhuman, clickedX, clickedY, clickedZ)) {
                        return false;
                    }
                    // CraftBukkit end

                    --itemstack.count;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static void place(World world, int i, int j, int k, int l, Block block) {
        // CraftBukkit start
        place(world, i, j, k, l, block, null, i, j, k);
    }

    public static boolean place(World world, int i, int j, int k, int l, Block block, EntityHuman entityhuman, int clickedX, int clickedY, int clickedZ) {
        // CraftBukkit end
        byte b0 = 0;
        byte b1 = 0;

        if (l == 0) {
            b1 = 1;
        }

        if (l == 1) {
            b0 = -1;
        }

        if (l == 2) {
            b1 = -1;
        }

        if (l == 3) {
            b0 = 1;
        }

        int i1 = (world.u(i - b0, j, k - b1) ? 1 : 0) + (world.u(i - b0, j + 1, k - b1) ? 1 : 0);
        int j1 = (world.u(i + b0, j, k + b1) ? 1 : 0) + (world.u(i + b0, j + 1, k + b1) ? 1 : 0);
        boolean flag = world.getTypeId(i - b0, j, k - b1) == block.id || world.getTypeId(i - b0, j + 1, k - b1) == block.id;
        boolean flag1 = world.getTypeId(i + b0, j, k + b1) == block.id || world.getTypeId(i + b0, j + 1, k + b1) == block.id;
        boolean flag2 = false;

        if (flag && !flag1) {
            flag2 = true;
        } else if (j1 > i1) {
            flag2 = true;
        }

        // CraftBukkit start
        if (entityhuman != null) {
            if (!ItemBlock.processBlockPlace(world, entityhuman, null, i, j, k, block.id, l, clickedX, clickedY, clickedZ)) {
                ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet53BlockChange(i, j + 1, k, world));
                return false;
            }

            if (world.getTypeId(i, j, k) != block.id) {
                ((EntityPlayer) entityhuman).playerConnection.sendPacket(new Packet53BlockChange(i, j + 1, k, world));
                return true;
            }
        } else {
            world.setTypeIdAndData(i, j, k, block.id, l, 2);
        }
        // CraftBukkit end
        world.setTypeIdAndData(i, j + 1, k, block.id, 8 | (flag2 ? 1 : 0), 2);
        world.applyPhysics(i, j, k, block.id);
        world.applyPhysics(i, j + 1, k, block.id);
        return true; // CraftBukkit
    }
}
