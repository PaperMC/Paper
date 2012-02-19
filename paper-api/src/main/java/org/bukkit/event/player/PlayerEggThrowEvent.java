package org.bukkit.event.player;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player throws an egg and it might hatch
 */
public class PlayerEggThrowEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Egg egg;
    private boolean hatching;
    private CreatureType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(final Player player, final Egg egg, final boolean hatching, final byte numHatches, final CreatureType hatchType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchType;
    }

    /**
     * Gets the egg involved in this event.
     *
     * @return the egg involved in this event
     */
    public Egg getEgg() {
        return egg;
    }

    /**
     * Gets whether the egg is hatching or not. Will be what the server
     * would've done without interaction.
     *
     * @return boolean Whether the egg is going to hatch or not
     */
    public boolean isHatching() {
        return hatching;
    }

    /**
     * Sets whether the egg will hatch or not.
     *
     * @param hatching true if you want the egg to hatch
     *            false if you want it not to
     */
    public void setHatching(boolean hatching) {
        this.hatching = hatching;
    }

    /**
     * Get the type of the mob being hatched (CreatureType.CHICKEN by default)
     *
     * @return The type of the mob being hatched by the egg
     */
    public CreatureType getHatchType() {
        return CreatureType.fromName(hatchType.getName());
    }

    /**
     * Change the type of mob being hatched by the egg
     *
     * @param hatchType The type of the mob being hatched by the egg
     */
    public void setHatchType(CreatureType hatchType) {
        this.hatchType = hatchType;
    }

    /**
     * Get the number of mob hatches from the egg. By default the number
     * will be he number the server would've done
     * <p />
     * 7/8 chance of being 0
     * 31/256 ~= 1/8 chance to be 1
     * 1/256 chance to be 4
     *
     * @return The number of mobs going to be hatched by the egg
     */
    public byte getNumHatches() {
        return numHatches;
    }

    /**
     * Change the number of mobs coming out of the hatched egg
     * <p />
     * The boolean hatching will override this number.
     * Ie. If hatching = false, this number will not matter
     *
     * @param numHatches The number of mobs coming out of the egg
     */
    public void setNumHatches(byte numHatches) {
        this.numHatches = numHatches;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
