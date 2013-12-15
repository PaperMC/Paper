package org.bukkit.event.player;

import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 * Called when a player throws an egg and it might hatch
 */
public class PlayerEggThrowEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Egg egg;
    private boolean hatching;
    private EntityType hatchType;
    private byte numHatches;

    public PlayerEggThrowEvent(final Player player, final Egg egg, final boolean hatching, final byte numHatches, final EntityType hatchingType) {
        super(player);
        this.egg = egg;
        this.hatching = hatching;
        this.numHatches = numHatches;
        this.hatchType = hatchingType;
    }

    @Deprecated
    public PlayerEggThrowEvent(Player player, Egg egg, boolean hatching, byte numHatches, CreatureType hatchingType) {
        this(player, egg, hatching, numHatches, hatchingType.toEntityType());
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
     * @param hatching true if you want the egg to hatch, false if you want it
     *     not to
     */
    public void setHatching(boolean hatching) {
        this.hatching = hatching;
    }

    /**
     * Get the type of the mob being hatched (EntityType.CHICKEN by default)
     *
     * @return The type of the mob being hatched by the egg
     * @deprecated In favour of {@link #getHatchingType()}.
     */
    @Deprecated
    public CreatureType getHatchType() {
        return CreatureType.fromEntityType(hatchType);
    }

    /**
     * Get the type of the mob being hatched (EntityType.CHICKEN by default)
     *
     * @return The type of the mob being hatched by the egg
     */
    public EntityType getHatchingType() {
        return hatchType;
    }

    /**
     * Change the type of mob being hatched by the egg
     *
     * @param hatchType The type of the mob being hatched by the egg
     * @deprecated In favour of {@link #setHatchingType(EntityType)}.
     */
    @Deprecated
    public void setHatchType(CreatureType hatchType) {
        this.hatchType = hatchType.toEntityType();
    }

    /**
     * Change the type of mob being hatched by the egg
     *
     * @param hatchType The type of the mob being hatched by the egg
     */
    public void setHatchingType(EntityType hatchType) {
        if(!hatchType.isSpawnable()) throw new IllegalArgumentException("Can't spawn that entity type from an egg!");
        this.hatchType = hatchType;
    }

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
    public byte getNumHatches() {
        return numHatches;
    }

    /**
     * Change the number of mobs coming out of the hatched egg
     * <p>
     * The boolean hatching will override this number. Ie. If hatching =
     * false, this number will not matter
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
