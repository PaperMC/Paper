package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.lifecycle.ServerLifecyclePoints;
import io.papermc.paper.registry.Reference;
import io.papermc.paper.registry.enums.EnumWritableRegistry;
import io.papermc.paper.registry.ExtendedRegistry;
import io.papermc.paper.registry.key.ResourceKey;
import io.papermc.paper.world.biome.BiomeSpecialEffects;
import io.papermc.paper.world.biome.ClimateSettings;
import net.kyori.adventure.key.Key;
import org.bukkit.Color;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    final ResourceKey<Biome> testBiomeKey = ResourceKey.create(ExtendedRegistry.BIOME_REGISTRY_KEY, Key.key("testplugin", "my_new_biome"));
    Reference<Biome> biomeReference;

    @Override
    public void bootstrap(@NotNull PluginProviderContext context) {
        final Biome biome = Biome.builder()
            .climateSettings(ClimateSettings.builder().build())
            .specialEffects(BiomeSpecialEffects.builder().grassColor(Color.BLACK).skyColor(Color.BLACK).build())
            .build(Key.key("test", "example"))
            .register();

        context.getLifecyclePointScheduler().schedule(ServerLifecyclePoints.WORLDGEN_REGISTRIES_INITIALIZED, registryInitializationContext -> {
            final EnumWritableRegistry<Biome> biomeRegistry = registryInitializationContext.writableRegistryAccess()
                .registry(ExtendedRegistry.BIOME_REGISTRY_KEY);

            final ClimateSettings climateSettings = ClimateSettings.builder()
                .precipitation(ClimateSettings.Precipitation.SNOW)
                .build();

            final BiomeSpecialEffects biomeSpecialEffects = BiomeSpecialEffects.builder()
                .grassColor(Color.fromRGB(0xE1F6FF))
                .skyColor(Color.fromRGB(0x004764))
                .build();

            final Biome newBiome = Biome.builder()
                .climateSettings(climateSettings)
                .specialEffects(biomeSpecialEffects)
                .build(testBiomeKey).value();
            biomeRegistry.register(newBiome);
            System.out.println("resourcekey: " + testBiomeKey);

        }).schedule(ServerLifecyclePoints.WORLDGEN_REGISTRIES_FROZEN, worldgenRegistryFrozenContext -> {
            final ExtendedRegistry<Biome> biomeRegistry = worldgenRegistryFrozenContext.registryAccess()
                .registry(ExtendedRegistry.BIOME_REGISTRY_KEY).orElseThrow();

            Biome testBiome = biomeRegistry.get(testBiomeKey);
            System.out.println("test biome: " + testBiome);
            System.out.println("temperature: " + testBiome.getClimateSettings().temperature());
            biomeReference = biomeRegistry.getReference(testBiomeKey).orElseThrow();
        }).build(context.getConfiguration());
    }

    @Override
    public @NotNull JavaPlugin createPlugin(@NotNull PluginProviderContext context) {
        if (context.getLifecyclePointScheduler().isBuilt())
            context.getSLF4JLogger().info("LifecyclePointContext is built! No modifications allowed!");
        return new TestPlugin(this.biomeReference);
    }
}
