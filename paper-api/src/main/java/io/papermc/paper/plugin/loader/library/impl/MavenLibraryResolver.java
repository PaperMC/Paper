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
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.supplier.RepositorySystemSupplier;
import org.eclipse.aether.transfer.AbstractTransferListener;
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
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
 *     "central", "default", "https://repo1.maven.org/maven2/"
 * ).build());
 * }</pre>
 * <p>
 * Plugins may create and register a {@link MavenLibraryResolver} after configuring it.
 */
@NullMarked
public class MavenLibraryResolver implements ClassPathLibrary {

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
        this.repository = new RepositorySystemSupplier().get();
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
}
