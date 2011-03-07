package org.bukkit.event.player;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;


/**
 *
 * @author tkelly
 *
 */
public class PlayerEggThrowEvent extends PlayerEvent {
    private Egg egg;
    private boolean hatching;
    private CreatureType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(Type type, Player player, Egg egg, boolean hatching, byte numHatches, CreatureType hatchType) {
        super(type, player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchType;
    }

    /**
     * Get the egg.
     *
     * @return the egg
     */
    public Egg getEgg() {
        return egg;
    }

    /**
     * Grabs whether the egg is hatching or not. Will be what the server
     * would've done without interaction.
     *
     * @return boolean Whether the egg is going to hatch or not
     */
    public boolean isHatching() {
        return hatching;
    }

    /**
     * Sets whether the egg will hatch.
     *
     * @param hatching true if you want the egg to hatch
     *                 false if you want it not to
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
     *
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
     *
     * The boolean hatching will override this number.
     * Ie. If hatching = false, this number will not matter
     *
     * @param numHatches The number of mobs coming out of the egg
     */
    public void setNumHatches(byte numHatches) {
        this.numHatches = numHatches;
    }
}
