package io.papermc.generator.types;

import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.utils.Annotations;
import javax.lang.model.element.Modifier;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class SimpleEnumGenerator<E extends Enum<E>> extends SimpleGenerator {

    private final Class<E> enumClass;

    public SimpleEnumGenerator(Class<E> enumClass, String packageName) {
        super(enumClass.getSimpleName(), packageName);
        this.enumClass = enumClass;
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeSpec.Builder typeBuilder = TypeSpec.enumBuilder(this.enumClass.getSimpleName())
            .addModifiers(Modifier.PUBLIC)
            .addAnnotations(Annotations.CLASS_HEADER);

        for (E enumValue : this.enumClass.getEnumConstants()) {
            typeBuilder.addEnumConstant(enumValue.name());
        }

        return typeBuilder.build();
    }
}
