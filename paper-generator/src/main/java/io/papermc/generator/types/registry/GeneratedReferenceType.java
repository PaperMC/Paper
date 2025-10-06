package io.papermc.generator.types.registry;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.types.SimpleGenerator;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import javax.lang.model.SourceVersion;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public abstract class GeneratedReferenceType<T> extends SimpleGenerator {

    public static class NonFiltered<T> extends GeneratedReferenceType<T> {

        public NonFiltered(String packageName, RegistryEntry<T> entry) {
            super(packageName, entry);
        }

        @Override
        public boolean isDisplayed(Holder.Reference<T> reference) {
            return true;
        }
    }

    public sealed interface MethodReference permits MethodReference.Identified, MethodReference.Built {

        void callFrom(FieldSpec.Builder fieldBuilder, Holder.Reference<?> item);

        static MethodReference wrap(MethodSpec.Builder builder) {
            return new Built(builder);
        }

        non-sealed class Identified implements MethodReference {
            private final Class<?> owner;
            protected final String name;

            public Identified(Class<?> owner, String name) {
                this.owner = owner;
                this.name = name;
            }

            @Override
            public void callFrom(FieldSpec.Builder fieldBuilder, Holder.Reference<?> item) {
                fieldBuilder.initializer("%s($S)".formatted(this.name), item.key().identifier().getPath());
            }

            public Class<?> owner() {
                return this.owner;
            }

            public String name() {
                return this.name;
            }
        }

        final class Built implements MethodReference {
            private final MethodSpec.Builder builder;
            private @Nullable MethodSpec cache;

            private Built(MethodSpec.Builder builder) {
                this.builder = builder;
            }

            public MethodSpec build(@Nullable TypeName returnType) {
                if (this.cache == null) {
                    this.builder.returns(returnType);
                    this.cache = this.builder.build();
                }
                return this.cache;
            }

            @Override
            public void callFrom(FieldSpec.Builder fieldBuilder, Holder.Reference<?> item) {
                fieldBuilder.initializer("$N($S)", this.build(null), item.key().identifier().getPath());
            }
        }
    }

    private final RegistryEntry<T> entry;
    private final MethodReference createMethod;

    public GeneratedReferenceType(String packageName, String className, RegistryEntry<T> entry) {
        super(className.concat("Ids"), packageName);
        this.entry = entry;
        this.createMethod = this.createMethod();
    }

    public GeneratedReferenceType(String packageName, RegistryEntry<T> entry) {
        this(packageName, entry.keyClassName(), entry);
    }

    protected MethodReference createMethod() {
        ParameterSpec keyParam = ParameterSpec.builder(String.class, "name", FINAL).build();
        return MethodReference.wrap(MethodSpec.methodBuilder("create")
            .addModifiers(PRIVATE, STATIC)
            .addParameter(keyParam)
            .addCode("return $T.create($T.$L, $T.withDefaultNamespace($N));", ResourceKey.class, Registries.class, this.entry.registryKeyField(), Identifier.class, keyParam));
    }

    private TypeSpec.Builder referenceHolderType() {
        return classBuilder(this.className)
            .addModifiers(PUBLIC, FINAL)
            .addAnnotations(Annotations.CONSTANTS_HEADER)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .build()
            );
    }

    public abstract boolean isDisplayed(Holder.Reference<T> reference);

    private TypeName fieldType() {
        if (this.createMethod instanceof MethodReference.Identified identified) {
            return ClassName.get(identified.owner());
        } else {
            if (EntityType.class.isAssignableFrom(this.entry.elementClass())) { // small hack for now
                return ParameterizedTypeName.get(
                    ClassName.get(ResourceKey.class),
                    ParameterizedTypeName.get(
                        ClassName.get(this.entry.elementClass()),
                        WildcardTypeName.subtypeOf(Object.class)
                    )
                );
            }
            return ParameterizedTypeName.get(ResourceKey.class, this.entry.elementClass());
        }
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeSpec.Builder typeBuilder = this.referenceHolderType();

        TypeName fieldType = this.fieldType();
        @Nullable MethodSpec explicitCreateMethod = null;
        if (this.createMethod instanceof MethodReference.Built built) {
            explicitCreateMethod = built.build(fieldType);
        }

        for (Holder.Reference<T> reference : this.entry.registry().listElements().filter(this::isDisplayed).sorted(Formatting.HOLDER_ORDER).toList()) {
            ResourceKey<T> key = reference.key();
            String fieldName = Formatting.formatKeyAsField(key.identifier().getPath());
            if (!SourceVersion.isIdentifier(fieldName) && this.entry.getFieldNames().containsKey(key)) {
                fieldName = this.entry.getFieldNames().get(key);
            }

            FieldSpec.Builder fieldBuilder = FieldSpec.builder(fieldType, fieldName, PUBLIC, STATIC, FINAL);
            this.createMethod.callFrom(fieldBuilder, reference);
            typeBuilder.addField(fieldBuilder.build());
        }

        if (explicitCreateMethod != null) {
            typeBuilder.addMethod(explicitCreateMethod);
        }
        return typeBuilder.build();
    }

    @Override
    protected void file(JavaFile.Builder builder) {
        if (this.createMethod instanceof MethodReference.Identified identified) {
            builder.addStaticImport(identified.owner(), identified.name());
        }
    }
}
