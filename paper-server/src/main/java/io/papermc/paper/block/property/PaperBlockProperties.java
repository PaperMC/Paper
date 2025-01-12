package io.papermc.paper.block.property;

import java.util.Collection;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

public final class PaperBlockProperties {

    public static void setup() {
        //<editor-fold desc="Paper API Properties Registration" defaultstate="collapsed">
        registerProp(BlockStateProperties.DOUBLE_BLOCK_HALF, BlockProperties.DOUBLE_BLOCK_HALF);
        registerProp(BlockStateProperties.HALF, BlockProperties.HALF);
        registerProp(BlockStateProperties.HORIZONTAL_AXIS, BlockProperties.HORIZONTAL_AXIS);
        registerProp(BlockStateProperties.AXIS, BlockProperties.AXIS);
        registerProp(BlockStateProperties.CHEST_TYPE, BlockProperties.CHEST_TYPE);
        registerProp(BlockStateProperties.PISTON_TYPE, BlockProperties.PISTON_TYPE);
        registerProp(BlockStateProperties.SLAB_TYPE, BlockProperties.SLAB_TYPE);
        registerProp(BlockStateProperties.MODE_COMPARATOR, BlockProperties.MODE_COMPARATOR);
        registerProp(BlockStateProperties.STRUCTUREBLOCK_MODE, BlockProperties.STRUCTUREBLOCK_MODE);
        registerProp(BlockStateProperties.TEST_BLOCK_MODE, BlockProperties.TEST_BLOCK_MODE);
        registerProp(BlockStateProperties.WEST, BlockProperties.WEST);
        registerProp(BlockStateProperties.WEST_WALL, BlockProperties.WEST_WALL);
        registerProp(BlockStateProperties.WEST_REDSTONE, BlockProperties.WEST_REDSTONE);
        registerProp(BlockStateProperties.EAST, BlockProperties.EAST);
        registerProp(BlockStateProperties.EAST_WALL, BlockProperties.EAST_WALL);
        registerProp(BlockStateProperties.EAST_REDSTONE, BlockProperties.EAST_REDSTONE);
        registerProp(BlockStateProperties.NORTH, BlockProperties.NORTH);
        registerProp(BlockStateProperties.NORTH_WALL, BlockProperties.NORTH_WALL);
        registerProp(BlockStateProperties.NORTH_REDSTONE, BlockProperties.NORTH_REDSTONE);
        registerProp(BlockStateProperties.SOUTH, BlockProperties.SOUTH);
        registerProp(BlockStateProperties.SOUTH_WALL, BlockProperties.SOUTH_WALL);
        registerProp(BlockStateProperties.SOUTH_REDSTONE, BlockProperties.SOUTH_REDSTONE);
        registerProp(BlockStateProperties.RAIL_SHAPE, BlockProperties.RAIL_SHAPE);
        registerProp(BlockStateProperties.RAIL_SHAPE_STRAIGHT, BlockProperties.RAIL_SHAPE_STRAIGHT);
        registerProp(BlockStateProperties.STAIRS_SHAPE, BlockProperties.STAIRS_SHAPE);
        registerProp(BlockStateProperties.LEVEL_CAULDRON, BlockProperties.LEVEL_CAULDRON);
        registerProp(BlockStateProperties.LEVEL_COMPOSTER, BlockProperties.LEVEL_COMPOSTER);
        registerProp(BlockStateProperties.LEVEL_FLOWING, BlockProperties.LEVEL_FLOWING);
        registerProp(BlockStateProperties.LEVEL, BlockProperties.LEVEL);
        registerProp(BlockStateProperties.DISTANCE, BlockProperties.DISTANCE);
        registerProp(BlockStateProperties.STABILITY_DISTANCE, BlockProperties.STABILITY_DISTANCE);
        registerProp(BlockStateProperties.FACING, BlockProperties.FACING);
        registerProp(BlockStateProperties.FACING_HOPPER, BlockProperties.FACING_HOPPER);
        registerProp(BlockStateProperties.HORIZONTAL_FACING, BlockProperties.HORIZONTAL_FACING);
        registerProp(BlockStateProperties.AGE_1, BlockProperties.AGE_1);
        registerProp(BlockStateProperties.AGE_2, BlockProperties.AGE_2);
        registerProp(BlockStateProperties.AGE_3, BlockProperties.AGE_3);
        registerProp(BlockStateProperties.AGE_4, BlockProperties.AGE_4);
        registerProp(BlockStateProperties.AGE_5, BlockProperties.AGE_5);
        registerProp(BlockStateProperties.AGE_7, BlockProperties.AGE_7);
        registerProp(BlockStateProperties.AGE_15, BlockProperties.AGE_15);
        registerProp(BlockStateProperties.AGE_25, BlockProperties.AGE_25);
        //</editor-fold>
    }

    private static void registerProp(final Property<?> nmsProperty, final BlockProperty<?> paperProperty) {
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
