package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerHopper extends Container {

    private final IInventory hopper;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory = new CraftInventory(this.hopper);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerHopper(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, new InventorySubcontainer(5));
    }

    public ContainerHopper(int i, PlayerInventory playerinventory, IInventory iinventory) {
        super(Containers.HOPPER, i);
        this.hopper = iinventory;
        this.player = playerinventory; // CraftBukkit - save player
        a(iinventory, 5);
        iinventory.startOpen(playerinventory.player);
        boolean flag = true;

        int j;

        for (j = 0; j < 5; ++j) {
            this.a(new Slot(iinventory, j, 44 + j * 18, 20));
        }

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, j * 18 + 51));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 109));
        }

    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.hopper.a(entityhuman);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < this.hopper.getSize()) {
                if (!this.a(itemstack1, this.hopper.getSize(), this.slots.size(), true)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 0, this.hopper.getSize(), false)) {
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
        this.hopper.closeContainer(entityhuman);
    }
}
