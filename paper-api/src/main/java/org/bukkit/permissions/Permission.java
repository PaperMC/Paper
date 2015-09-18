package org.bukkit.permissions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

/**
 * Represents a unique permission that may be attached to a {@link
 * Permissible}
 */
public class Permission {
    public static final PermissionDefault DEFAULT_PERMISSION = PermissionDefault.OP;

    private final String name;
    private final Map<String, Boolean> children = new LinkedHashMap<String, Boolean>();
    private PermissionDefault defaultValue = DEFAULT_PERMISSION;
    private String description;

    public Permission(String name) {
        this(name, null, null, null);
    }

    public Permission(String name, String description) {
        this(name, description, null, null);
    }

    public Permission(String name, PermissionDefault defaultValue) {
        this(name, null, defaultValue, null);
    }

    public Permission(String name, String description, PermissionDefault defaultValue) {
        this(name, description, defaultValue, null);
    }

    public Permission(String name, Map<String, Boolean> children) {
        this(name, null, null, children);
    }

    public Permission(String name, String description, Map<String, Boolean> children) {
        this(name, description, null, children);
    }

    public Permission(String name, PermissionDefault defaultValue, Map<String, Boolean> children) {
        this(name, null, defaultValue, children);
    }

    public Permission(String name, String description, PermissionDefault defaultValue, Map<String, Boolean> children) {
        Validate.notNull(name, "Name cannot be null");
        this.name = name;
        this.description = (description == null) ? "" : description;

        if (defaultValue != null) {
            this.defaultValue = defaultValue;
        }

        if (children != null) {
            this.children.putAll(children);
        }

        recalculatePermissibles();
    }

    /**
     * Returns the unique fully qualified name of this Permission
     *
     * @return Fully qualified name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the children of this permission.
     * <p>
     * If you change this map in any form, you must call {@link
     * #recalculatePermissibles()} to recalculate all {@link Permissible}s
     *
     * @return Permission children
     */
    public Map<String, Boolean> getChildren() {
        return children;
    }

    /**
     * Gets the default value of this permission.
     *
     * @return Default value of this permission.
     */
    public PermissionDefault getDefault() {
        return defaultValue;
    }

    /**
     * Sets the default value of this permission.
     * <p>
     * This will not be saved to disk, and is a temporary operation until the
     * server reloads permissions. Changing this default will cause all {@link
     * Permissible}s that contain this permission to recalculate their
     * permissions
     *
     * @param value The new default to set
     */
    public void setDefault(PermissionDefault value) {
        if (defaultValue == null) {
            throw new IllegalArgumentException("Default value cannot be null");
        }

        defaultValue = value;
        recalculatePermissibles();
    }

    /**
     * Gets a brief description of this permission, if set
     *
     * @return Brief description of this permission
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of this permission.
     * <p>
     * This will not be saved to disk, and is a temporary operation until the
     * server reloads permissions.
     *
     * @param value The new description to set
     */
    public void setDescription(String value) {
        if (value == null) {
            description = "";
        } else {
            description = value;
        }
    }

    /**
     * Gets a set containing every {@link Permissible} that has this
     * permission.
     * <p>
     * This set cannot be modified.
     *
     * @return Set containing permissibles with this permission
     */
    public Set<Permissible> getPermissibles() {
        return Bukkit.getServer().getPluginManager().getPermissionSubscriptions(name);
    }

    /**
     * Recalculates all {@link Permissible}s that contain this permission.
     * <p>
     * This should be called after modifying the children, and is
     * automatically called after modifying the default value
     */
    public void recalculatePermissibles() {
        Set<Permissible> perms = getPermissibles();

        Bukkit.getServer().getPluginManager().recalculatePermissionDefaults(this);

        for (Permissible p : perms) {
            p.recalculatePermissions();
        }
    }

    /**
     * Adds this permission to the specified parent permission.
     * <p>
     * If the parent permission does not exist, it will be created and
     * registered.
     *
     * @param name Name of the parent permission
     * @param value The value to set this permission to
     * @return Parent permission it created or loaded
     */
    public Permission addParent(String name, boolean value) {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        String lname = name.toLowerCase();

        Permission perm = pm.getPermission(lname);

        if (perm == null) {
            perm = new Permission(lname);
            pm.addPermission(perm);
        }

        addParent(perm, value);

        return perm;
    }

    /**
     * Adds this permission to the specified parent permission.
     *
     * @param perm Parent permission to register with
     * @param value The value to set this permission to
     */
    public void addParent(Permission perm, boolean value) {
        perm.getChildren().put(getName(), value);
        perm.recalculatePermissibles();
    }

