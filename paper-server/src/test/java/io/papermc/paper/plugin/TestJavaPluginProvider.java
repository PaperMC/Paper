package io.papermc.paper.plugin;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.provider.PluginProvider;
import io.papermc.paper.plugin.provider.configuration.LoadOrderConfiguration;
import io.papermc.paper.plugin.provider.entrypoint.DependencyContext;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;

public class TestJavaPluginProvider implements PluginProvider<PaperTestPlugin> {

    private final TestPluginMeta testPluginConfiguration;

    public TestJavaPluginProvider(TestPluginMeta testPluginConfiguration) {
        this.testPluginConfiguration = testPluginConfiguration;
    }

    @Override
    public @NotNull Path getSource() {
        return Path.of("dummy");
    }

    @Override
    public JarFile file() {
        throw new UnsupportedOperationException();
    }

    @Override
    public PaperTestPlugin createInstance() {
        return new PaperTestPlugin(this.testPluginConfiguration);
    }

    @Override
    public TestPluginMeta getMeta() {
        return this.testPluginConfiguration;
    }

    @Override
    public ComponentLogger getLogger() {
        return ComponentLogger.logger("TestPlugin");
    }

    @Override
    public LoadOrderConfiguration createConfiguration(@NotNull Map<String, PluginProvider<?>> toLoad) {
        return new LoadOrderConfiguration() {
            @Override
            public @NotNull List<String> getLoadBefore() {
                return TestJavaPluginProvider.this.testPluginConfiguration.getLoadBeforePlugins();
            }

            @Override
            public @NotNull List<String> getLoadAfter() {
                List<String> loadAfter = new ArrayList<>();
                loadAfter.addAll(TestJavaPluginProvider.this.testPluginConfiguration.getPluginDependencies());
                loadAfter.addAll(TestJavaPluginProvider.this.testPluginConfiguration.getPluginSoftDependencies());
                return loadAfter;
            }

            @Override
            public @NotNull PluginMeta getMeta() {
                return TestJavaPluginProvider.this.testPluginConfiguration;
            }
        };
    }

    @Override
    public List<String> validateDependencies(@NotNull DependencyContext context) {
        List<String> missingDependencies = new ArrayList<>();
        for (String hardDependency : this.getMeta().getPluginDependencies()) {
            if (!context.hasDependency(hardDependency)) {
                missingDependencies.add(hardDependency);
            }
        }

        return missingDependencies;
    }
}
