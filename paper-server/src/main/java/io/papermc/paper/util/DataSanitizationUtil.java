package io.papermc.paper.util;

import com.google.common.base.Preconditions;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class DataSanitizationUtil {

    static final ThreadLocal<DataSanitizer> DATA_SANITIZER = ThreadLocal.withInitial(DataSanitizer::new);

    // <editor-fold desc="Cache Optimization" defaultstate="collapsed">
    static Set<DataComponentType<?>> OVERRIDE_TYPES = new HashSet<>();
    static BoundObfuscationConfiguration BOUND_BASE = null;
    static Map<ResourceLocation, BoundObfuscationConfiguration> BOUND_OVERRIDES = new HashMap<>();
    // We need to have a special ignore item to indicate this item should not be obfuscated.
    // Randomize the namespace to ensure servers dont try to utilize this.
    static ResourceLocation IGNORE_OBFUSCATION_ITEM = ResourceLocation.tryParse("paper:ignore_obfuscation_item/" + UUID.randomUUID());

    public static void compute(GlobalConfiguration.Anticheat.Obfuscation.Items items) {

        OVERRIDE_TYPES.addAll(GlobalConfiguration.Anticheat.Obfuscation.Items.BASE_OVERRIDERS);

        // Add any possible new types obfuscated
        OVERRIDE_TYPES.addAll(items.allModels.alsoObfuscate());
        for (GlobalConfiguration.Anticheat.Obfuscation.Items.AssetObfuscationConfiguration configuration : items.modelOverrides.values()) {
            OVERRIDE_TYPES.addAll(configuration.alsoObfuscate());
        }

        // now bind them all
        BOUND_BASE = bind(items.allModels);
        for (Map.Entry<String, GlobalConfiguration.Anticheat.Obfuscation.Items.AssetObfuscationConfiguration> entry : items.modelOverrides.entrySet()) {
            BOUND_OVERRIDES.put(ResourceLocation.parse(entry.getKey()), bind(entry.getValue()));
        }
        BOUND_OVERRIDES.put(IGNORE_OBFUSCATION_ITEM, new BoundObfuscationConfiguration(false, Set.of()));
    }

    public record BoundObfuscationConfiguration(boolean sanitizeCount, Set<DataComponentType<?>> sanitize) {

    }

    public static BoundObfuscationConfiguration bind(GlobalConfiguration.Anticheat.Obfuscation.Items.AssetObfuscationConfiguration config) {
        Set<DataComponentType<?>> base = new HashSet<>(GlobalConfiguration.Anticheat.Obfuscation.Items.BASE_OVERRIDERS);
        base.addAll(config.alsoObfuscate());
        base.removeAll(config.dontObfuscate());

        return new BoundObfuscationConfiguration(config.sanitizeCount(), base);
    }

    public static void bindCodecs() {
        BuiltInRegistries.DATA_COMPONENT_TYPE.stream().forEach(DataComponentType::streamCodec); // populate the consumers
        OVERRIDE_TYPES.clear(); // We dont need this anymore
    }
    // </editor-fold>

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

    public static <T> StreamCodec<? super RegistryFriendlyByteBuf, T> get(DataComponentType<T> componentType, StreamCodec<? super RegistryFriendlyByteBuf, T> vanillaCodec) {
        return ItemComponentSanitizer.get(componentType, vanillaCodec);
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
