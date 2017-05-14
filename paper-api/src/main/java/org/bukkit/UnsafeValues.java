package org.bukkit;

import java.util.List;

import org.bukkit.advancement.Advancement;
import org.bukkit.inventory.ItemStack;

/**
 * This interface provides value conversions that may be specific to a
 * runtime, or have arbitrary meaning (read: magic values).
 * <p>
 * Their existence and behavior is not guaranteed across future versions. They
 * may be poorly named, throw exceptions, have misleading parameters, or any
 * other bad programming practice.
 */
@Deprecated
public interface UnsafeValues {

    Material getMaterialFromInternalName(String name);

    List<String> tabCompleteInternalMaterialName(String token, List<String> completions);

    ItemStack modifyItemStack(ItemStack stack, String arguments);

    Statistic getStatisticFromInternalName(String name);

    Achievement getAchievementFromInternalName(String name);

    List<String> tabCompleteInternalStatisticOrAchievementName(String token, List<String> completions);

    /**
     * Load an advancement represented by the specified string into the server.
     * The advancement format is governed by Minecraft and has no specified
     * layout.
     * <br>
     * It is currently a JSON object, as described by the Minecraft Wiki:
     * http://minecraft.gamepedia.com/Advancements
     * <br>
     * Loaded advancements will be stored and persisted across server restarts
     * and reloads.
     * <br>
     * Callers should be prepared for {@link Exception} to be thrown.
     *
     * @param key the unique advancement key
     * @param advancement representation of the advancement
     * @return the loaded advancement or null if an error occurred
     */
    Advancement loadAdvancement(NamespacedKey key, String advancement);

    /**
     * Delete an advancement which was loaded and saved by
     * {@link #loadAdvancement(org.bukkit.NamespacedKey, java.lang.String)}.
     * <br>
     * This method will only remove advancement from persistent storage. It
     * should be accompanied by a call to {@link Server#reloadData()} in order
     * to fully remove it from the running instance.
     *
     * @param key the unique advancement key
     * @return true if a file matching this key was found and deleted
     */
    boolean removeAdvancement(NamespacedKey key);
}
