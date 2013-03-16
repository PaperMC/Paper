package net.minecraft.server;

import java.util.List;

final class DispenseBehaviorArmor extends DispenseBehaviorItem {

    DispenseBehaviorArmor() {}

    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.j_(isourceblock.h());
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY() + enumfacing.d();
        int k = isourceblock.getBlockZ() + enumfacing.e();
        AxisAlignedBB axisalignedbb = AxisAlignedBB.a().a((double) i, (double) j, (double) k, (double) (i + 1), (double) (j + 1), (double) (k + 1));
        List list = isourceblock.k().a(EntityLiving.class, axisalignedbb, (IEntitySelector) (new EntitySelectorEquipable(itemstack)));

        if (list.size() > 0) {
            EntityLiving entityliving = (EntityLiving) list.get(0);
            int l = entityliving instanceof EntityHuman ? 1 : 0;
            int i1 = EntityLiving.b(itemstack);
            ItemStack itemstack1 = itemstack.cloneItemStack();

            itemstack1.count = 1;
            entityliving.setEquipment(i1 - l, itemstack1);
            entityliving.a(i1, 2.0F);
            --itemstack.count;
            return itemstack;
        } else {
            return super.b(isourceblock, itemstack);
        }
    }
}
