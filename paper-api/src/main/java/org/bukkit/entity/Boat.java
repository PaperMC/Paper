package org.bukkit.entity;

import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a boat entity.
 */
public interface Boat extends Vehicle, io.papermc.paper.entity.Leashable { // Paper - Leashable API

    /**
     * Gets the wood type of the boat.
     *
     * @return the wood type
     * @deprecated deprecated in favor of {@link #getBoatType()}
     */
    @Deprecated(since = "1.19", forRemoval = true)
    @NotNull
    TreeSpecies getWoodType();

    /**
     * Sets the wood type of the boat.
     *
     * @param species the new wood type
     * @deprecated deprecated in favor of {@link #setBoatType(Type)}
     */
    @Deprecated(since = "1.19", forRemoval = true)
    void setWoodType(@NotNull TreeSpecies species);

    /**
     * Gets the type of the boat.
     *
     * @return the boat type
     * @deprecated different boats types are now different entity types
     */
    @Deprecated(since = "1.21.2")
    @NotNull
    Type getBoatType();

    /**
     * Sets the type of the boat.
     *
     * @param type the new type
     * @deprecated different boats types are now different entity types
     */
    @Deprecated(since = "1.21.2")
    void setBoatType(@NotNull Type type);

    /**
     * Gets the maximum speed of a boat. The speed is unrelated to the
     * velocity.
     *
     * @return The max speed.
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public double getMaxSpeed();

    /**
     * Sets the maximum speed of a boat. Must be nonnegative. Default is 0.4D.
     *
     * @param speed The max speed.
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public void setMaxSpeed(double speed);

    /**
     * Gets the deceleration rate (newSpeed = curSpeed * rate) of occupied
     * boats. The default is 0.2.
     *
     * @return The rate of deceleration
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public double getOccupiedDeceleration();

    /**
     * Sets the deceleration rate (newSpeed = curSpeed * rate) of occupied
     * boats. Setting this to a higher value allows for quicker acceleration.
     * The default is 0.2.
     *
     * @param rate deceleration rate
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public void setOccupiedDeceleration(double rate);

    /**
     * Gets the deceleration rate (newSpeed = curSpeed * rate) of unoccupied
     * boats. The default is -1. Values below 0 indicate that no additional
     * deceleration is imposed.
     *
     * @return The rate of deceleration
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public double getUnoccupiedDeceleration();

    /**
     * Sets the deceleration rate (newSpeed = curSpeed * rate) of unoccupied
     * boats. Setting this to a higher value allows for quicker deceleration
     * of boats when a player disembarks. The default is -1. Values below 0
     * indicate that no additional deceleration is imposed.
     *
     * @param rate deceleration rate
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public void setUnoccupiedDeceleration(double rate);

    /**
     * Get whether boats can work on land.
     *
     * @return whether boats can work on land
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public boolean getWorkOnLand();

    /**
     * Set whether boats can work on land.
     *
     * @param workOnLand whether boats can work on land
     * @deprecated boats are complex and many of these methods do not work correctly across multiple versions.
     */
    @Deprecated(since = "1.9")
    public void setWorkOnLand(boolean workOnLand);

    /**
     * Gets the status of the boat.
     *
     * @return the status
     */
    @NotNull
    public Status getStatus();

    /**
     * Represents the type of boats.
     * @deprecated different boats types are now different entity types
     */
    @Deprecated(since = "1.21.2")
    public enum Type {
        OAK(Material.OAK_PLANKS),
        SPRUCE(Material.SPRUCE_PLANKS),
        BIRCH(Material.BIRCH_PLANKS),
        JUNGLE(Material.JUNGLE_PLANKS),
        ACACIA(Material.ACACIA_PLANKS),
        CHERRY(Material.CHERRY_PLANKS),
        DARK_OAK(Material.DARK_OAK_PLANKS),
        MANGROVE(Material.MANGROVE_PLANKS),
        BAMBOO(Material.BAMBOO_PLANKS),
        ;

        private final Material materialBlock;

        private Type(Material materialBlock) {
            this.materialBlock = materialBlock;
        }

        /**
         * Gets the material of the boat type.
         *
         * @return a material
         */
        @NotNull
        public Material getMaterial() {
            return this.materialBlock;
        }
    }

    /**
     * Represents the status of the boat.
     */
    public enum Status {

        NOT_IN_WORLD,
        // Start generate - BoatStatus
        // @GeneratedFrom 1.21.6-pre1
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
        // End generate - BoatStatus
    }

    // Paper start
    /**
     * Gets the {@link Material} that represents this Boat type.
     *
     * @return the boat material.
     */
    @NotNull
    public Material getBoatMaterial();
    // Paper end
}
