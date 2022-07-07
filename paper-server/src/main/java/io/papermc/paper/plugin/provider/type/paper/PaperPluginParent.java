package io.papermc.paper.plugin.provider.type.paper;

import com.destroystokyo.paper.util.SneakyThrow;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.configuration.type.DependencyConfiguration;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import io.papermc.paper.plugin.entrypoint.dependency.DependencyContextHolder;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.entrypoint.classloader.PaperPluginClassLoader;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.ProviderStatus;
import io.papermc.paper.plugin.provider.ProviderStatusHolder;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.type.PluginTypeFactory;
import io.papermc.paper.plugin.provider.util.ProviderUtil;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class PaperPluginParent {

    public static final PluginTypeFactory<PaperPluginParent, PaperPluginMeta> FACTORY = new PaperPluginProviderFactory();
    private final Path path;
    private final JarFile jarFile;
    private final PaperPluginMeta description;
    private final PaperPluginClassLoader classLoader;
    private final PluginProviderContext context;
    private final ComponentLogger logger;

    public PaperPluginParent(Path path, JarFile jarFile, PaperPluginMeta description, PaperPluginClassLoader classLoader, PluginProviderContext context) {
        this.path = path;
        this.jarFile = jarFile;
        this.description = description;
        this.classLoader = classLoader;
        this.context = context;
        this.logger = context.getLogger();
    }

    public boolean shouldCreateBootstrap() {
        return this.description.getBootstrapper() != null;
    }

    public PaperBootstrapProvider createBootstrapProvider() {
        return new PaperBootstrapProvider();
    }

    public PaperServerPluginProvider createPluginProvider(PaperBootstrapProvider provider) {
        return new PaperServerPluginProvider(provider);
    }

    public class PaperBootstrapProvider implements PluginProvider<PluginBootstrap>, ProviderStatusHolder, DependencyContextHolder {

        private ProviderStatus status;
        private PluginBootstrap lastProvided;

        @Override
        public @NotNull Path getSource() {
            return PaperPluginParent.this.path;
        }

        @Override
        public JarFile file() {
            return PaperPluginParent.this.jarFile;
        }

        @Override
        public PluginBootstrap createInstance() {
            PluginBootstrap bootstrap = ProviderUtil.loadClass(PaperPluginParent.this.description.getBootstrapper(),
                PluginBootstrap.class, PaperPluginParent.this.classLoader, () -> this.status = ProviderStatus.ERRORED);
            this.status = ProviderStatus.INITIALIZED;
            this.lastProvided = bootstrap;
            return bootstrap;
        }

        @Override
        public PaperPluginMeta getMeta() {
            return PaperPluginParent.this.description;
        }

        @Override
        public ComponentLogger getLogger() {
            return PaperPluginParent.this.logger;
        }

        @Override
        public LoadOrderConfiguration createConfiguration(@NotNull Map<String, PluginProvider<?>> toLoad) {
            return new PaperBootstrapOrderConfiguration(PaperPluginParent.this.description);
        }

        @Override
        public List<String> validateDependencies(@NotNull DependencyContext context) {
            List<String> missingDependencies = new ArrayList<>();
            for (Map.Entry<String, DependencyConfiguration> configuration : this.getMeta().getBootstrapDependencies().entrySet()) {
                String dependency = configuration.getKey();
                if (configuration.getValue().required() && !context.hasDependency(dependency)) {
                    missingDependencies.add(dependency);
                }
            }

            return missingDependencies;
        }

        @Override
        public ProviderStatus getLastProvidedStatus() {
            return this.status;
        }

        @Override
        public void setStatus(ProviderStatus status) {
            this.status = status;
        }

        public PluginBootstrap getLastProvided() {
            return this.lastProvided;
        }

        @Override
        public void setContext(DependencyContext context) {
            PaperPluginParent.this.classLoader.refreshClassloaderDependencyTree(context);
        }

        @Override
        public String toString() {
            return "PaperBootstrapProvider{" +
                "parent=" + PaperPluginParent.this +
                "status=" + status +
                ", lastProvided=" + lastProvided +
                '}';
        }
    }

    public class PaperServerPluginProvider implements PluginProvider<JavaPlugin>, ProviderStatusHolder, DependencyContextHolder {

        private final PaperBootstrapProvider bootstrapProvider;

        private ProviderStatus status;

        PaperServerPluginProvider(PaperBootstrapProvider bootstrapProvider) {
            this.bootstrapProvider = bootstrapProvider;
        }

        @Override
        public @NotNull Path getSource() {
            return PaperPluginParent.this.path;
        }

        @Override
        public JarFile file() {
            return PaperPluginParent.this.jarFile;
        }

        @Override
        public JavaPlugin createInstance() {
            PluginBootstrap bootstrap = null;
            if (this.bootstrapProvider != null && this.bootstrapProvider.getLastProvided() != null) {
                bootstrap = this.bootstrapProvider.getLastProvided();
            }

            try {
                JavaPlugin plugin;
                if (bootstrap == null) {
                    plugin = ProviderUtil.loadClass(PaperPluginParent.this.description.getMainClass(), JavaPlugin.class, PaperPluginParent.this.classLoader);
                } else {
                    plugin = bootstrap.createPlugin(PaperPluginParent.this.context);
                }

                if (!plugin.getClass().isAssignableFrom(Class.forName(PaperPluginParent.this.description.getMainClass(), true, plugin.getClass().getClassLoader()))) {
                    logger.info("Bootstrap of plugin " + PaperPluginParent.this.description.getName() + " provided a plugin instance of class " + plugin.getClass().getName() + " which does not match the plugin declared main class");
                }

                this.status = ProviderStatus.INITIALIZED;
                return plugin;
            } catch (Throwable throwable) {
                this.status = ProviderStatus.ERRORED;
                SneakyThrow.sneaky(throwable);
            }

            throw new AssertionError(); // Impossible
        }

        @Override
        public PaperPluginMeta getMeta() {
            return PaperPluginParent.this.description;
        }

        @Override
        public ComponentLogger getLogger() {
            return PaperPluginParent.this.logger;
        }

        @Override
        public LoadOrderConfiguration createConfiguration(@NotNull Map<String, PluginProvider<?>> toLoad) {
            return new PaperLoadOrderConfiguration(PaperPluginParent.this.description);
        }

        @Override
        public List<String> validateDependencies(@NotNull DependencyContext context) {
            List<String> missingDependencies = new ArrayList<>();
            for (Map.Entry<String, DependencyConfiguration> dependency : this.getMeta().getServerDependencies().entrySet()) {
                String name = dependency.getKey();
                if (dependency.getValue().required() && !context.hasDependency(name)) {
                    missingDependencies.add(name);
                }
            }

            return missingDependencies;
        }

        @Override
        public ProviderStatus getLastProvidedStatus() {
            return this.status;
        }

        @Override
        public void setStatus(ProviderStatus status) {
            this.status = status;
        }

        public boolean shouldSkipCreation() {
            if (this.bootstrapProvider == null) {
                return false;
            }

            return this.bootstrapProvider.getLastProvidedStatus() == ProviderStatus.ERRORED;
        }

        /*
        The plugin has to reuse the classloader in order to share the bootstrapper.
        However, a plugin may have totally separate dependencies during bootstrapping.
        This is a bit yuck, but in general we have to treat bootstrapping and normal game as connected.
         */
        @Override
        public void setContext(DependencyContext context) {
            PaperPluginParent.this.classLoader.refreshClassloaderDependencyTree(context);
        }

        @Override
        public String toString() {
            return "PaperServerPluginProvider{" +
                "parent=" + PaperPluginParent.this +
                "bootstrapProvider=" + bootstrapProvider +
                ", status=" + status +
                '}';
        }
    }


    @Override
    public String toString() {
        return "PaperPluginParent{" +
            "path=" + path +
            ", jarFile=" + jarFile +
            ", description=" + description +
            ", classLoader=" + classLoader +
            '}';
    }
}
