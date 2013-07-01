package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerPlayer extends Container {

    public InventoryCrafting craftInventory = new InventoryCrafting(this, 2, 2);
    public IInventory resultInventory = new InventoryCraftResult();
    public boolean g;
    private final EntityHuman h;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerPlayer(PlayerInventory playerinventory, boolean flag, EntityHuman entityhuman) {
        this.g = flag;
        this.h = entityhuman;
        this.resultInventory = new InventoryCraftResult(); // CraftBukkit - moved to before InventoryCrafting construction
        this.craftInventory = new InventoryCrafting(this, 2, 2, playerinventory.player); // CraftBukkit - pass player
        this.craftInventory.resultInventory = this.resultInventory; // CraftBukkit - let InventoryCrafting know about its result slot
        this.player = playerinventory; // CraftBukkit - save player
        this.a((Slot) (new SlotResult(playerinventory.player, this.craftInventory, this.resultInventory, 0, 144, 36)));

        int i;
        int j;

        for (i = 0; i < 2; ++i) {
            for (j = 0; j < 2; ++j) {
                this.a(new Slot(this.craftInventory, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }

        for (i = 0; i < 4; ++i) {
            this.a((Slot) (new SlotArmor(this, playerinventory, playerinventory.getSize() - 1 - i, 8, 8 + i * 18, i)));
        }

        for (i = 0; i < 3; ++i) {
            for (j = 0; j < 9; ++j) {
                this.a(new Slot(playerinventory, j + (i + 1) * 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.a(new Slot(playerinventory, i, 8 + i * 18, 142));
        }

        // this.a((IInventory) this.craftInventory); // CraftBukkit - unneeded since it just sets result slot to empty
    }

    public void a(IInventory iinventory) {
        // CraftBukkit start (Note: the following line would cause an error if called during construction)
        CraftingManager.getInstance().lastCraftView = getBukkitView();
        ItemStack craftResult = CraftingManager.getInstance().craft(this.craftInventory, this.h.world);
        this.resultInventory.setItem(0, craftResult);
        if (super.listeners.size() < 1) {
            return;
        }

        EntityPlayer player = (EntityPlayer) super.listeners.get(0); // TODO: Is this _always_ correct? Seems like it.
        player.playerConnection.sendPacket(new Packet103SetSlot(player.activeContainer.windowId, 0, craftResult));
        // CraftBukkit end
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);

        for (int i = 0; i < 4; ++i) {
            ItemStack itemstack = this.craftInventory.splitWithoutUpdate(i);

            if (itemstack != null) {
                entityhuman.drop(itemstack);
            }
        }

        this.resultInventory.setItem(0, (ItemStack) null);
    }

    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.c.get(i);

        if (slot != null && slot.e()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 9, 45, true)) {
                    return null;
                }

                slot.a(itemstack1, itemstack);
            } else if (i >= 1 && i < 5) {
                if (!this.a(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (i >= 5 && i < 9) {
                if (!this.a(itemstack1, 9, 45, false)) {
                    return null;
                }
            } else if (itemstack.getItem() instanceof ItemArmor && !((Slot) this.c.get(5 + ((ItemArmor) itemstack.getItem()).b)).e()) {
                int j = 5 + ((ItemArmor) itemstack.getItem()).b;

                if (!this.a(itemstack1, j, j + 1, false)) {
                    return null;
                }
            } else if (i >= 9 && i < 36) {
                if (!this.a(itemstack1, 36, 45, false)) {
                    return null;
                }
            } else if (i >= 36 && i < 45) {
                if (!this.a(itemstack1, 9, 36, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 9, 45, false)) {
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

    public boolean a(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.resultInventory && super.a(itemstack, slot);
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
