package io.papermc.paper.structure;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.EntityTransformer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.UnmodifiableView;
import org.jspecify.annotations.NullMarked;

/**
 * Represents options for placing a {@link Structure}.
 */
@NullMarked
public class PlacementOptions {

    private boolean includeEntities = true;
    private StructureRotation structureRotation = StructureRotation.NONE;
    private Mirror mirror = Mirror.NONE;
    private int palette = -1;
    private float integrity = 1;
    private final Random random;
    private boolean strict = false;
    private boolean applyWaterlogging = true;
    private Collection<BlockTransformer> blockTransformers = Collections.emptyList();
    private Collection<EntityTransformer> entityTransformers = Collections.emptyList();

    /**
     * Create a new set of placement options for placing a {@link Structure}.
     *
     * @param random The randomizer used for setting the structure's
     *               {@link org.bukkit.loot.LootTable LootTables} and integrity.
     */
    public static PlacementOptions placementOptions(Random random) {
        return new PlacementOptions(random);
    }

    private PlacementOptions(Random random) {
        this.random = random;
    }

    /**
     * Set whether to include entities when the structure is placed.
     * <p>Will default to true if not set.</p>
     *
     * @param includeEntities Whether to include entities.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions includeEntities(boolean includeEntities) {
        this.includeEntities = includeEntities;
        return this;
    }

    /**
     * Whether to include entities when the structure is placed.
     *
     * @return Whether to include entities.
     */
    public boolean includeEntities() {
        return includeEntities;
    }

    /**
     * Set the rotation a structure will place with.
     * <p>Will default to {@link StructureRotation#NONE} if not set.</p>
     *
     * @param structureRotation Rotation to place with.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions structureRotation(StructureRotation structureRotation) {
        this.structureRotation = structureRotation;
        return this;
    }

    /**
     * The rotation a structure will place with.
     *
     * @return Rotation to place with.
     */
    public StructureRotation structureRotation() {
        return structureRotation;
    }

    /**
     * Set the mirror a structure will place with.
     * <p>Will default to {@link Mirror#NONE} if not set</p>
     *
     * @param mirror Mirror to place with.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions mirror(Mirror mirror) {
        this.mirror = mirror;
        return this;
    }

    /**
     * The mirror a structure will place with.
     *
     * @return Mirror to place with.
     */
    public Mirror mirror() {
        return mirror;
    }

    /**
     * Set the palette used for placing a structure.
     * <p>Will default to {@code -1} if not set.</p>
     *
     * @param palette The palette index of the structure to use, starting at
     *                {@code 0}, or {@code -1} to pick a random palette.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions palette(int palette) {
        this.palette = palette;
        return this;
    }

    /**
     * The palette used for placing a structure.
     *
     * @return Palette used for placing a structure.
     */
    public int palette() {
        return palette;
    }

    /**
     * Set the integrity used for placing a structure.
     * <p>Will default to 1 if not set</p>
     *
     * @param integrity Determines how damaged the building should look by
     *                  randomly skipping blocks to place. This value can range from 0 to 1. With
     *                  0 removing all blocks and 1 spawning the structure in pristine condition.
     * @return This PlacementOptions
     */
    @Contract(value = "_ -> this", mutates = "this")
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
    public float integrity() {
        return integrity;
    }

    /**
     * Set whether the structure will place with strict block placement.
     * <p>When true, blocks placed in the structure will not perform block updates.
     * ie: Water won't spread, fences won't connect to blocks.</p>
     * <p>Will default to false if not set.</p>
     *
     * @param strict Whether to restrict block updates.
     * @return This PlacementOptions
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions strict(boolean strict) {
        this.strict = strict;
        return this;
    }

    /**
     * Whether the structure will place with strict block placement.
     *
     * @return Will place with strict block placement.
     */
    public boolean strict() {
        return strict;
    }

    /**
     * Set whether to apply waterlogging when placing a structure.
     * <p>Will default to true if not set.</p>
     *
     * @param applyWaterlogging Whether to apply waterlogging.
     * @return This PlacementOptions
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions applyWaterlogging(boolean applyWaterlogging) {
        this.applyWaterlogging = applyWaterlogging;
        return this;
    }

    /**
     * Whether waterlogging will be applied when placing a structure.
     *
     * @return Waterlogging will apply.
     */
    public boolean applyWaterlogging() {
        return applyWaterlogging;
    }

    /**
     * Set the collection of {@link BlockTransformer BlockTransformers} to apply to the structure.
     *
     * @param blockTransformers Collection of BlockTransformers.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions blockTransformers(Collection<BlockTransformer> blockTransformers) {
        this.blockTransformers = List.copyOf(blockTransformers);
        return this;
    }

    /**
     * Collection of {@link BlockTransformer BlockTransformers} to apply to the structure.
     *
     * @return BlockTransformers to apply.
     */
    public @UnmodifiableView Collection<BlockTransformer> blockTransformers() {
        return List.copyOf(blockTransformers);
    }

    /**
     * Set the collection of {@link EntityTransformer EntityTransformers} to apply to the structure.
     * <p>Will default to an empty list if not included.</p>
     *
     * @param entityTransformers EntityTransformers to apply.
     * @return This PlacementOptions.
     */
    @Contract(value = "_ -> this", mutates = "this")
    public PlacementOptions entityTransformers(Collection<EntityTransformer> entityTransformers) {
        this.entityTransformers = List.copyOf(entityTransformers);
        return this;
    }

    /**
     * Collection of {@link EntityTransformer EntityTransformers} to apply to the structure.
     *
     * @return EntityTransformers to apply.
     */
    public @UnmodifiableView Collection<EntityTransformer> entityTransformers() {
        return List.copyOf(entityTransformers);
    }

    /**
     * The random assigned to this PlacementOptions.
     *
     * @return Random assigned.
     */
    public Random random() {
        return random;
    }

}
