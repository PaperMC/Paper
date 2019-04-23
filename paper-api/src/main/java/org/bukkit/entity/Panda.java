package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;

/**
 * Panda entity.
 */
public interface Panda extends Animals {

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
