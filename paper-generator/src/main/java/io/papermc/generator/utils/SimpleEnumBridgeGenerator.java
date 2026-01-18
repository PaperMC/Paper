package io.papermc.generator.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.types.SimpleGenerator;
import java.util.EnumSet;
import javax.lang.model.element.Modifier;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jspecify.annotations.NonNull;

@DefaultQualifier(NonNull.class)
public class SimpleEnumBridgeGenerator<B extends Enum<B>, V extends Enum<V>> extends SimpleGenerator {

    private final Class<B> bukkitClass;
    private final Class<V> vanillaClass;

    public SimpleEnumBridgeGenerator(Class<B> bukkit, Class<V> vanilla, String packageName) {
        super("Craft%sBridge".formatted(bukkit.getSimpleName()), packageName);
        this.bukkitClass = bukkit;
        this.vanillaClass = vanilla;
    }

    @Override
    protected TypeSpec getTypeSpec() {

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(this.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotations(Annotations.CONSTANTS_HEADER);

        MethodSpec fromVanilla = generateMethod(vanillaClass, bukkitClass);
        MethodSpec fromBukkit = generateMethod(bukkitClass, vanillaClass);
        typeBuilder.addMethod(fromVanilla);
        typeBuilder.addMethod(fromBukkit);
        return typeBuilder.build();
    }

    private <F extends Enum<F>, T extends Enum<T>> MethodSpec generateMethod(Class<F> from, Class<T> to) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("from")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(ClassName.get(to))
            .addParameter(ClassName.get(from), "from");

        CodeBlock.Builder coderblockBuilder = CodeBlock.builder()
            .add("return switch ($N) {\n", "from")
            .indent();

        for (F fromConstants : EnumSet.allOf(from)) {
            coderblockBuilder.add("case $T.$L -> $T.$L;\n",
                ClassName.get(from), fromConstants.name(),
                ClassName.get(to), fromConstants.name());
        }
        coderblockBuilder.unindent()
            .add("};\n");

        builder.addCode(coderblockBuilder.build());
        return builder.build();
    }


    @Override
    protected JavaFile.Builder file(final JavaFile.Builder builder) {
        return builder.skipJavaLangImports(true);
    }
}
