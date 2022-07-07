package io.papermc.testplugin;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class TestPluginLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        //classpathBuilder.addLibrary(new JarLibrary(Path.of("bob.jar")));

        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("com.owen1212055:particlehelper:1.0.0-SNAPSHOT"), null));
        resolver.addRepository(new RemoteRepository.Builder("bytecode", "default", "https://repo.bytecode.space/repository/maven-public/").build());

        classpathBuilder.addLibrary(resolver);
    }
}
