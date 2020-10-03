package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftInventoryView; // CraftBukkit

public class ContainerBeacon extends Container {

    private final IInventory beacon;
    private final ContainerBeacon.SlotBeacon d;
    private final ContainerAccess containerAccess;
    private final IContainerProperties containerProperties;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerBeacon(int i, IInventory iinventory) {
        this(i, iinventory, new ContainerProperties(3), ContainerAccess.a);
    }

    public ContainerBeacon(int i, IInventory iinventory, IContainerProperties icontainerproperties, ContainerAccess containeraccess) {
        super(Containers.BEACON, i);
        player = (PlayerInventory) iinventory; // CraftBukkit - TODO: check this
        this.beacon = new InventorySubcontainer(1) {
            @Override
            public boolean b(int j, ItemStack itemstack) {
                return itemstack.getItem().a((Tag) TagsItem.BEACON_PAYMENT_ITEMS);
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        };
        a(icontainerproperties, 3);
        this.containerProperties = icontainerproperties;
        this.containerAccess = containeraccess;
        this.d = new ContainerBeacon.SlotBeacon(this.beacon, 0, 136, 110);
        this.a((Slot) this.d);
        this.a(icontainerproperties);
        boolean flag = true;
        boolean flag1 = true;

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(iinventory, k + j * 9 + 9, 36 + k * 18, 137 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(iinventory, j, 36 + j * 18, 195));
        }

    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        if (!entityhuman.world.isClientSide) {
            ItemStack itemstack = this.d.a(this.d.getMaxStackSize());

            if (!itemstack.isEmpty()) {
                entityhuman.drop(itemstack, false);
            }

        }
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return a(this.containerAccess, entityhuman, Blocks.BEACON);
    }

    @Override
    public void a(int i, int j) {
        super.a(i, j);
        this.c();
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 1, 37, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (!this.d.hasItem() && this.d.isAllowed(itemstack1) && itemstack1.getCount() == 1) {
                if (!this.a(itemstack1, 0, 1, false)) {
                    return ItemStack.b;
                }
            } else if (i >= 1 && i < 28) {
                if (!this.a(itemstack1, 28, 37, false)) {
                    return ItemStack.b;
                }
            } else if (i >= 28 && i < 37) {
                if (!this.a(itemstack1, 1, 28, false)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 1, 37, false)) {
                return ItemStack.b;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.b);
            } else {
                slot.d();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.b;
            }

            slot.a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    public void c(int i, int j) {
        if (this.d.hasItem()) {
            this.containerProperties.setProperty(1, i);
            this.containerProperties.setProperty(2, j);
            this.d.a(1);
        }

    }

    class SlotBeacon extends Slot {

        public SlotBeacon(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isAllowed(ItemStack itemstack) {
            return itemstack.getItem().a((Tag) TagsItem.BEACON_PAYMENT_ITEMS);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryBeacon(this.beacon);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
