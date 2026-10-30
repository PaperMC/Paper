package org.bukkit.inventory.meta;

import org.bukkit.entity.Axolotl;
import org.jspecify.annotations.NullMarked;

/**
 * Represents a bucket of axolotl.
 */
@NullMarked
public interface AxolotlBucketMeta extends ItemMeta {

    /**
     * Get the variant of the axolotl in the bucket.
     * <p>
     * Plugins should check that hasVariant() returns {@code true} before
     * calling this method.
     *
     * @return axolotl variant
     * @throws IllegalStateException if hasVariant() returns {@code false}
     */
    Axolotl.Variant getVariant();

    /**
     * Set the variant of this axolotl in the bucket.
     *
     * @param variant axolotl variant
     */
    void setVariant(Axolotl.Variant variant);

    /**
     * Checks for the existence of a variant indicating a specific axolotl will be
     * spawned.
     *
     * @return if there is a variant
     */
    boolean hasVariant();

    @Override
    AxolotlBucketMeta clone();
}
