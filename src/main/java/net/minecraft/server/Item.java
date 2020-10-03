package net.minecraft.server;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

public class Item implements IMaterial {

    public static final Map<Block, Item> e = Maps.newHashMap();
    protected static final UUID f = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    protected static final UUID g = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
    protected static final Random RANDOM = new Random();
    protected final CreativeModeTab i;
    private final EnumItemRarity a;
    private final int maxStackSize;
    private final int durability;
    private final boolean d;
    private final Item craftingResult;
    @Nullable
    private String name;
    @Nullable
    private final FoodInfo foodInfo;

    public static int getId(Item item) {
        return item == null ? 0 : IRegistry.ITEM.a((Object) item);
    }

    public static Item getById(int i) {
        return (Item) IRegistry.ITEM.fromId(i);
    }

    @Deprecated
    public static Item getItemOf(Block block) {
        return (Item) Item.e.getOrDefault(block, Items.AIR);
    }

    public Item(Item.Info item_info) {
        this.i = item_info.d;
        this.a = item_info.e;
        this.craftingResult = item_info.c;
        this.durability = item_info.b;
        this.maxStackSize = item_info.a;
        this.foodInfo = item_info.f;
        this.d = item_info.g;
    }

    public void a(World world, EntityLiving entityliving, ItemStack itemstack, int i) {}

    public boolean b(NBTTagCompound nbttagcompound) {
        return false;
    }

    public boolean a(IBlockData iblockdata, World world, BlockPosition blockposition, EntityHuman entityhuman) {
        return true;
    }

