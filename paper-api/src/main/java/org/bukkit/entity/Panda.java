package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Panda entity.
 */
public interface Panda extends Animals, Sittable {

    /**
     * Gets this Panda's main gene.
     *
     * @return main gene
     */
    @NotNull
    Gene getMainGene();

    /**
     * Sets this Panda's main gene.
     *
     * @param gene main gene
     */
    void setMainGene(@NotNull Gene gene);

    /**
     * Gets this Panda's hidden gene.
     *
     * @return hidden gene
     */
    @NotNull
    Gene getHiddenGene();

    /**
     * Sets this Panda's hidden gene.
     *
     * @param gene hidden gene
     */
    void setHiddenGene(@NotNull Gene gene);

    /**
     * Gets whether the Panda is rolling
     *
     * @return Whether the Panda is rolling
     */
    boolean isRolling();

    /**
     * Sets whether the Panda is rolling
     *
     * @param flag Whether the Panda is rolling
     */
    void setRolling(boolean flag);

    /**
     * Gets whether the Panda is sneezing
     *
     * @return Whether the Panda is sneezing
     */
    boolean isSneezing();

    /**
     * Sets whether the Panda is sneezing
     *
     * @param flag Whether the Panda is sneezing
     */
    void setSneezing(boolean flag);

    /**
     * Gets whether the Panda is on its back
     *
     * @return Whether the Panda is on its back
     */
    boolean isOnBack();

    /**
     * Sets whether the Panda is on its back
     *
     * @param flag Whether the Panda is on its back
     */
    void setOnBack(boolean flag);

    /**
     * Gets whether the Panda is eating
     *
     * @return Whether the Panda is eating
     */
    boolean isEating();

    /**
     * Sets the Panda's eating status. The panda must be holding food for this to work
     *
     * @param flag Whether the Panda is eating
     */
    void setEating(boolean flag);

    /**
     * Gets whether the Panda is scared
     *
     * @return Whether the Panda is scared
     */
    boolean isScared();

    /**
     * Gets how many ticks the panda will be unhappy for
     *
     * @return The number of ticks the panda will be unhappy for
     */
    int getUnhappyTicks();

    public enum Gene {

        NORMAL(false),
        LAZY(false),
        WORRIED(false),
        PLAYFUL(false),
        BROWN(true),
        WEAK(true),
        AGGRESSIVE(false);

        private final boolean recessive;

        private Gene(boolean recessive) {
            this.recessive = recessive;
        }

        /**
         * Gets whether this gene is recessive, i.e. required in both parents to
         * propagate to children.
         *
         * @return recessive status
         */
        public boolean isRecessive() {
            return recessive;
        }
    }
}
