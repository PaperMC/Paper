package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.block.BlockDispenseEvent;
// CraftBukkit end

public class DispenseBehaviorShears extends DispenseBehaviorMaybe {

    public DispenseBehaviorShears() {}

    @Override
    protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
        WorldServer worldserver = isourceblock.getWorld();
        // CraftBukkit start
        org.bukkit.block.Block bukkitBlock = worldserver.getWorld().getBlockAt(isourceblock.getBlockPosition().getX(), isourceblock.getBlockPosition().getY(), isourceblock.getBlockPosition().getZ());
        CraftItemStack craftItem = CraftItemStack.asCraftMirror(itemstack);

        BlockDispenseEvent event = new BlockDispenseEvent(bukkitBlock, craftItem.clone(), new org.bukkit.util.Vector(0, 0, 0));
        if (!BlockDispenser.eventFired) {
            worldserver.getServer().getPluginManager().callEvent(event);
        }

        if (event.isCancelled()) {
            return itemstack;
        }

        if (!event.getItem().equals(craftItem)) {
            // Chain to handler for new item
            ItemStack eventStack = CraftItemStack.asNMSCopy(event.getItem());
            IDispenseBehavior idispensebehavior = (IDispenseBehavior) BlockDispenser.REGISTRY.get(eventStack.getItem());
            if (idispensebehavior != IDispenseBehavior.NONE && idispensebehavior != this) {
                idispensebehavior.dispense(isourceblock, eventStack);
                return itemstack;
            }
        }
        // CraftBukkit end

        if (!worldserver.s_()) {
            BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));

            this.a(a((WorldServer) worldserver, blockposition) || b((WorldServer) worldserver, blockposition, bukkitBlock, craftItem)); // CraftBukkit
            if (this.a() && itemstack.isDamaged(1, worldserver.getRandom(), (EntityPlayer) null)) {
                itemstack.setCount(0);
            }
        }

        return itemstack;
    }

    private static boolean a(WorldServer worldserver, BlockPosition blockposition) {
        IBlockData iblockdata = worldserver.getType(blockposition);

        if (iblockdata.a((Tag) TagsBlock.BEEHIVES)) {
            int i = (Integer) iblockdata.get(BlockBeehive.b);

            if (i >= 5) {
                worldserver.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_BEEHIVE_SHEAR, SoundCategory.BLOCKS, 1.0F, 1.0F);
                BlockBeehive.a((World) worldserver, blockposition);
                ((BlockBeehive) iblockdata.getBlock()).a(worldserver, iblockdata, blockposition, (EntityHuman) null, TileEntityBeehive.ReleaseStatus.BEE_RELEASED);
                return true;
            }
        }

        return false;
    }

    private static boolean b(WorldServer worldserver, BlockPosition blockposition, org.bukkit.block.Block bukkitBlock, CraftItemStack craftItem) { // CraftBukkit - add args
        List<EntityLiving> list = worldserver.a(EntityLiving.class, new AxisAlignedBB(blockposition), IEntitySelector.g);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityLiving entityliving = (EntityLiving) iterator.next();

            if (entityliving instanceof IShearable) {
                IShearable ishearable = (IShearable) entityliving;

                if (ishearable.canShear()) {
                    // CraftBukkit start
                    if (CraftEventFactory.callBlockShearEntityEvent(entityliving, bukkitBlock, craftItem).isCancelled()) {
                        continue;
                    }
                    // CraftBukkit end
                    ishearable.shear(SoundCategory.BLOCKS);
                    return true;
                }
            }
        }

        return false;
    }
}
