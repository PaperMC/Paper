package net.minecraft.server;

import javax.annotation.Nullable;

public abstract class ContainerAnvilAbstract extends Container {

    protected final InventoryCraftResult resultInventory = new InventoryCraftResult();
    protected final IInventory repairInventory = new InventorySubcontainer(2) {
        @Override
        public void update() {
            super.update();
            ContainerAnvilAbstract.this.a((IInventory) this);
        }
    };
    protected final ContainerAccess containerAccess;
    protected final EntityHuman player;

    protected abstract boolean b(EntityHuman entityhuman, boolean flag);

    protected abstract ItemStack a(EntityHuman entityhuman, ItemStack itemstack);

    protected abstract boolean a(IBlockData iblockdata);

    public ContainerAnvilAbstract(@Nullable Containers<?> containers, int i, PlayerInventory playerinventory, ContainerAccess containeraccess) {
        super(containers, i);
        this.containerAccess = containeraccess;
        this.player = playerinventory.player;
        this.a(new Slot(this.repairInventory, 0, 27, 47));
        this.a(new Slot(this.repairInventory, 1, 76, 47));
        this.a(new Slot(this.resultInventory, 2, 134, 47) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public boolean isAllowed(EntityHuman entityhuman) {
                return ContainerAnvilAbstract.this.b(entityhuman, this.hasItem());
            }

            @Override
            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                return ContainerAnvilAbstract.this.a(entityhuman, itemstack);
            }
        });

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

    }

    public abstract void e();

    @Override
    public void a(IInventory iinventory) {
        super.a(iinventory);
        if (iinventory == this.repairInventory) {
            this.e();
        }

    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, world, this.repairInventory);
        });
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return (Boolean) this.containerAccess.a((world, blockposition) -> {
            return !this.a(world.getType(blockposition)) ? false : entityhuman.h((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

    protected boolean a(ItemStack itemstack) {
        return false;
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 39) {
                    int j = this.a(itemstack) ? 1 : 0;

                    if (!this.a(itemstack1, j, 2, false)) {
                        return ItemStack.b;
                    }
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
                return ItemStack.b;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.b);
            } else {
                slot.d();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.b;
            }

            slot.a(entityhuman, itemstack1);
        }

        return itemstack;
    }
}
