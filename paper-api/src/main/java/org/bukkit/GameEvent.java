package org.bukkit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a generic Mojang game event.
 */
public final class GameEvent implements Keyed {

    private static final Map<NamespacedKey, GameEvent> GAME_EVENTS = new HashMap<>();
    //
    public static final GameEvent BLOCK_ACTIVATE = getEvent("block_activate");
    public static final GameEvent BLOCK_ATTACH = getEvent("block_attach");
    public static final GameEvent BLOCK_CHANGE = getEvent("block_change");
    public static final GameEvent BLOCK_CLOSE = getEvent("block_close");
    public static final GameEvent BLOCK_DEACTIVATE = getEvent("block_deactivate");
    public static final GameEvent BLOCK_DESTROY = getEvent("block_destroy");
    public static final GameEvent BLOCK_DETACH = getEvent("block_detach");
    public static final GameEvent BLOCK_OPEN = getEvent("block_open");
    public static final GameEvent BLOCK_PLACE = getEvent("block_place");
    @Deprecated
    public static final GameEvent BLOCK_PRESS = getEvent("block_activate");
    @Deprecated
    public static final GameEvent BLOCK_SWITCH = getEvent("block_activate");
    @Deprecated
    public static final GameEvent BLOCK_UNPRESS = getEvent("block_deactivate");
    @Deprecated
    public static final GameEvent BLOCK_UNSWITCH = getEvent("block_deactivate");
    public static final GameEvent CONTAINER_CLOSE = getEvent("container_close");
    public static final GameEvent CONTAINER_OPEN = getEvent("container_open");
    public static final GameEvent DISPENSE_FAIL = getEvent("dispense_fail");
    public static final GameEvent DRINK = getEvent("drink");
    @Deprecated
    public static final GameEvent DRINKING_FINISH = getEvent("drink");
    public static final GameEvent EAT = getEvent("eat");
    @Deprecated
    public static final GameEvent ELYTRA_FREE_FALL = getEvent("elytra_glide");
    public static final GameEvent ELYTRA_GLIDE = getEvent("elytra_glide");
    public static final GameEvent ENTITY_DAMAGE = getEvent("entity_damage");
    @Deprecated
    public static final GameEvent ENTITY_DAMAGED = getEvent("entity_damage");
    public static final GameEvent ENTITY_DIE = getEvent("entity_die");
    @Deprecated
    public static final GameEvent ENTITY_DYING = getEvent("entity_die");
    public static final GameEvent ENTITY_INTERACT = getEvent("entity_interact");
    @Deprecated
    public static final GameEvent ENTITY_KILLED = getEvent("entity_die");
    public static final GameEvent ENTITY_PLACE = getEvent("entity_place");
    public static final GameEvent ENTITY_ROAR = getEvent("entity_roar");
    public static final GameEvent ENTITY_SHAKE = getEvent("entity_shake");
    public static final GameEvent EQUIP = getEvent("equip");
    public static final GameEvent EXPLODE = getEvent("explode");
    public static final GameEvent FLAP = getEvent("flap");
    public static final GameEvent FLUID_PICKUP = getEvent("fluid_pickup");
    public static final GameEvent FLUID_PLACE = getEvent("fluid_place");
    public static final GameEvent HIT_GROUND = getEvent("hit_ground");
    public static final GameEvent INSTRUMENT_PLAY = getEvent("instrument_play");
    public static final GameEvent ITEM_INTERACT_FINISH = getEvent("item_interact_finish");
    public static final GameEvent ITEM_INTERACT_START = getEvent("item_interact_start");
    public static final GameEvent LIGHTNING_STRIKE = getEvent("lightning_strike");
    @Deprecated
    public static final GameEvent MOB_INTERACT = getEvent("entity_interact");
    public static final GameEvent NOTE_BLOCK_PLAY = getEvent("note_block_play");
    public static final GameEvent PISTON_CONTRACT = getEvent("piston_contract");
    public static final GameEvent PISTON_EXTEND = getEvent("piston_extend");
    public static final GameEvent PRIME_FUSE = getEvent("prime_fuse");
    public static final GameEvent PROJECTILE_LAND = getEvent("projectile_land");
    public static final GameEvent PROJECTILE_SHOOT = getEvent("projectile_shoot");
    @Deprecated
    public static final GameEvent RAVAGER_ROAR = getEvent("entity_roar");
    @Deprecated
    public static final GameEvent RING_BELL = getEvent("block_change");
    public static final GameEvent SCULK_SENSOR_TENDRILS_CLICKING = getEvent("sculk_sensor_tendrils_clicking");
    public static final GameEvent SHEAR = getEvent("shear");
    public static final GameEvent SHRIEK = getEvent("shriek");
    @Deprecated
    public static final GameEvent SHULKER_CLOSE = getEvent("container_close");
    @Deprecated
    public static final GameEvent SHULKER_OPEN = getEvent("container_open");
    public static final GameEvent SPLASH = getEvent("splash");
    public static final GameEvent STEP = getEvent("step");
    public static final GameEvent SWIM = getEvent("swim");
    public static final GameEvent TELEPORT = getEvent("teleport");
    @Deprecated
    public static final GameEvent WOLF_SHAKING = getEvent("entity_shake");
    //
    private final NamespacedKey key;

    private GameEvent(NamespacedKey key) {
        this.key = key;

        GAME_EVENTS.put(key, this);
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Returns a {@link GameEvent} by a {@link NamespacedKey}.
     *
     * @param namespacedKey the key
     * @return the event or null
     */
    @Nullable
    public static GameEvent getByKey(@NotNull NamespacedKey namespacedKey) {
        return GAME_EVENTS.get(namespacedKey);
    }

    /**
     * Returns the set of all GameEvents.
     *
     * @return the memoryKeys
     */
    @NotNull
    public static Collection<GameEvent> values() {
        return Collections.unmodifiableCollection(GAME_EVENTS.values());
    }

    private static GameEvent getEvent(String vanilla) {
        return new GameEvent(NamespacedKey.minecraft(vanilla));
    }
}
