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
    public static final GameEvent BLOCK_ATTACH = getEvent("block_attach");
    public static final GameEvent BLOCK_CHANGE = getEvent("block_change");
    public static final GameEvent BLOCK_CLOSE = getEvent("block_close");
    public static final GameEvent BLOCK_DESTROY = getEvent("block_destroy");
    public static final GameEvent BLOCK_DETACH = getEvent("block_detach");
    public static final GameEvent BLOCK_OPEN = getEvent("block_open");
    public static final GameEvent BLOCK_PLACE = getEvent("block_place");
    public static final GameEvent BLOCK_PRESS = getEvent("block_press");
    public static final GameEvent BLOCK_SWITCH = getEvent("block_switch");
    public static final GameEvent BLOCK_UNPRESS = getEvent("block_unpress");
    public static final GameEvent BLOCK_UNSWITCH = getEvent("block_unswitch");
    public static final GameEvent CONTAINER_CLOSE = getEvent("container_close");
    public static final GameEvent CONTAINER_OPEN = getEvent("container_open");
    public static final GameEvent DISPENSE_FAIL = getEvent("dispense_fail");
    public static final GameEvent DRINKING_FINISH = getEvent("drinking_finish");
    public static final GameEvent EAT = getEvent("eat");
    public static final GameEvent ELYTRA_FREE_FALL = getEvent("elytra_free_fall");
    public static final GameEvent ENTITY_DAMAGED = getEvent("entity_damaged");
    public static final GameEvent ENTITY_KILLED = getEvent("entity_killed");
    public static final GameEvent ENTITY_PLACE = getEvent("entity_place");
    public static final GameEvent EQUIP = getEvent("equip");
    public static final GameEvent EXPLODE = getEvent("explode");
    public static final GameEvent FISHING_ROD_CAST = getEvent("fishing_rod_cast");
    public static final GameEvent FISHING_ROD_REEL_IN = getEvent("fishing_rod_reel_in");
    public static final GameEvent FLAP = getEvent("flap");
    public static final GameEvent FLUID_PICKUP = getEvent("fluid_pickup");
    public static final GameEvent FLUID_PLACE = getEvent("fluid_place");
    public static final GameEvent HIT_GROUND = getEvent("hit_ground");
    public static final GameEvent LIGHTNING_STRIKE = getEvent("lightning_strike");
    public static final GameEvent MINECART_MOVING = getEvent("minecart_moving");
    public static final GameEvent MOB_INTERACT = getEvent("mob_interact");
    public static final GameEvent PISTON_CONTRACT = getEvent("piston_contract");
    public static final GameEvent PISTON_EXTEND = getEvent("piston_extend");
    public static final GameEvent PRIME_FUSE = getEvent("prime_fuse");
    public static final GameEvent PROJECTILE_LAND = getEvent("projectile_land");
    public static final GameEvent PROJECTILE_SHOOT = getEvent("projectile_shoot");
    public static final GameEvent RAVAGER_ROAR = getEvent("ravager_roar");
    public static final GameEvent RING_BELL = getEvent("ring_bell");
    public static final GameEvent SHEAR = getEvent("shear");
    public static final GameEvent SHULKER_CLOSE = getEvent("shulker_close");
    public static final GameEvent SHULKER_OPEN = getEvent("shulker_open");
    public static final GameEvent SPLASH = getEvent("splash");
    public static final GameEvent STEP = getEvent("step");
    public static final GameEvent SWIM = getEvent("swim");
    public static final GameEvent WOLF_SHAKING = getEvent("wolf_shaking");
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
