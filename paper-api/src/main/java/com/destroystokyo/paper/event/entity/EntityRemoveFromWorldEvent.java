package com.destroystokyo.paper.event.entity;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Fired any time an entity is being removed from a world for any reason (including a chunk unloading).
 * Note: The entity is updated prior to this event being called, as such, the entity's world may not be equal to {@link #getWorld()}.
 */
@NullMarked
public class EntityRemoveFromWorldEvent extends EntityEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final World world;
    private final Cause cause;

    @ApiStatus.Internal
    public EntityRemoveFromWorldEvent(final Entity entity, final World world, final Cause cause) {
        super(entity);

        this.world = world;
        this.cause = cause;
    }

    /**
     * @return The world that the entity is being removed from
     */
    public World getWorld() {
        return this.world;
    }

    /**
     * Gets the reason the entity is being removed from the world.
     * @return the removal reason
     */
    public Cause getCause() {
        return cause;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public enum Cause {

        /**
         * When an entity dies.
         */
        DEATH(true, false),
        /**
         * When an entity does despawn. This includes mobs which are too far away,
         * items or arrows which lay to long on the ground or area effect cloud.
         */
        DESPAWN(true, false),
        /**
         * When an entity gets removed because it drops as an item.
         * For example, trident or falling sand.
         * <p>
         * <b>Note:</b> Depending on other factors, such as gamerules, no item will actually drop,
         * the cause, however, will still be drop.
         */
        DROP(true, false),
        /**
         * When an entity gets removed because it enters a block.
         * For example, bees or silverfish.
         */
        ENTER_BLOCK(true, false),
        /**
         * When an entity gets removed because it exploded.
         * For example, creepers, tnt or firework.
         */
        EXPLODE(true, false),
        /**
         * When an entity gets removed because it hit something. This mainly applies to projectiles.
         */
        HIT(true, false),
        /**
         * When an entity gets removed because it merges with another one.
         * For example, items or xp.
         */
        MERGE(true, false),
        /**
         * When an entity gets removed because it is too far below the world.
         * This only applies to entities which get removed immediately,
         * some entities get damage instead.
         */
        OUT_OF_WORLD(true, false),
        /**
         * When an entity gets removed because it got pickup.
         * For example, items, arrows, xp or parrots which get on a player shoulder.
         */
        PICKUP(true, false),
        /**
         * When an entity gets removed with a player because the player quits the game.
         * For example, a boat which gets removed with the player when he quits.
         */
        PLAYER_QUIT(false, false),
        /**
         * When a plugin manually removes an entity.
         */
        PLUGIN(true, false),
        /**
         * When an entity gets removed because it transforms into another one.
         */
        TRANSFORMATION(true, false),
        /**
         * When the chunk an entity is in gets unloaded.
         */
        UNLOAD(false, true),
        /**
         * When an entity is discarded, and a more specific cause does not exist.
         */
        DISCARD(true, false),
        /**
         * When an entity changes dimensions.
         */
        CHANGED_DIMENSION(false, false),
        /**
         * When the chunk an entity is in is no longer accessible, but not yet fully unloaded.
         */
        INACCESSIBLE(false, false),
        /**
         * Used when the cause of the removal is unknown.
         */
        UNKNOWN(false, false);

        private final boolean destroy;
        private final boolean save;

        Cause(boolean destroy, boolean save) {
            this.destroy = destroy;
            this.save = save;
        }

        /**
         * Whether the entity instance being removed will be destroyed.
         *
         * @return whether the entity will be destroyed
         */
        public boolean willDestroy() {
            return this.destroy;
        }

        /**
         * Whether the entity instance being removed will be saved. This does not account for the value of
         * {@link Entity#isPersistent}. Entities removed with {@link Cause#PLAYER_QUIT} are saved
         * prior to the event firing, and thus should be modified prior to this using another event, such as
         * with {@link PlayerQuitEvent}.
         *
         * @return whether the entity will be saved
         */
        public boolean willSave() {
            return this.save;
        }

    }

}