    @Override
    public Item getItem() {
        return this;
    }

    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        return EnumInteractionResult.PASS;
    }

    public float getDestroySpeed(ItemStack itemstack, IBlockData iblockdata) {
        return 1.0F;
    }

    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        if (this.isFood()) {
            ItemStack itemstack = entityhuman.b(enumhand);

            if (entityhuman.q(this.getFoodInfo().d())) {
                entityhuman.c(enumhand);
                return InteractionResultWrapper.consume(itemstack);
            } else {
                return InteractionResultWrapper.fail(itemstack);
            }
        } else {
            return InteractionResultWrapper.pass(entityhuman.b(enumhand));
        }
    }

    public ItemStack a(ItemStack itemstack, World world, EntityLiving entityliving) {
        return this.isFood() ? entityliving.a(world, itemstack) : itemstack;
    }

    public final int getMaxStackSize() {
        return this.maxStackSize;
    }

    public final int getMaxDurability() {
        return this.durability;
    }

    public boolean usesDurability() {
        return this.durability > 0;
    }

    public boolean a(ItemStack itemstack, EntityLiving entityliving, EntityLiving entityliving1) {
        return false;
    }

    public boolean a(ItemStack itemstack, World world, IBlockData iblockdata, BlockPosition blockposition, EntityLiving entityliving) {
        return false;
    }

    public boolean canDestroySpecialBlock(IBlockData iblockdata) {
        return false;
    }

    public EnumInteractionResult a(ItemStack itemstack, EntityHuman entityhuman, EntityLiving entityliving, EnumHand enumhand) {
        return EnumInteractionResult.PASS;
    }

    public String toString() {
        return IRegistry.ITEM.getKey(this).getKey();
    }

    protected String m() {
        if (this.name == null) {
            this.name = SystemUtils.a("item", IRegistry.ITEM.getKey(this));
        }

        return this.name;
    }

    public String getName() {
        return this.m();
    }

    public String f(ItemStack itemstack) {
        return this.getName();
    }

    public boolean n() {
        return true;
    }

    @Nullable
    public final Item getCraftingRemainingItem() {
        return this.craftingResult;
    }

    public boolean p() {
        return this.craftingResult != null;
    }

    public void a(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {}

    public void b(ItemStack itemstack, World world, EntityHuman entityhuman) {}

    public boolean ac_() {
        return false;
    }

    public EnumAnimation d_(ItemStack itemstack) {
        return itemstack.getItem().isFood() ? EnumAnimation.EAT : EnumAnimation.NONE;
    }

    public int e_(ItemStack itemstack) {
        return itemstack.getItem().isFood() ? (this.getFoodInfo().e() ? 16 : 32) : 0;
    }

    public void a(ItemStack itemstack, World world, EntityLiving entityliving, int i) {}

    public IChatBaseComponent h(ItemStack itemstack) {
        return new ChatMessage(this.f(itemstack));
    }

    public boolean e(ItemStack itemstack) {
        return itemstack.hasEnchantments();
    }

    public EnumItemRarity i(ItemStack itemstack) {
        if (!itemstack.hasEnchantments()) {
            return this.a;
        } else {
            switch (this.a) {
                case COMMON:
                case UNCOMMON:
                    return EnumItemRarity.RARE;
                case RARE:
                    return EnumItemRarity.EPIC;
                case EPIC:
                default:
                    return this.a;
            }
        }
    }

    public boolean f_(ItemStack itemstack) {
        return this.getMaxStackSize() == 1 && this.usesDurability();
    }

    protected static MovingObjectPositionBlock a(World world, EntityHuman entityhuman, RayTrace.FluidCollisionOption raytrace_fluidcollisionoption) {
        float f = entityhuman.pitch;
        float f1 = entityhuman.yaw;
        Vec3D vec3d = entityhuman.j(1.0F);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - 3.1415927F);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - 3.1415927F);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d0 = 5.0D;
        Vec3D vec3d1 = vec3d.add((double) f6 * 5.0D, (double) f5 * 5.0D, (double) f7 * 5.0D);

        return world.rayTrace(new RayTrace(vec3d, vec3d1, RayTrace.BlockCollisionOption.OUTLINE, raytrace_fluidcollisionoption, entityhuman));
    }

    public int c() {
        return 0;
    }

    public void a(CreativeModeTab creativemodetab, NonNullList<ItemStack> nonnulllist) {
        if (this.a(creativemodetab)) {
            nonnulllist.add(new ItemStack(this));
        }

    }

    protected boolean a(CreativeModeTab creativemodetab) {
        CreativeModeTab creativemodetab1 = this.q();

        return creativemodetab1 != null && (creativemodetab == CreativeModeTab.g || creativemodetab == creativemodetab1);
    }

    @Nullable
    public final CreativeModeTab q() {
        return this.i;
    }

    public boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return false;
    }

    public Multimap<AttributeBase, AttributeModifier> a(EnumItemSlot enumitemslot) {
        return ImmutableMultimap.of();
    }

    public boolean j(ItemStack itemstack) {
        return itemstack.getItem() == Items.CROSSBOW;
    }

    public ItemStack createItemStack() {
        return new ItemStack(this);
    }

    public boolean a(Tag<Item> tag) {
        return tag.isTagged(this);
    }

    public boolean isFood() {
        return this.foodInfo != null;
    }

    @Nullable
    public FoodInfo getFoodInfo() {
        return this.foodInfo;
    }

    public SoundEffect ae_() {
        return SoundEffects.ENTITY_GENERIC_DRINK;
    }

    public SoundEffect ad_() {
        return SoundEffects.ENTITY_GENERIC_EAT;
    }

    public boolean u() {
        return this.d;
    }

    public boolean a(DamageSource damagesource) {
        return !this.d || !damagesource.isFire();
    }

    public static class Info {

        private int a = 64;
        private int b;
        private Item c;
        private CreativeModeTab d;
        private EnumItemRarity e;
        private FoodInfo f;
        private boolean g;

        public Info() {
            this.e = EnumItemRarity.COMMON;
        }

        public Item.Info a(FoodInfo foodinfo) {
            this.f = foodinfo;
            return this;
        }

        public Item.Info a(int i) {
            if (this.b > 0) {
                throw new RuntimeException("Unable to have damage AND stack.");
            } else {
                this.a = i;
                return this;
            }
        }

        public Item.Info b(int i) {
            return this.b == 0 ? this.c(i) : this;
        }

        public Item.Info c(int i) {
            this.b = i;
            this.a = 1;
            return this;
        }

        public Item.Info a(Item item) {
            this.c = item;
            return this;
        }

        public Item.Info a(CreativeModeTab creativemodetab) {
            this.d = creativemodetab;
            return this;
        }

        public Item.Info a(EnumItemRarity enumitemrarity) {
            this.e = enumitemrarity;
            return this;
        }

        public Item.Info a() {
            this.g = true;
            return this;
        }
    }
}
