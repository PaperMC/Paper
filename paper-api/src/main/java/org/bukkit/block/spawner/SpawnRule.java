package org.bukkit.block.spawner;

import com.google.common.base.Preconditions;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.jspecify.annotations.Nullable;

/**
 * Represents a spawn rule that controls what conditions an entity from a
 * monster spawner can spawn.
 */
@SerializableAs("SpawnRule")
public class SpawnRule implements Cloneable, ConfigurationSerializable {

    private int minBlockLight;
    private int maxBlockLight;
    private int minSkyLight;
    private int maxSkyLight;

    /**
     * Constructs a new SpawnRule.
     *
     * @param minBlockLight The minimum (inclusive) block light required for
     *                      spawning to succeed.
     * @param maxBlockLight The maximum (inclusive) block light required for
     *                      spawning to succeed.
     * @param minSkyLight   The minimum (inclusive) sky light required for
     *                      spawning to succeed.
     * @param maxSkyLight   The maximum (inclusive) sky light required for
     *                      spawning to succeed.
     */
    public SpawnRule(final int minBlockLight, final int maxBlockLight, final int minSkyLight, final int maxSkyLight) {
        Preconditions.checkArgument(minBlockLight <= maxBlockLight, "minBlockLight must be <= maxBlockLight (%s <= %s)", minBlockLight, maxBlockLight);
        Preconditions.checkArgument(minSkyLight <= maxSkyLight, "minSkyLight must be <= maxSkyLight (%s <= %s)", minSkyLight, maxSkyLight);
        Preconditions.checkArgument(minBlockLight >= 0, "minBlockLight must be >= 0 (given %s)", minBlockLight);
        Preconditions.checkArgument(maxBlockLight >= 0, "maxBlockLight must be >= 0 (given %s)", maxBlockLight);
        Preconditions.checkArgument(minSkyLight >= 0, "minSkyLight must be >= 0 (given %s)", minSkyLight);
        Preconditions.checkArgument(maxSkyLight >= 0, "maxSkyLight must be >= 0 (given %s)", maxSkyLight);

        this.minBlockLight = minBlockLight;
        this.maxBlockLight = maxBlockLight;
        this.minSkyLight = minSkyLight;
        this.maxSkyLight = maxSkyLight;
    }

    /**
     * Gets the minimum (inclusive) block light required for spawning to
     * succeed.
     *
     * @return minimum block light
     */
    public int getMinBlockLight() {
        return this.minBlockLight;
    }

    /**
     * Sets the minimum (inclusive) block light required for spawning to
     * succeed.
     *
     * @param minBlockLight minimum block light
     */
    public void setMinBlockLight(final int minBlockLight) {
        Preconditions.checkArgument(minBlockLight >= 0, "minBlockLight must be >= 0 (given %s)", minBlockLight);
        Preconditions.checkArgument(minBlockLight <= this.maxBlockLight, "minBlockLight must be <= maxBlockLight (%s <= %s)", minBlockLight, this.maxBlockLight);

        this.minBlockLight = minBlockLight;
    }

    /**
     * Gets the maximum (inclusive) block light required for spawning to
     * succeed.
     *
     * @return maximum block light
     */
    public int getMaxBlockLight() {
        return this.maxBlockLight;
    }

    /**
     * Sets the maximum (inclusive) block light required for spawning to
     * succeed.
     *
     * @param maxBlockLight maximum block light
     */
    public void setMaxBlockLight(final int maxBlockLight) {
        Preconditions.checkArgument(maxBlockLight >= 0, "maxBlockLight must be >= 0 (given %s)", maxBlockLight);

        this.maxBlockLight = maxBlockLight;
    }

    /**
     * Gets the minimum (inclusive) sky light required for spawning to succeed.
     *
     * @return minimum sky light
     */
    public int getMinSkyLight() {
        return this.minSkyLight;
    }

    /**
     * Sets the minimum (inclusive) sky light required for spawning to succeed.
     *
     * @param minSkyLight minimum sky light
     */
    public void setMinSkyLight(final int minSkyLight) {
        Preconditions.checkArgument(minSkyLight >= 0, "minSkyLight must be >= 0 (given %s)", minSkyLight);
        Preconditions.checkArgument(minSkyLight <= this.maxSkyLight, "minSkyLight must be <= maxSkyLight (%s <= %s)", minSkyLight, this.maxSkyLight);

        this.minSkyLight = minSkyLight;
    }

    /**
     * Gets the maximum (inclusive) sky light required for spawning to succeed.
     *
     * @return maximum sky light
     */
    public int getMaxSkyLight() {
        return this.maxSkyLight;
    }

    /**
     * Sets the maximum (inclusive) sky light required for spawning to succeed.
     *
     * @param maxSkyLight maximum sky light
     */
    public void setMaxSkyLight(final int maxSkyLight) {
        Preconditions.checkArgument(maxSkyLight >= 0, "maxSkyLight must be >= 0 (given %s)", maxSkyLight);

        this.maxSkyLight = maxSkyLight;
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (!(obj instanceof final SpawnRule other)) {
            return false;
        }

        return this.minBlockLight == other.minBlockLight && this.maxBlockLight == other.maxBlockLight && this.minSkyLight == other.minSkyLight && this.maxSkyLight == other.maxSkyLight;
    }

    @Override
    public int hashCode() {
        int hash = this.minBlockLight;

        hash = (hash << 4) + this.maxBlockLight;
        hash = (hash << 4) + this.minSkyLight;
        hash = (hash << 4) + this.maxSkyLight;

        return hash;
    }

    @Override
    public SpawnRule clone() {
        try {
            return (SpawnRule) super.clone();
        } catch (final CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public Map<String, Object> serialize() {
        final Map<String, Object> result = new LinkedHashMap<>();
        final Map<String, Object> block = new LinkedHashMap<>();
        final Map<String, Object> sky = new LinkedHashMap<>();

        block.put("min", this.getMinBlockLight());
        block.put("max", this.getMaxBlockLight());
        sky.put("min", this.getMinSkyLight());
        sky.put("max", this.getMaxSkyLight());

        result.put("block-light", block);
        result.put("sky-light", sky);

        return result;
    }

    /**
     * Required method for configuration serialization.
     *
     * @param args map to deserialize
     * @return deserialized spawn rule
     * @see ConfigurationSerializable
     */
    public static SpawnRule deserialize(final Map<String, Object> args) {
        int minBlock = 0;
        int maxBlock = 0;
        int minSky = 0;
        int maxSky = 0;

        final Object block = args.get("block-light");
        final Object sky = args.get("sky-light");

        if (block instanceof final Map<?, ?> blockMap) {
            if (blockMap.containsKey("min")) {
                minBlock = (int) blockMap.get("min");
            }

            if (blockMap.containsKey("max")) {
                maxBlock = (int) blockMap.get("max");
            }
        }

        if (sky instanceof final Map<?, ?> skyMap) {
            if (skyMap.containsKey("min")) {
                minSky = (int) skyMap.get("min");
            }

            if (skyMap.containsKey("max")) {
                maxSky = (int) skyMap.get("max");
            }
        }

        return new SpawnRule(minBlock, maxBlock, minSky, maxSky);
    }
}
