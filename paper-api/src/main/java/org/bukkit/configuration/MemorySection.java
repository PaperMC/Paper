package org.bukkit.configuration;

import static org.bukkit.util.NumberConversions.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
 * A type of {@link ConfigurationSection} that is stored in memory.
 */
public class MemorySection implements ConfigurationSection {
    protected final Map<String, SectionPathData> map = new LinkedHashMap<String, SectionPathData>();
    private final Configuration root;
    private final ConfigurationSection parent;
    private final String path;
    private final String fullPath;

    /**
     * Creates an empty MemorySection for use as a root {@link Configuration}
     * section.
     * <p>
     * Note that calling this without being yourself a {@link Configuration}
     * will throw an exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link
     *     Configuration} root.
     */
    protected MemorySection() {
        if (!(this instanceof Configuration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
        }

        this.path = "";
        this.fullPath = "";
        this.parent = null;
        this.root = (Configuration) this;
    }

    /**
     * Creates an empty MemorySection with the specified parent and path.
     *
     * @param parent Parent section that contains this own section.
     * @param path Path that you may access this section from via the root
     *     {@link Configuration}.
     * @throws IllegalArgumentException Thrown is parent or path is null, or
     *     if parent contains no root Configuration.
     */
    protected MemorySection(@NotNull ConfigurationSection parent, @NotNull String path) {
        Preconditions.checkArgument(parent != null, "Parent cannot be null");
        Preconditions.checkArgument(path != null, "Path cannot be null");

        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();

        Preconditions.checkArgument(root != null, "Path cannot be orphaned");

        this.fullPath = createPath(parent, path);
    }

    @Override
    @NotNull
    public Set<String> getKeys(boolean deep) {
        Set<String> result = new LinkedHashSet<String>();

        Configuration root = getRoot();
        if (root != null && root.options().copyDefaults()) {
            ConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.addAll(defaults.getKeys(deep));
            }
        }

        mapChildrenKeys(result, this, deep);

        return result;
    }

