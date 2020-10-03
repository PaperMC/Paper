package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContainerAnvil extends ContainerAnvilAbstract {

    private static final Logger LOGGER = LogManager.getLogger();
    private int h;
    public String renameText;
    public final ContainerProperty levelCost;

    public ContainerAnvil(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerAnvil(int i, PlayerInventory playerinventory, ContainerAccess containeraccess) {
        super(Containers.ANVIL, i, playerinventory, containeraccess);
        this.levelCost = ContainerProperty.a();
        this.a(this.levelCost);
    }

    @Override
    protected boolean a(IBlockData iblockdata) {
        return iblockdata.a((Tag) TagsBlock.ANVIL);
    }

    @Override
    protected boolean b(EntityHuman entityhuman, boolean flag) {
        return (entityhuman.abilities.canInstantlyBuild || entityhuman.expLevel >= this.levelCost.get()) && this.levelCost.get() > 0;
    }

    @Override
    protected ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
        if (!entityhuman.abilities.canInstantlyBuild) {
            entityhuman.levelDown(-this.levelCost.get());
        }

        this.repairInventory.setItem(0, ItemStack.b);
        if (this.h > 0) {
            ItemStack itemstack1 = this.repairInventory.getItem(1);

            if (!itemstack1.isEmpty() && itemstack1.getCount() > this.h) {
                itemstack1.subtract(this.h);
                this.repairInventory.setItem(1, itemstack1);
            } else {
                this.repairInventory.setItem(1, ItemStack.b);
            }
        } else {
            this.repairInventory.setItem(1, ItemStack.b);
        }

        this.levelCost.set(0);
        this.containerAccess.a((world, blockposition) -> {
            IBlockData iblockdata = world.getType(blockposition);

            if (!entityhuman.abilities.canInstantlyBuild && iblockdata.a((Tag) TagsBlock.ANVIL) && entityhuman.getRandom().nextFloat() < 0.12F) {
                IBlockData iblockdata1 = BlockAnvil.c(iblockdata);

                if (iblockdata1 == null) {
                    world.a(blockposition, false);
                    world.triggerEffect(1029, blockposition, 0);
                } else {
                    world.setTypeAndData(blockposition, iblockdata1, 2);
                    world.triggerEffect(1030, blockposition, 0);
                }
            } else {
                world.triggerEffect(1030, blockposition, 0);
            }

        });
        return itemstack;
    }

    @Override
    public void e() {
        ItemStack itemstack = this.repairInventory.getItem(0);

        this.levelCost.set(1);
        int i = 0;
        byte b0 = 0;
        byte b1 = 0;

        if (itemstack.isEmpty()) {
            this.resultInventory.setItem(0, ItemStack.b);
            this.levelCost.set(0);
        } else {
            ItemStack itemstack1 = itemstack.cloneItemStack();
            ItemStack itemstack2 = this.repairInventory.getItem(1);
            Map<Enchantment, Integer> map = EnchantmentManager.a(itemstack1);
            int j = b0 + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());

            this.h = 0;
            if (!itemstack2.isEmpty()) {
                boolean flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.d(itemstack2).isEmpty();
                int k;
                int l;
                int i1;

                if (itemstack1.e() && itemstack1.getItem().a(itemstack, itemstack2)) {
                    k = Math.min(itemstack1.getDamage(), itemstack1.h() / 4);
                    if (k <= 0) {
                        this.resultInventory.setItem(0, ItemStack.b);
                        this.levelCost.set(0);
                        return;
                    }

                    for (i1 = 0; k > 0 && i1 < itemstack2.getCount(); ++i1) {
                        l = itemstack1.getDamage() - k;
                        itemstack1.setDamage(l);
                        ++i;
                        k = Math.min(itemstack1.getDamage(), itemstack1.h() / 4);
                    }

                    this.h = i1;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.e())) {
                        this.resultInventory.setItem(0, ItemStack.b);
                        this.levelCost.set(0);
                        return;
                    }

                    if (itemstack1.e() && !flag) {
                        k = itemstack.h() - itemstack.getDamage();
                        i1 = itemstack2.h() - itemstack2.getDamage();
                        l = i1 + itemstack1.h() * 12 / 100;
                        int j1 = k + l;
                        int k1 = itemstack1.h() - j1;

                        if (k1 < 0) {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.getDamage()) {
                            itemstack1.setDamage(k1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentManager.a(itemstack2);
                    boolean flag1 = false;
                    boolean flag2 = false;
                    Iterator iterator = map1.keySet().iterator();

                    while (iterator.hasNext()) {
                        Enchantment enchantment = (Enchantment) iterator.next();

                        if (enchantment != null) {
                            int l1 = (Integer) map.getOrDefault(enchantment, 0);
                            int i2 = (Integer) map1.get(enchantment);

                            i2 = l1 == i2 ? i2 + 1 : Math.max(i2, l1);
                            boolean flag3 = enchantment.canEnchant(itemstack);

                            if (this.player.abilities.canInstantlyBuild || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                                flag3 = true;
                            }

                            Iterator iterator1 = map.keySet().iterator();

                            while (iterator1.hasNext()) {
                                Enchantment enchantment1 = (Enchantment) iterator1.next();

                                if (enchantment1 != enchantment && !enchantment.isCompatible(enchantment1)) {
                                    flag3 = false;
                                    ++i;
                                }
                            }

                            if (!flag3) {
                                flag2 = true;
                            } else {
                                flag1 = true;
                                if (i2 > enchantment.getMaxLevel()) {
                                    i2 = enchantment.getMaxLevel();
                                }

                                map.put(enchantment, i2);
                                int j2 = 0;

                                switch (enchantment.d()) {
                                    case COMMON:
                                        j2 = 1;
                                        break;
                                    case UNCOMMON:
                                        j2 = 2;
                                        break;
                                    case RARE:
                                        j2 = 4;
                                        break;
                                    case VERY_RARE:
                                        j2 = 8;
                                }

                                if (flag) {
                                    j2 = Math.max(1, j2 / 2);
                                }

                                i += j2 * i2;
                                if (itemstack.getCount() > 1) {
                                    i = 40;
                                }
                            }
                        }
                    }

                    if (flag2 && !flag1) {
                        this.resultInventory.setItem(0, ItemStack.b);
                        this.levelCost.set(0);
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.renameText)) {
                if (itemstack.hasName()) {
                    b1 = 1;
                    i += b1;
                    itemstack1.s();
                }
            } else if (!this.renameText.equals(itemstack.getName().getString())) {
                b1 = 1;
                i += b1;
                itemstack1.a((IChatBaseComponent) (new ChatComponentText(this.renameText)));
            }

            this.levelCost.set(j + i);
            if (i <= 0) {
                itemstack1 = ItemStack.b;
            }

            if (b1 == i && b1 > 0 && this.levelCost.get() >= 40) {
                this.levelCost.set(39);
            }

            if (this.levelCost.get() >= 40 && !this.player.abilities.canInstantlyBuild) {
                itemstack1 = ItemStack.b;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getRepairCost();

                if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
                    k2 = itemstack2.getRepairCost();
                }

                if (b1 != i || b1 == 0) {
                    k2 = d(k2);
                }

                itemstack1.setRepairCost(k2);
                EnchantmentManager.a(map, itemstack1);
            }

            this.resultInventory.setItem(0, itemstack1);
            this.c();
        }
    }

    public static int d(int i) {
        return i * 2 + 1;
    }

    public void a(String s) {
        this.renameText = s;
        if (this.getSlot(2).hasItem()) {
            ItemStack itemstack = this.getSlot(2).getItem();

            if (StringUtils.isBlank(s)) {
                itemstack.s();
            } else {
                itemstack.a((IChatBaseComponent) (new ChatComponentText(this.renameText)));
            }
        }

        this.e();
    }
}
