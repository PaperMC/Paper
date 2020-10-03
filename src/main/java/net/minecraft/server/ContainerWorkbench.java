package net.minecraft.server;

import java.util.Optional;

public class ContainerWorkbench extends ContainerRecipeBook<InventoryCrafting> {

    private final InventoryCrafting craftInventory;
    private final InventoryCraftResult resultInventory;
    public final ContainerAccess containerAccess;
    private final EntityHuman f;

    public ContainerWorkbench(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerWorkbench(int i, PlayerInventory playerinventory, ContainerAccess containeraccess) {
        super(Containers.CRAFTING, i);
        this.craftInventory = new InventoryCrafting(this, 3, 3);
        this.resultInventory = new InventoryCraftResult();
        this.containerAccess = containeraccess;
        this.f = playerinventory.player;
        this.a((Slot) (new SlotResult(playerinventory.player, this.craftInventory, this.resultInventory, 0, 124, 35)));

        int j;
        int k;

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 3; ++k) {
                this.a(new Slot(this.craftInventory, k + j * 3, 30 + k * 18, 17 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

    }

    protected static void a(int i, World world, EntityHuman entityhuman, InventoryCrafting inventorycrafting, InventoryCraftResult inventorycraftresult) {
        if (!world.isClientSide) {
            EntityPlayer entityplayer = (EntityPlayer) entityhuman;
            ItemStack itemstack = ItemStack.b;
            Optional<RecipeCrafting> optional = world.getMinecraftServer().getCraftingManager().craft(Recipes.CRAFTING, inventorycrafting, world);

            if (optional.isPresent()) {
                RecipeCrafting recipecrafting = (RecipeCrafting) optional.get();

                if (inventorycraftresult.a(world, entityplayer, recipecrafting)) {
                    itemstack = recipecrafting.a(inventorycrafting);
                }
            }

            inventorycraftresult.setItem(0, itemstack);
            entityplayer.playerConnection.sendPacket(new PacketPlayOutSetSlot(i, 0, itemstack));
        }
    }

    @Override
    public void a(IInventory iinventory) {
        this.containerAccess.a((world, blockposition) -> {
            a(this.windowId, world, this.f, this.craftInventory, this.resultInventory);
        });
    }

    @Override
    public void a(AutoRecipeStackManager autorecipestackmanager) {
        this.craftInventory.a(autorecipestackmanager);
    }

    @Override
    public void e() {
        this.craftInventory.clear();
        this.resultInventory.clear();
    }

    @Override
    public boolean a(IRecipe<? super InventoryCrafting> irecipe) {
        return irecipe.a(this.craftInventory, this.f.world);
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, world, (IInventory) this.craftInventory);
        });
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return a(this.containerAccess, entityhuman, Blocks.CRAFTING_TABLE);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                this.containerAccess.a((world, blockposition) -> {
                    itemstack1.getItem().b(itemstack1, world, entityhuman);
                });
                if (!this.a(itemstack1, 10, 46, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i >= 10 && i < 46) {
                if (!this.a(itemstack1, 1, 10, false)) {
                    if (i < 37) {
                        if (!this.a(itemstack1, 37, 46, false)) {
                            return ItemStack.b;
                        }
                    } else if (!this.a(itemstack1, 10, 37, false)) {
                        return ItemStack.b;
                    }
                }
            } else if (!this.a(itemstack1, 10, 46, false)) {
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

            ItemStack itemstack2 = slot.a(entityhuman, itemstack1);

            if (i == 0) {
                entityhuman.drop(itemstack2, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean a(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.resultInventory && super.a(itemstack, slot);
    }

    @Override
    public int f() {
        return 0;
    }

    @Override
    public int g() {
        return this.craftInventory.g();
    }

    @Override
    public int h() {
        return this.craftInventory.f();
    }
}
