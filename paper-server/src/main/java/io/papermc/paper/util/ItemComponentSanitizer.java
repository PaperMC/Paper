package io.papermc.paper.util;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.configuration.GlobalConfiguration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.Util;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.LockCode;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.saveddata.maps.MapId;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

final class ItemComponentSanitizer {

    /*
    These represent codecs that are meant to help get rid of possibly big items by ALWAYS hiding this data.
     */
    public static final Map<DataComponentType<?>, StreamCodec<? super RegistryFriendlyByteBuf, ?>> OVERSIZE_OVERRIDES = Map.of(
        DataComponents.CHARGED_PROJECTILES, codec(ChargedProjectiles.STREAM_CODEC, ItemComponentSanitizer::sanitizeChargedProjectiles),
        DataComponents.BUNDLE_CONTENTS, codec(BundleContents.STREAM_CODEC, ItemComponentSanitizer::sanitizeBundleContents),
        DataComponents.CONTAINER, codec(ItemContainerContents.STREAM_CODEC, contents -> ItemContainerContents.EMPTY)
    );

    /*
     * This map defines overrides for specific DataComponentType entries that should be sanitized.
     *
     * Components critical to item functionality are not sanitized.
     * This map only applies to components that are patched ontop of items and should ignore its prototype.
     *
     * Any components not present in this map, the prototype value will be sent.
     */
    public static final Map<DataComponentType<?>, OverrideConfig<?>> SANITIZATION_OVERRIDES = Util.make(ImmutableMap.<DataComponentType<?>, OverrideConfig<?>>builder(), (map) -> {
            put(map, DataComponents.LODESTONE_TRACKER, empty(new LodestoneTracker(Optional.empty(), false)));
            put(map, DataComponents.ENCHANTMENTS, empty(dummyEnchantments()), true); // ALWAYS OVERRIDE - patched is different
            put(map, DataComponents.MAX_DAMAGE, empty(1));
            put(map, DataComponents.DAMAGE, empty(0));
            put(map, DataComponents.CUSTOM_NAME, empty(Component.empty()));
            put(map, DataComponents.TOOLTIP_STYLE, empty(ResourceLocation.parse("paper:fake-tooltip")));
            put(map, DataComponents.DEATH_PROTECTION, empty(new DeathProtection(List.of())));
            put(map, DataComponents.STORED_ENCHANTMENTS, empty(dummyEnchantments()));
            put(map, DataComponents.CAN_PLACE_ON, empty(new AdventureModePredicate(List.of(), true)));
            put(map, DataComponents.REPAIR_COST, empty(0));
            put(map, DataComponents.USE_REMAINDER, empty(new UseRemainder(ItemStack.EMPTY)));
            put(map, DataComponents.USE_COOLDOWN, empty(new UseCooldown(1, Optional.empty())));
            put(map, DataComponents.TOOL, empty(new Tool(List.of(), 1F, 1)));
            put(map, DataComponents.REPAIRABLE, empty(new Repairable(HolderSet.empty())));
            put(map, DataComponents.CUSTOM_DATA, empty(CustomData.EMPTY));
            put(map, DataComponents.POTION_CONTENTS, ItemComponentSanitizer::sanitizePotionContents);
            put(map, DataComponents.ENTITY_DATA, empty(CustomData.EMPTY));
            put(map, DataComponents.BUCKET_ENTITY_DATA, empty(CustomData.EMPTY));
            put(map, DataComponents.BLOCK_ENTITY_DATA, empty(CustomData.EMPTY));
            put(map, DataComponents.OMINOUS_BOTTLE_AMPLIFIER, empty(new OminousBottleAmplifier(0)));
            put(map, DataComponents.BEES, empty(List.of()));
            put(map, DataComponents.LOCK, empty(LockCode.NO_LOCK));
            put(map, DataComponents.WRITABLE_BOOK_CONTENT, empty(WritableBookContent.EMPTY));
            put(map, DataComponents.WRITTEN_BOOK_CONTENT, empty(WrittenBookContent.EMPTY));
            put(map, DataComponents.MAP_ID, empty(new MapId(0)));
        }
    ).build();

    private record OverrideConfig<T>(UnaryOperator<T> provider, boolean alwaysOverride) {
    }

    private static <T> void put(ImmutableMap.Builder map, DataComponentType<T> type, UnaryOperator<T> object) {
        put(map, type, object, false);
    }

    private static <T> void put(ImmutableMap.Builder map, DataComponentType<T> type, UnaryOperator<T> object, boolean alwaysOverride) {
        map.put(type, new OverrideConfig<>(object, alwaysOverride));
    }

