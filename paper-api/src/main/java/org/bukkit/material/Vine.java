package org.bukkit.material;

import java.util.Arrays;
import java.util.EnumSet;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Represents a vine
 */
public class Vine extends MaterialData {
    private static final int VINE_NORTH = 0x4;
    private static final int VINE_EAST = 0x8;
    private static final int VINE_WEST = 0x2;
    private static final int VINE_SOUTH = 0x1;
    EnumSet<BlockFace> possibleFaces = EnumSet.of(BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH);

    public Vine() {
        super(Material.VINE);
    }

    public Vine(int type, byte data){
        super(type, data);
    }

    public Vine(byte data) {
        super(Material.VINE, data);
    }

    public Vine(BlockFace... faces) {
        this(EnumSet.copyOf(Arrays.asList(faces)));
    }

    public Vine(EnumSet<BlockFace> faces) {
        this((byte) 0);
        faces.retainAll(possibleFaces);

        byte data = 0;

        if (faces.contains(BlockFace.NORTH)) {
            data |= VINE_NORTH;
        }

        if (faces.contains(BlockFace.EAST)) {
            data |= VINE_EAST;
        }

        if (faces.contains(BlockFace.WEST)) {
            data |= VINE_WEST;
        }

        if (faces.contains(BlockFace.SOUTH)) {
            data |= VINE_SOUTH;
        }

        setData(data);
    }

    /**
     * Check if the vine is attached to the specified face of an adjacent block. You can
     * check two faces at once by passing eg {@link BlockFace#NORTH_EAST}.
     *
     * @param face The face to check.
     * @return Whether it is attached to that face.
     */
    public boolean isOnFace(BlockFace face) {
        switch (face) {
            case NORTH:
                return (getData() & VINE_NORTH) > 0;
            case EAST:
                return (getData() & VINE_EAST) > 0;
            case WEST:
                return (getData() & VINE_WEST) > 0;
            case SOUTH:
                return (getData() & VINE_SOUTH) > 0;
            case NORTH_EAST:
                return isOnFace(BlockFace.NORTH) && isOnFace(BlockFace.EAST);
            case NORTH_WEST:
                return isOnFace(BlockFace.NORTH) && isOnFace(BlockFace.WEST);
            case SOUTH_EAST:
                return isOnFace(BlockFace.SOUTH) && isOnFace(BlockFace.EAST);
            case SOUTH_WEST:
                return isOnFace(BlockFace.SOUTH) && isOnFace(BlockFace.WEST);
            case UP: // It's impossible to be accurate with this since it's contextual
                return true;
            default:
                return false;
        }
    }

    /**
     * Attach the vine to the specified face of an adjacent block.
     *
     * @param face The face to attach.
     */
    public void putOnFace(BlockFace face) {
        switch(face) {
            case NORTH:
                setData((byte) (getData() | VINE_NORTH));
                break;
            case EAST:
                setData((byte) (getData() | VINE_EAST));
                break;
            case WEST:
                setData((byte) (getData() | VINE_WEST));
                break;
            case SOUTH:
                setData((byte) (getData() | VINE_SOUTH));
                break;
            case NORTH_EAST:
                putOnFace(BlockFace.NORTH);
                putOnFace(BlockFace.EAST);
                break;
            case NORTH_WEST:
                putOnFace(BlockFace.NORTH);
                putOnFace(BlockFace.WEST);
                break;
            case SOUTH_EAST:
                putOnFace(BlockFace.SOUTH);
                putOnFace(BlockFace.EAST);
                break;
            case SOUTH_WEST:
                putOnFace(BlockFace.SOUTH);
                putOnFace(BlockFace.WEST);
                break;
            case UP:
                break;
            default:
                throw new IllegalArgumentException("Vines can't go on face " + face.toString());
        }
    }

    /**
     * Detach the vine from the specified face of an adjacent block.
     *
     * @param face The face to detach.
     */
    public void removeFromFace(BlockFace face) {
        switch(face) {
            case NORTH:
                setData((byte) (getData() &~ VINE_NORTH));
                break;
            case EAST:
                setData((byte) (getData() &~ VINE_EAST));
                break;
            case WEST:
                setData((byte) (getData() &~ VINE_WEST));
                break;
            case SOUTH:
                setData((byte) (getData() &~ VINE_SOUTH));
                break;
            case NORTH_EAST:
                removeFromFace(BlockFace.NORTH);
                removeFromFace(BlockFace.EAST);
                break;
            case NORTH_WEST:
                removeFromFace(BlockFace.NORTH);
                removeFromFace(BlockFace.WEST);
                break;
            case SOUTH_EAST:
                removeFromFace(BlockFace.SOUTH);
                removeFromFace(BlockFace.EAST);
                break;
            case SOUTH_WEST:
                removeFromFace(BlockFace.SOUTH);
                removeFromFace(BlockFace.WEST);
                break;
            case UP:
                break;
            default:
                throw new IllegalArgumentException("Vines can't go on face " + face.toString());
        }
    }

    @Override
    public String toString() {
        return "VINE";
    }

    @Override
    public Vine clone() {
        return (Vine) super.clone();
    }
}
