package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.inventory.view.BeaconView;
import org.bukkit.inventory.view.BrewingStandView;
import org.bukkit.inventory.view.CrafterView;
import org.bukkit.inventory.view.EnchantmentView;
import org.bukkit.inventory.view.FurnaceView;
import org.bukkit.inventory.view.LecternView;
import org.bukkit.inventory.view.LoomView;
import org.bukkit.inventory.view.MerchantView;
import org.bukkit.inventory.view.StonecutterView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Represents different kinds of views, also known as menus, which can be
 * created and viewed by the player.
 */
@ApiStatus.Experimental
public interface MenuType extends Keyed {

    /**
     * A MenuType which represents a chest with 1 row.
     */
    MenuType.Typed<InventoryView> GENERIC_9X1 = get("generic_9x1");
    /**
     * A MenuType which represents a chest with 2 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_9X2 = get("generic_9x2");
    /**
     * A MenuType which represents a chest with 3 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_9X3 = get("generic_9x3");
    /**
     * A MenuType which represents a chest with 4 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_9X4 = get("generic_9x4");
    /**
     * A MenuType which represents a chest with 5 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_9X5 = get("generic_9x5");
    /**
     * A MenuType which represents a chest with 6 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_9X6 = get("generic_9x6");
    /**
     * A MenuType which represents a dispenser/dropper like menu with 3 columns
     * and 3 rows.
     */
    MenuType.Typed<InventoryView> GENERIC_3X3 = get("generic_3x3");
    /**
     * A MenuType which represents a crafter
     */
    MenuType.Typed<CrafterView> CRAFTER_3X3 = get("crafter_3x3");
    /**
     * A MenuType which represents an anvil.
     */
    MenuType.Typed<AnvilView> ANVIL = get("anvil");
    /**
     * A MenuType which represents a beacon.
     */
    MenuType.Typed<BeaconView> BEACON = get("beacon");
    /**
     * A MenuType which represents a blast furnace.
     */
    MenuType.Typed<FurnaceView> BLAST_FURNACE = get("blast_furnace");
    /**
     * A MenuType which represents a brewing stand.
     */
    MenuType.Typed<BrewingStandView> BREWING_STAND = get("brewing_stand");
    /**
     * A MenuType which represents a crafting table.
     */
    MenuType.Typed<InventoryView> CRAFTING = get("crafting");
    /**
     * A MenuType which represents an enchantment table.
     */
    MenuType.Typed<EnchantmentView> ENCHANTMENT = get("enchantment");
    /**
     * A MenuType which represents a furnace.
     */
    MenuType.Typed<FurnaceView> FURNACE = get("furnace");
    /**
     * A MenuType which represents a grindstone.
     */
    MenuType.Typed<InventoryView> GRINDSTONE = get("grindstone");
    /**
     * A MenuType which represents a hopper.
     */
    MenuType.Typed<InventoryView> HOPPER = get("hopper");
    /**
     * A MenuType which represents a lectern, a book like view.
     */
    MenuType.Typed<LecternView> LECTERN = get("lectern");
    /**
     * A MenuType which represents a loom.
     */
    MenuType.Typed<LoomView> LOOM = get("loom");
    /**
     * A MenuType which represents a merchant.
     */
    MenuType.Typed<MerchantView> MERCHANT = get("merchant");
    /**
     * A MenuType which represents a shulker box.
     */
    MenuType.Typed<InventoryView> SHULKER_BOX = get("shulker_box");
    /**
     * A MenuType which represents a stonecutter.
     */
    MenuType.Typed<InventoryView> SMITHING = get("smithing");
    /**
     * A MenuType which represents a smoker.
     */
    MenuType.Typed<FurnaceView> SMOKER = get("smoker");
    /**
     * A MenuType which represents a cartography table.
     */
    MenuType.Typed<InventoryView> CARTOGRAPHY_TABLE = get("cartography_table");
    /**
     * A MenuType which represents a stonecutter.
     */
    MenuType.Typed<StonecutterView> STONECUTTER = get("stonecutter");

    /**
     * Typed represents a subtype of {@link MenuType}s that have a known
     * {@link InventoryView} type at compile time.
     *
     * @param <V> the generic type of {@link InventoryView} that represents the
     * view type.
     */
    interface Typed<V extends InventoryView> extends MenuType {

        /**
         * Creates a view of the specified menu type.
         * <p>
         * The player provided to create this view must be the player the view
         * is opened for. See {@link HumanEntity#openInventory(InventoryView)}
         * for more information.
         *
         * @param player the player the view belongs to
         * @param title the title of the view
         * @return the created {@link InventoryView}
         */
        @NotNull
        V create(@NotNull HumanEntity player, @NotNull String title);
    }

    /**
     * Yields this MenuType as a typed version of itself with a plain
     * {@link InventoryView} representing it.
     *
     * @return the typed MenuType.
     */
    @NotNull
    MenuType.Typed<InventoryView> typed();

    /**
     * Yields this MenuType as a typed version of itself with a specific
     * {@link InventoryView} representing it.
     *
     * @param viewClass the class type of the {@link InventoryView} to type this
     * {@link InventoryView} with.
     * @param <V> the generic type of the InventoryView to get this MenuType
     * with
     * @return the typed MenuType
     * @throws IllegalArgumentException if the provided viewClass cannot be
     * typed to this MenuType
     */
    @NotNull
    <V extends InventoryView> MenuType.Typed<V> typed(@NotNull final Class<V> viewClass) throws IllegalArgumentException;

    /**
     * Gets the {@link InventoryView} class of this MenuType.
     *
     * @return the {@link InventoryView} class of this MenuType
     */
    @NotNull
    Class<? extends InventoryView> getInventoryViewClass();

    private static <T extends MenuType> T get(@NotNull final String key) {
        final MenuType type = Registry.MENU.get(NamespacedKey.minecraft(key));
        Preconditions.checkArgument(type != null, "The given string key must be an existing menu type");
        return (T) type;
    }
}
