package org.bukkit.event.player;

import org.bukkit.entity.MobType;
import org.bukkit.entity.Player;


/**
 *
 * @author tkelly
 *
 */
public class PlayerEggThrowEvent extends PlayerEvent {
    private boolean hatching;
    private MobType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(Type type, Player player, boolean hatching, byte numHatches, MobType hatchType) {
        super(type, player);
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchType;
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
     * Get the type of the mob being hatched (MobType.CHICKEN by default)
     *
     * @return The type of the mob being hatched by the egg
     */
    public MobType getHatchType() {
        return hatchType;
    }

    /**
     * Change the type of mob being hatched by the egg
     *
     * @param hatchType The type of the mob being hatched by the egg
     */
    public void setHatchType(MobType hatchType) {
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
