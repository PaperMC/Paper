package net.minecraft.server;

final class DispenseBehaviorBonemeal extends DispenseBehaviorItem {

    private boolean b = true;

    DispenseBehaviorBonemeal() {}

    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        if (itemstack.getData() == 15) {
            EnumFacing enumfacing = BlockDispenser.j_(isourceblock.h());
            World world = isourceblock.k();
            int i = isourceblock.getBlockX() + enumfacing.c();
            int j = isourceblock.getBlockY() + enumfacing.d();
            int k = isourceblock.getBlockZ() + enumfacing.e();

            if (ItemDye.a(itemstack, world, i, j, k)) {
                if (!world.isStatic) {
                    world.triggerEffect(2005, i, j, k, 0);
                }
            } else {
                this.b = false;
            }

            return itemstack;
        } else {
            return super.b(isourceblock, itemstack);
        }
    }

    protected void a(ISourceBlock isourceblock) {
        if (this.b) {
            isourceblock.k().triggerEffect(1000, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
        } else {
            isourceblock.k().triggerEffect(1001, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
        }
    }
}
