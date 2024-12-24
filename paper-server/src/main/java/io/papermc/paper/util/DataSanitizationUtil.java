package io.papermc.paper.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import com.google.common.base.Preconditions;
import io.papermc.paper.configuration.GlobalConfiguration;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
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

    public static SafeAutoClosable passContext(final ItemStack itemStack) {
        final DataSanitizer sanitizer = DATA_SANITIZER.get();
        if (!sanitizer.isSanitizing()) {
            return () -> {}; // Dont pass any context
        }

        return new ContentScope(sanitizer.scope.get(), itemStack);
    }


    public static final Map<DataComponentType<?>, StreamCodec<? super RegistryFriendlyByteBuf, ?>> BUILT_IN_CODEC_OVERRIDES = Map.of(
        DataComponents.CHARGED_PROJECTILES, codec(ChargedProjectiles.STREAM_CODEC, DataSanitizationUtil::sanitizeChargedProjectiles),
        DataComponents.BUNDLE_CONTENTS, codec(BundleContents.STREAM_CODEC, DataSanitizationUtil::sanitizeBundleContents),
        DataComponents.CONTAINER, codec(ItemContainerContents.STREAM_CODEC, contents -> ItemContainerContents.EMPTY)
    );

    public static final Map<DataComponentType<?>, Object> EMPTY_MAP = Map.of(
        DataComponents.LODESTONE_TRACKER, new LodestoneTracker(Optional.empty(), false)
    );


    public static <T> StreamCodec<? super RegistryFriendlyByteBuf, T> get(DataComponentType<T> componentType, StreamCodec<? super RegistryFriendlyByteBuf, T> vanillaCodec) {
        // First check do we have our built in overrides, if so, override it.
        if (BUILT_IN_CODEC_OVERRIDES.containsKey(componentType)) {
            return (StreamCodec<RegistryFriendlyByteBuf, T>) BUILT_IN_CODEC_OVERRIDES.get(componentType);
        }

        // Now check the obfuscation, where we lookup if the type is overriden in any of the configurations, if so, wrap the codec
        GlobalConfiguration.Anticheat.Obfuscation.Items obfuscation = GlobalConfiguration.get().anticheat.obfuscation.items;
        if (obfuscation.enableItemObfuscation && GlobalConfiguration.Anticheat.Obfuscation.Items.OVERRIDEN_TYPES.contains(componentType)) {
            return new DataSanitizationCodec<>(vanillaCodec, new DefaultDataComponentSanitizer<>(componentType, EMPTY_MAP.get(componentType)));
        }

        return vanillaCodec;
    }

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

    public static int sanitizeCount(ItemStack itemStack, int count) {
        GlobalConfiguration.Anticheat.Obfuscation.Items items = GlobalConfiguration.get().anticheat.obfuscation.items;
        if (items.enableItemObfuscation && items.getAssetObfuscation(itemStack).sanitizeCount()) {
            return 1;
        } else {
            return count;
        }
    }


    private record DefaultDataComponentSanitizer<A>(DataComponentType<?> type, @Nullable Object override) implements UnaryOperator<A> {

        @Override
        public A apply(final A oldvalue) {
            ContentScope scope = DATA_SANITIZER.get().scope.get();
            ItemStack targetItemstack = scope.itemStack;
            // Does this asset override this component? If not, return oldValue.
            if (!GlobalConfiguration.get().anticheat.obfuscation.items.getAssetObfuscation(targetItemstack).sanitizedComponents().contains(this.type)) {
                return oldvalue;
            }

            // We don't need to check if its overriden because we KNOW it is if we are serializing it over to the client.

            return (A) scope.itemStack.getPrototype().getOrDefault(this.type, this.override == null ? oldvalue : this.override);
        }
    }

    private record DataSanitizationCodec<B, A>(StreamCodec<B, A> delegate, UnaryOperator<A> sanitizer) implements StreamCodec<B, A> {

        @Override
        public @NonNull A decode(final @NonNull B buf) {
            return this.delegate.decode(buf);
        }

        @Override
        public void encode(final @NonNull B buf, final @NonNull A value) {
            if (!DATA_SANITIZER.get().isSanitizing()) {
                this.delegate.encode(buf, value);
            } else {
                this.delegate.encode(buf, this.sanitizer.apply(value));
            }
        }
    }

    public record DataSanitizer(AtomicBoolean value, AtomicReference<ContentScope> scope) implements SafeAutoClosable {

        public DataSanitizer() {
            this(new AtomicBoolean(false), new AtomicReference<>());
        }

        public void start() {
            this.value.compareAndSet(false, true);
        }

        @Override
        public void close() {
            this.value.compareAndSet(true, false);
        }

        public boolean isSanitizing() {
            return value().get();
        }
    }

    public record ContentScope(@Nullable ContentScope oldScope, ItemStack itemStack) implements SafeAutoClosable {
        public ContentScope {
            Preconditions.checkNotNull(DATA_SANITIZER.get(), "Expected data santizier content available");
        }

        @Override
        public void close() {
            DATA_SANITIZER.get().scope().set(this.oldScope);
        }
    }

    public interface SafeAutoClosable extends AutoCloseable {

        @Override
        void close();
    }

    private DataSanitizationUtil() {
    }
}
