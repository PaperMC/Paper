package io.papermc.paper.block.property;

import java.util.Collection;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public final class PaperBlockProperties {

    public static void setup() {
        //<editor-fold desc="Paper API Properties Registration" defaultstate="collapsed">
        // Start generate - PaperBlockProperties
        register(BlockStateProperties.AGE_1, BlockProperties.AGE_1);
        register(BlockStateProperties.AGE_2, BlockProperties.AGE_2);
        register(BlockStateProperties.AGE_3, BlockProperties.AGE_3);
        register(BlockStateProperties.AGE_4, BlockProperties.AGE_4);
        register(BlockStateProperties.AGE_5, BlockProperties.AGE_5);
        register(BlockStateProperties.AGE_7, BlockProperties.AGE_7);
        register(BlockStateProperties.AGE_15, BlockProperties.AGE_15);
        register(BlockStateProperties.AGE_25, BlockProperties.AGE_25);
        register(BlockStateProperties.AXIS, BlockProperties.AXIS);
        register(BlockStateProperties.CHEST_TYPE, BlockProperties.CHEST_TYPE);
        register(BlockStateProperties.DISTANCE, BlockProperties.DISTANCE);
        register(BlockStateProperties.DOUBLE_BLOCK_HALF, BlockProperties.DOUBLE_BLOCK_HALF);
        register(BlockStateProperties.EAST, BlockProperties.EAST);
        register(BlockStateProperties.EAST_REDSTONE, BlockProperties.EAST_REDSTONE);
        register(BlockStateProperties.EAST_WALL, BlockProperties.EAST_WALL);
        register(BlockStateProperties.FACING, BlockProperties.FACING);
        register(BlockStateProperties.FACING_HOPPER, BlockProperties.FACING_HOPPER);
        register(BlockStateProperties.HALF, BlockProperties.HALF);
        register(BlockStateProperties.HORIZONTAL_AXIS, BlockProperties.HORIZONTAL_AXIS);
        register(BlockStateProperties.HORIZONTAL_FACING, BlockProperties.HORIZONTAL_FACING);
        register(BlockStateProperties.LEVEL, BlockProperties.LEVEL);
        register(BlockStateProperties.LEVEL_CAULDRON, BlockProperties.LEVEL_CAULDRON);
        register(BlockStateProperties.LEVEL_COMPOSTER, BlockProperties.LEVEL_COMPOSTER);
        register(BlockStateProperties.LEVEL_FLOWING, BlockProperties.LEVEL_FLOWING);
        register(BlockStateProperties.MODE_COMPARATOR, BlockProperties.MODE_COMPARATOR);
        register(BlockStateProperties.NORTH, BlockProperties.NORTH);
        register(BlockStateProperties.NORTH_REDSTONE, BlockProperties.NORTH_REDSTONE);
        register(BlockStateProperties.NORTH_WALL, BlockProperties.NORTH_WALL);
        register(BlockStateProperties.PISTON_TYPE, BlockProperties.PISTON_TYPE);
        register(BlockStateProperties.RAIL_SHAPE, BlockProperties.RAIL_SHAPE);
        register(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockProperties.RAIL_SHAPE_STRAIGHT);
        register(BlockStateProperties.SLAB_TYPE, BlockProperties.SLAB_TYPE);
        register(BlockStateProperties.SOUTH, BlockProperties.SOUTH);
        register(BlockStateProperties.SOUTH_REDSTONE, BlockProperties.SOUTH_REDSTONE);
        register(BlockStateProperties.SOUTH_WALL, BlockProperties.SOUTH_WALL);
        register(BlockStateProperties.STABILITY_DISTANCE, BlockProperties.STABILITY_DISTANCE);
        register(BlockStateProperties.STAIRS_SHAPE, BlockProperties.STAIRS_SHAPE);
        register(BlockStateProperties.STRUCTUREBLOCK_MODE, BlockProperties.STRUCTUREBLOCK_MODE);
        register(BlockStateProperties.TEST_BLOCK_MODE, BlockProperties.TEST_BLOCK_MODE);
        register(BlockStateProperties.WEST, BlockProperties.WEST);
        register(BlockStateProperties.WEST_REDSTONE, BlockProperties.WEST_REDSTONE);
        register(BlockStateProperties.WEST_WALL, BlockProperties.WEST_WALL);
        // End generate - PaperBlockProperties
        //</editor-fold>
    }

    private static void register(final Property<?> nmsProperty, final BlockProperty<?> paperProperty) {
        CraftBlockData.DATA_PROPERTY_CACHE_MAP.put(nmsProperty, paperProperty);
    }


    public static BlockProperty<?> convertToPaperProperty(final Property<?> nmsProperty) {
        return CraftBlockData.DATA_PROPERTY_CACHE_MAP.computeIfAbsent(nmsProperty, prop -> {
            final Collection<BlockProperty<?>> properties = BlockProperties.PROPERTIES.get(prop.getName());
            if (properties.size() == 1) {
                return properties.iterator().next();
            } else {
                throw new IllegalArgumentException(nmsProperty + " should already be present in DATA_PROPERTY_CACHE_MAP");
            }
        });
    }

    public static Property<?> convertToNmsProperty(final BlockProperty<?> paperProperty) {
        return CraftBlockData.DATA_PROPERTY_CACHE_MAP.inverse().computeIfAbsent(paperProperty, prop -> {
            final Collection<Property<?>> properties = Property.PROPERTY_MULTIMAP.get(prop.name());
            if (properties.size() == 1) {
                return properties.iterator().next();
            } else {
                throw new IllegalArgumentException(paperProperty + " should already be present in DATA_PROPERTY_CACHE_MAP");
            }
        });
    }
}
