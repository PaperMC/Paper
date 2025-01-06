package io.papermc.generator.types.craftblockdata.property.appender;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rail;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PropertyAppenders {

    private static final Map<Property<?>, AppenderBase> APPENDERS = Stream.of(
        new EnumValuesAppender<>(BlockStateProperties.AXIS, Axis.class, "getAxes"),
        new EnumValuesAppender<>(BlockStateProperties.HORIZONTAL_AXIS, Axis.class, "getAxes"),
        new EnumValuesAppender<>(BlockStateProperties.FACING, BlockFace.class, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.HORIZONTAL_FACING, BlockFace.class, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.FACING_HOPPER, BlockFace.class, "getFaces"),
        new EnumValuesAppender<>(BlockStateProperties.RAIL_SHAPE, Rail.Shape.class, "getShapes"),
        new EnumValuesAppender<>(BlockStateProperties.RAIL_SHAPE_STRAIGHT, Rail.Shape.class, "getShapes"),
        new EnumValuesAppender<>(BlockStateProperties.VERTICAL_DIRECTION, BlockFace.class, "getVerticalDirections")
    ).collect(Collectors.toUnmodifiableMap(PropertyAppender::getProperty, key -> key));

    public static void ifPresent(Property<?> property, Consumer<AppenderBase> callback) {
        if (APPENDERS.containsKey(property)) {
            callback.accept(APPENDERS.get(property));
        }
    }
}
