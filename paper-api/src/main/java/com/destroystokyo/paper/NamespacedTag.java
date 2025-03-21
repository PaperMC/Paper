package com.destroystokyo.paper;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/* specific key
     * @deprecated should never be used by plugins, for internal use only!!
     */
    @Deprecated
    public NamespacedTag(@NotNull String namespace, @NotNull String key) {
        Preconditions.checkArgument(namespace != null && VALID_NAMESPACE.matcher(namespace).matches(), "Invalid namespace. Must be [a-z0-9._-]: %s", namespace);
        Preconditions.checkArgument(key != null && VALID_KEY.matcher(key).matches(), "Invalid key. Must be [a-z0-9/._-]: %s", key);

        this.namespace = namespace;
        this.key = key;

        String string = toString();
        Preconditions.checkArgument(string.length() < 256, "NamespacedTag must be less than 256 characters", string);
    }

    /**
     * Create a key in the plugin's namespace.
     * <pPreconditions.checkArgument(string.length() < 256, "NamespacedTag must be less than 256 characters (%s)", string);
    }

    @NotNull
    public String getNamespace() {
        return namespace;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.namespace.hashCode();
        hash = 47 * hash + this.key.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamespacedTag other = (NamespacedTag) obj;
        return this.namespace.equals(other.namespace) && this.key.equals(other.key);
    }

    @Override
    public String toString() {
        return "#" + this.namespace + ":" + this.key;
    }

    /**
     * Return a new random key in the {@link #BUKKIT} namespace.
     *
     * @return new key
     * @deprecated should never be used by plugins, for internal use only!!
     */
    @Deprecated
    @ApiStatus.Internal
    public static NamespacedTag randomKey() {
        return new NamespacedTag(BUKKIT, UUID.randomUUID().toString());
    }

    /**
     * Get a key in the Minecraft namespace.
     *
     * @param key the key to use
     * @return new key in the Minecraft namespace
     */
    @NotNull
    public static NamespacedTag minecraft(@NotNull String key) {
        return new NamespacedTag(MINECRAFT, key);
    }
}
