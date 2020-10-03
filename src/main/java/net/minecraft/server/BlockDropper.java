package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
// CraftBukkit end

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
                    // CraftBukkit start - Fire event when pushing items into other inventories
                    CraftItemStack oitemstack = CraftItemStack.asCraftMirror(itemstack.cloneItemStack().cloneAndSubtract(1));

                    org.bukkit.inventory.Inventory destinationInventory;
                    // Have to special case large chests as they work oddly
                    if (iinventory instanceof InventoryLargeChest) {
                        destinationInventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) iinventory);
                    } else {
                        destinationInventory = iinventory.getOwner().getInventory();
                    }

                    InventoryMoveItemEvent event = new InventoryMoveItemEvent(tileentitydispenser.getOwner().getInventory(), oitemstack.clone(), destinationInventory, true);
                    worldserver.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    itemstack1 = TileEntityHopper.addItem(tileentitydispenser, iinventory, CraftItemStack.asNMSCopy(event.getItem()), enumdirection.opposite());
                    if (event.getItem().equals(oitemstack) && itemstack1.isEmpty()) {
                        // CraftBukkit end
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
