package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorMonsterEgg extends DispenseBehaviorItem {

    DispenseBehaviorMonsterEgg() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.l_(isourceblock.h());
        double d0 = isourceblock.getX() + (double) enumfacing.c();
        double d1 = (double) ((float) isourceblock.getBlockY() + 0.2F);
        double d2 = isourceblock.getZ() + (double) enumfacing.e();

        // CraftBukkit start
        World world = isourceblock.k();
        ItemStack itemstack1 = itemstack.a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1, d2));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.count++;
            return itemstack;
        }

        if (!event.getItem().equals(craftItem)) {
            itemstack.count++;
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.a.a(eventStack.getItem());
            if (idispensebehavior != IDispenseBehavior.a && idispensebehavior != this) {
                idispensebehavior.a(isourceblock, eventStack);
                return itemstack;
            }
        }

        itemstack1 = CraftItemStack.asNMSCopy(event.getItem());

        Entity entity = ItemMonsterEgg.a(isourceblock.k(), itemstack.getData(), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());

        if (entity instanceof EntityLiving && itemstack.hasName()) {
            ((EntityInsentient) entity).setCustomName(itemstack.getName());
        }

        // itemstack.a(1); // Handled during event processing
        // CraftBukkit end
        return itemstack;
    }
}
