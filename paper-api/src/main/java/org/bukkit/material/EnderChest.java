package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents an ender chest
 */
public class EnderChest extends DirectionalContainer {

    public EnderChest() {
        super(Material.ENDER_CHEST);
    }

    /**
     * Instantiate an ender chest facing in a particular direction.
     *
     * @param direction the direction the ender chest's lid opens towards
     */
    public EnderChest(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public EnderChest(final int type) {
        super(type);
    }

    public EnderChest(final Material type) {
        super(type);
    }

    public EnderChest(final int type, final byte data) {
        super(type, data);
    }

    public EnderChest(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public EnderChest clone() {
        return (EnderChest) super.clone();
    }
}
