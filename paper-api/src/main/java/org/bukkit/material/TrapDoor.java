
package org.bukkit.material;

import org.bukkit.Material;

/**
 * Represents a trap door
 */
public class TrapDoor extends MaterialData implements Redstone {
    public TrapDoor() {
        super(Material.TRAP_DOOR);
    }

    public TrapDoor(final int type) {
        super(type);
    }

    public TrapDoor(final Material type) {
        super(type);
    }

    public TrapDoor(final int type, final byte data) {
        super(type, data);
    }

    public TrapDoor(final Material type, final byte data) {
        super(type, data);
    }

    /**
     * Gets the current state of this Material, indicating if it's powered or
     * unpowered
     *
     * @return true if powered, otherwise false
     */
    public boolean isPowered() {
        return (getData() & 0x8) == 0x8;
    }

    @Override
    public String toString() {
        return super.toString() + "[powered=" + isPowered() + "]";
    }
}