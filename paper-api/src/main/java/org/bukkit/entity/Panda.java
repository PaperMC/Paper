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

    // Paper start - Panda API
    /**
     * Sets the sneeze progress in this animation.
     * This value counts up only if {@link Panda#isSneezing()} is true
     *
     * @param ticks sneeze progress
     */
    void setSneezeTicks(int ticks);

    /**
     * Gets the current sneeze progress, or how many ticks this panda will sneeze for.
     *
     * @return sneeze progress
     */
    int getSneezeTicks();

    /**
     * Sets the eating ticks for this panda.
     * <p>
     *
     * This starts counting up as long as it is greater than 0.
     *
     * @param ticks eating ticks
     */
    void setEatingTicks(int ticks);

    /**
     * Gets the current eating progress, or how many ticks this panda has been eating for.
     *
     * @return eating progress
     */
    int getEatingTicks();

    /**
     * Sets the number of ticks this panda will be unhappy for.
     * <p>
     * This value counts down.
     *
     * @param ticks unhappy ticks
     */
    void setUnhappyTicks(int ticks);

    /**
     * Sets if this panda is currently on its back.
     *
     * @param onBack is on its back
     * @deprecated use {@link #setOnBack(boolean)}
     */
    @Deprecated(forRemoval = true, since = "1.19")
    default void setIsOnBack(boolean onBack) {
        this.setOnBack(onBack);
    }

    /**
     * Sets if this panda is currently sitting.
     *
     * @param sitting is currently sitting
     * @deprecated use {@link #setSitting(boolean)}
     */
    @Deprecated(forRemoval = true, since = "1.19")
    default void setIsSitting(boolean sitting) {
        this.setSitting(sitting);
    }

    /**
     * Gets this Panda's combined gene.
     * <p>
     * The combined gene can be modified using
     * {@link #setMainGene(Gene)} or {@link #setHiddenGene(Gene)}.
     *
     * @return combined gene
     */
    @NotNull
    Gene getCombinedGene();
    // Paper end - Panda API

    public enum Gene {

        // Start generate - PandaGene
        // @GeneratedFrom 1.21.6-pre1
        NORMAL(false),
        LAZY(false),
        WORRIED(false),
        PLAYFUL(false),
        BROWN(true),
        WEAK(true),
        AGGRESSIVE(false);
        // End generate - PandaGene

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
