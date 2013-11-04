package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorFilledBucket extends DispenseBehaviorItem {

    private final DispenseBehaviorItem b = new DispenseBehaviorItem();

    DispenseBehaviorFilledBucket() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        ItemBucket itembucket = (ItemBucket) itemstack.getItem();
        int i = isourceblock.getBlockX();
        int j = isourceblock.getBlockY();
        int k = isourceblock.getBlockZ();
        EnumFacing enumfacing = BlockDispenser.b(isourceblock.h());

        // CraftBukkit start
        World world = isourceblock.k();
        int x = i + enumfacing.c();
        int y = j + enumfacing.d();
        int z = k + enumfacing.e();
        if (world.isEmpty(x, y, z) || !world.getType(x, y, z).getMaterial().isBuildable()) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(x, y, z));
            if (!BlockDispenser.eventFired) {
                world.getServer().getPluginManager().callEvent(event);
            }

            if (event.isCancelled()) {
                return itemstack;
            }

            if (!event.getItem().equals(craftItem)) {
                // Chain to handler for new item
                ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
                IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.a.a(eventStack.getItem());
                if (idispensebehavior != IDispenseBehavior.a && idispensebehavior != this) {
                    idispensebehavior.a(isourceblock, eventStack);
                    return itemstack;
                }
            }

            itembucket = (ItemBucket) CraftItemStack.asNMSCopy(event.getItem()).getItem();
        }
        // CraftBukkit end

        if (itembucket.a(isourceblock.k(), i + enumfacing.c(), j + enumfacing.d(), k + enumfacing.e())) {
            // CraftBukkit start - Handle stacked buckets
            Item item = Items.BUCKET;
            if (--itemstack.count == 0) {
                itemstack.setItem(Items.BUCKET);
                itemstack.count = 1;
            } else if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
                this.b.a(isourceblock, new ItemStack(item));
            }
            // CraftBukkit end

            return itemstack;
        } else {
            return this.b.a(isourceblock, itemstack);
        }
    }
}
