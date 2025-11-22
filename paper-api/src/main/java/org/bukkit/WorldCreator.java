package org.bukkit;

import com.google.common.base.Preconditions;
import java.util.Random;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents various types of options that may be used to create a world.
 */
public class WorldCreator {
    private final NamespacedKey key; // Paper
    private final String name;
    private long seed;
    private World.Environment environment = World.Environment.NORMAL;
    private ChunkGenerator generator = null;
    private BiomeProvider biomeProvider = null;
    private WorldType type = WorldType.NORMAL;
    private boolean generateStructures = true;
    private String generatorSettings = "";
    private boolean hardcore = false;
    private boolean bonusChest = false;

    /**
     * Creates an empty WorldCreationOptions for the given world name
     *
     * @param name Name of the world that will be created
     */
    public WorldCreator(@NotNull String name) {
        // Paper start
        this(name, getWorldKey(name));
    }

    private static NamespacedKey getWorldKey(String name) {
        final String mainLevelName = Bukkit.getUnsafe().getMainLevelName();
        if (name.equals(mainLevelName)) {
            return NamespacedKey.minecraft("overworld");
        } else if (name.equals(mainLevelName + "_nether")) {
            return NamespacedKey.minecraft("the_nether");
        } else if (name.equals(mainLevelName + "_the_end")) {
            return NamespacedKey.minecraft("the_end");
        } else {
            return NamespacedKey.minecraft(name.toLowerCase(java.util.Locale.ENGLISH).replace(" ", "_"));
        }
    }

    /**
     * Creates an empty WorldCreator for the given world name and key
     *
     * @param levelName LevelName of the world that will be created
     * @param worldKey NamespacedKey of the world that will be created
     */
    public WorldCreator(@NotNull String levelName, @NotNull NamespacedKey worldKey) {
        if (levelName == null || worldKey == null) {
            throw new IllegalArgumentException("World name and key cannot be null");
        }
        this.name = levelName;
        this.seed = (new Random()).nextLong();
        this.key = worldKey;
    }

    /**
     * Creates an empty WorldCreator for the given key.
     * LevelName will be the Key part of the NamespacedKey.
     *
     * @param worldKey NamespacedKey of the world that will be created
     */
    public WorldCreator(@NotNull NamespacedKey worldKey) {
        this(worldKey.getKey(), worldKey);
    }

    /**
     * Gets the key for this WorldCreator
     *
     * @return the key
     */
    @NotNull
    public NamespacedKey key() {
        return key;
    }

    /**
     * Creates an empty WorldCreator for the given world name and key
     *
     * @param levelName LevelName of the world that will be created
     * @param worldKey NamespacedKey of the world that will be created
     */
    @NotNull
    public static WorldCreator ofNameAndKey(@NotNull String levelName, @NotNull NamespacedKey worldKey) {
        return new WorldCreator(levelName, worldKey);
    }

    /**
     * Creates an empty WorldCreator for the given key.
     * LevelName will be the Key part of the NamespacedKey.
     *
     * @param worldKey NamespacedKey of the world that will be created
     */
    @NotNull
    public static WorldCreator ofKey(@NotNull NamespacedKey worldKey) {
        return new WorldCreator(worldKey);
    }
    // Paper end

    /**
     * Copies the options from the specified world
     *
     * @param world World to copy options from
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator copy(@NotNull World world) {
        Preconditions.checkArgument(world != null, "World cannot be null");

        seed = world.getSeed();
        environment = world.getEnvironment();
        generator = world.getGenerator();
        biomeProvider = world.getBiomeProvider();
        type = world.getWorldType();
        generateStructures = world.canGenerateStructures();
        hardcore = world.isHardcore();
        bonusChest = world.hasBonusChest();

        return this;
    }

    /**
     * Copies the options from the specified {@link WorldCreator}
     *
     * @param creator World creator to copy options from
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator copy(@NotNull WorldCreator creator) {
        Preconditions.checkArgument(creator != null, "Creator cannot be null");

        seed = creator.seed();
        environment = creator.environment();
        generator = creator.generator();
        biomeProvider = creator.biomeProvider();
        type = creator.type();
        generateStructures = creator.generateStructures();
        generatorSettings = creator.generatorSettings();
        hardcore = creator.hardcore();
        bonusChest = creator.bonusChest();

        return this;
    }

    /**
     * Gets the name of the world that is to be loaded or created.
     *
     * @return World name
     */
    @NotNull
    public String name() {
        return name;
    }

