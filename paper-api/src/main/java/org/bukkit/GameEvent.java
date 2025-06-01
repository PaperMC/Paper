package org.bukkit;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a generic Mojang game event.
 */
public abstract class GameEvent implements Keyed {

    // Start generate - GameEvent
    // @GeneratedFrom 1.21.6-pre1
    public static final GameEvent BLOCK_ACTIVATE = getEvent("block_activate");

    public static final GameEvent BLOCK_ATTACH = getEvent("block_attach");

    public static final GameEvent BLOCK_CHANGE = getEvent("block_change");

    public static final GameEvent BLOCK_CLOSE = getEvent("block_close");

    public static final GameEvent BLOCK_DEACTIVATE = getEvent("block_deactivate");

    public static final GameEvent BLOCK_DESTROY = getEvent("block_destroy");

    public static final GameEvent BLOCK_DETACH = getEvent("block_detach");

    public static final GameEvent BLOCK_OPEN = getEvent("block_open");

    public static final GameEvent BLOCK_PLACE = getEvent("block_place");

    public static final GameEvent CONTAINER_CLOSE = getEvent("container_close");

    public static final GameEvent CONTAINER_OPEN = getEvent("container_open");

    public static final GameEvent DRINK = getEvent("drink");

    public static final GameEvent EAT = getEvent("eat");

    public static final GameEvent ELYTRA_GLIDE = getEvent("elytra_glide");

    public static final GameEvent ENTITY_ACTION = getEvent("entity_action");

    public static final GameEvent ENTITY_DAMAGE = getEvent("entity_damage");

    public static final GameEvent ENTITY_DIE = getEvent("entity_die");

    public static final GameEvent ENTITY_DISMOUNT = getEvent("entity_dismount");

    public static final GameEvent ENTITY_INTERACT = getEvent("entity_interact");

    public static final GameEvent ENTITY_MOUNT = getEvent("entity_mount");

    public static final GameEvent ENTITY_PLACE = getEvent("entity_place");

    public static final GameEvent EQUIP = getEvent("equip");

    public static final GameEvent EXPLODE = getEvent("explode");

    public static final GameEvent FLAP = getEvent("flap");

    public static final GameEvent FLUID_PICKUP = getEvent("fluid_pickup");

    public static final GameEvent FLUID_PLACE = getEvent("fluid_place");

    public static final GameEvent HIT_GROUND = getEvent("hit_ground");

    public static final GameEvent INSTRUMENT_PLAY = getEvent("instrument_play");

    public static final GameEvent ITEM_INTERACT_FINISH = getEvent("item_interact_finish");

    public static final GameEvent ITEM_INTERACT_START = getEvent("item_interact_start");

    public static final GameEvent JUKEBOX_PLAY = getEvent("jukebox_play");

    public static final GameEvent JUKEBOX_STOP_PLAY = getEvent("jukebox_stop_play");

    public static final GameEvent LIGHTNING_STRIKE = getEvent("lightning_strike");

    public static final GameEvent NOTE_BLOCK_PLAY = getEvent("note_block_play");

    public static final GameEvent PRIME_FUSE = getEvent("prime_fuse");

    public static final GameEvent PROJECTILE_LAND = getEvent("projectile_land");

    public static final GameEvent PROJECTILE_SHOOT = getEvent("projectile_shoot");

    public static final GameEvent RESONATE_1 = getEvent("resonate_1");

    public static final GameEvent RESONATE_2 = getEvent("resonate_2");

    public static final GameEvent RESONATE_3 = getEvent("resonate_3");

    public static final GameEvent RESONATE_4 = getEvent("resonate_4");

    public static final GameEvent RESONATE_5 = getEvent("resonate_5");

    public static final GameEvent RESONATE_6 = getEvent("resonate_6");

    public static final GameEvent RESONATE_7 = getEvent("resonate_7");

    public static final GameEvent RESONATE_8 = getEvent("resonate_8");

    public static final GameEvent RESONATE_9 = getEvent("resonate_9");

    public static final GameEvent RESONATE_10 = getEvent("resonate_10");

    public static final GameEvent RESONATE_11 = getEvent("resonate_11");

    public static final GameEvent RESONATE_12 = getEvent("resonate_12");

    public static final GameEvent RESONATE_13 = getEvent("resonate_13");

    public static final GameEvent RESONATE_14 = getEvent("resonate_14");

    public static final GameEvent RESONATE_15 = getEvent("resonate_15");

    public static final GameEvent SCULK_SENSOR_TENDRILS_CLICKING = getEvent("sculk_sensor_tendrils_clicking");

    public static final GameEvent SHEAR = getEvent("shear");

    public static final GameEvent SHRIEK = getEvent("shriek");

    public static final GameEvent SPLASH = getEvent("splash");

    public static final GameEvent STEP = getEvent("step");

