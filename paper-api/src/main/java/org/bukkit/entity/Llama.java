package org.bukkit.entity;

import com.destroystokyo.paper.entity.RangedEntity;
import org.bukkit.inventory.LlamaInventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Llama.
 */
public interface Llama extends ChestedHorse, RangedEntity { // Paper

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
    @NotNull
    Color getColor();

    /**
     * Sets the llama's color.
     *
     * @param color a {@link Color} for this llama
     */
    void setColor(@NotNull Color color);

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

    @NotNull
    @Override
    LlamaInventory getInventory();

    // Paper start
    /**
     * Checks if this llama is in a caravan.
     * This means that this llama is currently following
     * another llama.
     *
     * @return is in caravan
     */
    boolean inCaravan();

    /**
     * Joins a caravan, with the provided llama being the leader
     * of the caravan.
     * This llama will then follow the provided llama.
     *
     * @param llama head of caravan to join
     */
    void joinCaravan(@NotNull Llama llama);

    /**
     * Leaves the current caravan that they are in.
     */
    void leaveCaravan();

    /**
     * Get the llama that this llama is following.
     * <p>
     * Does not necessarily mean the leader of the entire caravan.
     *
     * @return the llama currently being followed
     */
    @org.jetbrains.annotations.Nullable
    Llama getCaravanHead();

    /**
     * Checks if another llama is currently following behind
     * this llama.
     *
     * @return true if being followed in the caravan
     */
    boolean hasCaravanTail();

    /**
     * Gets the llama that is currently following behind
     * this llama.
     *
     * @return the llama following this llama, or null if none is following them
     */
    @org.jetbrains.annotations.Nullable
    Llama getCaravanTail();
    // Paper end
}
