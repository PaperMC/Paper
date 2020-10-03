package net.minecraft.server;

public class EnchantmentWeaponDamage extends Enchantment {

    private static final String[] d = new String[]{"all", "undead", "arthropods"};
    private static final int[] e = new int[]{1, 5, 5};
    private static final int[] f = new int[]{11, 8, 8};
    private static final int[] g = new int[]{20, 20, 20};
    public final int a;

    public EnchantmentWeaponDamage(Enchantment.Rarity enchantment_rarity, int i, EnumItemSlot... aenumitemslot) {
        super(enchantment_rarity, EnchantmentSlotType.WEAPON, aenumitemslot);
        this.a = i;
    }

    @Override
    public int a(int i) {
        return EnchantmentWeaponDamage.e[this.a] + (i - 1) * EnchantmentWeaponDamage.f[this.a];
    }

    @Override
    public int b(int i) {
        return this.a(i) + EnchantmentWeaponDamage.g[this.a];
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float a(int i, EnumMonsterType enummonstertype) {
        return this.a == 0 ? 1.0F + (float) Math.max(0, i - 1) * 0.5F : (this.a == 1 && enummonstertype == EnumMonsterType.UNDEAD ? (float) i * 2.5F : (this.a == 2 && enummonstertype == EnumMonsterType.ARTHROPOD ? (float) i * 2.5F : 0.0F));
    }

    @Override
    public boolean a(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentWeaponDamage);
    }

    @Override
    public boolean canEnchant(ItemStack itemstack) {
        return itemstack.getItem() instanceof ItemAxe ? true : super.canEnchant(itemstack);
    }

    @Override
    public void a(EntityLiving entityliving, Entity entity, int i) {
        if (entity instanceof EntityLiving) {
            EntityLiving entityliving1 = (EntityLiving) entity;

            if (this.a == 2 && entityliving1.getMonsterType() == EnumMonsterType.ARTHROPOD) {
                int j = 20 + entityliving.getRandom().nextInt(10 * i);

                entityliving1.addEffect(new MobEffect(MobEffects.SLOWER_MOVEMENT, j, 3));
            }
        }

    }
}
