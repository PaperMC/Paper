package org.bukkit.permissions;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the possible default values for permissions
 */
public enum PermissionDefault {
    TRUE("true"),
    FALSE("false"),
    OP("op", "isop", "operator", "isoperator", "admin", "isadmin"),
    NOT_OP("!op", "notop", "!operator", "notoperator", "!admin", "notadmin");

    private final String[] names;
    private static final Map<String, PermissionDefault> lookup = new HashMap<String, PermissionDefault>();

    private PermissionDefault(/*@NotNull*/ String... names) {
        this.names = names;
    }

    /**
     * Calculates the value of this PermissionDefault for the given operator
     * value
     *
     * @param op If the target is op
     * @return True if the default should be true, or false
     */
    public boolean getValue(boolean op) {
        switch (this) {
        case TRUE:
            return true;
        case FALSE:
            return false;
        case OP:
            return op;
        case NOT_OP:
            return !op;
        default:
            return false;
        }
    }

    /**
     * Looks up a PermissionDefault by name
     *
     * @param name Name of the default
     * @return Specified value, or null if not found
     */
    @Nullable
    public static PermissionDefault getByName(@NotNull String name) {
        return lookup.get(name.toLowerCase(java.util.Locale.ENGLISH).replaceAll("[^a-z!]", ""));
    }

    @Override
    public String toString() {
        return names[0];
    }

    static {
        for (PermissionDefault value : values()) {
            for (String name : value.names) {
                lookup.put(name, value);
            }
        }
    }
}
