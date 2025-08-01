package io.papermc.paper.entity;

/**
 * Represents error in player <b>.dat</b> file deserialization or serialization
 */
public class PlayerSerializationException extends RuntimeException{
    public PlayerSerializationException(String message) {
        super(message);
    }
}
