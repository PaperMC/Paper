package org.bukkit;

import com.google.common.base.Preconditions;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Warden;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.jetbrains.annotations.NotNull;

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
     * Resets a spawner minecart's delay to 200. Does not effect actual spawning
     * delay, only the speed at which the entity in the spawner spins
     */
    RESET_SPAWNER_MINECART_DELAY(1, SpawnerMinecart.class),
    /**
     * When mobs get hurt.
     *
     * @deprecated Use {@link LivingEntity#playHurtAnimation(float)}
     */
    @Deprecated
    HURT(2, LivingEntity.class),
    /**
     * When a mob dies.
     * <p>
     * <b>This will cause client-glitches!</b>
     *
     * @deprecated split into individual effects
     * @see #EGG_BREAK
     * @see #SNOWBALL_BREAK
     * @see #ENTITY_DEATH
     */
    @Deprecated
    DEATH(3, Entity.class),
    /**
     * Spawns the egg breaking particles
     */
    EGG_BREAK(3, Egg.class),
    /**
     * Spawns the snowball breaking particles
     */
    SNOWBALL_BREAK(3, Snowball.class),
    /**
     * Plays the entity death sound and animation
     * <p>
     * <b>This will cause client-glitches!</b>
     */
    ENTITY_DEATH(3, LivingEntity.class),
    /**
     * Plays the fang attack animation
     */
    FANG_ATTACK(4, EvokerFangs.class),
    /**
     * Plays the hoglin attack animation
     */
    HOGLIN_ATTACK(4, Hoglin.class),
    /**
     * Plays the iron golem attack animation
     */
    IRON_GOLEN_ATTACK(4, IronGolem.class),
    /**
     * Plays the ravager attack animation
     */
    RAVAGER_ATTACK(4, Ravager.class),
    /**
     * Plays the warden attack animation
     */
    WARDEN_ATTACK(4, Warden.class),
    /**
     * Plays the zoglin attack animation
     */
    ZOGLIN_ATTACK(4, Zoglin.class),
    // 5 - unused
    /**
     * The smoke when taming an entity fails.
     */
    WOLF_SMOKE(6, Tameable.class),
    /**
     * The hearts when taming an entity succeeds.
     */
    WOLF_HEARTS(7, Tameable.class),
    /**
     * When a wolf shakes (after being wet).
     *
     * @see EntityEffect#WOLF_SHAKE_STOP
     */
    WOLF_SHAKE(8, Wolf.class),
    // 9 - unused
    /**
     * When an entity eats a LONG_GRASS block.
     *
     * @deprecated split into individual effects
     * @see #SHEEP_EAT_GRASS
     * @see #TNT_MINECART_IGNITE
     */
    @Deprecated
    SHEEP_EAT(10, Entity.class),
    /**
     * Plays the sheep eating grass animation
     */
    SHEEP_EAT_GRASS(10, Sheep.class),
    /**
     * Causes the TNT minecart to ignite, does not play the ignition sound
     * <p>
     * <b>This will cause client-glitches!</b>
     */
    TNT_MINECART_IGNITE(10, ExplosiveMinecart.class),
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
     * Guardian plays the attack sound effect.
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
    HURT_EXPLOSION(37, LivingEntity.class),
    /**
     * Dolphin has been fed and is locating a structure.
     */
    DOLPHIN_FED(38, Dolphin.class),
    /**
     * Ravager has been stunned for 40 ticks.
     */
    RAVAGER_STUNNED(39, Ravager.class),
    /**
     * Cat taming failed.
     */
    CAT_TAME_FAIL(40, Cat.class),
    /**
     * Cat taming succeeded.
     */
    CAT_TAME_SUCCESS(41, Cat.class),
    /**
     * Villager splashes particles during a raid.
     */
    VILLAGER_SPLASH(42, Villager.class),
    /**
     * Player's bad omen effect removed to start or increase raid difficult.
     */
    PLAYER_BAD_OMEN_RAID(43, Player.class),
    /**
     * Entity hurt due to berry bush. Prickly!
     */
    HURT_BERRY_BUSH(44, LivingEntity.class),
    /**
     * Fox chews the food in its mouth
     */
    FOX_CHEW(45, Fox.class),
    /**
     * Entity teleported as a result of chorus fruit or as an enderman
     */
    TELEPORT_ENDER(46, LivingEntity.class),
    /**
     * Entity breaks item in main hand
     */
    BREAK_EQUIPMENT_MAIN_HAND(47, LivingEntity.class),
    /**
     * Entity breaks item in off hand
     */
    BREAK_EQUIPMENT_OFF_HAND(48, LivingEntity.class),
    /**
     * Entity breaks item in helmet slot
     */
    BREAK_EQUIPMENT_HELMET(49, LivingEntity.class),
    /**
     * Entity breaks item in chestplate slot
     */
    BREAK_EQUIPMENT_CHESTPLATE(50, LivingEntity.class),
    /**
     * Entity breaks item in legging slot
     */
    BREAK_EQUIPMENT_LEGGINGS(51, LivingEntity.class),
    /**
     * Entity breaks item in boot slot
     */
    BREAK_EQUIPMENT_BOOTS(52, LivingEntity.class),
    /**
     * Spawns honey block slide particles at the entity's feet
     */
    HONEY_BLOCK_SLIDE_PARTICLES(53, Entity.class),
    /**
     * Spawns honey block fall particles at the entity's feet
     */
    HONEY_BLOCK_FALL_PARTICLES(54, LivingEntity.class),
    /**
     * Entity swaps the items in their hand and offhand
     */
    SWAP_HAND_ITEMS(55, LivingEntity.class),
    /**
     * Stops a wolf that is currently shaking
     *
     * @see EntityEffect#WOLF_SHAKE
     */
    WOLF_SHAKE_STOP(56, Wolf.class),
    // 57 - unused
    /**
     * Goat lowers its head for ramming
     *
     * @see #GOAT_RAISE_HEAD
     */
    GOAT_LOWER_HEAD(58, Goat.class),
    /**
     * Goat raises its head
     *
     * @see #GOAT_LOWER_HEAD
     */
    GOAT_RAISE_HEAD(59, Goat.class),
    /**
     * Spawns death smoke particles
     */
    SPAWN_DEATH_SMOKE(60, LivingEntity.class),
    /**
     * Warden shakes its tendrils
     */
    WARDEN_TENDRIL_SHAKE(61, Warden.class),
    /**
     * Warden performs sonic attack animation <br>
     * Does not play the sound or fire the beam
     */
    WARDEN_SONIC_ATTACK(62, Warden.class),
    /**
     * Plays sniffer digging sound <br>
     * Sniffer must have a target and be in {@link Sniffer.State#SEARCHING} or
     * {@link Sniffer.State#DIGGING}
     */
    SNIFFER_DIG(63, Sniffer.class);

    private final byte data;
    private final Class<? extends Entity> applicable;

    EntityEffect(final int data, /*@NotNull*/ Class<? extends Entity> clazz) {
        this.data = (byte) data;
        this.applicable = clazz;
    }

    /**
     * Gets the data value of this EntityEffect, may not be unique.
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
    @NotNull
    public Class<? extends Entity> getApplicable() {
        return applicable;
    }

    /**
     * Checks if this effect is applicable to the given entity.
     *
     * @param entity the entity to check
     * @return true if applicable
     */
    public boolean isApplicableTo(@NotNull Entity entity) {
        Preconditions.checkArgument(entity != null, "Entity cannot be null");

        return isApplicableTo(entity.getClass());
    }

    /**
     * Checks if this effect is applicable to the given entity class.
     *
     * @param clazz the entity class to check
     * @return true if applicable
     */
    public boolean isApplicableTo(@NotNull Class<? extends Entity> clazz) {
        Preconditions.checkArgument(clazz != null, "Class cannot be null");

        return applicable.isAssignableFrom(clazz);
    }
}
