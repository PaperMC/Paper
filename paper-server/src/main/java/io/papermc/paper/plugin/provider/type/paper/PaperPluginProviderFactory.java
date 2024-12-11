package io.papermc.paper.plugin.provider.type.paper;

import com.destroystokyo.paper.utils.PaperPluginLogger;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import io.papermc.paper.plugin.bootstrap.PluginProviderContextImpl;
import io.papermc.paper.plugin.entrypoint.classloader.PaperPluginClassLoader;
import io.papermc.paper.plugin.entrypoint.classloader.PaperSimplePluginClassLoader;
import io.papermc.paper.plugin.loader.PaperClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.provider.configuration.PaperPluginMeta;
import io.papermc.paper.plugin.provider.type.PluginTypeFactory;
import io.papermc.paper.plugin.provider.util.ProviderUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

class PaperPluginProviderFactory implements PluginTypeFactory<PaperPluginParent, PaperPluginMeta> {

    @Override
    public PaperPluginParent build(JarFile file, PaperPluginMeta configuration, Path source) {
        Logger jul = PaperPluginLogger.getLogger(configuration);
        ComponentLogger logger = ComponentLogger.logger(jul.getName());
        PluginProviderContext context = PluginProviderContextImpl.create(configuration, logger, source);

        PaperClasspathBuilder builder = new PaperClasspathBuilder(context);

        if (configuration.getLoader() != null) {
            try (
                PaperSimplePluginClassLoader simplePluginClassLoader = new PaperSimplePluginClassLoader(source, file, configuration, this.getClass().getClassLoader())
            ) {
                PluginLoader loader = ProviderUtil.loadClass(configuration.getLoader(), PluginLoader.class, simplePluginClassLoader);
                loader.classloader(builder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        PaperPluginClassLoader classLoader = builder.buildClassLoader(jul, source, file, configuration);
        return new PaperPluginParent(source, file, configuration, classLoader, context);
    }

    @Override
    public PaperPluginMeta create(JarFile file, JarEntry config) throws IOException {
        PaperPluginMeta configuration;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream(config)))) {
            configuration = PaperPluginMeta.create(bufferedReader);
        }
        return configuration;
    }
}
