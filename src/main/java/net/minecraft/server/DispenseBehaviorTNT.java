package net.minecraft.server;

final class DispenseBehaviorTNT extends DispenseBehaviorItem {

    DispenseBehaviorTNT() {}

    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.j_(isourceblock.h());
        World world = isourceblock.k();
        int i = isourceblock.getBlockX() + enumfacing.c();
        int j = isourceblock.getBlockY() + enumfacing.d();
        int k = isourceblock.getBlockZ() + enumfacing.e();
        EntityTNTPrimed entitytntprimed = new EntityTNTPrimed(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), (EntityLiving) null);

        world.addEntity(entitytntprimed);
        --itemstack.count;
        return itemstack;
    }
}
