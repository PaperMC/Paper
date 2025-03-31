package io.papermc.generator;

import io.papermc.generator.registry.RegistryBootstrapper;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.resources.DataFileLoader;
import io.papermc.generator.resources.DataFiles;
import io.papermc.generator.resources.data.EntityClassData;
import io.papermc.generator.rewriter.registration.PatternSourceSetRewriter;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.rewriter.types.registry.FeatureFlagRewriter;
import io.papermc.generator.rewriter.types.registry.PaperFeatureFlagMapping;
import io.papermc.generator.rewriter.types.registry.RegistriesArgumentProviderRewriter;
import io.papermc.generator.rewriter.types.registry.RegistryConversionTestRewriter;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.rewriter.types.registry.RegistryTagRewriter;
import io.papermc.generator.rewriter.types.registry.TagRewriter;
import io.papermc.generator.rewriter.types.simple.BlockPropertiesRewriter;
import io.papermc.generator.rewriter.types.simple.BlockTypeRewriter;
import io.papermc.generator.rewriter.types.simple.CraftBlockDataMapping;
import io.papermc.generator.rewriter.types.simple.CraftBlockEntityStateMapping;
import io.papermc.generator.rewriter.types.simple.CraftItemMetasRewriter;
import io.papermc.generator.rewriter.types.simple.CraftPotionUtilRewriter;
import io.papermc.generator.rewriter.types.simple.EntityTypeRewriter;
import io.papermc.generator.rewriter.types.simple.ItemTypeRewriter;
import io.papermc.generator.rewriter.types.simple.MapPaletteRewriter;
import io.papermc.generator.rewriter.types.simple.MaterialRewriter;
import io.papermc.generator.rewriter.types.simple.MemoryKeyRewriter;
import io.papermc.generator.rewriter.types.simple.StatisticRewriter;
import io.papermc.generator.rewriter.types.simple.trial.AttributeRewriter;
import io.papermc.generator.rewriter.types.simple.trial.PoseRewriter;
import io.papermc.generator.rewriter.types.simple.trial.VillagerProfessionRewriter;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.preset.EnumCloneRewriter;
import io.papermc.typewriter.preset.model.EnumConstant;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.lang.model.SourceVersion;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.vehicle.AbstractBoat;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.scores.DisplaySlot;
import org.jspecify.annotations.NullMarked;

import static io.papermc.generator.rewriter.registration.PaperPatternSourceSetRewriter.composite;
import static io.papermc.generator.rewriter.registration.RewriterHolder.holder;
import static io.papermc.generator.rewriter.registration.RewriterHolder.sameHolder;
import static io.papermc.generator.utils.Formatting.quoted;

@NullMarked
public final class Rewriters {

    public static void bootstrap(PatternSourceSetRewriter... sourceSets) {
        Iterator<Consumer<PatternSourceSetRewriter>> values = VALUES.values().iterator();
        for (PatternSourceSetRewriter sourceSetRewriter : sourceSets) {
            values.next().accept(sourceSetRewriter);
        }
    }

    public static final Map<String, Consumer<PatternSourceSetRewriter>> VALUES = Util.make(new LinkedHashMap<>(), map -> {
        map.put("api", Rewriters::bootstrapApi);
        map.put("impl", Rewriters::bootstrapImpl);
        map.put("impl-test", Rewriters::bootstrapImplTest);
    });

