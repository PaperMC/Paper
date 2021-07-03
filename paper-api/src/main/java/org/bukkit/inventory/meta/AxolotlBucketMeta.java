package org.bukkit.inventory.meta;

import org.bukkit.entity.Axolotl;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a bucket of axolotl.
 */
public interface AxolotlBucketMeta extends ItemMeta {

    /**
     * Get the variant of the axolotl in the bucket.
     * <p>
     * Plugins should check that hasVariant() returns <code>true</code> before
     * calling this method.
     * @return axolotl variant
     */
    @NotNull
    Axolotl.Variant getVariant();

    /**
     * Set the variant of this axolotl in the bucket.
     *
     * @param variant axolotl variant
     */
    void setVariant(@NotNull Axolotl.Variant variant);

    /**
     * Checks for existence of a variant tag indicating a specific axolotl will be
     * spawned.
     *
     * @return if there is a variant
     */
    boolean hasVariant();

    @Override
    @NotNull
    AxolotlBucketMeta clone();
}
