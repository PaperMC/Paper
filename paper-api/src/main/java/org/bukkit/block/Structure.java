package org.bukkit.block;

import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.block.structure.UsageMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockVector;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a structure block that can save and load blocks from a file. They
 * can only be used by OPs, and are not obtainable in survival.
 */
public interface Structure extends TileState {

    /**
     * The name of this structure.
     *
     * @return structure name
     */
    @NotNull
    String getStructureName();

    /**
     * Set the name of this structure. This is case-sensitive. The name of the
     * structure in the {@link UsageMode#SAVE} structure block MUST match the
     * name within the {@link UsageMode#CORNER} block or the size calculation
     * will fail.
     *
     * @param name the case-sensitive name of this structure
     */
    void setStructureName(@NotNull String name);

    /**
     * Get the name of who created this structure.
     *
     * @return the name of whoever created this structure.
     */
    @NotNull
    String getAuthor();

    /**
     * Set the name of whoever created this structure.
     *
     * @param author whoever created this structure (not empty)
     */
    void setAuthor(@NotNull String author);

    /**
     * Set the name of whoever created this structure using a
     * {@link LivingEntity}.
     *
     * @param livingEntity the entity who created this structure
     */
    void setAuthor(@NotNull LivingEntity livingEntity);

    /**
     * The relative position of the structure outline based on the position of
     * the structure block. Maximum allowed distance is 48 blocks in any
     * direction.
     *
     * @return a Location which contains the relative distance this structure is
     * from the structure block.
     */
    @NotNull
    BlockVector getRelativePosition();

    /**
     * Set the relative position from the structure block. Maximum allowed
     * distance is 48 blocks in any direction.
     *
     * @param vector the {@link BlockVector} containing the relative origin
     * coordinates of this structure.
     */
    void setRelativePosition(@NotNull BlockVector vector);

    /**
     * The distance to the opposite corner of this structure. The maximum
     * structure size is 48x48x48. When a structure has successfully been
     * calculated (i.e. it is within the maximum allowed distance) a white
     * border surrounds the structure.
     *
     * @return a {@link BlockVector} which contains the total size of the
     * structure.
     */
    @NotNull
    BlockVector getStructureSize();

    /**
     * Set the maximum size of this structure from the origin point. Maximum
     * allowed size is 48x48x48.
     *
     * @param vector the {@link BlockVector} containing the size of this
     * structure, based off of the origin coordinates.
     */
    void setStructureSize(@NotNull BlockVector vector);

    /**
     * Sets the mirroring of the structure.
     *
     * @param mirror the new mirroring method
     */
    void setMirror(@NotNull Mirror mirror);

    /**
     * How this structure is mirrored.
     *
     * @return the current mirroring method
     */
    @NotNull
    Mirror getMirror();

    /**
     * Set how this structure is rotated.
     *
     * @param rotation the new rotation
     */
    void setRotation(@NotNull StructureRotation rotation);

    /**
     * Get how this structure is rotated.
     *
     * @return the new rotation
     */
    @NotNull
    StructureRotation getRotation();

    /**
     * Set the {@link UsageMode} of this structure block.
     *
     * @param mode the new mode to set.
     */
    void setUsageMode(@NotNull UsageMode mode);

    /**
     * Get the {@link UsageMode} of this structure block.
     *
     * @return the mode this block is currently in.
     */
    @NotNull
    UsageMode getUsageMode();

    /**
     * While in {@link UsageMode#SAVE} mode, this will ignore any entities when
     * saving the structure.
     * <br>
     * While in {@link UsageMode#LOAD} mode this will ignore any entities that
     * were saved to file.
     *
     * @param ignoreEntities the flag to set
     */
    void setIgnoreEntities(boolean ignoreEntities);

    /**
     * Get if this structure block should ignore entities.
     *
     * @return true if the appropriate {@link UsageMode} should ignore entities.
     */
    boolean isIgnoreEntities();

    /**
     * Set if the structure outline should show air blocks.
     *
     * @param showAir if the structure block should show air blocks
     */
    void setShowAir(boolean showAir);

    /**
     * Check if this structure block is currently showing all air blocks
     *
     * @return true if the structure block is showing all air blocks
     */
    boolean isShowAir();

    /**
     * Set if this structure box should show the bounding box.
     *
     * @param showBoundingBox if the structure box should be shown
     */
    void setBoundingBoxVisible(boolean showBoundingBox);

    /**
     * Get if this structure block is currently showing the bounding box.
     *
     * @return true if the bounding box is shown
     */
    boolean isBoundingBoxVisible();

    /**
     * Set the integrity of the structure. Integrity must be between 0.0 and 1.0
     * Lower integrity values will result in more blocks being removed when
     * loading a structure. Integrity and {@link #getSeed()} are used together
     * to determine which blocks are randomly removed to mimic "decay."
     *
     * @param integrity the integrity of this structure
     */
    void setIntegrity(float integrity);

    /**
     * Get the integrity of this structure.
     *
     * @return the integrity of this structure
     */
    float getIntegrity();

    /**
     * The seed used to determine which blocks will be removed upon loading.
     * {@link #getIntegrity()} and seed are used together to determine which
     * blocks are randomly removed to mimic "decay."
     *
     * @param seed the seed used to determine how many blocks will be removed
     */
    void setSeed(long seed);

    /**
     * The seed used to determine how many blocks are removed upon loading of
     * this structure.
     *
     * @return the seed used
     */
    long getSeed();

    /**
     * Only applicable while in {@link UsageMode#DATA}. Metadata are specific
     * functions that can be applied to the structure location. Consult the
     * <a href="https://minecraft.gamepedia.com/Structure_Block#Data">Minecraft
     * wiki</a> for more information.
     *
     * @param metadata the function to perform on the selected location
     */
    void setMetadata(@NotNull String metadata);

    /**
     * Get the metadata function this structure block will perform when
     * activated. Consult the
     * <a href="https://minecraft.gamepedia.com/Structure_Block#Data">Minecraft
     * Wiki</a> for more information.
     *
     * @return the function that will be performed when this block is activated
     */
    @NotNull
    String getMetadata();
}
