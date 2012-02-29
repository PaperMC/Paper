package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryCrafting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerWorkbench extends Container {

    public InventoryCrafting craftInventory; // CraftBukkit - move initialization into constructor
    public IInventory resultInventory; // CraftBukkit - move initialization into constructor
    private World c;
    private int h;
    private int i;
    private int j;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerWorkbench(PlayerInventory playerinventory, World world, int i, int j, int k) {
        // CraftBukkit start - switched order of IInventory construction and stored player
        this.resultInventory = new InventoryCraftResult();
        this.craftInventory = new InventoryCrafting(this, 3, 3);
        this.craftInventory.resultInventory = this.resultInventory;
        this.player = playerinventory;
        // CraftBukkit end
        this.c = world;
        this.h = i;
        this.i = j;
        this.j = k;
        this.a((Slot) (new SlotResult(playerinventory.player, this.craftInventory, this.resultInventory, 0, 124, 35)));

        int l;
        int i1;

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 3; ++i1) {
                this.a(new Slot(this.craftInventory, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
            }
        }

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(playerinventory, l, 8 + l * 18, 142));
        }

        this.a((IInventory) this.craftInventory);
    }

    public void a(IInventory iinventory) {
        // CraftBukkit start
        CraftingManager.getInstance().lastCraftView = getBukkitView();
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
                    entityhuman.drop(itemstack);
                }
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.c.getTypeId(this.h, this.i, this.j) != Block.WORKBENCH.id ? false : entityhuman.e((double) this.h + 0.5D, (double) this.i + 0.5D, (double) this.j + 0.5D) <= 64.0D;
    }

    public ItemStack a(int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.e.get(i);

        if (slot != null && slot.c()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 10, 46, true)) {
                    return null;
                }
            } else if (i >= 10 && i < 37) {
                if (!this.a(itemstack1, 37, 46, false)) {
                    return null;
                }
            } else if (i >= 37 && i < 46) {
                if (!this.a(itemstack1, 10, 37, false)) {
                    return null;
                }
            } else if (!this.a(itemstack1, 10, 46, false)) {
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
        CraftInventoryCrafting inventory = new CraftInventoryCrafting(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
