package io.papermc.generator.types.craftblockdata.property.converter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.papermc.generator.types.Types;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RotationConverter implements Converter<Integer> {

    private static final String DIRECTION_VAR = "dir";
    private static final String ANGLE_VAR = "angle";

    @Override
    public Property<Integer> getProperty() {
        return BlockStateProperties.ROTATION_16;
    }

    @Override
    public TypeName getApiType() {
        return Types.BLOCK_FACE;
    }

    @Override
    public void convertSetter(MethodSpec.Builder method, FieldSpec field, ParameterSpec parameter) {
        method.addStatement("$T $L = $N.getDirection()", Types.VECTOR, DIRECTION_VAR, parameter);
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
