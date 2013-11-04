package net.minecraft.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import java.util.HashMap;
import java.util.Map;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryView;
// CraftBukkit end

public abstract class Container {

    public List b = new ArrayList();
    public List c = new ArrayList();
    public int windowId;
    private int f = -1;
    public int g; // CraftBukkit - private -> public
    private final Set h = new HashSet();
    protected List listeners = new ArrayList();
    private Set i = new HashSet();

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
        slot.rawSlotIndex = this.c.size();
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
        int l;
        ItemStack itemstack1;

        if (k == 5) {
            int i1 = this.g;

            this.g = c(j);
            if ((i1 != 1 || this.g != 2) && i1 != this.g) {
                this.d();
            } else if (playerinventory.getCarried() == null) {
                this.d();
            } else if (this.g == 0) {
                this.f = b(j);
                if (d(this.f)) {
                    this.g = 1;
                    this.h.clear();
                } else {
                    this.d();
                }
            } else if (this.g == 1) {
                Slot slot = (Slot) this.c.get(i);

                if (slot != null && a(slot, playerinventory.getCarried(), true) && slot.isAllowed(playerinventory.getCarried()) && playerinventory.getCarried().count > this.h.size() && this.b(slot)) {
                    this.h.add(slot);
                }
            } else if (this.g == 2) {
                if (!this.h.isEmpty()) {
                    itemstack1 = playerinventory.getCarried().cloneItemStack();
                    l = playerinventory.getCarried().count;
                    Iterator iterator = this.h.iterator();

                    Map<Integer, ItemStack> draggedSlots = new HashMap<Integer, ItemStack>(); // CraftBukkit - Store slots from drag in map (raw slot id -> new stack)
                    while (iterator.hasNext()) {
                        Slot slot1 = (Slot) iterator.next();

                        if (slot1 != null && a(slot1, playerinventory.getCarried(), true) && slot1.isAllowed(playerinventory.getCarried()) && playerinventory.getCarried().count >= this.h.size() && this.b(slot1)) {
                            ItemStack itemstack2 = itemstack1.cloneItemStack();
                            int j1 = slot1.e() ? slot1.getItem().count : 0;

                            a(this.h, this.f, itemstack2, j1);
                            if (itemstack2.count > itemstack2.getMaxStackSize()) {
                                itemstack2.count = itemstack2.getMaxStackSize();
                            }

                            if (itemstack2.count > slot1.a()) {
                                itemstack2.count = slot1.a();
                            }

                            l -= itemstack2.count - j1;
                            draggedSlots.put(slot1.rawSlotIndex, itemstack2); // CraftBukkit - Put in map instead of setting
                        }
                    }

                    // CraftBukkit start - InventoryDragEvent
                    InventoryView view = getBukkitView();
                    org.bukkit.inventory.ItemStack newcursor = CraftItemStack.asCraftMirror(itemstack1);
                    newcursor.setAmount(l);
                    Map<Integer, org.bukkit.inventory.ItemStack> eventmap = new HashMap<Integer, org.bukkit.inventory.ItemStack>();
                    for (Map.Entry<Integer, ItemStack> ditem : draggedSlots.entrySet()) {
                        eventmap.put(ditem.getKey(), CraftItemStack.asBukkitCopy(ditem.getValue()));
                    }

                    // It's essential that we set the cursor to the new value here to prevent item duplication if a plugin closes the inventory.
                    ItemStack oldCursor = playerinventory.getCarried();
                    playerinventory.setCarried(CraftItemStack.asNMSCopy(newcursor));

                    InventoryDragEvent event = new InventoryDragEvent(view, (newcursor.getType() != org.bukkit.Material.AIR ? newcursor : null), CraftItemStack.asBukkitCopy(oldCursor), this.f == 1, eventmap); // Should be dragButton
                    entityhuman.world.getServer().getPluginManager().callEvent(event);

                    // Whether or not a change was made to the inventory that requires an update.
                    boolean needsUpdate = event.getResult() != Result.DEFAULT;

                    if (event.getResult() != Result.DENY) {
                        for (Map.Entry<Integer, ItemStack> dslot : draggedSlots.entrySet()) {
                            view.setItem(dslot.getKey(), CraftItemStack.asBukkitCopy(dslot.getValue()));
                        }
                        // The only time the carried item will be set to null is if the inventory is closed by the server.
                        // If the inventory is closed by the server, then the cursor items are dropped.  This is why we change the cursor early.
                        if (playerinventory.getCarried() != null) {
                            playerinventory.setCarried(CraftItemStack.asNMSCopy(event.getCursor()));
                            needsUpdate = true;

                        }
                    } else {
                        playerinventory.setCarried(oldCursor);
                    }

                    if (needsUpdate && entityhuman instanceof EntityPlayer) {
                        ((EntityPlayer) entityhuman).updateInventory(this);
                    }
                    // CraftBukkit end
                }

                this.d();
            } else {
                this.d();
            }
        } else if (this.g != 0) {
            this.d();
        } else {
            Slot slot2;
            int k1;
            ItemStack itemstack3;

            if ((k == 0 || k == 1) && (j == 0 || j == 1)) {
                if (i == -999) {
                    if (playerinventory.getCarried() != null && i == -999) {
                        if (j == 0) {
                            entityhuman.drop(playerinventory.getCarried(), true);
                            playerinventory.setCarried((ItemStack) null);
                        }

                        if (j == 1) {
                            // CraftBukkit start - Store a reference
                            ItemStack itemstack4 = playerinventory.getCarried();
                            if (itemstack4.count > 0) {
                                entityhuman.drop(itemstack4.a(1), true);
                            }

                            if (itemstack4.count == 0) {
                                // CraftBukkit end
                                playerinventory.setCarried((ItemStack) null);
                            }
                        }
                    }
                } else if (k == 1) {
                    if (i < 0) {
                        return null;
                    }

                    slot2 = (Slot) this.c.get(i);
                    if (slot2 != null && slot2.a(entityhuman)) {
                        itemstack1 = this.b(entityhuman, i);
                        if (itemstack1 != null) {
                            Item item = itemstack1.getItem();

                            itemstack = itemstack1.cloneItemStack();
                            if (slot2.getItem() != null && slot2.getItem().getItem() == item) {
                                this.a(i, j, true, entityhuman);
                            }
                        }
                    }
                } else {
                    if (i < 0) {
                        return null;
                    }

                    slot2 = (Slot) this.c.get(i);
                    if (slot2 != null) {
                        itemstack1 = slot2.getItem();
                        ItemStack itemstack4 = playerinventory.getCarried();

                        if (itemstack1 != null) {
                            itemstack = itemstack1.cloneItemStack();
                        }

                        if (itemstack1 == null) {
                            if (itemstack4 != null && slot2.isAllowed(itemstack4)) {
                                k1 = j == 0 ? itemstack4.count : 1;
                                if (k1 > slot2.a()) {
                                    k1 = slot2.a();
                                }

                                if (itemstack4.count >= k1) {
                                    slot2.set(itemstack4.a(k1));
                                }

                                if (itemstack4.count == 0) {
                                    playerinventory.setCarried((ItemStack) null);
                                }
                            }
                        } else if (slot2.a(entityhuman)) {
                            if (itemstack4 == null) {
                                k1 = j == 0 ? itemstack1.count : (itemstack1.count + 1) / 2;
                                itemstack3 = slot2.a(k1);
                                playerinventory.setCarried(itemstack3);
                                if (itemstack1.count == 0) {
                                    slot2.set((ItemStack) null);
                                }

                                slot2.a(entityhuman, playerinventory.getCarried());
                            } else if (slot2.isAllowed(itemstack4)) {
                                if (itemstack1.getItem() == itemstack4.getItem() && itemstack1.getData() == itemstack4.getData() && ItemStack.equals(itemstack1, itemstack4)) {
                                    k1 = j == 0 ? itemstack4.count : 1;
                                    if (k1 > slot2.a() - itemstack1.count) {
                                        k1 = slot2.a() - itemstack1.count;
                                    }

                                    if (k1 > itemstack4.getMaxStackSize() - itemstack1.count) {
                                        k1 = itemstack4.getMaxStackSize() - itemstack1.count;
                                    }

                                    itemstack4.a(k1);
                                    if (itemstack4.count == 0) {
                                        playerinventory.setCarried((ItemStack) null);
                                    }

                                    itemstack1.count += k1;
                                } else if (itemstack4.count <= slot2.a()) {
                                    slot2.set(itemstack4);
                                    playerinventory.setCarried(itemstack1);
                                }
                            } else if (itemstack1.getItem() == itemstack4.getItem() && itemstack4.getMaxStackSize() > 1 && (!itemstack1.usesData() || itemstack1.getData() == itemstack4.getData()) && ItemStack.equals(itemstack1, itemstack4)) {
                                k1 = itemstack1.count;
                                if (k1 > 0 && k1 + itemstack4.count <= itemstack4.getMaxStackSize()) {
                                    itemstack4.count += k1;
                                    itemstack1 = slot2.a(k1);
                                    if (itemstack1.count == 0) {
                                        slot2.set((ItemStack) null);
                                    }

                                    slot2.a(entityhuman, playerinventory.getCarried());
                                }
                            }
                        }

                        slot2.f();
                    }
                }
            } else if (k == 2 && j >= 0 && j < 9) {
                slot2 = (Slot) this.c.get(i);
                if (slot2.a(entityhuman)) {
                    itemstack1 = playerinventory.getItem(j);
                    boolean flag = itemstack1 == null || slot2.inventory == playerinventory && slot2.isAllowed(itemstack1);

                    k1 = -1;
                    if (!flag) {
                        k1 = playerinventory.j();
                        flag |= k1 > -1;
                    }

                    if (slot2.e() && flag) {
                        itemstack3 = slot2.getItem();
                        playerinventory.setItem(j, itemstack3.cloneItemStack());
                        if ((slot2.inventory != playerinventory || !slot2.isAllowed(itemstack1)) && itemstack1 != null) {
                            if (k1 > -1) {
                                playerinventory.pickup(itemstack1);
                                slot2.a(itemstack3.count);
                                slot2.set((ItemStack) null);
                                slot2.a(entityhuman, itemstack3);
                            }
                        } else {
                            slot2.a(itemstack3.count);
                            slot2.set(itemstack1);
                            slot2.a(entityhuman, itemstack3);
                        }
                    } else if (!slot2.e() && itemstack1 != null && slot2.isAllowed(itemstack1)) {
                        playerinventory.setItem(j, (ItemStack) null);
                        slot2.set(itemstack1);
                    }
                }
            } else if (k == 3 && entityhuman.abilities.canInstantlyBuild && playerinventory.getCarried() == null && i >= 0) {
                slot2 = (Slot) this.c.get(i);
                if (slot2 != null && slot2.e()) {
                    itemstack1 = slot2.getItem().cloneItemStack();
                    itemstack1.count = itemstack1.getMaxStackSize();
                    playerinventory.setCarried(itemstack1);
                }
            } else if (k == 4 && playerinventory.getCarried() == null && i >= 0) {
                slot2 = (Slot) this.c.get(i);
                if (slot2 != null && slot2.e() && slot2.a(entityhuman)) {
                    itemstack1 = slot2.a(j == 0 ? 1 : slot2.getItem().count);
                    slot2.a(entityhuman, itemstack1);
                    entityhuman.drop(itemstack1, true);
                }
            } else if (k == 6 && i >= 0) {
                slot2 = (Slot) this.c.get(i);
                itemstack1 = playerinventory.getCarried();
                if (itemstack1 != null && (slot2 == null || !slot2.e() || !slot2.a(entityhuman))) {
                    l = j == 0 ? 0 : this.c.size() - 1;
                    k1 = j == 0 ? 1 : -1;

                    for (int l1 = 0; l1 < 2; ++l1) {
                        for (int i2 = l; i2 >= 0 && i2 < this.c.size() && itemstack1.count < itemstack1.getMaxStackSize(); i2 += k1) {
                            Slot slot3 = (Slot) this.c.get(i2);

                            if (slot3.e() && a(slot3, itemstack1, true) && slot3.a(entityhuman) && this.a(itemstack1, slot3) && (l1 != 0 || slot3.getItem().count != slot3.getItem().getMaxStackSize())) {
                                int j2 = Math.min(itemstack1.getMaxStackSize() - itemstack1.count, slot3.getItem().count);
                                ItemStack itemstack5 = slot3.a(j2);

                                itemstack1.count += j2;
                                if (itemstack5.count <= 0) {
                                    slot3.set((ItemStack) null);
                                }

                                slot3.a(entityhuman, itemstack5);
                            }
                        }
                    }
                }

                this.b();
            }
        }

