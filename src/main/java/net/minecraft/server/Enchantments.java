package net.minecraft.server;

public class Enchantments {

    private static final EnumItemSlot[] M = new EnumItemSlot[]{EnumItemSlot.HEAD, EnumItemSlot.CHEST, EnumItemSlot.LEGS, EnumItemSlot.FEET};
    public static final Enchantment PROTECTION_ENVIRONMENTAL = a("protection", new EnchantmentProtection(Enchantment.Rarity.COMMON, EnchantmentProtection.DamageType.ALL, Enchantments.M));
    public static final Enchantment PROTECTION_FIRE = a("fire_protection", new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.DamageType.FIRE, Enchantments.M));
    public static final Enchantment PROTECTION_FALL = a("feather_falling", new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.DamageType.FALL, Enchantments.M));
    public static final Enchantment PROTECTION_EXPLOSIONS = a("blast_protection", new EnchantmentProtection(Enchantment.Rarity.RARE, EnchantmentProtection.DamageType.EXPLOSION, Enchantments.M));
    public static final Enchantment PROTECTION_PROJECTILE = a("projectile_protection", new EnchantmentProtection(Enchantment.Rarity.UNCOMMON, EnchantmentProtection.DamageType.PROJECTILE, Enchantments.M));
    public static final Enchantment OXYGEN = a("respiration", new EnchantmentOxygen(Enchantment.Rarity.RARE, Enchantments.M));
    public static final Enchantment WATER_WORKER = a("aqua_affinity", new EnchantmentWaterWorker(Enchantment.Rarity.RARE, Enchantments.M));
    public static final Enchantment THORNS = a("thorns", new EnchantmentThorns(Enchantment.Rarity.VERY_RARE, Enchantments.M));
    public static final Enchantment DEPTH_STRIDER = a("depth_strider", new EnchantmentDepthStrider(Enchantment.Rarity.RARE, Enchantments.M));
    public static final Enchantment FROST_WALKER = a("frost_walker", new EnchantmentFrostWalker(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.FEET}));
    public static final Enchantment BINDING_CURSE = a("binding_curse", new EnchantmentBinding(Enchantment.Rarity.VERY_RARE, Enchantments.M));
    public static final Enchantment SOUL_SPEED = a("soul_speed", new EnchantmentSoulSpeed(Enchantment.Rarity.VERY_RARE, new EnumItemSlot[]{EnumItemSlot.FEET}));
    public static final Enchantment DAMAGE_ALL = a("sharpness", new EnchantmentWeaponDamage(Enchantment.Rarity.COMMON, 0, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment DAMAGE_UNDEAD = a("smite", new EnchantmentWeaponDamage(Enchantment.Rarity.UNCOMMON, 1, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment DAMAGE_ARTHROPODS = a("bane_of_arthropods", new EnchantmentWeaponDamage(Enchantment.Rarity.UNCOMMON, 2, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment KNOCKBACK = a("knockback", new EnchantmentKnockback(Enchantment.Rarity.UNCOMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment FIRE_ASPECT = a("fire_aspect", new EnchantmentFire(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment LOOT_BONUS_MOBS = a("looting", new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnchantmentSlotType.WEAPON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment SWEEPING = a("sweeping", new EnchantmentSweeping(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment DIG_SPEED = a("efficiency", new EnchantmentDigging(Enchantment.Rarity.COMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment SILK_TOUCH = a("silk_touch", new EnchantmentSilkTouch(Enchantment.Rarity.VERY_RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment DURABILITY = a("unbreaking", new EnchantmentDurability(Enchantment.Rarity.UNCOMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment LOOT_BONUS_BLOCKS = a("fortune", new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnchantmentSlotType.DIGGER, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment ARROW_DAMAGE = a("power", new EnchantmentArrowDamage(Enchantment.Rarity.COMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment ARROW_KNOCKBACK = a("punch", new EnchantmentArrowKnockback(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment ARROW_FIRE = a("flame", new EnchantmentFlameArrows(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment ARROW_INFINITE = a("infinity", new EnchantmentInfiniteArrows(Enchantment.Rarity.VERY_RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment LUCK = a("luck_of_the_sea", new EnchantmentLootBonus(Enchantment.Rarity.RARE, EnchantmentSlotType.FISHING_ROD, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment LURE = a("lure", new EnchantmentLure(Enchantment.Rarity.RARE, EnchantmentSlotType.FISHING_ROD, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment LOYALTY = a("loyalty", new EnchantmentTridentLoyalty(Enchantment.Rarity.UNCOMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment IMPALING = a("impaling", new EnchantmentTridentImpaling(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment RIPTIDE = a("riptide", new EnchantmentTridentRiptide(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment CHANNELING = a("channeling", new EnchantmentTridentChanneling(Enchantment.Rarity.VERY_RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment MULTISHOT = a("multishot", new EnchantmentMultishot(Enchantment.Rarity.RARE, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment QUICK_CHARGE = a("quick_charge", new EnchantmentQuickCharge(Enchantment.Rarity.UNCOMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment PIERCING = a("piercing", new EnchantmentPiercing(Enchantment.Rarity.COMMON, new EnumItemSlot[]{EnumItemSlot.MAINHAND}));
    public static final Enchantment MENDING = a("mending", new EnchantmentMending(Enchantment.Rarity.RARE, EnumItemSlot.values()));
    public static final Enchantment VANISHING_CURSE = a("vanishing_curse", new EnchantmentVanishing(Enchantment.Rarity.VERY_RARE, EnumItemSlot.values()));

    // CraftBukkit start
    static {
        for (Object enchantment : IRegistry.ENCHANTMENT) {
            org.bukkit.enchantments.Enchantment.registerEnchantment(new org.bukkit.craftbukkit.enchantments.CraftEnchantment((Enchantment) enchantment));
        }
    }
    // CraftBukkit end

    private static Enchantment a(String s, Enchantment enchantment) {
        return (Enchantment) IRegistry.a(IRegistry.ENCHANTMENT, s, enchantment); // CraftBukkit - decompile error
    }
}
