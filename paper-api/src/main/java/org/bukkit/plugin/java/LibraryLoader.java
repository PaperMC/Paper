package org.bukkit.plugin.java;

import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.bukkit.plugin.PluginDescriptionFile;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// Paper start
/**
 * @hidden
 */
@org.jetbrains.annotations.ApiStatus.Internal
public class LibraryLoader {
// Paper end

    private final Logger logger;
    private final RepositorySystem repository;
    private final DefaultRepositorySystemSession session;
    private final List<RemoteRepository> repositories;
    public static java.util.function.BiFunction<URL[], ClassLoader, URLClassLoader> LIBRARY_LOADER_FACTORY; // Paper - rewrite reflection in libraries
    public static java.util.function.Function<List<java.nio.file.Path>, List<java.nio.file.Path>> REMAPPER; // Paper - remap libraries

    private static List<RemoteRepository> getRepositories() {
        return List.of(new RemoteRepository.Builder("central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR).build());
    }

    public LibraryLoader(@NotNull Logger logger) {
        this.logger = logger;

        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        this.repository = locator.getService(RepositorySystem.class);
        this.session = MavenRepositorySystemUtils.newSession();

        session.setSystemProperties(System.getProperties()); // Paper - paper plugins, backport system properties fix for transitive dependency parsing, see #10116
        session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL);
        session.setLocalRepositoryManager(repository.newLocalRepositoryManager(session, new LocalRepository("libraries")));
        session.setTransferListener(new AbstractTransferListener() {
            @Override
            public void transferStarted(@NotNull TransferEvent event) {
                logger.log(Level.INFO, "Downloading {0}", event.getResource().getRepositoryUrl() + event.getResource().getResourceName());
            }
        });

        // SPIGOT-7638: Add system properties,
        // since JdkVersionProfileActivator needs 'java.version' when a profile has the 'jdk' element
        // otherwise it will silently fail and not resolves the dependencies in the affected pom.
        session.setSystemProperties(System.getProperties());
        session.setReadOnly();

        this.repositories = repository.newResolutionRepositories(session, getRepositories());
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc) {
        // Paper start - plugin loader api
        return this.createLoader(desc, null);
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc, java.util.@Nullable List<java.nio.file.Path> paperLibraryPaths) {
        if (desc.getLibraries().isEmpty() && paperLibraryPaths == null) {
            // Paper end - plugin loader api
            return null;
        }
        logger.log(Level.INFO, "[{0}] Loading {1} libraries... please wait", new Object[]
            {
                java.util.Objects.requireNonNullElseGet(desc.getPrefix(), desc::getName), desc.getLibraries().size() // Paper - use configured log prefix
            });

        List<Dependency> dependencies = new ArrayList<>();
        for (String library : desc.getLibraries()) {
            Artifact artifact = new DefaultArtifact(library);
            Dependency dependency = new Dependency(artifact, null);

            dependencies.add(dependency);
        }

        DependencyResult result;
        if (!dependencies.isEmpty()) {
            // Paper - plugin loader api
            try
            {
                result = repository.resolveDependencies(session, new DependencyRequest(new CollectRequest((Dependency) null, dependencies, repositories), null));
            } catch (DependencyResolutionException ex) {
                throw new RuntimeException("Error resolving libraries", ex);
            }
        } else {
            result = null; // Paper - plugin loader api
        }

        List<URL> jarFiles = new ArrayList<>();
        List<java.nio.file.Path> jarPaths = new ArrayList<>(); // Paper - remap libraries
        // Paper start - plugin loader api
        if (paperLibraryPaths != null) jarPaths.addAll(paperLibraryPaths);
        if (result != null) {
            for (ArtifactResult artifact : result.getArtifactResults()) {
                // Paper end - plugin loader api
                // Paper start - remap libraries
                jarPaths.add(artifact.getArtifact().getFile().toPath());
            }
        }
        if (REMAPPER != null) {
            jarPaths = REMAPPER.apply(jarPaths);
        }
        for (java.nio.file.Path path : jarPaths) {
            File file = path.toFile();
            // Paper end - remap libraries

            URL url;
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException ex) {
                throw new AssertionError(ex);
            }

            jarFiles.add(url);
            logger.log(Level.INFO, "[{0}] Loaded library {1}", new Object[]
                {
                    java.util.Objects.requireNonNullElseGet(desc.getPrefix(), desc::getName), file // Paper - use configured log prefix
                });
        }

        // Paper start - rewrite reflection in libraries
        URLClassLoader loader;
        if (LIBRARY_LOADER_FACTORY == null) {
            loader = new URLClassLoader(jarFiles.toArray(new URL[jarFiles.size()]), getClass().getClassLoader());
        } else {
            loader = LIBRARY_LOADER_FACTORY.apply(jarFiles.toArray(new URL[jarFiles.size()]), getClass().getClassLoader());
        }
        // Paper end - rewrite reflection in libraries

        return loader;
    }
}
