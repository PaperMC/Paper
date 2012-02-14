package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

/**
 * Represents dye
 */
public class Dye extends MaterialData implements Colorable {
    public Dye() {
        super(Material.INK_SACK);
    }

    public Dye(final int type) {
        super(type);
    }

    public Dye(final Material type) {
        super(type);
    }

    public Dye(final int type, final byte data) {
        super(type, data);
    }

    public Dye(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current color of this dye
     *
     * @return DyeColor of this dye
     */
    public DyeColor getColor() {
        return DyeColor.getByData((byte) (15 - getData()));
    }

    /**
     * Sets the color of this dye
     *
     * @param color New color of this dye
     */
    public void setColor(DyeColor color) {
        setData((byte) (15 - color.getData()));
    }

    @Override
    public String toString() {
        return getColor() + " DYE(" + getData() + ")";
    }

    @Override
    public Dye clone() {
        return (Dye) super.clone();
    }
}
