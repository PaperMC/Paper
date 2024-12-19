package org.bukkit.material;

/**
 * Indicated a Material that may carry or create a Redstone current
 *
 * @since 1.0.0 R1
 */
@Deprecated(forRemoval = true, since = "1.13")
public interface Redstone {

    /**
     * Gets the current state of this Material, indicating if it's powered or
     * unpowered
     *
     * @return true if powered, otherwise false
     */
    public boolean isPowered();
}
