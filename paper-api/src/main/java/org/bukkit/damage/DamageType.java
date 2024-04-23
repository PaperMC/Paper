package org.bukkit.damage;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represent a type of damage that an entity can receive.
 * <p>
 * Constants in this class include the base types provided by the vanilla
 * server. Data packs are capable of registering more types of damage which may
 * be obtained through the {@link Registry#DAMAGE_TYPE}.
 *
 * @see <a href="https://minecraft.wiki/w/Damage_type">Minecraft Wiki</a>
 */
@ApiStatus.Experimental
public interface DamageType extends Keyed, Translatable {

    public static final DamageType IN_FIRE = getDamageType("in_fire");
    public static final DamageType LIGHTNING_BOLT = getDamageType("lightning_bolt");
    public static final DamageType ON_FIRE = getDamageType("on_fire");
    public static final DamageType LAVA = getDamageType("lava");
    public static final DamageType HOT_FLOOR = getDamageType("hot_floor");
    public static final DamageType IN_WALL = getDamageType("in_wall");
    public static final DamageType CRAMMING = getDamageType("cramming");
    public static final DamageType DROWN = getDamageType("drown");
    public static final DamageType STARVE = getDamageType("starve");
    public static final DamageType CACTUS = getDamageType("cactus");
    public static final DamageType FALL = getDamageType("fall");
    public static final DamageType FLY_INTO_WALL = getDamageType("fly_into_wall");
    public static final DamageType OUT_OF_WORLD = getDamageType("out_of_world");
    public static final DamageType GENERIC = getDamageType("generic");
    public static final DamageType MAGIC = getDamageType("magic");
    public static final DamageType WITHER = getDamageType("wither");
    public static final DamageType DRAGON_BREATH = getDamageType("dragon_breath");
    public static final DamageType DRY_OUT = getDamageType("dry_out");
    public static final DamageType SWEET_BERRY_BUSH = getDamageType("sweet_berry_bush");
    public static final DamageType FREEZE = getDamageType("freeze");
    public static final DamageType STALAGMITE = getDamageType("stalagmite");
    public static final DamageType FALLING_BLOCK = getDamageType("falling_block");
    public static final DamageType FALLING_ANVIL = getDamageType("falling_anvil");
    public static final DamageType FALLING_STALACTITE = getDamageType("falling_stalactite");
    public static final DamageType STING = getDamageType("sting");
    public static final DamageType MOB_ATTACK = getDamageType("mob_attack");
    public static final DamageType MOB_ATTACK_NO_AGGRO = getDamageType("mob_attack_no_aggro");
    public static final DamageType PLAYER_ATTACK = getDamageType("player_attack");
    public static final DamageType ARROW = getDamageType("arrow");
    public static final DamageType TRIDENT = getDamageType("trident");
    public static final DamageType MOB_PROJECTILE = getDamageType("mob_projectile");
    public static final DamageType SPIT = getDamageType("spit");
    public static final DamageType FIREWORKS = getDamageType("fireworks");
    public static final DamageType FIREBALL = getDamageType("fireball");
    public static final DamageType UNATTRIBUTED_FIREBALL = getDamageType("unattributed_fireball");
    public static final DamageType WITHER_SKULL = getDamageType("wither_skull");
    public static final DamageType THROWN = getDamageType("thrown");
    public static final DamageType INDIRECT_MAGIC = getDamageType("indirect_magic");
    public static final DamageType THORNS = getDamageType("thorns");
    public static final DamageType EXPLOSION = getDamageType("explosion");
    public static final DamageType PLAYER_EXPLOSION = getDamageType("player_explosion");
    public static final DamageType SONIC_BOOM = getDamageType("sonic_boom");
    public static final DamageType BAD_RESPAWN_POINT = getDamageType("bad_respawn_point");
    public static final DamageType OUTSIDE_BORDER = getDamageType("outside_border");
    public static final DamageType GENERIC_KILL = getDamageType("generic_kill");

    @NotNull
    private static DamageType getDamageType(@NotNull String key) {
        NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
        return Preconditions.checkNotNull(Registry.DAMAGE_TYPE.get(namespacedKey), "No DamageType found for %s. This is a bug.", namespacedKey);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The returned key is that of the death message sent when this damage type
     * is responsible for the death of an entity.
     * <p>
     * <strong>Note</strong> This translation key is only used if
     * {@link #getDeathMessageType()} is {@link DeathMessageType#DEFAULT}
     */
    @NotNull
    @Override
    public String getTranslationKey();

    /**
     * Get the {@link DamageScaling} for this damage type.
     *
     * @return the damage scaling
     */
    @NotNull
    public DamageScaling getDamageScaling();

    /**
     * Get the {@link DamageEffect} for this damage type.
     *
     * @return the damage effect
     */
    @NotNull
    public DamageEffect getDamageEffect();

    /**
     * Get the {@link DeathMessageType} for this damage type.
     *
     * @return the death message type
     */
    @NotNull
    public DeathMessageType getDeathMessageType();

    /**
     * Get the amount of hunger exhaustion caused by this damage type.
     *
     * @return the exhaustion
     */
    public float getExhaustion();
}
