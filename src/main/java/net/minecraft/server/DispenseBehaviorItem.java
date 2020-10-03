package net.minecraft.server;

public class DispenseBehaviorItem implements IDispenseBehavior {

    public DispenseBehaviorItem() {}

    @Override
    public final ItemStack dispense(ISourceBlock isourceblock, ItemStack itemstack) {
        ItemStack itemstack1 = this.a(isourceblock, itemstack);

        this.a(isourceblock);
        this.a(isourceblock, (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING));
        return itemstack1;
    }

    protected ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumDirection enumdirection = (EnumDirection) isourceblock.getBlockData().get(BlockDispenser.FACING);
        IPosition iposition = BlockDispenser.a(isourceblock);
        ItemStack itemstack1 = itemstack.cloneAndSubtract(1);

        a(isourceblock.getWorld(), itemstack1, 6, enumdirection, iposition);
        return itemstack;
    }

    public static void a(World world, ItemStack itemstack, int i, EnumDirection enumdirection, IPosition iposition) {
        double d0 = iposition.getX();
        double d1 = iposition.getY();
        double d2 = iposition.getZ();

        if (enumdirection.n() == EnumDirection.EnumAxis.Y) {
            d1 -= 0.125D;
        } else {
            d1 -= 0.15625D;
        }

        EntityItem entityitem = new EntityItem(world, d0, d1, d2, itemstack);
        double d3 = world.random.nextDouble() * 0.1D + 0.2D;

        entityitem.setMot(world.random.nextGaussian() * 0.007499999832361937D * (double) i + (double) enumdirection.getAdjacentX() * d3, world.random.nextGaussian() * 0.007499999832361937D * (double) i + 0.20000000298023224D, world.random.nextGaussian() * 0.007499999832361937D * (double) i + (double) enumdirection.getAdjacentZ() * d3);
        world.addEntity(entityitem);
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.getWorld().triggerEffect(1000, isourceblock.getBlockPosition(), 0);
    }

    protected void a(ISourceBlock isourceblock, EnumDirection enumdirection) {
        isourceblock.getWorld().triggerEffect(2000, isourceblock.getBlockPosition(), enumdirection.c());
    }
}
