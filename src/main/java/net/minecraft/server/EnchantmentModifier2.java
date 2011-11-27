package net.minecraft.server;

final class EnchantmentModifier2 implements EnchantmentModifier {

    public int a;
    public EntityLiving b;

    private EnchantmentModifier2() {}

    public void a(Enchantment enchantment, int i) {
        this.a += enchantment.a(i, this.b);
    }

    EnchantmentModifier2(EmptyClass3 emptyclass3) {
        this();
    }
}