    private static void bootstrapApi(PatternSourceSetRewriter sourceSet) {
        sourceSet
            .register("PotionType", new EnumRegistryRewriter<>(Registries.POTION))
            .register("EntityType", new EntityTypeRewriter())
            .register("DisplaySlot", Types.DISPLAY_SLOT, new EnumCloneRewriter<>(DisplaySlot.class) {

                @Override
                protected EnumConstant.Builder constantPrototype(DisplaySlot slot) {
                    return EnumConstant.builder(Formatting.formatKeyAsField(slot.getSerializedName()));
                }

                @Override
                protected void rewriteConstant(EnumConstant.Builder builder, DisplaySlot slot) {
                    if (slot == DisplaySlot.LIST) {
                        builder.rename(name -> "PLAYER_LIST");
                    }
                    builder.argument(quoted(slot.getSerializedName()));
                }
            })
            .register("Pose", Types.POSE, new PoseRewriter())
            .register("SnifferState", Types.SNIFFER_STATE, new EnumCloneRewriter<>(Sniffer.State.class))
            .register("PandaGene", Types.PANDA_GENE, new EnumCloneRewriter<>(Panda.Gene.class) {
                @Override
                protected void rewriteConstant(EnumConstant.Builder builder, Panda.Gene gene) {
                    builder.argument(String.valueOf(gene.isRecessive()));
                }
            })
            .register("CookingBookCategory", Types.COOKING_BOOK_CATEGORY, new EnumCloneRewriter<>(CookingBookCategory.class))
            .register("CraftingBookCategory", Types.CRAFTING_BOOK_CATEGORY, new EnumCloneRewriter<>(CraftingBookCategory.class))
            .register("TropicalFishPattern", Types.TROPICAL_FISH_PATTERN, new EnumCloneRewriter<>(TropicalFish.Pattern.class))
            .register("BoatStatus", Types.BOAT_STATUS, new EnumCloneRewriter<>(AbstractBoat.Status.class))
            .register("FoxType", Types.FOX_TYPE, new EnumCloneRewriter<>(Fox.Variant.class))
            .register("SalmonVariant", Types.SALMON_VARIANT, new EnumCloneRewriter<>(Salmon.Variant.class))
            .register("ArmadilloState", Types.ARMADILLO_STATE, new EnumCloneRewriter<>(Armadillo.ArmadilloState.class))
            .register("SoundCategory", Types.SOUND_CATEGORY, new EnumCloneRewriter<>(SoundSource.class))
            .register("ItemUseAnimation", Types.ITEM_USE_ANIMATION, new EnumCloneRewriter<>(ItemUseAnimation.class))
            .register("ItemRarity", Types.ITEM_RARITY, new EnumCloneRewriter<>(Rarity.class) {
                @Override
                protected void rewriteConstant(EnumConstant.Builder builder, Rarity rarity) {
                    builder.argument("%s.%s".formatted(Types.NAMED_TEXT_COLOR.simpleName(), rarity.color().name()));
                }
            })
            .register(Types.MATERIAL, composite(
                sameHolder("Blocks", new MaterialRewriter.Blocks()),
                //sameHolder("Material#isTransparent", MaterialRewriter.IsTransparent()),

                sameHolder("Items", new MaterialRewriter.Items())
            ))
            .register(Types.STATISTIC, composite(
                sameHolder("StatisticCustom", new StatisticRewriter.Custom()),
                sameHolder("StatisticType", new StatisticRewriter.Type())
            ))
            .register(Types.VILLAGER, composite(
                holder("VillagerType", new RegistryFieldRewriter<>(Registries.VILLAGER_TYPE, "getType")),
                holder("VillagerProfession", new VillagerProfessionRewriter())
            ))
            .register("JukeboxSong", new RegistryFieldRewriter<>(Registries.JUKEBOX_SONG, "get") {
                @Override
                protected String rewriteFieldName(Holder.Reference<JukeboxSong> reference) {
                    String keyedName = super.rewriteFieldName(reference);
                    if (!SourceVersion.isIdentifier(keyedName)) {
                        // fallback to field names for invalid identifier (happens for 5, 11, 13 etc.)
                        return RegistryEntries.byRegistryKey(Registries.JUKEBOX_SONG).getFieldNames().get(reference.key());
                    }
                    return keyedName;
                }
            })
            .register("DamageTypeTags", Types.DAMAGE_TYPE_TAGS, new RegistryTagRewriter<>(Registries.DAMAGE_TYPE))
            .register("MapCursorType", new RegistryFieldRewriter<>(Registries.MAP_DECORATION_TYPE, "getType"))
            .register("Structure", new RegistryFieldRewriter<>(Registries.STRUCTURE, "getStructure"))
            .register("StructureType", new RegistryFieldRewriter<>(Registries.STRUCTURE_TYPE, "getStructureType"))
            .register("TrimPattern", new RegistryFieldRewriter<>(Registries.TRIM_PATTERN, "getTrimPattern"))
            .register("TrimMaterial", new RegistryFieldRewriter<>(Registries.TRIM_MATERIAL, "getTrimMaterial"))
            .register("DamageType", new RegistryFieldRewriter<>(Registries.DAMAGE_TYPE, "getDamageType"))
            .register("GameEvent", new RegistryFieldRewriter<>(Registries.GAME_EVENT, "getEvent"))
            .register("MusicInstrument", new RegistryFieldRewriter<>(Registries.INSTRUMENT, "getInstrument"))
            .register("WolfVariant", new RegistryFieldRewriter<>(Registries.WOLF_VARIANT, "getVariant"))
            .register("WolfSoundVariant", new RegistryFieldRewriter<>(Registries.WOLF_SOUND_VARIANT, "getSoundVariant"))
            .register("CatType", new RegistryFieldRewriter<>(Registries.CAT_VARIANT, "getType"))
            .register("FrogVariant", new RegistryFieldRewriter<>(Registries.FROG_VARIANT, "getVariant"))
            .register("PatternType", new RegistryFieldRewriter<>(Registries.BANNER_PATTERN, "getType"))
            .register("Biome", new RegistryFieldRewriter<>(Registries.BIOME, "getBiome"))
            .register("Fluid", new RegistryFieldRewriter<>(Registries.FLUID, "getFluid"))
            .register("Attribute", new AttributeRewriter())
            .register("Sound", new RegistryFieldRewriter<>(Registries.SOUND_EVENT, "getSound"))
            .register("Art", new RegistryFieldRewriter<>(Registries.PAINTING_VARIANT, "getArt"))
            .register("ChickenVariant", new RegistryFieldRewriter<>(Registries.CHICKEN_VARIANT, "getVariant"))
            .register("CowVariant", new RegistryFieldRewriter<>(Registries.COW_VARIANT, "getVariant"))
            .register("PigVariant", new RegistryFieldRewriter<>(Registries.PIG_VARIANT, "getVariant"))
            .register("Dialog", new RegistryFieldRewriter<>(Registries.DIALOG, "getDialog"))
            .register("BlockProperties", Types.BLOCK_PROPERTIES, new BlockPropertiesRewriter())
            .register("MemoryKey", new MemoryKeyRewriter())
            .register("ItemType", new ItemTypeRewriter())
            .register("BlockType", new BlockTypeRewriter())
            .register("FeatureFlag", Types.FEATURE_FLAG, new FeatureFlagRewriter())
            .register("Tag", Types.TAG, new TagRewriter())
            .register("MapPalette#colors", Types.MAP_PALETTE, new MapPaletteRewriter());
        RegistryBootstrapper.bootstrapApi(sourceSet);
    }

