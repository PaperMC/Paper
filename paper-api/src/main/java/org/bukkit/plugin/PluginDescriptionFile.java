package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

/// This type is the runtime-container for the information in the plugin.yml.
/// All plugins must have a respective plugin.yml. For plugins written in java
/// using the standard plugin loader, this file must be in the root of the jar
/// file.
///
/// When Bukkit loads a plugin, it needs to know some basic information about
/// it. It reads this information from a YAML file, 'plugin.yml'. This file
/// consists of a set of attributes, each defined on a new line and with no
/// indentation.
///
/// Every (almost\* every) method corresponds with a specific entry in the
/// plugin.yml. These are the **required** entries for every plugin.yml:
///
///   - [#getName()] - `name`
///   - [#getVersion()] - `version`
///   - [#getMain()] - `main`
///
/// Failing to include any of these items will throw an exception and cause the
/// server to ignore your plugin.
///
/// This is a list of the possible yaml keys, with specific details included in
/// the respective method documentations:
/// <table border="1">
/// <caption>The description of the plugin.yml layout</caption>
/// <tbody><tr>
///     <th>Node</th>
///     <th>Method</th>
///     <th>Summary</th>
/// </tr><tr>
///     <td><code>name</code></td>
///     <td>[#getName()]</td>
///     <td>The unique name of plugin</td>
/// </tr><tr>
///     <td><code>provides</code></td>
///     <td>[#getProvides()]</td>
///     <td>The plugin APIs which this plugin provides</td>
/// </tr><tr>
///     <td><code>version</code></td>
///     <td>[#getVersion()]</td>
///     <td>A plugin revision identifier</td>
/// </tr><tr>
///     <td><code>main</code></td>
///     <td>[#getMain()]</td>
///     <td>The plugin's initial class file</td>
/// </tr><tr>
///     <td><code>author</code><br><code>authors</code></td>
///     <td>[#getAuthors()]</td>
///     <td>The plugin authors</td>
/// </tr><tr>
///     <td><code>contributors</code></td>
///     <td>[#getContributors()]</td>
///     <td>The plugin contributors</td>
/// </tr><tr>
///     <td><code>description</code></td>
///     <td>[#getDescription()]</td>
///     <td>Human readable plugin summary</td>
/// </tr><tr>
///     <td><code>website</code></td>
///     <td>[#getWebsite()]</td>
///     <td>The URL to the plugin's site</td>
/// </tr><tr>
///     <td><code>prefix</code></td>
///     <td>[#getPrefix()]</td>
///     <td>The token to prefix plugin log entries</td>
/// </tr><tr>
///     <td><code>load</code></td>
///     <td>[#getLoad()]</td>
///     <td>The phase of server-startup this plugin will load during</td>
/// </tr><tr>
///     <td><code>depend</code></td>
///     <td>[#getDepend()]</td>
///     <td>Other required plugins</td>
/// </tr><tr>
///     <td><code>softdepend</code></td>
///     <td>[#getSoftDepend()]</td>
///     <td>Other plugins that add functionality</td>
/// </tr><tr>
///     <td><code>loadbefore</code></td>
///     <td>[#getLoadBefore()]</td>
///     <td>The inverse softdepend</td>
/// </tr><tr>
///     <td><code>commands</code></td>
///     <td>[#getCommands()]</td>
///     <td>The commands the plugin will register</td>
/// </tr><tr>
///     <td><code>permissions</code></td>
///     <td>[#getPermissions()]</td>
///     <td>The permissions the plugin will register</td>
/// </tr><tr>
///     <td><code>default-permission</code></td>
///     <td>[#getPermissionDefault()]</td>
///     <td>The default [<code>default</code>][Permission#getDefault()] permission
///         state for defined [<code>permissions</code>][#getPermissions()] the plugin
///         will register</td>
/// </tr><tr>
///     <td><code>awareness</code></td>
///     <td>[#getAwareness()]</td>
///     <td>The concepts that the plugin acknowledges</td>
/// </tr><tr>
///     <td><code>api-version</code></td>
///     <td>[#getAPIVersion()]</td>
///     <td>The API version which this plugin was programmed against</td>
/// </tr><tr>
///     <td><code>libraries</code></td>
///     <td>[<code>()</code>][#getLibraries()]</td>
///     <td>The libraries to be linked with this plugin</td>
/// </tr>
/// </tbody></table>
///
/// A plugin.yml example:
///
/// ```
/// name: Inferno
/// provides: [Hell]
/// version: 1.4.1
/// description: This plugin is so 31337. You can set yourself on fire.
/// # We could place every author in the authors list, but chose not to for illustrative purposes
/// # Also, having an author distinguishes that person as the project lead, and ensures their
/// # name is displayed first
/// author: CaptainInflamo
/// authors: [Cogito, verrier, EvilSeph]
/// contributors: [Choco, md_5]
/// website: http://www.curse.com/server-mods/minecraft/myplugin
///
/// main: com.captaininflamo.bukkit.inferno.Inferno
/// depend: [NewFire, FlameWire]
/// api-version: 1.13
/// libraries:
/// - com.squareup.okhttp3:okhttp:4.9.0
///
/// commands:
///  flagrate:
///    description: Set yourself on fire.
///    aliases: [combust_me, combustMe]
///    permission: inferno.flagrate
///    usage: Syntax error! Simply type /<command> to ignite yourself.
///  burningdeaths:
///    description: List how many times you have died by fire.
///    aliases: [burning_deaths, burningDeaths]
///    permission: inferno.burningdeaths
///    usage: |
///      /<command> [player]
///      Example: /<command> - see how many times you have burned to death
///      Example: /<command> CaptainIce - see how many times CaptainIce has burned to death
///
/// permissions:
///  inferno.*:
///    description: Gives access to all Inferno commands
///    children:
///      inferno.flagrate: true
///      inferno.burningdeaths: true
///      inferno.burningdeaths.others: true
///  inferno.flagrate:
///    description: Allows you to ignite yourself
///    default: true
///  inferno.burningdeaths:
///    description: Allows you to see how many times you have burned to death
///    default: true
///  inferno.burningdeaths.others:
///    description: Allows you to see how many times others have burned to death
///    default: op
///    children:
///      inferno.burningdeaths: true
/// ```
public final class PluginDescriptionFile implements io.papermc.paper.plugin.configuration.PluginMeta { // Paper
    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>() {
        @Override
        @NotNull
        protected Yaml initialValue() {
            DumperOptions dumperOptions = new DumperOptions();
            return new Yaml(new SafeConstructor(new LoaderOptions()) {
                {
                    yamlConstructors.put(null, new AbstractConstruct() {
                        @NotNull
                        @Override
                        public Object construct(@NotNull final Node node) {
                            if (!node.getTag().startsWith("!@")) {
                                // Unknown tag - will fail
                                return SafeConstructor.undefinedConstructor.construct(node);
                            }
                            // Unknown awareness - provide a graceful substitution
                            return new PluginAwareness() {
                                @Override
                                public String toString() {
                                    return node.toString();
                                }
                            };
                        }
                    });
                    for (final PluginAwareness.Flags flag : PluginAwareness.Flags.values()) {
                        yamlConstructors.put(new Tag("!@" + flag.name()), new AbstractConstruct() {
                            @NotNull
                            @Override
                            public PluginAwareness.Flags construct(@NotNull final Node node) {
                                return flag;
                            }
                        });
                    }
                }
            }, new Representer(dumperOptions), dumperOptions, new PluginDescriptionResolver());
        }
    };
    String rawName = null;
    private String name = null;
    private List<String> provides = ImmutableList.of();
    private String main = null;
    private String classLoaderOf = null;
    private List<String> depend = ImmutableList.of();
    private List<String> softDepend = ImmutableList.of();
    private List<String> loadBefore = ImmutableList.of();
    private String version = null;
    private Map<String, Map<String, Object>> commands = ImmutableMap.of();
    private String description = null;
    private List<String> authors = null;
    private List<String> contributors = null;
    private String website = null;
    private String prefix = null;
    private PluginLoadOrder order = PluginLoadOrder.POSTWORLD;
    private List<Permission> permissions = null;
    private Map<?, ?> lazyPermissions = null;
    private PermissionDefault defaultPerm = PermissionDefault.OP;
    private Set<PluginAwareness> awareness = ImmutableSet.of();
    private String apiVersion = null;
    private List<String> libraries = ImmutableList.of();
    // Paper start - plugin loader api
    private String paperPluginLoader;
    @org.jetbrains.annotations.ApiStatus.Internal @org.jetbrains.annotations.Nullable
    public String getPaperPluginLoader() {
        return this.paperPluginLoader;
    }
    // Paper end - plugin loader api
    // Paper start - oh my goddddd
    /// @hidden
    @org.jetbrains.annotations.ApiStatus.Internal
    public PluginDescriptionFile(String rawName, String name, List<String> provides, String main, String classLoaderOf, List<String> depend, List<String> softDepend, List<String> loadBefore, String version, Map<String, Map<String, Object>> commands, String description, List<String> authors, List<String> contributors, String website, String prefix, PluginLoadOrder order, List<Permission> permissions, PermissionDefault defaultPerm, Set<PluginAwareness> awareness, String apiVersion, List<String> libraries) {
        this.rawName = rawName;
        this.name = name;
        this.provides = provides;
        this.main = main;
        this.classLoaderOf = classLoaderOf;
        this.depend = depend;
        this.softDepend = softDepend;
        this.loadBefore = loadBefore;
        this.version = version;
        this.commands = commands;
        this.description = description;
        this.authors = authors;
        this.contributors = contributors;
        this.website = website;
        this.prefix = prefix;
        this.order = order;
        this.permissions = permissions;
        this.defaultPerm = defaultPerm;
        this.awareness = awareness;
        this.apiVersion = apiVersion;
        this.libraries = libraries;
    }

