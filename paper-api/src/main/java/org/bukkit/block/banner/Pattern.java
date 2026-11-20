package org.bukkit.block.banner;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import java.util.Map;
import java.util.NoSuchElementException;
import org.bukkit.DyeColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jspecify.annotations.Nullable;

/**
 * A layer of a banner, made of a {@link PatternType} and a {@link DyeColor}.
 */
@SerializableAs("Pattern")
public class Pattern implements ConfigurationSerializable {

    private static final String COLOR = "color";
    private static final String PATTERN = "pattern";

    private final DyeColor color;
    private final PatternType pattern;

    /**
     * Constructor for deserialization.
     *
     * @param map the map to deserialize from
     */
    public Pattern(final Map<String, Object> map) {
        this.color = DyeColor.legacyValueOf(getString(map, COLOR));

        final String value = getString(map, PATTERN);
        PatternType patternType = PatternType.getByIdentifier(value);

        if (patternType == null) {
            final NamespacedKey patternKey = NamespacedKey.fromString(value);
            if (patternKey != null) {
                patternType = RegistryAccess.registryAccess().getRegistry(RegistryKey.BANNER_PATTERN).get(patternKey);
            }
        }

        Preconditions.checkNotNull(patternType, "Pattern type for key %s cannot be null", value);

        this.pattern = patternType;
    }

    /**
     * Creates a new pattern from the specified color and
     * pattern type.
     *
     * @param color   the pattern color
     * @param pattern the pattern type
     */
    public Pattern(final DyeColor color, final PatternType pattern) {
        this.color = color;
        this.pattern = pattern;
    }

    private static String getString(final Map<?, ?> map, final Object key) {
        final Object str = map.get(key);
        if (str instanceof String) {
            return (String) str;
        }
        throw new NoSuchElementException(map + " does not contain " + key);
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.of(
            COLOR, this.color.toString(),
            PATTERN, this.pattern.getKey().toString()
        );
    }

    /**
     * Returns the color of the pattern.
     *
     * @return the color of the pattern
     */
    public DyeColor getColor() {
        return this.color;
    }

    /**
     * Returns the type of pattern.
     *
     * @return the pattern type
     */
    public PatternType getPattern() {
        return this.pattern;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.color.hashCode();
        hash = 97 * hash + this.pattern.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final Pattern other = (Pattern) obj;
        return this.color == other.color && this.pattern == other.pattern;
    }
}
