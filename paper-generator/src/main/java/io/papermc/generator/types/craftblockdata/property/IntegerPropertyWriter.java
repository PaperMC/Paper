package io.papermc.generator.types.craftblockdata.property;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.craftblockdata.CraftBlockDataGenerator;
import io.papermc.generator.types.craftblockdata.property.converter.Converters;
import io.papermc.generator.utils.NamingManager;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class IntegerPropertyWriter extends PropertyWriter<Integer> {

    protected IntegerPropertyWriter(IntegerProperty property) {
        super(property);
    }

    @Override
    public void addExtras(TypeSpec.Builder builder, FieldSpec field, CraftBlockDataGenerator<?> generator, NamingManager naming) {
        if (Converters.has(this.property)) {
            return;
        }

        IntegerProperty property = (IntegerProperty) this.property;

        if (property.getPossibleValues().getFirst() != 0 || property.getName().equals(BlockStateProperties.LEVEL.getName())) { // special case (levelled: composter)
            MethodSpec.Builder methodBuilder = generator.createMethod(naming.getterName(name -> true).pre("Minimum").concat());
            methodBuilder.addStatement("return $N.min", field);
            methodBuilder.returns(this.getApiType());

            builder.addMethod(methodBuilder.build());
        }

        {
            MethodSpec.Builder methodBuilder = generator.createMethod(naming.getterName(name -> true).pre("Maximum").concat());
            methodBuilder.addStatement("return $N.max", field);
            methodBuilder.returns(this.getApiType());

            builder.addMethod(methodBuilder.build());
        }
    }
}
