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

    /**
     * Lock the age of the animal, setting this will prevent the animal from
     * maturing.
     *
     * @param lock new lock
     */
    void setAgeLock(boolean lock);

    /**
     * Gets the current agelock.
     *
     * @return the current agelock
     */
    boolean getAgeLock();
}
