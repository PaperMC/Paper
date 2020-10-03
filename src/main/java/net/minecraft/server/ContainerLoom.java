package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Location;
import org.bukkit.craftbukkit.inventory.CraftInventoryLoom;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.Player;
// CraftBukkit end

public class ContainerLoom extends Container {

    // CraftBukkit start
    private CraftInventoryView bukkitEntity = null;
    private Player player;

    @Override
    public CraftInventoryView getBukkitView() {
        if (bukkitEntity != null) {
            return bukkitEntity;
        }

        CraftInventoryLoom inventory = new CraftInventoryLoom(this.craftInventory, this.resultInventory);
        bukkitEntity = new CraftInventoryView(this.player, inventory, this);
        return bukkitEntity;
    }
    // CraftBukkit end
    private final ContainerAccess containerAccess;
    private final ContainerProperty d;
    private Runnable e;
    private final Slot f;
    private final Slot g;
    private final Slot h;
    private final Slot i;
    private long j;
    private final IInventory craftInventory;
    private final IInventory resultInventory;

    public ContainerLoom(int i, PlayerInventory playerinventory) {
        this(i, playerinventory, ContainerAccess.a);
    }

    public ContainerLoom(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(Containers.LOOM, i);
        this.d = ContainerProperty.a();
        this.e = () -> {
        };
        this.craftInventory = new InventorySubcontainer(3) {
            @Override
            public void update() {
                super.update();
                ContainerLoom.this.a((IInventory) this);
                ContainerLoom.this.e.run();
            }

            // CraftBukkit start
            @Override
            public Location getLocation() {
                return containeraccess.getLocation();
            }
            // CraftBukkit end
        };
        this.resultInventory = new InventorySubcontainer(1) {
            @Override
            public void update() {
                super.update();
                ContainerLoom.this.e.run();
            }

            // CraftBukkit start
            @Override
            public Location getLocation() {
                return containeraccess.getLocation();
            }
            // CraftBukkit end
        };
        this.containerAccess = containeraccess;
        this.f = this.a(new Slot(this.craftInventory, 0, 13, 26) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() instanceof ItemBanner;
            }
        });
        this.g = this.a(new Slot(this.craftInventory, 1, 33, 26) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() instanceof ItemDye;
            }
        });
        this.h = this.a(new Slot(this.craftInventory, 2, 23, 45) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return itemstack.getItem() instanceof ItemBannerPattern;
            }
        });
        this.i = this.a(new Slot(this.resultInventory, 0, 143, 58) {
            @Override
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            @Override
            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                ContainerLoom.this.f.a(1);
                ContainerLoom.this.g.a(1);
                if (!ContainerLoom.this.f.hasItem() || !ContainerLoom.this.g.hasItem()) {
                    ContainerLoom.this.d.set(0);
                }

                containeraccess.a((world, blockposition) -> {
                    long j = world.getTime();

                    if (ContainerLoom.this.j != j) {
                        world.playSound((EntityHuman) null, blockposition, SoundEffects.UI_LOOM_TAKE_RESULT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        ContainerLoom.this.j = j;
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

        this.a(this.d);
        player = (Player) playerinventory.player.getBukkitEntity(); // CraftBukkit
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        if (!this.checkReachable) return true; // CraftBukkit
        return a(this.containerAccess, entityhuman, Blocks.LOOM);
    }

    @Override
    public boolean a(EntityHuman entityhuman, int i) {
        if (i > 0 && i <= EnumBannerPatternType.R) {
            this.d.set(i);
            this.j();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void a(IInventory iinventory) {
        ItemStack itemstack = this.f.getItem();
        ItemStack itemstack1 = this.g.getItem();
        ItemStack itemstack2 = this.h.getItem();
        ItemStack itemstack3 = this.i.getItem();

        if (!itemstack3.isEmpty() && (itemstack.isEmpty() || itemstack1.isEmpty() || this.d.get() <= 0 || this.d.get() >= EnumBannerPatternType.P - EnumBannerPatternType.Q && itemstack2.isEmpty())) {
            this.i.set(ItemStack.b);
            this.d.set(0);
        } else if (!itemstack2.isEmpty() && itemstack2.getItem() instanceof ItemBannerPattern) {
            NBTTagCompound nbttagcompound = itemstack.a("BlockEntityTag");
            boolean flag = nbttagcompound.hasKeyOfType("Patterns", 9) && !itemstack.isEmpty() && nbttagcompound.getList("Patterns", 10).size() >= 6;

            if (flag) {
                this.d.set(0);
            } else {
                this.d.set(((ItemBannerPattern) itemstack2.getItem()).b().ordinal());
            }
        }

        this.j();
        this.c();
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.b;
        Slot slot = (Slot) this.slots.get(i);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();

            itemstack = itemstack1.cloneItemStack();
            if (i == this.i.rawSlotIndex) {
                if (!this.a(itemstack1, 4, 40, true)) {
                    return ItemStack.b;
                }

                slot.a(itemstack1, itemstack);
            } else if (i != this.g.rawSlotIndex && i != this.f.rawSlotIndex && i != this.h.rawSlotIndex) {
                if (itemstack1.getItem() instanceof ItemBanner) {
                    if (!this.a(itemstack1, this.f.rawSlotIndex, this.f.rawSlotIndex + 1, false)) {
                        return ItemStack.b;
                    }
                } else if (itemstack1.getItem() instanceof ItemDye) {
                    if (!this.a(itemstack1, this.g.rawSlotIndex, this.g.rawSlotIndex + 1, false)) {
                        return ItemStack.b;
                    }
                } else if (itemstack1.getItem() instanceof ItemBannerPattern) {
                    if (!this.a(itemstack1, this.h.rawSlotIndex, this.h.rawSlotIndex + 1, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 4 && i < 31) {
                    if (!this.a(itemstack1, 31, 40, false)) {
                        return ItemStack.b;
                    }
                } else if (i >= 31 && i < 40 && !this.a(itemstack1, 4, 31, false)) {
                    return ItemStack.b;
                }
            } else if (!this.a(itemstack1, 4, 40, false)) {
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

    @Override
    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.containerAccess.a((world, blockposition) -> {
            this.a(entityhuman, entityhuman.world, this.craftInventory);
        });
    }

    private void j() {
        if (this.d.get() > 0) {
            ItemStack itemstack = this.f.getItem();
            ItemStack itemstack1 = this.g.getItem();
            ItemStack itemstack2 = ItemStack.b;

            if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
                itemstack2 = itemstack.cloneItemStack();
                itemstack2.setCount(1);
                EnumBannerPatternType enumbannerpatterntype = EnumBannerPatternType.values()[this.d.get()];
                EnumColor enumcolor = ((ItemDye) itemstack1.getItem()).d();
                NBTTagCompound nbttagcompound = itemstack2.a("BlockEntityTag");
                NBTTagList nbttaglist;

                if (nbttagcompound.hasKeyOfType("Patterns", 9)) {
                    nbttaglist = nbttagcompound.getList("Patterns", 10);
                    // CraftBukkit start
                    while (nbttaglist.size() > 20) {
                        nbttaglist.remove(20);
                    }
                    // CraftBukkit end
                } else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound.set("Patterns", nbttaglist);
                }

                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.setString("Pattern", enumbannerpatterntype.b());
                nbttagcompound1.setInt("Color", enumcolor.getColorIndex());
                nbttaglist.add(nbttagcompound1);
            }

            if (!ItemStack.matches(itemstack2, this.i.getItem())) {
                this.i.set(itemstack2);
            }
        }

    }
}
