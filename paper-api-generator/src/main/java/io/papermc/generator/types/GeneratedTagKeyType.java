package io.papermc.generator.types;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.papermc.generator.Main;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.Javadocs;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.key.Key;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.bukkit.MinecraftExperimental;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static io.papermc.generator.utils.Annotations.EXPERIMENTAL_API_ANNOTATION;
import static io.papermc.generator.utils.Annotations.experimentalAnnotations;
import static java.util.Objects.requireNonNull;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

public class GeneratedTagKeyType<T, A> extends SimpleGenerator {

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

    private static final String CREATE_JAVADOC = """
        Creates a tag key for {@link $T} in the registry {@code $L}.
        
        @param key the tag key's key
        @return a new tag key
        """;

    private final Class<A> apiType;
    private final ResourceKey<? extends Registry<T>> registryKey;
    private final RegistryKey<A> apiRegistryKey;
    private final boolean publicCreateKeyMethod;

    public GeneratedTagKeyType(final String keysClassName, final Class<A> apiType, final String pkg, final ResourceKey<? extends Registry<T>> registryKey, final RegistryKey<A> apiRegistryKey, final boolean publicCreateKeyMethod) {
        super(keysClassName, pkg);
        this.apiType = apiType;
        this.registryKey = registryKey;
        this.apiRegistryKey = apiRegistryKey;
        this.publicCreateKeyMethod = publicCreateKeyMethod;
    }

    private MethodSpec.Builder createMethod(final TypeName returnType) {
        final TypeName keyType = TypeName.get(Key.class);

        final ParameterSpec keyParam = ParameterSpec.builder(keyType, "key", FINAL).build();
        final MethodSpec.Builder create = MethodSpec.methodBuilder("create")
            .addModifiers(this.publicCreateKeyMethod ? PUBLIC : PRIVATE, STATIC)
            .addParameter(keyParam)
            .addCode("return $T.create($T.$L, $N);", TagKey.class, RegistryKey.class, requireNonNull(REGISTRY_KEY_FIELD_NAMES.get(this.apiRegistryKey), "Missing field for " + this.apiRegistryKey), keyParam)
            .returns(returnType);
        if (this.publicCreateKeyMethod) {
            create.addAnnotation(EXPERIMENTAL_API_ANNOTATION); // TODO remove once not experimental
            create.addJavadoc(CREATE_JAVADOC, this.apiType, this.registryKey.location().toString());
        }
        return create;
    }

    private TypeSpec.Builder keyHolderType() {
        return classBuilder(this.className)
            .addModifiers(PUBLIC, FINAL)
            .addJavadoc(Javadocs.getVersionDependentClassHeader("{@link $T#$L}"), RegistryKey.class, REGISTRY_KEY_FIELD_NAMES.get(this.apiRegistryKey))
            .addAnnotations(Annotations.CLASS_HEADER)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .build()
            );
    }

    @Override
    protected TypeSpec getTypeSpec() {
        final TypeName tagKey = ParameterizedTypeName.get(TagKey.class, this.apiType);

        final TypeSpec.Builder typeBuilder = this.keyHolderType();
        final MethodSpec.Builder createMethod = this.createMethod(tagKey);

        final Registry<T> registry = Main.REGISTRY_ACCESS.lookupOrThrow(this.registryKey);

        final AtomicBoolean allExperimental = new AtomicBoolean(true);
        registry.listTagIds().sorted(Formatting.alphabeticKeyOrder(nmsTagKey -> nmsTagKey.location().getPath())).forEach(nmsTagKey -> {
            final String fieldName = Formatting.formatKeyAsField(nmsTagKey.location().getPath());
            final FieldSpec.Builder fieldBuilder = FieldSpec.builder(tagKey, fieldName, PUBLIC, STATIC, FINAL)
                .initializer("$N(key($S))", createMethod.build(), nmsTagKey.location().getPath())
                .addJavadoc(Javadocs.getVersionDependentField("{@code $L}"), "#" + nmsTagKey.location());
            final String featureFlagName = Main.EXPERIMENTAL_TAGS.get(nmsTagKey);
            if (featureFlagName != null) {
                fieldBuilder.addAnnotations(experimentalAnnotations(MinecraftExperimental.Requires.valueOf(featureFlagName.toUpperCase(Locale.ENGLISH)))); // Update for Experimental API
            } else {
                allExperimental.set(false);
            }
            typeBuilder.addField(fieldBuilder.build());
        });
        if (allExperimental.get()) {
            typeBuilder.addAnnotations(experimentalAnnotations(null)); // Update for Experimental API
            createMethod.addAnnotations(experimentalAnnotations(null)); // Update for Experimental API
        } else {
            typeBuilder.addAnnotation(EXPERIMENTAL_API_ANNOTATION); // TODO experimental API
        }
        return typeBuilder.addMethod(createMethod.build()).build();
    }

    @Override
    protected JavaFile.Builder file(final JavaFile.Builder builder) {
        return builder
            .addStaticImport(Key.class, "key");
    }
}
