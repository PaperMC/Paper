package org.bukkit.entity;

/**
 * A baby {@link Frog}.
 */
public interface Tadpole extends Fish {

    /**
     * Gets the age of this tadpole.
     *
     * @return Age
     */
    public int getAge();

    /**
     * Sets the age of this tadpole.
     *
     * @param age New age
     */
    public void setAge(int age);

    /**
     * Lock the age of the tadpole, setting this will prevent the tadpole from
     * maturing. Plugins can still increase the age manually, however.
     *
     * @param lock new lock state
     */
    void setAgeLock(boolean lock);

    /**
     * Checks if the age of the tadpole is locked.
     *
     * @return the current lock state
     * @see #setAgeLock(boolean)
     */
    boolean getAgeLock();
}
