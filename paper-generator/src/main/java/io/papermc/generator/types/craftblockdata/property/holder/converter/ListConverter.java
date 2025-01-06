package io.papermc.generator.types.craftblockdata.property.holder.converter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import io.papermc.generator.types.craftblockdata.property.EnumPropertyWriter;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ListConverter implements DataConverter {

    @Override
    public DataHolderType getType() {
        return DataHolderType.LIST;
    }

    @Override
    public void convertSetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter, ParameterSpec parameter) {
        method.addStatement(childConverter.rawSetExprent().formatted("$N.get($N)"), field, indexParameter, parameter);
    }

    @Override
    public void convertGetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter) {
        if (childConverter instanceof EnumPropertyWriter<?> enumConverter) {
            method.addStatement("return " + childConverter.rawGetExprent().formatted("$N.get($N)"), field, indexParameter, enumConverter.getApiType());
        } else {
            method.addStatement("return " + childConverter.rawGetExprent().formatted("$N.get($N)"), field, indexParameter);
        }
    }
}
