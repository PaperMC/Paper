package org.bukkit.entity;

/**
 * Represents an entity that can age and breed.
 */
public interface Breedable extends Ageable {

    /**
     * Return the ability to breed of the animal.
     *
     * @return the ability to breed of the animal
     */
    public boolean canBreed();

    /**
     * Set breedability of the animal, if the animal is a baby and set to
     * breed it will instantly grow up.
     *
     * @param breed breedability of the animal
     */
    public void setBreed(boolean breed);

}
