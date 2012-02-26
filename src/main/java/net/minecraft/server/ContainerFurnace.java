package net.minecraft.server;

public class ContainerFurnace extends Container {

    private TileEntityFurnace a;
    private int b = 0;
    private int c = 0;
    private int h = 0;

    public ContainerFurnace(PlayerInventory playerinventory, TileEntityFurnace tileentityfurnace) {
        this.a = tileentityfurnace;
        this.a(new Slot(tileentityfurnace, 0, 56, 17));
        this.a(new Slot(tileentityfurnace, 1, 56, 53));
        this.a(new SlotResult2(playerinventory.d, tileentityfurnace, 2, 116, 35));

        int i;

        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.a(new Slot(playerinventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }
    }

    public void a(ICrafting icrafting) {
        super.a(icrafting);
        icrafting.a(this, 0, this.a.cookTime);
        icrafting.a(this, 1, this.a.burnTime);
        icrafting.a(this, 2, this.a.ticksForCurrentFuel);
    }

    public void a() {
        super.a();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            if (this.b != this.a.cookTime) {
                icrafting.a(this, 0, this.a.cookTime);
            }

            if (this.c != this.a.burnTime) {
                icrafting.a(this, 1, this.a.burnTime);
            }

            if (this.h != this.a.ticksForCurrentFuel) {
                icrafting.a(this, 2, this.a.ticksForCurrentFuel);
            }
        }

        this.b = this.a.cookTime;
        this.c = this.a.burnTime;
        this.h = this.a.ticksForCurrentFuel;
    }

    public boolean b(EntityHuman entityhuman) {
        return this.a.a(entityhuman);
    }

    public ItemStack a(int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.e.get(i);

        if (slot != null && slot.c()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return null;
                }
            } else if (i >= 3 && i < 30) {
                if (!this.a(itemstack1, 30, 39, false)) {
                    return null;
                }
            } else if (i >= 30 && i < 39) {
                if (!this.a(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.c((ItemStack) null);
            } else {
                slot.d();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.b(itemstack1);
        }

        return itemstack;
    }
}
