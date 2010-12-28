
package org.bukkit.event;

import org.bukkit.Player;
import org.bukkit.Server;
import org.bukkit.event.player.PlayerEvent;

/**
 * Represents an event
 */
public abstract class Event {
    private final Server server;
    private final Type type;

    protected Event(final Server instance, final Type type) {
        server = instance;
        this.type = type;
    }

    /**
     * Gets the Server instance that triggered this event
     * @return Server which this event was triggered on
     */
    public final Server getServer() {
        return server;
    }

    /**
     * Gets the Type of this event
     * @return Server which this event was triggered on
     */
    public Type getType() {
    	return type;
    }

    /**
     * Represents an events priority
     */
    public enum Priority {
        /**
         * Event is critical and must be called near-first
         */
        Highest,

        /**
         * Event is of high importance
         */
        High,

        /**
         * Event is neither important or unimportant, and may be ran normally
         */
        Normal,

        /**
         * Event is of low importance
         */
        Low,

        /**
         * Event is of extremely low importance, most likely just to monitor events, and must be run near-last
         */
        Lowest
    }
    
    public enum Category {
    	PLAYER,
    	BLOCK,
    	ITEM,
    	ENVIRONMENT,
    	ENTITY,
    	VEHICLE,
    	INVENTORY,
    	SIGN,
    	CUSTOM;
    }
    
    public enum Type {
    	/** 
    	 * Player Events
    	 */
        LOGINCHECK (Category.PLAYER),
        JOIN (Category.PLAYER),
        CHAT (Category.PLAYER),
        COMMAND (Category.PLAYER),
        SERVERCOMMAND (Category.PLAYER),
        BAN (Category.PLAYER),
        IPBAN (Category.PLAYER),
        KICK (Category.PLAYER),
        QUIT (Category.PLAYER),
        PLAYER_MOVE (Category.PLAYER),
        ARM_SWING (Category.PLAYER),
        TELEPORT (Category.PLAYER),
        /** 
    	 * Block Events
    	 */
        BLOCK_DESTROYED (Category.BLOCK),
        BLOCK_BROKEN (Category.BLOCK),
        BLOCK_PLACE (Category.BLOCK),
        BLOCK_RIGHTCLICKED (Category.BLOCK),
        REDSTONE_CHANGE (Category.BLOCK),
        BLOCK_PHYSICS (Category.BLOCK),
        /** 
    	 * Item Events
    	 */
        ITEM_DROP (Category.ITEM),
        ITEM_PICK_UP (Category.ITEM),
        ITEM_USE (Category.ITEM),
        /** 
    	 * Environment Events
    	 */
        IGNITE (Category.ENVIRONMENT),
        FLOW (Category.ENVIRONMENT),
        EXPLODE (Category.ENVIRONMENT),
        LIQUID_DESTROY (Category.ENVIRONMENT),
        /** 
    	 * Non-player Entity Events
    	 */
        MOB_SPAWN (Category.ENTITY),
        DAMAGE (Category.ENTITY),
        HEALTH_CHANGE (Category.ENTITY),
        ATTACK (Category.ENTITY), // Need to look into this category more
        /** 
    	 * Vehicle Events
    	 */
        VEHICLE_CREATE (Category.VEHICLE),
        VEHICLE_UPDATE (Category.VEHICLE),
        VEHICLE_DAMAGE (Category.VEHICLE),
        VEHICLE_COLLISION (Category.VEHICLE),
        VEHICLE_DESTROYED (Category.VEHICLE),
        VEHICLE_ENTERED (Category.VEHICLE),
        VEHICLE_POSITIONCHANGE (Category.VEHICLE),
        /** 
    	 * Inventory Events
    	 */
        OPEN_INVENTORY (Category.INVENTORY),
        /** 
    	 * Sign Events (Item events??)
    	 */
        SIGN_SHOW (Category.SIGN),
        SIGN_CHANGE (Category.SIGN),
        /** 
    	 * Custom Event Placeholder?
    	 */
        CUSTOM_EVENT (Category.CUSTOM);
        
        private Category category;
        
        private Type(Category category) {
        	this.category = category;
        }
        
        public Category getCategory() {
        	return category;
        }
    }
}