        return itemstack;
    }

    public boolean a(ItemStack itemstack, Slot slot) {
        return true;
    }

    protected void a(int i, int j, boolean flag, EntityHuman entityhuman) {
        this.clickItem(i, j, 1, entityhuman);
    }

    public void b(EntityHuman entityhuman) {
        PlayerInventory playerinventory = entityhuman.inventory;

        if (playerinventory.getCarried() != null) {
            entityhuman.drop(playerinventory.getCarried(), false);
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
        return !this.i.contains(entityhuman);
    }

    public void a(EntityHuman entityhuman, boolean flag) {
        if (flag) {
            this.i.remove(entityhuman);
        } else {
            this.i.add(entityhuman);
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
                if (itemstack1 != null && itemstack1.getItem() == itemstack.getItem() && (!itemstack.usesData() || itemstack.getData() == itemstack1.getData()) && ItemStack.equals(itemstack, itemstack1)) {
                    int l = itemstack1.count + itemstack.count;

                    if (l <= itemstack.getMaxStackSize()) {
                        itemstack.count = 0;
                        itemstack1.count = l;
                        slot.f();
                        flag1 = true;
                    } else if (itemstack1.count < itemstack.getMaxStackSize()) {
                        itemstack.count -= itemstack.getMaxStackSize() - itemstack1.count;
                        itemstack1.count = itemstack.getMaxStackSize();
                        slot.f();
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
                    slot.f();
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

    public static int b(int i) {
        return i >> 2 & 3;
    }

    public static int c(int i) {
        return i & 3;
    }

    public static boolean d(int i) {
        return i == 0 || i == 1;
    }

    protected void d() {
        this.g = 0;
        this.h.clear();
    }

    public static boolean a(Slot slot, ItemStack itemstack, boolean flag) {
        boolean flag1 = slot == null || !slot.e();

        if (slot != null && slot.e() && itemstack != null && itemstack.doMaterialsMatch(slot.getItem()) && ItemStack.equals(slot.getItem(), itemstack)) {
            int i = flag ? 0 : itemstack.count;

            flag1 |= slot.getItem().count + i <= itemstack.getMaxStackSize();
        }

        return flag1;
    }

    public static void a(Set set, int i, ItemStack itemstack, int j) {
        switch (i) {
        case 0:
            itemstack.count = MathHelper.d((float) itemstack.count / (float) set.size());
            break;

        case 1:
            itemstack.count = 1;
        }

        itemstack.count += j;
    }

    public boolean b(Slot slot) {
        return true;
    }

    public static int b(IInventory iinventory) {
        if (iinventory == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;

            for (int j = 0; j < iinventory.getSize(); ++j) {
                ItemStack itemstack = iinventory.getItem(j);

                if (itemstack != null) {
                    f += (float) itemstack.count / (float) Math.min(iinventory.getMaxStackSize(), itemstack.getMaxStackSize());
                    ++i;
                }
            }

            f /= (float) iinventory.getSize();
            return MathHelper.d(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }
}
