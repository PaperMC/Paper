package org.bukkit.util;

import java.util.Objects;

import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;

/**
 * The hit result of a ray trace.
 * <p>
 * Only the hit position is guaranteed to always be available. The availability
 * of the other attributes depends on what got hit and on the context in which
 * the ray trace was performed.
 */
public class RayTraceResult {

    private final Vector hitPosition;

    private final Block hitBlock;
    private final BlockFace hitBlockFace;
    private final Entity hitEntity;

    private RayTraceResult(Vector hitPosition, Block hitBlock, BlockFace hitBlockFace, Entity hitEntity) {
        Validate.notNull(hitPosition, "Hit position is null!");
        this.hitPosition = hitPosition.clone();
        this.hitBlock = hitBlock;
        this.hitBlockFace = hitBlockFace;
        this.hitEntity = hitEntity;
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     */
    public RayTraceResult(Vector hitPosition) {
        this(hitPosition, null, null, null);
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitBlockFace the hit block face
     */
    public RayTraceResult(Vector hitPosition, BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, null);
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitBlock the hit block
     * @param hitBlockFace the hit block face
     */
    public RayTraceResult(Vector hitPosition, Block hitBlock, BlockFace hitBlockFace) {
        this(hitPosition, hitBlock, hitBlockFace, null);
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitEntity the hit entity
     */
    public RayTraceResult(Vector hitPosition, Entity hitEntity) {
        this(hitPosition, null, null, hitEntity);
    }

    /**
     * Creates a RayTraceResult.
     *
     * @param hitPosition the hit position
     * @param hitEntity the hit entity
     * @param hitBlockFace the hit block face
     */
    public RayTraceResult(Vector hitPosition, Entity hitEntity, BlockFace hitBlockFace) {
        this(hitPosition, null, hitBlockFace, hitEntity);
    }

    /**
     * Gets the exact position of the hit.
     *
     * @return a copy of the exact hit position
     */
    public Vector getHitPosition() {
        return hitPosition.clone();
    }

    /**
     * Gets the hit block.
     *
     * @return the hit block, or <code>null</code> if not available
     */
    public Block getHitBlock() {
        return hitBlock;
    }

    /**
     * Gets the hit block face.
     *
     * @return the hit block face, or <code>null</code> if not available
     */
    public BlockFace getHitBlockFace() {
        return hitBlockFace;
    }

    /**
     * Gets the hit entity.
     *
     * @return the hit entity, or <code>null</code> if not available
     */
    public Entity getHitEntity() {
        return hitEntity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + hitPosition.hashCode();
        result = prime * result + ((hitBlock == null) ? 0 : hitBlock.hashCode());
        result = prime * result + ((hitBlockFace == null) ? 0 : hitBlockFace.hashCode());
        result = prime * result + ((hitEntity == null) ? 0 : hitEntity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof RayTraceResult)) return false;
        RayTraceResult other = (RayTraceResult) obj;
        if (!hitPosition.equals(other.hitPosition)) return false;
        if (!Objects.equals(hitBlock, other.hitBlock)) return false;
        if (!Objects.equals(hitBlockFace, other.hitBlockFace)) return false;
        if (!Objects.equals(hitEntity, other.hitEntity)) return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RayTraceResult [hitPosition=");
        builder.append(hitPosition);
        builder.append(", hitBlock=");
        builder.append(hitBlock);
        builder.append(", hitBlockFace=");
        builder.append(hitBlockFace);
        builder.append(", hitEntity=");
        builder.append(hitEntity);
        builder.append("]");
        return builder.toString();
    }
}
