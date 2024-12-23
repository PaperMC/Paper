package io.papermc.generator.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import net.minecraft.world.entity.Entity;
import org.bukkit.entity.EntityType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class EntityTypeToEntityClassGenerator extends SimpleGenerator {

    private static final ParameterizedTypeName ENTITY_CLASS = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Entity.class));
    private static final ParameterizedTypeName OUTER_MAP_TYPE = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(EntityType.class), ENTITY_CLASS);


    public EntityTypeToEntityClassGenerator(String className, String packageName) {
        super(className, packageName);
    }


    @Override
    protected TypeSpec getTypeSpec() {

        final List<TypeToClass> typeToClasses = ReflectionHelper.forClass(net.minecraft.world.entity.EntityType.class, field ->
            java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                java.lang.reflect.Modifier.isPublic(field.getModifiers()) &&
                java.lang.reflect.Modifier.isFinal(field.getModifiers()) &&
                field.getType().isAssignableFrom(net.minecraft.world.entity.EntityType.class)
        ).stream().map(this::createFromField).toList();

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(this.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotations(Annotations.CLASS_HEADER);

        generateEntityTypeToEntityClassMap(typeBuilder, typeToClasses);
        generateGetClassByEntityType(typeBuilder);

        return typeBuilder.build();
    }

    private TypeToClass createFromField(Field field) {
        return new TypeToClass(field.getName(), (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0]);
    }

    record TypeToClass(String entityType, Class<?> className) {}

    private void generateGetClassByEntityType(TypeSpec.Builder builder) {
        var methodBuilder = MethodSpec.methodBuilder("getClassByEntityType")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(ENTITY_CLASS)
            .addParameter(ClassName.get(EntityType.class), "entityType")
            .addStatement("return ENTITY_TYPE_TO_CLASS.get(entityType)");

        builder.addMethod(methodBuilder.build());
    }

    private void generateEntityTypeToEntityClassMap(TypeSpec.Builder typeBuilder, List<TypeToClass> classes) {
        typeBuilder.addField(
            FieldSpec.builder(OUTER_MAP_TYPE, "ENTITY_TYPE_TO_CLASS", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("initialize()")
                .build()
        );

        MethodSpec.Builder builder = MethodSpec.methodBuilder("initialize")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .returns(OUTER_MAP_TYPE)
            .addStatement("$T result = new $T<>()", OUTER_MAP_TYPE, ClassName.get(HashMap.class));

        classes.forEach(aClass -> {
            builder.addStatement("result.put(EntityType.$L, $T.class)", aClass.entityType, aClass.className);
        });

        typeBuilder.addMethod(builder.addStatement("return $T.copyOf(result)", Map.class).build());
    }


    @Override
    protected JavaFile.Builder file(final JavaFile.Builder builder) {
        return builder.skipJavaLangImports(true);
    }
}
