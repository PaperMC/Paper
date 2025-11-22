package io.papermc.generator;

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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import org.apache.commons.io.file.PathUtils;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.slf4j.Logger;
import picocli.CommandLine;

@CommandLine.Command(
    name = "generator",
    description = "Rewrite and generate API classes and its implementation for Paper"
)
public class Main implements Callable<Integer> {

    @CommandLine.Option(names = {"--sourceset"}, required = true)
    Path sourceSet;

    @CommandLine.Option(names = {"-cp", "--classpath"}, split = "[;:]", required = true)
    Set<Path> classpath;

    @CommandLine.Option(names = {"--rewrite"})
    boolean isRewrite;

    @CommandLine.Option(names = {"--side"}, required = true)
    String side;

    @CommandLine.Option(names = {"--bootstrap-tags"})
    boolean tagBootstrap;

    private static final Logger LOGGER = LogUtils.getLogger();

    public static RegistryAccess.@MonotonicNonNull Frozen REGISTRY_ACCESS;
    public static @MonotonicNonNull Map<TagKey<?>, String> EXPERIMENTAL_TAGS;

    public static CompletableFuture<Void> bootStrap(boolean withTags) {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        PackRepository packRepository = ServerPacksSource.createVanillaTrustedRepository();
        FeatureFlagSet flags = FeatureFlags.REGISTRY.allFlags();
        MinecraftServer.configurePackRepository(packRepository, new WorldDataConfiguration(new DataPackConfig(FeatureFlags.REGISTRY.toNames(flags).stream().map(ResourceLocation::getPath).toList(), List.of()), flags), true, false);
        CloseableResourceManager resourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, packRepository.openAllSelected());

        LayeredRegistryAccess<RegistryLayer> layers = RegistryLayer.createRegistryAccess();
        List<Registry.PendingTags<?>> pendingTags = TagLoader.loadTagsForExistingRegistries(resourceManager, layers.getLayer(RegistryLayer.STATIC));
        List<HolderLookup.RegistryLookup<?>> worldGenLayer = TagLoader.buildUpdatedLookups(layers.getAccessForLoading(RegistryLayer.WORLDGEN), pendingTags);
        RegistryAccess.Frozen frozenWorldgenRegistries = RegistryDataLoader.load(resourceManager, worldGenLayer, RegistryDataLoader.WORLDGEN_REGISTRIES);
        layers = layers.replaceFrom(RegistryLayer.WORLDGEN, frozenWorldgenRegistries);
        List<HolderLookup.RegistryLookup<?>> staticAndWorldgenLookups = Stream.concat(worldGenLayer.stream(), frozenWorldgenRegistries.listRegistries()).toList();
        RegistryAccess.Frozen dimensionRegistries = RegistryDataLoader.load(resourceManager, staticAndWorldgenLookups, RegistryDataLoader.DIMENSION_REGISTRIES);
        layers = layers.replaceFrom(RegistryLayer.DIMENSIONS, dimensionRegistries);
        REGISTRY_ACCESS = layers.compositeAccess().freeze();
        if (withTags) {
            return ReloadableServerResources.loadResources(
                resourceManager,
                layers,
                pendingTags,
                flags,
                Commands.CommandSelection.DEDICATED,
                Commands.LEVEL_GAMEMASTERS,
                Util.backgroundExecutor(),
                Runnable::run
            ).whenComplete((result, ex) -> {
                if (ex != null) {
                    resourceManager.close();
                }
            }).thenAccept(resources -> {
                resources.updateStaticRegistryTags();
                EXPERIMENTAL_TAGS = ExperimentalCollector.collectTags(resourceManager);
            });
        } else {
            EXPERIMENTAL_TAGS = Map.of();
            return CompletableFuture.completedFuture(null);
        }
    }

    @Override
    public Integer call() {
        bootStrap(this.tagBootstrap).join();

        try {
            if (this.isRewrite) {
                rewrite(this.sourceSet, this.classpath, this.side.equals("api") ? Rewriters.API : Rewriters.SERVER);
            } else {
                generate(this.sourceSet, this.side.equals("api") ? Generators.API : Generators.SERVER);
            }
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private static void rewrite(Path sourceSet, Set<Path> classpath, Consumer<PatternSourceSetRewriter> rewriters) throws IOException {
        PatternSourceSetRewriter sourceSetRewriter = new PaperPatternSourceSetRewriter(classpath);
        rewriters.accept(sourceSetRewriter);
        sourceSetRewriter.apply(sourceSet.resolve("src/main/java"));
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

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }
}
