package org.bukkit.block;

import java.util.HashMap;
import java.util.Map;

public enum PistonMoveReaction {
    MOVE(0),
    BREAK(1),
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

    public int getId() {
        return this.id;
    }

    public static PistonMoveReaction getById(int id) {
        return byId.get(id);
    }
}
