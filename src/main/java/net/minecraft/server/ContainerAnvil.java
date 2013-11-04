package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bukkit.craftbukkit.inventory.CraftInventoryView; // CraftBukkit

public class ContainerAnvil extends Container {

    private static final Logger f = LogManager.getLogger();
    private IInventory g = new InventoryCraftResult();
    private IInventory h = new ContainerAnvilInventory(this, "Repair", true, 2);
    private World i;
    private int j;
    private int k;
    private int l;
    public int a;
    private int m;
    private String n;
    private final EntityHuman o;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerAnvil(PlayerInventory playerinventory, World world, int i, int j, int k, EntityHuman entityhuman) {
        this.player = playerinventory; // CraftBukkit
        this.i = world;
        this.j = i;
        this.k = j;
        this.l = k;
        this.o = entityhuman;
        this.a(new Slot(this.h, 0, 27, 47));
        this.a(new Slot(this.h, 1, 76, 47));
        this.a((Slot) (new SlotAnvilResult(this, this.g, 2, 134, 47, world, i, j, k)));

        int l;

        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(playerinventory, l, 8 + l * 18, 142));
        }
    }

    public void a(IInventory iinventory) {
        super.a(iinventory);
        if (iinventory == this.h) {
            this.e();
        }
    }

    public void e() {
        ItemStack itemstack = this.h.getItem(0);

        this.a = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;

        if (itemstack == null) {
            this.g.setItem(0, (ItemStack) null);
            this.a = 0;
        } else {
            ItemStack itemstack1 = itemstack.cloneItemStack();
            ItemStack itemstack2 = this.h.getItem(1);
            Map map = EnchantmentManager.a(itemstack1);
            boolean flag = false;
            int k = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());

            this.m = 0;
            int l;
            int i1;
            int j1;
            int k1;
            int l1;
            Iterator iterator;
            Enchantment enchantment;

            if (itemstack2 != null) {
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && Items.ENCHANTED_BOOK.g(itemstack2).size() > 0;
                if (itemstack1.g() && itemstack1.getItem().a(itemstack, itemstack2)) {
                    l = Math.min(itemstack1.j(), itemstack1.l() / 4);
                    if (l <= 0) {
                        this.g.setItem(0, (ItemStack) null);
                        this.a = 0;
                        return;
                    }

                    for (i1 = 0; l > 0 && i1 < itemstack2.count; ++i1) {
                        j1 = itemstack1.j() - l;
                        itemstack1.setData(j1);
                        i += Math.max(1, l / 100) + map.size();
                        l = Math.min(itemstack1.j(), itemstack1.l() / 4);
                    }

                    this.m = i1;
                } else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.g())) {
                        this.g.setItem(0, (ItemStack) null);
                        this.a = 0;
                        return;
                    }

                    if (itemstack1.g() && !flag) {
                        l = itemstack.l() - itemstack.j();
                        i1 = itemstack2.l() - itemstack2.j();
                        j1 = i1 + itemstack1.l() * 12 / 100;
                        int i2 = l + j1;

                        k1 = itemstack1.l() - i2;
                        if (k1 < 0) {
                            k1 = 0;
                        }

                        if (k1 < itemstack1.getData()) {
                            itemstack1.setData(k1);
                            i += Math.max(1, j1 / 100);
                        }
                    }

                    Map map1 = EnchantmentManager.a(itemstack2);

                    iterator = map1.keySet().iterator();

                    while (iterator.hasNext()) {
                        j1 = ((Integer) iterator.next()).intValue();
                        enchantment = Enchantment.byId[j1];
                        k1 = map.containsKey(Integer.valueOf(j1)) ? ((Integer) map.get(Integer.valueOf(j1))).intValue() : 0;
                        l1 = ((Integer) map1.get(Integer.valueOf(j1))).intValue();
                        int j2;

                        if (k1 == l1) {
                            ++l1;
                            j2 = l1;
                        } else {
                            j2 = Math.max(l1, k1);
                        }

                        l1 = j2;
                        int k2 = l1 - k1;
                        boolean flag1 = enchantment.canEnchant(itemstack);

                        if (this.o.abilities.canInstantlyBuild || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                            flag1 = true;
                        }

                        Iterator iterator1 = map.keySet().iterator();

                        while (iterator1.hasNext()) {
                            int l2 = ((Integer) iterator1.next()).intValue();

                            if (l2 != j1 && !enchantment.a(Enchantment.byId[l2])) {
                                flag1 = false;
                                i += k2;
                            }
                        }

                        if (flag1) {
                            if (l1 > enchantment.getMaxLevel()) {
                                l1 = enchantment.getMaxLevel();
                            }

                            map.put(Integer.valueOf(j1), Integer.valueOf(l1));
                            int i3 = 0;

                            switch (enchantment.getRandomWeight()) {
                            case 1:
                                i3 = 8;
                                break;

                            case 2:
                                i3 = 4;

                            case 3:
                            case 4:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            default:
                                break;

                            case 5:
                                i3 = 2;
                                break;

                            case 10:
                                i3 = 1;
                            }

                            if (flag) {
                                i3 = Math.max(1, i3 / 2);
                            }

                            i += i3 * k2;
                        }
                    }
                }
            }

            if (StringUtils.isBlank(this.n)) {
                if (itemstack.hasName()) {
                    j = itemstack.g() ? 7 : itemstack.count * 5;
                    i += j;
                    itemstack1.t();
                }
            } else if (!this.n.equals(itemstack.getName())) {
                j = itemstack.g() ? 7 : itemstack.count * 5;
                i += j;
                if (itemstack.hasName()) {
                    k += j / 2;
                }

                itemstack1.c(this.n);
            }

            l = 0;

            for (iterator = map.keySet().iterator(); iterator.hasNext(); k += l + k1 * l1) {
                j1 = ((Integer) iterator.next()).intValue();
                enchantment = Enchantment.byId[j1];
                k1 = ((Integer) map.get(Integer.valueOf(j1))).intValue();
                l1 = 0;
                ++l;
                switch (enchantment.getRandomWeight()) {
                case 1:
                    l1 = 8;
                    break;

                case 2:
                    l1 = 4;

                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    break;

                case 5:
                    l1 = 2;
                    break;

                case 10:
                    l1 = 1;
                }

                if (flag) {
                    l1 = Math.max(1, l1 / 2);
                }
            }

            if (flag) {
                k = Math.max(1, k / 2);
            }

            this.a = k + i;
            if (i <= 0) {
                itemstack1 = null;
            }

            if (j == i && j > 0 && this.a >= 40) {
                this.a = 39;
            }

            if (this.a >= 40 && !this.o.abilities.canInstantlyBuild) {
                itemstack1 = null;
            }

            if (itemstack1 != null) {
                i1 = itemstack1.getRepairCost();
                if (itemstack2 != null && i1 < itemstack2.getRepairCost()) {
                    i1 = itemstack2.getRepairCost();
                }

                if (itemstack1.hasName()) {
                    i1 -= 9;
                }

                if (i1 < 0) {
                    i1 = 0;
                }

                i1 += 2;
                itemstack1.setRepairCost(i1);
                EnchantmentManager.a(map, itemstack1);
            }

            this.g.setItem(0, itemstack1);
            this.b();
        }
    }

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.a);
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        if (!this.i.isStatic) {
            for (int i = 0; i < this.h.getSize(); ++i) {
                ItemStack itemstack = this.h.splitWithoutUpdate(i);

                if (itemstack != null) {
                    entityhuman.drop(itemstack, false);
                }
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.i.getType(this.j, this.k, this.l) != Blocks.ANVIL ? false : entityhuman.e((double) this.j + 0.5D, (double) this.k + 0.5D, (double) this.l + 0.5D) <= 64.0D;
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
            } else if (i != 0 && i != 1) {
                if (i >= 3 && i < 39 && !this.a(itemstack1, 0, 2, false)) {
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

    public void a(String s) {
        this.n = s;
        if (this.getSlot(2).e()) {
            ItemStack itemstack = this.getSlot(2).getItem();

            if (StringUtils.isBlank(s)) {
                itemstack.t();
            } else {
                itemstack.c(this.n);
            }
        }

        this.e();
    }

    static IInventory a(ContainerAnvil containeranvil) {
        return containeranvil.h;
    }

    static int b(ContainerAnvil containeranvil) {
        return containeranvil.m;
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        org.bukkit.craftbukkit.inventory.CraftInventory inventory = new org.bukkit.craftbukkit.inventory.CraftInventoryAnvil(this.h, this.g);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
