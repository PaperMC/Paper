package org.bukkit.support;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.tags.TagLoader;
import net.minecraft.util.Util;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public final class RegistryHelper {

    private static SetupContext setupContext;

    private RegistryHelper() {
    }

    public static RegistryAccess registryAccess() {
        return context().registries();
    }

    public static SetupContext context() {
        if (setupContext == null) {
            throw new IllegalStateException("""
                Trying to access shared context while it is not setup.
                Make sure that either the class or method you test has the right test environment annotation present.
                You can find them in the package src/test/java/org.bukkit.support.environment""");
        }
        return setupContext;
    }

    public record SetupContext(
        ReloadableServerResources datapack,
        RegistryAccess registries,
        Supplier<PalettedContainerFactory> palettedContainerFactory
    ) {
    }

    public record PackedRegistries(LayeredRegistryAccess<RegistryLayer> layers, List<Registry.PendingTags<?>> pendingTags) {

        public RegistryAccess access() {
            return this.layers.compositeAccess().freeze();
        }
    }

    public static PackedRegistries createRegistries(FeatureFlagSet enabledFeatures) {
        return createRegistries(RegistryHelper.createResourceManager(enabledFeatures));
    }

    public static PackedRegistries createRegistries(ResourceManager resourceManager) {
        // add tags and loot tables for unit tests
        LayeredRegistryAccess<RegistryLayer> layers = RegistryLayer.createRegistryAccess();
        List<Registry.PendingTags<?>> pendingTags = TagLoader.loadTagsForExistingRegistries(resourceManager, layers.getLayer(RegistryLayer.STATIC));

        List<HolderLookup.RegistryLookup<?>> lookupsWithPendingTags = TagLoader.buildUpdatedLookups(layers.getAccessForLoading(RegistryLayer.WORLDGEN), pendingTags);
        RegistryAccess.Frozen worldGenRegistries = RegistryDataLoader.load(resourceManager, lookupsWithPendingTags, RegistryDataLoader.WORLDGEN_REGISTRIES);
        layers = layers.replaceFrom(RegistryLayer.WORLDGEN, worldGenRegistries);

        List<HolderLookup.RegistryLookup<?>> staticAndWorldgenLookups = Stream.concat(lookupsWithPendingTags.stream(), worldGenRegistries.listRegistries()).toList();
        RegistryAccess.Frozen dimensionRegistries = RegistryDataLoader.load(resourceManager, staticAndWorldgenLookups, RegistryDataLoader.DIMENSION_REGISTRIES);
        layers = layers.replaceFrom(RegistryLayer.DIMENSIONS, dimensionRegistries);
        // load registry here to ensure bukkit object registry are correctly delayed if needed
        try {
            Class.forName(org.bukkit.Registry.class.getName());
        } catch (final ClassNotFoundException ignored) {
        }

        return new PackedRegistries(layers, pendingTags);
    }

    public static void setup(FeatureFlagSet enabledFeatures) {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        Bootstrap.validate();

        ResourceManager resourceManager = RegistryHelper.createResourceManager(enabledFeatures);
        PackedRegistries registries = createRegistries(resourceManager);

        // Register vanilla packs
        ReloadableServerResources datapack = ReloadableServerResources.loadResources(resourceManager, registries.layers(), registries.pendingTags(), enabledFeatures, Commands.CommandSelection.DEDICATED, LevelBasedPermissionSet.ALL_PERMISSIONS, Util.backgroundExecutor(), Runnable::run).join();
        // Bind tags
        datapack.updateStaticRegistryTags();

        RegistryAccess registryAccess = registries.access();
        setupContext = new SetupContext(
            datapack,
            registryAccess,
            () -> PalettedContainerFactory.create(registryAccess)
        );
    }

    public static <T extends Keyed> Class<T> getFieldType(Class<T> apiClass, NamespacedKey key) {
        Class<T> fieldType = apiClass;
        // Some registries have extra Typed classes such as BlockType and ItemType.
        // To avoid class cast exceptions during init mock the Typed class.
        // To get the correct class, we just use the field type.
        try {
            fieldType = (Class<T>) apiClass.getField(formatKeyAsField(key.getKey())).getType();
        } catch (NoSuchFieldException e) {
            // continue with the less accurate type
        } catch (ClassCastException e) {
            throw new RuntimeException(e);
        }

        return fieldType;
    }

    private static MultiPackResourceManager createResourceManager(FeatureFlagSet enabledFeatures) {
        PackRepository packRepository = ServerPacksSource.createVanillaTrustedRepository();
        MinecraftServer.configurePackRepository(packRepository, new WorldDataConfiguration(new DataPackConfig(FeatureFlags.REGISTRY.toNames(enabledFeatures).stream().map(Identifier::getPath).toList(), List.of()), enabledFeatures), true, false);
        return new MultiPackResourceManager(PackType.SERVER_DATA, packRepository.openAllSelected());
    }

    private static final Pattern ILLEGAL_FIELD_CHARACTERS = Pattern.compile("[.-/]");

    public static String formatKeyAsField(String path) {
        return ILLEGAL_FIELD_CHARACTERS.matcher(path.toUpperCase(Locale.ENGLISH)).replaceAll("_");
    }
}