    @Override
    @NotNull
    public Map<String, Object> getValues(boolean deep) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        Configuration root = getRoot();
        if (root != null && root.options().copyDefaults()) {
            ConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.putAll(defaults.getValues(deep));
            }
        }

        mapChildrenValues(result, this, deep);

        return result;
    }

    @Override
    public boolean contains(@NotNull String path) {
        return contains(path, false);
    }

    @Override
    public boolean contains(@NotNull String path, boolean ignoreDefault) {
        return ((ignoreDefault) ? get(path, null) : get(path)) != null;
    }

    @Override
    public boolean isSet(@NotNull String path) {
        Configuration root = getRoot();
        if (root == null) {
            return false;
        }
        if (root.options().copyDefaults()) {
            return contains(path);
        }
        return get(path, null) != null;
    }

    @Override
    @NotNull
    public String getCurrentPath() {
        return fullPath;
    }

    @Override
    @NotNull
    public String getName() {
        return path;
    }

    @Override
    @Nullable
    public Configuration getRoot() {
        return root;
    }

    @Override
    @Nullable
    public ConfigurationSection getParent() {
        return parent;
    }

    @Override
    public void addDefault(@NotNull String path, @Nullable Object value) {
        Preconditions.checkArgument(path != null, "Path cannot be null");

        Configuration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot add default without root");
        }
        if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        }
        root.addDefault(createPath(this, path), value);
    }

    @Override
    @Nullable
    public ConfigurationSection getDefaultSection() {
        Configuration root = getRoot();
        Configuration defaults = root == null ? null : root.getDefaults();

        if (defaults != null) {
            if (defaults.isConfigurationSection(getCurrentPath())) {
                return defaults.getConfigurationSection(getCurrentPath());
            }
        }

        return null;
    }

    @Override
    public void set(@NotNull String path, @Nullable Object value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "Cannot set to an empty path");

        Configuration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                if (value == null) {
                    // no need to create missing sub-sections if we want to remove the value:
                    return;
                }
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            if (value == null) {
                map.remove(key);
            } else {
                SectionPathData entry = map.get(key);
                if (entry == null) {
                    map.put(key, new SectionPathData(value));
                } else {
                    entry.setData(value);
                }
            }
        } else {
            section.set(key, value);
        }
    }

    @Override
    @Nullable
    public Object get(@NotNull String path) {
        return get(path, getDefault(path));
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Object get(@NotNull String path, @Nullable Object def) {
        Preconditions.checkArgument(path != null, "Path cannot be null");

        if (path.length() == 0) {
            return this;
        }

        Configuration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            final String currentPath = path.substring(i2, i1);
            if (!section.contains(currentPath, true)) {
                return def;
            }
            section = section.getConfigurationSection(currentPath);
            if (section == null) {
                return def;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            SectionPathData result = map.get(key);
            return (result == null) ? def : result.getData();
        }
        return section.get(key, def);
    }

    @Override
    @NotNull
    public ConfigurationSection createSection(@NotNull String path) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(path), "Cannot create section at empty path");
        Configuration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
            } else {
                section = subSection;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            ConfigurationSection result = new MemorySection(this, key);
            map.put(key, new SectionPathData(result));
            return result;
        }
        return section.createSection(key);
    }

    @Override
    @NotNull
    public ConfigurationSection createSection(@NotNull String path, @NotNull Map<?, ?> map) {
        ConfigurationSection section = createSection(path);

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map<?, ?>) entry.getValue());
            } else {
                section.set(entry.getKey().toString(), entry.getValue());
            }
        }

        return section;
    }

    // Primitives
    @Override
    @Nullable
    public String getString(@NotNull String path) {
        Object def = getDefault(path);
        return getString(path, def != null ? def.toString() : null);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public String getString(@NotNull String path, @Nullable String def) {
        Object val = get(path, def);
        return (val != null) ? val.toString() : def;
    }

    @Override
    public boolean isString(@NotNull String path) {
        Object val = get(path);
        return val instanceof String;
    }

    @Override
    public int getInt(@NotNull String path) {
        Object def = getDefault(path);
        return getInt(path, (def instanceof Number) ? toInt(def) : 0);
    }

    @Override
    public int getInt(@NotNull String path, int def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toInt(val) : def;
    }

    @Override
    public boolean isInt(@NotNull String path) {
        Object val = get(path);
        return val instanceof Integer;
    }

    @Override
    public boolean getBoolean(@NotNull String path) {
        Object def = getDefault(path);
        return getBoolean(path, (def instanceof Boolean) ? (Boolean) def : false);
    }

    @Override
    public boolean getBoolean(@NotNull String path, boolean def) {
        Object val = get(path, def);
        return (val instanceof Boolean) ? (Boolean) val : def;
    }

    @Override
    public boolean isBoolean(@NotNull String path) {
        Object val = get(path);
        return val instanceof Boolean;
    }

    @Override
    public double getDouble(@NotNull String path) {
        Object def = getDefault(path);
        return getDouble(path, (def instanceof Number) ? toDouble(def) : 0);
    }

    @Override
    public double getDouble(@NotNull String path, double def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toDouble(val) : def;
    }

    @Override
    public boolean isDouble(@NotNull String path) {
        Object val = get(path);
        return val instanceof Double;
    }

    @Override
    public long getLong(@NotNull String path) {
        Object def = getDefault(path);
        return getLong(path, (def instanceof Number) ? toLong(def) : 0);
    }

    @Override
    public long getLong(@NotNull String path, long def) {
        Object val = get(path, def);
        return (val instanceof Number) ? toLong(val) : def;
    }

    @Override
    public boolean isLong(@NotNull String path) {
        Object val = get(path);
        return val instanceof Long;
    }

    // Java
    @Override
    @Nullable
    public List<?> getList(@NotNull String path) {
        Object def = getDefault(path);
        return getList(path, (def instanceof List) ? (List<?>) def : null);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public List<?> getList(@NotNull String path, @Nullable List<?> def) {
        Object val = get(path, def);
        return (List<?>) ((val instanceof List) ? val : def);
    }

    @Override
    public boolean isList(@NotNull String path) {
        Object val = get(path);
        return val instanceof List;
    }

    @Override
    @NotNull
    public List<String> getStringList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<String>(0);
        }

        List<String> result = new ArrayList<String>();

        for (Object object : list) {
            if ((object instanceof String) || (isPrimitiveWrapper(object))) {
                result.add(String.valueOf(object));
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Integer> getIntegerList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Integer>(0);
        }

        List<Integer> result = new ArrayList<Integer>();

        for (Object object : list) {
            if (object instanceof Integer) {
                result.add((Integer) object);
            } else if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((int) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).intValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Boolean> getBooleanList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Boolean>(0);
        }

        List<Boolean> result = new ArrayList<Boolean>();

        for (Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean) object);
            } else if (object instanceof String) {
                if (Boolean.TRUE.toString().equals(object)) {
                    result.add(true);
                } else if (Boolean.FALSE.toString().equals(object)) {
                    result.add(false);
                }
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Double> getDoubleList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Double>(0);
        }

        List<Double> result = new ArrayList<Double>();

        for (Object object : list) {
            if (object instanceof Double) {
                result.add((Double) object);
            } else if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((double) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).doubleValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Float> getFloatList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Float>(0);
        }

        List<Float> result = new ArrayList<Float>();

        for (Object object : list) {
            if (object instanceof Float) {
                result.add((Float) object);
            } else if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((float) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).floatValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Long> getLongList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Long>(0);
        }

        List<Long> result = new ArrayList<Long>();

        for (Object object : list) {
            if (object instanceof Long) {
                result.add((Long) object);
            } else if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((long) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).longValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Byte> getByteList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Byte>(0);
        }

        List<Byte> result = new ArrayList<Byte>();

        for (Object object : list) {
            if (object instanceof Byte) {
                result.add((Byte) object);
            } else if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((byte) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).byteValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Character> getCharacterList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Character>(0);
        }

        List<Character> result = new ArrayList<Character>();

        for (Object object : list) {
            if (object instanceof Character) {
                result.add((Character) object);
            } else if (object instanceof String) {
                String str = (String) object;

                if (str.length() == 1) {
                    result.add(str.charAt(0));
                }
            } else if (object instanceof Number) {
                result.add((char) ((Number) object).intValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Short> getShortList(@NotNull String path) {
        List<?> list = getList(path);

        if (list == null) {
            return new ArrayList<Short>(0);
        }

        List<Short> result = new ArrayList<Short>();

        for (Object object : list) {
            if (object instanceof Short) {
                result.add((Short) object);
            } else if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String) object));
                } catch (Exception ex) {
                }
            } else if (object instanceof Character) {
                result.add((short) ((Character) object).charValue());
            } else if (object instanceof Number) {
                result.add(((Number) object).shortValue());
            }
        }

        return result;
    }

    @Override
    @NotNull
    public List<Map<?, ?>> getMapList(@NotNull String path) {
        List<?> list = getList(path);
        List<Map<?, ?>> result = new ArrayList<Map<?, ?>>();

        if (list == null) {
            return result;
        }

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<?, ?>) object);
            }
        }

        return result;
    }

    // Bukkit
    @Nullable
    @Override
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz) {
        Preconditions.checkArgument(clazz != null, "Class cannot be null");
        Object def = getDefault(path);
        return getObject(path, clazz, (def != null && clazz.isInstance(def)) ? clazz.cast(def) : null);
    }

    @Contract("_, _, !null -> !null")
    @Nullable
    @Override
    public <T extends Object> T getObject(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        Preconditions.checkArgument(clazz != null, "Class cannot be null");
        Object val = get(path, def);
        return (val != null && clazz.isInstance(val)) ? clazz.cast(val) : def;
    }

    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz) {
        return getObject(path, clazz);
    }

    @Contract("_, _, !null -> !null")
    @Nullable
    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull String path, @NotNull Class<T> clazz, @Nullable T def) {
        return getObject(path, clazz, def);
    }

    @Override
    @Nullable
    public Vector getVector(@NotNull String path) {
        return getSerializable(path, Vector.class);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Vector getVector(@NotNull String path, @Nullable Vector def) {
        return getSerializable(path, Vector.class, def);
    }

    @Override
    public boolean isVector(@NotNull String path) {
        return getSerializable(path, Vector.class) != null;
    }

    @Override
    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path) {
        return getSerializable(path, OfflinePlayer.class);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public OfflinePlayer getOfflinePlayer(@NotNull String path, @Nullable OfflinePlayer def) {
        return getSerializable(path, OfflinePlayer.class, def);
    }

    @Override
    public boolean isOfflinePlayer(@NotNull String path) {
        return getSerializable(path, OfflinePlayer.class) != null;
    }

    @Override
    @Nullable
    public ItemStack getItemStack(@NotNull String path) {
        return getSerializable(path, ItemStack.class);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public ItemStack getItemStack(@NotNull String path, @Nullable ItemStack def) {
        return getSerializable(path, ItemStack.class, def);
    }

    @Override
    public boolean isItemStack(@NotNull String path) {
        return getSerializable(path, ItemStack.class) != null;
    }

    @Override
    @Nullable
    public Color getColor(@NotNull String path) {
        return getSerializable(path, Color.class);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Color getColor(@NotNull String path, @Nullable Color def) {
        return getSerializable(path, Color.class, def);
    }

    @Override
    public boolean isColor(@NotNull String path) {
        return getSerializable(path, Color.class) != null;
    }

    @Override
    @Nullable
    public Location getLocation(@NotNull String path) {
        return getSerializable(path, Location.class);
    }

    @Override
    @Contract("_, !null -> !null")
    @Nullable
    public Location getLocation(@NotNull String path, @Nullable Location def) {
        return getSerializable(path, Location.class, def);
    }

    @Override
    public boolean isLocation(@NotNull String path) {
        return getSerializable(path, Location.class) != null;
    }

    @Override
    @Nullable
    public ConfigurationSection getConfigurationSection(@NotNull String path) {
        Object val = get(path, null);
        if (val != null) {
            return (val instanceof ConfigurationSection) ? (ConfigurationSection) val : null;
        }

        val = get(path, getDefault(path));
        return (val instanceof ConfigurationSection) ? createSection(path) : null;
    }

    @Override
    public boolean isConfigurationSection(@NotNull String path) {
        Object val = get(path);
        return val instanceof ConfigurationSection;
    }

    protected boolean isPrimitiveWrapper(@Nullable Object input) {
        return input instanceof Integer || input instanceof Boolean
                || input instanceof Character || input instanceof Byte
                || input instanceof Short || input instanceof Double
                || input instanceof Long || input instanceof Float;
    }

    @Nullable
    protected Object getDefault(@NotNull String path) {
        Preconditions.checkArgument(path != null, "Path cannot be null");

        Configuration root = getRoot();
        Configuration defaults = root == null ? null : root.getDefaults();
        return (defaults == null) ? null : defaults.get(createPath(this, path));
    }

    protected void mapChildrenKeys(@NotNull Set<String> output, @NotNull ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection) section;

            for (Map.Entry<String, SectionPathData> entry : sec.map.entrySet()) {
                output.add(createPath(section, entry.getKey(), this));

                if ((deep) && (entry.getValue().getData() instanceof ConfigurationSection)) {
                    ConfigurationSection subsection = (ConfigurationSection) entry.getValue().getData();
                    mapChildrenKeys(output, subsection, deep);
                }
            }
        } else {
            Set<String> keys = section.getKeys(deep);

            for (String key : keys) {
                output.add(createPath(section, key, this));
            }
        }
    }

    protected void mapChildrenValues(@NotNull Map<String, Object> output, @NotNull ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection) section;

            for (Map.Entry<String, SectionPathData> entry : sec.map.entrySet()) {
                // Because of the copyDefaults call potentially copying out of order, we must remove and then add in our saved order
                // This means that default values we haven't set end up getting placed first
                // See SPIGOT-4558 for an example using spigot.yml - watch subsections move around to default order
                String childPath = createPath(section, entry.getKey(), this);
                output.remove(childPath);
                output.put(childPath, entry.getValue().getData());

                if (entry.getValue().getData() instanceof ConfigurationSection) {
                    if (deep) {
                        mapChildrenValues(output, (ConfigurationSection) entry.getValue().getData(), deep);
                    }
                }
            }
        } else {
            Map<String, Object> values = section.getValues(deep);

            for (Map.Entry<String, Object> entry : values.entrySet()) {
                output.put(createPath(section, entry.getKey(), this), entry.getValue());
            }
        }
    }

    /**
     * Creates a full path to the given {@link ConfigurationSection} from its
     * root {@link Configuration}.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @return Full path of the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull ConfigurationSection section, @Nullable String key) {
        return createPath(section, key, (section == null) ? null : section.getRoot());
    }

    /**
     * Creates a relative path to the given {@link ConfigurationSection} from
     * the given relative section.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not
     * only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @param relativeTo Section to create the path relative to.
     * @return Full path of the section from its root.
     */
    @NotNull
    public static String createPath(@NotNull ConfigurationSection section, @Nullable String key, @Nullable ConfigurationSection relativeTo) {
        Preconditions.checkArgument(section != null, "Cannot create path without a section");
        Configuration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        }
        char separator = root.options().pathSeparator();

        StringBuilder builder = new StringBuilder();
        for (ConfigurationSection parent = section; (parent != null) && (parent != relativeTo); parent = parent.getParent()) {
            if (builder.length() > 0) {
                builder.insert(0, separator);
            }
            builder.insert(0, parent.getName());
        }

        if ((key != null) && (key.length() > 0)) {
            if (builder.length() > 0) {
                builder.append(separator);
            }

            builder.append(key);
        }

        return builder.toString();
    }

    @Override
    @NotNull
    public List<String> getComments(@NotNull final String path) {
        final SectionPathData pathData = getSectionPathData(path);
        return pathData == null ? Collections.emptyList() : pathData.getComments();
    }

    @Override
    @NotNull
    public List<String> getInlineComments(@NotNull final String path) {
        final SectionPathData pathData = getSectionPathData(path);
        return pathData == null ? Collections.emptyList() : pathData.getInlineComments();
    }

    @Override
    public void setComments(@NotNull final String path, @Nullable final List<String> comments) {
        final SectionPathData pathData = getSectionPathData(path);
        if (pathData != null) {
            pathData.setComments(comments);
        }
    }

    @Override
    public void setInlineComments(@NotNull final String path, @Nullable final List<String> comments) {
        final SectionPathData pathData = getSectionPathData(path);
        if (pathData != null) {
            pathData.setInlineComments(comments);
        }
    }

    @Nullable
    private SectionPathData getSectionPathData(@NotNull String path) {
        Preconditions.checkArgument(path != null, "Path cannot be null");

        Configuration root = getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot access section without a root");
        }

        final char separator = root.options().pathSeparator();
        // i1 is the leading (higher) index
        // i2 is the trailing (lower) index
        int i1 = -1, i2;
        ConfigurationSection section = this;
        while ((i1 = path.indexOf(separator, i2 = i1 + 1)) != -1) {
            section = section.getConfigurationSection(path.substring(i2, i1));
            if (section == null) {
                return null;
            }
        }

        String key = path.substring(i2);
        if (section == this) {
            SectionPathData entry = map.get(key);
            if (entry != null) {
                return entry;
            }
        } else if (section instanceof MemorySection) {
            return ((MemorySection) section).getSectionPathData(key);
        }
        return null;
    }

    @Override
    public String toString() {
        Configuration root = getRoot();
        return new StringBuilder()
            .append(getClass().getSimpleName())
            .append("[path='")
            .append(getCurrentPath())
            .append("', root='")
            .append(root == null ? null : root.getClass().getSimpleName())
            .append("']")
            .toString();
    }
}
