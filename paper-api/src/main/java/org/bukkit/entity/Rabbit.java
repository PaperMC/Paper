package org.bukkit.entity;

public interface Rabbit extends Animals {

    /**
     * @return The type of rabbit.
     */
    public Type getRabbitType();

    /**
     * @param type Sets the type of rabbit for this entity.
     */
    public void setRabbitType(Type type);

    /**
     * Represents the various types a Rabbit might be.
     */
    public enum Type {

        /**
         * Chocolate colored rabbit.
         */
        BROWN,
        /**
         * Pure white rabbit.
         */
        WHITE,
        /**
         * Black rabbit.
         */
        BLACK,
        /**
         * Black with white patches, or white with black patches?
         */
        BLACK_AND_WHITE,
        /**
         * Golden bunny.
         */
        GOLD,
        /**
         * Salt and pepper colored, whatever that means.
         */
        SALT_AND_PEPPER,
        /**
         * Rabbit with pure white fur, blood red horizontal eyes, and is hostile to players.
         */
        THE_KILLER_BUNNY
    }
}
