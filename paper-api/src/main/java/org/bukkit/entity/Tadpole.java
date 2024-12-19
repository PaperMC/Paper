package org.bukkit.entity;

/**
 * A baby {@link Frog}.
 *
 * @since 1.19
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

    // Paper start - Tadpole age lock api
    /**
     * Lock the age of the animal, setting this will prevent the animal from
     * maturing.
     *
     * @param lock new lock
     * @since 1.19.3
     */
    void setAgeLock(boolean lock);

    /**
     * Gets the current agelock.
     *
     * @return the current agelock
     * @since 1.19.3
     */
    boolean getAgeLock();
    // Paper end
}
