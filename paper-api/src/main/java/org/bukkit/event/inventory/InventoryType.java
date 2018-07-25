package org.bukkit.event.inventory;

import org.bukkit.inventory.InventoryHolder;

/**
 * Represents the different kinds of inventories available in Bukkit.
 * <br>
 * Only InventoryTypes marked {@link #isCreatable()} can be created.
 * <br>
 * The current list of inventories that cannot be created via
 * {@link org.bukkit.Bukkit#createInventory} are:<br>
 * <blockquote>
 *     {@link InventoryType#CREATIVE} and {@link InventoryType#CRAFTING}
 * </blockquote>
 *
 * See {@link org.bukkit.Bukkit#createInventory} for more information.
 *
 * @see org.bukkit.Bukkit#createInventory(InventoryHolder, InventoryType)
 */
public enum InventoryType {

    /**
     * A chest inventory, with 0, 9, 18, 27, 36, 45, or 54 slots of type
     * CONTAINER.
     */
    CHEST(27,"Chest"),
    /**
     * A dispenser inventory, with 9 slots of type CONTAINER.
     */
    DISPENSER(9,"Dispenser"),
    /**
     * A dropper inventory, with 9 slots of type CONTAINER.
     */
    DROPPER(9, "Dropper"),
    /**
     * A furnace inventory, with a RESULT slot, a CRAFTING slot, and a FUEL
     * slot.
     */
    FURNACE(3,"Furnace"),
    /**
     * A workbench inventory, with 9 CRAFTING slots and a RESULT slot.
     */
    WORKBENCH(10,"Crafting"),
    /**
     * A player's crafting inventory, with 4 CRAFTING slots and a RESULT slot.
     * Also implies that the 4 ARMOR slots are accessible.
     */
    CRAFTING(5,"Crafting", false),
    /**
     * An enchantment table inventory, with two CRAFTING slots and three
     * enchanting buttons.
     */
    ENCHANTING(2,"Enchanting"),
    /**
     * A brewing stand inventory, with one FUEL slot and three CRAFTING slots.
     */
    BREWING(5,"Brewing"),
    /**
     * A player's inventory, with 9 QUICKBAR slots, 27 CONTAINER slots, 4 ARMOR
     * slots and 1 offhand slot. The ARMOR and offhand slots may not be visible
     * to the player, though.
     */
    PLAYER(41,"Player"),
    /**
     * The creative mode inventory, with only 9 QUICKBAR slots and nothing
     * else. (The actual creative interface with the items is client-side and
     * cannot be altered by the server.)
     */
    CREATIVE(9,"Creative", false),
    /**
     * The merchant inventory, with 2 TRADE-IN slots, and 1 RESULT slot.
     */
    MERCHANT(3,"Villager"),
    /**
     * The ender chest inventory, with 27 slots.
     */
    ENDER_CHEST(27,"Ender Chest"),
    /**
     * An anvil inventory, with 2 CRAFTING slots and 1 RESULT slot
     */
    ANVIL(3, "Repairing"),
    /**
     * A beacon inventory, with 1 CRAFTING slot
     */
    BEACON(1, "container.beacon"),
    /**
     * A hopper inventory, with 5 slots of type CONTAINER.
     */
    HOPPER(5, "Item Hopper"),
    /**
     * A shulker box inventory, with 27 slots of type CONTAINER.
     */
    SHULKER_BOX(27, "Shulker Box"),
    ;

    private final int size;
    private final String title;
    private final boolean isCreatable;

    private InventoryType(int defaultSize, String defaultTitle) {
        this(defaultSize, defaultTitle, true);
    }

    private InventoryType(int defaultSize, String defaultTitle, boolean isCreatable) {
        size = defaultSize;
        title = defaultTitle;
        this.isCreatable = isCreatable;
    }

    public int getDefaultSize() {
        return size;
    }

    public String getDefaultTitle() {
        return title;
    }

    /**
     * Denotes that this InventoryType can be created via the normal
     * {@link org.bukkit.Bukkit#createInventory} methods.
     *
     * @return if this InventoryType can be created and shown to a player
     */
    public boolean isCreatable() {
        return isCreatable;
    }

    public enum SlotType {
        /**
         * A result slot in a furnace or crafting inventory.
         */
        RESULT,
        /**
         * A slot in the crafting matrix, or the input slot in a furnace
         * inventory, the potion slot in the brewing stand, or the enchanting
         * slot.
         */
        CRAFTING,
        /**
         * An armour slot in the player's inventory.
         */
        ARMOR,
        /**
         * A regular slot in the container or the player's inventory; anything
         * not covered by the other enum values.
         */
        CONTAINER,
        /**
         * A slot in the bottom row or quickbar.
         */
        QUICKBAR,
        /**
         * A pseudo-slot representing the area outside the inventory window.
         */
        OUTSIDE,
        /**
         * The fuel slot in a furnace inventory, or the ingredient slot in a
         * brewing stand inventory.
         */
        FUEL;
    }
}
