package io.papermc.paper.datapack;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The registrar for datapacks. The event for this registrar
 * is called anytime the game tries to discover datapacks at any of the
 * configured locations. This means that if a datapack should stay available to the server,
 * it must always be discovered whenever this event fires.
 * <p>
 * An example of a plugin loading a datapack from within its own jar is below,
 * assuming the datapack is included under {@code resources/pack} folder:
 * <pre>{@code
 * public class YourPluginBootstrap implements PluginBootstrap {
 *     @Override
 *     public void bootstrap(BootstrapContext context) {
 *         final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
 *         manager.registerEventHandler(LifecycleEvents.DATAPACK_DISCOVERY, event -> {
 *             DatapackRegistrar registrar = event.registrar();
 *             try {
 *                 final URI uri = Objects.requireNonNull(
 *                     YourPluginBootstrap.class.getResource("/pack")
 *                 ).toURI();
 *                 registrar.discoverPack(uri, "packId");
 *             } catch (final URISyntaxException | IOException e) {
 *                 throw new RuntimeException(e);
 *             }
 *         });
 *     }
 * }
 * }</pre>
 *
 * @see io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents#DATAPACK_DISCOVERY
 */
@ApiStatus.NonExtendable
@ApiStatus.Experimental
@NullMarked
public interface DatapackRegistrar extends Registrar {

    /**
     * Checks if a datapack with the specified name has been discovered.
     *
     * @param name the name of the pack
     * @return true if the pack has been discovered
     * @see Datapack#getName()
     */
    @Contract(pure = true)
    boolean hasPackDiscovered(String name);

    /**
     * Gets a discovered datapack by its name.
     *
     * @param name the name of the pack
     * @return the datapack
     * @throws java.util.NoSuchElementException if the pack is not discovered
     * @see Datapack#getName()
     */
    @Contract(pure = true)
    DiscoveredDatapack getDiscoveredPack(String name);

    /**
     * Removes a discovered datapack by its name.
     *
     * @param name the name of the pack
     * @return true if the pack was removed
     * @see Datapack#getName()
     */
    @Contract(mutates = "this")
    boolean removeDiscoveredPack(String name);

    /**
     * Gets all discovered datapacks.
     *
     * @return an unmodifiable map of discovered packs
     */
    @Contract(pure = true)
    @Unmodifiable Map<String, DiscoveredDatapack> getDiscoveredPacks();

    /**
     * Discovers a datapack at the specified {@link URI} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param uri the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    default @Nullable DiscoveredDatapack discoverPack(final URI uri, final String id) throws IOException {
        return this.discoverPack(uri, id, c -> {});
    }

    /**
     * Discovers a datapack at the specified {@link URI} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param uri the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @param configurer a configurer for extra options
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    @Nullable DiscoveredDatapack discoverPack(URI uri, String id, Consumer<Configurer> configurer) throws IOException;

    /**
     * Discovers a datapack at the specified {@link Path} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param path the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    default @Nullable DiscoveredDatapack discoverPack(final Path path, final String id) throws IOException {
        return this.discoverPack(path, id, c -> {});
    }

    /**
     * Discovers a datapack at the specified {@link Path} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param path the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @param configurer a configurer for extra options
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    @Nullable DiscoveredDatapack discoverPack(Path path, String id, Consumer<Configurer> configurer) throws IOException;

    /**
     * Discovers a datapack at the specified {@link URI} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param pluginMeta the plugin which will be the "owner" of this datapack
     * @param uri the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @param configurer a configurer for extra options
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    @Nullable DiscoveredDatapack discoverPack(PluginMeta pluginMeta, URI uri, String id, Consumer<Configurer> configurer) throws IOException;

    /**
     * Discovers a datapack at the specified {@link Path} with the id.
     * <p>Symlinks obey the {@code allowed_symlinks.txt} in the server root directory.</p>
     *
     * @param pluginMeta the plugin which will be the "owner" of this datapack
     * @param path the location of the pack
     * @param id a unique id (will be combined with plugin for the datapacks name)
     * @param configurer a configurer for extra options
     * @return the discovered datapack (or null if it failed)
     * @throws IOException if any IO error occurs
     */
    @Nullable DiscoveredDatapack discoverPack(PluginMeta pluginMeta, Path path, String id, Consumer<Configurer> configurer) throws IOException;

    /**
     * Configures additional, optional, details about a datapack.
     */
    @ApiStatus.NonExtendable
    @ApiStatus.Experimental
    interface Configurer {

        /**
         * Changes the title of the datapack from the default which
         * is just the "id" in the {@code registerPack} methods.
         *
         * @param title the new title
         * @return the configurer for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Configurer title(Component title);

        /**
         * Sets whether this pack is going to be automatically enabled on server starts even if previously disabled.
         * Defaults to false.
         *
         * @param autoEnableOnServerStart true to ensure the pack is enabled on server starts.
         * @return the configurer for chaining
         */
        @Contract(value = "_ -> this", mutates = "this")
        Configurer autoEnableOnServerStart(boolean autoEnableOnServerStart);

        /**
         * Configures the position in the
         * load order of this datapack.
         *
         * @param fixed won't move around in the load order as packs are added/removed
         * @param position try to insert at the top of the order or bottom
         * @return the configurer for chaining
         */
        @Contract(value = "_, _ -> this", mutates = "this")
        Configurer position(boolean fixed, Datapack.Position position);
    }
}
