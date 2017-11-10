package org.bukkit;

import java.util.Map;

import com.google.common.collect.Maps;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.ZombieVillager;

/**
 * A list of all Effects that can happen to entities.
 */
public enum EntityEffect {

    /**
     * Colored particles from a tipped arrow.
     */
    ARROW_PARTICLES(0, TippedArrow.class),
    /**
     * Rabbit jumping.
     */
    RABBIT_JUMP(1, Rabbit.class),
    /**
     * When mobs get hurt.
     */
    HURT(2, LivingEntity.class),
    /**
     * When a mob dies.
     * <p>
     * <b>This will cause client-glitches!</b>
     *
     * @deprecated although this effect may trigger other events on non-living
     * entities, it's only supported usage is on living ones.
     */
    @Deprecated
    DEATH(3, Entity.class),
    // PAIL - SPIGOT-3641 duplicate
    // GOLEM_ATTACK(4, IronGolem.class),
    // 5 - unused
    /**
     * The smoke when taming a wolf fails.
     */
    WOLF_SMOKE(6, Tameable.class),
    /**
     * The hearts when taming a wolf succeeds.
     */
    WOLF_HEARTS(7, Wolf.class),
    /**
     * When a wolf shakes (after being wet).
     */
    WOLF_SHAKE(8, Wolf.class),
    // 9 - unused
    /**
     * When an entity eats a LONG_GRASS block.
     *
     * @deprecated although this effect may trigger other events on non-living
     * entities, it's only supported usage is on living ones.
     */
    @Deprecated
    SHEEP_EAT(10, Entity.class),
    /**
     * When an Iron Golem gives a rose.
     */
    IRON_GOLEM_ROSE(11, IronGolem.class),
    /**
     * Hearts from a villager.
     */
    VILLAGER_HEART(12, Villager.class),
    /**
     * When a villager is angry.
     */
    VILLAGER_ANGRY(13, Villager.class),
    /**
     * Happy particles from a villager.
     */
    VILLAGER_HAPPY(14, Villager.class),
    /**
     * Magic particles from a witch.
     */
    WITCH_MAGIC(15, Witch.class),
    /**
     * When a zombie transforms into a villager by shaking violently.
     */
    ZOMBIE_TRANSFORM(16, ZombieVillager.class),
    /**
     * When a firework explodes.
     */
    FIREWORK_EXPLODE(17, Firework.class),
    /**
     * Hearts from a breeding entity.
     */
    LOVE_HEARTS(18, Ageable.class),
    /**
     * Resets squid rotation.
     */
    SQUID_ROTATE(19, Squid.class),
    /**
     * Silverfish entering block, spawner spawning.
     */
    ENTITY_POOF(20, LivingEntity.class),
    /**
     * Guardian sets laser target.
     */
    GUARDIAN_TARGET(21, Guardian.class),
    // 22-28 player internal flags
    /**
     * Shield blocks attack.
     */
    SHIELD_BLOCK(29, LivingEntity.class),
    /**
     * Shield breaks.
     */
    SHIELD_BREAK(30, LivingEntity.class),
    // 31 - unused
    /**
     * Armor stand is hit.
     */
    ARMOR_STAND_HIT(32, ArmorStand.class),
    /**
     * Entity hurt by thorns attack.
     */
    THORNS_HURT(33, LivingEntity.class),
    /**
     * Iron golem puts away rose.
     */
    IRON_GOLEM_SHEATH(34, IronGolem.class),
    /**
     * Totem prevents entity death.
     */
    TOTEM_RESURRECT(35, LivingEntity.class),
    /**
     * Entity hurt due to drowning damage.
     */
    HURT_DROWN(36, LivingEntity.class),
    /**
     * Entity hurt due to explosion damage.
     */
    HURT_EXPLOSION(37, LivingEntity.class);

    private final byte data;
    private final Class<? extends Entity> applicable;
    private final static Map<Byte, EntityEffect> BY_DATA = Maps.newHashMap();

    EntityEffect(final int data, Class<? extends Entity> clazz) {
        this.data = (byte) data;
        this.applicable = clazz;
    }

    /**
     * Gets the data value of this EntityEffect
     *
     * @return The data value
     * @deprecated Magic value
     */
    @Deprecated
    public byte getData() {
        return data;
    }

    /**
     * Gets entity superclass which this affect is applicable to.
     *
     * @return applicable class
     */
    public Class<? extends Entity> getApplicable() {
        return applicable;
    }

    /**
     * Gets the EntityEffect with the given data value
     *
     * @param data Data value to fetch
     * @return The {@link EntityEffect} representing the given value, or null
     *     if it doesn't exist
     * @deprecated Magic value
     */
    @Deprecated
    public static EntityEffect getByData(final byte data) {
        return BY_DATA.get(data);
    }

    static {
        for (EntityEffect entityEffect : values()) {
            BY_DATA.put(entityEffect.data, entityEffect);
        }
    }
}
