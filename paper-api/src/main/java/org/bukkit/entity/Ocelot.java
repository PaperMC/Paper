
package org.bukkit.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A wild tameable cat
 */
public interface Ocelot extends Animals {

    /**
     * Checks if this ocelot trusts players.
     *
     * @return true if it trusts players
     */
    public boolean isTrusting();

    /**
     * Sets if this ocelot trusts players.
     *
     * @param trust true if it trusts players
     */
    public void setTrusting(boolean trust);

    /**
     * Gets the current type of this cat.
     *
     * @return Type of the cat.
     * @deprecated Cats are now a separate entity.
     */
    @NotNull
    @Deprecated
    public Type getCatType();

    /**
     * Sets the current type of this cat.
     *
     * @param type New type of this cat.
     * @deprecated Cats are now a separate entity.
     */
    @Deprecated
    public void setCatType(@NotNull Type type);

    /**
     * Represents the various different cat types there are.
     *
     * @deprecated Cats are now a separate entity.
     */
    @Deprecated
    public enum Type {
        WILD_OCELOT(0),
        BLACK_CAT(1),
        RED_CAT(2),
        SIAMESE_CAT(3);

        private static final Type[] types = new Type[Type.values().length];
        private final int id;

        static {
            for (Type type : values()) {
                types[type.getId()] = type;
            }
        }

        private Type(int id) {
            this.id = id;
        }

        /**
         * Gets the ID of this cat type.
         *
         * @return Type ID.
         * @deprecated Magic value
         */
        @Deprecated
        public int getId() {
            return id;
        }

        /**
         * Gets a cat type by its ID.
         *
         * @param id ID of the cat type to get.
         * @return Resulting type, or null if not found.
         * @deprecated Magic value
         */
        @Deprecated
        @Nullable
        public static Type getType(int id) {
            return (id >= types.length) ? null : types[id];
        }
    }
}
