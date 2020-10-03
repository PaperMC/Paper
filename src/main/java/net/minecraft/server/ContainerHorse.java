package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public class ContainerHorse extends Container {

    private final IInventory c;
    private final EntityHorseAbstract d;

    // CraftBukkit start
    org.bukkit.craftbukkit.inventory.CraftInventoryView bukkitEntity;
    PlayerInventory player;

    @Override
    public InventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        return bukkitEntity = new CraftInventoryView(player.player.getBukkitEntity(), c.getOwner().getInventory(), this);
    }

    public ContainerHorse(int i, PlayerInventory playerinventory, IInventory iinventory, final EntityHorseAbstract entityhorseabstract) {
        super((Containers) null, i);
        player = playerinventory;
        // CraftBukkit end
        this.c = iinventory;
        this.d = entityhorseabstract;
        boolean flag = true;

        iinventory.startOpen(playerinventory.player);
        boolean flag1 = true;

        this.a(new Slot(iinventory, 0, 8, 18) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() == Items.SADDLE && !this.hasItem() && entityhorseabstract.canSaddle();
            }
        });
        this.a(new Slot(iinventory, 1, 8, 36) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return entityhorseabstract.l(itemstack);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        int j;
        int k;

        if (entityhorseabstract instanceof EntityHorseChestedAbstract && ((EntityHorseChestedAbstract) entityhorseabstract).isCarryingChest()) {
            for (j = 0; j < 3; ++j) {
                for (k = 0; k < ((EntityHorseChestedAbstract) entityhorseabstract).eU(); ++k) {
                    this.a(new Slot(iinventory, 2 + k + j * ((EntityHorseChestedAbstract) entityhorseabstract).eU(), 80 + k * 18, 18 + j * 18));
                }
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 102 + j * 18 + -18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return this.c.a(entityhuman) && this.d.isAlive() && this.d.g((Entity) entityhuman) < 8.0F;
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            int j = this.c.getSize();

            if (i < j) {
                if (!this.a(itemstack1, j, this.slots.size(), true)) {
                    return ItemStack.b;
                }
            } else if (this.getSlot(1).isAllowed(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.a(itemstack1, 1, 2, false)) {
                    return ItemStack.b;
                }
            } else if (this.getSlot(0).isAllowed(itemstack1)) {
                if (!this.a(itemstack1, 0, 1, false)) {
                    return ItemStack.b;
                }
            } else if (j <= 2 || !this.a(itemstack1, 2, j, false)) {
                int k = j + 27;
                int l = k + 9;

                if (i >= k && i < l) {
                    if (!this.a(itemstack1, j, k, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= j && i < k) {
                    if (!this.a(itemstack1, k, l, false)) {
                        return ItemStack.b;
                    }
                } else if (!this.a(itemstack1, k, k, false)) {
                    return ItemStack.b;
                }

                return ItemStack.b;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.b);
            } else {
                slot.d();
            }
        }

        return itemstack;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.c.closeContainer(entityhuman);
    }
}
