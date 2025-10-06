package org.bukkit.support;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.List;
import java.util.Locale;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.PalettedContainerFactory;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public final class RegistryHelper {

    private static ReloadableServerResources dataPack;
    private static RegistryAccess.Frozen registry;
    private static Registry<Biome> biomes;
    private static PalettedContainerFactory palettedContainerFactory;

    private RegistryHelper() {
    }

    public static ReloadableServerResources getDataPack() {
        if (RegistryHelper.dataPack == null) {
            RegistryHelper.throwError("dataPack");
        }
        return RegistryHelper.dataPack;
    }

    public static RegistryAccess.Frozen getRegistry() {
        if (RegistryHelper.registry == null) {
            RegistryHelper.throwError("registry");
        }
        return RegistryHelper.registry;
    }

    public static Registry<Biome> getBiomes() {
        if (RegistryHelper.biomes == null) {
            RegistryHelper.throwError("biomes");
        }
        return RegistryHelper.biomes;
    }

    public static PalettedContainerFactory palettedContainerFactory() {
        if (RegistryHelper.palettedContainerFactory == null) {
            RegistryHelper.throwError("palettedContainerFactory");
        }
        return RegistryHelper.palettedContainerFactory;
    }

    public static RegistryAccess.Frozen createRegistry(FeatureFlagSet featureFlagSet) {
        MultiPackResourceManager ireloadableresourcemanager = RegistryHelper.createResourceManager(featureFlagSet);
        // add tags and loot tables for unit tests
        LayeredRegistryAccess<RegistryLayer> layeredregistryaccess = RegistryLayer.createRegistryAccess();
        List<Registry.PendingTags<?>> list = TagLoader.loadTagsForExistingRegistries(ireloadableresourcemanager, layeredregistryaccess.getLayer(RegistryLayer.STATIC));
        RegistryAccess.Frozen iregistrycustom_dimension = layeredregistryaccess.getAccessForLoading(RegistryLayer.WORLDGEN);
        List<HolderLookup.RegistryLookup<?>> list1 = TagLoader.buildUpdatedLookups(iregistrycustom_dimension, list);
        RegistryAccess.Frozen iregistrycustom_dimension1 = RegistryDataLoader.load((ResourceManager) ireloadableresourcemanager, list1, RegistryDataLoader.WORLDGEN_REGISTRIES);
        LayeredRegistryAccess<RegistryLayer> layers = layeredregistryaccess.replaceFrom(RegistryLayer.WORLDGEN, iregistrycustom_dimension1);
        // Paper start - load registry here to ensure bukkit object registry are correctly delayed if needed
        try {
            Class.forName("org.bukkit.Registry");
        } catch (final ClassNotFoundException ignored) {}
        // Paper end - load registry here to ensure bukkit object registry are correctly delayed if needed

        return layers.compositeAccess().freeze();
    }

    public static void setup(FeatureFlagSet featureFlagSet) {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        MultiPackResourceManager ireloadableresourcemanager = RegistryHelper.createResourceManager(featureFlagSet);
        // add tags and loot tables for unit tests
        LayeredRegistryAccess<RegistryLayer> layeredregistryaccess = RegistryLayer.createRegistryAccess();
        List<Registry.PendingTags<?>> list = TagLoader.loadTagsForExistingRegistries(ireloadableresourcemanager, layeredregistryaccess.getLayer(RegistryLayer.STATIC));
        RegistryAccess.Frozen iregistrycustom_dimension = layeredregistryaccess.getAccessForLoading(RegistryLayer.WORLDGEN);
        List<HolderLookup.RegistryLookup<?>> list1 = TagLoader.buildUpdatedLookups(iregistrycustom_dimension, list);
        RegistryAccess.Frozen iregistrycustom_dimension1 = RegistryDataLoader.load((ResourceManager) ireloadableresourcemanager, list1, RegistryDataLoader.WORLDGEN_REGISTRIES);
        LayeredRegistryAccess<RegistryLayer> layers = layeredregistryaccess.replaceFrom(RegistryLayer.WORLDGEN, iregistrycustom_dimension1);
        // Paper start - load registry here to ensure bukkit object registry are correctly delayed if needed
        try {
            Class.forName("org.bukkit.Registry");
        } catch (final ClassNotFoundException ignored) {}
        // Paper end - load registry here to ensure bukkit object registry are correctly delayed if needed
        RegistryHelper.registry = layers.compositeAccess().freeze();
        // Register vanilla pack
        RegistryHelper.dataPack = ReloadableServerResources.loadResources(ireloadableresourcemanager, layers, list, featureFlagSet, Commands.CommandSelection.DEDICATED, 0, MoreExecutors.directExecutor(), MoreExecutors.directExecutor()).join();
        // Bind tags
        RegistryHelper.dataPack.updateStaticRegistryTags();
        // Biome shortcut
        RegistryHelper.biomes = RegistryHelper.registry.lookupOrThrow(Registries.BIOME);
        // PalettedContainerFactory shortcut
        RegistryHelper.palettedContainerFactory = PalettedContainerFactory.create(RegistryHelper.registry);
    }

    public static <T extends Keyed> Class<T> updateClass(Class<T> aClass, NamespacedKey key) {
        Class<T> theClass;
        // Some registries have extra Typed classes such as BlockType and ItemType.
        // To avoid class cast exceptions during init mock the Typed class.
        // To get the correct class, we just use the field type.
        try {
            theClass = (Class<T>) aClass.getField(key.getKey().toUpperCase(Locale.ROOT).replace('.', '_')).getType();
        } catch (ClassCastException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }

        return theClass;
    }

    private static MultiPackResourceManager createResourceManager(FeatureFlagSet featureFlagSet) {
        // Populate available packs
        PackRepository resourceRepository = ServerPacksSource.createVanillaTrustedRepository();
        resourceRepository.reload();
        // Set up resource manager
        return new MultiPackResourceManager(PackType.SERVER_DATA, resourceRepository.getAvailablePacks().stream().filter(pack -> pack.getRequestedFeatures().isSubsetOf(featureFlagSet)).map(Pack::open).toList());
    }

    private static void throwError(String field) {
        throw new IllegalStateException(String.format("""
                Trying to access %s will it is not setup.
                Make sure that either the class or method you test has the right test environment annotation present.
                You can find them in the package src/test/java/org.bukkit.support.environment""", field));
    }
}
