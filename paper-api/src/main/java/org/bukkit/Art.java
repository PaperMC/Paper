package org.bukkit;

import java.util.HashMap;

import org.apache.commons.lang.Validate;

import com.google.common.collect.Maps;

/**
 * Represents the art on a painting
 */
public enum Art {
    KEBAB(0, 1, 1),
    AZTEC(1, 1, 1),
    ALBAN(2, 1, 1),
    AZTEC2(3, 1, 1),
    BOMB(4, 1, 1),
    PLANT(5, 1, 1),
    WASTELAND(6, 1, 1),
    POOL(7, 2, 1),
    COURBET(8, 2, 1),
    SEA(9, 2, 1),
    SUNSET(10, 2, 1),
    CREEBET(11, 2, 1),
    WANDERER(12, 1, 2),
    GRAHAM(13, 1, 2),
    MATCH(14, 2, 2),
    BUST(15, 2, 2),
    STAGE(16, 2, 2),
    VOID(17, 2, 2),
    SKULL_AND_ROSES(18, 2, 2),
    FIGHTERS(19, 4, 2),
    POINTER(20, 4, 4),
    PIGSCENE(21, 4, 4),
    BURNINGSKULL(22, 4, 4),
    SKELETON(23, 4, 3),
    DONKEYKONG(24, 4, 3);

    private int id, width, height;
    private static final HashMap<String, Art> BY_NAME = Maps.newHashMap();
    private static final HashMap<Integer, Art> BY_ID = Maps.newHashMap();

    private Art(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    /**
     * Gets the width of the painting, in blocks
     *
     * @return The width of the painting, in blocks
     */
    public int getBlockWidth() {
        return width;
    }

    /**
     * Gets the height of the painting, in blocks
     *
     * @return The height of the painting, in blocks
     */
    public int getBlockHeight() {
        return height;
    }

    /**
     * Get the ID of this painting.
     *
     * @return The ID of this painting
     */
    public int getId() {
        return id;
    }

    /**
     * Get a painting by its numeric ID
     *
     * @param id The ID
     * @return The painting
     */
    public static Art getById(int id) {
        return BY_ID.get(id);
    }

    /**
     * Get a painting by its unique name
     * <p />
     * This ignores underscores and capitalization
     *
     * @param name The name
     * @return The painting
     */
    public static Art getByName(String name) {
        Validate.notNull(name, "Name cannot be null");

        return BY_NAME.get(name.toLowerCase().replaceAll("_", ""));
    }

    static {
        for (Art art : values()) {
            BY_ID.put(art.id, art);
            BY_NAME.put(art.toString().toLowerCase().replaceAll("_", ""), art);
        }
    }
}
