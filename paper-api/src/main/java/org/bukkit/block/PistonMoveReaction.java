package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

public enum PistonMoveReaction {
    /**
     * Indicates that the block can be pushed or pulled.
     */
    MOVE(0),
    /**
     * Indicates the block is fragile and will break if pushed on.
     */
    BREAK(1),
    /**
     * Indicates that the block will resist being pushed or pulled.
     */
    BLOCK(2);

    private int id;
    private static Map<Integer, PistonMoveReaction> byId = new HashMap<Integer, PistonMoveReaction>();
    static {
        for (PistonMoveReaction reaction : PistonMoveReaction.values()) {
            byId.put(reaction.id, reaction);
        }
    }

    private PistonMoveReaction(int id) {
        this.id = id;
    }

    /**
     * @return The ID of the move reaction
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id An ID
     * @return The move reaction with that ID
     */
    public static PistonMoveReaction getById(int id) {
        return byId.get(id);
    }
}
