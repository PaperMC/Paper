package org.bukkit.entity;

/**
 * Represents an entity which can be shorn with shears.
 * @deprecated Spigots shearable API miserably fails at capturing all entities that may be sheared by a player, like
 * mushroom cows which, once sheared, convert into normal cows. For such entities, methods like
 * {@link #setSheared(boolean)} or {@link #isSheared()} make no sense, making this API and interface dead API from
 * the get-go.
 */
@Deprecated(forRemoval = true, since = "1.21")
public interface Shearable {

    /**
     * Gets whether the entity is in its sheared state.
     *
     * @return Whether the entity is sheared.
     * @deprecated Use {@link io.papermc.paper.entity.Shearable#readyToBeSheared()} instead.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    boolean isSheared();

    /**
     * Sets whether the entity is in its sheared state.
     *
     * @param flag Whether to shear the entity
     * @deprecated Use {@link io.papermc.paper.entity.Shearable#shear()} instead if applicable.
     * Some entities cannot be "unsheared".
     */
    @Deprecated(forRemoval = true, since = "1.21")
    void setSheared(boolean flag);
}
