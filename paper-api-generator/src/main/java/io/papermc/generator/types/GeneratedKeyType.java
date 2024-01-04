package io.papermc.generator.types;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.Main;
import io.papermc.generator.utils.CollectingContext;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import net.kyori.adventure.key.Key;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.registries.UpdateOneTwentyOneRegistries;
import net.minecraft.resources.ResourceKey;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static io.papermc.generator.types.Annotations.EXPERIMENTAL_API_ANNOTATION;
import static io.papermc.generator.types.Annotations.NOT_NULL;
import static io.papermc.generator.types.Annotations.experimentalAnnotations;
import static java.util.Objects.requireNonNull;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@DefaultQualifier(NonNull.class)
public class GeneratedKeyType<T, A> implements SourceGenerator {

    private static final Map<ResourceKey<? extends Registry<?>>, RegistrySetBuilder.RegistryBootstrap<?>> EXPERIMENTAL_REGISTRY_ENTRIES = UpdateOneTwentyOneRegistries.BUILDER.entries.stream()
            .collect(Collectors.toMap(RegistrySetBuilder.RegistryStub::key, RegistrySetBuilder.RegistryStub::bootstrap));

    private static final Map<RegistryKey<?>, String> REGISTRY_KEY_FIELD_NAMES;
    static {
        final Map<RegistryKey<?>, String> map = new HashMap<>();
        try {
            for (final Field field : RegistryKey.class.getFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !Modifier.isFinal(field.getModifiers()) || field.getType() != RegistryKey.class) {
                    continue;
                }
                map.put((RegistryKey<?>) field.get(null), field.getName());
            }
            REGISTRY_KEY_FIELD_NAMES = Map.copyOf(map);
        } catch (final ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static final AnnotationSpec SUPPRESS_WARNINGS = AnnotationSpec.builder(SuppressWarnings.class)
        .addMember("value", "$S", "unused")
        .addMember("value", "$S", "SpellCheckingInspection")
        .build();
    private static final AnnotationSpec GENERATED_FROM = AnnotationSpec.builder(GeneratedFrom.class)
        .addMember("value", "$S", SharedConstants.getCurrentVersion().getName())
        .build();
    private static final String TYPE_JAVADOC = """
        Vanilla keys for {@link $T#$L}.
        
        @apiNote The fields provided here are a direct representation of
        what is available from the vanilla game source. They may be
        changed (including removals) on any Minecraft version
        bump, so cross-version compatibility is not provided on the
        same level as it is on most of the other API.
        """;
    private static final String FIELD_JAVADOC = """
        {@code $L}
        
        @apiNote This field is version-dependant and may be removed in future Minecraft versions
        """;
    private static final String CREATE_JAVADOC = """
        Creates a key for {@link $T} in a registry.
        
        @param key the value's key in the registry
        @return a new typed key
        """;

    private final String keysClassName;
    private final Class<A> apiType;
    private final String pkg;
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final RegistryKey<A> apiRegistryKey;
    private final boolean publicCreateKeyMethod;

    public GeneratedKeyType(final String keysClassName, final Class<A> apiType, final String pkg, final ResourceKey<? extends Registry<T>> registryKey, final RegistryKey<A> apiRegistryKey, final boolean publicCreateKeyMethod) {
        this.keysClassName = keysClassName;
        this.apiType = apiType;
        this.pkg = pkg;
        this.registryKey = registryKey;
        this.apiRegistryKey = apiRegistryKey;
        this.publicCreateKeyMethod = publicCreateKeyMethod;
    }

    private MethodSpec.Builder createMethod(final TypeName returnType) {
        final TypeName keyType = TypeName.get(Key.class).annotated(NOT_NULL);

        final ParameterSpec keyParam = ParameterSpec.builder(keyType, "key", FINAL).build();
        final MethodSpec.Builder create = MethodSpec.methodBuilder("create")
            .addModifiers(this.publicCreateKeyMethod ? PUBLIC : PRIVATE, STATIC)
            .addParameter(keyParam)
            .addCode("return $T.create($T.$L, $N);", TypedKey.class, RegistryKey.class, requireNonNull(REGISTRY_KEY_FIELD_NAMES.get(this.apiRegistryKey), "Missing field for " + this.apiRegistryKey), keyParam)
            .returns(returnType.annotated(NOT_NULL));
        if (this.publicCreateKeyMethod) {
            create.addAnnotation(EXPERIMENTAL_API_ANNOTATION); // TODO remove once not experimental
            create.addJavadoc(CREATE_JAVADOC, this.apiType);
        }
        return create;
    }

    private TypeSpec.Builder keyHolderType() {
        return classBuilder(this.keysClassName)
            .addModifiers(PUBLIC, FINAL)
            .addJavadoc(TYPE_JAVADOC, RegistryKey.class, REGISTRY_KEY_FIELD_NAMES.get(this.apiRegistryKey))
            .addAnnotation(SUPPRESS_WARNINGS).addAnnotation(GENERATED_FROM)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .build()
            );
    }

