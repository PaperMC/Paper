package org.bukkit.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a section of a {@link Configuration}
 */
public interface ConfigurationSection {
    /**
     * Gets a set containing all keys in this section.
     * <p>
     * If deep is set to true, then this will contain all the keys within any
     * child {@link ConfigurationSection}s (and their children, etc). These
     * will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys of any
     * direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *     list.
     * @return Set of keys contained within this ConfigurationSection.
     */
    @NotNull
    public Set<String> getKeys(boolean deep);

    /**
     * Gets a Map containing all keys and their values for this section.
     * <p>
     * If deep is set to true, then this will contain all the keys and values
     * within any child {@link ConfigurationSection}s (and their children,
     * etc). These keys will be in a valid path notation for you to use.
     * <p>
     * If deep is set to false, then this will contain only the keys and
     * values of any direct children, and not their own children.
     *
     * @param deep Whether or not to get a deep list, as opposed to a shallow
     *     list.
     * @return Map of keys and values of this section.
     */
    @NotNull
    public Map<String, Object> getValues(boolean deep);

    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will return true.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, either via
     *     default or being set.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    public boolean contains(@NotNull String path);

    /**
     * Checks if this {@link ConfigurationSection} contains the given path.
     * <p>
     * If the value for the requested path does not exist, the boolean parameter
     * of true has been specified, a default value for the path exists, this
     * will return true.
     * <p>
     * If a boolean parameter of false has been specified, true will only be
     * returned if there is a set value for the specified path.
     *
     * @param path Path to check for existence.
     * @param ignoreDefault Whether or not to ignore if a default value for the
     * specified path exists.
     * @return True if this section contains the requested path, or if a default
     * value exist and the boolean parameter for this method is true.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    public boolean contains(@NotNull String path, boolean ignoreDefault);

    /**
     * Checks if this {@link ConfigurationSection} has a value set for the
     * given path.
     * <p>
     * If the value for the requested path does not exist but a default value
     * has been specified, this will still return false.
     *
     * @param path Path to check for existence.
     * @return True if this section contains the requested path, regardless of
     *     having a default.
     * @throws IllegalArgumentException Thrown when path is null.
     */
    public boolean isSet(@NotNull String path);

    /**
     * Gets the path of this {@link ConfigurationSection} from its root {@link
     * Configuration}
     * <p>
     * For any {@link Configuration} themselves, this will return an empty
     * string.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     * <p>
     * To retrieve the single name of this section, that is, the final part of
     * the path returned by this method, you may use {@link #getName()}.
     *
     * @return Path of this section relative to its root
     */
    @Nullable
    public String getCurrentPath();

    /**
     * Gets the name of this individual {@link ConfigurationSection}, in the
     * path.
     * <p>
     * This will always be the final part of {@link #getCurrentPath()}, unless
     * the section is orphaned.
     *
     * @return Name of this section
     */
    @NotNull
    public String getName();

    /**
     * Gets the root {@link Configuration} that contains this {@link
     * ConfigurationSection}
     * <p>
     * For any {@link Configuration} themselves, this will return its own
     * object.
     * <p>
     * If the section is no longer contained within its root for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Root configuration containing this section.
     */
    @Nullable
    public Configuration getRoot();

    /**
     * Gets the parent {@link ConfigurationSection} that directly contains
     * this {@link ConfigurationSection}.
     * <p>
     * For any {@link Configuration} themselves, this will return null.
     * <p>
     * If the section is no longer contained within its parent for any reason,
     * such as being replaced with a different value, this may return null.
     *
     * @return Parent section containing this section.
     */
    @Nullable
    public ConfigurationSection getParent();

    /**
     * Gets the requested Object by path.
     * <p>
     * If the Object does not exist but a default value has been specified,
     * this will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Object to get.
     * @return Requested Object.
     */
    @Nullable
    public Object get(@NotNull String path);

    /**
     * Gets the requested Object by path, returning a default value if not
     * found.
     * <p>
     * If the Object does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the Object to get.
     * @param def The default value to return if the path is not found.
     * @return Requested Object.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull String path, @Nullable Object def);

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     * <p>
     * Some implementations may have limitations on what you may store. See
     * their individual javadocs for details. No implementations should allow
     * you to store {@link Configuration}s or {@link ConfigurationSection}s,
     * please use {@link #createSection(java.lang.String)} for that.
     *
     * @param path Path of the object to set.
     * @param value New value to set the path to.
     */
    public void set(@NotNull String path, @Nullable Object value);

