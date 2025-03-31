package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Locale;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true)
public class CraftPotionUtilRewriter extends SearchReplaceRewriter {

    private final String statePrefix;

    public CraftPotionUtilRewriter(String statePrefix) {
        this.statePrefix = statePrefix;
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        String upperStatePrefix = this.statePrefix.toUpperCase(Locale.ENGLISH);
        ClassNamed potionType = RegistryEntries.byRegistryKey(Registries.POTION).data().api().klass().name();
        BuiltInRegistries.POTION.keySet().stream()
            .filter(key -> BuiltInRegistries.POTION.containsKey(key.withPath(path -> this.statePrefix + "_" + path)))
            .sorted(Formatting.alphabeticKeyOrder(ResourceLocation::getPath)).forEach(key -> {
                String keyedName = Formatting.formatKeyAsField(key.getPath());
                builder.append(metadata.indent());
                builder.append(".put(%1$s.%2$s, %1$s.%3$s_%4$s)".formatted(potionType.simpleName(), keyedName, upperStatePrefix, keyedName));
                builder.append('\n');
            });
    }
}
