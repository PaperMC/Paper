package io.papermc.generator.types.craftblockdata.property.converter;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ConverterBase {

    Class<?> getApiType();

    default void convertSetter(MethodSpec.Builder method, FieldSpec field, ParameterSpec parameter) {
        method.addStatement(this.rawSetExprent().formatted("$N"), field, parameter);
    }

    String rawSetExprent(); // this go on two layers which can be hard to follow refactor?

    default void convertGetter(MethodSpec.Builder method, FieldSpec field) {
        method.addStatement("return " + this.rawGetExprent().formatted("$N"), field);
    }

    String rawGetExprent();
}
