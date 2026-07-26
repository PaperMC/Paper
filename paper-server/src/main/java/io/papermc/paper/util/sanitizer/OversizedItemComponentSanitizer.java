package io.papermc.paper.util.sanitizer;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.papermc.paper.configuration.GlobalConfiguration;
import io.papermc.paper.util.SafeAutoClosable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemContainerContents;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

public final class OversizedItemComponentSanitizer {

    /*
    These represent codecs that are meant to help get rid of possibly big items by ALWAYS hiding this data.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> CHARGED_PROJECTILES_STREAM = codec(ChargedProjectiles.STREAM_CODEC, OversizedItemComponentSanitizer::sanitizeChargedProjectiles);
    public static final Codec<ChargedProjectiles> CHARGED_PROJECTILES = codec(ChargedProjectiles.CODEC, OversizedItemComponentSanitizer::sanitizeChargedProjectiles);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> CONTAINER_STREAM = codec(ItemContainerContents.STREAM_CODEC, OversizedItemComponentSanitizer::sanitizeItemContainerContents);
    public static final Codec<ItemContainerContents> CONTAINER = codec(ItemContainerContents.CODEC, OversizedItemComponentSanitizer::sanitizeItemContainerContents);
    public static final Codec<BundleContents> BUNDLE_CONTENTS = new Codec<>() {

        @Override
        public <T> DataResult<Pair<BundleContents, T>> decode(final DynamicOps<T> ops, final T input) {
            return BundleContents.CODEC.decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(final BundleContents input, final DynamicOps<T> ops, final T prefix) {
            if (!ItemObfuscationSession.currentSession().obfuscationLevel().obfuscateOversized()) {
                return BundleContents.CODEC.encode(input, ops, prefix);
            }

            // Disable further obfuscation to skip e.g. count.
            try (final SafeAutoClosable ignored = ItemObfuscationSession.withContext(c -> c.level(ItemObfuscationSession.ObfuscationLevel.OVERSIZED))) {
                return BundleContents.CODEC.encode(sanitizeBundleContents(input), ops, prefix);
            }
        }
    };
    public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> BUNDLE_CONTENTS_STREAM = new StreamCodec<>() {
        @Override
        public BundleContents decode(final RegistryFriendlyByteBuf buffer) {
            return BundleContents.STREAM_CODEC.decode(buffer);
        }

        @Override
        public void encode(final RegistryFriendlyByteBuf buffer, final BundleContents value) {
            if (!ItemObfuscationSession.currentSession().obfuscationLevel().obfuscateOversized()) {
                BundleContents.STREAM_CODEC.encode(buffer, value);
                return;
            }

            // Disable further obfuscation to skip e.g. count.
            try (final SafeAutoClosable ignored = ItemObfuscationSession.withContext(c -> c.level(ItemObfuscationSession.ObfuscationLevel.OVERSIZED))) {
                BundleContents.STREAM_CODEC.encode(buffer, sanitizeBundleContents(value));
            }
        }
    };

    private static <B, A> StreamCodec<B, A> codec(final StreamCodec<B, A> delegate, final UnaryOperator<A> sanitizer) {
        return new DataSanitizationStreamCodec<>(delegate, sanitizer);
    }

    private static <A> Codec<A> codec(final Codec<A> delegate, final UnaryOperator<A> sanitizer) {
        return new DataSanitizationCodec<>(delegate, sanitizer);
    }

    private static ChargedProjectiles sanitizeChargedProjectiles(final ChargedProjectiles projectiles) {
        if (projectiles.isEmpty()) {
            return projectiles;
        }

        if (GlobalConfiguration.get().unsupportedSettings.oversizedItemComponentSanitizer.dontSanitize().contains(DataComponents.CHARGED_PROJECTILES)) {
            return projectiles;
        }

        return ChargedProjectiles.of(new ItemStackTemplate(projectiles.contains(Items.FIREWORK_ROCKET) ? Items.FIREWORK_ROCKET : Items.ARROW));
    }

    private static ItemContainerContents sanitizeItemContainerContents(final ItemContainerContents contents) {
        if (GlobalConfiguration.get().unsupportedSettings.oversizedItemComponentSanitizer.dontSanitize().contains(DataComponents.CONTAINER)) {
            return contents;
        }
        return ItemContainerContents.EMPTY;
    }

    // Although bundles no longer change their size based on fullness, fullness is exposed in item models.
    private static BundleContents sanitizeBundleContents(final BundleContents contents) {
        if (contents.isEmpty()) {
            return contents;
        }

        if (GlobalConfiguration.get().unsupportedSettings.oversizedItemComponentSanitizer.dontSanitize().contains(DataComponents.BUNDLE_CONTENTS)) {
            return contents;
        }

        // A bundles content weight may be anywhere from 0 to, basically, infinity.
        // A weight of 1 is the usual maximum case
        int sizeUsed = Mth.mulAndTruncate(contents.weight().getOrThrow(), 64);
        // Early out, *most* bundles should not be overfilled above a weight of one.
        if (sizeUsed <= 64) {
            return new BundleContents(List.of(new ItemStackTemplate(Items.PAPER, Math.max(1, sizeUsed))));
        }

        final List<ItemStackTemplate> sanitizedRepresentation = new ObjectArrayList<>(sizeUsed / 64 + 1);
        while (sizeUsed > 0) {
            final int stackCount = Math.min(64, sizeUsed);
            sanitizedRepresentation.add(new ItemStackTemplate(Items.PAPER, stackCount));
            sizeUsed -= stackCount;
        }
        // Now we add a single fake item that uses the same amount of slots as all other items.
        // Ensure that potentially overstacked bundles are not represented by empty (count=0) itemstacks.
        return new BundleContents(sanitizedRepresentation);
    }

    // Empty marker interface for encoder cache checks
    public interface ObfuscationDependantCodec {
    }

    // Codec used to override encoding if sanitization is enabled
    private record DataSanitizationStreamCodec<B, A>(StreamCodec<B, A> delegate,
                                                     UnaryOperator<A> sanitizer) implements StreamCodec<B, A>, ObfuscationDependantCodec {

        @Override
        public @NonNull A decode(final @NonNull B buf) {
            return this.delegate.decode(buf);
        }

        @SuppressWarnings("resource")
        @Override
        public void encode(final @NonNull B buf, final @NonNull A value) {
            if (!ItemObfuscationSession.currentSession().obfuscationLevel().obfuscateOversized()) {
                this.delegate.encode(buf, value);
            } else {
                this.delegate.encode(buf, this.sanitizer.apply(value));
            }
        }
    }

    // Codec used to override encoding if sanitization is enabled
    private record DataSanitizationCodec<A>(Codec<A> delegate,
                                            UnaryOperator<A> sanitizer) implements Codec<A>, ObfuscationDependantCodec {

        @Override
        public <T> DataResult<Pair<A, T>> decode(final DynamicOps<T> ops, final T input) {
            return this.delegate.decode(ops, input);
        }

        @Override
        public <T> DataResult<T> encode(final A input, final DynamicOps<T> ops, final T prefix) {
            if (!ItemObfuscationSession.currentSession().obfuscationLevel().obfuscateOversized()) {
                return this.delegate.encode(input, ops, prefix);
            } else {
                return this.delegate.encode(this.sanitizer.apply(input), ops, prefix);
            }
        }
    }

    @ConfigSerializable
    public record AssetOversizedItemComponentSanitizerConfiguration(Set<DataComponentType<?>> dontSanitize) {
    }

}
