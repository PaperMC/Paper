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
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapter;
import io.papermc.paper.registry.data.typed.PaperTypedDataAdapters;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.InstrumentComponent;
import net.minecraft.world.item.component.MapPostProcessing;
import net.minecraft.world.item.component.ProvidesTrimMaterial;
import org.bukkit.DyeColor;
import org.bukkit.craftbukkit.CraftArt;
import org.bukkit.craftbukkit.CraftMusicInstrument;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.damage.CraftDamageType;
import org.bukkit.craftbukkit.entity.CraftCat;
import org.bukkit.craftbukkit.entity.CraftChicken;
import org.bukkit.craftbukkit.entity.CraftCow;
import org.bukkit.craftbukkit.entity.CraftFrog;
import org.bukkit.craftbukkit.entity.CraftPig;
import org.bukkit.craftbukkit.entity.CraftVillager;
import org.bukkit.craftbukkit.entity.CraftWolf;
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

import static io.papermc.paper.util.MCUtil.transformUnmodifiable;

public abstract class PaperDataComponentType<API, NMS> extends HolderableBase<net.minecraft.core.component.DataComponentType<NMS>> implements DataComponentType {

    private static final PaperTypedDataAdapters ADAPTERS = PaperTypedDataAdapters.create(
        BuiltInRegistries.DATA_COMPONENT_TYPE,
        PaperDataComponentTypeCollector::new,
        collector -> {
            collector.registerIdentity(DataComponents.MAX_STACK_SIZE, net.minecraft.core.component.DataComponentType::codec);
            collector.registerIdentity(DataComponents.MAX_DAMAGE, net.minecraft.core.component.DataComponentType::codec);
            collector.registerIdentity(DataComponents.DAMAGE, net.minecraft.core.component.DataComponentType::codec);
            collector.registerUntyped(DataComponents.UNBREAKABLE);
            collector.register(DataComponents.USE_EFFECTS, PaperUseEffects::new);
            collector.registerIdentity(DataComponents.POTION_DURATION_SCALE, net.minecraft.core.component.DataComponentType::codec);
            collector.register(DataComponents.CUSTOM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.registerIdentity(DataComponents.MINIMUM_ATTACK_CHARGE, net.minecraft.core.component.DataComponentType::codec);
            collector.register(DataComponents.DAMAGE_TYPE, nms -> CraftDamageType.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftDamageType.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.ITEM_NAME, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.ITEM_MODEL, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.LORE, PaperItemLore::new);
            collector.register(DataComponents.RARITY, nms -> ItemRarity.valueOf(nms.name()), api -> Rarity.valueOf(api.name()));
            collector.register(DataComponents.ENCHANTMENTS, PaperItemEnchantments::new);
            collector.register(DataComponents.CAN_PLACE_ON, PaperItemAdventurePredicate::new);
            collector.register(DataComponents.CAN_BREAK, PaperItemAdventurePredicate::new);
            collector.register(DataComponents.ATTRIBUTE_MODIFIERS, PaperItemAttributeModifiers::new);
            collector.register(DataComponents.CUSTOM_MODEL_DATA, PaperCustomModelData::new);
            collector.registerIdentity(DataComponents.REPAIR_COST, net.minecraft.core.component.DataComponentType::codec);
            // registerUntyped(DataComponents.CREATIVE_SLOT_LOCK);
            collector.registerIdentity(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, net.minecraft.core.component.DataComponentType::codec);
            collector.registerUntyped(DataComponents.INTANGIBLE_PROJECTILE);
            collector.register(DataComponents.FOOD, PaperFoodProperties::new);
            collector.register(DataComponents.CONSUMABLE, PaperConsumable::new);
            collector.register(DataComponents.USE_REMAINDER, PaperUseRemainder::new);
            collector.register(DataComponents.USE_COOLDOWN, PaperUseCooldown::new);
            collector.register(DataComponents.DAMAGE_RESISTANT, PaperDamageResistant::new);
            collector.register(DataComponents.TOOL, PaperItemTool::new);
            collector.register(DataComponents.ENCHANTABLE, PaperEnchantable::new);
            collector.register(DataComponents.EQUIPPABLE, PaperEquippable::new);
            collector.register(DataComponents.REPAIRABLE, PaperRepairable::new);
            collector.registerUntyped(DataComponents.GLIDER);
            collector.register(DataComponents.TOOLTIP_STYLE, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.DEATH_PROTECTION, PaperDeathProtection::new);
            collector.register(DataComponents.STORED_ENCHANTMENTS, PaperItemEnchantments::new);
            collector.register(DataComponents.DYED_COLOR, PaperDyedItemColor::new);
            collector.register(DataComponents.MAP_COLOR, PaperMapItemColor::new);
            collector.register(DataComponents.MAP_ID, PaperMapId::new);
            collector.register(DataComponents.MAP_DECORATIONS, PaperMapDecorations::new);
            collector.register(DataComponents.MAP_POST_PROCESSING, nms -> io.papermc.paper.item.MapPostProcessing.valueOf(nms.name()), api -> MapPostProcessing.valueOf(api.name()));
            collector.register(DataComponents.CHARGED_PROJECTILES, PaperChargedProjectiles::new);
            collector.register(DataComponents.BUNDLE_CONTENTS, PaperBundleContents::new);
            collector.register(DataComponents.POTION_CONTENTS, PaperPotionContents::new);
            collector.register(DataComponents.SUSPICIOUS_STEW_EFFECTS, PaperSuspiciousStewEffects::new);
            collector.register(DataComponents.WRITTEN_BOOK_CONTENT, PaperWrittenBookContent::new);
            collector.register(DataComponents.WRITABLE_BOOK_CONTENT, PaperWritableBookContent::new);
            collector.register(DataComponents.TRIM, PaperItemArmorTrim::new);
            // debug stick state
            // entity data
            // bucket entity data
            // block entity data
            collector.register(DataComponents.INSTRUMENT, nms -> CraftMusicInstrument.minecraftHolderToBukkit(nms.instrument().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new InstrumentComponent(CraftMusicInstrument.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.PROVIDES_TRIM_MATERIAL, nms -> CraftTrimMaterial.minecraftHolderToBukkit(nms.material().unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new ProvidesTrimMaterial(CraftTrimMaterial.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.OMINOUS_BOTTLE_AMPLIFIER, PaperOminousBottleAmplifier::new);
            collector.register(DataComponents.JUKEBOX_PLAYABLE, PaperJukeboxPlayable::new);
            collector.register(DataComponents.PROVIDES_BANNER_PATTERNS, PaperRegistries::fromNms, PaperRegistries::toNms);
            collector.register(
                DataComponents.RECIPES,
                nms -> transformUnmodifiable(nms, PaperAdventure::asAdventureKey),
                api -> transformUnmodifiable(api, key -> PaperAdventure.asVanilla(Registries.RECIPE, key))
            );
            collector.register(DataComponents.LODESTONE_TRACKER, PaperLodestoneTracker::new);
            collector.register(DataComponents.FIREWORK_EXPLOSION, CraftMetaFirework::getEffect, CraftMetaFirework::getExplosion);
            collector.register(DataComponents.FIREWORKS, PaperFireworks::new);
            collector.register(DataComponents.PROFILE, PaperResolvableProfile::new);
            collector.register(DataComponents.NOTE_BLOCK_SOUND, PaperAdventure::asAdventure, PaperAdventure::asVanilla);
            collector.register(DataComponents.BANNER_PATTERNS, PaperBannerPatternLayers::new);
            collector.register(DataComponents.BASE_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.POT_DECORATIONS, PaperPotDecorations::new);
            collector.register(DataComponents.CONTAINER, PaperItemContainerContents::new);
            collector.register(DataComponents.BLOCK_STATE, PaperBlockItemDataProperties::new);
            // bees
            // register(DataComponents.LOCK, PaperLockCode::new);
            collector.register(DataComponents.CONTAINER_LOOT, PaperSeededContainerLoot::new);
            collector.register(DataComponents.BREAK_SOUND, nms -> PaperAdventure.asAdventure(nms.value().location()), PaperAdventure::resolveSound);
            collector.register(DataComponents.TOOLTIP_DISPLAY, PaperTooltipDisplay::new);
            collector.register(DataComponents.WEAPON, PaperWeapon::new);
            collector.register(DataComponents.BLOCKS_ATTACKS, PaperBlocksAttacks::new);
            collector.register(DataComponents.PIERCING_WEAPON, PaperPiercingWeapon::new);
            collector.register(DataComponents.KINETIC_WEAPON, PaperKineticWeapon::new);
            collector.register(DataComponents.ATTACK_RANGE, PaperAttackRange::new);
            collector.register(DataComponents.SWING_ANIMATION, PaperSwingAnimation::new);
            collector.register(DataComponents.VILLAGER_VARIANT, CraftVillager.CraftType::minecraftHolderToBukkit, CraftVillager.CraftType::bukkitToMinecraftHolder);
            collector.register(DataComponents.WOLF_VARIANT, CraftWolf.CraftVariant::minecraftHolderToBukkit, CraftWolf.CraftVariant::bukkitToMinecraftHolder);
            collector.register(DataComponents.WOLF_COLLAR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.WOLF_SOUND_VARIANT, CraftWolf.CraftSoundVariant::minecraftHolderToBukkit, CraftWolf.CraftSoundVariant::bukkitToMinecraftHolder);
            collector.register(DataComponents.FOX_VARIANT, nms -> org.bukkit.entity.Fox.Type.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.fox.Fox.Variant.byId(api.ordinal()));
            collector.register(DataComponents.SALMON_SIZE, nms -> Salmon.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.fish.Salmon.Variant.values()[api.ordinal()]);
            collector.register(DataComponents.PARROT_VARIANT, nms -> Parrot.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.parrot.Parrot.Variant.byId(api.ordinal()));
            collector.register(DataComponents.TROPICAL_FISH_PATTERN, nms -> TropicalFish.Pattern.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.fish.TropicalFish.Pattern.values()[api.ordinal()]);
            collector.register(DataComponents.TROPICAL_FISH_BASE_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.TROPICAL_FISH_PATTERN_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.MOOSHROOM_VARIANT, nms -> MushroomCow.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.cow.MushroomCow.Variant.values()[api.ordinal()]);
            collector.register(DataComponents.RABBIT_VARIANT, nms -> Rabbit.Type.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.rabbit.Rabbit.Variant.byId(api.ordinal()));
            collector.register(DataComponents.PIG_VARIANT, CraftPig.CraftVariant::minecraftHolderToBukkit, CraftPig.CraftVariant::bukkitToMinecraftHolder);
            collector.register(DataComponents.COW_VARIANT, CraftCow.CraftVariant::minecraftHolderToBukkit, CraftCow.CraftVariant::bukkitToMinecraftHolder);
            collector.register(DataComponents.CHICKEN_VARIANT, nms -> CraftChicken.CraftVariant.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftChicken.CraftVariant.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.FROG_VARIANT, CraftFrog.CraftVariant::minecraftHolderToBukkit, CraftFrog.CraftVariant::bukkitToMinecraftHolder);
            collector.register(DataComponents.ZOMBIE_NAUTILUS_VARIANT, nms -> CraftZombieNautilus.CraftVariant.minecraftHolderToBukkit(nms.unwrap(CraftRegistry.getMinecraftRegistry()).orElseThrow()), api -> new EitherHolder<>(CraftZombieNautilus.CraftVariant.bukkitToMinecraftHolder(api)));
            collector.register(DataComponents.HORSE_VARIANT, nms -> Horse.Color.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.equine.Variant.byId(api.ordinal()));
            collector.register(DataComponents.PAINTING_VARIANT, CraftArt::minecraftHolderToBukkit, CraftArt::bukkitToMinecraftHolder);
            collector.register(DataComponents.LLAMA_VARIANT, nms -> Llama.Color.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.equine.Llama.Variant.byId(api.ordinal()));
            collector.register(DataComponents.AXOLOTL_VARIANT, nms -> Axolotl.Variant.values()[nms.ordinal()], api -> net.minecraft.world.entity.animal.axolotl.Axolotl.Variant.byId(api.ordinal()));
            collector.register(DataComponents.CAT_VARIANT, CraftCat.CraftType::minecraftHolderToBukkit, CraftCat.CraftType::bukkitToMinecraftHolder);
            collector.register(DataComponents.CAT_COLLAR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.SHEEP_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
            collector.register(DataComponents.SHULKER_COLOR, nms -> DyeColor.getByWoolData((byte) nms.getId()), api -> net.minecraft.world.item.DyeColor.byId(api.getWoolData()));
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
        return type.getAdapter().fromVanilla(nmsValue);
    }

    private final PaperTypedDataAdapter<API, NMS> adapter;

    private PaperDataComponentType(final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder, final PaperTypedDataAdapter<API, NMS> adapter) {
        super(holder);
        this.adapter = adapter;
    }

    @Override
    public boolean isPersistent() {
        return !this.getHandle().isTransient();
    }

    public PaperTypedDataAdapter<API, NMS> getAdapter() {
        return this.adapter;
    }

    @SuppressWarnings("unchecked")
    public static <NMS> DataComponentType of(final Holder<?> holder) {
        final PaperTypedDataAdapter<?, NMS> adapter = PaperDataComponentType.ADAPTERS.getAdapter(holder.unwrapKey().orElseThrow());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for " + holder);
        }
        if (adapter.isUnimplemented()) {
            return new Unimplemented<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        } else if (adapter.isValued()) {
            return new ValuedImpl<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        } else {
            return new NonValuedImpl<>((Holder<net.minecraft.core.component.DataComponentType<NMS>>) holder, adapter);
        }
    }

    public static final class NonValuedImpl<API, NMS> extends PaperDataComponentType<API, NMS> implements NonValued {

        NonValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class ValuedImpl<API, NMS> extends PaperDataComponentType<API, NMS> implements Valued<API> {

        ValuedImpl(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }

    public static final class Unimplemented<API, NMS> extends PaperDataComponentType<API, NMS> {

        public Unimplemented(
            final Holder<net.minecraft.core.component.DataComponentType<NMS>> holder,
            final PaperTypedDataAdapter<API, NMS> adapter
        ) {
            super(holder, adapter);
        }
    }
}
