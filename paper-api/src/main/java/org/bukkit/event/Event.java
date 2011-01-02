
package org.bukkit.event;

/**
 * Represents an event
 */
public abstract class Event {
    private final Type type;
    private final String name;

    protected Event(final Type type) {
        exAssert(type != null, "type is null");
        exAssert(type != Type.CUSTOM_EVENT, "use Event(String) to make custom events");
        this.type = type;
        this.name = null;
    }
    
    protected Event(final String name) {
        exAssert(name != null, "name is null");
        this.type = Type.CUSTOM_EVENT;
        this.name = name;
    }
    
    /**
     * Gets the Type of this event
     * @return Event type that this object represents
     */
    public final Type getType() {
        return type;
    }
    
    private void exAssert(boolean b, String s) {
        if(!b) throw new IllegalArgumentException(s);
    }
    
    /**
     * Gets the event's name. Should only be used if getType returns null.
     * @return 
     */
    public final String getEventName() {
        if(type!=Type.CUSTOM_EVENT) return type.toString();
        else return name;
    }

    /**
     * Represents an events priority
     */
    public enum Priority {
        /**
         * Event call is of very low importance and should be ran first, to allow
         * other plugins to further customise the outcome
         */
        Lowest,

        /**
         * Event call is of low importance
         */
        Low,

        /**
         * Event call is neither important or unimportant, and may be ran normally
         */
        Normal,

        /**
         * Event call is of high importance
         */
        High,

        /**
         * Event call is critical and must have the final say in what happens
         * to the event
         */
        Highest,

        /**
         * Event is listened to purely for monitoring the outcome of an event.
         *
         * No modifications to the event should be made under this priority
         */
        Monitor
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
        BLOCK_BROKEN (Category.BLOCK),
        BLOCK_CANBUILD (Category.BLOCK),
        BLOCK_FLOW (Category.BLOCK),
        BLOCK_IGNITE (Category.BLOCK),
        BLOCK_PHYSICS (Category.BLOCK),
        BLOCK_PLACED (Category.BLOCK),
        BLOCK_RIGHTCLICKED (Category.BLOCK),
        REDSTONE_CHANGE (Category.BLOCK),


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
        SIGN_CHANGE (Category.SIGN)
         */
        
        CUSTOM_EVENT (Category.CUSTOM);

        private final Category category;
        
        private Type(Category category) {
            this.category = category;
        }

        public Category getCategory() {
            return category;
        }
    }
}
