package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.Map;

import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerEnchantTable extends Container {

    // CraftBukkit - make type specific (changed from IInventory)
    public ContainerEnchantTableInventory enchantSlots = new ContainerEnchantTableInventory(this, "Enchant", true, 1);
    private World world;
    private int x;
    private int y;
    private int z;
    private Random l = new Random();
    public long f;
    public int[] costs = new int[3];
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;
    // CraftBukkit end

    public ContainerEnchantTable(PlayerInventory playerinventory, World world, int i, int j, int k) {
        this.world = world;
        this.x = i;
        this.y = j;
        this.z = k;
        this.a((Slot) (new SlotEnchant(this, this.enchantSlots, 0, 25, 47)));

        int l;

        for (l = 0; l < 3; ++l) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.a(new Slot(playerinventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.a(new Slot(playerinventory, l, 8 + l * 18, 142));
        }

        // CraftBukkit start
        player = (Player) playerinventory.player.getBukkitEntity();
        enchantSlots.player = player;
        // CraftBukkit end
    }

    public void addSlotListener(ICrafting icrafting) {
        super.addSlotListener(icrafting);
        icrafting.setContainerData(this, 0, this.costs[0]);
        icrafting.setContainerData(this, 1, this.costs[1]);
        icrafting.setContainerData(this, 2, this.costs[2]);
    }

    public void b() {
        super.b();

        for (int i = 0; i < this.listeners.size(); ++i) {
            ICrafting icrafting = (ICrafting) this.listeners.get(i);

            icrafting.setContainerData(this, 0, this.costs[0]);
            icrafting.setContainerData(this, 1, this.costs[1]);
            icrafting.setContainerData(this, 2, this.costs[2]);
        }
    }

    public void a(IInventory iinventory) {
        if (iinventory == this.enchantSlots) {
            ItemStack itemstack = iinventory.getItem(0);
            int i;

            if (itemstack != null) { // CraftBukkit - relax condition
                this.f = this.l.nextLong();
                if (!this.world.isStatic) {
                    i = 0;

                    int j;

                    for (j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.world.isEmpty(this.x + k, this.y, this.z + j) && this.world.isEmpty(this.x + k, this.y + 1, this.z + j)) {
                                if (this.world.getTypeId(this.x + k * 2, this.y, this.z + j * 2) == Block.BOOKSHELF.id) {
                                    ++i;
                                }

                                if (this.world.getTypeId(this.x + k * 2, this.y + 1, this.z + j * 2) == Block.BOOKSHELF.id) {
                                    ++i;
                                }

                                if (k != 0 && j != 0) {
                                    if (this.world.getTypeId(this.x + k * 2, this.y, this.z + j) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.world.getTypeId(this.x + k * 2, this.y + 1, this.z + j) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.world.getTypeId(this.x + k, this.y, this.z + j * 2) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }

                                    if (this.world.getTypeId(this.x + k, this.y + 1, this.z + j * 2) == Block.BOOKSHELF.id) {
                                        ++i;
                                    }
                                }
                            }
                        }
                    }

                    for (j = 0; j < 3; ++j) {
                        this.costs[j] = EnchantmentManager.a(this.l, j, i, itemstack);
                    }

                    // CraftBukkit start
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(), this.world.getWorld().getBlockAt(this.x, this.y, this.z), item, this.costs, i);
                    event.setCancelled(!itemstack.x());
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        for (i = 0; i < 3; ++i) {
                            this.costs[i] = 0;
                        }
                        return;
                    }
                    // CraftBukkit end

                    this.b();
                }
            } else {
                for (i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                }
            }
        }
    }

    public boolean a(EntityHuman entityhuman, int i) {
        ItemStack itemstack = this.enchantSlots.getItem(0);

        if (this.costs[i] > 0 && itemstack != null && (entityhuman.expLevel >= this.costs[i] || entityhuman.abilities.canInstantlyBuild)) {
            if (!this.world.isStatic) {
                List list = EnchantmentManager.b(this.l, itemstack, this.costs[i]);
                boolean flag = itemstack.id == Item.BOOK.id;

                if (list != null) {
                    // CraftBukkit start
                    Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new java.util.HashMap<org.bukkit.enchantments.Enchantment, Integer>();
                    for (Object obj : list) {
                        EnchantmentInstance instance = (EnchantmentInstance) obj;
                        enchants.put(org.bukkit.enchantments.Enchantment.getById(instance.enchantment.id), instance.level);
                    }
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);

                    EnchantItemEvent event = new EnchantItemEvent((Player) entityhuman.getBukkitEntity(), this.getBukkitView(), this.world.getWorld().getBlockAt(this.x, this.y, this.z), item, this.costs[i], enchants, i);
                    this.world.getServer().getPluginManager().callEvent(event);

                    int level = event.getExpLevelCost();
                    if (event.isCancelled() || (level > entityhuman.expLevel && !entityhuman.abilities.canInstantlyBuild) || enchants.isEmpty()) {
                        return false;
                    }

                    boolean applied = !flag;
                    for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : event.getEnchantsToAdd().entrySet()) {
                        try {
                            if (flag) {
                                int enchantId = entry.getKey().getId();
                                if (Enchantment.byId[enchantId] == null) {
                                    continue;
                                }

                                EnchantmentInstance enchantment = new EnchantmentInstance(enchantId, entry.getValue());
                                Item.ENCHANTED_BOOK.a(itemstack, enchantment);
                                applied = true;
                                itemstack.id = Item.ENCHANTED_BOOK.id;
                                break;
                            } else {
                                item.addEnchantment(entry.getKey(), entry.getValue());
                            }
                        } catch (IllegalArgumentException e) {
                            /* Just swallow invalid enchantments */
                        }
                    }

                    // Only down level if we've applied the enchantments
                    if (applied) {
                        entityhuman.levelDown(-level);
                    }
                    // CraftBukkit end

                    this.a(this.enchantSlots);
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        if (!this.world.isStatic) {
            ItemStack itemstack = this.enchantSlots.splitWithoutUpdate(0);

            if (itemstack != null) {
                entityhuman.drop(itemstack);
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return this.world.getTypeId(this.x, this.y, this.z) != Block.ENCHANTMENT_TABLE.id ? false : entityhuman.e((double) this.x + 0.5D, (double) this.y + 0.5D, (double) this.z + 0.5D) <= 64.0D;
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.c.get(i);

        if (slot != null && slot.e()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == 0) {
                if (!this.a(itemstack1, 1, 37, true)) {
                    return null;
                }
            } else {
                if (((Slot) this.c.get(0)).e() || !((Slot) this.c.get(0)).isAllowed(itemstack1)) {
                    return null;
                }

                if (itemstack1.hasTag() && itemstack1.count == 1) {
                    ((Slot) this.c.get(0)).set(itemstack1.cloneItemStack());
                    itemstack1.count = 0;
                } else if (itemstack1.count >= 1) {
                    ((Slot) this.c.get(0)).set(new ItemStack(itemstack1.id, 1, itemstack1.getData()));
                    --itemstack1.count;
                }
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

    // CraftBukkit start
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryEnchanting inventory = new CraftInventoryEnchanting(this.enchantSlots);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
}
