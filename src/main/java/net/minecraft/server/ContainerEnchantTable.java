package net.minecraft.server;

import java.util.List;
import java.util.Random;

public class ContainerEnchantTable extends Container {

    private final IInventory enchantSlots;
    private final ContainerAccess containerAccess;
    private final Random h;
    private final ContainerProperty i;
    public final int[] costs;
    public final int[] enchantments;
    public final int[] levels;

    public ContainerEnchantTable(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerEnchantTable(int i, PlayerInventory playerinventory, ContainerAccess containeraccess) {
        super(Containers.ENCHANTMENT, i);
        this.enchantSlots = new InventorySubcontainer(2) {
            @Override
            public void update() {
                super.update();
                ContainerEnchantTable.this.a((IInventory) this);
            }
        };
        this.h = new Random();
        this.i = ContainerProperty.a();
        this.costs = new int[3];
        this.enchantments = new int[]{-1, -1, -1};
        this.levels = new int[]{-1, -1, -1};
        this.containerAccess = containeraccess;
        this.a(new Slot(this.enchantSlots, 0, 15, 47) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return true;
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        this.a(new Slot(this.enchantSlots, 1, 35, 47) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() == Items.LAPIS_LAZULI;
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

        this.a(ContainerProperty.a(this.costs, 0));
        this.a(ContainerProperty.a(this.costs, 1));
        this.a(ContainerProperty.a(this.costs, 2));
        this.a(this.i).set(playerinventory.player.eF());
        this.a(ContainerProperty.a(this.enchantments, 0));
        this.a(ContainerProperty.a(this.enchantments, 1));
        this.a(ContainerProperty.a(this.enchantments, 2));
        this.a(ContainerProperty.a(this.levels, 0));
        this.a(ContainerProperty.a(this.levels, 1));
        this.a(ContainerProperty.a(this.levels, 2));
    }

    @Override
    public void a(IInventory iinventory) {
        if (iinventory == this.enchantSlots) {
            ItemStack itemstack = iinventory.getItem(0);

            if (!itemstack.isEmpty() && itemstack.canEnchant()) {
                this.containerAccess.a((world, blockposition) -> {
                    int i = 0;

                    int j;

                    for (j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && world.isEmpty(blockposition.b(k, 0, j)) && world.isEmpty(blockposition.b(k, 1, j))) {
                                if (world.getType(blockposition.b(k * 2, 0, j * 2)).a(Blocks.BOOKSHELF)) {
                                    ++i;
                                }

                                if (world.getType(blockposition.b(k * 2, 1, j * 2)).a(Blocks.BOOKSHELF)) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (world.getType(blockposition.b(k * 2, 0, j)).a(Blocks.BOOKSHELF)) {
                                        ++i;
                                    }

                                    if (world.getType(blockposition.b(k * 2, 1, j)).a(Blocks.BOOKSHELF)) {
                                        ++i;
                                    }

                                    if (world.getType(blockposition.b(k, 0, j * 2)).a(Blocks.BOOKSHELF)) {
                                        ++i;
                                    }

                                    if (world.getType(blockposition.b(k, 1, j * 2)).a(Blocks.BOOKSHELF)) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    this.h.setSeed((long) this.i.get());

                    for (j = 0; j < 3; ++j) {
                        this.costs[j] = EnchantmentManager.a(this.h, j, i, itemstack);
                        this.enchantments[j] = -1;
                        this.levels[j] = -1;
                        if (this.costs[j] < j + 1) {
                            this.costs[j] = 0;
                        }
                    }

                    for (j = 0; j < 3; ++j) {
                        if (this.costs[j] > 0) {
                            List<WeightedRandomEnchant> list = this.a(itemstack, j, this.costs[j]);

                            if (list != null && !list.isEmpty()) {
                                WeightedRandomEnchant weightedrandomenchant = (WeightedRandomEnchant) list.get(this.h.nextInt(list.size()));

                                this.enchantments[j] = IRegistry.ENCHANTMENT.a((Object) weightedrandomenchant.enchantment);
                                this.levels[j] = weightedrandomenchant.level;
                            }
                        }
                    }

                    this.c();
                });
            } else {
                for (int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantments[i] = -1;
                    this.levels[i] = -1;
                }
            }
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman, int i) {
        ItemStack itemstack = this.enchantSlots.getItem(0);
        ItemStack itemstack1 = this.enchantSlots.getItem(1);
        int j = i + 1;

        if ((itemstack1.isEmpty() || itemstack1.getCount() < j) && !entityhuman.abilities.canInstantlyBuild) {
            return false;
        } else if (this.costs[i] > 0 && !itemstack.isEmpty() && (entityhuman.expLevel >= j && entityhuman.expLevel >= this.costs[i] || entityhuman.abilities.canInstantlyBuild)) {
            this.containerAccess.a((world, blockposition) -> {
                ItemStack itemstack2 = itemstack;
                List<WeightedRandomEnchant> list = this.a(itemstack, i, this.costs[i]);

                if (!list.isEmpty()) {
                    entityhuman.enchantDone(itemstack, j);
                    boolean flag = itemstack.getItem() == Items.BOOK;

                    if (flag) {
                        itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                        NBTTagCompound nbttagcompound = itemstack.getTag();

                        if (nbttagcompound != null) {
                            itemstack2.setTag(nbttagcompound.clone());
                        }

                        this.enchantSlots.setItem(0, itemstack2);
                    }

                    for (int k = 0; k < list.size(); ++k) {
                        WeightedRandomEnchant weightedrandomenchant = (WeightedRandomEnchant) list.get(k);

                        if (flag) {
                            ItemEnchantedBook.a(itemstack2, weightedrandomenchant);
                        } else {
                            itemstack2.addEnchantment(weightedrandomenchant.enchantment, weightedrandomenchant.level);
                        }
                    }

                    if (!entityhuman.abilities.canInstantlyBuild) {
                        itemstack1.subtract(j);
                        if (itemstack1.isEmpty()) {
                            this.enchantSlots.setItem(1, ItemStack.b);
                        }
                    }

                    entityhuman.a(StatisticList.ENCHANT_ITEM);
                    if (entityhuman instanceof EntityPlayer) {
                        CriterionTriggers.i.a((EntityPlayer) entityhuman, itemstack2, j);
                    }

                    this.enchantSlots.update();
                    this.i.set(entityhuman.eF());
                    this.a(this.enchantSlots);
                    world.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
                }

            });
            return true;
        } else {
            return false;
        }
    }

    private List<WeightedRandomEnchant> a(ItemStack itemstack, int i, int j) {
        this.h.setSeed((long) (this.i.get() + i));
        List<WeightedRandomEnchant> list = EnchantmentManager.b(this.h, itemstack, j, false);

        if (itemstack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(this.h.nextInt(list.size()));
        }

        return list;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, entityhuman.world, this.enchantSlots);
        });
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return a(this.containerAccess, entityhuman, Blocks.ENCHANTING_TABLE);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 2, 38, true)) {
                    return ItemStack.b;
                }
            } else if (i == 1) {
                if (!this.a(itemstack1, 2, 38, true)) {
                    return ItemStack.b;
                }
            } else if (itemstack1.getItem() == Items.LAPIS_LAZULI) {
                if (!this.a(itemstack1, 1, 2, true)) {
                    return ItemStack.b;
                }
            } else {
                if (((Slot) this.slots.get(0)).hasItem() || !((Slot) this.slots.get(0)).isAllowed(itemstack1)) {
                    return ItemStack.b;
                }

                ItemStack itemstack2 = itemstack1.cloneItemStack();

                itemstack2.setCount(1);
                itemstack1.subtract(1);
                ((Slot) this.slots.get(0)).set(itemstack2);
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
}
