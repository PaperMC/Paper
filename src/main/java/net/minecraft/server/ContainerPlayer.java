package net.minecraft.server;

// CraftBukkit
import java.util.ArrayList;

public class ContainerPlayer extends Container {

    public InventoryCrafting a;
    public IInventory b;
    public boolean c;

    public ContainerPlayer(InventoryPlayer inventoryplayer) {
        this(inventoryplayer, true);
    }

    public ContainerPlayer(InventoryPlayer inventoryplayer, boolean flag) {
        this.a = new InventoryCrafting(this, 2, 2);
        this.b = new InventoryCraftResult();
        this.c = false;
        this.c = flag;
        this.a((Slot) (new SlotResult(inventoryplayer.d, this.a, this.b, 0, 144, 36)));

        int i;
        int j;

        for (i = 0; i < 2; ++i) {
            for (j = 0; j < 2; ++j) {
                this.a(new Slot(this.a, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            this.a((Slot) (new SlotArmor(this, inventoryplayer, inventoryplayer.getSize() - 1 - i, 8, 8 + i * 18, i)));
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.a(new Slot(inventoryplayer, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new Slot(inventoryplayer, i, 8 + i * 18, 142));
        }

        this.a((IInventory) this.a);
    }

    public void a(IInventory iinventory) {
        // CraftBukkit start
        ItemStack craftResult = CraftingManager.a().a(this.a);
        this.b.setItem(0, craftResult);
        if (super.g.size() < 1) {
            return;
        }
        EntityPlayer player = (EntityPlayer) super.g.get(0); // TODO: Is this _always_ correct? Seems like it.
        player.netServerHandler.sendPacket((Packet) (new Packet103SetSlot(player.activeContainer.f, 0, craftResult)));
        // CraftBukkit end
    }

    public void a(EntityHuman entityhuman) {
        super.a(entityhuman);

        for (int i = 0; i < 4; ++i) {
            ItemStack itemstack = this.a.getItem(i);

            if (itemstack != null) {
                entityhuman.b(itemstack);
                this.a.setItem(i, (ItemStack) null);
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        return true;
    }
}
