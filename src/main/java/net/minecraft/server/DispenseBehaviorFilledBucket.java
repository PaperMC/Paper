package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class DispenseBehaviorFilledBucket extends DispenseBehaviorItem {

    private final DispenseBehaviorItem c;

    final MinecraftServer b;

    public DispenseBehaviorFilledBucket(MinecraftServer minecraftserver) {
        this.b = minecraftserver;
        this.c = new DispenseBehaviorItem();
    }

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        ItemBucket itembucket = (ItemBucket) itemstack.getItem();
        int i = isourceblock.getBlockX();
        int j = isourceblock.getBlockY();
        int k = isourceblock.getBlockZ();
        EnumFacing enumfacing = EnumFacing.a(isourceblock.h());

        // CraftBukkit start
        World world = isourceblock.k();
        int i2 = i + enumfacing.c();
        int k2 = k + enumfacing.e();
        if (world.isEmpty(i2, j, k2) || world.getMaterial(i2, j, k2).isBuildable()) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(i, j, k);
            CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

            BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
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

        if (itembucket.a(isourceblock.k(), (double) i, (double) j, (double) k, i + enumfacing.c(), j, k + enumfacing.e())) {
            // CraftBukkit start - handle stacked buckets
            Item item = Item.BUCKET;
            if (--itemstack.count == 0) {
                itemstack.id = item.id;
                itemstack.count = 1;
            } else if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
                this.c.a(isourceblock, new ItemStack(item));
            }
            // CraftBukkit end

            return itemstack;
        } else {
            return this.c.a(isourceblock, itemstack);
        }
    }
}
