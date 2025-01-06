package io.papermc.generator;

import com.google.common.util.concurrent.MoreExecutors;
import com.mojang.logging.LogUtils;
import io.papermc.generator.rewriter.registration.PaperPatternSourceSetRewriter;
import io.papermc.generator.rewriter.registration.PatternSourceSetRewriter;
import io.papermc.generator.types.SourceGenerator;
import io.papermc.generator.utils.experimental.ExperimentalCollector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.flag.FeatureFlags;
import org.apache.commons.io.file.PathUtils;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

@NullMarked
public class Main {

    private static final Logger LOGGER = LogUtils.getLogger();
    public static final RegistryAccess.Frozen REGISTRY_ACCESS;
    public static final Map<TagKey<?>, String> EXPERIMENTAL_TAGS;

    static {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        PackRepository resourceRepository = ServerPacksSource.createVanillaTrustedRepository();
        resourceRepository.reload();
        MultiPackResourceManager resourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, resourceRepository.getAvailablePacks().stream().map(Pack::open).toList());
        LayeredRegistryAccess<RegistryLayer> layers = RegistryLayer.createRegistryAccess();
        List<Registry.PendingTags<?>> pendingTags = TagLoader.loadTagsForExistingRegistries(resourceManager, layers.getLayer(RegistryLayer.STATIC));
        List<HolderLookup.RegistryLookup<?>> worldGenLayer = TagLoader.buildUpdatedLookups(layers.getAccessForLoading(RegistryLayer.WORLDGEN), pendingTags);
        RegistryAccess.Frozen frozenWorldgenRegistries = RegistryDataLoader.load(resourceManager, worldGenLayer, RegistryDataLoader.WORLDGEN_REGISTRIES);
        layers = layers.replaceFrom(RegistryLayer.WORLDGEN, frozenWorldgenRegistries);
        REGISTRY_ACCESS = layers.compositeAccess().freeze();
        ReloadableServerResources reloadableServerResources = ReloadableServerResources.loadResources(
            resourceManager,
            layers,
            pendingTags,
            FeatureFlags.VANILLA_SET,
            Commands.CommandSelection.DEDICATED,
            0,
            MoreExecutors.directExecutor(),
            MoreExecutors.directExecutor()
        ).join();
        reloadableServerResources.updateStaticRegistryTags();
        EXPERIMENTAL_TAGS = ExperimentalCollector.collectTags(resourceManager);
    }

    private Main() {
    }

    public static class Rewriter extends Main {

        public static void main(String[] args) {
            boolean isApi = args[0].endsWith("-api");
            PatternSourceSetRewriter sourceSet = args.length >= 2 ? PaperPatternSourceSetRewriter.from(args[1]) : new PaperPatternSourceSetRewriter();
            (isApi ? Rewriters.API : Rewriters.SERVER).accept(sourceSet);
            try {
                sourceSet.apply(Path.of(args[0], "src/main/java"));
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Generator extends Main {

        public static void main(String[] args) {
            boolean isApi = args[0].endsWith("-api");

            try {
                generate(Path.of(args[0]), isApi ? Generators.API : Generators.SERVER);
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private static void generate(Path sourceSet, Collection<SourceGenerator> generators) throws IOException {
            Path output = sourceSet.resolve("src/generated/java");
            if (Files.exists(output)) {
                PathUtils.deleteDirectory(output);
            }

            for (SourceGenerator generator : generators) {
                generator.writeToFile(output);
            }
            LOGGER.info("Files written to {}", output.toAbsolutePath());
        }
    }
}
