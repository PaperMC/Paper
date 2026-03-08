package io.papermc.paper.datacomponent;

import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.item.PaperAttackRange;
import io.papermc.paper.datacomponent.item.PaperBannerPatternLayers;
import io.papermc.paper.datacomponent.item.PaperBlockItemDataProperties;
import io.papermc.paper.datacomponent.item.PaperBlocksAttacks;
import io.papermc.paper.datacomponent.item.PaperBundleContents;
import io.papermc.paper.datacomponent.item.PaperChargedProjectiles;
import io.papermc.paper.datacomponent.item.PaperConsumable;
import io.papermc.paper.datacomponent.item.PaperCustomModelData;
import io.papermc.paper.datacomponent.item.PaperDamageResistant;
import io.papermc.paper.datacomponent.item.PaperDeathProtection;
import io.papermc.paper.datacomponent.item.PaperDyedItemColor;
import io.papermc.paper.datacomponent.item.PaperEnchantable;
import io.papermc.paper.datacomponent.item.PaperEquippable;
import io.papermc.paper.datacomponent.item.PaperFireworks;
import io.papermc.paper.datacomponent.item.PaperFoodProperties;
import io.papermc.paper.datacomponent.item.PaperItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.PaperItemArmorTrim;
import io.papermc.paper.datacomponent.item.PaperItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.PaperItemContainerContents;
import io.papermc.paper.datacomponent.item.PaperItemEnchantments;
import io.papermc.paper.datacomponent.item.PaperItemLore;
import io.papermc.paper.datacomponent.item.PaperItemTool;
import io.papermc.paper.datacomponent.item.PaperJukeboxPlayable;
import io.papermc.paper.datacomponent.item.PaperKineticWeapon;
import io.papermc.paper.datacomponent.item.PaperLodestoneTracker;
import io.papermc.paper.datacomponent.item.PaperMapDecorations;
import io.papermc.paper.datacomponent.item.PaperMapId;
import io.papermc.paper.datacomponent.item.PaperMapItemColor;
import io.papermc.paper.datacomponent.item.PaperOminousBottleAmplifier;
import io.papermc.paper.datacomponent.item.PaperPiercingWeapon;
import io.papermc.paper.datacomponent.item.PaperPotDecorations;
import io.papermc.paper.datacomponent.item.PaperPotionContents;
import io.papermc.paper.datacomponent.item.PaperRepairable;
import io.papermc.paper.datacomponent.item.PaperResolvableProfile;
import io.papermc.paper.datacomponent.item.PaperSeededContainerLoot;
import io.papermc.paper.datacomponent.item.PaperSuspiciousStewEffects;
import io.papermc.paper.datacomponent.item.PaperSwingAnimation;
import io.papermc.paper.datacomponent.item.PaperTooltipDisplay;
import io.papermc.paper.datacomponent.item.PaperUseCooldown;
import io.papermc.paper.datacomponent.item.PaperUseEffects;
import io.papermc.paper.datacomponent.item.PaperUseRemainder;
import io.papermc.paper.datacomponent.item.PaperWeapon;
import io.papermc.paper.datacomponent.item.PaperWritableBookContent;
import io.papermc.paper.datacomponent.item.PaperWrittenBookContent;
import io.papermc.paper.item.MapPostProcessing;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.typed.PaperTypedDataAdapters;
import io.papermc.paper.registry.typed.TypedDataCollector;
import io.papermc.paper.registry.typed.converter.Converter;
import io.papermc.paper.registry.typed.converter.Converters;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftZombieNautilus;
import org.bukkit.craftbukkit.inventory.CraftMetaFirework;
import org.bukkit.craftbukkit.inventory.trim.CraftTrimMaterial;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.TropicalFish;
import org.bukkit.inventory.ItemRarity;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.typed.converter.Converter.direct;
import static io.papermc.paper.registry.typed.converter.Converters.list;
import static io.papermc.paper.registry.typed.converter.Converters.registryElement;
import static io.papermc.paper.registry.typed.converter.Converters.sameName;
import static io.papermc.paper.registry.typed.converter.Converters.sameOrder;
import static io.papermc.paper.registry.typed.converter.Converters.wrapper;

public abstract class PaperDataComponentType<A, M> extends HolderableBase<net.minecraft.core.component.DataComponentType<M>> implements DataComponentType {

    // this is a hack around generics limitations to prevent
    // having to define generics on each register call as seen below, making collectors easier to read:
    //   collector.<T, A>register(...)
    private static <M, A> void register(final TypedDataCollector<net.minecraft.core.component.DataComponentType<?>> collector, final net.minecraft.core.component.DataComponentType<M> dataComponentType, final Function<M, A> vanillaToApi, final Function<A, M> apiToVanilla) {
        collector.register(dataComponentType, vanillaToApi, apiToVanilla);
    }

