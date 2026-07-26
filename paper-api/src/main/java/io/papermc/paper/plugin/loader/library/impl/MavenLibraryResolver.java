package io.papermc.paper.plugin.loader.library.impl;

import io.papermc.paper.plugin.loader.library.ClassPathLibrary;
import io.papermc.paper.plugin.loader.library.LibraryLoadingException;
import io.papermc.paper.plugin.loader.library.LibraryStore;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
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
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The maven library resolver acts as a resolver for yet to be resolved jar libraries that may be pulled from a
 * remote maven repository.
 * <p>
 * Plugins may create and configure a {@link MavenLibraryResolver} by creating a new one and registering both
 * a dependency artifact that should be resolved to a library at runtime and the repository it is found in.
 * An example of this would be the inclusion of the jooq library for typesafe SQL queries:
 * <pre>{@code
 * MavenLibraryResolver resolver = new MavenLibraryResolver();
 * resolver.addDependency(new Dependency(new DefaultArtifact("org.jooq:jooq:3.17.7"), null));
 * resolver.addRepository(new RemoteRepository.Builder(
 *     "central", "default", MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR
 * ).build());
 * }</pre>
 * <p>
 * Plugins may create and register a {@link MavenLibraryResolver} after configuring it.
 */
@NullMarked
public class MavenLibraryResolver implements ClassPathLibrary {

    /**
     * The default Maven Central mirror, configurable through the {@code PAPER_DEFAULT_CENTRAL_REPOSITORY} environment
     * variable. Use this instead of Maven Central directly when you do not have your own mirror, as using
     * Maven Central as a CDN is against the Maven Central Terms of Service, and you will cause users to hit
     * rate limits.
     *
     * <p>This repository is also used by the legacy {@link org.bukkit.plugin.java.LibraryLoader}.</p>
     */
    public static final String MAVEN_CENTRAL_DEFAULT_MIRROR = getDefaultMavenCentralMirror();
    private static final List<String> MAVEN_CENTRAL_URLS = List.of(
        "https://repo1.maven.org/maven2",
        "http://repo1.maven.org/maven2",
        "https://repo.maven.apache.org/maven2",
        "http://repo.maven.apache.org/maven2"
    );
    private static final Logger LOGGER = LoggerFactory.getLogger("MavenLibraryResolver");

    private final RepositorySystem repository;
    private final DefaultRepositorySystemSession session;
    private final List<RemoteRepository> repositories = new ArrayList<>();
    private final List<Dependency> dependencies = new ArrayList<>();

    /**
     * Creates a new maven library resolver instance.
     * <p>
     * The created instance will use the servers {@code libraries} folder to cache fetched libraries in.
     * Notably, the resolver is created without any repository, not even maven central.
     * It is hence crucial that plugins which aim to use this api register all required repositories before
     * submitting the {@link MavenLibraryResolver} to the {@link io.papermc.paper.plugin.loader.PluginClasspathBuilder}.
     */
    public MavenLibraryResolver() {
        final DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);

        this.repository = locator.getService(RepositorySystem.class);
        this.session = MavenRepositorySystemUtils.newSession();

        this.session.setSystemProperties(System.getProperties());
        this.session.setChecksumPolicy(RepositoryPolicy.CHECKSUM_POLICY_FAIL);
        this.session.setLocalRepositoryManager(this.repository.newLocalRepositoryManager(this.session, new LocalRepository("libraries")));
        this.session.setTransferListener(new AbstractTransferListener() {
            @Override
            public void transferInitiated(final TransferEvent event) throws TransferCancelledException {
                LOGGER.info("Downloading {}", event.getResource().getRepositoryUrl() + event.getResource().getResourceName());
            }
        });
        this.session.setReadOnly();
    }

    /**
     * Adds the provided dependency to the library resolver.
     * The artifact from the first valid repository matching the passed dependency will be chosen.
     *
     * @param dependency the definition of the dependency the maven library resolver should resolve when running
     * @see MavenLibraryResolver#addRepository(RemoteRepository)
     */
    public void addDependency(final Dependency dependency) {
        this.dependencies.add(dependency);
    }

    /**
     * Adds the provided repository to the library resolver.
     * The order in which these are added does matter, as dependency resolving will start at the first added
     * repository.
     *
     * @param remoteRepository the configuration that defines the maven repository this library resolver should fetch
     * dependencies from
     */
    public void addRepository(final RemoteRepository remoteRepository) {
        if (MAVEN_CENTRAL_URLS.stream().anyMatch(remoteRepository.getUrl()::startsWith)) {
            LOGGER.warn(
                "Use of Maven Central as a CDN is against the Maven Central Terms of Service. Use MavenLibraryResolver.MAVEN_CENTRAL_DEFAULT_MIRROR instead.",
                new RuntimeException("Plugin used Maven Central for library resolution (%s)".formatted(remoteRepository.toString()))
            );
        }
        this.repositories.add(remoteRepository);
    }

    /**
     * Resolves the provided dependencies and adds them to the library store.
     *
     * @param store the library store the then resolved and downloaded dependencies are registered into
     * @throws LibraryLoadingException if resolving a dependency failed
     */
    @Override
    public void register(final LibraryStore store) throws LibraryLoadingException {
        final List<RemoteRepository> repos = this.repository.newResolutionRepositories(this.session, this.repositories);

        final DependencyResult result;
        try {
            result = this.repository.resolveDependencies(this.session, new DependencyRequest(new CollectRequest((Dependency) null, this.dependencies, repos), null));
        } catch (final DependencyResolutionException ex) {
            throw new LibraryLoadingException("Error resolving libraries", ex);
        }

        for (final ArtifactResult artifact : result.getArtifactResults()) {
            final File file = artifact.getArtifact().getFile();
            store.addLibrary(file.toPath());
        }
    }

    private static String getDefaultMavenCentralMirror() {
        String central = System.getenv("PAPER_DEFAULT_CENTRAL_REPOSITORY");
        if (central == null) {
            central = System.getProperty("org.bukkit.plugin.java.LibraryLoader.centralURL");
        }
        if (central == null) {
            central = "https://maven-central.storage-download.googleapis.com/maven2";
        }
        return central;
    }
}
