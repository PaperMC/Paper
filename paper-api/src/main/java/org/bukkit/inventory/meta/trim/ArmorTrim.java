package org.bukkit.inventory.meta.trim;

import com.google.common.base.Preconditions;
import java.util.Objects;
import org.bukkit.inventory.meta.ArmorMeta;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an armor trim that may be applied to an item.
 *
 * @see ArmorMeta#setTrim(ArmorTrim)
 */
public class ArmorTrim {

    private final TrimMaterial material;
    private final TrimPattern pattern;

    /**
     * Create a new {@link ArmorTrim} given a {@link TrimMaterial} and
     * {@link TrimPattern}.
     *
     * @param material the material
     * @param pattern the pattern
     */
    public ArmorTrim(@NotNull TrimMaterial material, @NotNull TrimPattern pattern) {
        Preconditions.checkArgument(material != null, "material must not be null");
        Preconditions.checkArgument(pattern != null, "pattern must not be null");

        this.material = material;
        this.pattern = pattern;
    }

    /**
     * Get the {@link TrimMaterial} for this armor trim.
     *
     * @return the material
     */
    @NotNull
    public TrimMaterial getMaterial() {
        return material;
    }

    /**
     * Get the {@link TrimPattern} for this armor trim.
     *
     * @return the pattern
     */
    @NotNull
    public TrimPattern getPattern() {
        return pattern;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(material);
        hash = 31 * hash + Objects.hashCode(pattern);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof ArmorTrim)) {
            return false;
        }

        ArmorTrim other = (ArmorTrim) obj;
        return material == other.material && pattern == other.pattern;
    }
}
