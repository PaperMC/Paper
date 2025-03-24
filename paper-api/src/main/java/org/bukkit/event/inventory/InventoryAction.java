package org.bukkit.event.inventory;

/**
 * An estimation of what the result will be.
 */
public enum InventoryAction {

    /**
     * Nothing will happen from the click.
     * <p>
     * There may be cases where nothing will happen and this is value is not
     * provided, but it is guaranteed that this value is accurate when given.
     */
    NOTHING,
    /**
     * All of the items on the clicked slot are moved to the cursor.
     */
    PICKUP_ALL,
    /**
     * Some of the items on the clicked slot are moved to the cursor.
     */
    PICKUP_SOME,
    /**
     * Half of the items on the clicked slot are moved to the cursor.
     */
    PICKUP_HALF,
    /**
     * One of the items on the clicked slot are moved to the cursor.
     */
    PICKUP_ONE,
    /**
     * All of the items on the cursor are moved to the clicked slot.
     */
    PLACE_ALL,
    /**
     * Some of the items from the cursor are moved to the clicked slot
     * (usually up to the max stack size).
     */
    PLACE_SOME,
    /**
     * A single item from the cursor is moved to the clicked slot.
     */
    PLACE_ONE,
    /**
     * The clicked item and the cursor are exchanged.
     */
    SWAP_WITH_CURSOR,
    /**
     * The entire cursor item is dropped.
     */
    DROP_ALL_CURSOR,
    /**
     * One item is dropped from the cursor.
     */
    DROP_ONE_CURSOR,
    /**
     * The entire clicked slot is dropped.
     */
    DROP_ALL_SLOT,
    /**
     * One item is dropped from the clicked slot.
     */
    DROP_ONE_SLOT,
    /**
     * The item is moved to the opposite inventory if a space is found.
     */
    MOVE_TO_OTHER_INVENTORY,
    /**
     * The clicked item is moved to the hotbar, and the item currently there
     * is re-added to the player's inventory.
     *
     * The hotbar includes the player's off hand.
     * @deprecated This action no longer happens, they are all {@link #HOTBAR_SWAP}s.
     */
    @Deprecated(since = "1.20.6", forRemoval = true) // Paper
    HOTBAR_MOVE_AND_READD,
    /**
     * The clicked slot and the picked hotbar slot are swapped.
     *
     * The hotbar includes the player's off hand.
     */
    HOTBAR_SWAP,
    /**
     * A max-size stack of the clicked item is put on the cursor.
     */
    CLONE_STACK,
    /**
     * The inventory is searched for the same material, and they are put on
     * the cursor up to {@link org.bukkit.Material#getMaxStackSize()}.
     */
    COLLECT_TO_CURSOR,
    /**
     * An unrecognized ClickType.
     */
    UNKNOWN,
    /**
     * The first stack of items in the clicked bundle is moved to the cursor.
     */
    PICKUP_FROM_BUNDLE,
    /**
     * All of the items on the clicked slot are moved into the bundle on the cursor.
     */
    PICKUP_ALL_INTO_BUNDLE,
    /**
     * Some of the items on the clicked slot are moved into the bundle on the cursor.
     */
    PICKUP_SOME_INTO_BUNDLE,
    /**
     * The first stack of items is moved to the clicked slot.
     */
    PLACE_FROM_BUNDLE,
    /**
     * All of the items on the cursor are moved into the bundle in the clicked slot.
     */
    PLACE_ALL_INTO_BUNDLE,
    /**
     * Some of the items on the cursor are moved into the bundle in the clicked slot.
     */
    PLACE_SOME_INTO_BUNDLE,
    ;
}
