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
     * Gets the llamas's color.
     *
     * @return a {@link Color} representing the llama's color
     */
    public Color getColor();

    /**
     * Sets the llama's color.
     *
     * @param color a {@link Color} for this llama
     */
    public void setColor(Color color);

    @Override
    public LlamaInventory getInventory();
}
