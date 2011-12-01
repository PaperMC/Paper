package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.block.BlockPlaceEvent;
// CraftBukkit end

public class ItemWaterLily extends ItemColoredBlock {

    public ItemWaterLily(int i) {
        super(i, false);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        MovingObjectPosition movingobjectposition = this.a(world, entityhuman, true);

        if (movingobjectposition == null) {
            return itemstack;
        } else {
            if (movingobjectposition.type == EnumMovingObjectType.TILE) {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                if (!world.a(entityhuman, i, j, k)) {
                    return itemstack;
                }

                if (!entityhuman.d(i, j, k)) {
                    return itemstack;
                }

                if (world.getMaterial(i, j, k) == Material.WATER && world.getData(i, j, k) == 0 && world.isEmpty(i, j + 1, k)) {
                    CraftBlockState blockState = CraftBlockState.getBlockState(world, i, j + 1, k); // CraftBukkit

                    world.setTypeId(i, j + 1, k, Block.WATER_LILY.id);

                    // CraftBukkit start - waterlily
                    BlockPlaceEvent event = CraftEventFactory.callBlockPlaceEvent(world, entityhuman, blockState, i, j, k, Block.WATER_LILY.id);

                    if (event.isCancelled() || !event.canBuild()) {
                        event.getBlockPlaced().setTypeId(0);
                        return itemstack;
                    }
                    // CraftBukkit end

                    if (!entityhuman.abilities.canInstantlyBuild) {
                        --itemstack.count;
                    }
                }
            }

            return itemstack;
        }
    }
}
