package org.bukkit.entity;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.DyeColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Wolf
 */
public interface Wolf extends Tameable, Sittable, io.papermc.paper.entity.CollarColorable { // Paper - CollarColorable

    /**
     * Checks if this wolf is angry
     *
     * @return Anger true if angry
     */
    public boolean isAngry();

    /**
     * Sets the anger of this wolf.
     * <p>
     * An angry wolf can not be fed or tamed.
     *
     * @param angry true if angry
     * @see #setTarget(org.bukkit.entity.LivingEntity)
     */
    public void setAngry(boolean angry);

    /**
     * Get the collar color of this wolf
     *
     * @return the color of the collar
     */
    @NotNull
    @Override // Paper
    public DyeColor getCollarColor();

    /**
     * Set the collar color of this wolf
     *
     * @param color the color to apply
     */
    @Override // Paper
    public void setCollarColor(@NotNull DyeColor color);

    /**
     * Gets whether the wolf is wet
     *
     * @return Whether the wolf is wet
     */
    public boolean isWet();

    /**
     * Gets the wolf's tail angle in radians
     *
     * @return The angle of the wolf's tail in radians
     */
    public float getTailAngle();

    /**
     * Gets if the wolf is interested
     *
     * @return Whether the wolf is interested
     */
    public boolean isInterested();

    /**
     * Set wolf to be interested
     *
     * @param interested Whether the wolf is interested
     */
    public void setInterested(boolean interested);

    /**
     * Get the variant of this wolf.
     *
     * @return wolf variant
     */
    @NotNull
    Variant getVariant();

    /**
     * Set the variant of this wolf.
     *
     * @param variant wolf variant
     */
    void setVariant(@NotNull Variant variant);

    /**
     * Represents the variant of a wolf.
     */
    interface Variant extends Keyed {

        Variant PALE = getVariant("pale");
        Variant SPOTTED = getVariant("spotted");
        Variant SNOWY = getVariant("snowy");
        Variant BLACK = getVariant("black");
        Variant ASHEN = getVariant("ashen");
        Variant RUSTY = getVariant("rusty");
        Variant WOODS = getVariant("woods");
        Variant CHESTNUT = getVariant("chestnut");
        Variant STRIPED = getVariant("striped");

        @NotNull
        private static Variant getVariant(@NotNull String key) {
            return RegistryAccess.registryAccess().getRegistry(RegistryKey.WOLF_VARIANT).getOrThrow(NamespacedKey.minecraft(key));
        }
    }
}
