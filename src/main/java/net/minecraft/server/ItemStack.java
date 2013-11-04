package net.minecraft.server;

import java.text.DecimalFormat;
import java.util.Random;

import net.minecraft.util.com.google.common.collect.HashMultimap;
import net.minecraft.util.com.google.common.collect.Multimap;

public final class ItemStack {

    public static final DecimalFormat a = new DecimalFormat("#.###");
    public int count;
    public int c;
    private Item item;
    public NBTTagCompound tag;
    private int damage;
    private EntityItemFrame g;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(Item.getItemOf(block), i, j);
    }

    public ItemStack(Item item) {
        this(item, 1);
    }

    public ItemStack(Item item, int i) {
        this(item, i, 0);
    }

    public ItemStack(Item item, int i, int j) {
        this.item = item;
        this.count = i;
        // CraftBukkit start - Pass to setData to do filtering
        this.setData(j);
        //this.damage = j;
        //if (this.damage < 0) {
        //    this.damage = 0;
        //}
        // CraftBukkit end
    }

    public static ItemStack createStack(NBTTagCompound nbttagcompound) {
        ItemStack itemstack = new ItemStack();

        itemstack.c(nbttagcompound);
        return itemstack.getItem() != null ? itemstack : null;
    }

    private ItemStack() {}

    public ItemStack a(int i) {
        ItemStack itemstack = new ItemStack(this.item, i, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        this.count -= i;
        return itemstack;
    }

    public Item getItem() {
        return this.item;
    }

    public boolean placeItem(EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        boolean flag = this.getItem().interactWith(this, entityhuman, world, i, j, k, l, f, f1, f2);

        if (flag) {
            entityhuman.a(StatisticList.E[Item.b(this.item)], 1);
        }

        return flag;
    }

    public float a(Block block) {
        return this.getItem().getDestroySpeed(this, block);
    }

    public ItemStack a(World world, EntityHuman entityhuman) {
        return this.getItem().a(this, world, entityhuman);
    }

    public ItemStack b(World world, EntityHuman entityhuman) {
        return this.getItem().b(this, world, entityhuman);
    }

    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("id", (short) Item.b(this.item));
        nbttagcompound.setByte("Count", (byte) this.count);
        nbttagcompound.setShort("Damage", (short) this.damage);
        if (this.tag != null) {
            nbttagcompound.set("tag", this.tag.clone()); // CraftBukkit - make defensive copy, data is going to another thread
        }

        return nbttagcompound;
    }

    public void c(NBTTagCompound nbttagcompound) {
        this.item = Item.d(nbttagcompound.getShort("id"));
        this.count = nbttagcompound.getByte("Count");
        this.damage = nbttagcompound.getShort("Damage");
        if (this.damage < 0) {
            this.damage = 0;
        }

        if (nbttagcompound.hasKeyOfType("tag", 10)) {
            // CraftBukkit - make defensive copy as this data may be coming from the save thread
            this.tag = (NBTTagCompound) nbttagcompound.getCompound("tag").clone();
        }
    }

    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.g() || !this.i());
    }

    public boolean g() {
        return this.item.getMaxDurability() <= 0 ? false : !this.hasTag() || !this.getTag().getBoolean("Unbreakable");
    }

    public boolean usesData() {
        return this.item.n();
    }

    public boolean i() {
        return this.g() && this.damage > 0;
    }

    public int j() {
        return this.damage;
    }

    public int getData() {
        return this.damage;
    }

    public void setData(int i) {
        // CraftBukkit start - Filter out data for items that shouldn't have it
        // The crafting system uses this value for a special purpose so we have to allow it
        if (i == 32767) {
            this.damage = i;
            return;
        }

        if (!(this.usesData() || this.getItem().usesDurability())) { // || this.id > 255)) {
            i = 0;
        }

        // Filter wool to avoid confusing the client
        if (this.getItem() == Item.getItemOf(Blocks.WOOL)) {
            i = Math.min(15, i);
        }
        // CraftBukkit end

        this.damage = i;
        if (this.damage < -1) { // CraftBukkit - don't filter -1, we use it
            this.damage = 0;
        }
    }

    public int l() {
        return this.item.getMaxDurability();
    }

    public boolean isDamaged(int i, Random random) {
        if (!this.g()) {
            return false;
        } else {
            if (i > 0) {
                int j = EnchantmentManager.getEnchantmentLevel(Enchantment.DURABILITY.id, this);
                int k = 0;

                for (int l = 0; j > 0 && l < i; ++l) {
                    if (EnchantmentDurability.a(this, j, random)) {
                        ++k;
                    }
                }

                i -= k;
                if (i <= 0) {
                    return false;
                }
            }

            this.damage += i;
            return this.damage > this.l();
        }
    }

    public void damage(int i, EntityLiving entityliving) {
        if (!(entityliving instanceof EntityHuman) || !((EntityHuman) entityliving).abilities.canInstantlyBuild) {
            if (this.g()) {
                if (this.isDamaged(i, entityliving.aI())) {
                    entityliving.a(this);
                    --this.count;
                    if (entityliving instanceof EntityHuman) {
                        EntityHuman entityhuman = (EntityHuman) entityliving;

                        entityhuman.a(StatisticList.F[Item.b(this.item)], 1);
                        if (this.count == 0 && this.getItem() instanceof ItemBow) {
                            entityhuman.bE();
                        }
                    }

                    if (this.count < 0) {
                        this.count = 0;
                    }

                    // CraftBukkit start - Check for item breaking
                    if (this.count == 0 && entityliving instanceof EntityHuman) {
                        org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerItemBreakEvent((EntityHuman) entityliving, this);
                    }
                    // CraftBukkit end

                    this.damage = 0;
                }
            }
        }
    }

    public void a(EntityLiving entityliving, EntityHuman entityhuman) {
        boolean flag = this.item.a(this, entityliving, (EntityLiving) entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[Item.b(this.item)], 1);
        }
    }

    public void a(World world, Block block, int i, int j, int k, EntityHuman entityhuman) {
        boolean flag = this.item.a(this, world, block, i, j, k, entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[Item.b(this.item)], 1);
        }
    }

    public boolean b(Block block) {
        return this.item.canDestroySpecialBlock(block);
    }

    public boolean a(EntityHuman entityhuman, EntityLiving entityliving) {
        return this.item.a(this, entityhuman, entityliving);
    }

    public ItemStack cloneItemStack() {
        ItemStack itemstack = new ItemStack(this.item, this.count, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        return itemstack;
    }

    public static boolean equals(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? (itemstack.tag == null && itemstack1.tag != null ? false : itemstack.tag == null || itemstack.tag.equals(itemstack1.tag)) : false);
    }

    public static boolean matches(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack == null && itemstack1 == null ? true : (itemstack != null && itemstack1 != null ? itemstack.d(itemstack1) : false);
    }

    private boolean d(ItemStack itemstack) {
        return this.count != itemstack.count ? false : (this.item != itemstack.item ? false : (this.damage != itemstack.damage ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag))));
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return this.item == itemstack.item && this.damage == itemstack.damage;
    }

    public String a() {
        return this.item.a(this);
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.cloneItemStack();
    }

    public String toString() {
        return this.count + "x" + this.item.getName() + "@" + this.damage;
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.c > 0) {
            --this.c;
        }

        this.item.a(this, world, entity, i, flag);
    }

    public void a(World world, EntityHuman entityhuman, int i) {
        entityhuman.a(StatisticList.D[Item.b(this.item)], i);
        this.item.d(this, world, entityhuman);
    }

    public int n() {
        return this.getItem().d_(this);
    }

    public EnumAnimation o() {
        return this.getItem().d(this);
    }

    public void b(World world, EntityHuman entityhuman, int i) {
        this.getItem().a(this, world, entityhuman, i);
    }

    public boolean hasTag() {
        return this.tag != null;
    }

    public NBTTagCompound getTag() {
        return this.tag;
    }

    public NBTTagList getEnchantments() {
        return this.tag == null ? null : this.tag.getList("ench", 10);
    }

    public void setTag(NBTTagCompound nbttagcompound) {
        this.tag = nbttagcompound;
    }

    public String getName() {
        String s = this.getItem().n(this);

        if (this.tag != null && this.tag.hasKeyOfType("display", 10)) {
            NBTTagCompound nbttagcompound = this.tag.getCompound("display");

            if (nbttagcompound.hasKeyOfType("Name", 8)) {
                s = nbttagcompound.getString("Name");
            }
        }

        return s;
    }

    public ItemStack c(String s) {
        if (this.tag == null) {
            this.tag = new NBTTagCompound();
        }

        if (!this.tag.hasKeyOfType("display", 10)) {
            this.tag.set("display", new NBTTagCompound());
        }

        this.tag.getCompound("display").setString("Name", s);
        return this;
    }

    public void t() {
        if (this.tag != null) {
            if (this.tag.hasKeyOfType("display", 10)) {
                NBTTagCompound nbttagcompound = this.tag.getCompound("display");

                nbttagcompound.remove("Name");
                if (nbttagcompound.isEmpty()) {
                    this.tag.remove("display");
                    if (this.tag.isEmpty()) {
                        this.setTag((NBTTagCompound) null);
                    }
                }
            }
        }
    }

    public boolean hasName() {
        return this.tag == null ? false : (!this.tag.hasKeyOfType("display", 10) ? false : this.tag.getCompound("display").hasKeyOfType("Name", 8));
    }

    public EnumItemRarity w() {
        return this.getItem().f(this);
    }

    public boolean x() {
        return !this.getItem().e_(this) ? false : !this.hasEnchantments();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        if (!this.tag.hasKeyOfType("ench", 9)) {
            this.tag.set("ench", new NBTTagList());
        }

        NBTTagList nbttaglist = this.tag.getList("ench", 10);
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setShort("id", (short) enchantment.id);
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.add(nbttagcompound);
    }

    public boolean hasEnchantments() {
        return this.tag != null && this.tag.hasKeyOfType("ench", 9);
    }

    public void a(String s, NBTBase nbtbase) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        this.tag.set(s, nbtbase);
    }

    public boolean z() {
        return this.getItem().v();
    }

    public boolean A() {
        return this.g != null;
    }

    public void a(EntityItemFrame entityitemframe) {
        this.g = entityitemframe;
    }

    public EntityItemFrame B() {
        return this.g;
    }

    public int getRepairCost() {
        return this.hasTag() && this.tag.hasKeyOfType("RepairCost", 3) ? this.tag.getInt("RepairCost") : 0;
    }

    public void setRepairCost(int i) {
        if (!this.hasTag()) {
            this.tag = new NBTTagCompound();
        }

        this.tag.setInt("RepairCost", i);
    }

    public Multimap D() {
        Object object;

        if (this.hasTag() && this.tag.hasKeyOfType("AttributeModifiers", 9)) {
            object = HashMultimap.create();
            NBTTagList nbttaglist = this.tag.getList("AttributeModifiers", 10);

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.get(i);
                AttributeModifier attributemodifier = GenericAttributes.a(nbttagcompound);

                if (attributemodifier.a().getLeastSignificantBits() != 0L && attributemodifier.a().getMostSignificantBits() != 0L) {
                    ((Multimap) object).put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        } else {
            object = this.getItem().k();
        }

        return (Multimap) object;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public IChatBaseComponent E() {
        IChatBaseComponent ichatbasecomponent = (new ChatComponentText("[")).a(this.getName()).a("]");

        if (this.item != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            this.save(nbttagcompound);
            ichatbasecomponent.b().a(new ChatHoverable(EnumHoverAction.SHOW_ITEM, new ChatComponentText(nbttagcompound.toString())));
            ichatbasecomponent.b().setColor(this.w().e);
        }

        return ichatbasecomponent;
    }
}
