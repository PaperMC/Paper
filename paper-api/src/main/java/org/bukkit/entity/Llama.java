package org.bukkit.entity;

import org.bukkit.inventory.LlamaInventory;

/**
 * Represents a Llama.
 */
public interface Llama extends ChestedHorse {

    /**
     * Represents the base color that the llama has.
     */
    public enum Color {

        /**
         * A cream-colored llama.
         */
        CREAMY,
        /**
         * A white llama.
         */
        WHITE,
        /**
         * A brown llama.
         */
        BROWN,
        /**
         * A gray llama.
         */
        GRAY;
    }

    /**
     * Gets the llama's color.
     *
     * @return a {@link Color} representing the llama's color
     */
    Color getColor();

    /**
     * Sets the llama's color.
     *
     * @param color a {@link Color} for this llama
     */
    void setColor(Color color);

    /**
     * Gets the llama's strength. A higher strength llama will have more
     * inventory slots and be more threatening to entities.
     *
     * @return llama strength [1,5]
     */
    int getStrength();

    /**
     * Sets the llama's strength. A higher strength llama will have more
     * inventory slots and be more threatening to entities. Inventory slots are
     * equal to strength * 3.
     *
     * @param strength llama strength [1,5]
     */
    void setStrength(int strength);

    @Override
    LlamaInventory getInventory();
}
