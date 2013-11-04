package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorEmptyBucket extends DispenseBehaviorItem {

    private final DispenseBehaviorItem b = new DispenseBehaviorItem();

    DispenseBehaviorEmptyBucket() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.b(isourceblock.h());
        World world = isourceblock.k();
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY() + enumfacing.d();
        int k = isourceblock.getBlockZ() + enumfacing.e();
        Material material = world.getType(i, j, k).getMaterial();
        int l = world.getData(i, j, k);
        Item item;

        if (Material.WATER.equals(material) && l == 0) {
            item = Items.WATER_BUCKET;
        } else {
            if (!Material.LAVA.equals(material) || l != 0) {
                return super.b(isourceblock, itemstack);
            }

            item = Items.LAVA_BUCKET;
        }

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(i, j, k));
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
        // CraftBukkit end

        world.setAir(i, j, k);
        if (--itemstack.count == 0) {
            itemstack.setItem(item);
            itemstack.count = 1;
        } else if (((TileEntityDispenser) isourceblock.getTileEntity()).addItem(new ItemStack(item)) < 0) {
            this.b.a(isourceblock, new ItemStack(item));
        }

        return itemstack;
    }
}
