package io.papermc.generator.types.craftblockdata.property.appender;

import io.papermc.generator.types.Types;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PropertyAppenders {

    private static final Map<Property<?>, AppenderBase> APPENDERS = Stream.of(
        new EnumValuesAppender<>(BlockStateProperties.AXIS, Types.AXIS, "getAxes"),
        new EnumValuesAppender<>(BlockStateProperties.HORIZONTAL_AXIS, Types.AXIS, "getAxes"),
        new EnumValuesAppender<>(BlockStateProperties.FACING, Types.BLOCK_FACE, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.HORIZONTAL_FACING, Types.BLOCK_FACE, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.FACING_HOPPER, Types.BLOCK_FACE, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.RAIL_SHAPE, Types.BLOCK_DATA_RAIL_SHAPE, "getShapes"),
        new EnumValuesAppender<>(BlockStateProperties.RAIL_SHAPE_STRAIGHT, Types.BLOCK_DATA_RAIL_SHAPE, "getShapes"),
        new EnumValuesAppender<>(BlockStateProperties.VERTICAL_DIRECTION, Types.BLOCK_FACE, "getVerticalDirections")
    ).collect(Collectors.toUnmodifiableMap(PropertyAppender::getProperty, key -> key));

    public static void ifPresent(Property<?> property, Consumer<AppenderBase> callback) {
        if (APPENDERS.containsKey(property)) {
            callback.accept(APPENDERS.get(property));
        }
    }
}