    /**
     * Gets the seed that will be used to create this world
     *
     * @return World seed
     */
    public long seed() {
        return seed;
    }

    /**
     * Sets the seed that will be used to create this world
     *
     * @param seed World seed
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator seed(long seed) {
        this.seed = seed;

        return this;
    }

    /**
     * Gets the environment that will be used to create or load the world
     *
     * @return World environment
     */
    @NotNull
    public World.Environment environment() {
        return environment;
    }

    /**
     * Sets the environment that will be used to create or load the world
     *
     * @param env World environment
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator environment(@NotNull World.Environment env) {
        this.environment = env;

        return this;
    }

    /**
     * Gets the type of the world that will be created or loaded
     *
     * @return World type
     */
    @NotNull
    public WorldType type() {
        return type;
    }

    /**
     * Sets the type of the world that will be created or loaded
     *
     * @param type World type
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator type(@NotNull WorldType type) {
        this.type = type;

        return this;
    }

    /**
     * Gets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     *
     * @return Chunk generator
     */
    @Nullable
    public ChunkGenerator generator() {
        return generator;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     *
     * @param generator Chunk generator
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator generator(@Nullable ChunkGenerator generator) {
        this.generator = generator;

        return this;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     * <p>
     * If the generator cannot be found for the given name, the natural
     * environment generator will be used instead and a warning will be
     * printed to the console.
     *
     * @param generator Name of the generator to use, in "plugin:id" notation
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator generator(@Nullable String generator) {
        this.generator = getGeneratorForName(name, generator, Bukkit.getConsoleSender());

        return this;
    }

    /**
     * Sets the generator that will be used to create or load the world.
     * <p>
     * This may be null, in which case the "natural" generator for this
     * environment will be used.
     * <p>
     * If the generator cannot be found for the given name, the natural
     * environment generator will be used instead and a warning will be
     * printed to the specified output
     *
     * @param generator Name of the generator to use, in "plugin:id" notation
     * @param output {@link CommandSender} that will receive any error
     *     messages
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator generator(@Nullable String generator, @Nullable CommandSender output) {
        this.generator = getGeneratorForName(name, generator, output);

        return this;
    }

    /**
     * Gets the biome provider that will be used to create or load the world.
     * <p>
     * This may be null, in which case the biome provider from the {@link ChunkGenerator}
     * will be used. If no {@link ChunkGenerator} is specific the "natural" biome provider
     * for this environment will be used.
     *
     * @return Biome provider
     */
    @Nullable
    public BiomeProvider biomeProvider() {
        return biomeProvider;
    }

    /**
     * Sets the biome provider that will be used to create or load the world.
     * <p>
     * This may be null, in which case the biome provider from the
     * {@link ChunkGenerator} will be used. If no {@link ChunkGenerator} is
     * specific the "natural" biome provider for this environment will be used.
     *
     * @param biomeProvider Biome provider
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator biomeProvider(@Nullable BiomeProvider biomeProvider) {
        this.biomeProvider = biomeProvider;

        return this;
    }

    /**
     * Sets the biome provider that will be used to create or load the world.
     * <p>
     * This may be null, in which case the biome provider from the
     * {@link ChunkGenerator} will be used. If no {@link ChunkGenerator} is
     * specific the "natural" biome provider for this environment will be used.
     * <p>
     * If the biome provider cannot be found for the given name and no
     * {@link ChunkGenerator} is specific, the natural environment biome
     * provider will be used instead and a warning will be printed to the
     * specified output
     *
     * @param biomeProvider Name of the biome provider to use, in "plugin:id"
     * notation
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator biomeProvider(@Nullable String biomeProvider) {
        this.biomeProvider = getBiomeProviderForName(name, biomeProvider, Bukkit.getConsoleSender());

        return this;
    }

    /**
     * Sets the biome provider that will be used to create or load the world.
     * <p>
     * This may be null, in which case the biome provider from the
     * {@link ChunkGenerator} will be used. If no {@link ChunkGenerator} is
     * specific the "natural" biome provider for this environment will be used.
     * <p>
     * If the biome provider cannot be found for the given name and no
     * {@link ChunkGenerator} is specific, the natural environment biome
     * provider will be used instead and a warning will be printed to the
     * specified output
     *
     * @param biomeProvider Name of the biome provider to use, in "plugin:id"
     * notation
     * @param output {@link CommandSender} that will receive any error messages
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator biomeProvider(@Nullable String biomeProvider, @Nullable CommandSender output) {
        this.biomeProvider = getBiomeProviderForName(name, biomeProvider, output);

        return this;
    }

    /**
     * Sets the generator settings of the world that will be created or loaded.
     * <p>
     * Currently only {@link WorldType#FLAT} uses these settings, and expects
     * them to be in JSON format with a valid biome (1.18.2 and
     * above) defined. An example valid configuration is as follows:
     * <code>{"layers": [{"block": "stone", "height": 1}, {"block": "grass_block", "height": 1}], "biome":"plains"}</code>
     *
     * @param generatorSettings The settings that should be used by the
     * generator
     * @return This object, for chaining
     * @see <a href="https://minecraft.wiki/w/Custom_dimension">Custom
     * dimension</a> (scroll to "When the generator ID type is
     * <code>minecraft:flat</code>)"
     */
    @NotNull
    public WorldCreator generatorSettings(@NotNull String generatorSettings) {
        this.generatorSettings = generatorSettings;

        return this;
    }

    /**
     * Gets the generator settings of the world that will be created or loaded.
     *
     * @return The settings that should be used by the generator
     * @see #generatorSettings(java.lang.String)
     */
    @NotNull
    public String generatorSettings() {
        return generatorSettings;
    }

    /**
     * Sets whether or not worlds created or loaded with this creator will
     * have structures.
     *
     * @param generate Whether to generate structures
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator generateStructures(boolean generate) {
        this.generateStructures = generate;

        return this;
    }

    /**
     * Gets whether or not structures will be generated in the world.
     *
     * @return True if structures will be generated
     */
    public boolean generateStructures() {
        return generateStructures;
    }

    /**
     * Sets whether the world will be hardcore or not.
     *
     * In a hardcore world the difficulty will be locked to hard.
     *
     * @param hardcore Whether the world will be hardcore
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator hardcore(boolean hardcore) {
        this.hardcore = hardcore;

        return this;
    }

    /**
     * Gets whether the world will be hardcore or not.
     * <p>
     * In a hardcore world the difficulty will be locked to hard.
     *
     * @return hardcore status
     */
    public boolean hardcore() {
        return hardcore;
    }

    /**
     * Sets whether a bonus chest should be generated or not.
     *
     * @param bonusChest indicating whether the bonus chest should be generated
     * @return This object, for chaining
     */
    @NotNull
    public WorldCreator bonusChest(final boolean bonusChest) {
        this.bonusChest = bonusChest;
        return this;
    }

    /**
     * Gets whether the bonus chest feature is enabled.
     *
     * @return true if the bonus chest is enabled, false otherwise.
     */
    public boolean bonusChest() {
        return bonusChest;
    }

    /**
     * Creates a world with the specified options.
     * <p>
     * If the world already exists, it will be loaded from disk and some
     * options may be ignored.
     *
     * @return Newly created or loaded world
     */
    @Nullable
    public World createWorld() {
        return Bukkit.createWorld(this);
    }

    /**
     * Creates a new {@link WorldCreator} for the given world name
     *
     * @param name Name of the world to load or create
     * @return Resulting WorldCreator
     */
    @NotNull
    public static WorldCreator name(@NotNull String name) {
        return new WorldCreator(name);
    }

    /**
     * Attempts to get the {@link ChunkGenerator} with the given name.
     * <p>
     * If the generator is not found, null will be returned and a message will
     * be printed to the specified {@link CommandSender} explaining why.
     * <p>
     * The name must be in the "plugin:id" notation, or optionally just
     * "plugin", where "plugin" is the safe-name of a plugin and "id" is an
     * optional unique identifier for the generator you wish to request from
     * the plugin.
     *
     * @param world Name of the world this will be used for
     * @param name Name of the generator to retrieve
     * @param output Where to output if errors are present
     * @return Resulting generator, or null
     */
    @Nullable
    public static ChunkGenerator getGeneratorForName(@NotNull String world, @Nullable String name, @Nullable CommandSender output) {
        Preconditions.checkArgument(world != null, "World name must be specified");
        ChunkGenerator result = null;

        if (output == null) {
            output = Bukkit.getConsoleSender();
        }

        if (name != null) {
            String[] split = name.split(":", 2);
            String id = (split.length > 1) ? split[1] : null;
            Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);

            if (plugin == null) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                output.sendMessage("Could not set generator for world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled");
            } else {
                result = plugin.getDefaultWorldGenerator(world, id);
            }
        }

        return result;
    }