    @Override
    public @NotNull String getMainClass() {
        return this.main;
    }

    @Override
    public @NotNull PluginLoadOrder getLoadOrder() {
        return this.order;
    }

    @Override
    public @Nullable String getLoggerPrefix() {
        return this.prefix;
    }

    @Override
    public @NotNull List<String> getPluginDependencies() {
        return this.depend;
    }

    @Override
    public @NotNull List<String> getPluginSoftDependencies() {
        return this.softDepend;
    }

    @Override
    public @NotNull List<String> getLoadBeforePlugins() {
        return this.loadBefore;
    }

    @Override
    public @NotNull List<String> getProvidedPlugins() {
        return this.provides;
    }
    // Paper end

    public PluginDescriptionFile(@NotNull final InputStream stream) throws InvalidDescriptionException {
        loadMap(asMap(YAML.get().load(stream)));
    }

    /// Loads a PluginDescriptionFile from the specified reader
    ///
    /// @param reader The reader
    /// @throws InvalidDescriptionException If the PluginDescriptionFile is
    ///     invalid
    public PluginDescriptionFile(@NotNull final Reader reader) throws InvalidDescriptionException {
        loadMap(asMap(YAML.get().load(reader)));
    }

    /// Creates a new PluginDescriptionFile with the given detailed
    ///
    /// @param pluginName Name of this plugin
    /// @param pluginVersion Version of this plugin
    /// @param mainClass Full location of the main class of this plugin
    public PluginDescriptionFile(@NotNull final String pluginName, @NotNull final String pluginVersion, @NotNull final String mainClass) {
        name = rawName = pluginName;

        if (!VALID_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException("name '" + name + "' contains invalid characters.");
        }
        name = name.replace(' ', '_');
        version = pluginVersion;
        main = mainClass;
    }

