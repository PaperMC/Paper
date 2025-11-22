package io.papermc.paper.plugin.configuration;

import java.util.List;
import java.util.Locale;
import net.kyori.adventure.key.KeyPattern;
import net.kyori.adventure.key.Namespaced;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * This class acts as an abstraction for a plugin configuration.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface PluginMeta extends Namespaced {

    /**
     * Provides the name of the plugin. This name uniquely identifies the plugin amongst all loaded plugins on the
     * server.
     * <ul>
     * <li>Will only contain alphanumeric characters, underscores, hyphens,
     *     and periods: [a-zA-Z0-9_\-\.].
     * <li>Typically used for identifying the plugin data folder.
     * <li>The name also acts as the token referenced in {@link #getPluginDependencies()},
     * {@link #getPluginSoftDependencies()}, and {@link #getLoadBeforePlugins()}.
     * </ul>
     * <p>
     * In the plugin.yml, this entry is named <code>name</code>.
     * <p>
     * Example:<blockquote><pre>name: MyPlugin</pre></blockquote>
     *
     * @return the name of the plugin
     */
    String getName();

    /**
     * Returns the display name of the plugin, including the version.
     *
     * @return a descriptive name of the plugin and respective version
     */
    default String getDisplayName() {
        return this.getName() + " v" + this.getVersion();
    }

    /**
     * Provides the fully qualified class name of the main class for the plugin.
     * A subtype of {@link JavaPlugin} is expected at this location.
     *
     * @return the fully qualified class name of the plugin's main class.
     */
    String getMainClass();

    /**
     * Returns the phase of the server startup logic that the plugin should be loaded.
     *
     * @return the plugin load order
     * @see PluginLoadOrder for further details regards the available load orders.
     */
    PluginLoadOrder getLoadOrder();

    /**
     * Provides the version of this plugin as defined by the plugin.
     * There is no inherit format defined/enforced for the version of a plugin, however a common approach
     * might be semantic versioning.
     *
     * @return the string representation of the plugin's version
     */
    String getVersion();

    /**
     * Provides the prefix that should be used for the plugin logger.
     * The logger prefix allows plugins to overwrite the usual default of the logger prefix, which is the name of the
     * plugin.
     *
     * @return the specific overwrite of the logger prefix as defined by the plugin. If the plugin did not define a
     *     custom logger prefix, this method will return null
     */
    @Nullable String getLoggerPrefix();

    /**
     * Provides a list of dependencies that are required for this plugin to load.
     * The list holds the unique identifiers, following the constraints laid out in {@link #getName()}, of the
     * dependencies.
     * <p>
     * If any of the dependencies defined by this list are not installed on the server, this plugin will fail to load.
     *
     * @return an immutable list of required dependency names
     */
    List<String> getPluginDependencies();

    /**
     * Provides a list of dependencies that are used but not required by this plugin.
     * The list holds the unique identifiers, following the constraints laid out in {@link #getName()}, of the soft
     * dependencies.
     * <p>
     * If these dependencies are installed on the server, they will be loaded first and supplied as dependencies to this
     * plugin, however the plugin will load even if these dependencies are not installed.
     *
     * @return immutable list of soft dependencies
     */
    List<String> getPluginSoftDependencies();

    /**
     * Provides a list of plugins that should be loaded before this plugin is loaded.
     * The list holds the unique identifiers, following the constraints laid out in {@link #getName()}, of the
     * plugins that should be loaded before the plugin described by this plugin meta.
     * <p>
     * The plugins referenced in the list provided by this method are not considered dependencies of this plugin and
     * are hence not available to the plugin at runtime. They merely load before this plugin.
     *
     * @return immutable list of plugins to load before this plugin
     */
    List<String> getLoadBeforePlugins();

    /**
     * Returns the list of plugins/dependencies that this plugin provides.
     * The list holds the unique identifiers, following the constraints laid out in {@link #getName()}, for each plugin
     * it provides the expected classes for.
     *
     * @return immutable list of provided plugins/dependencies
     */
    List<String> getProvidedPlugins();

    /**
     * Provides the list of authors that are credited with creating this plugin.
     * The author names are in no particular format.
     *
     * @return an immutable list of the plugin's authors
     */
    List<String> getAuthors();

    /**
     * Provides a list of contributors that contributed to the plugin but are not considered authors.
     * The names of the contributors are in no particular format.
     *
     * @return an immutable list of the plugin's contributors
     */
    List<String> getContributors();

    /**
     * Gives a human-friendly description of the functionality the plugin
     * provides.
     *
     * @return description or null if the plugin did not define a human readable description.
     */
    @Nullable String getDescription();

    /**
     * Provides the website for the plugin or the plugin's author.
     * The defined string value is <b>not guaranteed</b> to be in the form of a url.
     *
     * @return a string representation of the website that serves as the main hub for this plugin/its author.
     */
    @Nullable String getWebsite();

    /**
     * Provides the list of permissions that are defined via the plugin meta instance.
     *
     * @return an immutable list of permissions
     */
    List<Permission> getPermissions();

    /**
     * Provides the default values that apply to the permissions defined in this plugin meta.
     *
     * @return the bukkit permission default container.
     * @see #getPermissions()
     */
    PermissionDefault getPermissionDefault();

    /**
     * Gets the api version that this plugin supports.
     * Nullable if this version is not specified, and should be
     * considered legacy (spigot plugins only)
     *
     * @return the version string made up of the major and minor version (e.g. 1.18 or 1.19). Minor versions like 1.18.2
     * are unified to their major release version (in this example 1.18)
     */
    @Nullable String getAPIVersion();

    @KeyPattern.Namespace
    @SuppressWarnings("PatternValidation")
    @Override
    default String namespace() {
        return this.getName().toLowerCase(Locale.ROOT);
    }
}
