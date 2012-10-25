package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class DispenseBehaviorMonsterEgg extends DispenseBehaviorItem {

    final MinecraftServer b;

    public DispenseBehaviorMonsterEgg(MinecraftServer minecraftserver) {
        this.b = minecraftserver;
    }

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = EnumFacing.a(isourceblock.h());
        double d0 = isourceblock.getX() + (double) enumfacing.c();
        double d1 = (double) ((float) isourceblock.getBlockY() + 0.2F);
        double d2 = isourceblock.getZ() + (double) enumfacing.e();

        // CraftBukkit start
        World world = isourceblock.k();
        ItemStack itemstack1 = itemstack.a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        org.bukkit.inventory.ItemStack bukkitItem = new CraftItemStack(itemstack1).clone();

        BlockDispenseEvent event = new BlockDispenseEvent(block, bukkitItem, new org.bukkit.util.Vector(d0, d1, d2));
        if (!BlockDispenser.eventFired) {
            world.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.count++;
            return itemstack;
        }

        if (!event.getItem().equals(bukkitItem)) {
            itemstack.count++;
            // Chain to handler for new item
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.a.a(itemstack.getItem());
            if (idispensebehavior != IDispenseBehavior.a && idispensebehavior != this) {
                idispensebehavior.a(isourceblock, CraftItemStack.createNMSItemStack(event.getItem()));
                return itemstack;
            }
        }

        itemstack1 = CraftItemStack.createNMSItemStack(event.getItem());
        ItemMonsterEgg.a(isourceblock.k(), itemstack1.getData(), event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ());
        // itemstack.a(1); // Handled during event processing
        // CraftBukkit end
        return itemstack;
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.k().triggerEffect(1002, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
    }
}
