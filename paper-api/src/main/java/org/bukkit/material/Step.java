package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents the different types of steps.
 * @author sunkid
 */
public class Step extends MaterialData {
    public Step(final int type) {
        super(type);
    }

    public Step(final Material type) {
        super(type);
    }

    public Step(final int type, final byte data) {
        super(type, data);
    }

    public Step(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current Material this step is made of
     *
     * @return Material of this step
     */
    public Material getMaterial() {
        switch ((int) getData()) {
        case 1:
            return Material.SANDSTONE;
        case 2:
            return Material.WOOD;
        case 3:
            return Material.COBBLESTONE;
        case 0:
        default:
            return Material.STONE;
        }
    }

    /**
     * Sets the material this step is made of
     *
     * @param material New material of this step
     */
    public void setMaterial(Material material) {
        switch (material) {
        case SANDSTONE:
            setData((byte) 0x1);
        case WOOD:
            setData((byte) 0x2);
        case COBBLESTONE:
            setData((byte) 0x3);
        case STONE:
        default:
            setData((byte) 0x0);
        }
    }
}