    @SuppressWarnings("RedundantTypeArguments")
    private static final PaperTypedDataAdapters<net.minecraft.core.component.DataComponentType<?>> ADAPTERS = PaperTypedDataAdapters.<net.minecraft.core.component.DataComponentType<?>, TypedDataCollector.Unvaluable<net.minecraft.core.component.DataComponentType<?>>>create(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        TypedDataCollector.Unvaluable::new,
        collector -> {
            final Converter<net.minecraft.world.item.DyeColor, DyeColor> dyeColor = direct(
                nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData())
            );
            collector.dispatch(type -> Converter.identity(type.codec())).add(
                DataComponents.MAX_STACK_SIZE,
                DataComponents.MAX_DAMAGE,
                DataComponents.DAMAGE,
                DataComponents.POTION_DURATION_SCALE,
                DataComponents.MINIMUM_ATTACK_CHARGE,
                DataComponents.REPAIR_COST,
                DataComponents.ENCHANTMENT_GLINT_OVERRIDE
            );
            collector.registerUnvalued(DataComponents.UNBREAKABLE);
            collector.register(DataComponents.USE_EFFECTS, wrapper(PaperUseEffects::new));
            register(collector, DataComponents.CUSTOM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            register(collector, DataComponents.DAMAGE_TYPE, nms -> CraftDamageType.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftDamageType.bukkitToMinecraftHolder(api)));
            register(collector, DataComponents.ITEM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            register(collector, DataComponents.ITEM_MODEL, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.LORE, wrapper(PaperItemLore::new));
            collector.register(DataComponents.RARITY, sameName(ItemRarity.class, Rarity.class));
            collector.register(DataComponents.ENCHANTMENTS, wrapper(PaperItemEnchantments::of));
            collector.register(DataComponents.CAN_PLACE_ON, wrapper(PaperItemAdventurePredicate::new));
            collector.register(DataComponents.CAN_BREAK, wrapper(PaperItemAdventurePredicate::new));
            collector.register(DataComponents.ATTRIBUTE_MODIFIERS, wrapper(PaperItemAttributeModifiers::new));
            collector.register(DataComponents.CUSTOM_MODEL_DATA, wrapper(PaperCustomModelData::new));
            // registerUntyped(DataComponents.CREATIVE_SLOT_LOCK);
            collector.registerUnvalued(DataComponents.INTANGIBLE_PROJECTILE);
            collector.register(DataComponents.FOOD, wrapper(PaperFoodProperties::new));
            collector.register(DataComponents.CONSUMABLE, wrapper(PaperConsumable::new));
            collector.register(DataComponents.USE_REMAINDER, wrapper(PaperUseRemainder::new));
            collector.register(DataComponents.USE_COOLDOWN, wrapper(PaperUseCooldown::new));
            collector.register(DataComponents.DAMAGE_RESISTANT, wrapper(PaperDamageResistant::new));
            collector.register(DataComponents.TOOL, wrapper(PaperItemTool::new));
            collector.register(DataComponents.ENCHANTABLE, wrapper(PaperEnchantable::new));
            collector.register(DataComponents.EQUIPPABLE, wrapper(PaperEquippable::new));
            collector.register(DataComponents.REPAIRABLE, wrapper(PaperRepairable::new));
            collector.registerUnvalued(DataComponents.GLIDER);
            register(collector, DataComponents.TOOLTIP_STYLE, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.DEATH_PROTECTION, wrapper(PaperDeathProtection::new));
            collector.register(DataComponents.STORED_ENCHANTMENTS, wrapper(PaperItemEnchantments::of));
            collector.register(DataComponents.DYED_COLOR, wrapper(PaperDyedItemColor::new));
            collector.register(DataComponents.MAP_COLOR, wrapper(PaperMapItemColor::new));
            collector.register(DataComponents.MAP_ID, wrapper(PaperMapId::new));
            collector.register(DataComponents.MAP_DECORATIONS, wrapper(PaperMapDecorations::new));
            collector.register(DataComponents.MAP_POST_PROCESSING, sameName(MapPostProcessing.class, net.minecraft.world.item.component.MapPostProcessing.class));
            collector.register(DataComponents.CHARGED_PROJECTILES, wrapper(PaperChargedProjectiles::new));
            collector.register(DataComponents.BUNDLE_CONTENTS, wrapper(PaperBundleContents::new));
            collector.register(DataComponents.POTION_CONTENTS, wrapper(PaperPotionContents::new));
            collector.register(DataComponents.SUSPICIOUS_STEW_EFFECTS, wrapper(PaperSuspiciousStewEffects::new));
            collector.register(DataComponents.WRITTEN_BOOK_CONTENT, wrapper(PaperWrittenBookContent::new));
            collector.register(DataComponents.WRITABLE_BOOK_CONTENT, wrapper(PaperWritableBookContent::new));
            collector.register(DataComponents.TRIM, wrapper(PaperItemArmorTrim::new));
            // debug stick state
            // entity data
            // bucket entity data
            // block entity data
            register(collector, DataComponents.INSTRUMENT, nms -> CraftMusicInstrument.minecraftHolderToBukkit(nms.instrument().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new InstrumentComponent(CraftMusicInstrument.bukkitToMinecraftHolder(api)));
            register(collector, DataComponents.PROVIDES_TRIM_MATERIAL, nms -> CraftTrimMaterial.minecraftHolderToBukkit(nms.material().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new ProvidesTrimMaterial(CraftTrimMaterial.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, wrapper(PaperOminousBottleAmplifier::new));
            collector.register(DataComponents.JUKEBOX_PLAYABLE, wrapper(PaperJukeboxPlayable::new));
            register(collector, DataComponents.PROVIDES_BANNER_PATTERNS, PaperRegistries::fromNms, PaperRegistries::toNms);
            collector.register(DataComponents.RECIPES, list(PaperAdventure::asAdventureKey, key -> PaperAdventure.asVanilla(Registries.RECIPE, key)));
            collector.register(DataComponents.LODESTONE_TRACKER, wrapper(PaperLodestoneTracker::new));
            collector.register(DataComponents.FIREWORK_EXPLOSION, CraftMetaFirework::getEffect, CraftMetaFirework::getExplosion);
            collector.register(DataComponents.FIREWORKS, wrapper(PaperFireworks::new));
            collector.register(DataComponents.PROFILE, wrapper(PaperResolvableProfile::new));
            register(collector, DataComponents.NOTE_BLOCK_SOUND, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.BANNER_PATTERNS, wrapper(PaperBannerPatternLayers::new));
            collector.register(DataComponents.BASE_COLOR, dyeColor);
            collector.register(DataComponents.POT_DECORATIONS, wrapper(PaperPotDecorations::new));
            collector.register(DataComponents.CONTAINER, wrapper(PaperItemContainerContents::new));
            collector.register(DataComponents.BLOCK_STATE, wrapper(PaperBlockItemDataProperties::new));
            // bees
            // register(DataComponents.LOCK, wrapper(PaperLockCode::new));
            collector.register(DataComponents.CONTAINER_LOOT, wrapper(PaperSeededContainerLoot::new));
            collector.register(DataComponents.BREAK_SOUND, nms -> PaperAdventure.asAdventure(nms.value().location()), PaperAdventure::resolveSound);
            collector.register(DataComponents.TOOLTIP_DISPLAY, wrapper(PaperTooltipDisplay::new));
            collector.register(DataComponents.WEAPON, wrapper(PaperWeapon::new));
            collector.register(DataComponents.BLOCKS_ATTACKS, wrapper(PaperBlocksAttacks::new));
            collector.register(DataComponents.PIERCING_WEAPON, wrapper(PaperPiercingWeapon::new));
            collector.register(DataComponents.KINETIC_WEAPON, wrapper(PaperKineticWeapon::new));
            collector.register(DataComponents.ATTACK_RANGE, wrapper(PaperAttackRange::new));
            collector.register(DataComponents.SWING_ANIMATION, wrapper(PaperSwingAnimation::new));
            collector.register(DataComponents.VILLAGER_VARIANT, registryElement(Registries.VILLAGER_TYPE));
            collector.register(DataComponents.WOLF_VARIANT, registryElement(Registries.WOLF_VARIANT));
            collector.register(DataComponents.WOLF_COLLAR, dyeColor);
            collector.register(DataComponents.WOLF_SOUND_VARIANT, registryElement(Registries.WOLF_SOUND_VARIANT));
            collector.register(DataComponents.FOX_VARIANT, sameOrder(org.bukkit.entity.Fox.Type.class, net.minecraft.world.entity.animal.fox.Fox.Variant.class));
            collector.register(DataComponents.SALMON_SIZE, sameOrder(Salmon.Variant.class, net.minecraft.world.entity.animal.fish.Salmon.Variant.class));
            collector.register(DataComponents.PARROT_VARIANT, sameOrder(Parrot.Variant.class, net.minecraft.world.entity.animal.parrot.Parrot.Variant.class));
            collector.register(DataComponents.TROPICAL_FISH_PATTERN, sameOrder(TropicalFish.Pattern.class, net.minecraft.world.entity.animal.fish.TropicalFish.Pattern.class));
            collector.register(DataComponents.TROPICAL_FISH_BASE_COLOR, dyeColor);
            collector.register(DataComponents.TROPICAL_FISH_PATTERN_COLOR, dyeColor);
            collector.register(DataComponents.MOOSHROOM_VARIANT, sameOrder(MushroomCow.Variant.class, net.minecraft.world.entity.animal.cow.MushroomCow.Variant.class));
            collector.register(DataComponents.RABBIT_VARIANT, sameOrder(Rabbit.Type.class, net.minecraft.world.entity.animal.rabbit.Rabbit.Variant.class));
            collector.register(DataComponents.PIG_VARIANT, registryElement(Registries.PIG_VARIANT));
            collector.register(DataComponents.COW_VARIANT, registryElement(Registries.COW_VARIANT));
            register(collector, DataComponents.CHICKEN_VARIANT, nms -> CraftChicken.CraftVariant.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftChicken.CraftVariant.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.FROG_VARIANT, registryElement(Registries.FROG_VARIANT));
            register(collector, DataComponents.ZOMBIE_NAUTILUS_VARIANT, nms -> CraftZombieNautilus.CraftVariant.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftZombieNautilus.CraftVariant.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.HORSE_VARIANT, sameOrder(Horse.Color.class, net.minecraft.world.entity.animal.equine.Variant.class));
            collector.register(DataComponents.PAINTING_VARIANT, registryElement(Registries.PAINTING_VARIANT));
            collector.register(DataComponents.LLAMA_VARIANT, sameOrder(Llama.Color.class, net.minecraft.world.entity.animal.equine.Llama.Variant.class));
            collector.register(DataComponents.AXOLOTL_VARIANT, sameOrder(Axolotl.Variant.class, net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.class));
            collector.register(DataComponents.CAT_VARIANT, registryElement(Registries.CAT_VARIANT));
            collector.register(DataComponents.CAT_COLLAR, dyeColor);
            collector.register(DataComponents.SHEEP_COLOR, dyeColor);
            collector.register(DataComponents.SHULKER_COLOR, dyeColor);
        }
    );

    public static <T> net.minecraft.core.component.DataComponentType<T> bukkitToMinecraft(final DataComponentType type) {
        return CraftRegistry.bukkitToMinecraft(type);
    }

    public static DataComponentType minecraftToBukkit(final net.minecraft.core.component.DataComponentType<?> type) {
        return CraftRegistry.minecraftToBukkit(type, Registries.DATA_COMPONENT_TYPE);
    }

    public static Set<DataComponentType> minecraftToBukkit(final Set<net.minecraft.core.component.DataComponentType<?>> nmsTypes) {
        final Set<DataComponentType> types = new HashSet<>(nmsTypes.size());
        for (final net.minecraft.core.component.DataComponentType<?> nmsType : nmsTypes) {
            types.add(PaperDataComponentType.minecraftToBukkit(nmsType));
        }
        return Collections.unmodifiableSet(types);
    }

    public static <B, M> @Nullable B convertDataComponentValue(final DataComponentMap map, final PaperDataComponentType.ValuedImpl<B, M> type) {
        final net.minecraft.core.component.DataComponentType<M> nms = bukkitToMinecraft(type);
        final M nmsValue = map.get(nms);
        if (nmsValue == null) {
            return null;
        }
        return type.getConverter().fromVanilla(nmsValue);
    }

    private final Converter<M, A> converter;

    private PaperDataComponentType(final Holder<net.minecraft.core.component.DataComponentType<M>> holder, final Converter<M, A> converter) {
        super(holder);
        this.converter = converter;
    }

    @Override
    public boolean isPersistent() {
        return !this.getHandle().isTransient();
    }

    public Converter<M, A> getConverter() {
        return this.converter;
    }

    @SuppressWarnings("unchecked")
    public static <M> DataComponentType of(final Holder<?> holder) {
        final Holder.Reference<net.minecraft.core.component.DataComponentType<M>> reference = (Holder.Reference<net.minecraft.core.component.DataComponentType<M>>) holder;
        final Converter<M, ?> converter = PaperDataComponentType.ADAPTERS.get(reference.key());
        if (converter == Converters.unimplemented()) {
            return new Unimplemented<>(reference, converter);
        } else if (converter == Converters.unvalued()) {
            return new NonValuedImpl<>(reference, converter);
        } else {
            return new ValuedImpl<>(reference, converter);
        }
    }

    public static final class NonValuedImpl<A, M> extends PaperDataComponentType<A, M> implements NonValued {

        NonValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<M>> holder,
            final Converter<M, A> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class ValuedImpl<A, M> extends PaperDataComponentType<A, M> implements Valued<A> {

        ValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<M>> holder,
            final Converter<M, A> converter
        ) {
            super(holder, converter);
        }
    }

    public static final class Unimplemented<A, M> extends PaperDataComponentType<A, M> {

        public Unimplemented(
            final Holder<net.minecraft.core.component.DataComponentType<M>> holder,
            final Converter<M, A> converter
        ) {
            super(holder, converter);
        }
    }
}
