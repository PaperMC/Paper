package io.papermc.generator.rewriter.types;

import com.squareup.javapoet.ClassName;
import io.papermc.typewriter.ClassNamed;
import java.util.ArrayList;
import java.util.List;
import org.jspecify.annotations.NullMarked;

import static io.papermc.generator.utils.BasePackage.BUKKIT;
import static io.papermc.generator.utils.BasePackage.CRAFT_BUKKIT;
import static io.papermc.generator.utils.BasePackage.PAPER;
import static io.papermc.generator.utils.BasePackage.PAPER_LEGACY;

@NullMarked
public final class Types {

    public static final ClassNamed BUKKIT_CLASS = BUKKIT.rootClassNamed("Bukkit");

    public static final ClassNamed FEATURE_FLAG = BUKKIT.rootClassNamed("FeatureFlag");

    public static final ClassNamed REGISTRY = BUKKIT.rootClassNamed("Registry");

    public static final ClassNamed TAG = BUKKIT.rootClassNamed("Tag");

    public static final ClassNamed NAMESPACED_KEY = typed(io.papermc.generator.types.Types.NAMESPACED_KEY);

    public static final ClassNamed MINECRAFT_EXPERIMENTAL = typed(io.papermc.generator.types.Types.MINECRAFT_EXPERIMENTAL);
    public static final ClassNamed MINECRAFT_EXPERIMENTAL_REQUIRES = typed(io.papermc.generator.types.Types.MINECRAFT_EXPERIMENTAL_REQUIRES);

    @Deprecated
    public static final ClassNamed STATISTIC = BUKKIT.rootClassNamed("Statistic");

    public static final ClassNamed STATISTIC_TYPE = BUKKIT.rootClassNamed("Statistic", "Type");

    public static final ClassNamed BLOCK_TYPE_TYPED = BUKKIT.relativeClassNamed("block", "BlockType", "Typed");

    public static final ClassNamed ITEM_TYPE_TYPED = BUKKIT.relativeClassNamed("inventory", "ItemType", "Typed");

    public static final ClassNamed ITEM_META = BUKKIT.relativeClassNamed("inventory.meta", "ItemMeta");

    public static final ClassNamed LOCATION = BUKKIT.rootClassNamed("Location");

    public static final ClassNamed MATERIAL = BUKKIT.rootClassNamed("Material");

    public static final ClassNamed POSE = BUKKIT.relativeClassNamed("entity", "Pose");

    @Deprecated
    public static final ClassNamed VILLAGER = BUKKIT.relativeClassNamed("entity", "Villager");

    public static final ClassNamed SNIFFER_STATE = BUKKIT.relativeClassNamed("entity", "Sniffer", "State");

    public static final ClassNamed TROPICAL_FISH_PATTERN = BUKKIT.relativeClassNamed("entity", "TropicalFish", "Pattern");

    public static final ClassNamed FOX_TYPE = BUKKIT.relativeClassNamed("entity", "Fox", "Type");

    public static final ClassNamed SALMON_VARIANT = BUKKIT.relativeClassNamed("entity", "Salmon", "Variant");

    public static final ClassNamed ARMADILLO_STATE = BUKKIT.relativeClassNamed("entity", "Armadillo", "State");

    public static final ClassNamed PANDA_GENE = BUKKIT.relativeClassNamed("entity", "Panda", "Gene");

    public static final ClassNamed BOAT_STATUS = BUKKIT.relativeClassNamed("entity", "Boat", "Status");

    public static final ClassNamed ITEM_RARITY = BUKKIT.relativeClassNamed("inventory", "ItemRarity");

    public static final ClassNamed COOKING_BOOK_CATEGORY = BUKKIT.relativeClassNamed("inventory.recipe", "CookingBookCategory");

    public static final ClassNamed CRAFTING_BOOK_CATEGORY = BUKKIT.relativeClassNamed("inventory.recipe", "CraftingBookCategory");

    public static final ClassNamed MAP_PALETTE = BUKKIT.relativeClassNamed("map", "MapPalette");

    public static final ClassNamed DISPLAY_SLOT = BUKKIT.relativeClassNamed("scoreboard", "DisplaySlot");

    public static final ClassNamed SOUND_CATEGORY = BUKKIT.rootClassNamed("SoundCategory");

    public static final ClassNamed DAMAGE_TYPE_TAGS = BUKKIT.relativeClassNamed("tag", "DamageTypeTags");

    public static final ClassNamed BLOCK_DATA = BUKKIT.relativeClassNamed("block.data", "BlockData");

