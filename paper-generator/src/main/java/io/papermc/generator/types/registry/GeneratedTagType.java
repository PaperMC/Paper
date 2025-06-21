package io.papermc.generator.types.registry;

import com.squareup.javapoet.*;
import io.papermc.generator.Main;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.registry.RegistryIdentifiable;
import io.papermc.generator.types.SimpleGenerator;
import io.papermc.generator.types.Types;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.BasePackage;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.Javadocs;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static io.papermc.generator.utils.Annotations.EXPERIMENTAL_API_ANNOTATION;
import static io.papermc.generator.utils.Annotations.experimentalAnnotations;
import static javax.lang.model.element.Modifier.*;

@NullMarked
public class GeneratedTagType<T> extends SimpleGenerator implements RegistryIdentifiable<T> {

    private final RegistryEntry<T> entry;

    public GeneratedTagType(RegistryEntry<T> entry, String packageName) {
        super(entry.generatedClassPrefix().concat("Tags"), packageName);
        this.entry = entry;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.entry.getRegistryKey();
    }

    private MethodSpec.Builder fetchMethod(TypeName paramType, TypeName returnType) {
        ParameterSpec keyParam = ParameterSpec.builder(paramType, "tagKey", FINAL).build();
        return MethodSpec.methodBuilder("fetch")
            .addModifiers(PRIVATE, STATIC)
            .addParameter(keyParam)
            .addCode("return $T.registryAccess().getRegistry($T.$L).getTag($N);", Types.REGISTRY_ACCESS, Types.REGISTRY_KEY, this.entry.registryKeyField(), keyParam)
            .returns(returnType);
    }

    private TypeSpec.Builder keyHolderType() {
        return classBuilder(this.className)
            .addModifiers(PUBLIC, FINAL)
            .addJavadoc(Javadocs.getVersionDependentClassHeader("tags", "{@link $T#$L}"), Types.REGISTRY_KEY, this.entry.registryKeyField())
            .addAnnotations(Annotations.CLASS_HEADER)
            .addMethod(MethodSpec.constructorBuilder()
                .addModifiers(PRIVATE)
                .build()
            );
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeName apiType = this.entry.data().api().klass().getType();

        TypeName tagKeyType = ParameterizedTypeName.get(Types.TAG_KEY, apiType);
        TypeName tagType = ParameterizedTypeName.get(Types.TAG, apiType);

        TypeSpec.Builder typeBuilder = this.keyHolderType();
        MethodSpec.Builder fetchMethod = this.fetchMethod(tagKeyType, tagType);

        AtomicBoolean allExperimental = new AtomicBoolean(true);
        ClassName tagKeyHolder = ClassName.get(BasePackage.PAPER.name() + ".registry.keys.tags", this.entry.generatedClassPrefix().concat("TagKeys"));
        this.entry.registry().listTagIds().sorted(Formatting.TAG_ORDER).forEach(tagKey -> {
            String fieldName = Formatting.formatKeyAsField(tagKey.location().getPath());
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(tagType, fieldName, PUBLIC, STATIC, FINAL)
                .initializer("$N($T.$L)", fetchMethod.build(), tagKeyHolder, fieldName)
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
            fetchMethod.addAnnotation(EXPERIMENTAL_API_ANNOTATION);
        }
        return typeBuilder.addMethod(fetchMethod.build()).build();
    }

    @Override
    protected JavaFile.Builder file(JavaFile.Builder builder) {
        return builder.addStaticImport(Types.KEY, "key");
    }
}
