package net.minecraft.server;

import java.util.List;
import java.util.Random;

// CraftBukkit start
import java.util.Collections;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.inventory.CraftInventoryEnchanting;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerEnchantTable extends Container {

    private final IInventory enchantSlots;
    private final ContainerAccess containerAccess;
    private final Random h;
    private final ContainerProperty i;
    public final int[] costs;
    public final int[] enchantments;
    public final int[] levels;
    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;
    // CraftBukkit end

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

            // CraftBukkit start
            @Override
            public Location getLocation() {
                return containeraccess.getLocation();
            }
            // CraftBukkit end
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
        // CraftBukkit start
        player = (Player) playerinventory.player.getBukkitEntity();
        // CraftBukkit end
    }

    @Override
    public void a(IInventory iinventory) {
        if (iinventory == this.enchantSlots) {
            ItemStack itemstack = iinventory.getItem(0);

            if (!itemstack.isEmpty()) { // CraftBukkit - relax condition
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

                                this.enchantments[j] = IRegistry.ENCHANTMENT.a(weightedrandomenchant.enchantment); // CraftBukkit - decompile error
                                this.levels[j] = weightedrandomenchant.level;
                            }
                        }
                    }

                    // CraftBukkit start
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack);
                    org.bukkit.enchantments.EnchantmentOffer[] offers = new EnchantmentOffer[3];
                    for (j = 0; j < 3; ++j) {
                        org.bukkit.enchantments.Enchantment enchantment = (this.enchantments[j] >= 0) ? org.bukkit.enchantments.Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(IRegistry.ENCHANTMENT.getKey(IRegistry.ENCHANTMENT.fromId(this.enchantments[j])))) : null;
                        offers[j] = (enchantment != null) ? new EnchantmentOffer(enchantment, this.levels[j], this.costs[j]) : null;
                    }

                    PrepareItemEnchantEvent event = new PrepareItemEnchantEvent(player, this.getBukkitView(), containerAccess.getLocation().getBlock(), item, offers, i);
                    event.setCancelled(!itemstack.canEnchant());
                    world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        for (j = 0; j < 3; ++j) {
                            this.costs[j] = 0;
                            this.enchantments[j] = -1;
                            this.levels[j] = -1;
                        }
                        return;
                    }

                    for (j = 0; j < 3; j++) {
                        EnchantmentOffer offer = event.getOffers()[j];
                        if (offer != null) {
                            this.costs[j] = offer.getCost();
                            this.enchantments[j] = IRegistry.ENCHANTMENT.a(IRegistry.ENCHANTMENT.get(CraftNamespacedKey.toMinecraft(offer.getEnchantment().getKey())));
                            this.levels[j] = offer.getEnchantmentLevel();
                        } else {
                            this.costs[j] = 0;
                            this.enchantments[j] = -1;
                            this.levels[j] = -1;
                        }
                    }
                    // CraftBukkit end

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

                // CraftBukkit start
                if (true || !list.isEmpty()) {
                    // entityhuman.enchantDone(itemstack, j); // Moved down
                    boolean flag = itemstack.getItem() == Items.BOOK;
                    Map<org.bukkit.enchantments.Enchantment, Integer> enchants = new java.util.HashMap<org.bukkit.enchantments.Enchantment, Integer>();
                    for (Object obj : list) {
                        WeightedRandomEnchant instance = (WeightedRandomEnchant) obj;
                        enchants.put(org.bukkit.enchantments.Enchantment.getByKey(CraftNamespacedKey.fromMinecraft(IRegistry.ENCHANTMENT.getKey(instance.enchantment))), instance.level);
                    }
                    CraftItemStack item = CraftItemStack.asCraftMirror(itemstack2);

                    EnchantItemEvent event = new EnchantItemEvent((Player) entityhuman.getBukkitEntity(), this.getBukkitView(), containerAccess.getLocation().getBlock(), item, this.costs[i], enchants, i);
                    world.getServer().getPluginManager().callEvent(event);

                    int level = event.getExpLevelCost();
                    if (event.isCancelled() || (level > entityhuman.expLevel && !entityhuman.abilities.canInstantlyBuild) || event.getEnchantsToAdd().isEmpty()) {
                        return;
                    }

                    if (flag) {
                        itemstack2 = new ItemStack(Items.ENCHANTED_BOOK);
                        NBTTagCompound nbttagcompound = itemstack.getTag();

                        if (nbttagcompound != null) {
                            itemstack2.setTag(nbttagcompound.clone());
                        }

                        this.enchantSlots.setItem(0, itemstack2);
                    }

                    for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : event.getEnchantsToAdd().entrySet()) {
                        try {
                            if (flag) {
                                NamespacedKey enchantId = entry.getKey().getKey();
                                Enchantment nms = IRegistry.ENCHANTMENT.get(CraftNamespacedKey.toMinecraft(enchantId));
                                if (nms == null) {
                                    continue;
                                }

                                WeightedRandomEnchant weightedrandomenchant = new WeightedRandomEnchant(nms, entry.getValue());
                                ItemEnchantedBook.a(itemstack2, weightedrandomenchant);
                            } else {
                                item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                            }
                        } catch (IllegalArgumentException e) {
                            /* Just swallow invalid enchantments */
                        }
                    }

                    entityhuman.enchantDone(itemstack, j);
                    // CraftBukkit end

                    // CraftBukkit - TODO: let plugins change this
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
        if (!this.checkReachable) return true; // CraftBukkit
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

    // CraftBukkit start
    @Override
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