    /// Gives the name of the plugin. This name is a unique identifier for
    /// plugins.
    ///
    ///   - Must consist of all alphanumeric characters, underscores, hyphon,
    ///     and period (a-z,A-Z,0-9, \_.-). Any other character will cause the
    ///     plugin.yml to fail loading.
    ///   - Used to determine the name of the plugin's data folder. Data
    ///     folders are placed in the ./plugins/ directory by default, but this
    ///     behavior should not be relied on. [Plugin#getDataFolder()]
    ///     should be used to reference the data folder.
    ///   - It is good practice to name your jar the same as this, for example
    ///     'MyPlugin.jar'.
    ///   - Case sensitive.
    ///   - It's the token referenced in [#getDepend()],
    ///     [#getSoftDepend()], and [#getLoadBefore()].
    ///   - Using spaces in the plugin's name is deprecated.
    ///
    /// In the plugin.yml, this entry is named `name`.
    ///
    /// Example:
    ///
    /// ```
    /// name: MyPlugin
    /// ```
    ///
    /// @return the name of the plugin
    @NotNull
    public String getName() {
        return name;
    }

    /// Gives the list of other plugin APIs which this plugin provides.
    /// These are usable for other plugins to depend on.
    ///
    ///   - Must consist of all alphanumeric characters, underscores, hyphon,
    ///     and period (a-z,A-Z,0-9, \_.-). Any other character will cause the
    ///     plugin.yml to fail loading.
    ///   - A different plugin providing the same one or using it as their name
    ///     will not result in the plugin to fail loading.
    ///   - Case sensitive.
    ///   - An entry of this list can be referenced in [#getDepend()],
    ///     [#getSoftDepend()], and [#getLoadBefore()].
    ///   - `provides` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this entry is named `provides`.
    ///
    /// Example:
    ///
    /// ```
    /// provides:
    /// - OtherPluginName
    /// - OldPluginName
    /// ```
    ///
    /// @return immutable list of the plugin APIs which this plugin provides
    @NotNull
    public List<String> getProvides() {
        return provides;
    }

    /// Gives the version of the plugin.
    ///
    ///   - Version is an arbitrary string, however the most common format is
    ///     MajorRelease.MinorRelease.Build (eg: 1.4.1).
    ///   - Typically you will increment this every time you release a new
    ///     feature or bug fix.
    ///   - Displayed when a user types `/version PluginName`
    ///
    /// In the plugin.yml, this entry is named `version`.
    ///
    /// Example:
    ///
    /// ```
    /// version: 1.4.1
    /// ```
    ///
    /// @return the version of the plugin
    @NotNull
    public String getVersion() {
        return version;
    }

    /// Gives the fully qualified name of the main class for a plugin. The
    /// format should follow the [ClassLoader#loadClass(String)] syntax
    /// to successfully be resolved at runtime. For most plugins, this is the
    /// class that extends [JavaPlugin].
    ///
    ///   - This must contain the full namespace including the class file
    ///     itself.
    ///   - If your namespace is `org.bukkit.plugin`, and your class
    ///     file is called `MyPlugin` then this must be
    ///     `org.bukkit.plugin.MyPlugin`
    ///   - No plugin can use `org.bukkit.` as a base package for
    ///     **any class**, including the main class.
    ///
    /// In the plugin.yml, this entry is named `main`.
    ///
    /// Example:
    ///
    /// ```
    /// main: org.bukkit.plugin.MyPlugin
    /// ```
    ///
    /// @return the fully qualified main class for the plugin
    @NotNull
    public String getMain() {
        return main;
    }

    /// Gives a human-friendly description of the functionality the plugin
    /// provides.
    ///
    ///   - The description can have multiple lines.
    ///   - Displayed when a user types `/version PluginName`
    ///
    /// In the plugin.yml, this entry is named `description`.
    ///
    /// Example:
    ///
    /// ```
    /// description: This plugin is so 31337. You can set yourself on fire.
    /// ```
    ///
    /// @return description of this plugin, or null if not specified
    @Nullable
    public String getDescription() {
        return description;
    }

    /// Gives the phase of server startup that the plugin should be loaded.
    ///
    ///   - Possible values are in [PluginLoadOrder].
    ///   - Defaults to [PluginLoadOrder#POSTWORLD].
    ///   - Certain caveats apply to each phase.
    ///   - When different, [#getDepend()], [#getSoftDepend()], and
    ///     [#getLoadBefore()] become relative in order loaded per-phase.
    ///     If a plugin loads at `STARTUP`, but a dependency loads
    ///     at `POSTWORLD`, the dependency will not be loaded before
    ///     the plugin is loaded.
    ///
    /// In the plugin.yml, this entry is named `load`.
    ///
    /// Example:
    ///
    /// ```
    /// load: STARTUP
    /// ```
    ///
    /// @return the phase when the plugin should be loaded
    @NotNull
    public PluginLoadOrder getLoad() {
        return order;
    }

