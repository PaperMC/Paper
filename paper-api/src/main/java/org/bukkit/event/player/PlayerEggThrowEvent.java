package org.bukkit.event.player;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.HandlerList;

/**
 * Called when a player throws an egg and it might hatch
 */
public interface PlayerEggThrowEvent extends PlayerEvent {

    /**
     * Gets the egg involved in this event.
     *
     * @return the egg involved in this event
     */
    Egg getEgg();

    /**
     * Gets whether the egg is hatching or not. Will be what the server
     * would've done without interaction.
     *
     * @return boolean Whether the egg is going to hatch or not
     */
    boolean isHatching();

    /**
     * Sets whether the egg will hatch or not.
     *
     * @param hatching {@code true} if you want the egg to hatch, {@code false} if you want it
     *     not to
     */
    void setHatching(boolean hatching);

    /**
     * Get the type of the mob being hatched (EntityType.CHICKEN by default)
     *
     * @return The type of the mob being hatched by the egg
     */
    EntityType getHatchingType();

    /**
     * Change the type of mob being hatched by the egg
     *
     * @param hatchType The type of the mob being hatched by the egg
     */
    void setHatchingType(EntityType hatchType);

    /**
     * Get the number of mob hatches from the egg. By default the number will
     * be the number the server would've done
     * <ul>
     * <li>7/8 chance of being 0
     * <li>31/256 ~= 1/8 chance to be 1
     * <li>1/256 chance to be 4
     * </ul>
     *
     * @return The number of mobs going to be hatched by the egg
     */
    byte getNumHatches();

    /**
     * Change the number of mobs coming out of the hatched egg
     * <p>
     * The boolean hatching will override this number. I.e. If hatching =
     * false, this number will not matter
     *
     * @param numHatches The number of mobs coming out of the egg
     */
    void setNumHatches(byte numHatches);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
