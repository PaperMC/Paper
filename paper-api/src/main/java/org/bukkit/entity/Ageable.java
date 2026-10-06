package org.bukkit.entity;

/**
 * Represents an entity that can age.
 */
public interface Ageable extends Creature {
    /**
     * Gets the age of this creature.
     *
     * @return Age
     */
    public int getAge();

    /**
     * Sets the age of this creature.
     *
     * @param age New age
     */
    public void setAge(int age);

    /**
     * Lock the age of the creature, setting this will prevent the creature from
     * maturing or getting ready for mating. Plugins can still increase the age
     * manually, however.
     *
     * @param lock new lock state
     */
    public void setAgeLock(boolean lock);

    /**
     * Checks if the age of the creature is locked.
     *
     * @return the current lock state
     * @see #setAgeLock(boolean)
     */
    public boolean getAgeLock();

    /**
     * Transforms this creature to its baby counter-part.
     * <p>
     * This will not have any effect if the creature is already in
     * this state.
     *
     * @apiNote some of them cannot be represented as such, so it's
     * wise to check if {@link #isAdult()} returns {@code false} after this method
     * for general purpose use.
     * @see #setAdult()
     * @see #setAge(int)
     */
    public void setBaby();

    /**
     * Transforms this creature to its adult counter-part.
     * <p>
     * This will not have any effect if the creature is already in
     * this state.
     *
     * @see #setBaby()
     * @see #setAge(int)
     */
    public void setAdult();

    /**
     * Returns true if the creature is an adult.
     *
     * @return if the creature is an adult
     * @see #getAge()
     */
    public boolean isAdult();

    /**
     * Return the ability to breed of the creature.
     *
     * @return the ability to breed of the creature
     * @deprecated see {@link Breedable#canBreed()}
     */
    @Deprecated(since = "1.16.2")
    public boolean canBreed();

    /**
     * Set breedability of the creature, if the creature is a baby and set to
     * breed it will instantly grow up.
     *
     * @param breed breedability of the creature
     * @deprecated see {@link Breedable#setBreed(boolean)}
     */
    @Deprecated(since = "1.16.2")
    public void setBreed(boolean breed);
}
