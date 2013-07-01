package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

final class DispenseBehaviorMinecart extends DispenseBehaviorItem {

    private final DispenseBehaviorItem b = new DispenseBehaviorItem();

    DispenseBehaviorMinecart() {}

    public ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.l_(isourceblock.h());
        World world = isourceblock.k();
        double d0 = isourceblock.getX() + (double) ((float) enumfacing.c() * 1.125F);
        double d1 = isourceblock.getY() + (double) ((float) enumfacing.d() * 1.125F);
        double d2 = isourceblock.getZ() + (double) ((float) enumfacing.e() * 1.125F);
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY() + enumfacing.d();
        int k = isourceblock.getBlockZ() + enumfacing.e();
        int l = world.getTypeId(i, j, k);
        double d3;

        if (BlockMinecartTrackAbstract.e_(l)) {
            d3 = 0.0D;
        } else {
            if (l != 0 || !BlockMinecartTrackAbstract.e_(world.getTypeId(i, j - 1, k))) {
                return this.b.a(isourceblock, itemstack);
            }

            d3 = -1.0D;
        }

        // CraftBukkit start
        ItemStack itemstack1 = itemstack.a(1);
        org.bukkit.block.Block block = world.getWorld().getBlockAt(isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector(d0, d1 + d3, d2));
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
        EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), ((ItemMinecart) itemstack1.getItem()).a);
        // CraftBukkit end

        if (itemstack.hasName()) {
            entityminecartabstract.a(itemstack.getName());
        }

        world.addEntity(entityminecartabstract);
        // itemstack.a(1); // CraftBukkit - handled during event processing
        return itemstack;
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.k().triggerEffect(1000, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
    }
}
