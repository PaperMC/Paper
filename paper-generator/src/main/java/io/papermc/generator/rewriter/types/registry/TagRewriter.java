package io.papermc.generator.rewriter.types.registry;

import io.papermc.generator.Main;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import java.util.Locale;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.bukkit.Bukkit;
import org.bukkit.Fluid;
import org.bukkit.GameEvent;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static io.papermc.generator.utils.Formatting.quoted;

@NullMarked
@ApiStatus.Obsolete
public class TagRewriter extends SearchReplaceRewriter {

    public record TagRegistry(String legacyFolderName, Class<? extends Keyed> apiType, ResourceKey<? extends Registry<?>> registryKey) { // TODO remove Keyed
    }

    private static final TagRegistry[] SUPPORTED_REGISTRIES = { // 1.21 folder name are normalized to registry key but api will stay as is
        new TagRegistry("blocks", Material.class, Registries.BLOCK),
        new TagRegistry("items", Material.class, Registries.ITEM),
        new TagRegistry("fluids", Fluid.class, Registries.FLUID),
        new TagRegistry("entity_types", EntityType.class, Registries.ENTITY_TYPE),
        new TagRegistry("game_events", GameEvent.class, Registries.GAME_EVENT)
        // new TagRegistry("damage_types", DamageType.class, Registries.DAMAGE_TYPE) - separate in DamageTypeTags
    };

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        for (int i = 0, len = SUPPORTED_REGISTRIES.length; i < len; i++) {
            final TagRegistry tagRegistry = SUPPORTED_REGISTRIES[i];

            final ResourceKey<? extends Registry<?>> registryKey = tagRegistry.registryKey();
            final Registry<?> registry = Main.REGISTRY_ACCESS.lookupOrThrow(registryKey);

            final String fieldPrefix = Formatting.formatTagFieldPrefix(tagRegistry.legacyFolderName(), registryKey);
            final String registryFieldName = "REGISTRY_" + tagRegistry.legacyFolderName().toUpperCase(Locale.ENGLISH);

            if (i != 0) {
                builder.append('\n'); // extra line before the registry field
            }

            // registry name field
            builder.append(metadata.indent());
            builder.append("%s %s = %s;".formatted(String.class.getSimpleName(), registryFieldName, quoted(tagRegistry.legacyFolderName())));

            builder.append('\n');
            builder.append('\n');

            Iterator<? extends TagKey<?>> keyIterator = registry.listTagIds().sorted(Formatting.TAG_ORDER).iterator();

            while (keyIterator.hasNext()) {
                TagKey<?> tagKey = keyIterator.next();
                final String keyPath = tagKey.location().getPath();
                final String fieldName = fieldPrefix + Formatting.formatKeyAsField(keyPath);

                // tag field
                String featureFlagName = Main.EXPERIMENTAL_TAGS.get(tagKey);
                if (featureFlagName != null) {
                    Annotations.experimentalAnnotations(builder, metadata.indent(), this.importCollector, SingleFlagHolder.fromName(featureFlagName));
                }

                builder.append(metadata.indent());
                builder.append("%s<%s>".formatted(this.source.mainClass().simpleName(), this.importCollector.getShortName(tagRegistry.apiType()))).append(' ').append(fieldName);
                builder.append(" = ");
                builder.append("%s.getTag(%s, %s.minecraft(%s), %s.class)".formatted(Bukkit.class.getSimpleName(), registryFieldName, NamespacedKey.class.getSimpleName(), quoted(keyPath), tagRegistry.apiType().getSimpleName())); // assume type is imported properly
                builder.append(';');

                builder.append('\n');
                if (keyIterator.hasNext()) {
                    builder.append('\n');
                }
            }
        }
    }
}
