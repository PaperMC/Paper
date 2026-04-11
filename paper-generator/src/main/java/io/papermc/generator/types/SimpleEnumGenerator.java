package io.papermc.generator.types;

import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.utils.Annotations;
import javax.lang.model.element.Modifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SimpleEnumGenerator<T extends Enum<T>> extends SimpleGenerator {

    private final Class<T> enumClass;

    public SimpleEnumGenerator(Class<T> enumClass, String packageName) {
        super(enumClass.getSimpleName(), packageName);
        this.enumClass = enumClass;
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeSpec.Builder typeBuilder = TypeSpec.enumBuilder(this.enumClass.getSimpleName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotations(Annotations.CONSTANTS_HEADER);

        for (T enumValue : this.enumClass.getEnumConstants()) {
            typeBuilder.addEnumConstant(enumValue.name());
        }

        return typeBuilder.build();
    }
}
