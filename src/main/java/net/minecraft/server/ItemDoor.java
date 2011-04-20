package net.minecraft.server;

// CraftBukkit start
import org.bukkit.block.BlockState;
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
            int clickedX = i, clickedY = j, clickedZ = k; // CraftBukkit;

            ++j;
            Block block;

            if (this.a == Material.WOOD) {
                block = Block.WOODEN_DOOR;
            } else {
                block = Block.IRON_DOOR_BLOCK;
            }

            if (!block.canPlace(world, i, j, k)) {
                return false;
            } else {
                int i1 = MathHelper.floor((double) ((entityhuman.yaw + 180.0F) * 4.0F / 360.0F) - 0.5D) & 3;
                byte b0 = 0;
                byte b1 = 0;

                if (i1 == 0) {
                    b1 = 1;
                }

                if (i1 == 1) {
                    b0 = -1;
                }

                if (i1 == 2) {
                    b1 = -1;
                }

                if (i1 == 3) {
                    b0 = 1;
                }

                int j1 = (world.d(i - b0, j, k - b1) ? 1 : 0) + (world.d(i - b0, j + 1, k - b1) ? 1 : 0);
                int k1 = (world.d(i + b0, j, k + b1) ? 1 : 0) + (world.d(i + b0, j + 1, k + b1) ? 1 : 0);
                boolean flag = world.getTypeId(i - b0, j, k - b1) == block.id || world.getTypeId(i - b0, j + 1, k - b1) == block.id;
                boolean flag1 = world.getTypeId(i + b0, j, k + b1) == block.id || world.getTypeId(i + b0, j + 1, k + b1) == block.id;
                boolean flag2 = false;

                if (flag && !flag1) {
                    flag2 = true;
                } else if (k1 > j1) {
                    flag2 = true;
                }

                if (flag2) {
                    i1 = i1 - 1 & 3;
                    i1 += 4;
                }

                BlockState blockState = CraftBlockState.getBlockState(world, i, j, k); // CraftBukkit

                world.setTypeId(i, j, k, block.id);
                world.setData(i, j, k, i1);

                // CraftBukkit start - bed
                BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, clickedX, clickedY, clickedZ, block);

                if (event.isCancelled() || !event.canBuild()) {
                    event.getBlockPlaced().setTypeIdAndData(blockState.getTypeId(), blockState.getRawData(), false);
                    return false;
                }
                // CraftBukkit end

                world.setTypeId(i, j + 1, k, block.id);
                world.setData(i, j + 1, k, i1 + 8);
                --itemstack.count;
                return true;
            }
        }
    }
}
