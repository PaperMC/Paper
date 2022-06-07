package org.bukkit.entity;

/**
 * A baby {@link Frog}.
 */
public interface Tadpole extends Fish {

    /**
     * Gets the age of this mob.
     *
     * @return Age
     */
    public int getAge();

    /**
     * Sets the age of this mob.
     *
     * @param age New age
     */
    public void setAge(int age);
}
