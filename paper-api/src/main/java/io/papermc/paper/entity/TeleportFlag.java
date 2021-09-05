package io.papermc.paper.entity;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Represents a flag that can be set on teleportation that may
 * slightly modify the behavior.
 *
 * @see EntityState
 * @see Relative
 */
public sealed interface TeleportFlag permits TeleportFlag.EntityState, TeleportFlag.Relative {

    /**
     * Note: These flags only work on {@link org.bukkit.entity.Player} entities.
     * <p>
     * Relative flags enable a player to not lose their velocity in the flag-specific axis/context when teleporting.
     *
     * @apiNote The relative flags exposed in the API do *not* mirror all flags known to vanilla, as relative flags concerning
     * the position are non-applicable given teleports always expect an absolute location.
     * @see org.bukkit.entity.Player#teleport(Location, PlayerTeleportEvent.TeleportCause, TeleportFlag...)
     */
    enum Relative implements TeleportFlag {
        /**
         * Configures the player to not lose velocity in their x axis during the teleport.
         */
        VELOCITY_X,
        /**
         * Configures the player to not lose velocity in their y axis during the teleport.
         */
        VELOCITY_Y,
        /**
         * Configures the player to not lose velocity in their z axis during the teleport.
         */
        VELOCITY_Z,
        /**
         * Configures the player to not lose velocity in their current rotation during the teleport.
         */
        VELOCITY_ROTATION;
        /**
         * Configures the player to not loose velocity in their x axis during the teleport.
         * @deprecated Since 1.21.3, vanilla split up the relative teleport flags into velocity and position related
         * ones. As the API does not deal with position relative flags, this name is no longer applicable.
         * Use {@link #VELOCITY_X} instead.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative X = VELOCITY_X;
        /**
         * Configures the player to not loose velocity in their y axis during the teleport.
         * @deprecated Since 1.21.3, vanilla split up the relative teleport flags into velocity and position related
         * ones. As the API does not deal with position relative flags, this name is no longer applicable.
         * Use {@link #VELOCITY_Y} instead.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative Y = VELOCITY_Y;
        /**
         * Configures the player to not loose velocity in their z axis during the teleport.
         * @deprecated Since 1.21.3, vanilla split up the relative teleport flags into velocity and position related
         * ones. As the API does not deal with position relative flags, this name is no longer applicable.
         * Use {@link #VELOCITY_Z} instead.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative Z = VELOCITY_Z;
        /**
         * Represents the player's yaw
         *
         * @deprecated relative velocity flags now allow for the whole rotation to be relative, instead of the yaw and
         * pitch having individual options. Use {@link #VELOCITY_ROTATION} instead.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative YAW = VELOCITY_ROTATION;
        /**
         * Represents the player's pitch
         *
         * @deprecated relative velocity flags now allow for the whole rotation to be relative, instead of the yaw and
         * pitch having individual options. Use {@link #VELOCITY_ROTATION} instead.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative PITCH = VELOCITY_ROTATION;
    }

    /**
     * Represents flags that effect the entity's state on
     * teleportation.
     */
    enum EntityState implements TeleportFlag {
        /**
         * If all passengers should not be required to be removed prior to teleportation.
         * <p>
         * Note:
         * Teleporting to a different world with this flag present while the entity has entities riding it
         * will cause this teleportation to return false and not occur.
         */
        RETAIN_PASSENGERS,
        /**
         * If the entity should not be dismounted if they are riding another entity.
         * <p>
         * Note:
         * Teleporting to a different world with this flag present while this entity is riding another entity will
         * cause this teleportation to return false and not occur.
         */
        RETAIN_VEHICLE,
        /**
         * Indicates that a player should not have their current open inventory closed when teleporting.
         * <p>
         * Note:
         * This option will be ignored when teleported to a different world.
         */
        RETAIN_OPEN_INVENTORY;
    }

}
