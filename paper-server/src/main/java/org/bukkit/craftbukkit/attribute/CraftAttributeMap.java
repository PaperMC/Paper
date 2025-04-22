package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class CraftAttributeMap implements Attributable {

    private final AttributeMap handle;

    // convert legacy attributes
    private static final com.google.common.collect.ImmutableMap<String, String> LEGACY_ATTRIBUTE_MAP = com.google.common.collect.ImmutableMap.<String, String>builder()
        .put("generic.maxHealth", "generic.max_health")
        .put("Max Health", "generic.max_health")
        .put("zombie.spawnReinforcements", "zombie.spawn_reinforcements")
        .put("Spawn Reinforcements Chance", "zombie.spawn_reinforcements")
        .put("horse.jumpStrength", "horse.jump_strength")
        .put("Jump Strength", "horse.jump_strength")
        .put("generic.followRange", "generic.follow_range")
        .put("Follow Range", "generic.follow_range")
        .put("generic.knockbackResistance", "generic.knockback_resistance")
        .put("Knockback Resistance", "generic.knockback_resistance")
        .put("generic.movementSpeed", "generic.movement_speed")
        .put("Movement Speed", "generic.movement_speed")
        .put("generic.flyingSpeed", "generic.flying_speed")
        .put("Flying Speed", "generic.flying_speed")
        .put("generic.attackDamage", "generic.attack_damage")
        .put("generic.attackKnockback", "generic.attack_knockback")
        .put("generic.attackSpeed", "generic.attack_speed")
        .put("generic.armorToughness", "generic.armor_toughness")
        .buildOrThrow();

    public static String convertIfNeeded(String nms) {
        nms = LEGACY_ATTRIBUTE_MAP.getOrDefault(nms, nms);
        if (!nms.toLowerCase(java.util.Locale.ROOT).equals(nms) || nms.indexOf(' ') != -1) {
            return null;
        }
        return nms;
    }

    public CraftAttributeMap(AttributeMap handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.world.entity.ai.attributes.AttributeInstance nms = this.handle.getInstance(CraftAttribute.bukkitToMinecraftHolder(attribute));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    @Override
    public void registerAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        this.handle.registerAttribute(CraftAttribute.bukkitToMinecraftHolder(attribute));
    }
}