    protected TypeSpec createTypeSpec() {
        final TypeName typedKey = ParameterizedTypeName.get(TypedKey.class, this.apiType);

        final TypeSpec.Builder typeBuilder = this.keyHolderType();
        typeBuilder.addAnnotation(EXPERIMENTAL_API_ANNOTATION); // TODO experimental API
        final MethodSpec.Builder createMethod = this.createMethod(typedKey);

        final Registry<T> registry = Main.REGISTRY_ACCESS.registryOrThrow(this.registryKey);
        final List<ResourceKey<T>> experimental = this.collectExperimentalKeys(registry);

        boolean allExperimental = true;
        for (final T value : registry) {
            final ResourceKey<T> key = registry.getResourceKey(value).orElseThrow();
            final String keyPath = key.location().getPath();
            final String fieldName = keyPath.toUpperCase(Locale.ENGLISH).replaceAll("[.-/]", "_"); // replace invalid field name chars
            final FieldSpec.Builder fieldBuilder = FieldSpec.builder(typedKey, fieldName, PUBLIC, STATIC, FINAL)
                .initializer("$N(key($S))", createMethod.build(), keyPath)
                .addJavadoc(FIELD_JAVADOC, key.location().toString());
            if (experimental.contains(key)) {
                fieldBuilder.addAnnotations(experimentalAnnotations("update 1.21"));
            } else {
                allExperimental = false;
            }
            typeBuilder.addField(fieldBuilder.build());
        }
        if (allExperimental) {
            typeBuilder.addAnnotations(experimentalAnnotations("update 1.21"));
            createMethod.addAnnotations(experimentalAnnotations("update 1.21"));
        }
        return typeBuilder.addMethod(createMethod.build()).build();
    }

    @SuppressWarnings("unchecked")
    private List<ResourceKey<T>> collectExperimentalKeys(final Registry<T> registry) {
        final RegistrySetBuilder.@Nullable RegistryBootstrap<T> registryBootstrap = (RegistrySetBuilder.RegistryBootstrap<T>) EXPERIMENTAL_REGISTRY_ENTRIES.get(this.registryKey);
        if (registryBootstrap == null) {
            return Collections.emptyList();
        }
        final List<ResourceKey<T>> experimental = new ArrayList<>();
        final CollectingContext<T> context = new CollectingContext<>(experimental, registry);
        registryBootstrap.run(context);
        return experimental;
    }

    protected JavaFile createFile() {
        return JavaFile.builder(this.pkg, this.createTypeSpec())
            .skipJavaLangImports(true)
            .addStaticImport(Key.class, "key")
            .indent("    ")
            .build();
    }

    @Override
    public final String outputString() {
        return this.createFile().toString();
    }

    @Override
    public void writeToFile(final Path parent) throws IOException {
        final Path pkgDir = parent.resolve(this.pkg.replace('.', '/'));
        Files.createDirectories(pkgDir);
        Files.writeString(pkgDir.resolve(this.keysClassName + ".java"), this.outputString(), StandardCharsets.UTF_8);
    }
}
