package net.minecraft.server;

public class ContainerCartography extends Container {

    private final ContainerAccess containerAccess;
    private long e;
    public final IInventory inventory;
    private final InventoryCraftResult resultInventory;

    public ContainerCartography(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerCartography(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(Containers.CARTOGRAPHY_TABLE, i);
        this.inventory = new InventorySubcontainer(2) {
            @Override
            public void update() {
                ContainerCartography.this.a((IInventory) this);
                super.update();
            }
        };
        this.resultInventory = new InventoryCraftResult() {
            @Override
            public void update() {
                ContainerCartography.this.a((IInventory) this);
                super.update();
            }
        };
        this.containerAccess = containeraccess;
        this.a(new Slot(this.inventory, 0, 15, 15) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() == Items.FILLED_MAP;
            }
        });
        this.a(new Slot(this.inventory, 1, 15, 52) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                Item item = itemstack.getItem();

                return item == Items.PAPER || item == Items.MAP || item == Items.dP;
            }
        });
        this.a(new Slot(this.resultInventory, 2, 145, 39) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                ((Slot) ContainerCartography.this.slots.get(0)).a(1);
                ((Slot) ContainerCartography.this.slots.get(1)).a(1);
                itemstack.getItem().b(itemstack, entityhuman.world, entityhuman);
                containeraccess.a((world, blockposition) -> {
                    long j = world.getTime();

                    if (ContainerCartography.this.e != j) {
                        world.playSound((EntityHuman) null, blockposition, SoundEffects.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        ContainerCartography.this.e = j;
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

    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return a(this.containerAccess, entityhuman, Blocks.CARTOGRAPHY_TABLE);
    }

    @Override
    public void a(IInventory iinventory) {
        ItemStack itemstack = this.inventory.getItem(0);
        ItemStack itemstack1 = this.inventory.getItem(1);
        ItemStack itemstack2 = this.resultInventory.getItem(2);

        if (!itemstack2.isEmpty() && (itemstack.isEmpty() || itemstack1.isEmpty())) {
            this.resultInventory.splitWithoutUpdate(2);
        } else if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
            this.a(itemstack, itemstack1, itemstack2);
        }

    }

    private void a(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2) {
        this.containerAccess.a((world, blockposition) -> {
            Item item = itemstack1.getItem();
            WorldMap worldmap = ItemWorldMap.a(itemstack, world);

            if (worldmap != null) {
                ItemStack itemstack3;

                if (item == Items.PAPER && !worldmap.locked && worldmap.scale < 4) {
                    itemstack3 = itemstack.cloneItemStack();
                    itemstack3.setCount(1);
                    itemstack3.getOrCreateTag().setInt("map_scale_direction", 1);
                    this.c();
                } else if (item == Items.dP && !worldmap.locked) {
                    itemstack3 = itemstack.cloneItemStack();
                    itemstack3.setCount(1);
                    itemstack3.getOrCreateTag().setBoolean("map_to_lock", true);
                    this.c();
                } else {
                    if (item != Items.MAP) {
                        this.resultInventory.splitWithoutUpdate(2);
                        this.c();
                        return;
                    }

                    itemstack3 = itemstack.cloneItemStack();
                    itemstack3.setCount(2);
                    this.c();
                }

                if (!ItemStack.matches(itemstack3, itemstack2)) {
                    this.resultInventory.setItem(2, itemstack3);
                    this.c();
                }

            }
        });
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
            if (i == 2) {
                item.b(itemstack1, entityhuman.world, entityhuman);
                if (!this.a(itemstack1, 3, 39, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 1 && i != 0) {
                if (item == Items.FILLED_MAP) {
                    if (!this.a(itemstack1, 0, 1, false)) {
                        return ItemStack.b;
                    }
                } else if (item != Items.PAPER && item != Items.MAP && item != Items.dP) {
                    if (i >= 3 && i < 30) {
                        if (!this.a(itemstack1, 30, 39, false)) {
                            return ItemStack.b;
                        }
                    } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                        return ItemStack.b;
                    }
                } else if (!this.a(itemstack1, 1, 2, false)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 3, 39, false)) {
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
        this.resultInventory.splitWithoutUpdate(2);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, entityhuman.world, this.inventory);
        });
    }
}
