package org.bukkit.entity;

import org.bukkit.DyeColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * Meow.
 */
public interface Cat extends Tameable, Sittable {

    /**
     * Gets the current type of this cat.
     *
     * @return Type of the cat.
     */
    @NotNull
    public Type getCatType();

    /**
     * Sets the current type of this cat.
     *
     * @param type New type of this cat.
     */
    public void setCatType(@NotNull Type type);

    /**
     * Get the collar color of this cat
     *
     * @return the color of the collar
     */
    @NotNull
    public DyeColor getCollarColor();

    /**
     * Set the collar color of this cat
     *
     * @param color the color to apply
     */
    public void setCollarColor(@NotNull DyeColor color);

    /**
     * Represents the various different cat types there are.
     */
    public enum Type implements Keyed {
        TABBY("tabby"),
        BLACK("black"),
        RED("red"),
        SIAMESE("siamese"),
        BRITISH_SHORTHAIR("british_shorthair"),
        CALICO("calico"),
        PERSIAN("persian"),
        RAGDOLL("ragdoll"),
        WHITE("white"),
        JELLIE("jellie"),
        ALL_BLACK("all_black");

        private final NamespacedKey key;

        private Type(String key) {
            this.key = NamespacedKey.minecraft(key);
        }

        @Override
        @NotNull
        public NamespacedKey getKey() {
            return key;
        }
    }
}
