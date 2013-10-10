package net.minecraft.server;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.text.DecimalFormat;
import java.util.Random;

public final class ItemStack {

    public static final DecimalFormat a = new DecimalFormat("#.###");
    public int count;
    public int c;
    public int id;
    public NBTTagCompound tag;
    private int damage;
    private EntityItemFrame g;

    public ItemStack(Block block) {
        this(block, 1);
    }

    public ItemStack(Block block, int i) {
        this(block.id, i, 0);
    }

    public ItemStack(Block block, int i, int j) {
        this(block.id, i, j);
    }

    public ItemStack(Item item) {
        this(item.id, 1, 0);
    }

    public ItemStack(Item item, int i) {
        this(item.id, i, 0);
    }

    public ItemStack(Item item, int i, int j) {
        this(item.id, i, j);
    }

    public ItemStack(int i, int j, int k) {
        this.id = i;
        this.count = j;
        // CraftBukkit start - Pass to setData to do filtering
        this.setData(k);
        //this.damage = k;
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
        ItemStack itemstack = new ItemStack(this.id, i, this.damage);

        if (this.tag != null) {
            itemstack.tag = (NBTTagCompound) this.tag.clone();
        }

        this.count -= i;
        return itemstack;
    }

    public Item getItem() {
        return Item.byId[this.id];
    }

    public boolean placeItem(EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        boolean flag = this.getItem().interactWith(this, entityhuman, world, i, j, k, l, f, f1, f2);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
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
        nbttagcompound.setShort("id", (short) this.id);
        nbttagcompound.setByte("Count", (byte) this.count);
        nbttagcompound.setShort("Damage", (short) this.damage);
        if (this.tag != null) {
            nbttagcompound.set("tag", this.tag.clone()); // CraftBukkit - make defensive copy, data is going to another thread
        }

        return nbttagcompound;
    }

