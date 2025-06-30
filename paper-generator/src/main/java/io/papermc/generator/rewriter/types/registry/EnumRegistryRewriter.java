package io.papermc.generator.rewriter.types.registry;

import com.google.common.base.Suppliers;
import io.papermc.generator.Main;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.ExperimentalCollector;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.preset.EnumRewriter;
import io.papermc.typewriter.preset.model.EnumValue;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlags;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.generator.utils.Formatting.quoted;

@NullMarked
@ApiStatus.Obsolete
public class EnumRegistryRewriter<T> extends EnumRewriter<Holder.Reference<T>> {

    private final Supplier<Registry<T>> registry;
    private final Supplier<Map<ResourceKey<T>, SingleFlagHolder>> experimentalKeys;
    private final boolean isFilteredRegistry;
    private final boolean hasKeyArgument;

    public EnumRegistryRewriter(ResourceKey<? extends Registry<T>> registryKey) {
        this(registryKey, true);
    }

    protected EnumRegistryRewriter(ResourceKey<? extends Registry<T>> registryKey, boolean hasKeyArgument) {
        this.registry = Suppliers.memoize(() -> Main.REGISTRY_ACCESS.lookupOrThrow(registryKey));
        this.experimentalKeys = Suppliers.memoize(() -> ExperimentalCollector.collectDataDrivenElementIds(this.registry.get()));
        this.isFilteredRegistry = FeatureElement.FILTERED_REGISTRIES.contains(registryKey);
        this.hasKeyArgument = hasKeyArgument;
    }

    @Override
    protected Iterable<Holder.Reference<T>> getValues() {
        return this.registry.get().listElements().sorted(Formatting.HOLDER_ORDER)::iterator;
    }

    @Override
    protected EnumValue.Builder rewriteEnumValue(Holder.Reference<T> reference) {
        EnumValue.Builder value = EnumValue.builder(Formatting.formatKeyAsField(reference.key().location().getPath()));
        if (this.hasKeyArgument) {
            value.argument(quoted(reference.key().location().getPath()));
        }
        return value;
    }

    @Override
    protected void appendEnumValue(Holder.Reference<T> reference, StringBuilder builder, String indent, boolean reachEnd) {
        // experimental annotation
        SingleFlagHolder requiredFeature = this.getRequiredFeature(reference);
        if (requiredFeature != null) {
            Annotations.experimentalAnnotations(builder, indent, this.importCollector, requiredFeature);
        }

        super.appendEnumValue(reference, builder, indent, reachEnd);
    }

    protected @Nullable SingleFlagHolder getRequiredFeature(Holder.Reference<T> reference) {
        if (this.isFilteredRegistry) {
            // built-in registry
            FeatureElement element = (FeatureElement) reference.value();
            if (FeatureFlags.isExperimental(element.requiredFeatures())) {
                return SingleFlagHolder.fromSet(element.requiredFeatures());
            }
        } else {
            // data-driven registry
            return this.experimentalKeys.get().get(reference.key());
        }
        return null;
    }
}
