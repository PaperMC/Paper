package org.bukkit.entity;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Locale;
import org.bukkit.DyeColor;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;

/**
 * Meow.
 *
 * @since 1.14
 */
public interface Cat extends Tameable, Sittable, io.papermc.paper.entity.CollarColorable { // Paper - CollarColorable

    /**
     * Gets the current type of this cat.
     *
     * @return Type of the cat.
     */
    @NotNull
    public Type getCatType();

    /**
     * Sets the current type of this cat.
     *
     * @param type New type of this cat.
     */
    public void setCatType(@NotNull Type type);

    /**
     * Get the collar color of this cat
     *
     * @return the color of the collar
     */
    @NotNull
    @Override // Paper
    public DyeColor getCollarColor();

    /**
     * Set the collar color of this cat
     *
     * @param color the color to apply
     */
    @Override // Paper
    public void setCollarColor(@NotNull DyeColor color);

    /**
     * Represents the various different cat types there are.
     *
     * @since 1.14
     */
    interface Type extends OldEnum<Type>, Keyed {

        Type TABBY = getType("tabby");
        Type BLACK = getType("black");
        Type RED = getType("red");
        Type SIAMESE = getType("siamese");
        Type BRITISH_SHORTHAIR = getType("british_shorthair");
        Type CALICO = getType("calico");
        Type PERSIAN = getType("persian");
        Type RAGDOLL = getType("ragdoll");
        Type WHITE = getType("white");
        Type JELLIE = getType("jellie");
        Type ALL_BLACK = getType("all_black");

        @NotNull
        private static Type getType(@NotNull String key) {
            return Registry.CAT_VARIANT.getOrThrow(NamespacedKey.minecraft(key));
        }

        /**
         * @param name of the cat type.
         * @return the cat type with the given name.
         * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type valueOf(@NotNull String name) {
            Type type = Registry.CAT_VARIANT.get(NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
            Preconditions.checkArgument(type != null, "No cat type found with the name %s", name);
            return type;
        }

        /**
         * @return an array of all known cat types.
         * @deprecated use {@link Registry#iterator()}.
         */
        @NotNull
        @Deprecated(since = "1.21", forRemoval = true) @org.jetbrains.annotations.ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
        static Type[] values() {
            return Lists.newArrayList(Registry.CAT_VARIANT).toArray(new Type[0]);
        }
    }

    // Paper start - More cat api
    /**
     * Sets if the cat is lying down.
     * This is visual and does not affect the behaviour of the cat.
     *
     * @param lyingDown whether the cat should lie down
     * @since 1.17.1
     */
    public void setLyingDown(boolean lyingDown);

    /**
     * Gets if the cat is lying down.
     *
     * @return whether the cat is lying down
     * @since 1.17.1
     */
    public boolean isLyingDown();

    /**
     * Sets if the cat has its head up.
     * This is visual and does not affect the behaviour of the cat.
     *
     * @param headUp head is up
     * @since 1.17.1
     */
    public void setHeadUp(boolean headUp);

    /**
     * Gets if the cat has its head up.
     *
     * @return head is up
     * @since 1.17.1
     */
    public boolean isHeadUp();
    // Paper end - More cat api
}
