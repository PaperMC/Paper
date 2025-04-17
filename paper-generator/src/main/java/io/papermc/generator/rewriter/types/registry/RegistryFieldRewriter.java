package io.papermc.generator.rewriter.types.registry;

import com.google.common.base.Suppliers;
import io.papermc.generator.registry.RegistryData;
import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.registry.RegistryEntry;
import io.papermc.generator.registry.RegistryIdentifiable;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.ExperimentalCollector;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.SourceFile;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlags;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.generator.utils.Formatting.quoted;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class RegistryFieldRewriter<T> extends SearchReplaceRewriter implements RegistryIdentifiable<T> {

    private final ResourceKey<? extends Registry<T>> registryKey;
    private final boolean isFilteredRegistry;
    private final @Nullable String fetchMethod;

    protected @MonotonicNonNull ClassNamed fieldClass;
    private @MonotonicNonNull Supplier<Map<ResourceKey<T>, SingleFlagHolder>> experimentalKeys;

    public RegistryFieldRewriter(ResourceKey<? extends Registry<T>> registryKey, @Nullable String fetchMethod) {
        this.registryKey = registryKey;
        this.isFilteredRegistry = FeatureElement.FILTERED_REGISTRIES.contains(registryKey);
        this.fetchMethod = fetchMethod;
    }

    @Override
    public ResourceKey<? extends Registry<T>> getRegistryKey() {
        return this.registryKey;
    }

    @Override
    public boolean registerFor(SourceFile file) {
        this.fieldClass = this.options.targetClass().orElse(file.mainClass());
        return super.registerFor(file);
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        RegistryEntry<T> entry = RegistryEntries.byRegistryKey(this.registryKey);
        boolean isInterface = entry.data().api().type() == RegistryData.Api.Type.INTERFACE;
        Registry<T> registry = entry.registry();
        this.experimentalKeys = Suppliers.memoize(() -> ExperimentalCollector.collectDataDrivenElementIds(registry));
        Iterator<Holder.Reference<T>> referenceIterator = registry.listElements().filter(this::canPrintField).sorted(Formatting.alphabeticKeyOrder(reference -> reference.key().location().getPath())).iterator();

        while (referenceIterator.hasNext()) {
            Holder.Reference<T> reference = referenceIterator.next();

            this.rewriteJavadocs(reference, metadata.replacedContent(), metadata.indent(), builder);

            SingleFlagHolder requiredFeature = this.getRequiredFeature(reference);
            if (requiredFeature != null) {
                Annotations.experimentalAnnotations(builder, metadata.indent(), this.importCollector, requiredFeature);
            }

            builder.append(metadata.indent());
            if (!isInterface) {
                builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));
            }

            builder.append(this.rewriteFieldType(reference)).append(' ').append(this.rewriteFieldName(reference));
            builder.append(" = ");
            builder.append(this.rewriteFieldValue(reference));
            builder.append(';');

            builder.append('\n');
            if (referenceIterator.hasNext()) {
                builder.append('\n');
            }
        }
    }

    protected void rewriteJavadocs(Holder.Reference<T> reference, String replacedContent, String indent, StringBuilder builder) {
    }

    protected boolean canPrintField(Holder.Reference<T> reference) {
        return true;
    }

    protected String rewriteFieldType(Holder.Reference<T> reference) {
        return this.fieldClass.simpleName();
    }

    protected String rewriteFieldName(Holder.Reference<T> reference) {
        return Formatting.formatKeyAsField(reference.key().location().getPath());
    }

    protected String rewriteFieldValue(Holder.Reference<T> reference) {
        return "%s(%s)".formatted(this.fetchMethod, quoted(reference.key().location().getPath()));
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
