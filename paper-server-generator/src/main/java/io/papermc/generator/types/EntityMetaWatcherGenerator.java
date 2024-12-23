package io.papermc.generator.types;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.ReflectionHelper;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.world.entity.Entity;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class EntityMetaWatcherGenerator extends SimpleGenerator {

    private static final ParameterizedTypeName GENERIC_ENTITY_DATA_SERIALIZER = ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Long.class), ParameterizedTypeName.get(ClassName.get(EntityDataSerializer.class), WildcardTypeName.subtypeOf(Object.class)));
    private static final ParameterizedTypeName ENTITY_CLASS = ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Entity.class));
    private static final ParameterizedTypeName OUTER_MAP_TYPE = ParameterizedTypeName.get(ClassName.get(Map.class), ENTITY_CLASS, GENERIC_ENTITY_DATA_SERIALIZER);

    public EntityMetaWatcherGenerator(String className, String packageName) {
        super(className, packageName);
    }

    @Override
    protected TypeSpec getTypeSpec() {
        Map<EntityDataSerializer<?>, String> dataAccessorStringMap = serializerMap();

        List<Class<?>> classes;
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("net.minecraft").scan()) {
            classes = scanResult.getSubclasses(net.minecraft.world.entity.Entity.class.getName()).loadClasses();
        }

        classes = classes.stream()
            .filter(clazz -> !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()))
            .toList();

        record Pair(Class<?> clazz, List<? extends EntityDataAccessor<?>> metaResults) {}

        final List<Pair> list = classes.stream()
            .map(clazz -> new Pair(
                clazz,
                    ReflectionHelper.getAllForAllParents(clazz, EntityMetaWatcherGenerator::doFilter)
                        .stream()
                        .map(this::createData)
                        .filter(Objects::nonNull)
                        .toList()
                )
            )
            .toList();

        Map<Class<?>, List<? extends EntityDataAccessor<?>>> vanillaNames = new TreeMap<>(Comparator.comparing(Class::getSimpleName));
        vanillaNames.putAll(list.stream()
            .collect(Collectors.toMap(pair -> pair.clazz, pair -> pair.metaResults)));

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(this.className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addAnnotations(Annotations.CLASS_HEADER);

        generateIdAccessorMethods(vanillaNames, dataAccessorStringMap, typeBuilder);
        generateClassToTypeMap(typeBuilder, vanillaNames.keySet());
        generateIsValidAccessorForEntity(typeBuilder);

        return typeBuilder.build();
    }

    private void generateIsValidAccessorForEntity(TypeSpec.Builder builder) {
        var methodBuilder = MethodSpec.methodBuilder("isValidForClass")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
            .returns(boolean.class)
            .addParameter(ParameterizedTypeName.get(ClassName.get(Class.class), WildcardTypeName.subtypeOf(Entity.class)), "clazz")
            .addParameter(ParameterizedTypeName.get(ClassName.get(EntityDataSerializer.class), WildcardTypeName.subtypeOf(Object.class)), "entityDataSerializer")
            .addParameter(int.class, "id")
            .addStatement("Map<Long, EntityDataSerializer<?>> serializerMap = VALID_ENTITY_META_MAP.get(clazz)")
            .beginControlFlow("if(serializerMap == null)")
            .addStatement("return false")
            .endControlFlow()
            .addStatement("var serializer = serializerMap.get(id)")
            .addStatement("return serializer != null && serializer == entityDataSerializer");

        builder.addMethod(methodBuilder.build());
    }

    private void generateClassToTypeMap(TypeSpec.Builder typeBuilder, Set<Class<?>> classes){
        typeBuilder.addField(
            FieldSpec.builder(OUTER_MAP_TYPE, "VALID_ENTITY_META_MAP", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .initializer("initialize()")
                .build()
        );

        MethodSpec.Builder builder = MethodSpec.methodBuilder("initialize")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
            .returns(OUTER_MAP_TYPE)
            .addStatement("$T result = new $T<>()", OUTER_MAP_TYPE, ClassName.get(HashMap.class));

        classes.forEach(aClass -> {
            String name = StringUtils.uncapitalize(aClass.getSimpleName());
            if(!name.isBlank()) {
                builder.addStatement("result.put($T.class, $L())", aClass, name);
            }
        });

        typeBuilder.addMethod(builder.addStatement("return $T.copyOf(result)", Map.class).build());
    }

    private static void generateIdAccessorMethods(Map<Class<?>, List<? extends EntityDataAccessor<?>>> vanillaNames, Map<EntityDataSerializer<?>, String> dataAccessorStringMap, TypeSpec.Builder typeBuilder) {
        for (final Map.Entry<Class<?>, List<? extends EntityDataAccessor<?>>> perClassResults : vanillaNames.entrySet()) {
            if (perClassResults.getKey().getSimpleName().isBlank()) {
                continue;
            }
            var simpleName = perClassResults.getKey().getSimpleName();

            ClassName hashMap = ClassName.get(HashMap.class);

            MethodSpec.Builder builder = MethodSpec.methodBuilder(StringUtils.uncapitalize(simpleName))
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                .returns(GENERIC_ENTITY_DATA_SERIALIZER)
                .addStatement("$T result = new $T<>()", GENERIC_ENTITY_DATA_SERIALIZER, hashMap);

            perClassResults.getValue().stream().sorted(Comparator.comparing(EntityDataAccessor::id)).forEach(result -> {
                builder.addStatement("result.put($LL, $T.$L)", result.id(), EntityDataSerializers.class, dataAccessorStringMap.get(result.serializer()));
            });

            var method = builder.addStatement("return $T.copyOf(result)", Map.class)
                .build();

            typeBuilder.addMethod(method);
        }
    }

    private @Nullable EntityDataAccessor<?> createData(Field field) {
        try {
            field.setAccessible(true);
             return (EntityDataAccessor<?>) field.get(null);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    private static boolean doFilter(Field field) {
        return java.lang.reflect.Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(EntityDataAccessor.class);
    }

    @Override
    protected JavaFile.Builder file(JavaFile.Builder builder) {
        return builder.skipJavaLangImports(true);
    }

    private Map<EntityDataSerializer<?>, String> serializerMap(){
        return Arrays.stream(EntityDataSerializers.class.getDeclaredFields())
            .filter(field -> field.getType() == EntityDataSerializer.class)
            .map(field -> {
                try {
                    return Map.entry((EntityDataSerializer<?>)field.get(0), field.getName());
                } catch (IllegalAccessException e) {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
