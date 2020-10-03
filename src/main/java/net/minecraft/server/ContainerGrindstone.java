package net.minecraft.server;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftInventoryGrindstone;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerGrindstone extends Container {

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryGrindstone inventory = new CraftInventoryGrindstone(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
    private final IInventory resultInventory;
    private final IInventory craftInventory;
    private final ContainerAccess containerAccess;

    public ContainerGrindstone(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerGrindstone(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(Containers.GRINDSTONE, i);
        this.resultInventory = new InventoryCraftResult();
        this.craftInventory = new InventorySubcontainer(2) {
            @Override
            public void update() {
                super.update();
                ContainerGrindstone.this.a((IInventory) this);
            }

            // CraftBukkit start
            @Override
            public Location getLocation() {
                return containeraccess.getLocation();
            }
            // CraftBukkit end
        };
        this.containerAccess = containeraccess;
        this.a(new Slot(this.craftInventory, 0, 49, 19) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.e() || itemstack.getItem() == Items.ENCHANTED_BOOK || itemstack.hasEnchantments();
            }
        });
        this.a(new Slot(this.craftInventory, 1, 49, 40) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.e() || itemstack.getItem() == Items.ENCHANTED_BOOK || itemstack.hasEnchantments();
            }
        });
        this.a(new Slot(this.resultInventory, 2, 129, 34) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                containeraccess.a((world, blockposition) -> {
                    int j = this.a(world);

                    while (j > 0) {
                        int k = EntityExperienceOrb.getOrbValue(j);

                        j -= k;
                        world.addEntity(new EntityExperienceOrb(world, (double) blockposition.getX(), (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, k));
                    }

                    world.triggerEffect(1042, blockposition, 0);
                });
                ContainerGrindstone.this.craftInventory.setItem(0, ItemStack.b);
                ContainerGrindstone.this.craftInventory.setItem(1, ItemStack.b);
                return itemstack;
            }

            private int a(World world) {
                byte b0 = 0;
                int j = b0 + this.e(ContainerGrindstone.this.craftInventory.getItem(0));

                j += this.e(ContainerGrindstone.this.craftInventory.getItem(1));
                if (j > 0) {
                    int k = (int) Math.ceil((double) j / 2.0D);

                    return k + world.random.nextInt(k);
                } else {
                    return 0;
                }
            }

            private int e(ItemStack itemstack) {
                int j = 0;
                Map<Enchantment, Integer> map = EnchantmentManager.a(itemstack);
                Iterator iterator = map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Entry<Enchantment, Integer> entry = (Entry) iterator.next();
                    Enchantment enchantment = (Enchantment) entry.getKey();
                    Integer integer = (Integer) entry.getValue();

                    if (!enchantment.c()) {
                        j += enchantment.a(integer);
                    }
                }

                return j;
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

        player = (Player) playerinventory.player.getBukkitEntity(); // CraftBukkit
    }

    @Override
    public void a(IInventory iinventory) {
        super.a(iinventory);
        if (iinventory == this.craftInventory) {
            this.e();
        }

    }

    private void e() {
        ItemStack itemstack = this.craftInventory.getItem(0);
        ItemStack itemstack1 = this.craftInventory.getItem(1);
        boolean flag = !itemstack.isEmpty() || !itemstack1.isEmpty();
        boolean flag1 = !itemstack.isEmpty() && !itemstack1.isEmpty();

        if (flag) {
            boolean flag2 = !itemstack.isEmpty() && itemstack.getItem() != Items.ENCHANTED_BOOK && !itemstack.hasEnchantments() || !itemstack1.isEmpty() && itemstack1.getItem() != Items.ENCHANTED_BOOK && !itemstack1.hasEnchantments();

            if (itemstack.getCount() > 1 || itemstack1.getCount() > 1 || !flag1 && flag2) {
                this.resultInventory.setItem(0, ItemStack.b);
                this.c();
                return;
            }

            byte b0 = 1;
            int i;
            ItemStack itemstack2;

            if (flag1) {
                if (itemstack.getItem() != itemstack1.getItem()) {
                    this.resultInventory.setItem(0, ItemStack.b);
                    this.c();
                    return;
                }

                Item item = itemstack.getItem();
                int j = item.getMaxDurability() - itemstack.getDamage();
                int k = item.getMaxDurability() - itemstack1.getDamage();
                int l = j + k + item.getMaxDurability() * 5 / 100;

                i = Math.max(item.getMaxDurability() - l, 0);
                itemstack2 = this.b(itemstack, itemstack1);
                if (!itemstack2.e()) {
                    if (!ItemStack.matches(itemstack, itemstack1)) {
                        this.resultInventory.setItem(0, ItemStack.b);
                        this.c();
                        return;
                    }

                    b0 = 2;
                }
            } else {
                boolean flag3 = !itemstack.isEmpty();

                i = flag3 ? itemstack.getDamage() : itemstack1.getDamage();
                itemstack2 = flag3 ? itemstack : itemstack1;
            }

            this.resultInventory.setItem(0, this.a(itemstack2, i, b0));
        } else {
            this.resultInventory.setItem(0, ItemStack.b);
        }

        this.c();
    }

    private ItemStack b(ItemStack itemstack, ItemStack itemstack1) {
        ItemStack itemstack2 = itemstack.cloneItemStack();
        Map<Enchantment, Integer> map = EnchantmentManager.a(itemstack1);
        Iterator iterator = map.entrySet().iterator();

        while (iterator.hasNext()) {
            Entry<Enchantment, Integer> entry = (Entry) iterator.next();
            Enchantment enchantment = (Enchantment) entry.getKey();

            if (!enchantment.c() || EnchantmentManager.getEnchantmentLevel(enchantment, itemstack2) == 0) {
                itemstack2.addEnchantment(enchantment, (Integer) entry.getValue());
            }
        }

        return itemstack2;
    }

    private ItemStack a(ItemStack itemstack, int i, int j) {
        ItemStack itemstack1 = itemstack.cloneItemStack();

        itemstack1.removeTag("Enchantments");
        itemstack1.removeTag("StoredEnchantments");
        if (i > 0) {
            itemstack1.setDamage(i);
        } else {
            itemstack1.removeTag("Damage");
        }

        itemstack1.setCount(j);
        Map<Enchantment, Integer> map = (Map) EnchantmentManager.a(itemstack).entrySet().stream().filter((entry) -> {
            return ((Enchantment) entry.getKey()).c();
        }).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        EnchantmentManager.a(map, itemstack1);
        itemstack1.setRepairCost(0);
        if (itemstack1.getItem() == Items.ENCHANTED_BOOK && map.size() == 0) {
            itemstack1 = new ItemStack(Items.BOOK);
            if (itemstack.hasName()) {
                itemstack1.a(itemstack.getName());
            }
        }

        for (int k = 0; k < map.size(); ++k) {
            itemstack1.setRepairCost(ContainerAnvil.d(itemstack1.getRepairCost()));
        }

        return itemstack1;
    }

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, world, this.craftInventory);
        });
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return a(this.containerAccess, entityhuman, Blocks.GRINDSTONE);
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            ItemStack itemstack2 = this.craftInventory.getItem(0);
            ItemStack itemstack3 = this.craftInventory.getItem(1);

            if (i == 2) {
                if (!this.a(itemstack1, 3, 39, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != 0 && i != 1) {
                if (!itemstack2.isEmpty() && !itemstack3.isEmpty()) {
                    if (i >= 3 && i < 30) {
                        if (!this.a(itemstack1, 30, 39, false)) {
                            return ItemStack.b;
                        }
                    } else if (i >= 30 && i < 39 && !this.a(itemstack1, 3, 30, false)) {
                        return ItemStack.b;
                    }
                } else if (!this.a(itemstack1, 0, 2, false)) {
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
}