    /// Gives the list of authors for the plugin.
    ///
    ///   - Gives credit to the developer.
    ///   - Used in some server error messages to provide helpful feedback on
    ///     who to contact when an error occurs.
    ///   - A SpigotMC forum handle or email address is recommended.
    ///   - Is displayed when a user types `/version PluginName`
    ///   - `authors` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this has two entries, `author` and
    /// `authors`.
    ///
    /// Single author example:
    ///
    /// ```
    /// author: CaptainInflamo
    /// ```
    ///
    /// Multiple author example:
    /// ```
    /// authors: [Cogito, verrier, EvilSeph]
    /// ```
    ///
    /// When both are specified, author will be the first entry in the list, so
    /// this example:
    /// ```
    /// author: Grum
    /// authors:
    /// - feildmaster
    /// - amaranth
    /// ```
    ///
    /// Is equivalent to this example:
    /// <pre>
    /// authors: [Grum, feildmaster, aramanth]
    /// </pre>
    ///
    /// @return an immutable list of the plugin's authors
    @NotNull
    public List<String> getAuthors() {
        return authors;
    }

    /// Gives the list of contributors for the plugin.
    ///
    ///   - Gives credit to those that have contributed to the plugin, though
    ///     not enough so to warrant authorship.
    ///   - Unlike [#getAuthors()], contributors will not be mentioned in
    ///     server error messages as a means of contact.
    ///   - A SpigotMC forum handle or email address is recommended.
    ///   - Is displayed when a user types `/version PluginName`
    ///   - `contributors` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// Example:
    ///
    /// ```
    /// authors: [Choco, md_5]
    /// ```
    ///
    /// @return an immutable list of the plugin's contributors
    @NotNull
    public List<String> getContributors() {
        return contributors;
    }

    /// Gives the plugin's or plugin's author's website.
    ///
    ///   - A link to the Curse page that includes documentation and downloads
    ///     is highly recommended.
    ///   - Displayed when a user types `/version PluginName`
    ///
    /// In the plugin.yml, this entry is named `website`.
    ///
    /// Example:
    ///
    /// ```
    /// website: http://www.curse.com/server-mods/minecraft/myplugin
    /// ```
    ///
    /// @return description of this plugin, or null if not specified
    @Nullable
    public String getWebsite() {
        return website;
    }

    /// Gives a list of other plugins that the plugin requires.
    ///
    ///   - Use the value in the [#getName()] of the target plugin to
    ///     specify the dependency.
    ///   - If any plugin listed here is not found, your plugin will fail to
    ///     load at startup.
    ///   - If multiple plugins list each other in `depend`,
    ///     creating a network with no individual plugin does not list another
    ///     plugin in the [network](https://en.wikipedia.org/wiki/Circular_dependency),
    ///     all plugins in that network will fail.
    ///   - `depend` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this entry is named `depend`.
    ///
    /// Example:
    ///
    /// ```
    /// depend:
    /// - OnePlugin
    /// - AnotherPlugin
    /// ```
    ///
    /// @return immutable list of the plugin's dependencies
    @NotNull
    public List<String> getDepend() {
        return depend;
    }

    /// Gives a list of other plugins that the plugin requires for full
    /// functionality. The [PluginManager] will make best effort to treat
    /// all entries here as if they were a [`dependency`][#getDepend()], but
    /// will never fail because of one of these entries.
    ///
    ///   - Use the value in the [#getName()] of the target plugin to
    ///     specify the dependency.
    ///   - When an unresolvable plugin is listed, it will be ignored and does
    ///     not affect load order.
    ///   - When a circular dependency occurs (a network of plugins depending
    ///     or soft-depending on each other), it will arbitrarily choose a
    ///     plugin that can be resolved when ignoring soft-dependencies.
    ///   - `softdepend` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this entry is named `softdepend`.
    ///
    /// Example:
    ///
    /// ```
    /// softdepend: [OnePlugin, AnotherPlugin]
    /// ```
    ///
    /// @return immutable list of the plugin's preferred dependencies
    @NotNull
    public List<String> getSoftDepend() {
        return softDepend;
    }

    /// Gets the list of plugins that should consider this plugin a
    /// soft-dependency.
    ///
    ///   - Use the value in the [#getName()] of the target plugin to
    ///     specify the dependency.
    ///   - The plugin should load before any other plugins listed here.
    ///   - Specifying another plugin here is strictly equivalent to having the
    ///     specified plugin's [#getSoftDepend()] include
    ///     [`this plugin`][#getName()].
    ///   - `loadbefore` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this entry is named `loadbefore`.
    ///
    /// Example:
    ///
    /// ```
    /// loadbefore:
    /// - OnePlugin
    /// - AnotherPlugin
    /// ```
    ///
    /// @return immutable list of plugins that should consider this plugin a
    ///     soft-dependency
    @NotNull
    public List<String> getLoadBefore() {
        return loadBefore;
    }

    /// Gives the token to prefix plugin-specific logging messages with.
    ///
    ///   - This includes all messages using [Plugin#getLogger()].
    ///   - If not specified, the server uses the plugin's [`name`][#getName()].
    ///   - This should clearly indicate what plugin is being logged.
    ///
    /// In the plugin.yml, this entry is named `prefix`.
    ///
    /// Example:
    ///
    /// ```
    /// prefix: ex-why-zee
    /// ```
    ///
    /// @return the prefixed logging token, or null if not specified
    @Nullable
    public String getPrefix() {
        return prefix;
    }

