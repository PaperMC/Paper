package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemDoor extends Item {

    private Material a;

    public ItemDoor(int i, Material material) {
        super(i);
        this.a = material;
        this.maxStackSize = 1;
    }

    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
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

            if (entityhuman.c(i, j, k) && entityhuman.c(i, j + 1, k)) {
                if (!block.canPlace(world, i, j, k)) {
                    return false;
                } else {
                    int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;

                    // CraftBukkit start
                    if (a(world, i, j, k, i1, block, entityhuman)) {
                        --itemstack.count;
                        return true;
                    } else {
                        return false;
                    }
                    // CraftBukkit end
                }
            } else {
                return false;
            }
        }
    }

    // Craftbukkit - void -> boolean + entityhuman argument
    public static boolean a(World world, int i, int j, int k, int l, Block block, EntityHuman entityhuman) {
        int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit
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

        int i1 = (world.e(i - b0, j, k - b1) ? 1 : 0) + (world.e(i - b0, j + 1, k - b1) ? 1 : 0);
        int j1 = (world.e(i + b0, j, k + b1) ? 1 : 0) + (world.e(i + b0, j + 1, k + b1) ? 1 : 0);
        boolean flag = world.getTypeId(i - b0, j, k - b1) == block.id || world.getTypeId(i - b0, j + 1, k - b1) == block.id;
        boolean flag1 = world.getTypeId(i + b0, j, k + b1) == block.id || world.getTypeId(i + b0, j + 1, k + b1) == block.id;
        boolean flag2 = false;

        if (flag && !flag1) {
            flag2 = true;
        } else if (j1 > i1) {
            flag2 = true;
        }

        if (flag2) {
            l = l - 1 & 3;
            l += 4;
        }

        CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

        world.suppressPhysics = true;
        world.setTypeIdAndData(i, j, k, block.id, l);
        // CraftBukkit start - bed
        world.suppressPhysics = false;
        world.applyPhysics(i, j, k, Block.REDSTONE_WIRE.id);
        BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ, block);

        if (event.isCancelled() || !event.canBuild()) {
            event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
            return false;
        }

        world.suppressPhysics = true;
        // CraftBukkit end
        world.setTypeIdAndData(i, j + 1, k, block.id, l + 8);
        world.suppressPhysics = false;
        // world.applyPhysics(i, j, k, block.id); // CraftBukkit - moved up
        world.applyPhysics(i, j + 1, k, block.id);

        return true; // CraftBukkit
    }
}
