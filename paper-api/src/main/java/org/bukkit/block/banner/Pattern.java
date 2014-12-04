package org.bukkit.block.banner;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bukkit.DyeColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("Pattern")
public class Pattern implements ConfigurationSerializable {

    private static final String COLOR = "color";
    private static final String PATTERN = "pattern";
    
    private final DyeColor color;
    private final PatternType pattern;

    /**
     * Creates a new pattern from the specified color and
     * pattern type
     *
     * @param color   the pattern color
     * @param pattern the pattern type
     */
    public Pattern(DyeColor color, PatternType pattern) {
        this.color = color;
        this.pattern = pattern;
    }
    
    /**
     * Constructor for deserialization.
     *
     * @param map the map to deserialize from
     */
    public Pattern(Map<String, Object> map) {
        color = DyeColor.valueOf(getString(map, COLOR));
        pattern = PatternType.getByIdentifier(getString(map, PATTERN));
    }    

    private static String getString(Map<?,?> map, Object key) {
        Object str = map.get(key);
        if (str instanceof String) {
            return (String) str;
        }
        throw new NoSuchElementException(map + " does not contain " + key);
    }
    
    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>of(
            COLOR, color.toString(),
            PATTERN, pattern.getIdentifier()
        );
    }

    /**
     * Returns the color of the pattern
     *
     * @return the color of the pattern
     */
    public DyeColor getColor() {
        return color;
    }

    /**
     * Returns the type of pattern
     *
     * @return the pattern type
     */
    public PatternType getPattern() {
        return pattern;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 97 * hash + (this.pattern != null ? this.pattern.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pattern other = (Pattern) obj;
        return this.color == other.color && this.pattern == other.pattern;
    }
}