    /// Gives the map of command-name to command-properties. Each entry in this
    /// map corresponds to a single command and the respective values are the
    /// properties of the command. Each property, _with the exception of
    /// aliases_, can be defined at runtime using methods in
    /// [PluginCommand] and are defined here only as a convenience.
    /// <table border="1">
    /// <caption>The command section's description</caption>
    /// <tbody><tr>
    ///     <th>Node</th>
    ///     <th>Method</th>
    ///     <th>Type</th>
    ///     <th>Description</th>
    ///     <th>Example</th>
    /// </tr><tr>
    ///     <td><code>description</code></td>
    ///     <td>[PluginCommand#setDescription(String)]</td>
    ///     <td>String</td>
    ///     <td>A user-friendly description for a command. It is useful for
    ///         documentation purposes as well as in-game help.</td>
    ///     <td><blockquote><pre>description: Set yourself on fire</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>aliases</code></td>
    ///     <td>[PluginCommand#setAliases(List)]</td>
    ///     <td>String or <a href="https://en.wikipedia.org/wiki/YAML#Lists">List</a> of
    ///         strings</td>
    ///     <td>Alternative command names, with special usefulness for commands
    ///         that are already registered. <i>Aliases are not effective when
    ///         defined at runtime,</i> so the plugin description file is the
    ///         only way to have them properly defined.
    ///         <p>
    ///         Note: Command aliases may not have a colon in them.</p></td>
    ///     <td>Single alias format:
    ///         <blockquote><pre>aliases: combust_me</pre></blockquote> or
    ///         multiple alias format:
    ///         <blockquote><pre>aliases: [combust_me, combustMe]</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>permission</code></td>
    ///     <td>[PluginCommand#setPermission(String)]</td>
    ///     <td>String</td>
    ///     <td>The name of the [Permission] required to use the command.
    ///         A user without the permission will receive the specified
    ///         message (see
    /// [below][PluginCommand#setPermissionMessage(String)]), or a
    ///         standard one if no specific message is defined. Without the
    ///         permission node, no
    /// [<code>CommandExecutor</code>][PluginCommand#setExecutor(CommandExecutor)] or
    ///         [PluginCommand#setTabCompleter(TabCompleter)] will be called.</td>
    ///     <td><blockquote><pre>permission: inferno.flagrate</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>permission-message</code></td>
    ///     <td>[PluginCommand#setPermissionMessage(String)]</td>
    ///     <td>String</td>
    ///     <td><ul>
    ///         <li>Displayed to a player that attempts to use a command, but
    ///             does not have the required permission. See
    /// [<code>above</code>][PluginCommand#getPermission()].
    ///         </li><li>&lt;permission&gt; is a macro that is replaced with the
    ///             permission node required to use the command.
    ///         </li><li>Using empty quotes is a valid way to indicate nothing
    ///             should be displayed to a player.
    ///         </li></ul></td>
    ///     <td><blockquote><pre>permission-message: You do not have /&lt;permission&gt;</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>usage</code></td>
    ///     <td>[PluginCommand#setUsage(String)]</td>
    ///     <td>String</td>
    ///     <td>This message is displayed to a player when the
    /// [PluginCommand#setExecutor(CommandExecutor)]
    /// [returns false][CommandExecutor#onCommand(CommandSender, Command, String, String[])].
    ///         &lt;command&gt; is a macro that is replaced the command issued.</td>
    ///     <td><blockquote><pre>usage: Syntax error! Perhaps you meant /&lt;command&gt; PlayerName?</pre></blockquote>
    ///         It is worth noting that to use a colon in a yaml, like
    ///         <code>`usage: Usage: /god [player]'</code>, you need to
    ///         <a href="http://yaml.org/spec/current.html#id2503232">surround
    ///         the message with double-quote</a>:
    ///         <blockquote><pre>usage: "Usage: /god [player]"</pre></blockquote></td>
    /// </tr>
    /// </tbody></table>
    ///
    /// The commands are structured as a hierarchy of [nested mappings](http://yaml.org/spec/current.html#id2502325).
    /// The primary (top-level, no intendentation) node is
    /// \``commands`', while each individual command name is
    /// indented, indicating it maps to some value (in our case, the
    /// properties of the table above).
    ///
    /// Here is an example bringing together the piecemeal examples above, as
    /// well as few more definitions:
    ///
    /// ```
    /// commands:
    ///  flagrate:
    ///    description: Set yourself on fire.
    ///    aliases: [combust_me, combustMe]
    ///    permission: inferno.flagrate
    ///    permission-message: You do not have /<permission>
    ///    usage: Syntax error! Perhaps you meant /<command> PlayerName?
    ///  burningdeaths:
    ///    description: List how many times you have died by fire.
    ///    aliases:
    ///    - burning_deaths
    ///    - burningDeaths
    ///    permission: inferno.burningdeaths
    ///    usage: |
    ///      /<command> [player]
    ///      Example: /<command> - see how many times you have burned to death
    ///      Example: /<command> CaptainIce - see how many times CaptainIce has burned to death
    ///  # The next command has no description, aliases, etc. defined, but is still valid
    ///  # Having an empty declaration is useful for defining the description, permission, and messages from a configuration dynamically
    ///  apocalypse:
    /// ```
    ///
    /// Note: Command names may not have a colon in their name.
    ///
    /// @return the commands this plugin will register
    @NotNull
    public Map<String, Map<String, Object>> getCommands() {
        return commands;
    }

