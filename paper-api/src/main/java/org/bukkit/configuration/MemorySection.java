package org.bukkit.configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import static org.bukkit.util.NumberConversions.*;

/**
 * A type of {@link ConfigurationSection} that is stored in memory.
 */
public class MemorySection implements ConfigurationSection {
    protected final Map<String, Object> map = new LinkedHashMap<String, Object>();
    private final Configuration root;
    private final ConfigurationSection parent;
    private final String path;
    private final String fullPath;

    /**
     * Creates an empty MemorySection for use as a root {@link Configuration} section.
     * <p>
     * Note that calling this without being yourself a {@link Configuration} will throw an
     * exception!
     *
     * @throws IllegalStateException Thrown if this is not a {@link Configuration} root.
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
     * @param path Path that you may access this section from via the root {@link Configuration}.
     * @throws IllegalArgumentException Thrown is parent or path is null, or if parent contains no root Configuration.
     */
    protected MemorySection(ConfigurationSection parent, String path) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent cannot be null");
        }
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();

        if (root == null) {
            throw new IllegalArgumentException("Path cannot be orphaned");
        }

        this.fullPath = createPath(parent, path);
    }

    public Set<String> getKeys(boolean deep) {
        Set<String> result = new LinkedHashSet<String>();

        if (getRoot().options().copyDefaults()) {
            ConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.addAll(defaults.getKeys(deep));
            }
        }

        mapChildrenKeys(result, this, deep);

        return result;
    }

    public Map<String, Object> getValues(boolean deep) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        if (getRoot().options().copyDefaults()) {
            ConfigurationSection defaults = getDefaultSection();

            if (defaults != null) {
                result.putAll(defaults.getValues(deep));
            }
        }

        mapChildrenValues(result, this, deep);

        return result;
    }

    public boolean contains(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        return get(path) != null;
    }

    public boolean isSet(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        if (getRoot().options().copyDefaults()) {
            return contains(path);
        } else {
            return get(path, null) != null;
        }
    }

    public String getCurrentPath() {
        return fullPath;
    }

    public String getName() {
        return path;
    }

    public Configuration getRoot() {
        return root;
    }

    public ConfigurationSection getParent() {
        return parent;
    }

    public void addDefault(String path, Object value) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        if (root == null) {
            throw new IllegalStateException("Cannot set default on orphaned section");
        } else {
            root.addDefault(createPath(this, path), value);
        }
    }

    public ConfigurationSection getDefaultSection() {
        if (getRoot() == null) {
            return null;
        }

        Configuration defaults = getRoot().getDefaults();

        if (defaults != null) {
            if (defaults.isConfigurationSection(getCurrentPath())) {
                return defaults.getConfigurationSection(getCurrentPath());
            }
        }

        return null;
    }

    public void set(String path, Object value) {
        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        ConfigurationSection section = this;

        if (path.length() == 0) {
            throw new IllegalArgumentException("Cannot set to an empty path");
        }

        for (int i = 0; i < split.length - 1; i++) {
            ConfigurationSection last = section;

            section = last.getConfigurationSection(split[i]);

            if (section == null) {
                section = last.createSection(split[i]);
            }
        }

        String key = split[split.length - 1];

        if (section == this) {
            if (value == null) {
                map.remove(key);
            } else {
                map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    public Object get(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        return get(path, getDefault(path));
    }

    public Object get(String path, Object def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        } else if (path.length() == 0) {
            return this;
        }

        Object result = null;
        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        ConfigurationSection section = this;

        for (int i = 0; i < split.length - 1; i++) {
            section = section.getConfigurationSection(split[i]);

            if (section == null) {
                return def;
            }
        }

        String key = split[split.length - 1];

        if (section == this) {
            result = map.get(key);
            return (result == null) ? def : result;
        }
        return section.get(key, def);
    }

    public ConfigurationSection createSection(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        } else if (path.length() == 0) {
            throw new IllegalArgumentException("Cannot create section at empty path");
        }

        String[] split = path.split(Pattern.quote(Character.toString(getRoot().options().pathSeparator())));
        ConfigurationSection section = this;

        for (int i = 0; i < split.length - 1; i++) {
            ConfigurationSection last = section;

            section = getConfigurationSection(split[i]);

            if (section == null) {
                section = last.createSection(split[i]);
            }
        }

        String key = split[split.length - 1];

        if (section == this) {
            ConfigurationSection result = new MemorySection(this, key);
            map.put(key, result);
            return result;
        } else {
            return section.createSection(key);
        }
    }

    @SuppressWarnings("unchecked")
    public ConfigurationSection createSection(String path, Map<String, Object> map) {
        ConfigurationSection section = createSection(path);

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey(), (Map<String, Object>) entry.getValue());
            } else {
                section.set(entry.getKey(), entry.getValue());
            }
        }

        return section;
    }

    // Primitives
    public String getString(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getString(path, def != null ? def.toString() : null);
    }

    public String getString(String path, String def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val != null) ? val.toString() : def;
    }

    public boolean isString(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof String;
    }

    public int getInt(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getInt(path, (def instanceof Number) ? toInt(def) : 0);
    }

    public int getInt(String path, int def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof Number) ? toInt(val) : def;
    }

    public boolean isInt(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof Integer;
    }

    public boolean getBoolean(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getBoolean(path, (def instanceof Boolean) ? (Boolean) def : false);
    }

    public boolean getBoolean(String path, boolean def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof Boolean) ? (Boolean) val : def;
    }

    public boolean isBoolean(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof Boolean;
    }

    public double getDouble(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getDouble(path, (def instanceof Number) ? toDouble(def) : 0);
    }

    public double getDouble(String path, double def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof Number) ? toDouble(val) : def;
    }

    public boolean isDouble(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof Double;
    }

    public long getLong(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getLong(path, (def instanceof Number) ? toLong(def) : 0);
    }

    public long getLong(String path, long def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof Number) ? toLong(val) : def;
    }

    public boolean isLong(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof Long;
    }

    // Java
    @SuppressWarnings("unchecked")
    public List<Object> getList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getList(path, (def instanceof List) ? (List<Object>) def : null);
    }

    @SuppressWarnings("unchecked")
    public List<Object> getList(String path, List<?> def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (List<Object>) ((val instanceof List) ? val : def);
    }

    public boolean isList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof List;
    }

    public List<String> getStringList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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

    public List<Integer> getIntegerList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Integer) (int) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Integer) (int) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Integer) (int) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Integer) (int) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Integer) (int) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Integer) (int) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Integer) (int) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Boolean> getBooleanList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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

    public List<Double> getDoubleList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Double) (double) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Double) (double) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Double) (double) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Double) (double) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Double) (double) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Double) (double) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Double) (double) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Float> getFloatList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Float) (float) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Float) (float) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Float) (float) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Float) (float) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Float) (float) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Float) (float) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Float) (float) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Long> getLongList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Long) (long) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Long) (long) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Long) (long) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Long) (long) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Long) (long) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Long) (long) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Long) (long) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Byte> getByteList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Byte) (byte) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Byte) (byte) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Byte) (byte) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Byte) (byte) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Byte) (byte) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Byte) (byte) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Byte) (byte) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Character> getCharacterList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Character) (char) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Character) (char) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Character) (char) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Character) (char) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Character) (char) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Character) (char) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Character) (char) (double) (Double) object);
            }
        }

        return result;
    }

    public List<Short> getShortList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);

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
            } else if (object instanceof Byte) {
                result.add((Short) (short) (byte) (Byte) object);
            } else if (object instanceof Character) {
                result.add((Short) (short) (char) (Character) object);
            } else if (object instanceof Short) {
                result.add((Short) (short) (short) (Short) object);
            } else if (object instanceof Integer) {
                result.add((Short) (short) (int) (Integer) object);
            } else if (object instanceof Long) {
                result.add((Short) (short) (long) (Long) object);
            } else if (object instanceof Float) {
                result.add((Short) (short) (float) (Float) object);
            } else if (object instanceof Double) {
                result.add((Short) (short) (double) (Double) object);
            }
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getMapList(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        List<Object> list = getList(path);
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        for (Object object : list) {
            if (object instanceof Map) {
                result.add((Map<String, Object>) object);
            }
        }

        return result;
    }

    // Bukkit
    public Vector getVector(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getVector(path, (def instanceof Vector) ? (Vector) def : null);
    }

    public Vector getVector(String path, Vector def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof Vector) ? (Vector) val : def;
    }

    public boolean isVector(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof Vector;
    }

    public OfflinePlayer getOfflinePlayer(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getOfflinePlayer(path, (def instanceof OfflinePlayer) ? (OfflinePlayer) def : null);
    }

    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof OfflinePlayer) ? (OfflinePlayer) val : def;
    }

    public boolean isOfflinePlayer(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof OfflinePlayer;
    }

    public ItemStack getItemStack(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object def = getDefault(path);
        return getItemStack(path, (def instanceof ItemStack) ? (ItemStack) def : null);
    }

    public ItemStack getItemStack(String path, ItemStack def) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, def);
        return (val instanceof ItemStack) ? (ItemStack) val : def;
    }

    public boolean isItemStack(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof ItemStack;
    }

    public ConfigurationSection getConfigurationSection(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path, null);
        if (val != null) {
            return (val instanceof ConfigurationSection) ? (ConfigurationSection) val : null;
        }

        val = get(path, getDefault(path));
        return (val instanceof ConfigurationSection) ? createSection(path) : null;
    }

    public boolean isConfigurationSection(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Object val = get(path);
        return val instanceof ConfigurationSection;
    }

    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean ||
                input instanceof Character || input instanceof Byte ||
                input instanceof Short || input instanceof Double ||
                input instanceof Long || input instanceof Float;
    }

    protected Object getDefault(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        Configuration defaults = root.getDefaults();
        return (defaults == null) ? null : defaults.get(createPath(this, path));
    }

    protected void mapChildrenKeys(Set<String> output, ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection) section;

            for (Map.Entry<String, Object> entry : sec.map.entrySet()) {
                output.add(createPath(section, entry.getKey(), this));

                if ((deep) && (entry.getValue() instanceof ConfigurationSection)) {
                    ConfigurationSection subsection = (ConfigurationSection) entry.getValue();
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

    protected void mapChildrenValues(Map<String, Object> output, ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection) section;

            for (Map.Entry<String, Object> entry : sec.map.entrySet()) {
                output.put(createPath(section, entry.getKey(), this), entry.getValue());

                if (entry.getValue() instanceof ConfigurationSection) {
                    if (deep) {
                        mapChildrenValues(output, (ConfigurationSection) entry.getValue(), deep);
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
     * Creates a full path to the given {@link ConfigurationSection} from its root {@link Configuration}.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @return Full path of the section from its root.
     */
    public static String createPath(ConfigurationSection section, String key) {
        return createPath(section, key, (section == null) ? null : section.getRoot());
    }

    /**
     * Creates a relative path to the given {@link ConfigurationSection} from the given relative section.
     * <p>
     * You may use this method for any given {@link ConfigurationSection}, not only {@link MemorySection}.
     *
     * @param section Section to create a path for.
     * @param key Name of the specified section.
     * @param relativeTo Section to create the path relative to.
     * @return Full path of the section from its root.
     */
    public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo) {
        StringBuilder builder = new StringBuilder();

        if (section != null) {
            for (ConfigurationSection parent = section; (parent != null) && (parent != relativeTo); parent = parent.getParent()) {
                if (builder.length() > 0) {
                    builder.insert(0, section.getRoot().options().pathSeparator());
                }

                builder.insert(0, parent.getName());
            }
        }

        if ((key != null) && (key.length() > 0)) {
            if (builder.length() > 0) {
                builder.append(section.getRoot().options().pathSeparator());
            }

            builder.append(key);
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getSimpleName());
        builder.append("[path='");
        builder.append(getCurrentPath());
        builder.append("', root='");
        builder.append(root.getClass().getSimpleName());
        builder.append("']");

        return builder.toString();
    }
}
