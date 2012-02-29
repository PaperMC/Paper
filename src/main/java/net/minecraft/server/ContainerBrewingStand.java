package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerBrewingStand extends Container {

    private TileEntityBrewingStand brewingStand;
    private int b = 0;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerBrewingStand(PlayerInventory playerinventory, TileEntityBrewingStand tileentitybrewingstand) {
        player = playerinventory; // CraftBukkit
        this.brewingStand = tileentitybrewingstand;
        this.a(new SlotPotionBottle(this, playerinventory.player, tileentitybrewingstand, 0, 56, 46));
        this.a(new SlotPotionBottle(this, playerinventory.player, tileentitybrewingstand, 1, 79, 53));
        this.a(new SlotPotionBottle(this, playerinventory.player, tileentitybrewingstand, 2, 102, 46));
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

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.brewingStand.h());
    }

    public void a() {
        super.a();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            if (this.b != this.brewingStand.h()) {
                icrafting.setContainerData(this, 0, this.brewingStand.h());
            }
        }

        this.b = this.brewingStand.h();
    }

    public boolean b(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.brewingStand.a(entityhuman);
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
                slot.set((ItemStack) null);
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

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }
        CraftInventory inventory = new CraftInventory(this.brewingStand);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
