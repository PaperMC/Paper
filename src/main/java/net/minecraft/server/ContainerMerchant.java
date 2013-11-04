package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftInventoryView; // CraftBukkit

public class ContainerMerchant extends Container {

    private IMerchant merchant;
    private InventoryMerchant f;
    private final World g;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity == null) {
            bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), new org.bukkit.craftbukkit.inventory.CraftInventoryMerchant(this.getMerchantInventory()), this);
        }
        return bukkitEntity;
    }
    // CraftBukkit end


    public ContainerMerchant(PlayerInventory playerinventory, IMerchant imerchant, World world) {
        this.merchant = imerchant;
        this.g = world;
        this.f = new InventoryMerchant(playerinventory.player, imerchant);
        this.a(new Slot(this.f, 0, 36, 53));
        this.a(new Slot(this.f, 1, 62, 53));
        this.a((Slot) (new SlotMerchantResult(playerinventory.player, imerchant, this.f, 2, 120, 53)));
        this.player = playerinventory; // CraftBukkit - save player

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

    public InventoryMerchant getMerchantInventory() {
        return this.f;
    }

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
    }

    public void b() {
        super.b();
    }

    public void a(IInventory iinventory) {
        this.f.h();
        super.a(iinventory);
    }

    public void e(int i) {
        this.f.c(i);
    }

    public boolean a(EntityHuman entityhuman) {
        return this.merchant.b() == entityhuman;
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.c.get(i);

        if (slot != null && slot.e()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 30) {
                    if (!this.a(itemstack1, 30, 39, false)) {
                        return null;
                    }
                } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.set((ItemStack) null);
            } else {
                slot.f();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.merchant.a_((EntityHuman) null);
        super.b(entityhuman);
        if (!this.g.isStatic) {
            ItemStack itemstack = this.f.splitWithoutUpdate(0);

            if (itemstack != null) {
                entityhuman.drop(itemstack, false);
            }

            itemstack = this.f.splitWithoutUpdate(1);
            if (itemstack != null) {
                entityhuman.drop(itemstack, false);
            }
        }
    }
}
