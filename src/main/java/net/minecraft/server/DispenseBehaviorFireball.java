package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorFireball extends DispenseBehaviorItem {

    DispenseBehaviorFireball() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.b(isourceblock.h());
        IPosition iposition = BlockDispenser.a(isourceblock);
        double d0 = iposition.getX() + (double) ((float) enumfacing.c() * 0.3F);
        double d1 = iposition.getY() + (double) ((float) enumfacing.c() * 0.3F);
        double d2 = iposition.getZ() + (double) ((float) enumfacing.e() * 0.3F);
        World world = isourceblock.k();
        Random random = world.random;
        double d3 = random.nextGaussian() * 0.05D + (double) enumfacing.c();
        double d4 = random.nextGaussian() * 0.05D + (double) enumfacing.d();
        double d5 = random.nextGaussian() * 0.05D + (double) enumfacing.e();

        // CraftBukkit start
        ItemStack itemstack1 = itemstack.a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d3, d4, d5));
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

        world.addEntity(new EntitySmallFireball(world, d0, d1, d2, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ()));
        // itemstack.a(1); // Handled during event processing
        // CraftBukkit end

        return itemstack;
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.k().triggerEffect(1009, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
    }
}
