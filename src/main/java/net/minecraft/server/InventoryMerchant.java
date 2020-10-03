package net.minecraft.server;

import java.util.Iterator;
import javax.annotation.Nullable;
// CraftBukkit start
import java.util.List;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftAbstractVillager;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryMerchant implements IInventory {

    private final IMerchant merchant;
    private final NonNullList<ItemStack> itemsInSlots;
    @Nullable
    private MerchantRecipe recipe;
    public int selectedIndex;
    private int e;

    // CraftBukkit start - add fields and methods
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    private int maxStack = MAX_STACK;

    public List<ItemStack> getContents() {
        return this.itemsInSlots;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
        merchant.setTradingPlayer((EntityHuman) null); // SPIGOT-4860
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }

    public void setMaxStackSize(int i) {
        maxStack = i;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return (merchant instanceof EntityVillagerAbstract) ? (CraftAbstractVillager) ((EntityVillagerAbstract) this.merchant).getBukkitEntity() : null;
    }

    @Override
    public Location getLocation() {
        return (merchant instanceof EntityVillager) ? ((EntityVillager) this.merchant).getBukkitEntity().getLocation() : null;
    }
    // CraftBukkit end

    public InventoryMerchant(IMerchant imerchant) {
        this.itemsInSlots = NonNullList.a(3, ItemStack.b);
        this.merchant = imerchant;
    }

    @Override
    public int getSize() {
        return this.itemsInSlots.size();
    }

    @Override
    public boolean isEmpty() {
        Iterator iterator = this.itemsInSlots.iterator();

        ItemStack itemstack;

        do {
            if (!iterator.hasNext()) {
                return true;
            }

            itemstack = (ItemStack) iterator.next();
        } while (itemstack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getItem(int i) {
        return (ItemStack) this.itemsInSlots.get(i);
    }

    @Override
    public ItemStack splitStack(int i, int j) {
        ItemStack itemstack = (ItemStack) this.itemsInSlots.get(i);

        if (i == 2 && !itemstack.isEmpty()) {
            return ContainerUtil.a(this.itemsInSlots, i, itemstack.getCount());
        } else {
            ItemStack itemstack1 = ContainerUtil.a(this.itemsInSlots, i, j);

            if (!itemstack1.isEmpty() && this.d(i)) {
                this.f();
            }

            return itemstack1;
        }
    }

    private boolean d(int i) {
        return i == 0 || i == 1;
    }

    @Override
    public ItemStack splitWithoutUpdate(int i) {
        return ContainerUtil.a(this.itemsInSlots, i);
    }

    @Override
    public void setItem(int i, ItemStack itemstack) {
        this.itemsInSlots.set(i, itemstack);
        if (!itemstack.isEmpty() && itemstack.getCount() > this.getMaxStackSize()) {
            itemstack.setCount(this.getMaxStackSize());
        }

        if (this.d(i)) {
            this.f();
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return this.merchant.getTrader() == entityhuman;
    }

    @Override
    public void update() {
        this.f();
    }

    public void f() {
        this.recipe = null;
        ItemStack itemstack;
        ItemStack itemstack1;

        if (((ItemStack) this.itemsInSlots.get(0)).isEmpty()) {
            itemstack = (ItemStack) this.itemsInSlots.get(1);
            itemstack1 = ItemStack.b;
        } else {
            itemstack = (ItemStack) this.itemsInSlots.get(0);
            itemstack1 = (ItemStack) this.itemsInSlots.get(1);
        }

        if (itemstack.isEmpty()) {
            this.setItem(2, ItemStack.b);
            this.e = 0;
        } else {
            MerchantRecipeList merchantrecipelist = this.merchant.getOffers();

            if (!merchantrecipelist.isEmpty()) {
                MerchantRecipe merchantrecipe = merchantrecipelist.a(itemstack, itemstack1, this.selectedIndex);

                if (merchantrecipe == null || merchantrecipe.isFullyUsed()) {
                    this.recipe = merchantrecipe;
                    merchantrecipe = merchantrecipelist.a(itemstack1, itemstack, this.selectedIndex);
                }

                if (merchantrecipe != null && !merchantrecipe.isFullyUsed()) {
                    this.recipe = merchantrecipe;
                    this.setItem(2, merchantrecipe.f());
                    this.e = merchantrecipe.getXp();
                } else {
                    this.setItem(2, ItemStack.b);
                    this.e = 0;
                }
            }

            this.merchant.k(this.getItem(2));
        }
    }

    @Nullable
    public MerchantRecipe getRecipe() {
        return this.recipe;
    }

    public void c(int i) {
        this.selectedIndex = i;
        this.f();
    }

    @Override
    public void clear() {
        this.itemsInSlots.clear();
    }
}