    /// Gives the list of permissions the plugin will register at runtime,
    /// immediately preceding enabling. The format for defining permissions is
    /// a map from permission name to properties. To represent a map without
    /// any specific property, empty [curly-braces](http://yaml.org/spec/current.html#id2502702) (
    /// `{}` ) may be used (as a null value is not
    /// accepted, unlike the [`commands`][#getCommands()] above).
    ///
    /// A list of optional properties for permissions:
    /// <table border="1">
    /// <caption>The permission section's description</caption>
    /// <tbody><tr>
    ///     <th>Node</th>
    ///     <th>Description</th>
    ///     <th>Example</th>
    /// </tr><tr>
    ///     <td><code>description</code></td>
    ///     <td>Plaintext (user-friendly) description of what the permission
    ///         is for.</td>
    ///     <td><blockquote><pre>description: Allows you to set yourself on fire</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>default</code></td>
    ///     <td>The default state for the permission, as defined by
    /// [Permission#getDefault()]. If not defined, it will be set to
    ///         the value of [PluginDescriptionFile#getPermissionDefault()].
    ///         <p>
    ///         For reference:</p><ul>
    ///         <li><code>true</code> - Represents a positive assignment to
    ///             [<code>permissibles</code>][Permissible].
    ///         </li><li><code>false</code> - Represents no assignment to
    /// [<code>permissibles</code>][Permissible].
    ///         </li><li><code>op</code> - Represents a positive assignment to
    ///             [<code>operator permissibles</code>][Permissible#isOp()].
    ///         </li><li><code>notop</code> - Represents a positive assignment to
    ///             [<code>non-operator permissibiles</code>][Permissible#isOp()].
    ///         </li></ul></td>
    ///     <td><blockquote><pre>default: true</pre></blockquote></td>
    /// </tr><tr>
    ///     <td><code>children</code></td>
    ///     <td>Allows other permissions to be set as a
    /// [relation][Permission#getChildren()] to the parent permission.
    ///         When a parent permissions is assigned, child permissions are
    ///         respectively assigned as well.
    ///         <ul>
    ///         <li>When a parent permission is assigned negatively, child
    ///             permissions are assigned based on an inversion of their
    ///             association.
    ///         </li><li>When a parent permission is assigned positively, child
    ///             permissions are assigned based on their association.
    ///         </li></ul>
    ///         <p>
    ///         Child permissions may be defined in a number of ways:</p><ul>
    ///         <li>Children may be defined as a <a href="https://en.wikipedia.org/wiki/YAML#Lists">list</a> of
    ///             names. Using a list will treat all children associated
    ///             positively to their parent.
    ///         </li><li>Children may be defined as a map. Each permission name maps
    ///             to either a boolean (representing the association), or a
    ///             nested permission definition (just as another permission).
    ///             Using a nested definition treats the child as a positive
    ///             association.
    ///         </li><li>A nested permission definition must be a map of these same
    ///             properties. To define a valid nested permission without
    ///             defining any specific property, empty curly-braces (
    ///             <code>{}</code> ) must be used.
    ///          </li><li>A nested permission may carry its own nested permissions
    ///              as children, as they may also have nested permissions, and
    ///              so forth. There is no direct limit to how deep the
    ///              permission tree is defined.
    ///         </li></ul></td>
    ///     <td>As a list:
    ///         <blockquote><pre>children: [inferno.flagrate, inferno.burningdeaths]</pre></blockquote>
    ///         Or as a mapping:
    ///         <blockquote><pre>children:
    ///  inferno.flagrate: true
    ///  inferno.burningdeaths: true</pre></blockquote>
    ///         An additional example showing basic nested values can be seen
    ///         <a href="doc-files/permissions-example_plugin.yml">here</a>.
    ///         </td>
    /// </tr>
    /// </tbody></table>
    ///
    /// The permissions are structured as a hierarchy of [nested mappings](http://yaml.org/spec/current.html#id2502325).
    /// The primary (top-level, no indentation) node is
    /// \``permissions`', while each individual permission name is
    /// indented, indicating it maps to some value (in our case, the
    /// properties of the table above).
    ///
    /// Here is an example using some of the properties:
    ///
    /// ```
    /// permissions:
    ///  inferno.*:
    ///    description: Gives access to all Inferno commands
    ///    children:
    ///      inferno.flagrate: true
    ///      inferno.burningdeaths: true
    ///  inferno.flagate:
    ///    description: Allows you to ignite yourself
    ///    default: true
    ///  inferno.burningdeaths:
    ///    description: Allows you to see how many times you have burned to death
    ///    default: true
    /// ```
    ///
    /// Another example, with nested definitions, can be found [here](doc-files/permissions-example_plugin.yml).
    ///
    /// @return the permissions this plugin will register
    @NotNull
    public List<Permission> getPermissions() {
        if (permissions == null) {
            if (lazyPermissions == null) {
                permissions = ImmutableList.<Permission>of();
            } else {
                permissions = ImmutableList.copyOf(Permission.loadPermissions(lazyPermissions, "Permission node '%s' in plugin description file for " + getFullName() + " is invalid", defaultPerm));
                lazyPermissions = null;
            }
        }
        return permissions;
    }

    /// Gives the default [`default`][Permission#getDefault()] state of
    /// [`permissions`][#getPermissions()] registered for the plugin.
    ///
    ///   - If not specified, it will be [PermissionDefault#OP].
    ///   - It is matched using [PermissionDefault#getByName(String)]
    ///   - It only affects permissions that do not define the
    ///     `default` node.
    ///   - It may be any value in [PermissionDefault].
    ///
    /// In the plugin.yml, this entry is named `default-permission`.
    ///
    /// Example:
    ///
    /// ```
    /// default-permission: NOT_OP
    /// ```
    ///
    /// @return the default value for the plugin's permissions
    @NotNull
    public PermissionDefault getPermissionDefault() {
        return defaultPerm;
    }

