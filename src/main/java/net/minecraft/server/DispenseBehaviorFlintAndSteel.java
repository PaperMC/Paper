package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorFlintAndSteel extends DispenseBehaviorItem {

    private boolean b = true;

    DispenseBehaviorFlintAndSteel() {}

    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.l_(isourceblock.h());
        World world = isourceblock.k();
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY() + enumfacing.d();
        int k = isourceblock.getBlockZ() + enumfacing.e();

        // CraftBukkit start
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
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
        // CraftBukkit end

        if (world.isEmpty(i, j, k)) {
            // CraftBukkit start - Ignition by dispensing flint and steel
            if (!org.bukkit.craftbukkit.event.CraftEventFactory.callBlockIgniteEvent(world, i, j, k, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ()).isCancelled()) {
                world.setTypeIdUpdate(i, j, k, Block.FIRE.id);
                if (itemstack.isDamaged(1, world.random)) {
                    itemstack.count = 0;
                }
            }
            // CraftBukkit end
        } else if (world.getTypeId(i, j, k) == Block.TNT.id) {
            Block.TNT.postBreak(world, i, j, k, 1);
            world.setAir(i, j, k);
        } else {
            this.b = false;
        }

        return itemstack;
    }

    protected void a(ISourceBlock isourceblock) {
        if (this.b) {
            isourceblock.k().triggerEffect(1000, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
        } else {
            isourceblock.k().triggerEffect(1001, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
        }
    }
}
