package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerBrewingStand extends Container {

    private final IInventory brewingStand;
    private final IContainerProperties d;
    private final Slot e;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerBrewingStand(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, new InventorySubcontainer(5), new ContainerProperties(2));
    }

    public ContainerBrewingStand(int i, PlayerInventory playerinventory, IInventory iinventory, IContainerProperties icontainerproperties) {
        super(Containers.BREWING_STAND, i);
        player = playerinventory; // CraftBukkit
        a(iinventory, 5);
        a(icontainerproperties, 2);
        this.brewingStand = iinventory;
        this.d = icontainerproperties;
        this.a((Slot) (new ContainerBrewingStand.SlotPotionBottle(iinventory, 0, 56, 51)));
        this.a((Slot) (new ContainerBrewingStand.SlotPotionBottle(iinventory, 1, 79, 58)));
        this.a((Slot) (new ContainerBrewingStand.SlotPotionBottle(iinventory, 2, 102, 51)));
        this.e = this.a((Slot) (new ContainerBrewingStand.SlotBrewing(iinventory, 3, 79, 17)));
        this.a((Slot) (new ContainerBrewingStand.a(iinventory, 4, 17, 17)));
        this.a(icontainerproperties);

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.brewingStand.a(entityhuman);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if ((i < 0 || i > 2) && i != 3 && i != 4) {
                if (ContainerBrewingStand.a.a_(itemstack)) {
                    if (this.a(itemstack1, 4, 5, false) || this.e.isAllowed(itemstack1) && !this.a(itemstack1, 3, 4, false)) {
                        return ItemStack.b;
                    }
                } else if (this.e.isAllowed(itemstack1)) {
                    if (!this.a(itemstack1, 3, 4, false)) {
                        return ItemStack.b;
                    }
                } else if (ContainerBrewingStand.SlotPotionBottle.b_(itemstack) && itemstack.getCount() == 1) {
                    if (!this.a(itemstack1, 0, 3, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 5 && i < 32) {
                    if (!this.a(itemstack1, 32, 41, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 32 && i < 41) {
                    if (!this.a(itemstack1, 5, 32, false)) {
                        return ItemStack.b;
                    }
                } else if (!this.a(itemstack1, 5, 41, false)) {
                    return ItemStack.b;
                }
            } else {
                if (!this.a(itemstack1, 5, 41, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
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

    static class a extends Slot {

        public a(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isAllowed(ItemStack itemstack) {
            return a_(itemstack);
        }

        public static boolean a_(ItemStack itemstack) {
            return itemstack.getItem() == Items.BLAZE_POWDER;
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }

    static class SlotBrewing extends Slot {

        public SlotBrewing(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isAllowed(ItemStack itemstack) {
            return PotionBrewer.a(itemstack);
        }

        @Override
        public int getMaxStackSize() {
            return 64;
        }
    }

    static class SlotPotionBottle extends Slot {

        public SlotPotionBottle(IInventory iinventory, int i, int j, int k) {
            super(iinventory, i, j, k);
        }

        @Override
        public boolean isAllowed(ItemStack itemstack) {
            return b_(itemstack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
            PotionRegistry potionregistry = PotionUtil.d(itemstack);

            if (entityhuman instanceof EntityPlayer) {
                CriterionTriggers.k.a((EntityPlayer) entityhuman, potionregistry);
            }

            super.a(entityhuman, itemstack);
            return itemstack;
        }

        public static boolean b_(ItemStack itemstack) {
            Item item = itemstack.getItem();

            return item == Items.POTION || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
        }
    }

    // CraftBukkit start
    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryBrewer inventory = new CraftInventoryBrewer(this.brewingStand);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
