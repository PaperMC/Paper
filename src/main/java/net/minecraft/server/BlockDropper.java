package net.minecraft.server;

public class BlockDropper extends BlockDispenser {

    private final IDispenseBehavior cR = new DispenseBehaviorItem();

    protected BlockDropper(int i) {
        super(i);
    }

    protected IDispenseBehavior a(ItemStack itemstack) {
        return this.cR;
    }

    public TileEntity b(World world) {
        return new TileEntityDropper();
    }

    public void dispense(World world, int i, int j, int k) { // CraftBukkit - protected -> public
        SourceBlock sourceblock = new SourceBlock(world, i, j, k);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getTileEntity();

        if (tileentitydispenser != null) {
            int l = tileentitydispenser.j();

            if (l < 0) {
                world.triggerEffect(1001, i, j, k, 0);
            } else {
                ItemStack itemstack = tileentitydispenser.getItem(l);
                int i1 = world.getData(i, j, k) & 7;
                IInventory iinventory = TileEntityHopper.getInventoryAt(world, (double) (i + Facing.b[i1]), (double) (j + Facing.c[i1]), (double) (k + Facing.d[i1]));
                ItemStack itemstack1;

                if (iinventory != null) {
                    itemstack1 = TileEntityHopper.addItem(iinventory, itemstack.cloneItemStack().a(1), Facing.OPPOSITE_FACING[i1]);
                    if (itemstack1 == null) {
                        itemstack1 = itemstack.cloneItemStack();
                        if (--itemstack1.count == 0) {
                            itemstack1 = null;
                        }
                    } else {
                        itemstack1 = itemstack.cloneItemStack();
                    }
                } else {
                    itemstack1 = this.cR.a(sourceblock, itemstack);
                    if (itemstack1 != null && itemstack1.count == 0) {
                        itemstack1 = null;
                    }
                }

                tileentitydispenser.setItem(l, itemstack1);
            }
        }
    }
}