    public static final GameEvent SWIM = getEvent("swim");

    public static final GameEvent TELEPORT = getEvent("teleport");

    public static final GameEvent UNEQUIP = getEvent("unequip");
    // End generate - GameEvent
    /**
     * @deprecated in favor of {@link #BLOCK_ACTIVATE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent BLOCK_PRESS = BLOCK_ACTIVATE;
    /**
     * @deprecated in favor of {@link #BLOCK_ACTIVATE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent BLOCK_SWITCH = BLOCK_ACTIVATE;
    /**
     * @deprecated in favor of {@link #BLOCK_DEACTIVATE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent BLOCK_UNPRESS = BLOCK_DEACTIVATE;
    /**
     * @deprecated in favor of {@link #BLOCK_DEACTIVATE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent BLOCK_UNSWITCH = BLOCK_DEACTIVATE;
    /**
     * @deprecated in favor of {@link #BLOCK_ACTIVATE}
     */
    @Deprecated(since = "1.20")
    public static final GameEvent DISPENSE_FAIL = BLOCK_ACTIVATE;
    /**
     * @deprecated in favor of {@link #DRINK}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent DRINKING_FINISH = DRINK;
    /**
     * @deprecated in favor of {@link #ELYTRA_GLIDE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent ELYTRA_FREE_FALL = ELYTRA_GLIDE;
    /**
     * @deprecated in favor of {@link #ENTITY_DAMAGE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent ENTITY_DAMAGED = ENTITY_DAMAGE;
    /**
     * @deprecated in favor of {@link #ENTITY_DIE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent ENTITY_DYING = ENTITY_DIE;
    /**
     * @deprecated in favor of {@link #ENTITY_DIE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent ENTITY_KILLED = ENTITY_DIE;
    /**
     * @deprecated in favor of {@link #ENTITY_ACTION}
     */
    @Deprecated(since = "1.20.2")
    public static final GameEvent ENTITY_ROAR = ENTITY_ACTION;
    /**
     * @deprecated in favor of {@link #ENTITY_ACTION}
     */
    @Deprecated(since = "1.20.2")
    public static final GameEvent ENTITY_SHAKE = ENTITY_ACTION;
    /**
     * @deprecated in favor of {@link #ENTITY_INTERACT}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent MOB_INTERACT = ENTITY_INTERACT;
    /**
     * @deprecated in favor of {@link #BLOCK_DEACTIVATE}
     */
    @Deprecated(since = "1.20")
    public static final GameEvent PISTON_CONTRACT = BLOCK_DEACTIVATE;
    /**
     * @deprecated in favor of {@link #BLOCK_ACTIVATE}
     */
    @Deprecated(since = "1.20")
    public static final GameEvent PISTON_EXTEND = BLOCK_ACTIVATE;
    /**
     * @deprecated in favor of {@link #ENTITY_ACTION}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent RAVAGER_ROAR = ENTITY_ACTION;
    /**
     * @deprecated in favor of {@link #BLOCK_CHANGE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent RING_BELL = BLOCK_CHANGE;
    /**
     * @deprecated in favor of {@link #CONTAINER_CLOSE}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent SHULKER_CLOSE = CONTAINER_CLOSE;
    /**
     * @deprecated in favor of {@link #CONTAINER_OPEN}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent SHULKER_OPEN = CONTAINER_OPEN;
    /**
     * @deprecated in favor of {@link #ENTITY_ACTION}
     */
    @Deprecated(since = "1.19")
    public static final GameEvent WOLF_SHAKING = ENTITY_ACTION;

    /**
     * Returns a {@link GameEvent} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the key
     * @return the event or null
     * @deprecated Use {@link Registry#get(NamespacedKey)} instead.
     */
    @Nullable
    @Deprecated(since = "1.20.1")
    public static GameEvent getByKey(@NotNull NamespacedKey namespacedKey) {
        return Registry.GAME_EVENT.get(namespacedKey);
    }

    /**
     * Returns the set of all GameEvents.
     *
     * @return the memoryKeys
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.20.1")
    public static Collection<GameEvent> values() {
        return Collections.unmodifiableCollection(Lists.newArrayList(Registry.GAME_EVENT));
    }

    @NotNull
    private static GameEvent getEvent(@NotNull String key) {
        return Registry.GAME_EVENT.getOrThrow(NamespacedKey.minecraft(key));
    }
    // Paper start
    /**
     * Gets the range of the event which is used to
     * notify listeners of the event.
     *
     * @return the range
     */
    public abstract int getRange();

    /**
     * Gets the vibration level of the game event for vibration listeners.
     * Not all events have vibration levels, and a level of 0 means
     * it won't cause any vibrations.
     *
     * @return the vibration level
     */
    public abstract int getVibrationLevel();
    // Paper end
}