    /**
     * Attempts to get the {@link BiomeProvider} with the given name.
     * <p>
     * If the biome provider is not found, null will be returned and a message
     * will be printed to the specified {@link CommandSender} explaining why.
     * <p>
     * The name must be in the "plugin:id" notation, or optionally just
     * "plugin", where "plugin" is the safe-name of a plugin and "id" is an
     * optional unique identifier for the biome provider you wish to request
     * from the plugin.
     *
     * @param world Name of the world this will be used for
     * @param name Name of the biome provider to retrieve
     * @param output Where to output if errors are present
     * @return Resulting biome provider, or null
     */
    @Nullable
    public static BiomeProvider getBiomeProviderForName(@NotNull String world, @Nullable String name, @Nullable CommandSender output) {
        Preconditions.checkArgument(world != null, "World name must be specified");
        BiomeProvider result = null;

        if (output == null) {
            output = Bukkit.getConsoleSender();
        }

        if (name != null) {
            String[] split = name.split(":", 2);
            String id = (split.length > 1) ? split[1] : null;
            Plugin plugin = Bukkit.getPluginManager().getPlugin(split[0]);

            if (plugin == null) {
                output.sendMessage("Could not set biome provider for world '" + world + "': Plugin '" + split[0] + "' does not exist");
            } else if (!plugin.isEnabled()) {
                output.sendMessage("Could not set set biome provider for world '" + world + "': Plugin '" + plugin.getDescription().getFullName() + "' is not enabled");
            } else {
                result = plugin.getDefaultBiomeProvider(world, id);
            }
        }

        return result;
    }

    // Paper start - keep spawn loaded tristate
    /**
     * Returns the current intent to keep the world loaded, @see {@link WorldCreator#keepSpawnLoaded(net.kyori.adventure.util.TriState)}
     *
     * @return the current tristate value
     * @deprecated completely unfunctional as the server no longer has always loaded spawn chunks.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.21.9")
    public net.kyori.adventure.util.TriState keepSpawnLoaded() {
        return net.kyori.adventure.util.TriState.FALSE;
    }

    /**
     * Controls if a world should be kept loaded or not, default (NOT_SET) will use the servers standard
     * configuration, otherwise, will act as an override towards this setting
     *
     * @param keepSpawnLoaded the new value
     * @return This object, for chaining
     * @deprecated completely unfunctional as the server no longer has always loaded spawn chunks.
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.21.9")
    public WorldCreator keepSpawnLoaded(@NotNull net.kyori.adventure.util.TriState keepSpawnLoaded) {
        return this;
    }
    // Paper end - keep spawn loaded tristate
}
