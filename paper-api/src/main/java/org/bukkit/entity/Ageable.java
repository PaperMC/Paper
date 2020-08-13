package org.bukkit.entity;

/**
 * Represents an entity that can age.
 */
public interface Ageable extends Creature {
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
     * maturing or getting ready for mating.
     *
     * @param lock new lock
     * @deprecated see {@link Breedable#setAgeLock(boolean)}
     */
    @Deprecated
    public void setAgeLock(boolean lock);

    /**
     * Gets the current agelock.
     *
     * @return the current agelock
     * @deprecated see {@link Breedable#getAgeLock()}
     */
    @Deprecated
    public boolean getAgeLock();

    /**
     * Sets the age of the mob to a baby
     */
    public void setBaby();

    /**
     * Sets the age of the mob to an adult
     */
    public void setAdult();

    /**
     * Returns true if the mob is an adult.
     *
     * @return return true if the mob is an adult
     */
    public boolean isAdult();

    /**
     * Return the ability to breed of the animal.
     *
     * @return the ability to breed of the animal
     * @deprecated see {@link Breedable#canBreed()}
     */
    @Deprecated
    public boolean canBreed();

    /**
     * Set breedability of the animal, if the animal is a baby and set to
     * breed it will instantly grow up.
     *
     * @param breed breedability of the animal
     * @deprecated see {@link Breedable#setBreed(boolean)}
     */
    @Deprecated
    public void setBreed(boolean breed);
}
