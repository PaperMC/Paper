package io.papermc.paper.util;

import com.google.common.base.Preconditions;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class DataSanitizationUtil {

    static final ThreadLocal<DataSanitizer> DATA_SANITIZER = ThreadLocal.withInitial(DataSanitizer::new);

    static BoundObfuscationConfiguration BOUND_BASE = null;
    static Map<ResourceLocation, BoundObfuscationConfiguration> BOUND_OVERRIDES = new HashMap<>();
    // We need to have a special ignore item to indicate this item should not be obfuscated.
    // Randomize the namespace to ensure servers dont try to utilize this.
    static ResourceLocation IGNORE_OBFUSCATION_ITEM = ResourceLocation.tryParse("paper:ignore_obfuscation_item/" + UUID.randomUUID());

    public static void compute(GlobalConfiguration.Anticheat.Obfuscation.Items items) {
        // now bind them all
        BOUND_BASE = bind(items.allModels);
        for (Map.Entry<String, GlobalConfiguration.Anticheat.Obfuscation.Items.AssetObfuscationConfiguration> entry : items.modelOverrides.entrySet()) {
            BOUND_OVERRIDES.put(ResourceLocation.parse(entry.getKey()), bind(entry.getValue()));
        }
        BOUND_OVERRIDES.put(IGNORE_OBFUSCATION_ITEM, new BoundObfuscationConfiguration(false, Map.of()));
    }

    public static boolean shouldDrop(DataComponentType<?> key) {
        return ItemComponentSanitizer.shouldDrop(key);
    }

    public static Optional<?> override(DataComponentType<?> key, Optional<?> value) {
        return ItemComponentSanitizer.override(key, value);
    }

    public record BoundObfuscationConfiguration(
        boolean sanitizeCount, Map<DataComponentType<?>, MutationType> patchStrategy
        ) {

        sealed interface MutationType permits MutationType.Drop, MutationType.Sanitize {

            enum Drop implements MutationType {
                INSTANCE
            }

            record Sanitize(UnaryOperator sanitizer) implements MutationType {

            }
        }
    }

    public static BoundObfuscationConfiguration bind(GlobalConfiguration.Anticheat.Obfuscation.Items.AssetObfuscationConfiguration config) {
        Set<DataComponentType<?>> base = new HashSet<>(GlobalConfiguration.Anticheat.Obfuscation.Items.BASE_OVERRIDERS);
        base.addAll(config.alsoObfuscate());
        base.removeAll(config.dontObfuscate());

        Map<DataComponentType<?>, BoundObfuscationConfiguration.MutationType> finalStrategy = new HashMap<>();
        // Configure what path the data component should go through, should it be dropped, or should it be sanitized?
        for (DataComponentType<?> type : base) {
            // We require some special logic, sanitize it rather than dropping it.
            UnaryOperator<?> sanitizationOverride = ItemComponentSanitizer.SANITIZATION_OVERRIDES.get(type);
            if (sanitizationOverride != null) {
                finalStrategy.put(type, new BoundObfuscationConfiguration.MutationType.Sanitize(sanitizationOverride));
            } else {
                finalStrategy.put(type, BoundObfuscationConfiguration.MutationType.Drop.INSTANCE);
            }
        }

        return new BoundObfuscationConfiguration(config.sanitizeCount(), finalStrategy);
    }

    public static BoundObfuscationConfiguration getAssetObfuscation(ItemStack resourceLocation) {
        return BOUND_OVERRIDES.getOrDefault(resourceLocation.get(DataComponents.ITEM_MODEL), BOUND_BASE);
    }

    public static DataSanitizer start(final boolean sanitize) {
        final DataSanitizer sanitizer = DATA_SANITIZER.get();
        if (sanitize) {
            sanitizer.start();
        }
        return sanitizer;
    }

    public static SafeAutoClosable passContext(final ItemStack itemStack) {
        final DataSanitizer sanitizer = DATA_SANITIZER.get();
        if (sanitizer.isNotSanitizing()) {
            return () -> {}; // Dont pass any context
        }

        ContentScope closable = new ContentScope(sanitizer.scope.get(), itemStack);;
        sanitizer.scope.set(closable);
        return closable;
    }

    public static int sanitizeCount(ItemStack itemStack, int count) {
        return ItemComponentSanitizer.sanitizeCount(itemStack, count);
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

        public boolean isNotSanitizing() {
            return !value().get();
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
