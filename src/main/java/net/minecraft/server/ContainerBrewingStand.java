package net.minecraft.server;

public class ContainerBrewingStand extends Container {

    private TileEntityBrewingStand a;
    private int b = 0;

    public ContainerBrewingStand(PlayerInventory playerinventory, TileEntityBrewingStand tileentitybrewingstand) {
        this.a = tileentitybrewingstand;
        this.a(new SlotPotionBottle(this, playerinventory.d, tileentitybrewingstand, 0, 56, 46));
        this.a(new SlotPotionBottle(this, playerinventory.d, tileentitybrewingstand, 1, 79, 53));
        this.a(new SlotPotionBottle(this, playerinventory.d, tileentitybrewingstand, 2, 102, 46));
        this.a(new SlotBrewing(this, tileentitybrewingstand, 3, 79, 17));

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
        icrafting.a(this, 0, this.a.h());
    }

    public void a() {
        super.a();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            if (this.b != this.a.h()) {
                icrafting.a(this, 0, this.a.h());
            }
        }

        this.b = this.a.h();
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
            if ((i < 0 || i > 2) && i != 3) {
                if (i >= 4 && i < 31) {
                    if (!this.a(itemstack1, 31, 40, false)) {
                        return null;
                    }
                } else if (i >= 31 && i < 40) {
                    if (!this.a(itemstack1, 4, 31, false)) {
                        return null;
                    }
                } else if (!this.a(itemstack1, 4, 40, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 4, 40, true)) {
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
