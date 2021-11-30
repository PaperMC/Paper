// CHECKSTYLE:OFF
package org.bukkit.plugin.java;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.aether.transfer.TransferCancelledException;
import org.eclipse.aether.transfer.TransferEvent;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class LibraryLoader
{

    private final Logger logger;
    private final RepositorySystem repository;
    private final DefaultRepositorySystemSession session;
    private final List<RemoteRepository> repositories;

    public LibraryLoader(@NotNull Logger logger)
    {
        this.logger = logger;

        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class );
        locator.addService( TransporterFactory.class, HttpTransporterFactory.class );

        this.repository = locator.getService( RepositorySystem.class );
        this.session = MavenRepositorySystemUtils.newSession();

        session.setChecksumPolicy( RepositoryPolicy.CHECKSUM_POLICY_FAIL );
        session.setLocalRepositoryManager( repository.newLocalRepositoryManager( session, new LocalRepository( "libraries" ) ) );
        session.setTransferListener( new AbstractTransferListener()
        {
            @Override
            public void transferStarted(@NotNull TransferEvent event) throws TransferCancelledException
            {
                logger.log( Level.INFO, "Downloading {0}", event.getResource().getRepositoryUrl() + event.getResource().getResourceName() );
            }
        } );
        session.setReadOnly();

        this.repositories = repository.newResolutionRepositories( session, Arrays.asList( new RemoteRepository.Builder( "central", "default", "https://repo.maven.apache.org/maven2" ).build() ) );
    }

    @Nullable
    public ClassLoader createLoader(@NotNull PluginDescriptionFile desc)
    {
        if ( desc.getLibraries().isEmpty() )
        {
            return null;
        }
        logger.log( Level.INFO, "[{0}] Loading {1} libraries... please wait", new Object[]
        {
            desc.getName(), desc.getLibraries().size()
        } );

        List<Dependency> dependencies = new ArrayList<>();
        for ( String library : desc.getLibraries() )
        {
            Artifact artifact = new DefaultArtifact( library );
            Dependency dependency = new Dependency( artifact, null );

            dependencies.add( dependency );
        }

        DependencyResult result;
        try
        {
            result = repository.resolveDependencies( session, new DependencyRequest( new CollectRequest( (Dependency) null, dependencies, repositories ), null ) );
        } catch ( DependencyResolutionException ex )
        {
            throw new RuntimeException( "Error resolving libraries", ex );
        }

        List<URL> jarFiles = new ArrayList<>();
        for ( ArtifactResult artifact : result.getArtifactResults() )
        {
            File file = artifact.getArtifact().getFile();

            URL url;
            try
            {
                url = file.toURI().toURL();
            } catch ( MalformedURLException ex )
            {
                throw new AssertionError( ex );
            }

            jarFiles.add( url );
            logger.log( Level.INFO, "[{0}] Loaded library {1}", new Object[]
            {
                desc.getName(), file
            } );
        }

        URLClassLoader loader = new URLClassLoader( jarFiles.toArray( new URL[ jarFiles.size() ] ), getClass().getClassLoader() );

        return loader;
    }
}
