
package org.bukkit.permissions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Represents a unique permission that may be attached to a {@link Permissible}
 */
public class Permission {
    private final String name;
    private final Map<String, Boolean> children = new LinkedHashMap<String, Boolean>();
    private PermissionDefault defaultValue = PermissionDefault.FALSE;
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
        this.name = name;
        this.description = (description == null) ? "" : description;
        this.defaultValue = (defaultValue == null) ? defaultValue.FALSE : defaultValue;
        
        if (children != null) {
            this.children.putAll(children);
        }
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
     *
     * This is a copy and changes will not be saved.
     *
     * @return Permission children
     */
    public Map<String, Boolean> getChildren() {
        return new LinkedHashMap<String, Boolean>(children);
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
     * Gets a brief description of this permission, if set
     *
     * @return Brief description of this permission
     */
    public String getDescription() {
        return description;
    }

    /**
     * Loads a Permission from a map of data, usually used from retrieval from a yaml file.
     *
     * The data may contain the following keys:
     * default: Boolean true or false. If not specified, false.
     * children: Map<String, Boolean> of child permissions. If not specified, empty list.
     * description: Short string containing a very small description of this description. If not specified, empty string.
     *
     * @param name Name of the permission
     * @param data Map of keys
     * @return Permission object
     */
    public static Permission loadPermission(String name, Map<String, Object> data) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        String desc = null;
        PermissionDefault def = null;
        Map<String, Boolean> children = null;

        if (data.containsKey("default")) {
            try {
                PermissionDefault value = PermissionDefault.getByName(data.get("default").toString());
                if (value != null) {
                    def = value;
                } else {
                    throw new IllegalArgumentException("'default' key contained unknown value");
                }
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("'default' key is of wrong type", ex);
            }
        }

        if (data.containsKey("children")) {
            try {
                children = extractChildren(data);
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("'children' key is of wrong type", ex);
            }
        }

        if (data.containsKey("description")) {
            try {
                desc = (String)data.get("description");
            } catch (ClassCastException ex) {
                throw new IllegalArgumentException("'description' key is of wrong type", ex);
            }
        }

        return new Permission(name, desc, def, children);
    }

    private static Map<String, Boolean> extractChildren(Map<String, Object> data) {
        Map<String, Boolean> input = (Map<String, Boolean>)data.get("children");
        Set<Entry<String, Boolean>> entries = input.entrySet();

        for (Map.Entry<String, Boolean> entry : entries) {
            if (!(entry.getValue() instanceof Boolean)) {
                throw new IllegalArgumentException("Child '" + entry.getKey() + "' contains invalid value");
            }
        }

        return input;
    }
}
