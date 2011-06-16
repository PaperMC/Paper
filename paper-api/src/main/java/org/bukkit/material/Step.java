package org.bukkit.material;

import java.util.HashSet;

import org.bukkit.Material;

/**
 * Represents the different types of steps.
 */
public class Step extends MaterialData {
    private static HashSet<Material> stepTypes = new HashSet<Material>();
    static {
        stepTypes.add(Material.SANDSTONE);
        stepTypes.add(Material.WOOD);
        stepTypes.add(Material.COBBLESTONE);
        stepTypes.add(Material.STONE);
    }

    public Step() {
        super(Material.STEP);
    }

    public Step(final int type) {
        super(type);
    }

    public Step(final Material type) {
        super((stepTypes.contains(type)) ? Material.STEP : type);
        if (stepTypes.contains(type)) {
            setMaterial(type);
        }
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
            break;

        case WOOD:
            setData((byte) 0x2);
            break;

        case COBBLESTONE:
            setData((byte) 0x3);
            break;

        case STONE:
        default:
            setData((byte) 0x0);
        }
    }

    @Override
    public String toString() {
        return getMaterial() + " " + super.toString();
    }
}
