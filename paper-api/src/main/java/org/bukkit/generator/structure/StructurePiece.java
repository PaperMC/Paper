package org.bukkit.generator.structure;

import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an individual part of a {@link GeneratedStructure}.
 *
 * @see GeneratedStructure
 * @since 1.20.4
 */
public interface StructurePiece {

    /**
     * Gets the bounding box of this structure piece.
     *
     * @return bounding box of this structure piece
     */
    @NotNull
    public BoundingBox getBoundingBox();
}
