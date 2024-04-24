package org.bukkit.block.banner;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Keyed;
import org.bukkit.MinecraftExperimental;
import org.bukkit.MinecraftExperimental.Requires;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PatternType implements Keyed {
    BASE("b", "base"),
    SQUARE_BOTTOM_LEFT("bl", "square_bottom_left"),
    SQUARE_BOTTOM_RIGHT("br", "square_bottom_right"),
    SQUARE_TOP_LEFT("tl", "square_top_left"),
    SQUARE_TOP_RIGHT("tr", "square_top_right"),
    STRIPE_BOTTOM("bs", "stripe_bottom"),
    STRIPE_TOP("ts", "stripe_top"),
    STRIPE_LEFT("ls", "stripe_left"),
    STRIPE_RIGHT("rs", "stripe_right"),
    STRIPE_CENTER("cs", "stripe_center"),
    STRIPE_MIDDLE("ms", "stripe_middle"),
    STRIPE_DOWNRIGHT("drs", "stripe_downright"),
    STRIPE_DOWNLEFT("dls", "stripe_downleft"),
    SMALL_STRIPES("ss", "small_stripes"),
    CROSS("cr", "cross"),
    STRAIGHT_CROSS("sc", "straight_cross"),
    TRIANGLE_BOTTOM("bt", "triangle_bottom"),
    TRIANGLE_TOP("tt", "triangle_top"),
    TRIANGLES_BOTTOM("bts", "triangles_bottom"),
    TRIANGLES_TOP("tts", "triangles_top"),
    DIAGONAL_LEFT("ld", "diagonal_left"),
    DIAGONAL_UP_RIGHT("rd", "diagonal_up_right"),
    DIAGONAL_UP_LEFT("lud", "diagonal_up_left"),
    DIAGONAL_RIGHT("rud", "diagonal_right"),
    CIRCLE("mc", "circle"),
    RHOMBUS("mr", "rhombus"),
    HALF_VERTICAL("vh", "half_vertical"),
    HALF_HORIZONTAL("hh", "half_horizontal"),
    HALF_VERTICAL_RIGHT("vhr", "half_vertical_right"),
    HALF_HORIZONTAL_BOTTOM("hhb", "half_horizontal_bottom"),
    BORDER("bo", "border"),
    CURLY_BORDER("cbo", "curly_border"),
    CREEPER("cre", "creeper"),
    GRADIENT("gra", "gradient"),
    GRADIENT_UP("gru", "gradient_up"),
    BRICKS("bri", "bricks"),
    SKULL("sku", "skull"),
    FLOWER("flo", "flower"),
    MOJANG("moj", "mojang"),
    GLOBE("glb", "globe"),
    PIGLIN("pig", "piglin"),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    FLOW("flw", "flow"),
    @MinecraftExperimental(Requires.UPDATE_1_21)
    GUSTER("gus", "guster");

    private final String identifier;
    private final NamespacedKey key;
    private static final Map<String, PatternType> byString = new HashMap<String, PatternType>();

    static {
        for (PatternType p : values()) {
            byString.put(p.identifier, p);
        }
    }

    private PatternType(/*@NotNull*/ String identifier, String key) {
        this.identifier = identifier;
        this.key = NamespacedKey.minecraft(key);
    }

    @Override
    @NotNull
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Returns the identifier used to represent
     * this pattern type
     *
     * @return the pattern's identifier
     * @see #getKey
     * @deprecated magic value
     */
    @NotNull
    @Deprecated(forRemoval = true)
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the pattern type which matches the passed
     * identifier or null if no matches are found
     *
     * @param identifier the identifier
     * @return the matched pattern type or null
     * @see Registry#BANNER_PATTERN
     * @deprecated magic value, use {@link Registry#get(NamespacedKey)} instead
     */
    @Contract("null -> null")
    @Nullable
    @Deprecated(forRemoval = true)
    public static PatternType getByIdentifier(@Nullable String identifier) {
        return byString.get(identifier);
    }
}
