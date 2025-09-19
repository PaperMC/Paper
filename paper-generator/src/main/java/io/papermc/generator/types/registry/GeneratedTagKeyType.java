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
import io.papermc.generator.Main;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.types.SimpleGenerator;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.Javadocs;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import java.util.concurrent.atomic.AtomicBoolean;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static io.papermc.generator.utils.Annotations.EXPERIMENTAL_API_ANNOTATION;
import static io.papermc.generator.utils.Annotations.experimentalAnnotations;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class GeneratedTagKeyType extends SimpleGenerator {

    private final RegistryEntry<?> entry;

    public GeneratedTagKeyType(RegistryEntry<?> entry, String packageName) {
        super(entry.keyClassName().concat("TagKeys"), packageName);
        this.entry = entry;
    }

    private MethodSpec.Builder createMethod(TypeName returnType) {
        boolean publicCreateKeyMethod = true; // tag lifecycle event exists

        ParameterSpec keyParam = ParameterSpec.builder(Key.class, "key", FINAL).build();
        MethodSpec.Builder create = MethodSpec.methodBuilder("create")
            .addModifiers(publicCreateKeyMethod ? PUBLIC : PRIVATE, STATIC)
            .addParameter(keyParam)
            .addCode("return $T.create($T.$L, $N);", TagKey.class, RegistryKey.class, this.entry.registryKeyField(), keyParam)
            .returns(returnType);
        if (publicCreateKeyMethod) {
            create.addJavadoc(Javadocs.CREATED_TAG_KEY_JAVADOC, this.entry.apiClass(), this.entry.registryKey().location().toString());
        }
        return create;
    }

    private TypeSpec.Builder keyHolderType() {
        return classBuilder(this.className)
            .addModifiers(PUBLIC, FINAL)
            .addJavadoc(Javadocs.getVersionDependentClassHeader("tag keys", "{@link $T#$L}"), RegistryKey.class, this.entry.registryKeyField())
            .addAnnotations(Annotations.CONSTANTS_HEADER)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .build()
            );
    }

    @Override
    protected TypeSpec getTypeSpec() {
        final TypeName apiType;
        if (this.entry.genericArgCount() > 0) {
            final TypeName[] args = new TypeName[this.entry.genericArgCount()];
            for (int i = 0; i < args.length; i++) {
                args[i] = WildcardTypeName.subtypeOf(Object.class);
            }
            apiType = ParameterizedTypeName.get(ClassName.get(this.entry.apiClass()), args);
        } else {
            apiType = ClassName.get(this.entry.apiClass());
        }
        TypeName tagKeyType = ParameterizedTypeName.get(ClassName.get(TagKey.class), apiType);

        TypeSpec.Builder typeBuilder = this.keyHolderType();
        MethodSpec.Builder createMethod = this.createMethod(tagKeyType);

        AtomicBoolean allExperimental = new AtomicBoolean(true);
        this.entry.registry().listTagIds().sorted(Formatting.TAG_ORDER).forEach(tagKey -> {
            String fieldName = Formatting.formatKeyAsField(tagKey.location().getPath());
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(tagKeyType, fieldName, PUBLIC, STATIC, FINAL)
                .initializer("$N(key($S))", createMethod.build(), tagKey.location().getPath())
                .addJavadoc(Javadocs.getVersionDependentField("{@code $L}"), "#" + tagKey.location());

            String featureFlagName = Main.EXPERIMENTAL_TAGS.get(tagKey);
            if (featureFlagName != null) {
                fieldBuilder.addAnnotations(experimentalAnnotations(SingleFlagHolder.fromName(featureFlagName)));
            } else {
                allExperimental.set(false);
            }
            typeBuilder.addField(fieldBuilder.build());
        });
        if (allExperimental.get()) {
            typeBuilder.addAnnotation(EXPERIMENTAL_API_ANNOTATION);
            createMethod.addAnnotation(EXPERIMENTAL_API_ANNOTATION);
        }
        return typeBuilder.addMethod(createMethod.build()).build();
    }

    @Override
    protected JavaFile.Builder file(JavaFile.Builder builder) {
        return builder.addStaticImport(Key.class, "key");
    }
}
