package org.bukkit.structure;

import com.google.common.base.Preconditions;
import java.util.Random;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;

/**
 * Represents options for placing a {@link Structure}.
 */
public class PlacementOptions {

    private boolean includeEntities = true;
    private StructureRotation structureRotation = StructureRotation.NONE;
    private Mirror mirror = Mirror.NONE;
    private int palette = -1;
    private float integrity = 1;
    private final Random random;
    private boolean strict = false;
    private boolean applyWaterlogging = true;

    /**
     * Create a new set of placement options for placing a {@link Structure}.
     *
     * @param random The randomizer used for setting the structure's
     *               {@link org.bukkit.loot.LootTable LootTables} and integrity.
     */
    public PlacementOptions(Random random) {
        this.random = random;
    }

    /**
     * Whether to include entities when the structure is placed.
     *
     * @param includeEntities Whether to include entities.
     * @return This PlacementOptions.
     */
    public PlacementOptions includeEntities(boolean includeEntities) {
        this.includeEntities = includeEntities;
        return this;
    }

    /**
     * Whether to include entities when the structure is placed.
     *
     * @return Whether to include entities.
     */
    public boolean isIncludeEntities() {
        return includeEntities;
    }

    /**
     * Set the rotation a structure will place with.
     *
     * @param structureRotation Rotation to place with.
     * @return This PlacementOptions.
     */
    public PlacementOptions structureRotation(StructureRotation structureRotation) {
        this.structureRotation = structureRotation;
        return this;
    }

    /**
     * The rotation a structure will place with.
     *
     * @return Rotation to place with.
     */
    public StructureRotation getStructureRotation() {
        return structureRotation;
    }

    /**
     * Set the mirror a structure will place with.
     *
     * @param mirror Mirror to place with.
     * @return This PlacementOptions.
     */
    public PlacementOptions mirror(Mirror mirror) {
        this.mirror = mirror;
        return this;
    }

    /**
     * The mirror a structure will place with.
     *
     * @return Mirror to place with.
     */
    public Mirror getMirror() {
        return mirror;
    }

    /**
     * Set the palette used for placing a structure.
     *
     * @param palette The palette index of the structure to use, starting at
     *                {@code 0}, or {@code -1} to pick a random palette.
     * @return This PlacementOptions.
     */
    public PlacementOptions palette(int palette) {
        this.palette = palette;
        return this;
    }

    /**
     * The palette used for placing a structure.
     *
     * @return Palette used for placing a structure.
     */
    public int getPalette() {
        return palette;
    }

    /**
     * Set the integrity used for placing a structure.
     *
     * @param integrity Determines how damaged the building should look by
     *                  randomly skipping blocks to place. This value can range from 0 to 1. With
     *                  0 removing all blocks and 1 spawning the structure in pristine condition.
     * @return This PlacementOptions
     */
    public PlacementOptions integrity(float integrity) {
        Preconditions.checkArgument(integrity >= 0F && integrity <= 1F, "Integrity value (%S) must be between 0 and 1 inclusive", integrity);
        this.integrity = integrity;
        return this;
    }

    /**
     * The integrity used for placing a structure.
     *
     * @return Integrity used for placing a structure.
     */
    public float getIntegrity() {
        return integrity;
    }

    /**
     * Set whether the structure will place with strict block placement.
     * <p>When true, blocks placed in the structure will not perform block updates.
     * ie: Water won't spread, fences won't connect to blocks.</p>
     *
     * @param strict Whether to restrict block updates.
     * @return This PlacementOptions
     */
    public PlacementOptions strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    /**
     * Whether the structure will place with strict block placement.
     *
     * @return Will place with strict block placement.
     */
    public boolean isStrict() {
        return strict;
    }

    /**
     * Set whether to apply waterlogging when placing a structure.
     *
     * @param applyWaterlogging Whether to apply waterlogging.
     * @return This PlacementOptions
     */
    public PlacementOptions applyWaterlogging(boolean applyWaterlogging) {
        this.applyWaterlogging = applyWaterlogging;
        return this;
    }

    /**
     * Whether waterlogging will be applied when placing a structure.
     *
     * @return Waterlogging will apply.
     */
    public boolean isApplyWaterlogging() {
        return applyWaterlogging;
    }

    /**
     * The random assigned to this PlacementOptions.
     *
     * @return Random assigned.
     */
    public Random getRandom() {
        return random;
    }

}