    public static final ClassNamed BLOCK_DATA_REDSTONE_WIRE = BUKKIT.relativeClassNamed("block.data.type", "RedstoneWire");

    public static final ClassNamed BLOCK_DATA_MULTIPLE_FACING = BUKKIT.relativeClassNamed("block.data", "MultipleFacing");

    public static final ClassNamed BLOCK_DATA_RAIL_SHAPE = typed(io.papermc.generator.types.Types.BLOCK_DATA_RAIL_SHAPE);

    public static final ClassNamed AXIS = typed(io.papermc.generator.types.Types.AXIS);

    public static final ClassNamed BLOCK_FACE = typed(io.papermc.generator.types.Types.BLOCK_FACE);

    public static final ClassNamed NOTE = typed(io.papermc.generator.types.Types.NOTE);

    public static final ClassNamed NAMED_TEXT_COLOR = ClassNamed.of("net.kyori.adventure.text.format", "NamedTextColor");

    public static final ClassNamed REGISTRY_KEY = typed(io.papermc.generator.types.Types.REGISTRY_KEY);

    public static final ClassNamed REGISTRY_EVENTS = PAPER.relativeClassNamed("registry.event", "RegistryEvents");

    public static final ClassNamed REGISTRY_EVENT_PROVIDER = PAPER.relativeClassNamed("registry.event", "RegistryEventProvider");

    public static final ClassNamed ITEM_USE_ANIMATION = PAPER.relativeClassNamed("datacomponent.item.consumable", "ItemUseAnimation");

    public static final ClassNamed GENERATED_FROM = typed(io.papermc.generator.types.Types.GENERATED_FROM);

    public static final ClassNamed BLOCK_PROPERTIES = PAPER.relativeClassNamed("block.property", "BlockProperties");

    public static final ClassNamed BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "BlockProperty");

    public static final ClassNamed BOOLEAN_BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "BooleanBlockProperty");

    public static final ClassNamed ENUM_BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "EnumBlockProperty");

    public static final ClassNamed INTEGER_BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "IntegerBlockProperty");

    public static final ClassNamed ROTATION_BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "RotationBlockProperty");

    public static final ClassNamed NOTE_BLOCK_PROPERTY = PAPER.relativeClassNamed("block.property", "NoteBlockProperty");

    public static final ClassNamed CRAFT_BLOCK_DATA = typed(io.papermc.generator.types.Types.CRAFT_BLOCK_DATA);

    public static final ClassNamed CRAFT_BLOCK_STATES = CRAFT_BUKKIT.relativeClassNamed("block", "CraftBlockStates");

    public static final ClassNamed CRAFT_ITEM_METAS = CRAFT_BUKKIT.relativeClassNamed("inventory", "CraftItemMetas");

    public static final ClassNamed CRAFT_STATISTIC = CRAFT_BUKKIT.relativeClassNamed(null, "CraftStatistic");

    public static final ClassNamed CRAFT_POTION_UTIL = CRAFT_BUKKIT.relativeClassNamed("potion", "CraftPotionUtil");

    public static final ClassNamed FIELD_RENAME = CRAFT_BUKKIT.relativeClassNamed("legacy", "FieldRename");

    public static final ClassNamed REGISTRIES_ARGUMENT_PROVIDER = BUKKIT.relativeClassNamed("support.provider" , "RegistriesArgumentProvider");

    public static final ClassNamed REGISTRY_CONVERSION_TEST = BUKKIT.relativeClassNamed("registry", "RegistryConversionTest");

    public static final ClassNamed PAPER_REGISTRIES = PAPER.relativeClassNamed("registry", "PaperRegistries");

    public static final ClassNamed PAPER_FEATURE_FLAG_PROVIDER_IMPL = PAPER.relativeClassNamed("world.flag", "PaperFeatureFlagProviderImpl");

    public static final ClassNamed PAPER_SIMPLE_REGISTRY = PAPER.relativeClassNamed("registry", "PaperSimpleRegistry");

    public static final ClassNamed REGISTRY_MODIFICATION_API_SUPPORT = PAPER.relativeClassNamed("registry.entry", "RegistryEntryMeta", "RegistryModificationApiSupport");

    public static final ClassNamed MOB_GOAL_HELPER = PAPER_LEGACY.relativeClassNamed("entity.ai", "MobGoalHelper");

    public static ClassNamed typed(ClassName name) {
        List<String> names = new ArrayList<>(name.simpleNames());
        String topName = names.removeFirst();
        return ClassNamed.of(name.packageName(), topName, names.toArray(new String[0]));
    }
}
