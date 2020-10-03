package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftMerchantRecipe; // CraftBukkit

public class MerchantRecipe {

    public ItemStack buyingItem1;
    public ItemStack buyingItem2;
    public final ItemStack sellingItem;
    public int uses;
    public int maxUses;
    public boolean rewardExp;
    private int specialPrice;
    private int demand;
    public float priceMultiplier;
    public int xp;
    // CraftBukkit start
    private CraftMerchantRecipe bukkitHandle;

    public CraftMerchantRecipe asBukkit() {
        return (bukkitHandle == null) ? bukkitHandle = new CraftMerchantRecipe(this) : bukkitHandle;
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int uses, int maxUses, int experience, float priceMultiplier, CraftMerchantRecipe bukkit) {
        this(itemstack, itemstack1, itemstack2, uses, maxUses, experience, priceMultiplier);
        this.bukkitHandle = bukkit;
    }
    // CraftBukkit end

    public MerchantRecipe(NBTTagCompound nbttagcompound) {
        this.rewardExp = true;
        this.xp = 1;
        this.buyingItem1 = ItemStack.a(nbttagcompound.getCompound("buy"));
        this.buyingItem2 = ItemStack.a(nbttagcompound.getCompound("buyB"));
        this.sellingItem = ItemStack.a(nbttagcompound.getCompound("sell"));
        this.uses = nbttagcompound.getInt("uses");
        if (nbttagcompound.hasKeyOfType("maxUses", 99)) {
            this.maxUses = nbttagcompound.getInt("maxUses");
        } else {
            this.maxUses = 4;
        }

        if (nbttagcompound.hasKeyOfType("rewardExp", 1)) {
            this.rewardExp = nbttagcompound.getBoolean("rewardExp");
        }

        if (nbttagcompound.hasKeyOfType("xp", 3)) {
            this.xp = nbttagcompound.getInt("xp");
        }

        if (nbttagcompound.hasKeyOfType("priceMultiplier", 5)) {
            this.priceMultiplier = nbttagcompound.getFloat("priceMultiplier");
        }

        this.specialPrice = nbttagcompound.getInt("specialPrice");
        this.demand = nbttagcompound.getInt("demand");
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, int i, int j, float f) {
        this(itemstack, ItemStack.b, itemstack1, i, j, f);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, float f) {
        this(itemstack, itemstack1, itemstack2, 0, i, j, f);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, int k, float f) {
        this(itemstack, itemstack1, itemstack2, i, j, k, f, 0);
    }

    public MerchantRecipe(ItemStack itemstack, ItemStack itemstack1, ItemStack itemstack2, int i, int j, int k, float f, int l) {
        this.rewardExp = true;
        this.xp = 1;
        this.buyingItem1 = itemstack;
        this.buyingItem2 = itemstack1;
        this.sellingItem = itemstack2;
        this.uses = i;
        this.maxUses = j;
        this.xp = k;
        this.priceMultiplier = f;
        this.demand = l;
    }

    public ItemStack a() {
        return this.buyingItem1;
    }

    public ItemStack getBuyItem1() {
        int i = this.buyingItem1.getCount();
        if (i <= 0) return ItemStack.b; // CraftBukkit - SPIGOT-5476
        ItemStack itemstack = this.buyingItem1.cloneItemStack();
        int j = Math.max(0, MathHelper.d((float) (i * this.demand) * this.priceMultiplier));

        itemstack.setCount(MathHelper.clamp(i + j + this.specialPrice, 1, this.buyingItem1.getItem().getMaxStackSize()));
        return itemstack;
    }

    public ItemStack getBuyItem2() {
        return this.buyingItem2;
    }

    public ItemStack getSellingItem() {
        return this.sellingItem;
    }

    public void e() {
        this.demand = this.demand + this.uses - (this.maxUses - this.uses);
    }

    public ItemStack f() {
        return this.sellingItem.cloneItemStack();
    }

    public int getUses() {
        return this.uses;
    }

    public void resetUses() {
        this.uses = 0;
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public void increaseUses() {
        ++this.uses;
    }

    public int getDemand() {
        return this.demand;
    }

    public void increaseSpecialPrice(int i) {
        this.specialPrice += i;
    }

    public void setSpecialPrice() {
        this.specialPrice = 0;
    }

    public int getSpecialPrice() {
        return this.specialPrice;
    }

    public void setSpecialPrice(int i) {
        this.specialPrice = i;
    }

    public float getPriceMultiplier() {
        return this.priceMultiplier;
    }

    public int getXp() {
        return this.xp;
    }

    public boolean isFullyUsed() {
        return this.uses >= this.maxUses;
    }

    public void q() {
        this.uses = this.maxUses;
    }

    public boolean r() {
        return this.uses > 0;
    }

    public boolean isRewardExp() {
        return this.rewardExp;
    }

    public NBTTagCompound t() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.set("buy", this.buyingItem1.save(new NBTTagCompound()));
        nbttagcompound.set("sell", this.sellingItem.save(new NBTTagCompound()));
        nbttagcompound.set("buyB", this.buyingItem2.save(new NBTTagCompound()));
        nbttagcompound.setInt("uses", this.uses);
        nbttagcompound.setInt("maxUses", this.maxUses);
        nbttagcompound.setBoolean("rewardExp", this.rewardExp);
        nbttagcompound.setInt("xp", this.xp);
        nbttagcompound.setFloat("priceMultiplier", this.priceMultiplier);
        nbttagcompound.setInt("specialPrice", this.specialPrice);
        nbttagcompound.setInt("demand", this.demand);
        return nbttagcompound;
    }

    public boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return this.c(itemstack, this.getBuyItem1()) && itemstack.getCount() >= this.getBuyItem1().getCount() && this.c(itemstack1, this.buyingItem2) && itemstack1.getCount() >= this.buyingItem2.getCount();
    }

    private boolean c(ItemStack itemstack, ItemStack itemstack1) {
        if (itemstack1.isEmpty() && itemstack.isEmpty()) {
            return true;
        } else {
            ItemStack itemstack2 = itemstack.cloneItemStack();

            if (itemstack2.getItem().usesDurability()) {
                itemstack2.setDamage(itemstack2.getDamage());
            }

            return ItemStack.c(itemstack2, itemstack1) && (!itemstack1.hasTag() || itemstack2.hasTag() && GameProfileSerializer.a(itemstack1.getTag(), itemstack2.getTag(), false));
        }
    }

    public boolean b(ItemStack itemstack, ItemStack itemstack1) {
        if (!this.a(itemstack, itemstack1)) {
            return false;
        } else {
            itemstack.subtract(this.getBuyItem1().getCount());
            if (!this.getBuyItem2().isEmpty()) {
                itemstack1.subtract(this.getBuyItem2().getCount());
            }

            return true;
        }
    }
}
