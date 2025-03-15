package io.papermc.paper.util.sanitizer;

import io.papermc.paper.util.SafeAutoClosable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemContainerContents;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class OversizedItemComponentSanitizer {

    /*
    These represent codecs that are meant to help get rid of possibly big items by ALWAYS hiding this data.
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> CHARGED_PROJECTILES = codec(ChargedProjectiles.STREAM_CODEC, OversizedItemComponentSanitizer::sanitizeChargedProjectiles);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> CONTAINER = codec(ItemContainerContents.STREAM_CODEC, contents -> ItemContainerContents.EMPTY);
    public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> BUNDLE_CONTENTS = new StreamCodec<>() {
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
            try (final SafeAutoClosable ignored = ItemObfuscationSession.withContext(c -> c.level(ItemObfuscationSession.ObfuscationLevel.OVERSIZED))){
                BundleContents.STREAM_CODEC.encode(buffer, sanitizeBundleContents(value));
            }
        }
    };

    private static <B, A> StreamCodec<B, A> codec(final StreamCodec<B, A> delegate, final UnaryOperator<A> sanitizer) {
        return new DataSanitizationCodec<>(delegate, sanitizer);
    }

    private static ChargedProjectiles sanitizeChargedProjectiles(final ChargedProjectiles projectiles) {
        if (projectiles.isEmpty()) {
            return projectiles;
        }

        return ChargedProjectiles.of(List.of(
            new ItemStack(
                projectiles.contains(Items.FIREWORK_ROCKET)
                    ? Items.FIREWORK_ROCKET
                    : Items.ARROW
            )));
    }

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
            return new BundleContents(List.of(new ItemStack(Items.PAPER, Math.max(1, sizeUsed))));
        }

        final List<ItemStack> sanitizedRepresentation = new ObjectArrayList<>(sizeUsed / 64 + 1);
        while (sizeUsed > 0) {
            final int stackCount = Math.min(64, sizeUsed);
            sanitizedRepresentation.add(new ItemStack(Items.PAPER, stackCount));
            sizeUsed -= stackCount;
        }
        // Now we add a single fake item that uses the same amount of slots as all other items.
        // Ensure that potentially overstacked bundles are not represented by empty (count=0) itemstacks.
        return new BundleContents(sanitizedRepresentation);
    }

    // Codec used to override encoding if sanitization is enabled
    private record DataSanitizationCodec<B, A>(StreamCodec<B, A> delegate,
                                               UnaryOperator<A> sanitizer) implements StreamCodec<B, A> {

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

}
