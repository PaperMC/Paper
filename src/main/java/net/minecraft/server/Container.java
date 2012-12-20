package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public abstract class Container {

    public List b = new ArrayList();
    public List c = new ArrayList();
    public int windowId = 0;
    private short a = 0;
    protected List listeners = new ArrayList();
    private Set f = new HashSet();

    // CraftBukkit start
    public boolean checkReachable = true;
    public abstract InventoryView getBukkitView();
    public void transferTo(Container other, org.bukkit.craftbukkit.entity.CraftHumanEntity player) {
        InventoryView source = this.getBukkitView(), destination = other.getBukkitView();
        ((CraftInventory) source.getTopInventory()).getInventory().onClose(player);
        ((CraftInventory) source.getBottomInventory()).getInventory().onClose(player);
        ((CraftInventory) destination.getTopInventory()).getInventory().onOpen(player);
        ((CraftInventory) destination.getBottomInventory()).getInventory().onOpen(player);
    }
    // CraftBukkit end

    public Container() {}

    protected Slot a(Slot slot) {
        slot.g = this.c.size();
        this.c.add(slot);
        this.b.add(null);
        return slot;
    }

    public void addSlotListener(ICrafting icrafting) {
        if (this.listeners.contains(icrafting)) {
            throw new IllegalArgumentException("Listener already listening");
        } else {
            this.listeners.add(icrafting);
            icrafting.a(this, this.a());
            this.b();
        }
    }

    public List a() {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < this.c.size(); ++i) {
            arraylist.add(((Slot) this.c.get(i)).getItem());
        }

        return arraylist;
    }

    public void b() {
        for (int i = 0; i < this.c.size(); ++i) {
            ItemStack itemstack = ((Slot) this.c.get(i)).getItem();
            ItemStack itemstack1 = (ItemStack) this.b.get(i);

            if (!ItemStack.matches(itemstack1, itemstack)) {
                itemstack1 = itemstack == null ? null : itemstack.cloneItemStack();
                this.b.set(i, itemstack1);

                for (int j = 0; j < this.listeners.size(); ++j) {
                    ((ICrafting) this.listeners.get(j)).a(this, i, itemstack1);
                }
            }
        }
    }

    public boolean a(EntityHuman entityhuman, int i) {
        return false;
    }

    public Slot a(IInventory iinventory, int i) {
        for (int j = 0; j < this.c.size(); ++j) {
            Slot slot = (Slot) this.c.get(j);

            if (slot.a(iinventory, i)) {
                return slot;
            }
        }

        return null;
    }

    public Slot getSlot(int i) {
        return (Slot) this.c.get(i);
    }

    public ItemStack b(EntityHuman entityhuman, int i) {
        Slot slot = (Slot) this.c.get(i);

        return slot != null ? slot.getItem() : null;
    }

    public ItemStack clickItem(int i, int j, int k, EntityHuman entityhuman) {
        ItemStack itemstack = null;
        PlayerInventory playerinventory = entityhuman.inventory;
        Slot slot;
        ItemStack itemstack1;
        int l;
        ItemStack itemstack2;

        if ((k == 0 || k == 1) && (j == 0 || j == 1)) {
            if (i == -999) {
                if (playerinventory.getCarried() != null && i == -999) {
                    if (j == 0) {
                        entityhuman.drop(playerinventory.getCarried());
                        playerinventory.setCarried((ItemStack) null);
                    }

                    if (j == 1) {
                        // CraftBukkit start - store a reference
                        ItemStack itemstack3 = playerinventory.getCarried();
                        if (itemstack3.count > 0) {
                            entityhuman.drop(itemstack3.a(1));
                        }

                        if (itemstack3.count == 0) {
                            // CraftBukkit end
                            playerinventory.setCarried((ItemStack) null);
                        }
                    }
                }
            } else if (k == 1) {
                slot = (Slot) this.c.get(i);
                if (slot != null && slot.a(entityhuman)) {
                    itemstack1 = this.b(entityhuman, i);
                    if (itemstack1 != null) {
                        int i1 = itemstack1.id;

                        itemstack = itemstack1.cloneItemStack();
                        if (slot != null && slot.getItem() != null && slot.getItem().id == i1) {
                            this.a(i, j, true, entityhuman);
                        }
                    }
                }
            } else {
                if (i < 0) {
                    return null;
                }

                slot = (Slot) this.c.get(i);
                if (slot != null) {
                    itemstack1 = slot.getItem();
                    ItemStack itemstack3 = playerinventory.getCarried();

                    if (itemstack1 != null) {
                        itemstack = itemstack1.cloneItemStack();
                    }

                    if (itemstack1 == null) {
                        if (itemstack3 != null && slot.isAllowed(itemstack3)) {
                            l = j == 0 ? itemstack3.count : 1;
                            if (l > slot.a()) {
                                l = slot.a();
                            }

                            // CraftBukkit start
                            if (itemstack3.count >= l) {
                                slot.set(itemstack3.a(l));
                            }
                            // CraftBukkit end

                            if (itemstack3.count == 0) {
                                playerinventory.setCarried((ItemStack) null);
                            }
                        }
                    } else if (slot.a(entityhuman)) {
                        if (itemstack3 == null) {
                            l = j == 0 ? itemstack1.count : (itemstack1.count + 1) / 2;
                            itemstack2 = slot.a(l);
                            playerinventory.setCarried(itemstack2);
                            if (itemstack1.count == 0) {
                                slot.set((ItemStack) null);
                            }

                            slot.a(entityhuman, playerinventory.getCarried());
                        } else if (slot.isAllowed(itemstack3)) {
                            if (itemstack1.id == itemstack3.id && itemstack1.getData() == itemstack3.getData() && ItemStack.equals(itemstack1, itemstack3)) {
                                l = j == 0 ? itemstack3.count : 1;
                                if (l > slot.a() - itemstack1.count) {
                                    l = slot.a() - itemstack1.count;
                                }

                                if (l > itemstack3.getMaxStackSize() - itemstack1.count) {
                                    l = itemstack3.getMaxStackSize() - itemstack1.count;
                                }

                                itemstack3.a(l);
                                if (itemstack3.count == 0) {
                                    playerinventory.setCarried((ItemStack) null);
                                }

                                itemstack1.count += l;
                            } else if (itemstack3.count <= slot.a()) {
                                slot.set(itemstack3);
                                playerinventory.setCarried(itemstack1);
                            }
                        } else if (itemstack1.id == itemstack3.id && itemstack3.getMaxStackSize() > 1 && (!itemstack1.usesData() || itemstack1.getData() == itemstack3.getData()) && ItemStack.equals(itemstack1, itemstack3)) {
                            l = itemstack1.count;
                            if (l > 0 && l + itemstack3.count <= itemstack3.getMaxStackSize()) {
                                itemstack3.count += l;
                                itemstack1 = slot.a(l);
                                if (itemstack1.count == 0) {
                                    slot.set((ItemStack) null);
                                }

                                slot.a(entityhuman, playerinventory.getCarried());
                            }
                        }
                    }

                    slot.e();
                }
            }
        } else if (k == 2 && j >= 0 && j < 9) {
            slot = (Slot) this.c.get(i);
            if (slot.a(entityhuman)) {
                itemstack1 = playerinventory.getItem(j);
                boolean flag = itemstack1 == null || slot.inventory == playerinventory && slot.isAllowed(itemstack1);

                l = -1;
                if (!flag) {
                    l = playerinventory.i();
                    flag |= l > -1;
                }

                if (slot.d() && flag) {
                    itemstack2 = slot.getItem();
                    playerinventory.setItem(j, itemstack2);
                    if ((slot.inventory != playerinventory || !slot.isAllowed(itemstack1)) && itemstack1 != null) {
                        if (l > -1) {
                            playerinventory.pickup(itemstack1);
                            slot.a(itemstack2.count);
                            slot.set((ItemStack) null);
                            slot.a(entityhuman, itemstack2);
                        }
                    } else {
                        slot.a(itemstack2.count);
                        slot.set(itemstack1);
                        slot.a(entityhuman, itemstack2);
                    }
                } else if (!slot.d() && itemstack1 != null && slot.isAllowed(itemstack1)) {
                    playerinventory.setItem(j, (ItemStack) null);
                    slot.set(itemstack1);
                }
            }
        } else if (k == 3 && entityhuman.abilities.canInstantlyBuild && playerinventory.getCarried() == null && i >= 0) {
            slot = (Slot) this.c.get(i);
            if (slot != null && slot.d()) {
                itemstack1 = slot.getItem().cloneItemStack();
                itemstack1.count = itemstack1.getMaxStackSize();
                playerinventory.setCarried(itemstack1);
            }
        }

        return itemstack;
    }

    protected void a(int i, int j, boolean flag, EntityHuman entityhuman) {
        this.clickItem(i, j, 1, entityhuman);
    }

    public void b(EntityHuman entityhuman) {
        PlayerInventory playerinventory = entityhuman.inventory;

        if (playerinventory.getCarried() != null) {
            entityhuman.drop(playerinventory.getCarried());
            playerinventory.setCarried((ItemStack) null);
        }
    }

    public void a(IInventory iinventory) {
        this.b();
    }

    public void setItem(int i, ItemStack itemstack) {
        this.getSlot(i).set(itemstack);
    }

    public boolean c(EntityHuman entityhuman) {
        return !this.f.contains(entityhuman);
    }

    public void a(EntityHuman entityhuman, boolean flag) {
        if (flag) {
            this.f.remove(entityhuman);
        } else {
            this.f.add(entityhuman);
        }
    }

    public abstract boolean a(EntityHuman entityhuman);

    protected boolean a(ItemStack itemstack, int i, int j, boolean flag) {
        boolean flag1 = false;
        int k = i;

        if (flag) {
            k = j - 1;
        }

        Slot slot;
        ItemStack itemstack1;

        if (itemstack.isStackable()) {
            while (itemstack.count > 0 && (!flag && k < j || flag && k >= i)) {
                slot = (Slot) this.c.get(k);
                itemstack1 = slot.getItem();
                if (itemstack1 != null && itemstack1.id == itemstack.id && (!itemstack.usesData() || itemstack.getData() == itemstack1.getData()) && ItemStack.equals(itemstack, itemstack1)) {
                    int l = itemstack1.count + itemstack.count;

                    if (l <= itemstack.getMaxStackSize()) {
                        itemstack.count = 0;
                        itemstack1.count = l;
                        slot.e();
                        flag1 = true;
                    } else if (itemstack1.count < itemstack.getMaxStackSize()) {
                        itemstack.count -= itemstack.getMaxStackSize() - itemstack1.count;
                        itemstack1.count = itemstack.getMaxStackSize();
                        slot.e();
                        flag1 = true;
                    }
                }

                if (flag) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        if (itemstack.count > 0) {
            if (flag) {
                k = j - 1;
            } else {
                k = i;
            }

            while (!flag && k < j || flag && k >= i) {
                slot = (Slot) this.c.get(k);
                itemstack1 = slot.getItem();
                if (itemstack1 == null) {
                    slot.set(itemstack.cloneItemStack());
                    slot.e();
                    itemstack.count = 0;
                    flag1 = true;
                    break;
                }

                if (flag) {
                    --k;
                } else {
                    ++k;
                }
            }
        }

        return flag1;
    }
}
