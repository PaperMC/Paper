package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public abstract class DispenseBehaviorProjectile extends DispenseBehaviorItem {

    public DispenseBehaviorProjectile() {}

    @Override
    public ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
        WorldServer worldserver = isourceblock.getWorld();
        IPosition iposition = BlockDispenser.a(isourceblock);
        EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
        IProjectile iprojectile = this.a((World) worldserver, iposition, itemstack);

        // iprojectile.shoot((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ(), this.getPower(), this.a());
        // CraftBukkit start
        ItemStack itemstack1 = itemstack.cloneAndSubtract(1);
        org.bukkit.block.Block block = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack1);

        BlockDispenseEvent event = new BlockDispenseEvent(block, craftItem.clone(), new org.bukkit.util.Vector((double) enumdirection.getAdjacentX(), (double) ((float) enumdirection.getAdjacentY() + 0.1F), (double) enumdirection.getAdjacentZ()));
        if (!BlockDispenser.eventFired) {
            worldserver.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            itemstack.add(1);
            return itemstack;
        }

        if (!event.getItem().equals(craftItem)) {
            itemstack.add(1);
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
            if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                idispensebehavior.dispense(isourceblock, eventStack);
                return itemstack;
            }
        }

        iprojectile.shoot(event.getVelocity().getX(), event.getVelocity().getY(), event.getVelocity().getZ(), this.getPower(), this.a());
        ((Entity) iprojectile).projectileSource = new org.bukkit.craftbukkit.projectiles.CraftBlockProjectileSource((TileEntityDispenser) isourceblock.getTileEntity());
        // CraftBukkit end
        worldserver.addEntity(iprojectile);
        // itemstack.subtract(1); // CraftBukkit - Handled during event processing
        return itemstack;
    }

    @Override
    protected void a(ISourceBlock isourceblock) {
        isourceblock.getWorld().triggerEffect(1002, isourceblock.getBlockPosition(), 0);
    }

    protected abstract IProjectile a(World world, IPosition iposition, ItemStack itemstack);

    protected float a() {
        return 6.0F;
    }

    protected float getPower() {
        return 1.1F;
    }
}
