package net.minecraft.server;

// CraftBukkit
import java.util.ArrayList;

public class ContainerWorkbench extends Container {

    public InventoryCrafting a = new InventoryCrafting(this, 3, 3);
    public IInventory b = new InventoryCraftResult();
    private World c;
    private int h;
    private int i;
    private int j;

    public ContainerWorkbench(InventoryPlayer inventoryplayer, World world, int i, int j, int k) {
        this.c = world;
        this.h = i;
        this.i = j;
        this.j = k;
        this.a((Slot) (new SlotResult(inventoryplayer.d, this.a, this.b, 0, 124, 35)));

        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.a(new Slot(this.a, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
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

        for (int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.a.getItem(i);

            if (itemstack != null) {
                entityhuman.b(itemstack);
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        return this.c.getTypeId(this.h, this.i, this.j) != Block.WORKBENCH.id ? false : entityhuman.d((double) this.h + 0.5D, (double) this.i + 0.5D, (double) this.j + 0.5D) <= 64.0D;
    }
}
