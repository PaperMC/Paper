package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;

public class ContainerStonecutter extends Container {

    private final ContainerAccess containerAccess;
    private final ContainerProperty containerProperty;
    private final World world;
    private List<RecipeStonecutting> i;
    private ItemStack j;
    private long k;
    final Slot c;
    final Slot d;
    private Runnable l;
    public final IInventory inventory;
    private final InventoryCraftResult resultInventory;

    public ContainerStonecutter(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerStonecutter(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(Containers.STONECUTTER, i);
        this.containerProperty = ContainerProperty.a();
        this.i = Lists.newArrayList();
        this.j = ItemStack.b;
        this.l = () -> {
        };
        this.inventory = new InventorySubcontainer(1) {
            @Override
            public void update() {
                super.update();
                ContainerStonecutter.this.a((IInventory) this);
                ContainerStonecutter.this.l.run();
            }
        };
        this.resultInventory = new InventoryCraftResult();
        this.containerAccess = containeraccess;
        this.world = playerinventory.player.world;
        this.c = this.a(new Slot(this.inventory, 0, 20, 33));
        this.d = this.a(new Slot(this.resultInventory, 1, 143, 33) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                itemstack.a(entityhuman.world, entityhuman, itemstack.getCount());
                ContainerStonecutter.this.resultInventory.b(entityhuman);
                ItemStack itemstack1 = ContainerStonecutter.this.c.a(1);

                if (!itemstack1.isEmpty()) {
                    ContainerStonecutter.this.i();
                }

                containeraccess.a((world, blockposition) -> {
                    long j = world.getTime();

                    if (ContainerStonecutter.this.k != j) {
                        world.playSound((EntityHuman) null, blockposition, SoundEffects.UI_STONECUTTER_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        ContainerStonecutter.this.k = j;
                    }

                });
                return super.a(entityhuman, itemstack);
            }
        });

        int j;

        for (j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.a(new Slot(playerinventory, k + j * 9 + 9, 8 + k * 18, 84 + j * 18));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerinventory, j, 8 + j * 18, 142));
        }

        this.a(this.containerProperty);
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return a(this.containerAccess, entityhuman, Blocks.STONECUTTER);
    }

    @Override
    public boolean a(EntityHuman entityhuman, int i) {
        if (this.d(i)) {
            this.containerProperty.set(i);
            this.i();
        }

        return true;
    }

    private boolean d(int i) {
        return i >= 0 && i < this.i.size();
    }

    @Override
    public void a(IInventory iinventory) {
        ItemStack itemstack = this.c.getItem();

        if (itemstack.getItem() != this.j.getItem()) {
            this.j = itemstack.cloneItemStack();
            this.a(iinventory, itemstack);
        }

    }

    private void a(IInventory iinventory, ItemStack itemstack) {
        this.i.clear();
        this.containerProperty.set(-1);
        this.d.set(ItemStack.b);
        if (!itemstack.isEmpty()) {
            this.i = this.world.getCraftingManager().b(Recipes.STONECUTTING, iinventory, this.world);
        }

    }

    private void i() {
        if (!this.i.isEmpty() && this.d(this.containerProperty.get())) {
            RecipeStonecutting recipestonecutting = (RecipeStonecutting) this.i.get(this.containerProperty.get());

            this.resultInventory.a((IRecipe) recipestonecutting);
            this.d.set(recipestonecutting.a(this.inventory));
        } else {
            this.d.set(ItemStack.b);
        }

        this.c();
    }

    @Override
    public Containers<?> getType() {
        return Containers.STONECUTTER;
    }

    @Override
    public boolean a(ItemStack itemstack, Slot slot) {
        return slot.inventory != this.resultInventory && super.a(itemstack, slot);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            Item item = itemstack1.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 1) {
                item.b(itemstack1, entityhuman.world, entityhuman);
                if (!this.a(itemstack1, 2, 38, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i == 0) {
                if (!this.a(itemstack1, 2, 38, false)) {
                    return ItemStack.b;
                }
            } else if (this.world.getCraftingManager().craft(Recipes.STONECUTTING, new InventorySubcontainer(new ItemStack[]{itemstack1}), this.world).isPresent()) {
                if (!this.a(itemstack1, 0, 1, false)) {
                    return ItemStack.b;
                }
            } else if (i >= 2 && i < 29) {
                if (!this.a(itemstack1, 29, 38, false)) {
                    return ItemStack.b;
                }
            } else if (i >= 29 && i < 38 && !this.a(itemstack1, 2, 29, false)) {
                return ItemStack.b;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.b);
            }

            slot.d();
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.b;
            }

            slot.a(entityhuman, itemstack1);
            this.c();
        }

        return itemstack;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.resultInventory.splitWithoutUpdate(1);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, entityhuman.world, this.inventory);
        });
    }
}
