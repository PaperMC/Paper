package io.papermc.generator.types.craftblockdata.property.holder.converter;

import com.google.common.base.Preconditions;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import io.papermc.generator.types.craftblockdata.property.EnumPropertyWriter;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import java.util.stream.Collectors;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MapConverter implements DataConverter {

    private static final String PROPERTY_VAR = "property";

    @Override
    public DataHolderType getType() {
        return DataHolderType.MAP;
    }

    @Override
    public void convertSetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter, ParameterSpec parameter) {
        method.addStatement("$T $L = $N.get($N)", ((ParameterizedTypeName) field.type).typeArguments.get(1), PROPERTY_VAR, field, indexParameter);
        method.addStatement("$T.checkArgument($N != null, $S, $N.keySet().stream().map($T::name).collect($T.joining($S)))",
            Preconditions.class, PROPERTY_VAR, "Invalid " + indexParameter.name + ", only %s are allowed!", field, Enum.class, Collectors.class, ", ");

        method.addStatement(childConverter.rawSetExprent().formatted("$L"), PROPERTY_VAR, parameter);
    }

    @Override
    public void convertGetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter) {
        method.addStatement("$T $L = $N.get($N)", ((ParameterizedTypeName) field.type).typeArguments.get(1), PROPERTY_VAR, field, indexParameter);
        method.addStatement("$T.checkArgument($N != null, $S, $N.keySet().stream().map($T::name).collect($T.joining($S)))",
            Preconditions.class, PROPERTY_VAR, "Invalid " + indexParameter.name + ", only %s are allowed!", field, Enum.class, Collectors.class, ", ");

        if (childConverter instanceof EnumPropertyWriter<?> enumConverter) {
            method.addStatement("return " + childConverter.rawGetExprent().formatted("$L"), PROPERTY_VAR, enumConverter.getApiType());
        } else {
            method.addStatement("return " + childConverter.rawGetExprent().formatted("$L"), PROPERTY_VAR);
        }
    }
}