    private static <T> UnaryOperator<T> empty(T object) {
        return (unused) -> object;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> StreamCodec<? super RegistryFriendlyByteBuf, T> get(DataComponentType<T> componentType, StreamCodec<? super RegistryFriendlyByteBuf, T> vanillaCodec) {
        // First check do we have our built in overrides, if so, override it.
        if (OVERSIZE_OVERRIDES.containsKey(componentType)) {
            return (StreamCodec<RegistryFriendlyByteBuf, T>) OVERSIZE_OVERRIDES.get(componentType);
        }

        // Now check the obfuscation, where we lookup if the type is overriden in any of the configurations, if so, wrap the codec
        GlobalConfiguration.Anticheat.Obfuscation.Items obfuscation = GlobalConfiguration.get().anticheat.obfuscation.items;
        if (obfuscation.enableItemObfuscation && DataSanitizationUtil.OVERRIDE_TYPES.contains(componentType)) {
            return codec(vanillaCodec, new DefaultDataComponentSanitizer<>(componentType, (OverrideConfig) SANITIZATION_OVERRIDES.get(componentType)));
        }

        return vanillaCodec;
    }

    // <editor-fold desc="Component Sanitizers" defaultstate="collapsed">

    private static PotionContents sanitizePotionContents(final PotionContents potionContents) {
        // We have a custom color! We can hide everything!
        if (potionContents.customColor().isPresent()) {
            return new PotionContents(Optional.empty(), potionContents.customColor(), List.of(), Optional.empty());
        }

        // WE cannot hide anything really, as the color is a mix of potion/potion contents, which can
        // possibly be reversed.
        return potionContents;
    }

    // We cant use the empty map from enchantments because we want to keep the glow
    private static ItemEnchantments dummyEnchantments() {
        ItemEnchantments.Mutable obj = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        obj.set(MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandom(RandomSource.create()).orElseThrow(), 1);
        return obj.toImmutable();
    }

    private static ChargedProjectiles sanitizeChargedProjectiles(final ChargedProjectiles projectiles) {
        if (projectiles.isEmpty()) {
            return projectiles;
        }

        return ChargedProjectiles.of(List.of(
            new ItemStack(projectiles.contains(Items.FIREWORK_ROCKET) ? Items.FIREWORK_ROCKET : Items.ARROW)
        ));
    }

    private static final ItemStack BUNDLE_ITEM_FILLER = Util.make(new ItemStack(Items.PAPER, 1), (itemStack) -> {
        if (GlobalConfiguration.get().anticheat.obfuscation.items.enableItemObfuscation) {
            itemStack.set(DataComponents.ITEM_MODEL, DataSanitizationUtil.IGNORE_OBFUSCATION_ITEM); // Prevent this item from being obfuscated
        }
    });

    // Although bundles no longer change their size based on fullness, fullness is exposed in item models.
    private static BundleContents sanitizeBundleContents(final BundleContents contents) {
        if (contents.isEmpty()) {
            return contents;
        }

        // A bundles content weight may be anywhere from 0 to, basically, infinity.
        // A weight of 1 is the usual maximum case
        int sizeUsed = Mth.mulAndTruncate(contents.weight(), 64);
        // Early out, *most* bundles should not be overfilled above a weight of one.
        if (sizeUsed <= 64) {
            return new BundleContents(List.of(BUNDLE_ITEM_FILLER.copyWithCount(Math.max(1, sizeUsed))));
        }

        final List<ItemStack> sanitizedRepresentation = new ObjectArrayList<>(sizeUsed / 64 + 1);
        while (sizeUsed > 0) {
            final int stackCount = Math.min(64, sizeUsed);
            sanitizedRepresentation.add(BUNDLE_ITEM_FILLER.copyWithCount(stackCount));
            sizeUsed -= stackCount;
        }
        // Now we add a single fake item that uses the same amount of slots as all other items.
        // Ensure that potentially overstacked bundles are not represented by empty (count=0) itemstacks.
        return new BundleContents(sanitizedRepresentation);
    }
    // </editor-fold>

    public static int sanitizeCount(ItemStack itemStack, int count) {
        if (DataSanitizationUtil.DATA_SANITIZER.get().isNotSanitizing()) {
            return count;
        }

        GlobalConfiguration.Anticheat.Obfuscation.Items items = GlobalConfiguration.get().anticheat.obfuscation.items;
        if (items.enableItemObfuscation && DataSanitizationUtil.getAssetObfuscation(itemStack).sanitizeCount()) {
            return 1;
        } else {
            return count;
        }
    }

    private static <B, A> StreamCodec<B, A> codec(final StreamCodec<B, A> delegate, final UnaryOperator<A> sanitizer) {
        return new DataSanitizationCodec<>(delegate, sanitizer);
    }

    // Serializer that will override the type depending on the asset of the item provided
    private record DefaultDataComponentSanitizer<A>(DataComponentType<?> type,
                                                    @Nullable OverrideConfig<A> override) implements UnaryOperator<A> {

        @Override
        public A apply(final A oldvalue) {
            DataSanitizationUtil.ContentScope scope = DataSanitizationUtil.DATA_SANITIZER.get().scope().get();
            ItemStack targetItemstack = scope.itemStack();
            // Does this asset override this component? If not, return oldValue.
            if (!DataSanitizationUtil.getAssetObfuscation(targetItemstack).sanitize().contains(this.type)) {
                return oldvalue;
            }
            // Is we need to force use override, do that
            if (this.override != null && this.override.alwaysOverride) {
                return this.override.provider.apply(oldvalue);
            }

            // If we can use the prototype, lets try to do that whenever possible
            A value = (A) scope.itemStack().getPrototype().get(this.type);
            if (value != null) {
                return value;
            } else {
                return this.override == null ? oldvalue : this.override.provider.apply(oldvalue);
            }
        }
    }


    // Codec used to override encoding if sanitization is enabled
    private record DataSanitizationCodec<B, A>(StreamCodec<B, A> delegate,
                                               UnaryOperator<A> sanitizer) implements StreamCodec<B, A> {

        @Override
        public @NonNull A decode(final @NonNull B buf) {
            return this.delegate.decode(buf);
        }

        @Override
        public void encode(final @NonNull B buf, final @NonNull A value) {
            if (DataSanitizationUtil.DATA_SANITIZER.get().isNotSanitizing()) {
                this.delegate.encode(buf, value);
            } else {
                this.delegate.encode(buf, this.sanitizer.apply(value));
            }
        }
    }

}
