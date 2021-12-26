package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * An Axolotl.
 */
public interface Axolotl extends Animals, io.papermc.paper.entity.Bucketable { // Paper - Bucketable API

    /**
     * Gets if this axolotl is playing dead.
     *
     * An axolotl may play dead when it is damaged underwater.
     *
     * @return playing dead status
     */
    boolean isPlayingDead();

    /**
     * Sets if this axolotl is playing dead.
     *
     * An axolotl may play dead when it is damaged underwater.
     *
     * @param playingDead playing dead status
     */
    void setPlayingDead(boolean playingDead);

    /**
     * Get the variant of this axolotl.
     *
     * @return axolotl variant
     */
    @NotNull
    Variant getVariant();

    /**
     * Set the variant of this axolotl.
     *
     * @param variant axolotl variant
     */
    void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a axolotl - ie its color.
     */
    public enum Variant {

        /**
         * Leucistic (pink) axolotl.
         */
        LUCY,
        /**
         * Brown axolotl.
         */
        WILD,
        /**
         * Gold axolotl.
         */
        GOLD,
        /**
         * Cyan axolotl.
         */
        CYAN,
        /**
         * Blue axolotl.
         */
        BLUE;
    }
}
