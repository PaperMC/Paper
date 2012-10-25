package net.minecraft.server;

import org.bukkit.craftbukkit.block.CraftBlockState; // CraftBukkit

public class ItemDoor extends Item {

    private Material a;

    public ItemDoor(int i, Material material) {
        super(i);
        this.a = material;
        this.maxStackSize = 1;
        this.a(CreativeModeTab.d);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
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
                    if (!place(world, i, j, k, i1, block, entityhuman)) {
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
        place(world, i, j, k, l, block, null);
    }

    public static boolean place(World world, int i, int j, int k, int l, Block block, EntityHuman entityhuman) {
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

        int i1 = (world.s(i - b0, j, k - b1) ? 1 : 0) + (world.s(i - b0, j + 1, k - b1) ? 1 : 0);
        int j1 = (world.s(i + b0, j, k + b1) ? 1 : 0) + (world.s(i + b0, j + 1, k + b1) ? 1 : 0);
        boolean flag = world.getTypeId(i - b0, j, k - b1) == block.id || world.getTypeId(i - b0, j + 1, k - b1) == block.id;
        boolean flag1 = world.getTypeId(i + b0, j, k + b1) == block.id || world.getTypeId(i + b0, j + 1, k + b1) == block.id;
        boolean flag2 = false;

        if (flag && !flag1) {
            flag2 = true;
        } else if (j1 > i1) {
            flag2 = true;
        }

        CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit
        world.suppressPhysics = true;
        world.setTypeIdAndData(i, j, k, block.id, l);
        // CraftBukkit start
        if (entityhuman != null) {
            org.bukkit.event.block.BlockPlaceEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, i, j, k);

            if (event.isCancelled() || !event.canBuild()) {
                event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                return false;
            }
        }
        // CraftBukkit end
        world.setTypeIdAndData(i, j + 1, k, block.id, 8 | (flag2 ? 1 : 0));
        world.suppressPhysics = false;
        world.applyPhysics(i, j, k, block.id);
        world.applyPhysics(i, j + 1, k, block.id);
        return true; // CraftBukkit
    }
}
