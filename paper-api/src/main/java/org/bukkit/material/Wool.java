package org.bukkit.material;

import org.bukkit.DyeColor;
import org.bukkit.Material;

/**
 * Represents a Wool/Cloth block
 */
public class Wool extends MaterialData implements Colorable {
    public Wool() {
        super(Material.WOOL);
    }

    public Wool(DyeColor color) {
        this();
        setColor(color);
    }

    public Wool(final int type) {
        super(type);
    }

    public Wool(final Material type) {
        super(type);
    }

    public Wool(final int type, final byte data) {
        super(type, data);
    }

    public Wool(final Material type, final byte data) {
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

    @Override
    public String toString() {
        return getColor() + " " + super.toString();
    }

    @Override
    public Wool clone() {
        return (Wool) super.clone();
    }
}
