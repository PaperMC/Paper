package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the art on a painting
 */
public enum Art implements Keyed {
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
    WITHER(19, 2, 2),
    FIGHTERS(20, 4, 2),
    POINTER(21, 4, 4),
    PIGSCENE(22, 4, 4),
    BURNING_SKULL(23, 4, 4),
    SKELETON(24, 4, 3),
    DONKEY_KONG(25, 4, 3),
    EARTH(26, 2, 2),
    WIND(27, 2, 2),
    WATER(28, 2, 2),
    FIRE(29, 2, 2),
    BAROQUE(30, 2, 2),
    HUMBLE(31, 2, 2),
    MEDITATIVE(32, 1, 1),
    PRAIRIE_RIDE(33, 1, 2),
    UNPACKED(34, 4, 4),
    BACKYARD(35, 3, 4),
    BOUQUET(36, 3, 3),
    CAVEBIRD(37, 3, 3),
    CHANGING(38, 4, 2),
    COTAN(39, 3, 3),
    ENDBOSS(40, 3, 3),
    FERN(41, 3, 3),
    FINDING(42, 4, 2),
    LOWMIST(43, 4, 2),
    ORB(44, 4, 4),
    OWLEMONS(45, 3, 3),
    PASSAGE(46, 4, 2),
    POND(47, 3, 4),
    SUNFLOWERS(48, 3, 3),
    TIDES(49, 3, 3);

    private final int id, width, height;
    private final NamespacedKey key;
    private static final HashMap<String, Art> BY_NAME = Maps.newHashMap();
    private static final HashMap<Integer, Art> BY_ID = Maps.newHashMap();

    private Art(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.key = NamespacedKey.minecraft(name().toLowerCase(Locale.ROOT));
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
     * @deprecated Magic value
     */
    @Deprecated
    public int getId() {
        return id;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get a painting by its numeric ID
     *
     * @param id The ID
     * @return The painting
     * @deprecated Magic value
     */
    @Deprecated
    @Nullable
    public static Art getById(int id) {
        return BY_ID.get(id);
    }

    /**
     * Get a painting by its unique name
     * <p>
     * This ignores underscores and capitalization
     *
     * @param name The name
     * @return The painting
     */
    @Nullable
    public static Art getByName(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Name cannot be null");

        return BY_NAME.get(name.toLowerCase(Locale.ROOT));
    }

    static {
        for (Art art : values()) {
            BY_ID.put(art.id, art);
            BY_NAME.put(art.toString().toLowerCase(Locale.ROOT), art);
        }
    }
}