    /**
     * Creates an empty {@link ConfigurationSection} at the specified path.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @return Newly created section
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path);

    /**
     * Creates a {@link ConfigurationSection} at the specified path, with
     * specified values.
     * <p>
     * Any value that was previously set at this path will be overwritten. If
     * the previous value was itself a {@link ConfigurationSection}, it will
     * be orphaned.
     *
     * @param path Path to create the section at.
     * @param map The values to used.
     * @return Newly created section
     */
    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map);

    // Primitives
    /**
     * Gets the requested String by path.
     * <p>
     * If the String does not exist but a default value has been specified,
     * this will return the default value. If the String does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the String to get.
     * @return Requested String.
     */
    @Nullable
    public String getString(@NotNull String path);

    /**
     * Gets the requested String by path, returning a default value if not
     * found.
     * <p>
     * If the String does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the String to get.
     * @param def The default value to return if the path is not found or is
     *     not a String.
     * @return Requested String.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public String getString(@NotNull String path, @Nullable String def);

    /**
     * Checks if the specified path is a String.
     * <p>
     * If the path exists but is not a String, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a String and return appropriately.
     *
     * @param path Path of the String to check.
     * @return Whether or not the specified path is a String.
     */
    public boolean isString(@NotNull String path);

    /**
     * Gets the requested int by path.
     * <p>
     * If the int does not exist but a default value has been specified, this
     * will return the default value. If the int does not exist and no default
     * value was specified, this will return 0.
     *
     * @param path Path of the int to get.
     * @return Requested int.
     */
    public int getInt(@NotNull String path);

    /**
     * Gets the requested int by path, returning a default value if not found.
     * <p>
     * If the int does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the int to get.
     * @param def The default value to return if the path is not found or is
     *     not an int.
     * @return Requested int.
     */
    public int getInt(@NotNull String path, int def);

    /**
     * Checks if the specified path is an int.
     * <p>
     * If the path exists but is not a int, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a int and return appropriately.
     *
     * @param path Path of the int to check.
     * @return Whether or not the specified path is an int.
     */
    public boolean isInt(@NotNull String path);

    /**
     * Gets the requested boolean by path.
     * <p>
     * If the boolean does not exist but a default value has been specified,
     * this will return the default value. If the boolean does not exist and
     * no default value was specified, this will return false.
     *
     * @param path Path of the boolean to get.
     * @return Requested boolean.
     */
    public boolean getBoolean(@NotNull String path);

    /**
     * Gets the requested boolean by path, returning a default value if not
     * found.
     * <p>
     * If the boolean does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the boolean to get.
     * @param def The default value to return if the path is not found or is
     *     not a boolean.
     * @return Requested boolean.
     */
    public boolean getBoolean(@NotNull String path, boolean def);

    /**
     * Checks if the specified path is a boolean.
     * <p>
     * If the path exists but is not a boolean, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a boolean and return appropriately.
     *
     * @param path Path of the boolean to check.
     * @return Whether or not the specified path is a boolean.
     */
    public boolean isBoolean(@NotNull String path);

    /**
     * Gets the requested double by path.
     * <p>
     * If the double does not exist but a default value has been specified,
     * this will return the default value. If the double does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path of the double to get.
     * @return Requested double.
     */
    public double getDouble(@NotNull String path);

    /**
     * Gets the requested double by path, returning a default value if not
     * found.
     * <p>
     * If the double does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the double to get.
     * @param def The default value to return if the path is not found or is
     *     not a double.
     * @return Requested double.
     */
    public double getDouble(@NotNull String path, double def);

    /**
     * Checks if the specified path is a double.
     * <p>
     * If the path exists but is not a double, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a double and return appropriately.
     *
     * @param path Path of the double to check.
     * @return Whether or not the specified path is a double.
     */
    public boolean isDouble(@NotNull String path);

    /**
     * Gets the requested long by path.
     * <p>
     * If the long does not exist but a default value has been specified, this
     * will return the default value. If the long does not exist and no
     * default value was specified, this will return 0.
     *
     * @param path Path of the long to get.
     * @return Requested long.
     */
    public long getLong(@NotNull String path);

    /**
     * Gets the requested long by path, returning a default value if not
     * found.
     * <p>
     * If the long does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the long to get.
     * @param def The default value to return if the path is not found or is
     *     not a long.
     * @return Requested long.
     */
    public long getLong(@NotNull String path, long def);

    /**
     * Checks if the specified path is a long.
     * <p>
     * If the path exists but is not a long, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a long and return appropriately.
     *
     * @param path Path of the long to check.
     * @return Whether or not the specified path is a long.
     */
    public boolean isLong(@NotNull String path);

    // Java
    /**
     * Gets the requested List by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the List to get.
     * @return Requested List.
     */
    @Nullable
    public List<?> getList(@NotNull String path);

    /**
     * Gets the requested List by path, returning a default value if not
     * found.
     * <p>
     * If the List does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the List to get.
     * @param def The default value to return if the path is not found or is
     *     not a List.
     * @return Requested List.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull String path, @Nullable List<?> def);

    /**
     * Checks if the specified path is a List.
     * <p>
     * If the path exists but is not a List, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a List and return appropriately.
     *
     * @param path Path of the List to check.
     * @return Whether or not the specified path is a List.
     */
    public boolean isList(@NotNull String path);

    /**
     * Gets the requested List of String by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a String if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of String.
     */
    @NotNull
    public List<String> getStringList(@NotNull String path);

    /**
     * Gets the requested List of Integer by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Integer if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Integer.
     */
    @NotNull
    public List<Integer> getIntegerList(@NotNull String path);

    /**
     * Gets the requested List of Boolean by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Boolean if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Boolean.
     */
    @NotNull
    public List<Boolean> getBooleanList(@NotNull String path);

    /**
     * Gets the requested List of Double by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Double if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Double.
     */
    @NotNull
    public List<Double> getDoubleList(@NotNull String path);

    /**
     * Gets the requested List of Float by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Float if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Float.
     */
    @NotNull
    public List<Float> getFloatList(@NotNull String path);

    /**
     * Gets the requested List of Long by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Long if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Long.
     */
    @NotNull
    public List<Long> getLongList(@NotNull String path);

    /**
     * Gets the requested List of Byte by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Byte if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Byte.
     */
    @NotNull
    public List<Byte> getByteList(@NotNull String path);

    /**
     * Gets the requested List of Character by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Character if
     * possible, but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Character.
     */
    @NotNull
    public List<Character> getCharacterList(@NotNull String path);

    /**
     * Gets the requested List of Short by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Short if possible,
     * but may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Short.
     */
    @NotNull
    public List<Short> getShortList(@NotNull String path);

    /**
     * Gets the requested List of Maps by path.
     * <p>
     * If the List does not exist but a default value has been specified, this
     * will return the default value. If the List does not exist and no
     * default value was specified, this will return an empty List.
     * <p>
     * This method will attempt to cast any values into a Map if possible, but
     * may miss any values out if they are not compatible.
     *
     * @param path Path of the List to get.
     * @return Requested List of Maps.
     */
    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull String path);

    // Bukkit
    /**
     * Gets the requested object at the given path.
     *
     * If the Object does not exist but a default value has been specified, this
     * will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * <b>Note:</b> For example #getObject(path, String.class) is <b>not</b>
     * equivalent to {@link #getString(String) #getString(path)} because
     * {@link #getString(String) #getString(path)} converts internally all
     * Objects to Strings. However, #getObject(path, Boolean.class) is
     * equivalent to {@link #getBoolean(String) #getBoolean(path)} for example.
     *
     * @param <T> the type of the requested object
     * @param path the path to the object.
     * @param clazz the type of the requested object
     * @return Requested object
     */
    @Nullable
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz);

    /**
     * Gets the requested object at the given path, returning a default value if
     * not found
     *
     * If the Object does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * <b>Note:</b> For example #getObject(path, String.class, def) is
     * <b>not</b> equivalent to
     * {@link #getString(String, String) #getString(path, def)} because
     * {@link #getString(String, String) #getString(path, def)} converts
     * internally all Objects to Strings. However, #getObject(path,
     * Boolean.class, def) is equivalent to {@link #getBoolean(String, boolean) #getBoolean(path,
     * def)} for example.
     *
     * @param <T> the type of the requested object
     * @param path the path to the object.
     * @param clazz the type of the requested object
     * @param def the default object to return if the object is not present at
     * the path
     * @return Requested object
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given
     * path.
     *
     * If the Object does not exist but a default value has been specified, this
     * will return the default value. If the Object does not exist and no
     * default value was specified, this will return null.
     *
     * @param <T> the type of {@link ConfigurationSerializable}
     * @param path the path to the object.
     * @param clazz the type of {@link ConfigurationSerializable}
     * @return Requested {@link ConfigurationSerializable} object
     */
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz);

    /**
     * Gets the requested {@link ConfigurationSerializable} object at the given
     * path, returning a default value if not found
     *
     * If the Object does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param <T> the type of {@link ConfigurationSerializable}
     * @param path the path to the object.
     * @param clazz the type of {@link ConfigurationSerializable}
     * @param def the default object to return if the object is not present at
     * the path
     * @return Requested {@link ConfigurationSerializable} object
     */
    @Contract("_, _, !null -> !null")
    @Nullable
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def);

    /**
     * Gets the requested Vector by path.
     * <p>
     * If the Vector does not exist but a default value has been specified,
     * this will return the default value. If the Vector does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Vector to get.
     * @return Requested Vector.
     */
    @Nullable
    public Vector getVector(@NotNull String path);

    /**
     * Gets the requested {@link Vector} by path, returning a default value if
     * not found.
     * <p>
     * If the Vector does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the Vector to get.
     * @param def The default value to return if the path is not found or is
     *     not a Vector.
     * @return Requested Vector.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public Vector getVector(@NotNull String path, @Nullable Vector def);

    /**
     * Checks if the specified path is a Vector.
     * <p>
     * If the path exists but is not a Vector, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a Vector and return appropriately.
     *
     * @param path Path of the Vector to check.
     * @return Whether or not the specified path is a Vector.
     */
    public boolean isVector(@NotNull String path);

    /**
     * Gets the requested OfflinePlayer by path.
     * <p>
     * If the OfflinePlayer does not exist but a default value has been
     * specified, this will return the default value. If the OfflinePlayer
     * does not exist and no default value was specified, this will return
     * null.
     *
     * @param path Path of the OfflinePlayer to get.
     * @return Requested OfflinePlayer.
     */
    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path);

    /**
     * Gets the requested {@link OfflinePlayer} by path, returning a default
     * value if not found.
     * <p>
     * If the OfflinePlayer does not exist then the specified default value
     * will returned regardless of if a default has been identified in the
     * root {@link Configuration}.
     *
     * @param path Path of the OfflinePlayer to get.
     * @param def The default value to return if the path is not found or is
     *     not an OfflinePlayer.
     * @return Requested OfflinePlayer.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def);

    /**
     * Checks if the specified path is an OfflinePlayer.
     * <p>
     * If the path exists but is not a OfflinePlayer, this will return false.
     * If the path does not exist, this will return false. If the path does
     * not exist but a default value has been specified, this will check if
     * that default value is a OfflinePlayer and return appropriately.
     *
     * @param path Path of the OfflinePlayer to check.
     * @return Whether or not the specified path is an OfflinePlayer.
     */
    public boolean isOfflinePlayer(@NotNull String path);

    /**
     * Gets the requested ItemStack by path.
     * <p>
     * If the ItemStack does not exist but a default value has been specified,
     * this will return the default value. If the ItemStack does not exist and
     * no default value was specified, this will return null.
     *
     * @param path Path of the ItemStack to get.
     * @return Requested ItemStack.
     */
    @Nullable
    public ItemStack getItemStack(@NotNull String path);

    /**
     * Gets the requested {@link ItemStack} by path, returning a default value
     * if not found.
     * <p>
     * If the ItemStack does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the ItemStack to get.
     * @param def The default value to return if the path is not found or is
     *     not an ItemStack.
     * @return Requested ItemStack.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def);

    /**
     * Checks if the specified path is an ItemStack.
     * <p>
     * If the path exists but is not a ItemStack, this will return false. If
     * the path does not exist, this will return false. If the path does not
     * exist but a default value has been specified, this will check if that
     * default value is a ItemStack and return appropriately.
     *
     * @param path Path of the ItemStack to check.
     * @return Whether or not the specified path is an ItemStack.
     */
    public boolean isItemStack(@NotNull String path);

    /**
     * Gets the requested Color by path.
     * <p>
     * If the Color does not exist but a default value has been specified,
     * this will return the default value. If the Color does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Color to get.
     * @return Requested Color.
     */
    @Nullable
    public Color getColor(@NotNull String path);

    /**
     * Gets the requested {@link Color} by path, returning a default value if
     * not found.
     * <p>
     * If the Color does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the Color to get.
     * @param def The default value to return if the path is not found or is
     *     not a Color.
     * @return Requested Color.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public Color getColor(@NotNull String path, @Nullable Color def);

    /**
     * Checks if the specified path is a Color.
     * <p>
     * If the path exists but is not a Color, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a Color and return appropriately.
     *
     * @param path Path of the Color to check.
     * @return Whether or not the specified path is a Color.
     */
    public boolean isColor(@NotNull String path);

    /**
     * Gets the requested Location by path.
     * <p>
     * If the Location does not exist but a default value has been specified,
     * this will return the default value. If the Location does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Path of the Location to get.
     * @return Requested Location.
     */
    @Nullable
    public Location getLocation(@NotNull String path);

    /**
     * Gets the requested {@link Location} by path, returning a default value if
     * not found.
     * <p>
     * If the Location does not exist then the specified default value will
     * returned regardless of if a default has been identified in the root
     * {@link Configuration}.
     *
     * @param path Path of the Location to get.
     * @param def The default value to return if the path is not found or is not
     * a Location.
     * @return Requested Location.
     */
    @Contract("_, !null -> !null")
    @Nullable
    public Location getLocation(@NotNull String path, @Nullable Location def);

    /**
     * Checks if the specified path is a Location.
     * <p>
     * If the path exists but is not a Location, this will return false. If the
     * path does not exist, this will return false. If the path does not exist
     * but a default value has been specified, this will check if that default
     * value is a Location and return appropriately.
     *
     * @param path Path of the Location to check.
     * @return Whether or not the specified path is a Location.
     */
    public boolean isLocation(@NotNull String path);

    /**
     * Gets the requested ConfigurationSection by path.
     * <p>
     * If the ConfigurationSection does not exist but a default value has been
     * specified, this will return the default value. If the
     * ConfigurationSection does not exist and no default value was specified,
     * this will return null.
     *
     * @param path Path of the ConfigurationSection to get.
     * @return Requested ConfigurationSection.
     */
    @Nullable
    public ConfigurationSection getConfigurationSection(@NotNull String path);

    /**
     * Checks if the specified path is a ConfigurationSection.
     * <p>
     * If the path exists but is not a ConfigurationSection, this will return
     * false. If the path does not exist, this will return false. If the path
     * does not exist but a default value has been specified, this will check
     * if that default value is a ConfigurationSection and return
     * appropriately.
     *
     * @param path Path of the ConfigurationSection to check.
     * @return Whether or not the specified path is a ConfigurationSection.
     */
    public boolean isConfigurationSection(@NotNull String path);

    /**
     * Gets the equivalent {@link ConfigurationSection} from the default
     * {@link Configuration} defined in {@link #getRoot()}.
     * <p>
     * If the root contains no defaults, or the defaults doesn't contain a
     * value for this path, or the value at this path is not a {@link
     * ConfigurationSection} then this will return null.
     *
     * @return Equivalent section in root configuration
     */
    @Nullable
    public ConfigurationSection getDefaultSection();

    /**
     * Sets the default value in the root at the given path as provided.
     * <p>
     * If no source {@link Configuration} was provided as a default
     * collection, then a new {@link MemoryConfiguration} will be created to
     * hold the new default value.
     * <p>
     * If value is null, the value will be removed from the default
     * Configuration source.
     * <p>
     * If the value as returned by {@link #getDefaultSection()} is null, then
     * this will create a new section at the path, replacing anything that may
     * have existed there previously.
     *
     * @param path Path of the value to set.
     * @param value Value to set the default to.
     * @throws IllegalArgumentException Thrown if path is null.
     */
    public void addDefault(@NotNull String path, @Nullable Object value);

    /**
     * Gets the requested comment list by path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param path Path of the comments to get.
     * @return A unmodifiable list of the requested comments, every entry
     * represents one line.
     */
    @NotNull
    public List<String> getComments(@NotNull String path);

    /**
     * Gets the requested inline comment list by path.
     * <p>
     * If no comments exist, an empty list will be returned. A null entry
     * represents an empty line and an empty String represents an empty comment
     * line.
     *
     * @param path Path of the comments to get.
     * @return A unmodifiable list of the requested comments, every entry
     * represents one line.
     */
    @NotNull
    public List<String> getInlineComments(@NotNull String path);

    /**
     * Sets the comment list at the specified path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comments will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     *
     * @param path Path of the comments to set.
     * @param comments New comments to set at the path, every entry represents
     * one line.
     */
    public void setComments(@NotNull String path, @Nullable List<String> comments);

    /**
     * Sets the inline comment list at the specified path.
     * <p>
     * If value is null, the comments will be removed. A null entry is an empty
     * line and an empty String entry is an empty comment line. If the path does
     * not exist, no comment will be set. Any existing comments will be
     * replaced, regardless of what the new comments are.
     * <p>
     * Some implementations may have limitations on what persists. See their
     * individual javadocs for details.
     *
     * @param path Path of the comments to set.
     * @param comments New comments to set at the path, every entry represents
     * one line.
     */
    public void setInlineComments(@NotNull String path, @Nullable List<String> comments);
}
