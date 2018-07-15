package org.bukkit.block.data;

/**
 * 'age' represents the different growth stages that a crop-like block can go
 * through.
 * <br>
 * A value of 0 indicates that the crop was freshly planted, whilst a value
 * equal to {@link #getMaximumAge()} indicates that the crop is ripe and ready
 * to be harvested.
 */
public interface Ageable extends BlockData {

    /**
     * Gets the value of the 'age' property.
     *
     * @return the 'age' value
     */
    int getAge();

    /**
     * Sets the value of the 'age' property.
     *
     * @param age the new 'age' value
     */
    void setAge(int age);

    /**
     * Gets the maximum allowed value of the 'age' property.
     *
     * @return the maximum 'age' value
     */
    int getMaximumAge();
}
