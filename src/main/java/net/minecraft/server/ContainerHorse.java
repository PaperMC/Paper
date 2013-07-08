package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public class ContainerHorse extends Container {

    private IInventory a;
    private EntityHorse f;

    // CraftBukkit start
    org.bukkit.craftbukkit.inventory.CraftInventoryView bukkitEntity;
    PlayerInventory player;

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryHorse(this.a);
        return bukkitEntity = new CraftInventoryView(player.player.getBukkitEntity(), inventory, this);
    }

    public ContainerHorse(IInventory iinventory, IInventory iinventory1, EntityHorse entityhorse) {
        player = (PlayerInventory) iinventory;
        // CraftBukkit end
        this.a = iinventory1;
        this.f = entityhorse;
        byte b0 = 3;

        iinventory1.startOpen();
        int i = (b0 - 4) * 18;

        this.a(new SlotHorseSaddle(this, iinventory1, 0, 8, 18));
        this.a(new SlotHorseArmor(this, iinventory1, 1, 8, 36, entityhorse));
        int j;
        int k;

        if (entityhorse.hasChest()) {
            for (j = 0; j < b0; ++j) {
                for (k = 0; k < 5; ++k) {
                    this.a(new Slot(iinventory1, 2 + k + j * 5, 80 + k * 18, 18 + j * 18));
                }
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(iinventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(iinventory, j, 8 + j * 18, 160 + i));
        }
    }

    public boolean a(EntityHuman entityhuman) {
        return this.a.a(entityhuman) && this.f.isAlive() && this.f.d(entityhuman) < 8.0F;
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.c.get(i);

        if (slot != null && slot.e()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < this.a.getSize()) {
                if (!this.a(itemstack1, this.a.getSize(), this.c.size(), true)) {
                    return null;
                }
            } else if (this.getSlot(1).isAllowed(itemstack1) && !this.getSlot(1).e()) {
                if (!this.a(itemstack1, 1, 2, false)) {
                    return null;
                }
            } else if (this.getSlot(0).isAllowed(itemstack1)) {
                if (!this.a(itemstack1, 0, 1, false)) {
                    return null;
                }
            } else if (this.a.getSize() <= 2 || !this.a(itemstack1, 2, this.a.getSize(), false)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.set((ItemStack) null);
            } else {
                slot.f();
            }
        }

        return itemstack;
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.a.g();
    }
}
