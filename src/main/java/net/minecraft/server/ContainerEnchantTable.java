package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.Map;
import java.util.HashMap;

import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerEnchantTable extends Container {

    // CraftBukkit start - make type specific (changed from IInventory)
    public ContainerEnchantTableInventory a = new ContainerEnchantTableInventory(this, "Enchant", 1);
    // CraftBukkit end
    private World h;
    private int i;
    private int j;
    private int k;
    private Random l = new Random();
    public long b;
    public int[] c = new int[3];
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;
    // CraftBukkit end

    public ContainerEnchantTable(PlayerInventory playerinventory, World world, int i, int j, int k) {
        this.h = world;
        this.i = i;
        this.j = j;
        this.k = k;
        this.a((Slot) (new SlotEnchant(this, this.a, 0, 25, 47)));

        int l;

        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(playerinventory, l, 8 + l * 18, 142));
        }
        player = (Player) playerinventory.d.getBukkitEntity();    // CraftBukkit
    }

    public void a(ICrafting icrafting) {
        super.a(icrafting);
        icrafting.a(this, 0, this.c[0]);
        icrafting.a(this, 1, this.c[1]);
        icrafting.a(this, 2, this.c[2]);
    }

    public void a() {
        super.a();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            icrafting.a(this, 0, this.c[0]);
            icrafting.a(this, 1, this.c[1]);
            icrafting.a(this, 2, this.c[2]);
        }
    }

    public void a(IInventory iinventory) {
        if (iinventory == this.a) {
            ItemStack itemstack = iinventory.getItem(0);
            int i;

            if (itemstack != null && itemstack.q()) {
                this.b = this.l.nextLong();
                if (!this.h.isStatic) {
                    i = 0;

                    int j;

                    for (j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.h.isEmpty(this.i + k, this.j, this.k + j) && this.h.isEmpty(this.i + k, this.j + 1, this.k + j)) {
                                if (this.h.getTypeId(this.i + k * 2, this.j, this.k + j * 2) == Block.BOOKSHELF.id) {
                                    ++i;
                                }

                                if (this.h.getTypeId(this.i + k * 2, this.j + 1, this.k + j * 2) == Block.BOOKSHELF.id) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (this.h.getTypeId(this.i + k * 2, this.j, this.k + j) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.h.getTypeId(this.i + k * 2, this.j + 1, this.k + j) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.h.getTypeId(this.i + k, this.j, this.k + j * 2) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.h.getTypeId(this.i + k, this.j + 1, this.k + j * 2) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    for (j = 0; j < 3; ++j) {
                        this.c[j] = EnchantmentManager.a(this.l, j, i, itemstack);
                    }

                    // CraftBukkit start
                    CraftItemStack item = new CraftItemStack(itemstack);
                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(), h.getWorld().getBlockAt(this.i, this.j, this.k), item, this.c, i);
                    h.getServer().getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        for (i = 0; i < 3; ++i) {
                            this.c[i] = 0;
                        }
                        return;
                    }
                    // CraftBukkit end
                    this.a();
                }
            } else {
                for (i = 0; i < 3; ++i) {
                    this.c[i] = 0;
                }
            }
        }
    }

    public boolean a(EntityHuman entityhuman, int i) {
        ItemStack itemstack = this.a.getItem(0);

        if (this.c[i] > 0 && itemstack != null && entityhuman.expLevel >= this.c[i]) {
            if (!this.h.isStatic) {
                List list = EnchantmentManager.a(this.l, itemstack, this.c[i]);

                // CraftBukkit start
                Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new HashMap<org.bukkit.enchantments.Enchantment, Integer>();
                for (Object o : list) {
                    EnchantmentInstance e = (EnchantmentInstance) o;
                    enchants.put(org.bukkit.enchantments.Enchantment.getById(e.a.id), e.b);
                }
                CraftItemStack item = new CraftItemStack(itemstack);
                EnchantItemEvent event = new EnchantItemEvent((Player) entityhuman.bukkitEntity, this.getBukkitView(), h.getWorld().getBlockAt(this.i, this.j, this.k), item, this.c[i], enchants, i);
                h.getServer().getPluginManager().callEvent(event);

                int level = event.getExpLevelCost();
                if (event.isCancelled() || (level > entityhuman.expLevel)) {
                    return false;
                }
                if (enchants.isEmpty() == false) {
                    entityhuman.levelDown(level);
                    for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> ent : event.getEnchantsToAdd().entrySet()) {
                        try {
                            item.addEnchantment(ent.getKey(), ent.getValue());
                        } catch (IllegalArgumentException iax) {
                            /* Just swallow invalid enchantments */
                        }
                    }
                    // CraftBukkit end

                    this.a(this.a);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void a(EntityHuman entityhuman) {
        super.a(entityhuman);
        if (!this.h.isStatic) {
            ItemStack itemstack = this.a.getItem(0);

            if (itemstack != null) {
                entityhuman.b(itemstack);
            }
        }
    }

    public boolean b(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.h.getTypeId(this.i, this.j, this.k) != Block.ENCHANTMENT_TABLE.id ? false : entityhuman.e((double) this.i + 0.5D, (double) this.j + 0.5D, (double) this.k + 0.5D) <= 64.0D;
    }

    public ItemStack a(int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.e.get(i);

        if (slot != null && slot.c()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i != 0) {
                return null;
            }

            if (!this.a(itemstack1, 1, 37, true)) {
                return null;
            }

            if (itemstack1.count == 0) {
                slot.c((ItemStack) null);
            } else {
                slot.d();
            }

            if (itemstack1.count == itemstack.count) {
                return null;
            }

            slot.b(itemstack1);
        }

        return itemstack;
    }

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }
        CraftInventoryEnchanting inventory = new CraftInventoryEnchanting(this.a);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
