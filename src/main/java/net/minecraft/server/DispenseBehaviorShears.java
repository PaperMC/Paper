package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class DispenseBehaviorShears extends DispenseBehaviorMaybe {

    public DispenseBehaviorShears() {}

    @Override
    protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
        WorldServer worldserver = isourceblock.getWorld();

        if (!worldserver.s_()) {
            BlockPosition blockposition = isourceblock.getBlockPosition().shift((EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));

            this.a(a((WorldServer) worldserver, blockposition) || b((WorldServer) worldserver, blockposition));
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

    private static boolean b(WorldServer worldserver, BlockPosition blockposition) {
        List<EntityLiving> list = worldserver.a(EntityLiving.class, new AxisAlignedBB(blockposition), IEntitySelector.g);
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            EntityLiving entityliving = (EntityLiving) iterator.next();

            if (entityliving instanceof IShearable) {
                IShearable ishearable = (IShearable) entityliving;

                if (ishearable.canShear()) {
                    ishearable.shear(SoundCategory.BLOCKS);
                    return true;
                }
            }
        }

        return false;
    }
}