    /**
     * Loads a list of Permissions from a map of data, usually used from
     * retrieval from a yaml file.
     * <p>
     * The data may contain a list of name:data, where the data contains the
     * following keys:
     * <ul>
     * <li>default: Boolean true or false. If not specified, false.
     * <li>children: {@code Map<String, Boolean>} of child permissions. If not
     *     specified, empty list.
     * <li>description: Short string containing a very small description of
     *     this description. If not specified, empty string.
     * </ul>
     *
     * @param data Map of permissions
     * @param error An error message to show if a permission is invalid.
     * @param def Default permission value to use if missing
     * @return Permission object
     */
    public static List<Permission> loadPermissions(Map<?, ?> data, String error, PermissionDefault def) {
        List<Permission> result = new ArrayList<Permission>();

        for (Map.Entry<?, ?> entry : data.entrySet()) {
            try {
                result.add(Permission.loadPermission(entry.getKey().toString(), (Map<?, ?>) entry.getValue(), def, result));
            } catch (Throwable ex) {
                Bukkit.getServer().getLogger().log(Level.SEVERE, String.format(error, entry.getKey()), ex);
            }
        }

        return result;
    }

    /**
     * Loads a Permission from a map of data, usually used from retrieval from
     * a yaml file.
     * <p>
     * The data may contain the following keys:
     * <ul>
     * <li>default: Boolean true or false. If not specified, false.
     * <li>children: {@code Map<String, Boolean>} of child permissions. If not
     *     specified, empty list.
     * <li>description: Short string containing a very small description of
     *     this description. If not specified, empty string.
     * </ul>
     *
     * @param name Name of the permission
     * @param data Map of keys
     * @return Permission object
     */
    public static Permission loadPermission(String name, Map<String, Object> data) {
        return loadPermission(name, data, DEFAULT_PERMISSION, null);
    }

    /**
     * Loads a Permission from a map of data, usually used from retrieval from
     * a yaml file.
     * <p>
     * The data may contain the following keys:
     * <ul>
     * <li>default: Boolean true or false. If not specified, false.
     * <li>children: {@code Map<String, Boolean>} of child permissions. If not
     *     specified, empty list.
     * <li>description: Short string containing a very small description of
     *     this description. If not specified, empty string.
     * </ul>
     *
     * @param name Name of the permission
     * @param data Map of keys
     * @param def Default permission value to use if not set
     * @param output A list to append any created child-Permissions to, may be null
     * @return Permission object
     */
    public static Permission loadPermission(String name, Map<?, ?> data, PermissionDefault def, List<Permission> output) {
        Validate.notNull(name, "Name cannot be null");
        Validate.notNull(data, "Data cannot be null");

        String desc = null;
        Map<String, Boolean> children = null;

        if (data.get("default") != null) {
            PermissionDefault value = PermissionDefault.getByName(data.get("default").toString());
            if (value != null) {
                def = value;
            } else {
                throw new IllegalArgumentException("'default' key contained unknown value");
            }
        }

        if (data.get("children") != null) {
            Object childrenNode = data.get("children");
            if (childrenNode instanceof Iterable) {
                children = new LinkedHashMap<String, Boolean>();
                for (Object child : (Iterable<?>) childrenNode) {
                    if (child != null) {
                        children.put(child.toString(), Boolean.TRUE);
                    }
                }
            } else if (childrenNode instanceof Map) {
                children = extractChildren((Map<?,?>) childrenNode, name, def, output);
            } else {
                throw new IllegalArgumentException("'children' key is of wrong type");
            }
        }

        if (data.get("description") != null) {
            desc = data.get("description").toString();
        }

        return new Permission(name, desc, def, children);
    }

    private static Map<String, Boolean> extractChildren(Map<?, ?> input, String name, PermissionDefault def, List<Permission> output) {
        Map<String, Boolean> children = new LinkedHashMap<String, Boolean>();

        for (Map.Entry<?, ?> entry : input.entrySet()) {
            if ((entry.getValue() instanceof Boolean)) {
                children.put(entry.getKey().toString(), (Boolean) entry.getValue());
            } else if ((entry.getValue() instanceof Map)) {
                try {
                    Permission perm = loadPermission(entry.getKey().toString(), (Map<?, ?>) entry.getValue(), def, output);
                    children.put(perm.getName(), Boolean.TRUE);

                    if (output != null) {
                        output.add(perm);
                    }
                } catch (Throwable ex) {
                    throw new IllegalArgumentException("Permission node '" + entry.getKey().toString() + "' in child of " + name + " is invalid", ex);
                }
            } else {
                throw new IllegalArgumentException("Child '" + entry.getKey().toString() + "' contains invalid value");
            }
        }

        return children;
    }
}
