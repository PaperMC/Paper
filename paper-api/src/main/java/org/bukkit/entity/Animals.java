package org.bukkit.entity;

/**
 * Represents an Animal.
 */
public interface Animals extends Creature {
    /**
     * Gets the age of this animal.
     *
     * @return Age
     */
    public int getAge();

    /**
     * Sets the age of this animal.
     *
     * @param age New age
     */
    public void setAge(int age);
}
