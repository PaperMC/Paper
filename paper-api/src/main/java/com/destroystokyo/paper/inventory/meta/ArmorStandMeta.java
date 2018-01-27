package com.destroystokyo.paper.inventory.meta;

import org.bukkit.inventory.meta.ItemMeta;

public interface ArmorStandMeta extends ItemMeta {

    /**
     * Gets whether the ArmorStand should be invisible when spawned
     *
     * @return true if this should be invisible
     */
    boolean isInvisible();

    /**
     * Gets whether this ArmorStand should have no base plate when spawned
     *
     * @return true if it will not have a base plate
     */
    boolean hasNoBasePlate();

    /**
     * Gets whether this ArmorStand should show arms when spawned
     *
     * @return true if it will show arms
     */
    boolean shouldShowArms();

    /**
     * Gets whether this ArmorStand will be small when spawned
     *
     * @return true if it will be small
     */
    boolean isSmall();

    /**
     * Gets whether this ArmorStand will be a marker when spawned
     * The exact details of this flag are an implementation detail
     *
     * @return true if it will be a marker
     */
    boolean isMarker();

    /**
     * Sets that this ArmorStand should be invisible when spawned
     *
     * @param invisible true if set invisible
     */
    void setInvisible(boolean invisible);

    /**
     * Sets that this ArmorStand should have no base plate when spawned
     *
     * @param noBasePlate true if no base plate
     */
    void setNoBasePlate(boolean noBasePlate);

    /**
     * Sets that this ArmorStand should show arms when spawned
     *
     * @param showArms true if show arms
     */
    void setShowArms(boolean showArms);

    /**
     * Sets that this ArmorStand should be small when spawned
     *
     * @param small true if small
     */
    void setSmall(boolean small);

    /**
     * Sets that this ArmorStand should be a marker when spawned
     * The exact details of this flag are an implementation detail
     *
     * @param marker true if a marker
     */
    void setMarker(boolean marker);
}
