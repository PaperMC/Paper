package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a pressure plate
 */
public class PressurePlate extends MaterialData implements PressureSensor {
    public PressurePlate() {
        super(Material.LEGACY_WOOD_PLATE);
    }

    public PressurePlate(Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public PressurePlate(Material type, byte data) {
        super(type, data);
    }

    public boolean isPressed() {
        return getData() == 0x1;
    }

    @Override
    public String toString() {
        return super.toString() + (isPressed() ? " PRESSED" : "");
    }

    @Override
    public PressurePlate clone() {
        return (PressurePlate) super.clone();
    }
}
