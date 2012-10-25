package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryAnvil;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
// CraftBukkit end

public class ContainerAnvil extends Container {

    private IInventory f = new InventoryCraftResult();
    private IInventory g = new ContainerAnvilInventory(this, "Repair", 2);
    private World h;
    private int i;
    private int j;
    private int k;
    public int e = 0;
    private int l = 0;
    private String m;
    private final EntityHuman n;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;
    // CraftBukkit end

    public ContainerAnvil(PlayerInventory playerinventory, World world, int i, int j, int k, EntityHuman entityhuman) {
        this.player = playerinventory; // CraftBukkit
        this.h = world;
        this.i = i;
        this.j = j;
        this.k = k;
        this.n = entityhuman;
        this.a(new Slot(this.g, 0, 27, 47));
        this.a(new Slot(this.g, 1, 76, 47));
        this.a((Slot) (new SlotAnvilResult(this, this.f, 2, 134, 47, world, i, j, k)));

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
        if (iinventory == this.g) {
            this.d();
        }
    }

    public void d() {
        ItemStack itemstack = this.g.getItem(0);

        this.e = 0;
        int i = 0;
        byte b0 = 0;
        int j = 0;

        if (itemstack == null) {
            this.f.setItem(0, (ItemStack) null);
            this.e = 0;
        } else {
            ItemStack itemstack1 = itemstack.cloneItemStack();
            ItemStack itemstack2 = this.g.getItem(1);
            Map map = EnchantmentManager.a(itemstack1);
            int k = b0 + itemstack.getRepairCost() + (itemstack2 == null ? 0 : itemstack2.getRepairCost());

            this.l = 0;
            int l;
            int i1;
            int j1;
            int k1;
            Enchantment enchantment;
            Iterator iterator;

            if (itemstack2 != null) {
                if (itemstack1.f() && Item.byId[itemstack1.id].a(itemstack, itemstack2)) {
                    l = Math.min(itemstack1.i(), itemstack1.k() / 4);
                    if (l <= 0) {
                        this.f.setItem(0, (ItemStack) null);
                        this.e = 0;
                        return;
                    }

                    for (i1 = 0; l > 0 && i1 < itemstack2.count; ++i1) {
                        j1 = itemstack1.i() - l;
                        itemstack1.setData(j1);
                        i += Math.max(1, l / 100) + map.size();
                        l = Math.min(itemstack1.i(), itemstack1.k() / 4);
                    }

                    this.l = i1;
                } else {
                    if (itemstack1.id != itemstack2.id || !itemstack1.f()) {
                        this.f.setItem(0, (ItemStack) null);
                        this.e = 0;
                        return;
                    }

                    if (itemstack1.f()) {
                        l = itemstack.k() - itemstack.i();
                        i1 = itemstack2.k() - itemstack2.i();
                        j1 = i1 + itemstack1.k() * 12 / 100;
                        int l1 = l + j1;

                        k1 = itemstack1.k() - l1;
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
                        int i2 = ((Integer) map1.get(Integer.valueOf(j1))).intValue();
                        int j2;

                        if (k1 == i2) {
                            ++i2;
                            j2 = i2;
                        } else {
                            j2 = Math.max(i2, k1);
                        }

                        i2 = j2;
                        int k2 = i2 - k1;
                        boolean flag = true;
                        Iterator iterator1 = map.keySet().iterator();

                        while (iterator1.hasNext()) {
                            int l2 = ((Integer) iterator1.next()).intValue();

                            if (l2 != j1 && !enchantment.a(Enchantment.byId[l2])) {
                                flag = false;
                                i += k2;
                            }
                        }

                        if (flag) {
                            if (i2 > enchantment.getMaxLevel()) {
                                i2 = enchantment.getMaxLevel();
                            }

                            map.put(Integer.valueOf(j1), Integer.valueOf(i2));
                            byte b1 = 0;

                            switch (enchantment.getRandomWeight()) {
                            case 1:
                                b1 = 8;
                                break;

                            case 2:
                                b1 = 4;

                            case 3:
                            case 4:
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            default:
                                break;

                            case 5:
                                b1 = 2;
                                break;

                            case 10:
                                b1 = 1;
                            }

                            i += b1 * k2;
                        }
                    }
                }
            }

            if (this.m != null && !this.m.equalsIgnoreCase(itemstack.r()) && this.m.length() > 0) {
                j = itemstack.f() ? 7 : itemstack.count * 5;
                i += j;
                if (itemstack.s()) {
                    k += j / 2;
                }

                itemstack1.c(this.m);
            }

            l = 0;

            byte b2;

            for (iterator = map.keySet().iterator(); iterator.hasNext(); k += l + k1 * b2) {
                j1 = ((Integer) iterator.next()).intValue();
                enchantment = Enchantment.byId[j1];
                k1 = ((Integer) map.get(Integer.valueOf(j1))).intValue();
                b2 = 0;
                ++l;
                switch (enchantment.getRandomWeight()) {
                case 1:
                    b2 = 8;
                    break;

                case 2:
                    b2 = 4;

                case 3:
                case 4:
                case 6:
                case 7:
                case 8:
                case 9:
                default:
                    break;

                case 5:
                    b2 = 2;
                    break;

                case 10:
                    b2 = 1;
                }
            }

            this.e = k + i;
            if (i <= 0) {
                itemstack1 = null;
            }

            if (j == i && j > 0 && this.e >= 40) {
                System.out.println("Naming an item only, cost too high; giving discount to cap cost to 39 levels");
                this.e = 39;
            }

            if (this.e >= 40 && !this.n.abilities.canInstantlyBuild) {
                itemstack1 = null;
            }

            if (itemstack1 != null) {
                i1 = itemstack1.getRepairCost();
                if (itemstack2 != null && i1 < itemstack2.getRepairCost()) {
                    i1 = itemstack2.getRepairCost();
                }

                if (itemstack1.s()) {
                    i1 -= 5;
                }

                if (i1 < 0) {
                    i1 = 0;
                }

                i1 += 2;
                itemstack1.setRepairCost(i1);
                EnchantmentManager.a(map, itemstack1);
            }

            this.f.setItem(0, itemstack1);
            this.b();
        }
    }

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.e);
    }

    public void a(EntityHuman entityhuman) {
        super.a(entityhuman);
        if (!this.h.isStatic) {
            for (int i = 0; i < this.g.getSize(); ++i) {
                ItemStack itemstack = this.g.splitWithoutUpdate(i);

                if (itemstack != null) {
                    entityhuman.drop(itemstack);
                }
            }
        }
    }

    public boolean c(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.h.getTypeId(this.i, this.j, this.k) != Block.ANVIL.id ? false : entityhuman.e((double) this.i + 0.5D, (double) this.j + 0.5D, (double) this.k + 0.5D) <= 64.0D;
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.b.get(i);

        if (slot != null && slot.d()) {
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
                slot.e();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.a(entityhuman, itemstack1);
        }

        return itemstack;
    }

    public void a(String s) {
        this.m = s;
        if (this.getSlot(2).d()) {
            this.getSlot(2).getItem().c(this.m);
        }

        this.d();
    }

    static IInventory a(ContainerAnvil containeranvil) {
        return containeranvil.g;
    }

    static int b(ContainerAnvil containeranvil) {
        return containeranvil.l;
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventory inventory = new CraftInventoryAnvil(this.g);
        bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