    /// Gives a set of every [PluginAwareness] for a plugin. An awareness
    /// dictates something that a plugin developer acknowledges when the plugin
    /// is compiled. Some implementations may define extra awarenesses that are
    /// not included in the API. Any unrecognized
    /// awareness (one unsupported or in a future version) will cause a dummy
    /// object to be created instead of failing.
    ///
    ///   - Currently only supports the enumerated values in
    ///     [PluginAwareness.Flags].
    ///   - Each awareness starts the identifier with bang-at
    ///     (`!@`).
    ///   - Unrecognized (future / unimplemented) entries are quietly replaced
    ///     by a generic object that implements PluginAwareness.
    ///   - A type of awareness must be defined by the runtime and acknowledged
    ///     by the API, effectively discluding any derived type from any
    ///     plugin's classpath.
    ///   - `awareness` must be in [YAML list
    ///     format](https://en.wikipedia.org/wiki/YAML#Lists).
    ///
    /// In the plugin.yml, this entry is named `awareness`.
    ///
    /// Example:
    ///
    /// ```
    /// awareness:
    /// - !@UTF8
    /// ```
    ///
    /// **Note:** Although unknown versions of some future awareness are
    /// gracefully substituted, previous versions of Bukkit (ones prior to the
    /// first implementation of awareness) will fail to load a plugin that
    /// defines any awareness.
    ///
    /// @return a set containing every awareness for the plugin
    @NotNull
    public Set<PluginAwareness> getAwareness() {
        return awareness;
    }

    /// Returns the name of a plugin, including the version. This method is
    /// provided for convenience; it uses the [#getName()] and
    /// [#getVersion()] entries.
    ///
    /// @return a descriptive name of the plugin and respective version
    @NotNull
    public String getFullName() {
        return name + " v" + version;
    }

    /// Gives the API version which this plugin is designed to support. No
    /// specific format is guaranteed.
    ///
    ///   - Refer to release notes for supported API versions.
    ///
    /// In the plugin.yml, this entry is named `api-version`.
    ///
    /// Example:
    ///
    /// ```
    /// api-version: 1.13
    /// ```
    ///
    /// @return the version of the plugin
    @Nullable
    public String getAPIVersion() {
        return apiVersion;
    }

    /// Gets the libraries this plugin requires. This is a preview feature.
    ///
    ///   - Libraries must be GAV specifiers and are loaded from Maven Central.
    ///
    /// Example:
    ///
    /// ```
    /// libraries:
    ///     - com.squareup.okhttp3:okhttp:4.9.0
    /// ```
    ///
    /// @return required libraries
    @NotNull
    public List<String> getLibraries() {
        return libraries;
    }

    /// @return unused
    /// @deprecated unused
    @Deprecated(since = "1.7.2", forRemoval = true)
    @Nullable
    public String getClassLoaderOf() {
        return classLoaderOf;
    }

    /// Saves this PluginDescriptionFile to the given writer
    ///
    /// @param writer Writer to output this file to
    public void save(@NotNull Writer writer) {
        YAML.get().dump(saveMap(), writer);
    }

