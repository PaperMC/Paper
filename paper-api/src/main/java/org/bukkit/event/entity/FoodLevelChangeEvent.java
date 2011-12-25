package org.bukkit.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;

/**
 * Called when a human entity's food level changes
 */
@SuppressWarnings("serial")
public class FoodLevelChangeEvent extends EntityEvent implements Cancellable {
    private boolean cancel = false;
    private int level;

    public FoodLevelChangeEvent(Entity what, int level) {
        super(Type.FOOD_LEVEL_CHANGE, what);
        this.level = level;
    }

    /**
     * Gets the resultant food level that the entity involved in this event should be set to.
     * <br /><br />
     * Where 20 is a full food bar and 0 is an empty one.
     *
     * @return The resultant food level
     */
    public int getFoodLevel() {
        return level;
    }

    /**
     * Sets the resultant food level that the entity involved in this event should be set to
     *
     * @param level the resultant food level that the entity involved in this event should be set to
     */
    public void setFoodLevel(int level) {
        if (level > 20) level = 20;
        else if (level < 0) level = 0;

        this.level = level;
    }

    public boolean isCancelled() {
        return cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