    public void c(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.getShort("id");
        this.count = nbttagcompound.getByte("Count");
        this.damage = nbttagcompound.getShort("Damage");
        if (this.damage < 0) {
            this.damage = 0;
        }

        if (nbttagcompound.hasKey("tag")) {
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
        return Item.byId[this.id].getMaxDurability() > 0;
    }

    public boolean usesData() {
        return Item.byId[this.id].n();
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

        if (!(this.usesData() || Item.byId[this.id].usesDurability() || this.id > 255)) {
            i = 0;
        }

        // Filter wool to avoid confusing the client
        if (this.id == Block.WOOL.id) {
            i = Math.min(15, i);
        }
        // CraftBukkit end

        this.damage = i;
        if (this.damage < -1) { // CraftBukkit - don't filter -1, we use it
            this.damage = 0;
        }
    }

    public int l() {
        return Item.byId[this.id].getMaxDurability();
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
                if (this.isDamaged(i, entityliving.aD())) {
                    entityliving.a(this);
                    --this.count;
                    if (entityliving instanceof EntityHuman) {
                        EntityHuman entityhuman = (EntityHuman) entityliving;

                        entityhuman.a(StatisticList.F[this.id], 1);
                        if (this.count == 0 && this.getItem() instanceof ItemBow) {
                            entityhuman.bz();
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
        boolean flag = Item.byId[this.id].a(this, entityliving, (EntityLiving) entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public void a(World world, int i, int j, int k, int l, EntityHuman entityhuman) {
        boolean flag = Item.byId[this.id].a(this, world, i, j, k, l, entityhuman);

        if (flag) {
            entityhuman.a(StatisticList.E[this.id], 1);
        }
    }

    public boolean b(Block block) {
        return Item.byId[this.id].canDestroySpecialBlock(block);
    }

    public boolean a(EntityHuman entityhuman, EntityLiving entityliving) {
        return Item.byId[this.id].a(this, entityhuman, entityliving);
    }

    public ItemStack cloneItemStack() {
        ItemStack itemstack = new ItemStack(this.id, this.count, this.damage);

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
        return this.count != itemstack.count ? false : (this.id != itemstack.id ? false : (this.damage != itemstack.damage ? false : (this.tag == null && itemstack.tag != null ? false : this.tag == null || this.tag.equals(itemstack.tag))));
    }

    public boolean doMaterialsMatch(ItemStack itemstack) {
        return this.id == itemstack.id && this.damage == itemstack.damage;
    }

    public String a() {
        return Item.byId[this.id].d(this);
    }

    public static ItemStack b(ItemStack itemstack) {
        return itemstack == null ? null : itemstack.cloneItemStack();
    }

    public String toString() {
        return this.count + "x" + Item.byId[this.id].getName() + "@" + this.damage;
    }

    public void a(World world, Entity entity, int i, boolean flag) {
        if (this.c > 0) {
            --this.c;
        }

        Item.byId[this.id].a(this, world, entity, i, flag);
    }

    public void a(World world, EntityHuman entityhuman, int i) {
        entityhuman.a(StatisticList.D[this.id], i);
        Item.byId[this.id].d(this, world, entityhuman);
    }

    public int n() {
        return this.getItem().d_(this);
    }

    public EnumAnimation o() {
        return this.getItem().c_(this);
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
        return this.tag == null ? null : (NBTTagList) this.tag.get("ench");
    }

    public void setTag(NBTTagCompound nbttagcompound) {
        // CraftBukkit start - Set compound name to "tag," remove discrepancy
        if (nbttagcompound != null) {
            nbttagcompound.setName("tag");
        }
        // CraftBukkit end
        this.tag = nbttagcompound;
    }

    public String getName() {
        String s = this.getItem().l(this);

        if (this.tag != null && this.tag.hasKey("display")) {
            NBTTagCompound nbttagcompound = this.tag.getCompound("display");

            if (nbttagcompound.hasKey("Name")) {
                s = nbttagcompound.getString("Name");
            }
        }

        return s;
    }

    public void c(String s) {
        if (this.tag == null) {
            this.tag = new NBTTagCompound("tag");
        }

        if (!this.tag.hasKey("display")) {
            this.tag.setCompound("display", new NBTTagCompound());
        }

        this.tag.getCompound("display").setString("Name", s);
    }

    public void t() {
        if (this.tag != null) {
            if (this.tag.hasKey("display")) {
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
        return this.tag == null ? false : (!this.tag.hasKey("display") ? false : this.tag.getCompound("display").hasKey("Name"));
    }

    public boolean x() {
        return !this.getItem().e_(this) ? false : !this.hasEnchantments();
    }

    public void addEnchantment(Enchantment enchantment, int i) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        if (!this.tag.hasKey("ench")) {
            this.tag.set("ench", new NBTTagList("ench"));
        }

        NBTTagList nbttaglist = (NBTTagList) this.tag.get("ench");
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        nbttagcompound.setShort("id", (short) enchantment.id);
        nbttagcompound.setShort("lvl", (short) ((byte) i));
        nbttaglist.add(nbttagcompound);
    }

    public boolean hasEnchantments() {
        return this.tag != null && this.tag.hasKey("ench");
    }

    public void a(String s, NBTBase nbtbase) {
        if (this.tag == null) {
            this.setTag(new NBTTagCompound());
        }

        this.tag.set(s, nbtbase);
    }

    public boolean z() {
        return this.getItem().z();
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
        return this.hasTag() && this.tag.hasKey("RepairCost") ? this.tag.getInt("RepairCost") : 0;
    }

    public void setRepairCost(int i) {
        if (!this.hasTag()) {
            this.tag = new NBTTagCompound("tag");
        }

        this.tag.setInt("RepairCost", i);
    }

    public Multimap D() {
        Object object;

        if (this.hasTag() && this.tag.hasKey("AttributeModifiers")) {
            object = HashMultimap.create();
            NBTTagList nbttaglist = this.tag.getList("AttributeModifiers");

            for (int i = 0; i < nbttaglist.size(); ++i) {
                NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.get(i);
                AttributeModifier attributemodifier = GenericAttributes.a(nbttagcompound);

                if (attributemodifier.a().getLeastSignificantBits() != 0L && attributemodifier.a().getMostSignificantBits() != 0L) {
                    ((Multimap) object).put(nbttagcompound.getString("AttributeName"), attributemodifier);
                }
            }
        } else {
            object = this.getItem().h();
        }

        return (Multimap) object;
    }
}
