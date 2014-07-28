package net.minecraft.server;

public class GenericAttributes {

    public static final AttributeBase MAX_HEALTH = a("generic.max_health", (new AttributeRanged("attribute.name.generic.max_health", 20.0D, 1.0D, org.spigotmc.SpigotConfig.maxHealth)).a(true));
    public static final AttributeBase FOLLOW_RANGE = a("generic.follow_range", new AttributeRanged("attribute.name.generic.follow_range", 32.0D, 0.0D, 2048.0D));
    public static final AttributeBase KNOCKBACK_RESISTANCE = a("generic.knockback_resistance", new AttributeRanged("attribute.name.generic.knockback_resistance", 0.0D, 0.0D, 1.0D));
    public static final AttributeBase MOVEMENT_SPEED = a("generic.movement_speed", (new AttributeRanged("attribute.name.generic.movement_speed", 0.699999988079071D, 0.0D, org.spigotmc.SpigotConfig.movementSpeed)).a(true));
    public static final AttributeBase FLYING_SPEED = a("generic.flying_speed", (new AttributeRanged("attribute.name.generic.flying_speed", 0.4000000059604645D, 0.0D, 1024.0D)).a(true));
    public static final AttributeBase ATTACK_DAMAGE = a("generic.attack_damage", new AttributeRanged("attribute.name.generic.attack_damage", 2.0D, 0.0D, org.spigotmc.SpigotConfig.attackDamage));
    public static final AttributeBase ATTACK_KNOCKBACK = a("generic.attack_knockback", new AttributeRanged("attribute.name.generic.attack_knockback", 0.0D, 0.0D, 5.0D));
    public static final AttributeBase ATTACK_SPEED = a("generic.attack_speed", (new AttributeRanged("attribute.name.generic.attack_speed", 4.0D, 0.0D, 1024.0D)).a(true));
    public static final AttributeBase ARMOR = a("generic.armor", (new AttributeRanged("attribute.name.generic.armor", 0.0D, 0.0D, 30.0D)).a(true));
    public static final AttributeBase ARMOR_TOUGHNESS = a("generic.armor_toughness", (new AttributeRanged("attribute.name.generic.armor_toughness", 0.0D, 0.0D, 20.0D)).a(true));
    public static final AttributeBase LUCK = a("generic.luck", (new AttributeRanged("attribute.name.generic.luck", 0.0D, -1024.0D, 1024.0D)).a(true));
    public static final AttributeBase SPAWN_REINFORCEMENTS = a("zombie.spawn_reinforcements", new AttributeRanged("attribute.name.zombie.spawn_reinforcements", 0.0D, 0.0D, 1.0D));
    public static final AttributeBase JUMP_STRENGTH = a("horse.jump_strength", (new AttributeRanged("attribute.name.horse.jump_strength", 0.7D, 0.0D, 2.0D)).a(true));

    private static AttributeBase a(String s, AttributeBase attributebase) {
        return (AttributeBase) IRegistry.a(IRegistry.ATTRIBUTE, s, attributebase);
    }
}
