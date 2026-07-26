package io.papermc.generator.rewriter.types;

import io.papermc.typewriter.ClassNamed;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Types {

    public static final String BASE_PACKAGE = "org.bukkit.craftbukkit";

    public static final ClassNamed CRAFT_BLOCK_DATA = ClassNamed.of(BASE_PACKAGE + ".block.data", "CraftBlockData");

    public static final ClassNamed CRAFT_BLOCK_STATES = ClassNamed.of(BASE_PACKAGE + ".block", "CraftBlockStates");

    public static final ClassNamed CRAFT_STATISTIC = ClassNamed.of(BASE_PACKAGE, "CraftStatistic");

    public static final ClassNamed CRAFT_POTION_UTIL = ClassNamed.of(BASE_PACKAGE + ".potion", "CraftPotionUtil");

    public static final ClassNamed FIELD_RENAME = ClassNamed.of(BASE_PACKAGE + ".legacy", "FieldRename");

    public static final ClassNamed PAPER_REGISTRIES = ClassNamed.of("io.papermc.paper.registry", "PaperRegistries");

    public static final ClassNamed REGISTRY_MODIFICATION_API_SUPPORT = ClassNamed.of("io.papermc.paper.registry.entry", "RegistryEntryMeta", "RegistryModificationApiSupport");

    public static final ClassNamed PAPER_FEATURE_FLAG_PROVIDER_IMPL = ClassNamed.of("io.papermc.paper.world.flag", "PaperFeatureFlagProviderImpl");

    public static final ClassNamed PAPER_SIMPLE_REGISTRY = ClassNamed.of("io.papermc.paper.registry", "PaperSimpleRegistry");

    public static final ClassNamed MOB_GOAL_HELPER = ClassNamed.of("com.destroystokyo.paper.entity.ai", "MobGoalHelper");
}
