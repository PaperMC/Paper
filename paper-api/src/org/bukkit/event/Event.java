
package org.bukkit.event;

/**
 * Represents an event
 */
public abstract class Event {
    private final Type type;

    protected Event(final Type type) {
        this.type = type;
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
        PLAYER_JOIN (Category.PLAYER),
        PLAYER_LOGIN (Category.PLAYER),
        PLAYER_CHAT (Category.PLAYER),
        PLAYER_COMMAND (Category.PLAYER),
        PLAYER_QUIT (Category.PLAYER),
        PLAYER_MOVE (Category.PLAYER),
        //PLAYER_ANIMATION (Category.PLAYER),
        PLAYER_TELEPORT (Category.PLAYER),
        /** 
    	 * Block Events
    	 */
        BLOCK_DAMAGED (Category.BLOCK),
        BLOCK_FLOW (Category.BLOCK),
        BLOCK_IGNITE (Category.BLOCK),
        BLOCK_PHYSICS (Category.BLOCK),
        BLOCK_PLACED (Category.BLOCK),
        BLOCK_RIGHTCLICKED (Category.BLOCK),
        REDSTONE_CHANGE (Category.BLOCK);
        
        
        /** 
    	 * Item Events
    	 
        ITEM_DROP (Category.ITEM),
        ITEM_PICK_UP (Category.ITEM),
        ITEM_USE (Category.ITEM),
        /** 
    	 * Environment Events
    	 
        IGNITE (Category.ENVIRONMENT),
        FLOW (Category.ENVIRONMENT),
        EXPLODE (Category.ENVIRONMENT),
        LIQUID_DESTROY (Category.ENVIRONMENT),
        /** 
    	 * Non-player Entity Events
    	 
        MOB_SPAWN (Category.ENTITY),
        DAMAGE (Category.ENTITY),
        HEALTH_CHANGE (Category.ENTITY),
        ATTACK (Category.ENTITY), // Need to look into this category more
        /** 
    	 * Vehicle Events
    	 
        VEHICLE_CREATE (Category.VEHICLE),
        VEHICLE_UPDATE (Category.VEHICLE),
        VEHICLE_DAMAGE (Category.VEHICLE),
        VEHICLE_COLLISION (Category.VEHICLE),
        VEHICLE_DESTROYED (Category.VEHICLE),
        VEHICLE_ENTERED (Category.VEHICLE),
        VEHICLE_POSITIONCHANGE (Category.VEHICLE),
        /** 
    	 * Inventory Events
    	 
        OPEN_INVENTORY (Category.INVENTORY),
        /** 
    	 * Sign Events (Item events??)
    	 
        SIGN_SHOW (Category.SIGN),
        SIGN_CHANGE (Category.SIGN);
         */
        
        private Category category;
        
        private Type(Category category) {
        	this.category = category;
        }
        
        public Category getCategory() {
        	return category;
        }
    }
}
