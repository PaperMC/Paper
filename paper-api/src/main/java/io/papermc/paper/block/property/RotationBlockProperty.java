package io.papermc.paper.block.property;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Set;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.ApiStatus;

/**
 * Special exception for the {@link BlockProperties#ROTATION_16} property because
 * in the API it's represented as an enum, but stored as an integer.
 */
@ApiStatus.Internal
final class RotationBlockProperty extends EnumBlockPropertyImpl<BlockFace> implements AsIntegerProperty<BlockFace> {

    private static final Set<BlockFace> VALUES;

    static {
        final Set<BlockFace> values = new LinkedHashSet<>();
        for (final BlockFace face : BlockFace.values()) {
            if (face.getModY() == 0 && (face.getModZ() != 0 || face.getModX() != 0)) {
                values.add(face);
            }
        }
        Preconditions.checkArgument(values.size() == 16, "Expected 16 enum values");
        VALUES = Sets.immutableEnumSet(values);
    }

    private final BiMap<Integer, BlockFace> cache;

    RotationBlockProperty(final String name) {
        super(name, BlockFace.class, VALUES);
        this.cache = AsIntegerProperty.createCache(0xF, RotationBlockProperty::intToEnum);
    }

    @Override
    public BiMap<Integer, BlockFace> cache() {
        return this.cache;
    }

    private static BlockFace intToEnum(final int value) {
        return switch (value) {
            case 0x0 -> BlockFace.SOUTH;
            case 0x1 -> BlockFace.SOUTH_SOUTH_WEST;
            case 0x2 -> BlockFace.SOUTH_WEST;
            case 0x3 -> BlockFace.WEST_SOUTH_WEST;
            case 0x4 -> BlockFace.WEST;
            case 0x5 -> BlockFace.WEST_NORTH_WEST;
            case 0x6 -> BlockFace.NORTH_WEST;
            case 0x7 -> BlockFace.NORTH_NORTH_WEST;
            case 0x8 -> BlockFace.NORTH;
            case 0x9 -> BlockFace.NORTH_NORTH_EAST;
            case 0xA -> BlockFace.NORTH_EAST;
            case 0xB -> BlockFace.EAST_NORTH_EAST;
            case 0xC -> BlockFace.EAST;
            case 0xD -> BlockFace.EAST_SOUTH_EAST;
            case 0xE -> BlockFace.SOUTH_EAST;
            case 0xF -> BlockFace.SOUTH_SOUTH_EAST;
            default -> throw new IllegalArgumentException("Illegal value " + value);
        };
    }
}
