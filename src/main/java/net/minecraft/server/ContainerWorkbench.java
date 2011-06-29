package net.minecraft.server;

public class ContainerWorkbench extends Container {

    public InventoryCrafting craftInventory = new InventoryCrafting(this, 3, 3);
    public IInventory resultInventory = new InventoryCraftResult();
    private World c;
    private int h;
    private int i;
    private int j;

    public ContainerWorkbench(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
        this.c = world;
        this.h = i;
        this.i = j;
        this.j = k;
        this.a((Slot) (new SlotResult(inventoryplayer.d, this.craftInventory, this.resultInventory, 0, 124, 35)));

        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.a(new Slot(this.craftInventory, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(inventoryplayer, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(inventoryplayer, l, 8 + l * 18, 142));
        }

        this.a((IInventory) this.craftInventory);
    }

    public void a(IInventory iinventory) {
        // CraftBukkit start
        ItemStack craftResult = CraftingManager.getInstance().craft(this.craftInventory);
        this.resultInventory.setItem(0, craftResult);
        if (super.listeners.size() < 1) {
            return;
        }

        EntityPlayer player = (EntityPlayer) super.listeners.get(0); // TODO: Is this _always_ correct? Seems like it.
        player.netServerHandler.sendPacket(new Packet103SetSlot(player.activeContainer.windowId, 0, craftResult));
        // CraftBukkit end
    }

    public void a(EntityHuman entityhuman) {
        super.a(entityhuman);
        if (!this.c.isStatic) {
            for (int i = 0; i < 9; ++i) {
                ItemStack itemstack = this.craftInventory.getItem(i);

                if (itemstack != null) {
                    entityhuman.b(itemstack);
                }
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        return this.c.getTypeId(this.h, this.i, this.j) != Block.WORKBENCH.id ? false : entityhuman.d((double) this.h + 0.5D, (double) this.i + 0.5D, (double) this.j + 0.5D) <= 64.0D;
    }

    public ItemStack a(int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.e.get(i);

        if (slot != null && slot.b()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                this.a(itemstack1, 10, 46, true);
            } else if (i >= 10 && i < 37) {
                this.a(itemstack1, 37, 46, false);
            } else if (i >= 37 && i < 46) {
                this.a(itemstack1, 10, 37, false);
            } else {
                this.a(itemstack1, 10, 46, false);
            }

            if (itemstack1.count == 0) {
                slot.c((ItemStack) null);
            } else {
                slot.c();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.a(itemstack1);
        }

        return itemstack;
    }
}
