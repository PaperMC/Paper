package org.bukkit.material;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a pumpkin.
 *
 * @deprecated all usage of MaterialData is deprecated and subject to removal.
 * Use {@link org.bukkit.block.data.BlockData}.
 */
@Deprecated
public class Pumpkin extends MaterialData implements Directional {

    public Pumpkin() {
        super(Material.LEGACY_PUMPKIN);
    }

    /**
     * Instantiate a pumpkin facing in a particular direction.
     *
     * @param direction the direction the pumkin's face is facing
     */
    public Pumpkin(BlockFace direction) {
        this();
        setFacingDirection(direction);
    }

    public Pumpkin(final Material type) {
        super(type);
    }

    /**
     * @param type the type
     * @param data the raw data value
     * @deprecated Magic value
     */
    @Deprecated
    public Pumpkin(final Material type, final byte data) {
        super(type, data);
    }

    public boolean isLit() {
        return getItemType() == Material.LEGACY_JACK_O_LANTERN;
    }

    @Override
    public void setFacingDirection(BlockFace face) {
        byte data;

        switch (face) {
        case NORTH:
            data = 0x0;
            break;

        case EAST:
            data = 0x1;
            break;

        case SOUTH:
            data = 0x2;
            break;

        case WEST:
        default:
            data = 0x3;
        }

        setData(data);
    }

    @Override
    public BlockFace getFacing() {
        byte data = getData();

        switch (data) {
        case 0x0:
            return BlockFace.NORTH;

        case 0x1:
            return BlockFace.EAST;

        case 0x2:
            return BlockFace.SOUTH;

        case 0x3:
        default:
            return BlockFace.EAST;
        }
    }

    @Override
    public String toString() {
        return super.toString() + " facing " + getFacing() + " " + (isLit() ? "" : "NOT ") + "LIT";
    }

    @Override
    public Pumpkin clone() {
        return (Pumpkin) super.clone();
    }
}
