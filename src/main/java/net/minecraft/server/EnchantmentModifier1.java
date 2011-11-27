package net.minecraft.server;

final class EnchantmentModifier1 implements EnchantmentModifier {

    public int a;
    public DamageSource b;

    private EnchantmentModifier1() {}

    public void a(Enchantment enchantment, int i) {
        this.a += enchantment.a(i, this.b);
    }

    EnchantmentModifier1(EmptyClass3 emptyclass3) {
        this();
    }
}
