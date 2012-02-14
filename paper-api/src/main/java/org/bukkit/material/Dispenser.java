package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a dispenser.
 */
public class Dispenser extends FurnaceAndDispenser {

    public Dispenser() {
        super(Material.DISPENSER);
    }

    public Dispenser(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public Dispenser(final int type) {
        super(type);
    }

    public Dispenser(final Material type) {
        super(type);
    }

    public Dispenser(final int type, final byte data) {
        super(type, data);
    }

    public Dispenser(final Material type, final byte data) {
        super(type, data);
    }

    @Override
    public Dispenser clone() {
        return (Dispenser) super.clone();
    }
}
