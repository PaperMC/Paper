package org.bukkit.event.inventory;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.MenuType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the different kinds of inventories available in Bukkit.
 * <br>
 * Only InventoryTypes marked {@link #isCreatable()} can be created.
 * <br>
 * The current list of inventories that cannot be created via
 * {@link org.bukkit.Bukkit#createInventory} are:<br>
 * <blockquote>
 *     {@link InventoryType#CREATIVE}, {@link InventoryType#CRAFTING} and
 *     {@link InventoryType#MERCHANT}
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
    CHEST(27, "Chest", MenuType.GENERIC_9X3),
    /**
     * A dispenser inventory, with 9 slots of type CONTAINER.
     */
    DISPENSER(9, "Dispenser", MenuType.GENERIC_3X3),
    /**
     * A dropper inventory, with 9 slots of type CONTAINER.
     */
    DROPPER(9, "Dropper", MenuType.GENERIC_3X3),
    /**
     * A furnace inventory, with a RESULT slot, a CRAFTING slot, and a FUEL
     * slot.
     */
    FURNACE(3, "Furnace", MenuType.FURNACE),
    /**
     * A workbench inventory, with 9 CRAFTING slots and a RESULT slot.
     */
    WORKBENCH(10, "Crafting", MenuType.CRAFTING),
    /**
     * A player's crafting inventory, with 4 CRAFTING slots and a RESULT slot.
     * Also implies that the 4 ARMOR slots are accessible.
     */
    CRAFTING(5, "Crafting", null, false),
    /**
     * An enchantment table inventory, with two CRAFTING slots and three
     * enchanting buttons.
     */
    ENCHANTING(2, "Enchanting", MenuType.ENCHANTMENT),
    /**
     * A brewing stand inventory, with one FUEL slot and four CRAFTING slots.
     */
    BREWING(5, "Brewing", MenuType.BREWING_STAND),
    /**
     * A player's inventory, with 9 QUICKBAR slots, 27 CONTAINER slots, 4 ARMOR
     * slots, 1 offhand slot, 1 body slot and 1 saddle slot.
     * <p>
     * The ARMOR and offhand slots are conditionally visible to the player,
     * while body and saddle slot are never visible.
     */
    PLAYER(43, "Player", MenuType.GENERIC_9X4),
    /**
     * The creative mode inventory, with only 9 QUICKBAR slots and nothing
     * else. (The actual creative interface with the items is client-side and
     * cannot be altered by the server.)
     */
    CREATIVE(9, "Creative", null, false),
    /**
     * The merchant inventory, with 2 CRAFTING slots, and 1 RESULT slot.
     */
    MERCHANT(3, "Villager", MenuType.MERCHANT, false),
    /**
     * The ender chest inventory, with 27 slots.
     */
    ENDER_CHEST(27, "Ender Chest", MenuType.GENERIC_9X3),
    /**
     * An anvil inventory, with 2 CRAFTING slots and 1 RESULT slot
     */
    ANVIL(3, "Repairing", MenuType.ANVIL),
    /**
     * A smithing inventory, with 3 CRAFTING slots and 1 RESULT slot.
     */
    SMITHING(4, "Upgrade Gear", MenuType.SMITHING),
    /**
     * A beacon inventory, with 1 CRAFTING slot
     */
    BEACON(1, "container.beacon", MenuType.BEACON),
    /**
     * A hopper inventory, with 5 slots of type CONTAINER.
     */
    HOPPER(5, "Item Hopper", MenuType.HOPPER),
    /**
     * A shulker box inventory, with 27 slots of type CONTAINER.
     */
    SHULKER_BOX(27, "Shulker Box", MenuType.SHULKER_BOX),
    /**
     * A barrel box inventory, with 27 slots of type CONTAINER.
     */
    BARREL(27, "Barrel", MenuType.GENERIC_9X3),
    /**
     * A blast furnace inventory, with a RESULT slot, a CRAFTING slot, and a
     * FUEL slot.
     */
    BLAST_FURNACE(3, "Blast Furnace", MenuType.BLAST_FURNACE),
    /**
     * A lectern inventory, with 1 BOOK slot.
     */
    LECTERN(1, "Lectern", MenuType.LECTERN),
    /**
     * A smoker inventory, with a RESULT slot, a CRAFTING slot, and a FUEL slot.
     */
    SMOKER(3, "Smoker", MenuType.SMOKER),
    /**
     * Loom inventory, with 3 CRAFTING slots, and 1 RESULT slot.
     */
    LOOM(4, "Loom", MenuType.LOOM),
    /**
     * Cartography inventory with 2 CRAFTING slots, and 1 RESULT slot.
     */
    CARTOGRAPHY(3, "Cartography Table", MenuType.CARTOGRAPHY_TABLE),
    /**
     * Grindstone inventory with 2 CRAFTING slots, and 1 RESULT slot.
     */
    GRINDSTONE(3, "Repair & Disenchant", MenuType.GRINDSTONE),
    /**
     * Stonecutter inventory with 1 CRAFTING slot, and 1 RESULT slot.
     */
    STONECUTTER(2, "Stonecutter", MenuType.STONECUTTER),
    /**
     * Pseudo composter inventory with 0 or 1 slots of undefined type.
     */
    COMPOSTER(1, "Composter", null, false),
    /**
     * Pseudo chiseled bookshelf inventory, with 6 slots of undefined type.
     */
    CHISELED_BOOKSHELF(6, "Chiseled Bookshelf", null, false),
    /**
     * Pseudo shelf inventory, with 3 slots of undefined type.
     */
    SHELF(3, "Shelf", null, false),
    /**
     * Pseudo jukebox inventory with 1 slot of undefined type.
     */
    JUKEBOX(1, "Jukebox", null, false),
    // Paper start - add missing type
    /**
     * Pseudo decorated pot with 1 slot of undefined type.
     */
    DECORATED_POT(1, "Decorated Pot", null, false),
    // Paper end - add missing type
    /**
     * A crafter inventory, with 9 CRAFTING slots.
     */
    CRAFTER(9, "Crafter", MenuType.CRAFTER_3X3),
    /**
     * The new smithing inventory, with 3 CRAFTING slots and 1 RESULT slot.
     *
     * @deprecated use {@link #SMITHING}
     */
    @Deprecated(since = "1.20.1", forRemoval = true) // Paper
    SMITHING_NEW(4, "Upgrade Gear", MenuType.SMITHING),
    ;

    private final int size;
    private final String title;
    private final MenuType menuType;
    private final boolean isCreatable;
    // Paper start
    private final net.kyori.adventure.text.Component defaultTitleComponent;

    /**
     * Gets the inventory's default title.
     *
     * @return the inventory's default title
     */
    public net.kyori.adventure.text.@NotNull Component defaultTitle() {
        return defaultTitleComponent;
    }
    // Paper end
    private InventoryType(int defaultSize, /*@NotNull*/ String defaultTitle, @Nullable MenuType type) {
        this(defaultSize, defaultTitle, type, true);
    }

    private InventoryType(int defaultSize, /*@NotNull*/ String defaultTitle, @Nullable MenuType type, boolean isCreatable) {
        size = defaultSize;
        title = defaultTitle;
        this.menuType = type;
        this.isCreatable = isCreatable;
        this.defaultTitleComponent = net.kyori.adventure.text.Component.text(defaultTitle); // Paper - Adventure
    }

    public int getDefaultSize() {
        return size;
    }

    @NotNull
    @Deprecated // Paper
    public String getDefaultTitle() {
        return title;
    }

    /**
     * Gets the corresponding {@link MenuType} of this InventoryType.
     * <p>
     * Not all InventoryType correspond to a {@link MenuType}. These
     * InventoryTypes are also not creatable. If this method returns null,
     * {@link #isCreatable()} will return false, with the exception of
     * {@link #MERCHANT}.
     * <p>
     * As well as not necessarily corresponding to a {@link MenuType} some
     * InventoryType correspond to the same {@link MenuType}, including:
     * <ul>
     * <li>Dropper, Dispenser
     * <li>ShulkerBox, Barrel, Chest
     * </ul>
     *
     * @return the corresponding {@link MenuType}
     */
    @Nullable
    public MenuType getMenuType() {
        return menuType;
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
         * A slot in the crafting matrix, or an 'input' slot.
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
