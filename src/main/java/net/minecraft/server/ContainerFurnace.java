package net.minecraft.server;

public abstract class ContainerFurnace extends ContainerRecipeBook<IInventory> {

    private final IInventory furnace;
    private final IContainerProperties e;
    protected final World c;
    private final Recipes<? extends RecipeCooking> f;
    private final RecipeBookType g;

    protected ContainerFurnace(Containers<?> containers, Recipes<? extends RecipeCooking> recipes, RecipeBookType recipebooktype, int i, PlayerInventory playerinventory) {
        this(containers, recipes, recipebooktype, i, playerinventory, new InventorySubcontainer(3), new ContainerProperties(4));
    }

    protected ContainerFurnace(Containers<?> containers, Recipes<? extends RecipeCooking> recipes, RecipeBookType recipebooktype, int i, PlayerInventory playerinventory, IInventory iinventory, IContainerProperties icontainerproperties) {
        super(containers, i);
        this.f = recipes;
        this.g = recipebooktype;
        a(iinventory, 3);
        a(icontainerproperties, 4);
        this.furnace = iinventory;
        this.e = icontainerproperties;
        this.c = playerinventory.player.world;
        this.a(new Slot(iinventory, 0, 56, 17));
        this.a((Slot) (new SlotFurnaceFuel(this, iinventory, 1, 56, 53)));
        this.a((Slot) (new SlotFurnaceResult(playerinventory.player, iinventory, 2, 116, 35)));

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

        this.a(icontainerproperties);
    }

    @Override
    public void a(AutoRecipeStackManager autorecipestackmanager) {
        if (this.furnace instanceof AutoRecipeOutput) {
            ((AutoRecipeOutput) this.furnace).a(autorecipestackmanager);
        }

    }

    @Override
    public void e() {
        this.furnace.clear();
    }

    @Override
    public void a(boolean flag, IRecipe<?> irecipe, EntityPlayer entityplayer) {
        (new AutoRecipeFurnace<>(this)).a(entityplayer, irecipe, flag);
    }

    @Override
    public boolean a(IRecipe<? super IInventory> irecipe) {
        return irecipe.a(this.furnace, this.c);
    }

    @Override
    public int f() {
        return 2;
    }

    @Override
    public int g() {
        return 1;
    }

    @Override
    public int h() {
        return 1;
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return this.furnace.a(entityhuman);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 1 && i != 0) {
                if (this.a(itemstack1)) {
                    if (!this.a(itemstack1, 0, 1, false)) {
                        return ItemStack.b;
                    }
                } else if (this.b(itemstack1)) {
                    if (!this.a(itemstack1, 1, 2, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 3 && i < 30) {
                    if (!this.a(itemstack1, 30, 39, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
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

    protected boolean a(ItemStack itemstack) {
        return this.c.getCraftingManager().craft(this.f, new InventorySubcontainer(new ItemStack[]{itemstack}), this.c).isPresent();
    }

    protected boolean b(ItemStack itemstack) {
        return TileEntityFurnace.isFuel(itemstack);
    }
}
