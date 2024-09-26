package org.bukkit.support;

import com.google.common.util.concurrent.MoreExecutors;
import java.util.Locale;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandDispatcher;
import net.minecraft.core.IRegistry;
import net.minecraft.core.IRegistryCustom;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.DataPackResources;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.packs.EnumResourcePackType;
import net.minecraft.server.packs.repository.ResourcePackLoader;
import net.minecraft.server.packs.repository.ResourcePackRepository;
import net.minecraft.server.packs.repository.ResourcePackSourceVanilla;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.BiomeBase;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;

public final class RegistryHelper {

    private static DataPackResources dataPack;
    private static IRegistryCustom.Dimension registry;
    private static IRegistry<BiomeBase> biomes;

    private RegistryHelper() {
    }

    public static DataPackResources getDataPack() {
        if (dataPack == null) {
            throwError("dataPack");
        }
        return dataPack;
    }

    public static IRegistryCustom.Dimension getRegistry() {
        if (registry == null) {
            throwError("registry");
        }
        return registry;
    }

    public static IRegistry<BiomeBase> getBiomes() {
        if (biomes == null) {
            throwError("biomes");
        }
        return biomes;
    }

    public static IRegistryCustom.Dimension createRegistry(FeatureFlagSet featureFlagSet) {
        return createLayers(createResourceManager(featureFlagSet)).compositeAccess().freeze();
    }

    public static void setup(FeatureFlagSet featureFlagSet) {
        SharedConstants.tryDetectVersion();
        DispenserRegistry.bootStrap();

        ResourceManager resourceManager = createResourceManager(featureFlagSet);
        LayeredRegistryAccess<RegistryLayer> layers = createLayers(resourceManager);
        registry = layers.compositeAccess().freeze();
        // Register vanilla pack
        dataPack = DataPackResources.loadResources(resourceManager, layers, featureFlagSet, CommandDispatcher.ServerType.DEDICATED, 0, MoreExecutors.directExecutor(), MoreExecutors.directExecutor()).join();
        // Bind tags
        dataPack.updateRegistryTags();
        // Biome shortcut
        biomes = registry.registryOrThrow(Registries.BIOME);
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

    private static ResourceManager createResourceManager(FeatureFlagSet featureFlagSet) {
        // Populate available packs
        ResourcePackRepository resourceRepository = ResourcePackSourceVanilla.createVanillaTrustedRepository();
        resourceRepository.reload();
        // Set up resource manager
        return new ResourceManager(EnumResourcePackType.SERVER_DATA, resourceRepository.getAvailablePacks().stream().filter(pack -> pack.getRequestedFeatures().isSubsetOf(featureFlagSet)).map(ResourcePackLoader::open).toList());
    }

    private static LayeredRegistryAccess<RegistryLayer> createLayers(ResourceManager resourceManager) {
        // add tags and loot tables for unit tests
        LayeredRegistryAccess<RegistryLayer> layers = RegistryLayer.createRegistryAccess();
        layers = WorldLoader.loadAndReplaceLayer(resourceManager, layers, RegistryLayer.WORLDGEN, RegistryDataLoader.WORLDGEN_REGISTRIES);

        return layers;
    }

    private static void throwError(String field) {
        throw new IllegalStateException(String.format("""
                Trying to access %s will it is not setup.
                Make sure that either the class or method you test has the right test environment annotation present.
                You can find them in the package src/test/java/org.bukkit.support.environment""", field));
    }
}
