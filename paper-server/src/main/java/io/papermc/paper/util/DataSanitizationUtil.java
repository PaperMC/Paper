package io.papermc.paper.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemContainerContents;
import org.apache.commons.lang3.math.Fraction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class DataSanitizationUtil {

    private static final ThreadLocal<DataSanitizer> DATA_SANITIZER = ThreadLocal.withInitial(DataSanitizer::new);

    public static DataSanitizer start(final boolean sanitize) {
        final DataSanitizer sanitizer = DATA_SANITIZER.get();
        if (sanitize) {
            sanitizer.start();
        }
        return sanitizer;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, ChargedProjectiles> CHARGED_PROJECTILES = codec(ChargedProjectiles.STREAM_CODEC, DataSanitizationUtil::sanitizeChargedProjectiles);
    public static final StreamCodec<RegistryFriendlyByteBuf, BundleContents> BUNDLE_CONTENTS = codec(BundleContents.STREAM_CODEC, DataSanitizationUtil::sanitizeBundleContents);
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> CONTAINER = codec(ItemContainerContents.STREAM_CODEC, contents -> ItemContainerContents.EMPTY);

    private static ChargedProjectiles sanitizeChargedProjectiles(final ChargedProjectiles projectiles) {
        if (projectiles.isEmpty()) {
            return projectiles;
        }

        return ChargedProjectiles.of(List.of(
            new ItemStack(projectiles.contains(Items.FIREWORK_ROCKET) ? Items.FIREWORK_ROCKET : Items.ARROW)
        ));
    }

    private static BundleContents sanitizeBundleContents(final BundleContents contents) {
        if (contents.isEmpty()) {
            return contents;
        }

        // Bundles change their texture based on their fullness.
        // A bundles content weight may be anywhere from 0 to, basically, infinity.
        // A weight of 1 is the usual maximum case
        int sizeUsed = Mth.mulAndTruncate(contents.weight(), 64);
        // Early out, *most* bundles should not be overfilled above a weight of one.
        if (sizeUsed <= 64) return new BundleContents(List.of(new ItemStack(Items.PAPER, Math.max(1, sizeUsed))));

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

    private static <B, A> StreamCodec<B, A> codec(final StreamCodec<B, A> delegate, final UnaryOperator<A> sanitizer) {
        return new DataSanitizationCodec<>(delegate, sanitizer);
    }

    private record DataSanitizationCodec<B, A>(StreamCodec<B, A> delegate, UnaryOperator<A> sanitizer) implements StreamCodec<B, A> {

        @Override
        public @NonNull A decode(final @NonNull B buf) {
            return this.delegate.decode(buf);
        }

        @Override
        public void encode(final @NonNull B buf, final @NonNull A value) {
            if (!DATA_SANITIZER.get().value().get()) {
                this.delegate.encode(buf, value);
            } else {
                this.delegate.encode(buf, this.sanitizer.apply(value));
            }
        }
    }

    public record DataSanitizer(AtomicBoolean value) implements AutoCloseable {

        public DataSanitizer() {
            this(new AtomicBoolean(false));
        }

        public void start() {
            this.value.compareAndSet(false, true);
        }

        @Override
        public void close() {
            this.value.compareAndSet(true, false);
        }
    }

    private DataSanitizationUtil() {
    }
}
