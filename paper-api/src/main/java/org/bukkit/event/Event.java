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
     * Gets the event's name. Should only be used if getType() == Type.CUSTOM
     *
     * @return Name of this event
     */
    public final String getEventName() {
        if(type!=Type.CUSTOM_EVENT) return type.toString();
        else return name;
    }

    /**
     * Represents an events priority in execution
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

    /**
     * Represents a category used by Type
     */
    public enum Category {
        /**
         * Represents Player-based events
         * @see Category.LIVING_ENTITY
         */
        PLAYER,

        /**
         * Represents Block-based events
         */
        BLOCK,

        /**
         * Represents LivingEntity-based events
         */
        LIVING_ENTITY,

        /**
         * Represents Vehicle-based events
         */
        VEHICLE,

        /**
         * Represents World-based events
         */
        WORLD,

        /**
         * Represents Server and Plugin based events
         */
        SERVER,

        /**
         * Represents Inventory-based events
         */
        INVENTORY,

        /**
         * Any miscellaneous events
         */
        MISCELLANEOUS;
    }

    /**
     * Provides a lookup for all core events
     */
    public enum Type {
        /**
         * PLAYER EVENTS
         */

        /** 
         * Called when a player joins a server
         */
        PLAYER_JOIN (Category.PLAYER),

        /**
         * Called when a player is attempting to join a server
         */
        PLAYER_LOGIN (Category.PLAYER),

        /**
         * Called when a player sends a chat message
         */
        PLAYER_CHAT (Category.PLAYER),

        /**
         * Called when a player attempts to use a command
         */
        PLAYER_COMMAND (Category.PLAYER),

        /**
         * Called when a player leaves a server
         */
        PLAYER_QUIT (Category.PLAYER),

        /**
         * Called when a player moves position in the world
         */
        PLAYER_MOVE (Category.PLAYER),

        /**
         * Called when a player undergoes an animation, such as arm swinging
         */
        PLAYER_ANIMATION (Category.PLAYER),

        /**
         * Called when a player teleports from one position to another
         */
        PLAYER_TELEPORT (Category.PLAYER),

        /**
         * BLOCK EVENTS
         */

        /** 
         * Called when a block is damaged (hit by a player)
         */
        BLOCK_DAMAGED (Category.BLOCK),

        /**
         * Called when a block is undergoing a check on whether it can be built
         *
         * For example, cacti cannot be built on grass unless overridden here
         */
        BLOCK_CANBUILD (Category.BLOCK),

        /**
         * Called when a block of water or lava attempts to flow into another
         * block
         */
        BLOCK_FLOW (Category.BLOCK),

        /**
         * Called when a block is being set on fire from another block, such as
         * an adjacent block of fire attempting to set fire to wood
         */
        BLOCK_IGNITE (Category.BLOCK),

        /**
         * Called when a block undergoes a physics check
         *
         * A physics check is commonly called when an adjacent block changes
         * type
         */
        BLOCK_PHYSICS (Category.BLOCK),

        /**
         * Called when a player is attempting to place a block
         */
        BLOCK_PLACED (Category.BLOCK),

        /**
         * Called when a specific block is being sent to a player
         */
        BLOCK_SENT (Category.BLOCK),

        /**
         * Called when a liquid attempts to flow into a block which already
         * contains a "breakable" block, such as redstone wire
         */
        LIQUID_DESTROY (Category.BLOCK),

        /**
         * Called when a block changes redstone current. Only triggered on blocks
         * that are actually capable of transmitting or carrying a redstone
         * current
         */
        REDSTONE_CHANGE (Category.BLOCK),

        /**
         * INVENTORY EVENTS
         */

        /** 
         * Called when a player opens an inventory
         */
        INVENTORY_OPEN (Category.INVENTORY),

        /**
         * Called when a player closes an inventory
         */
        INVENTORY_CLOSE (Category.INVENTORY),

        /**
         * Called when a player clicks on an inventory slot
         */
        INVENTORY_CLICK (Category.INVENTORY),

        /**
         * Called when an inventory slot changes values or type
         */
        INVENTORY_CHANGE (Category.INVENTORY),

        /**
         * Called when a player is attempting to perform an inventory transaction
         */
        INVENTORY_TRANSACTION (Category.INVENTORY),

        /**
         * SERVER EVENTS
         */

        /**
         * Called when a plugin is enabled
         */
        PLUGIN_ENABLE (Category.SERVER),

        /**
         * Called when a plugin is disabled
         */
        PLUGIN_DISABLE (Category.SERVER),

        /**
         * WORLD EVENTS
         */

        /**
         * Called when a chunk is loaded
         *
         * If a new chunk is being generated for loading, it will call
         * Type.CHUNK_GENERATION and then Type.CHUNK_LOADED upon completion
         */
        CHUNK_LOADED (Category.WORLD),

        /**
         * Called when a chunk is unloaded
         */
        CHUNK_UNLOADED (Category.WORLD),

        /**
         * Called when a chunk needs to be generated
         */
        CHUNK_GENERATION (Category.WORLD),

        /**
         * Called when an ItemEntity spawns in the world
         */
        ITEM_SPAWN (Category.WORLD),

        /**
         * LIVING_ENTITY EVENTS
         */

        /** 
         * Called when a creature, either hostile or neutral, attempts to spawn
         * in the world "naturally"
         */
        CREATURE_SPAWN (Category.LIVING_ENTITY),

        /**
         * Called when a LivingEntity is damaged by the environment (for example,
         * falling or lava)
         */
        ENTITY_DAMAGEDBY_BLOCK (Category.LIVING_ENTITY),

        /**
         * Called when a LivingEntity is damaged by another LivingEntity
         */
        ENTITY_DAMAGEDBY_ENTITY (Category.LIVING_ENTITY),

        /**
         * Called when a LivingEntity dies
         */
        ENTITY_DEATH (Category.LIVING_ENTITY),

        /**
         * VEHICLE EVENTS
         */

        /** 
         * Called when a vehicle is placed by a player
         */
        VEHICLE_CREATE (Category.VEHICLE),

        /**
         * Called when a vehicle is damaged by a LivingEntity
         */
        VEHICLE_DAMAGE (Category.VEHICLE),

        /**
         * Called when a vehicle collides with an Entity
         */
        VEHICLE_COLLISION_ENTITY (Category.VEHICLE),

        /**
         * Called when a vehicle collides with a Block
         */
        VEHICLE_COLLISION_BLOCK (Category.VEHICLE),

        /**
         * Called when a vehicle is entered by a LivingEntity
         */
        VEHICLE_ENTER (Category.VEHICLE),

        /**
         * Called when a vehicle is exited by a LivingEntity
         */
        VEHICLE_EXIT (Category.VEHICLE),

        /**
         * Called when a vehicle moves position in the world
         */
        VEHICLE_MOVE (Category.VEHICLE),

        /**
         * MISCELLANEOUS EVENTS
         */

        /**
         * Represents a custom event, isn't actually used
         */
        CUSTOM_EVENT (Category.MISCELLANEOUS);

        private final Category category;
        
        private Type(Category category) {
            this.category = category;
        }

        /**
         * Gets the Category assigned to this event
         *
         * @return Category of this Event.Type
         */
        public Category getCategory() {
            return category;
        }
    }
}
