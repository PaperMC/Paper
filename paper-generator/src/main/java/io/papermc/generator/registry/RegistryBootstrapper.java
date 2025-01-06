package io.papermc.generator.registry;

import io.papermc.generator.rewriter.registration.PatternSourceSetRewriter;
import io.papermc.generator.rewriter.types.Types;
import io.papermc.generator.rewriter.types.registry.PaperRegistriesRewriter;
import io.papermc.generator.rewriter.types.registry.RegistryEventsRewriter;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.types.registry.GeneratedKeyType;
import io.papermc.generator.types.registry.GeneratedTagKeyType;
import io.papermc.paper.registry.event.RegistryEvents;
import java.util.List;
import net.minecraft.core.registries.Registries;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class RegistryBootstrapper {

    private static final String PAPER_REGISTRY_PACKAGE = "io.papermc.paper.registry";

    public static void bootstrap(List<SourceGenerator> generators) {
        // typed/tag keys
        RegistryEntries.forEach(entry -> {
            generators.add(new GeneratedKeyType<>(PAPER_REGISTRY_PACKAGE + ".keys", entry));
            if (entry.registry().listTags().findAny().isPresent()) {
                generators.add(new GeneratedTagKeyType(entry, PAPER_REGISTRY_PACKAGE + ".keys.tags"));
            }
        });

        // todo remove once entity type is a registry
        generators.add(new GeneratedTagKeyType(RegistryEntries.byRegistryKey(Registries.ENTITY_TYPE), PAPER_REGISTRY_PACKAGE + ".keys.tags"));
    }

    public static void bootstrap(PatternSourceSetRewriter apiSourceSet, PatternSourceSetRewriter serverSourceSet) {
        bootstrapApi(apiSourceSet);
        bootstrapServer(serverSourceSet);
    }

    public static void bootstrapApi(PatternSourceSetRewriter sourceSet) {
        sourceSet.register("RegistryEvents", RegistryEvents.class, new RegistryEventsRewriter());
    }

    public static void bootstrapServer(PatternSourceSetRewriter sourceSet) {
        sourceSet.register("RegistryDefinitions", Types.PAPER_REGISTRIES, new PaperRegistriesRewriter());
    }
}
