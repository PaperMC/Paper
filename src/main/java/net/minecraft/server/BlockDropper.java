package net.minecraft.server;

public class BlockDropper extends BlockDispenser {

    private static final IDispenseBehavior c = new DispenseBehaviorItem();

    public BlockDropper(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    protected IDispenseBehavior a(ItemStack itemstack) {
        return BlockDropper.c;
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityDropper();
    }

    @Override
    public void dispense(WorldServer worldserver, BlockPosition blockposition) {
        SourceBlock sourceblock = new SourceBlock(worldserver, blockposition);
        TileEntityDispenser tileentitydispenser = (TileEntityDispenser) sourceblock.getTileEntity();
        int i = tileentitydispenser.h();

        if (i < 0) {
            worldserver.triggerEffect(1001, blockposition, 0);
        } else {
            ItemStack itemstack = tileentitydispenser.getItem(i);

            if (!itemstack.isEmpty()) {
                EnumDirection enumdirection = (EnumDirection) worldserver.getType(blockposition).get(BlockDropper.FACING);
                IInventory iinventory = TileEntityHopper.b((World) worldserver, blockposition.shift(enumdirection));
                ItemStack itemstack1;

                if (iinventory == null) {
                    itemstack1 = BlockDropper.c.dispense(sourceblock, itemstack);
                } else {
                    itemstack1 = TileEntityHopper.addItem(tileentitydispenser, iinventory, itemstack.cloneItemStack().cloneAndSubtract(1), enumdirection.opposite());
                    if (itemstack1.isEmpty()) {
                        itemstack1 = itemstack.cloneItemStack();
                        itemstack1.subtract(1);
                    } else {
                        itemstack1 = itemstack.cloneItemStack();
                    }
                }

                tileentitydispenser.setItem(i, itemstack1);
            }
        }
    }
}
