package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ItemCrossbow extends ItemProjectileWeapon implements ItemVanishable {

    private boolean c = false;
    private boolean d = false;

    public ItemCrossbow(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public Predicate<ItemStack> e() {
        return ItemCrossbow.b;
    }

    @Override
    public Predicate<ItemStack> b() {
        return ItemCrossbow.a;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (d(itemstack)) {
            a(world, entityhuman, enumhand, itemstack, m(itemstack), 1.0F);
            a(itemstack, false);
            return InteractionResultWrapper.consume(itemstack);
        } else if (!entityhuman.f(itemstack).isEmpty()) {
            if (!d(itemstack)) {
                this.c = false;
                this.d = false;
                entityhuman.c(enumhand);
            }

            return InteractionResultWrapper.consume(itemstack);
        } else {
            return InteractionResultWrapper.fail(itemstack);
        }
    }

    @Override
    public void a(ItemStack itemstack, World world, EntityLiving entityliving, int i) {
        int j = this.e_(itemstack) - i;
        float f = a(j, itemstack);

        if (f >= 1.0F && !d(itemstack) && a(entityliving, itemstack)) {
            a(itemstack, true);
            SoundCategory soundcategory = entityliving instanceof EntityHuman ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;

            world.playSound((EntityHuman) null, entityliving.locX(), entityliving.locY(), entityliving.locZ(), SoundEffects.ITEM_CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (ItemCrossbow.RANDOM.nextFloat() * 0.5F + 1.0F) + 0.2F);
        }

    }

    private static boolean a(EntityLiving entityliving, ItemStack itemstack) {
        int i = EnchantmentManager.getEnchantmentLevel(Enchantments.MULTISHOT, itemstack);
        int j = i == 0 ? 1 : 3;
        boolean flag = entityliving instanceof EntityHuman && ((EntityHuman) entityliving).abilities.canInstantlyBuild;
        ItemStack itemstack1 = entityliving.f(itemstack);
        ItemStack itemstack2 = itemstack1.cloneItemStack();

        for (int k = 0; k < j; ++k) {
            if (k > 0) {
                itemstack1 = itemstack2.cloneItemStack();
            }

            if (itemstack1.isEmpty() && flag) {
                itemstack1 = new ItemStack(Items.ARROW);
                itemstack2 = itemstack1.cloneItemStack();
            }

            if (!a(entityliving, itemstack, itemstack1, k > 0, flag)) {
                return false;
            }
        }

        return true;
    }

    private static boolean a(EntityLiving entityliving, ItemStack itemstack, ItemStack itemstack1, boolean flag, boolean flag1) {
        if (itemstack1.isEmpty()) {
            return false;
        } else {
            boolean flag2 = flag1 && itemstack1.getItem() instanceof ItemArrow;
            ItemStack itemstack2;

            if (!flag2 && !flag1 && !flag) {
                itemstack2 = itemstack1.cloneAndSubtract(1);
                if (itemstack1.isEmpty() && entityliving instanceof EntityHuman) {
                    ((EntityHuman) entityliving).inventory.f(itemstack1);
                }
            } else {
                itemstack2 = itemstack1.cloneItemStack();
            }

            b(itemstack, itemstack2);
            return true;
        }
    }

    public static boolean d(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        return nbttagcompound != null && nbttagcompound.getBoolean("Charged");
    }

    public static void a(ItemStack itemstack, boolean flag) {
        NBTTagCompound nbttagcompound = itemstack.getOrCreateTag();

        nbttagcompound.setBoolean("Charged", flag);
    }

    private static void b(ItemStack itemstack, ItemStack itemstack1) {
        NBTTagCompound nbttagcompound = itemstack.getOrCreateTag();
        NBTTagList nbttaglist;

        if (nbttagcompound.hasKeyOfType("ChargedProjectiles", 9)) {
            nbttaglist = nbttagcompound.getList("ChargedProjectiles", 10);
        } else {
            nbttaglist = new NBTTagList();
        }

        NBTTagCompound nbttagcompound1 = new NBTTagCompound();

        itemstack1.save(nbttagcompound1);
        nbttaglist.add(nbttagcompound1);
        nbttagcompound.set("ChargedProjectiles", nbttaglist);
    }

    private static List<ItemStack> k(ItemStack itemstack) {
        List<ItemStack> list = Lists.newArrayList();
        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("ChargedProjectiles", 9)) {
            NBTTagList nbttaglist = nbttagcompound.getList("ChargedProjectiles", 10);

            if (nbttaglist != null) {
                for (int i = 0; i < nbttaglist.size(); ++i) {
                    NBTTagCompound nbttagcompound1 = nbttaglist.getCompound(i);

                    list.add(ItemStack.a(nbttagcompound1));
                }
            }
        }

        return list;
    }

    private static void l(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null) {
            NBTTagList nbttaglist = nbttagcompound.getList("ChargedProjectiles", 9);

            nbttaglist.clear();
            nbttagcompound.set("ChargedProjectiles", nbttaglist);
        }

    }

    public static boolean a(ItemStack itemstack, Item item) {
        return k(itemstack).stream().anyMatch((itemstack1) -> {
            return itemstack1.getItem() == item;
        });
    }

    private static void a(World world, EntityLiving entityliving, EnumHand enumhand, ItemStack itemstack, ItemStack itemstack1, float f, boolean flag, float f1, float f2, float f3) {
        if (!world.isClientSide) {
            boolean flag1 = itemstack1.getItem() == Items.FIREWORK_ROCKET;
            Object object;

            if (flag1) {
                object = new EntityFireworks(world, itemstack1, entityliving, entityliving.locX(), entityliving.getHeadY() - 0.15000000596046448D, entityliving.locZ(), true);
            } else {
                object = a(world, entityliving, itemstack, itemstack1);
                if (flag || f3 != 0.0F) {
                    ((EntityArrow) object).fromPlayer = EntityArrow.PickupStatus.CREATIVE_ONLY;
                }
            }

            if (entityliving instanceof ICrossbow) {
                ICrossbow icrossbow = (ICrossbow) entityliving;

                icrossbow.a(icrossbow.getGoalTarget(), itemstack, (IProjectile) object, f3);
            } else {
                Vec3D vec3d = entityliving.i(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3fa(vec3d), f3, true);
                Vec3D vec3d1 = entityliving.f(1.0F);
                Vector3fa vector3fa = new Vector3fa(vec3d1);

                vector3fa.a(quaternion);
                ((IProjectile) object).shoot((double) vector3fa.a(), (double) vector3fa.b(), (double) vector3fa.c(), f1, f2);
            }

            itemstack.damage(flag1 ? 3 : 1, entityliving, (entityliving1) -> {
                entityliving1.broadcastItemBreak(enumhand);
            });
            world.addEntity((Entity) object);
            world.playSound((EntityHuman) null, entityliving.locX(), entityliving.locY(), entityliving.locZ(), SoundEffects.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, f);
        }
    }

    private static EntityArrow a(World world, EntityLiving entityliving, ItemStack itemstack, ItemStack itemstack1) {
        ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (itemstack1.getItem() instanceof ItemArrow ? itemstack1.getItem() : Items.ARROW));
        EntityArrow entityarrow = itemarrow.a(world, itemstack1, entityliving);

        if (entityliving instanceof EntityHuman) {
            entityarrow.setCritical(true);
        }

        entityarrow.a(SoundEffects.ITEM_CROSSBOW_HIT);
        entityarrow.setShotFromCrossbow(true);
        int i = EnchantmentManager.getEnchantmentLevel(Enchantments.PIERCING, itemstack);

        if (i > 0) {
            entityarrow.setPierceLevel((byte) i);
        }

        return entityarrow;
    }

    public static void a(World world, EntityLiving entityliving, EnumHand enumhand, ItemStack itemstack, float f, float f1) {
        List<ItemStack> list = k(itemstack);
        float[] afloat = a(entityliving.getRandom());

        for (int i = 0; i < list.size(); ++i) {
            ItemStack itemstack1 = (ItemStack) list.get(i);
            boolean flag = entityliving instanceof EntityHuman && ((EntityHuman) entityliving).abilities.canInstantlyBuild;

            if (!itemstack1.isEmpty()) {
                if (i == 0) {
                    a(world, entityliving, enumhand, itemstack, itemstack1, afloat[i], flag, f, f1, 0.0F);
                } else if (i == 1) {
                    a(world, entityliving, enumhand, itemstack, itemstack1, afloat[i], flag, f, f1, -10.0F);
                } else if (i == 2) {
                    a(world, entityliving, enumhand, itemstack, itemstack1, afloat[i], flag, f, f1, 10.0F);
                }
            }
        }

        a(world, entityliving, itemstack);
    }

    private static float[] a(Random random) {
        boolean flag = random.nextBoolean();

        return new float[]{1.0F, a(flag), a(!flag)};
    }

    private static float a(boolean flag) {
        float f = flag ? 0.63F : 0.43F;

        return 1.0F / (ItemCrossbow.RANDOM.nextFloat() * 0.5F + 1.8F) + f;
    }

    private static void a(World world, EntityLiving entityliving, ItemStack itemstack) {
        if (entityliving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityliving;

            if (!world.isClientSide) {
                CriterionTriggers.F.a(entityplayer, itemstack);
            }

            entityplayer.b(StatisticList.ITEM_USED.b(itemstack.getItem()));
        }

        l(itemstack);
    }

    @Override
    public void a(World world, EntityLiving entityliving, ItemStack itemstack, int i) {
        if (!world.isClientSide) {
            int j = EnchantmentManager.getEnchantmentLevel(Enchantments.QUICK_CHARGE, itemstack);
            SoundEffect soundeffect = this.a(j);
            SoundEffect soundeffect1 = j == 0 ? SoundEffects.ITEM_CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (itemstack.k() - i) / (float) g(itemstack);

            if (f < 0.2F) {
                this.c = false;
                this.d = false;
            }

            if (f >= 0.2F && !this.c) {
                this.c = true;
                world.playSound((EntityHuman) null, entityliving.locX(), entityliving.locY(), entityliving.locZ(), soundeffect, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }

            if (f >= 0.5F && soundeffect1 != null && !this.d) {
                this.d = true;
                world.playSound((EntityHuman) null, entityliving.locX(), entityliving.locY(), entityliving.locZ(), soundeffect1, SoundCategory.PLAYERS, 0.5F, 1.0F);
            }
        }

    }

    @Override
    public int e_(ItemStack itemstack) {
        return g(itemstack) + 3;
    }

    public static int g(ItemStack itemstack) {
        int i = EnchantmentManager.getEnchantmentLevel(Enchantments.QUICK_CHARGE, itemstack);

        return i == 0 ? 25 : 25 - 5 * i;
    }

    @Override
    public EnumAnimation d_(ItemStack itemstack) {
        return EnumAnimation.CROSSBOW;
    }

    private SoundEffect a(int i) {
        switch (i) {
            case 1:
                return SoundEffects.ITEM_CROSSBOW_QUICK_CHARGE_1;
            case 2:
                return SoundEffects.ITEM_CROSSBOW_QUICK_CHARGE_2;
            case 3:
                return SoundEffects.ITEM_CROSSBOW_QUICK_CHARGE_3;
            default:
                return SoundEffects.ITEM_CROSSBOW_LOADING_START;
        }
    }

    private static float a(int i, ItemStack itemstack) {
        float f = (float) i / (float) g(itemstack);

        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    private static float m(ItemStack itemstack) {
        return itemstack.getItem() == Items.CROSSBOW && a(itemstack, Items.FIREWORK_ROCKET) ? 1.6F : 3.15F;
    }

    @Override
    public int d() {
        return 8;
    }
}
