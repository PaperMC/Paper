package io.papermc.generator.types.craftblockdata.property.holder.converter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import io.papermc.generator.types.craftblockdata.property.converter.ConverterBase;
import io.papermc.generator.types.craftblockdata.property.holder.DataHolderType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface DataConverter {

    DataHolderType getType();

    void convertSetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter, ParameterSpec parameter);

    void convertGetter(ConverterBase childConverter, MethodSpec.Builder method, FieldSpec field, ParameterSpec indexParameter);
}
