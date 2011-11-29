package net.minecraft.server;

final class EnchantmentModifierDamage implements EnchantmentModifier {

    public int a;
    public EntityLiving b;

    private EnchantmentModifierDamage() {}

    public void a(Enchantment enchantment, int i) {
        this.a += enchantment.a(i, this.b);
    }

    EnchantmentModifierDamage(EmptyClass3 emptyclass3) {
        this();
    }
}
