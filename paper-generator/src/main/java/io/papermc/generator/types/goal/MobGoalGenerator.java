package io.papermc.generator.types.goal;

import com.destroystokyo.paper.entity.ai.GoalKey;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeVariableName;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.papermc.generator.types.SimpleGenerator;
import io.papermc.generator.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.Javadocs;
import io.papermc.typewriter.util.ClassHelper;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.jspecify.annotations.NullMarked;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class MobGoalGenerator extends SimpleGenerator {

    private static final String CLASS_HEADER = Javadocs.getVersionDependentClassHeader("keys", "Mob Goals");

    public MobGoalGenerator(String className, String packageName) {
        super(className, packageName);
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeVariableName type = TypeVariableName.get("T", Mob.class);
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(this.className)
            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(com.destroystokyo.paper.entity.ai.Goal.class), type))
            .addModifiers(PUBLIC)
            .addTypeVariable(type)
            .addAnnotations(Annotations.CONSTANTS_HEADER)
            .addJavadoc(CLASS_HEADER);

        TypeName mobType = ParameterizedTypeName.get(ClassName.get(Class.class), type);

        ParameterSpec keyParam = ParameterSpec.builder(String.class, "key", FINAL).build();
        ParameterSpec typeParam = ParameterSpec.builder(mobType, "type", FINAL).build();
        MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create")
            .addModifiers(PRIVATE, STATIC)
            .addParameter(keyParam)
            .addParameter(typeParam)
            .addCode("return $T.of($N, $T.minecraft($N));", GoalKey.class, typeParam, NamespacedKey.class, keyParam)
            .addTypeVariable(type)
            .returns(ParameterizedTypeName.get(ClassName.get(GoalKey.class), type));

        List<Class<Goal>> classes;
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("net.minecraft").scan()) {
            classes = scanResult.getSubclasses(Goal.class.getName()).loadClasses(Goal.class);
        }

        Stream<GoalKey<Mob>> vanillaGoals = classes.stream()
            .filter(clazz -> !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()))
            .filter(clazz -> !clazz.isAnonymousClass() || ClassHelper.getTopLevelClass(clazz) != GoalSelector.class)
            .filter(clazz -> !WrappedGoal.class.equals(clazz)) // TODO - properly fix
            .map(MobGoalNames::getKey)
            .sorted(Comparator.<GoalKey<?>, String>comparing(o -> o.getEntityClass().getSimpleName())
                .thenComparing(vanillaGoalKey -> vanillaGoalKey.getNamespacedKey().getKey())
            );

        vanillaGoals.forEach(goalKey -> {
            String keyPath = goalKey.getNamespacedKey().getKey();
            String fieldName = Formatting.formatKeyAsField(keyPath);

            TypeName typedKey = ParameterizedTypeName.get(GoalKey.class, goalKey.getEntityClass());
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(typedKey, fieldName, PUBLIC, STATIC, FINAL)
                .initializer("$N($S, $T.class)", createMethod.build(), keyPath, goalKey.getEntityClass());
            typeBuilder.addField(fieldBuilder.build());
        });

        return typeBuilder.addMethod(createMethod.build()).build();
    }
}