    private static void bootstrapImpl(PatternSourceSetRewriter sourceSet) {
        sourceSet
            .register("CraftBlockData#MAP", Types.CRAFT_BLOCK_DATA, new CraftBlockDataMapping())
            .register("CraftBlockEntityStates", Types.CRAFT_BLOCK_STATES, new CraftBlockEntityStateMapping())
            .register("CraftItemMetas#getItemMetaData", Types.CRAFT_ITEM_METAS, new CraftItemMetasRewriter())
            .register(Types.CRAFT_STATISTIC, composite(
                sameHolder("CraftStatisticCustom", new StatisticRewriter.CraftCustom()),
                sameHolder("CraftStatisticType", new StatisticRewriter.CraftType())
            ))
            .register(Types.CRAFT_POTION_UTIL, composite(
                sameHolder("CraftPotionUtil#upgradeable", new CraftPotionUtilRewriter("strong")),
                sameHolder("CraftPotionUtil#extendable", new CraftPotionUtilRewriter("long"))
            ))
            .register("PaperFeatureFlagProviderImpl#FLAGS", Types.PAPER_FEATURE_FLAG_PROVIDER_IMPL, new PaperFeatureFlagMapping())
            .register("MobGoalHelper#BUKKIT_BRIDGE", Types.MOB_GOAL_HELPER, new SearchReplaceRewriter() {
                @Override
                protected void insert(SearchMetadata metadata, StringBuilder builder) {
                    for (Map.Entry<Class<? extends Mob>, EntityClassData> entry : DataFileLoader.get(DataFiles.ENTITY_CLASS_NAMES).entrySet()) {
                        builder.append(metadata.indent()).append("map.put(%s.class, %s.class);".formatted(
                            entry.getKey().getCanonicalName(), this.importCollector.getShortName(Types.typed(entry.getValue().name()))
                        ));
                        builder.append('\n');
                    }
                }
            });
        RegistryBootstrapper.bootstrapServer(sourceSet);
    }

    private static void bootstrapImplTest(PatternSourceSetRewriter sourceSet) {
        sourceSet
            .register("RegistriesArgumentProvider#DATA", Types.REGISTRIES_ARGUMENT_PROVIDER, new RegistriesArgumentProviderRewriter())
            .register("RegistryConversionTest#IGNORE_FOR_DIRECT_HOLDER", Types.REGISTRY_CONVERSION_TEST, new RegistryConversionTestRewriter());
    }
}