    private void loadMap(@NotNull Map<?, ?> map) throws InvalidDescriptionException {
        try {
            name = rawName = map.get("name").toString();

            if (!VALID_NAME.matcher(name).matches()) {
                throw new InvalidDescriptionException("name '" + name + "' contains invalid characters.");
            }
            name = name.replace(' ', '_');
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "name is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "name is of wrong type");
        }

        provides = makePluginNameList(map, "provides");

        try {
            version = map.get("version").toString();
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "version is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "version is of wrong type");
        }

        try {
            main = map.get("main").toString();
            if (main.startsWith("org.bukkit.")) {
                throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
            }
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "main is not defined");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "main is of wrong type");
        }

        if (map.get("commands") != null) {
            ImmutableMap.Builder<String, Map<String, Object>> commandsBuilder = ImmutableMap.<String, Map<String, Object>>builder();
            try {
                for (Map.Entry<?, ?> command : ((Map<?, ?>) map.get("commands")).entrySet()) {
                    ImmutableMap.Builder<String, Object> commandBuilder = ImmutableMap.<String, Object>builder();
                    if (command.getValue() != null) {
                        for (Map.Entry<?, ?> commandEntry : ((Map<?, ?>) command.getValue()).entrySet()) {
                            if (commandEntry.getValue() instanceof Iterable) {
                                // This prevents internal alias list changes
                                ImmutableList.Builder<Object> commandSubList = ImmutableList.<Object>builder();
                                for (Object commandSubListItem : (Iterable<?>) commandEntry.getValue()) {
                                    if (commandSubListItem != null) {
                                        commandSubList.add(commandSubListItem);
                                    }
                                }
                                commandBuilder.put(commandEntry.getKey().toString(), commandSubList.build());
                            } else if (commandEntry.getValue() != null) {
                                commandBuilder.put(commandEntry.getKey().toString(), commandEntry.getValue());
                            }
                        }
                    }
                    commandsBuilder.put(command.getKey().toString(), commandBuilder.build());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "commands are of wrong type");
            }
            commands = commandsBuilder.build();
        }

        if (map.get("class-loader-of") != null) {
            classLoaderOf = map.get("class-loader-of").toString();
        }

        depend = makePluginNameList(map, "depend");
        softDepend = makePluginNameList(map, "softdepend");
        loadBefore = makePluginNameList(map, "loadbefore");

        if (map.get("website") != null) {
            website = map.get("website").toString();
        }

        if (map.get("description") != null) {
            description = map.get("description").toString();
        }

        if (map.get("load") != null) {
            try {
                order = PluginLoadOrder.valueOf(((String) map.get("load")).toUpperCase(Locale.ROOT).replaceAll("\\W", ""));
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "load is of wrong type");
            } catch (IllegalArgumentException ex) {
                throw new InvalidDescriptionException(ex, "load is not a valid choice");
            }
        }

        if (map.get("authors") != null) {
            ImmutableList.Builder<String> authorsBuilder = ImmutableList.<String>builder();
            if (map.get("author") != null) {
                authorsBuilder.add(map.get("author").toString());
            }
            try {
                for (Object o : (Iterable<?>) map.get("authors")) {
                    authorsBuilder.add(o.toString());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "authors are of wrong type");
            } catch (NullPointerException ex) {
                throw new InvalidDescriptionException(ex, "authors are improperly defined");
            }
            authors = authorsBuilder.build();
        } else if (map.get("author") != null) {
            authors = ImmutableList.of(map.get("author").toString());
        } else {
            authors = ImmutableList.<String>of();
        }

        if (map.get("contributors") != null) {
            ImmutableList.Builder<String> contributorsBuilder = ImmutableList.<String>builder();
            try {
                for (Object o : (Iterable<?>) map.get("contributors")) {
                    contributorsBuilder.add(o.toString());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "contributors are of wrong type");
            }
            contributors = contributorsBuilder.build();
        } else {
            contributors = ImmutableList.<String>of();
        }

        if (map.get("default-permission") != null) {
            try {
                defaultPerm = PermissionDefault.getByName(map.get("default-permission").toString());
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "default-permission is of wrong type");
            } catch (IllegalArgumentException ex) {
                throw new InvalidDescriptionException(ex, "default-permission is not a valid choice");
            }
        }

        if (map.get("awareness") instanceof Iterable) {
            Set<PluginAwareness> awareness = new HashSet<PluginAwareness>();
            try {
                for (Object o : (Iterable<?>) map.get("awareness")) {
                    awareness.add((PluginAwareness) o);
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "awareness has wrong type");
            }
            this.awareness = ImmutableSet.copyOf(awareness);
        }

        if (map.get("api-version") != null) {
            apiVersion = map.get("api-version").toString();
        }

        if (map.get("libraries") != null) {
            ImmutableList.Builder<String> contributorsBuilder = ImmutableList.<String>builder();
            try {
                for (Object o : (Iterable<?>) map.get("libraries")) {
                    contributorsBuilder.add(o.toString());
                }
            } catch (ClassCastException ex) {
                throw new InvalidDescriptionException(ex, "libraries are of wrong type");
            }
            libraries = contributorsBuilder.build();
        } else {
            libraries = ImmutableList.<String>of();
        }
        // Paper start - plugin loader api
        if (map.containsKey("paper-plugin-loader")) {
            this.paperPluginLoader = map.get("paper-plugin-loader").toString();
        }

        /*
        Allow skipping the Bukkit/Spigot 'libraries' list. By default, both the 'libraries'
        list and the 'paper-plugin-loader' will contribute libraries. It may be desired to only
        use one or the other. (i.e. 'libraries' on Spigot and 'paper-plugin-loader' on Paper)
        */
        if (map.containsKey("paper-skip-libraries")) {
            String skip = map.get("paper-skip-libraries").toString();
            if (skip.equalsIgnoreCase("true")) {
                this.libraries = ImmutableList.of();
            }
        }
        // Paper end - plugin loader api

        try {
            lazyPermissions = (Map<?, ?>) map.get("permissions");
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, "permissions are of the wrong type");
        }

        if (map.get("prefix") != null) {
            prefix = map.get("prefix").toString();
        }
    }

    @NotNull
    private static List<String> makePluginNameList(@NotNull final Map<?, ?> map, @NotNull final String key) throws InvalidDescriptionException {
        final Object value = map.get(key);
        if (value == null) {
            return ImmutableList.of();
        }

        final ImmutableList.Builder<String> builder = ImmutableList.<String>builder();
        try {
            for (final Object entry : (Iterable<?>) value) {
                builder.add(entry.toString().replace(' ', '_'));
            }
        } catch (ClassCastException ex) {
            throw new InvalidDescriptionException(ex, key + " is of wrong type");
        } catch (NullPointerException ex) {
            throw new InvalidDescriptionException(ex, "invalid " + key + " format");
        }
        return builder.build();
    }

    @NotNull
    private Map<String, Object> saveMap() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("name", name);
        if (provides != null) {
            map.put("provides", provides);
        }
        map.put("main", main);
        map.put("version", version);
        map.put("order", order.toString());
        map.put("default-permission", defaultPerm.toString());

        if (commands != null) {
            map.put("command", commands);
        }
        if (depend != null) {
            map.put("depend", depend);
        }
        if (softDepend != null) {
            map.put("softdepend", softDepend);
        }
        if (website != null) {
            map.put("website", website);
        }
        if (description != null) {
            map.put("description", description);
        }

        if (authors.size() == 1) {
            map.put("author", authors.get(0));
        } else if (authors.size() > 1) {
            map.put("authors", authors);
        }

        if (contributors != null) {
            map.put("contributors", contributors);
        }

        if (apiVersion != null) {
            map.put("api-version", apiVersion);
        }

        if (libraries != null) {
            map.put("libraries", libraries);
        }

        if (classLoaderOf != null) {
            map.put("class-loader-of", classLoaderOf);
        }

        if (prefix != null) {
            map.put("prefix", prefix);
        }

        return map;
    }

    @NotNull
    private Map<?, ?> asMap(@NotNull Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return (Map<?, ?>) object;
        }
        throw new InvalidDescriptionException("Plugin description file is empty or not properly structured. Is " + object + "but should be a map.");
    }

    /// @hidden
    @ApiStatus.Internal
    @NotNull
    public String getRawName() {
        return rawName;
    }
}
