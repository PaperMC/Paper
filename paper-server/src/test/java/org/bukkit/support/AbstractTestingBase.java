package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Collections;
import java.util.List;
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
import net.minecraft.server.packs.repository.ResourcePackSourceVanilla;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.biome.BiomeBase;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.junit.Assert;

/**
 *  If you are getting: java.lang.ExceptionInInitializerError
 *    at net.minecraft.server.StatisticList.&lt;clinit&gt;(SourceFile:58)
 *    at net.minecraft.server.Item.&lt;clinit&gt;(SourceFile:252)
 *    at net.minecraft.server.Block.&lt;clinit&gt;(Block.java:577)
 *
 *  extend this class to solve it.
 */
public abstract class AbstractTestingBase {
    // Materials that only exist in block form (or are legacy)
    public static final List<Material> INVALIDATED_MATERIALS;

    public static final DataPackResources DATA_PACK;
    public static final IRegistryCustom.Dimension REGISTRY_CUSTOM;
    public static final IRegistry<BiomeBase> BIOMES;

    static {
        SharedConstants.tryDetectVersion();
        DispenserRegistry.bootStrap();
        // Set up resource manager
        ResourceManager resourceManager = new ResourceManager(EnumResourcePackType.SERVER_DATA, Collections.singletonList(new ResourcePackSourceVanilla().getVanillaPack()));
        // add tags and loot tables for unit tests
        LayeredRegistryAccess<RegistryLayer> layers = RegistryLayer.createRegistryAccess();
        layers = WorldLoader.loadAndReplaceLayer(resourceManager, layers, RegistryLayer.WORLDGEN, RegistryDataLoader.WORLDGEN_REGISTRIES);
        REGISTRY_CUSTOM = layers.compositeAccess().freeze();
        // Register vanilla pack
        DATA_PACK = DataPackResources.loadResources(resourceManager, REGISTRY_CUSTOM, FeatureFlags.REGISTRY.allFlags(), CommandDispatcher.ServerType.DEDICATED, 0, MoreExecutors.directExecutor(), MoreExecutors.directExecutor()).join();
        // Bind tags
        DATA_PACK.updateRegistryTags(REGISTRY_CUSTOM);
        // Biome shortcut
        BIOMES = REGISTRY_CUSTOM.registryOrThrow(Registries.BIOME);

        DummyServer.setup();
        DummyEnchantments.setup();

        ImmutableList.Builder<Material> builder = ImmutableList.builder();
        for (Material m : Material.values()) {
            if (m.isLegacy() || CraftMagicNumbers.getItem(m) == null) {
                builder.add(m);
            }
        }
        INVALIDATED_MATERIALS = builder.build();
        Assert.assertEquals("Expected 604 invalidated materials (got " + INVALIDATED_MATERIALS.size() + ")", 604, INVALIDATED_MATERIALS.size());
    }
}
