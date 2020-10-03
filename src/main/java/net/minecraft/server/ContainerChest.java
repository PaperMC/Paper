package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerChest extends Container {

    private final IInventory container;
    private final int d;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory;
        if (this.container instanceof PlayerInventory) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryPlayer((PlayerInventory) this.container);
        } else if (this.container instanceof InventoryLargeChest) {
            inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryDoubleChest((InventoryLargeChest) this.container);
        } else {
            inventory = new CraftInventory(this.container);
        }

        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    private ContainerChest(Containers<?> containers, int i, PlayerInventory playerinventory, int j) {
        this(containers, i, playerinventory, new InventorySubcontainer(9 * j), j);
    }

    public static ContainerChest a(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X1, i, playerinventory, 1);
    }

    public static ContainerChest b(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X2, i, playerinventory, 2);
    }

    public static ContainerChest c(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X3, i, playerinventory, 3);
    }

    public static ContainerChest d(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X4, i, playerinventory, 4);
    }

    public static ContainerChest e(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X5, i, playerinventory, 5);
    }

    public static ContainerChest f(int i, PlayerInventory playerinventory) {
        return new ContainerChest(Containers.GENERIC_9X6, i, playerinventory, 6);
    }

    public static ContainerChest a(int i, PlayerInventory playerinventory, IInventory iinventory) {
        return new ContainerChest(Containers.GENERIC_9X3, i, playerinventory, iinventory, 3);
    }

    public static ContainerChest b(int i, PlayerInventory playerinventory, IInventory iinventory) {
        return new ContainerChest(Containers.GENERIC_9X6, i, playerinventory, iinventory, 6);
    }

    public ContainerChest(Containers<?> containers, int i, PlayerInventory playerinventory, IInventory iinventory, int j) {
        super(containers, i);
        a(iinventory, j * 9);
        this.container = iinventory;
        this.d = j;
        iinventory.startOpen(playerinventory.player);
        int k = (this.d - 4) * 18;

        // CraftBukkit start - Save player
        this.player = playerinventory;
        // CraftBukkit end

        int l;
        int i1;

        for (l = 0; l < this.d; ++l) {
            for (i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(iinventory, i1 + l * 9, 8 + i1 * 18, 18 + l * 18));
            }
        }

        for (l = 0; l < 3; ++l) {
            for (i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 103 + l * 18 + k));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(playerinventory, l, 8 + l * 18, 161 + k));
        }

    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.container.a(entityhuman);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i < this.d * 9) {
                if (!this.a(itemstack1, this.d * 9, this.slots.size(), true)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 0, this.d * 9, false)) {
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
        this.container.closeContainer(entityhuman);
    }

    public IInventory e() {
        return this.container;
    }
}
