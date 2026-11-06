package io.papermc.paper.entity;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Represents a flag that can be set on teleportation that may
 * slightly modify the behavior.
 *
 * @see Relative
 */
public sealed interface TeleportFlag permits TeleportFlag.EntityState, TeleportFlag.Relative {

    /**
     * Relative flags enable an entity to keep part of its current state when teleporting.
     * <ul>
     *  <li>{@code VELOCITY_*} flags prevent the entity from losing its velocity in an axis.</li>
     *  <li>{@code POSITION_*} flags make the teleport relative to the entity's server-side position.
     *      The provided location is still absolute, the server sends the offset to it.
     *      For players, the client applies the offset to its own position; this allows for smoother teleportation at
     *      the cost of possibly offsetting the player slightly from the target location due to latency.</li>
     * </ul>
     * @see Player#teleport(Location, PlayerTeleportEvent.TeleportCause, TeleportFlag...)
     * @see Entity#teleport(Location, PlayerTeleportEvent.TeleportCause, TeleportFlag...)
     */
    enum Relative implements TeleportFlag {
        /**
         * Configures the entity to not lose velocity in its x axis during the teleport.
         */
        VELOCITY_X,
        /**
         * Configures the entity to not lose velocity in its y axis during the teleport.
         */
        VELOCITY_Y,
        /**
         * Configures the entity to not lose velocity in its z axis during the teleport.
         */
        VELOCITY_Z,
        /**
         * Configures the entity's velocity to be rotated along with the change in its rotation during the teleport.
         * <p>
         * This only affects the axes the velocity is kept in.
         */
        VELOCITY_ROTATION,
        /**
         * Configures the teleport to be relative to the current x position of the entity.
         * <p>
         * The provided location is still absolute, the server sends the offset to it from the entity's position on the
         * server.
         */
        POSITION_X,
        /**
         * Configures the teleport to be relative to the current y position of the entity.
         * <p>
         * The provided location is still absolute, the server sends the offset to it from the entity's position on the
         * server.
         */
        POSITION_Y,
        /**
         * Configures the teleport to be relative to the current z position of the entity.
         * <p>
         * The provided location is still absolute, the server sends the offset to it from the entity's position on the
         * server.
         */
        POSITION_Z;
        /**
         * Configures the entity to not lose velocity in its x axis during the teleport.
         *
         * @deprecated This flag only affects velocity. Use {@link #VELOCITY_X} instead, and additionally
         * {@link #POSITION_X} to also make the position relative, as this flag did before 1.21.3.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative X = VELOCITY_X;
        /**
         * Configures the entity to not lose velocity in its y axis during the teleport.
         *
         * @deprecated This flag only affects velocity. Use {@link #VELOCITY_Y} instead, and additionally
         * {@link #POSITION_Y} to also make the position relative, as this flag did before 1.21.3.
         */
        @Deprecated(since = "1.21.3", forRemoval = true)
        public static final Relative Y = VELOCITY_Y;
        /**
         * Configures the entity to not lose velocity in its z axis during the teleport.
         *
         * @deprecated This flag only affects velocity. Use {@link #VELOCITY_Z} instead, and additionally
         * {@link #POSITION_Z} to also make the position relative, as this flag did before 1.21.3.
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
     *
     * @deprecated As of 1.21.10, the default behavior for teleportation is now aligned with vanilla
     * behavior. This means all of these flags are functionally done by default.
     */
    @Deprecated(since = "1.21.10", forRemoval = true)
    enum EntityState implements TeleportFlag {
        /**
         * If all passengers should not be required to be removed prior to teleportation.
         * <p>
         * Note:
         * Teleporting to a different world with this flag present while the entity has entities riding it
         * will cause this teleportation to return false and not occur.
         *
         * @deprecated This is now default behavior in teleportation. If you want to dismount all passengers,
         * remove them with {@link Entity#removePassenger(Entity)}.
         */
        @Deprecated(since = "1.21.10", forRemoval = true)
        RETAIN_PASSENGERS,
        /**
         * If the entity should not be dismounted if they are riding another entity.
         * <p>
         * Note:
         * Teleporting to a different world with this flag present while this entity is riding another entity will
         * cause this teleportation to return false and not occur.
         *
         * @deprecated This behavior was highly technical and is not replicatable due to client limitations,
         * and has not functioned for many updates.
         */
        @Deprecated(since = "1.21.10", forRemoval = true)
        RETAIN_VEHICLE,
        /**
         * Indicates that a player should not have their current open inventory closed when teleporting.
         * <p>
         * Note:
         * This option will be ignored when teleported to a different world.
         *
         * @deprecated No longer done on teleportation, use {@link Player#closeInventory()} to do this yourself.
         */
        @Deprecated(since = "1.21.10", forRemoval = true)
        RETAIN_OPEN_INVENTORY;
    }

}
