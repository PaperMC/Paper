package io.papermc.generator.types.goal;

import com.destroystokyo.paper.entity.RangedEntity;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.lang.model.element.Modifier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Raider;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@DefaultQualifier(NonNull.class)
public class MobGoalGenerator extends SimpleGenerator {

    private static final String CLASS_HEADER = Javadocs.getVersionDependentClassHeader("Mob Goals");

    private static final DeprecatedEntry[] DEPRECATED_ENTRIES = {
        //<editor-fold defaultstate="collapsed" desc="legacy entries">
        new DeprecatedEntry(Vindicator.class, "vindicator_melee_attack", null, "1.20.2"),
        new DeprecatedEntry(Ravager.class, "ravager_melee_attack", null, "1.20.2"),
        new DeprecatedEntry(Rabbit.class, "evil_rabbit_attack", null, "1.20.2"),
        new DeprecatedEntry(PigZombie.class, "anger", "1.21", "1.16"),
        new DeprecatedEntry(PigZombie.class, "anger_other", "1.21", "1.16"),
        new DeprecatedEntry(Blaze.class, "blaze_fireball", "1.21", null),
        new DeprecatedEntry(Cat.class, "tempt_chance", "1.21", null),
        new DeprecatedEntry(Dolphin.class, "dolphin_play_with_items", "1.21", null),
        new DeprecatedEntry(Drowned.class, "drowned_goto_beach", "1.21", null),
        new DeprecatedEntry(Creature.class, "drowned_goto_water", "1.21", null),
        new DeprecatedEntry(Enderman.class, "enderman_pickup_block", "1.21", null),
        new DeprecatedEntry(Enderman.class, "enderman_place_block", "1.21", null),
        new DeprecatedEntry(Enderman.class, "player_who_looked_at_target", "1.21", null),
        new DeprecatedEntry(Evoker.class, "evoker_cast_spell", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_defend_trusted", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_faceplant", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_perch_and_search", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_sleep", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_seek_shelter", "1.21", null),
        new DeprecatedEntry(Fox.class, "fox_stalk_prey", "1.21", null),
        new DeprecatedEntry(Ghast.class, "ghast_attack_target", "1.21", null),
        new DeprecatedEntry(Ghast.class, "ghast_idle_move", "1.21", null),
        new DeprecatedEntry(Ghast.class, "ghast_move_towards_target", "1.21", null),
        new DeprecatedEntry(Spellcaster.class, "spellcaster_cast_spell", "1.21", null),
        new DeprecatedEntry(TraderLlama.class, "llamatrader_defended_wandering_trader", "1.21", null),
        new DeprecatedEntry(Panda.class, "panda_hurt_by_target", "1.21", null),
        new DeprecatedEntry(PolarBear.class, "polarbear_attack_players", "1.21", null),
        new DeprecatedEntry(PolarBear.class, "polarbear_hurt_by", "1.21", null),
        new DeprecatedEntry(PolarBear.class, "polarbear_melee", "1.21", null),
        new DeprecatedEntry(PolarBear.class, "polarbear_panic", "1.21", null),
        new DeprecatedEntry(Rabbit.class, "eat_carrots", "1.21", null),
        new DeprecatedEntry(Rabbit.class, "killer_rabbit_melee_attack", "1.21", null),
        new DeprecatedEntry(Rabbit.class, "rabbit_avoid_target", "1.21", null),
        new DeprecatedEntry(Raider.class, "raider_hold_ground", "1.21", null),
        new DeprecatedEntry(Raider.class, "raider_obtain_banner", "1.21", null),
        new DeprecatedEntry(Shulker.class, "shulker_defense", "1.21", null),
        new DeprecatedEntry(Shulker.class, "shulker_nearest", "1.21", null),
        new DeprecatedEntry(Silverfish.class, "silverfish_hide_in_block", "1.21", null),
        new DeprecatedEntry(Silverfish.class, "silverfish_wake_others", "1.21", null),
        new DeprecatedEntry(Slime.class, "slime_idle", "1.21", null),
        new DeprecatedEntry(Slime.class, "slime_nearest_player", "1.21", null),
        new DeprecatedEntry(Slime.class, "slime_random_jump", "1.21", null),
        new DeprecatedEntry(Spider.class, "spider_melee_attack", "1.21", null),
        new DeprecatedEntry(Spider.class, "spider_nearest_attackable_target", "1.21", null),
        new DeprecatedEntry(Squid.class, "squid", "1.21", null),
        new DeprecatedEntry(Turtle.class, "turtle_goto_water", "1.21", null),
        new DeprecatedEntry(Turtle.class, "turtle_tempt", "1.21", null),
        new DeprecatedEntry(Vex.class, "vex_copy_target_of_owner", "1.21", null),
        new DeprecatedEntry(WanderingTrader.class, "villagertrader_wander_to_position", "1.21", null),
        new DeprecatedEntry(RangedEntity.class, "arrow_attack", "1.21", null),
        new DeprecatedEntry(Creature.class, "avoid_target", "1.21", null),
        new DeprecatedEntry(Monster.class, "bow_shoot", "1.21", null),
        new DeprecatedEntry(Creature.class, "breath", "1.21", null),
        new DeprecatedEntry(Cat.class, "cat_sit_on_bed", "1.21", null),
        new DeprecatedEntry(Monster.class, "crossbow_attack", "1.21", null),
        new DeprecatedEntry(Mob.class, "door_open", "1.21", null),
        new DeprecatedEntry(Mob.class, "eat_tile", "1.21", null),
        new DeprecatedEntry(Fish.class, "fish_school", "1.21", null),
        new DeprecatedEntry(Mob.class, "follow_entity", "1.21", null),
        new DeprecatedEntry(SkeletonHorse.class, "horse_trap", "1.21", null),
        new DeprecatedEntry(Creature.class, "hurt_by_target", "1.21", null),
        new DeprecatedEntry(Cat.class, "jump_on_block", "1.21", null),
        new DeprecatedEntry(Mob.class, "leap_at_target", "1.21", null),
        new DeprecatedEntry(Llama.class, "llama_follow", "1.21", null),
        new DeprecatedEntry(Creature.class, "move_towards_target", "1.21", null),
        new DeprecatedEntry(Mob.class, "nearest_attackable_target", "1.21", null),
        new DeprecatedEntry(Raider.class, "nearest_attackable_target_witch", "1.21", null),
        new DeprecatedEntry(Creature.class, "nearest_village", "1.21", null),
        new DeprecatedEntry(Tameable.class, "owner_hurt_by_target", "1.21", null),
        new DeprecatedEntry(Tameable.class, "owner_hurt_target", "1.21", null),
        new DeprecatedEntry(Parrot.class, "perch", "1.21", null),
        new DeprecatedEntry(Raider.class, "raid", "1.21", null),
        new DeprecatedEntry(Creature.class, "random_fly", "1.21", null),
        new DeprecatedEntry(Mob.class, "random_lookaround", "1.21", null),
        new DeprecatedEntry(Creature.class, "random_stroll_land", "1.21", null),
        new DeprecatedEntry(Creature.class, "random_swim", "1.21", null),
        new DeprecatedEntry(Tameable.class, "random_target_non_tamed", "1.21", null),
        new DeprecatedEntry(Tameable.class, "sit", "1.21", null),
        new DeprecatedEntry(Creature.class, "stroll_village", "1.21", null),
        new DeprecatedEntry(AbstractHorse.class, "tame", "1.21", null),
        new DeprecatedEntry(Creature.class, "water", "1.21", null),
        new DeprecatedEntry(Dolphin.class, "water_jump", "1.21", null),
        new DeprecatedEntry(Creature.class, "stroll_village_golem", "1.21", null),
        new DeprecatedEntry(Mob.class, "universal_anger_reset", "1.21", null)
        //</editor-fold>
    };

    public MobGoalGenerator(final String keysClassName, final String pkg) {
        super(keysClassName, pkg);
    }

    @Override
    protected TypeSpec getTypeSpec() {
        TypeName clazzType = TypeName.get(Class.class)
            .annotated(Annotations.NOT_NULL);
        TypeName keyType = TypeName.get(String.class)
            .annotated(Annotations.NOT_NULL);

        MethodSpec.Builder createMethod = MethodSpec.methodBuilder("create")
            .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
            .addParameter(ParameterSpec.builder(keyType, "key", Modifier.FINAL)
                .build()
            )
            .addParameter(ParameterSpec.builder(clazzType, "clazz", Modifier.FINAL)
                .build()
            )
            .addCode("return $T.of(clazz, $T.minecraft(key));", GoalKey.class, NamespacedKey.class)
            .returns(ParameterizedTypeName.get(GoalKey.class).annotated(Annotations.NOT_NULL));


        TypeVariableName type = TypeVariableName.get("T", Mob.class);
        TypeSpec.Builder typeBuilder = TypeSpec.interfaceBuilder(this.className)
            .addSuperinterface(ParameterizedTypeName.get(ClassName.get(com.destroystokyo.paper.entity.ai.Goal.class), type))
            .addModifiers(Modifier.PUBLIC)
            .addTypeVariable(type)
            .addAnnotations(Annotations.CLASS_HEADER)
            .addJavadoc(CLASS_HEADER);


        List<Class<?>> classes;
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().whitelistPackages("net.minecraft").scan()) {
            classes = scanResult.getSubclasses(net.minecraft.world.entity.ai.goal.Goal.class.getName()).loadClasses();
        }

        List<VanillaGoalKey> vanillaNames = classes.stream()
            .filter(clazz -> !java.lang.reflect.Modifier.isAbstract(clazz.getModifiers()))
            .filter(clazz -> !WrappedGoal.class.equals(clazz)) // TODO - properly fix
            .map(goalClass -> new VanillaGoalKey(goalClass, MobGoalNames.getKey(goalClass.getName(), (Class<? extends Goal>) goalClass)))
            .filter((key) -> !MobGoalNames.isIgnored(key.key().getNamespacedKey().getKey()))
            .sorted(Comparator.<VanillaGoalKey, String>comparing(o -> o.key().getEntityClass().getSimpleName())
                .thenComparing(vanillaGoalKey -> vanillaGoalKey.key.getNamespacedKey().getKey())
            )
            .toList();


        for (final VanillaGoalKey vanillaGoalKey : vanillaNames) {
            GoalKey<?> value = vanillaGoalKey.key();
            TypeName typedKey = ParameterizedTypeName.get(GoalKey.class, value.getEntityClass());
            NamespacedKey key = value.getNamespacedKey();

            String keyPath = key.getKey();
            String fieldName = Formatting.formatKeyAsField(key);
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(typedKey, fieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .initializer("$N($S, $T.class)", createMethod.build(), keyPath, value.getEntityClass());
            typeBuilder.addField(fieldBuilder.build());
        }

        for (final DeprecatedEntry value : DEPRECATED_ENTRIES) {
            TypeName typedKey = ParameterizedTypeName.get(GoalKey.class, value.entity);
            NamespacedKey key = NamespacedKey.minecraft(value.entryName);

            String keyPath = key.getKey();
            String fieldName = Formatting.formatKeyAsField(key);
            FieldSpec.Builder fieldBuilder = FieldSpec.builder(typedKey, fieldName, Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                .addAnnotation(Annotations.deprecatedVersioned(value.removedVersion, value.removalVersion != null))
                .initializer("$N($S, $T.class)", createMethod.build(), keyPath, value.entity);

            if (value.removedVersion != null) {
                fieldBuilder.addJavadoc("Removed in $L", value.removedVersion);
            }
            if (value.removalVersion != null) {
                fieldBuilder.addAnnotation(Annotations.scheduledRemoval(value.removalVersion));
            }

            typeBuilder.addField(fieldBuilder.build());
        }

        return typeBuilder.addMethod(createMethod.build()).build();
    }

    @Override
    protected JavaFile.Builder file(JavaFile.Builder builder) {
        return builder
            .skipJavaLangImports(true);
    }

    record VanillaGoalKey(Class<?> clazz, GoalKey<?> key) {
    }

    record DeprecatedEntry(Class<?> entity, String entryName, @Nullable String removalVersion,
                           @Nullable String removedVersion) {

    }

}
