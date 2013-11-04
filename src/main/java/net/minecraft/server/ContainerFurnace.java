package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerFurnace extends Container {

    private TileEntityFurnace furnace;
    private int f;
    private int g;
    private int h;

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;

    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryFurnace inventory = new CraftInventoryFurnace(this.furnace);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end

    public ContainerFurnace(PlayerInventory playerinventory, TileEntityFurnace tileentityfurnace) {
        this.furnace = tileentityfurnace;
        this.a(new Slot(tileentityfurnace, 0, 56, 17));
        this.a(new Slot(tileentityfurnace, 1, 56, 53));
        this.a(new SlotFurnaceResult(playerinventory.player, tileentityfurnace, 2, 116, 35));
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

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.furnace.cookTime);
        icrafting.setContainerData(this, 1, this.furnace.burnTime);
        icrafting.setContainerData(this, 2, this.furnace.ticksForCurrentFuel);
    }

    public void b() {
        super.b();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            if (this.f != this.furnace.cookTime) {
                icrafting.setContainerData(this, 0, this.furnace.cookTime);
            }

            if (this.g != this.furnace.burnTime) {
                icrafting.setContainerData(this, 1, this.furnace.burnTime);
            }

            if (this.h != this.furnace.ticksForCurrentFuel) {
                icrafting.setContainerData(this, 2, this.furnace.ticksForCurrentFuel);
            }
        }

        this.f = this.furnace.cookTime;
        this.g = this.furnace.burnTime;
        this.h = this.furnace.ticksForCurrentFuel;
    }

    public boolean a(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.furnace.a(entityhuman);
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
            } else if (i != 1 && i != 0) {
                if (RecipesFurnace.getInstance().getResult(itemstack1) != null) {
                    if (!this.a(itemstack1, 0, 1, false)) {
                        return null;
                    }
                } else if (TileEntityFurnace.isFuel(itemstack1)) {
                    if (!this.a(itemstack1, 1, 2, false)) {
                        return null;
                    }
                } else if (i >= 3 && i < 30) {
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
}
