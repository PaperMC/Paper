package io.papermc.paper.plugin.provider.type;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.Entrypoint;
import io.papermc.paper.plugin.entrypoint.EntrypointHandler;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.type.paper.PaperPluginParent;
import io.papermc.paper.plugin.provider.type.spigot.SpigotPluginProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This is where spigot/paper plugins are registered.
 * This will get the jar and find a certain config file, create an object
 * then registering it into a {@link EntrypointHandler} at a certain {@link Entrypoint}.
 */
public abstract class PluginFileType<T, C extends PluginMeta> {

    public static final String PAPER_PLUGIN_YML = "paper-plugin.yml";
    private static final List<String> CONFIG_TYPES = new ArrayList<>();

    public static final PluginFileType<PaperPluginParent, PaperPluginMeta> PAPER = new PluginFileType<>(PAPER_PLUGIN_YML, PaperPluginParent.FACTORY) {
        @Override
        protected void register(EntrypointHandler entrypointHandler, PaperPluginParent parent) {
            PaperPluginParent.PaperBootstrapProvider bootstrapPluginProvider = null;
            if (parent.shouldCreateBootstrap()) {
                bootstrapPluginProvider = parent.createBootstrapProvider();
                entrypointHandler.register(Entrypoint.BOOTSTRAPPER, bootstrapPluginProvider);
            }

            entrypointHandler.register(Entrypoint.PLUGIN, parent.createPluginProvider(bootstrapPluginProvider));
        }
    };
    public static final PluginFileType<SpigotPluginProvider, PluginDescriptionFile> SPIGOT = new PluginFileType<>("plugin.yml", SpigotPluginProvider.FACTORY) {
        @Override
        protected void register(EntrypointHandler entrypointHandler, SpigotPluginProvider provider) {
            entrypointHandler.register(Entrypoint.PLUGIN, provider);
        }
    };

    private static final List<PluginFileType<?, ?>> VALUES = List.of(PAPER, SPIGOT);

    private final String config;
    private final PluginTypeFactory<T, C> factory;

    PluginFileType(String config, PluginTypeFactory<T, C> factory) {
        this.config = config;
        this.factory = factory;
        CONFIG_TYPES.add(config);
    }

    @Nullable
    public static PluginFileType<?, ?> guessType(JarFile file) {
        for (PluginFileType<?, ?> type : VALUES) {
            JarEntry entry = file.getJarEntry(type.config);
            if (entry != null) {
                return type;
            }
        }

        return null;
    }

    public T register(EntrypointHandler entrypointHandler, JarFile file, Path context) throws Exception {
        C config = this.getConfig(file);
        T provider = this.factory.build(file, config, context);
        this.register(entrypointHandler, provider);
        return provider;
    }

    public C getConfig(JarFile file) throws Exception {
        return this.factory.create(file, file.getJarEntry(this.config));
    }

    protected abstract void register(EntrypointHandler entrypointHandler, T provider);

    public static List<String> getConfigTypes() {
        return CONFIG_TYPES;
    }
}
