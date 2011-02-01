
package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

/**
 * Represents dye
 */
public class Dye extends MaterialData {
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
        return DyeColor.getByData(getData());
    }

    /**
     * Sets the color of this dye
     *
     * @param color New color of this dye
     */
    public void setColor(DyeColor color) {
        setData(color.getData());
    }
}
