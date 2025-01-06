package io.papermc.generator.types.craftblockdata.property.converter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RotationConverter implements Converter<Integer, BlockFace> {

    private static final String DIRECTION_VAR = "dir";
    private static final String ANGLE_VAR = "angle";

    @Override
    public Property<Integer> getProperty() {
        return BlockStateProperties.ROTATION_16;
    }

    @Override
    public Class<BlockFace> getApiType() {
        return BlockFace.class;
    }

    @Override
    public void convertSetter(MethodSpec.Builder method, FieldSpec field, ParameterSpec parameter) {
        method.addStatement("$T $L = $N.getDirection()", Vector.class, DIRECTION_VAR, parameter);
        method.addStatement("$1T $2L = ($1T) -$3T.toDegrees($3T.atan2($4L.getX(), $4L.getZ()))", Float.TYPE, ANGLE_VAR, Math.class, DIRECTION_VAR);
        method.addStatement(this.rawSetExprent().formatted("$N", ANGLE_VAR), field, RotationSegment.class);
    }

    @Override
    public String rawSetExprent() {
        return "this.set(%s, $T.convertToSegment(%s))";
    }

    @Override
    public String rawGetExprent() {
        return "CraftBlockData.ROTATION_CYCLE[this.get(%s)]";
    }
}
