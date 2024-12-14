package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Simple utility class for attachable MaterialData subclasses
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 * @since 1.0.0 R1
 */
@Deprecated(since = "1.13", forRemoval = true)
public abstract class SimpleAttachableMaterialData extends MaterialData implements Attachable {

    public SimpleAttachableMaterialData(Material type, BlockFace direction) {
        this(type);
        setFacingDirection(direction);
    }

    public SimpleAttachableMaterialData(Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    public SimpleAttachableMaterialData(Material type, byte data) {
        super(type, data);
    }

    @Override
    public BlockFace getFacing() {
        BlockFace attachedFace = getAttachedFace();
        return attachedFace == null ? null : attachedFace.getOppositeFace();
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing();
    }

    /**
     * @since 1.1.0 R5
     */
    @Override
    public SimpleAttachableMaterialData clone() {
        return (SimpleAttachableMaterialData) super.clone();
    }
}
