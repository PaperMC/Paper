package org.bukkit.event.block;

import com.google.common.base.Preconditions;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

public class CauldronLevelChangeEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    //
    private final Entity entity;
    private final ChangeReason reason;
    private final int oldLevel;
    private int newLevel;

    public CauldronLevelChangeEvent(Block block, Entity entity, ChangeReason reason, int oldLevel, int newLevel) {
        super(block);
        this.entity = entity;
        this.reason = reason;
        this.oldLevel = oldLevel;
        this.newLevel = newLevel;
    }

    /**
     * Get entity which did this. May be null.
     *
     * @return acting entity
     */
    public Entity getEntity() {
        return entity;
    }

    public ChangeReason getReason() {
        return reason;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(int newLevel) {
        Preconditions.checkArgument(0 <= newLevel && newLevel <= 3, "Cauldron level out of bounds 0 <= %s <= 3", newLevel);
        this.newLevel = newLevel;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public enum ChangeReason {
        /**
         * Player emptying the cauldron by filling their bucket.
         */
        BUCKET_FILL,
        /**
         * Player filling the cauldron by emptying their bucket.
         */
        BUCKET_EMPTY,
        /**
         * Player emptying the cauldron by filling their bottle.
         */
        BOTTLE_FILL,
        /**
         * Player filling the cauldron by emptying their bottle.
         */
        BOTTLE_EMPTY,
        /**
         * Player cleaning their banner.
         */
        BANNER_WASH,
        /**
         * Player cleaning their armor.
         */
        ARMOR_WASH,
        /**
         * Entity being extinguished.
         */
        EXTINGUISH,
        /**
         * Evaporating due to biome dryness.
         */
        EVAPORATE,
        /**
         * Unknown.
         */
        UNKNOWN
    }
}
