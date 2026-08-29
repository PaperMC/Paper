package io.papermc.paper.event.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a {@link Player} selects an item inside a bundle.
 *
 * @apiNote This event does not fire for bundle item selections in creative mode player inventories.
 */
@NullMarked
public final class PlayerBundleItemSelectEvent extends InventoryEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final ItemStack bundle;
    private final int rawSlot;
    private final int slot;

    private final ItemStack previousItem;
    private final ItemStack selectedItem;

    private final int previousIndex;
    private final int selectedIndex;
    private final Direction direction;

    @ApiStatus.Internal
    public PlayerBundleItemSelectEvent(final InventoryView view, final ItemStack bundle, final int rawSlot, final ItemStack previousItem, final ItemStack selectedItem, final int previousIndex, final int selectedIndex, final Direction direction) {
        super(view);

        this.bundle = bundle;
        this.rawSlot = rawSlot;
        this.slot = view.convertSlot(rawSlot);

        this.previousItem = previousItem;
        this.selectedItem = selectedItem;

        this.previousIndex = previousIndex;
        this.selectedIndex = selectedIndex;
        this.direction = direction;
    }

    /**
     * Gets the player who triggered the event.
     *
     * @return the player
     */
    public Player getPlayer() {
        return (Player) this.getView().getPlayer();
    }

    /**
     * Gets the bundle item.
     *
     * @return the bundle item
     */
    public ItemStack getBundle() {
        return this.bundle;
    }

    /**
     * Gets the slot number of the bundle, depending on which {@link Inventory} it is located in.
     *
     * @return the slot number
     * @see InventoryClickEvent#getSlot()
     */
    public int getSlot() {
        return this.slot;
    }

    /**
     * Gets the raw slot number of the bundle inside the {@link InventoryView}.
     *
     * @return the raw slot number
     * @see InventoryClickEvent#getRawSlot()
     */
    public int getRawSlot() {
        return this.rawSlot;
    }

    /**
     * Gets the previously selected item inside the bundle. If no item was previously selected, this will return an empty item.
     *
     * @return the previously selected item
     */
    public ItemStack getPreviousItem() {
        return this.previousItem.clone();
    }

    /**
     * Gets the selected item inside the bundle.
     *
     * @return the selected item
     */
    public ItemStack getSelectedItem() {
        return this.selectedItem.clone();
    }

    /**
     * Gets the previously selected index. If no item was previously selected, this will be -1.
     *
     * @return the previously selected index
     */
    public int getPreviousIndex() {
        return this.previousIndex;
    }

    /**
     * Gets the selected index.
     *
     * @return the selected index
     */
    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    /**
     * Gets the direction from the previous selection to the new one.
     *
     * @apiNote The direction cannot be reliably determined if there are less than three item stacks inside the bundle, in which case this returns {@link Direction#UNKNOWN}.
     *
     * @return the direction
     */
    public Direction getDirection() {
        return this.direction;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Represents a selection direction.
     */
    public enum Direction {
        /**
         * Represents forward direction.
         */
        FORWARD(1),
        /**
         * Represents backward direction.
         */
        BACKWARD(-1),
        /**
         * Represents indeterminable direction.
         * <p>
         * That is, for bundles with less than three item stacks inside.
         */
        UNKNOWN(0);

        private final int delta;

        Direction(final int delta) {
            this.delta = delta;
        }

        /**
         * Returns the delta integer of the direction.
         *
         * @return the delta
         */
        public int getDelta() {
            return this.delta;
        }

        /**
         * Gets the Direction associated with the given delta.
         *
         * @param delta the delta integer
         * @return the direction
         * @throws IllegalArgumentException if delta is none of {@code {0, -1, 1}}
         */
        public static Direction ofDelta(final int delta) {
            return switch (delta) {
                case 1 -> FORWARD;
                case -1 -> BACKWARD;
                case 0 -> UNKNOWN;
                default -> throw new IllegalArgumentException("Invalid delta: " + delta);
            };
        }
    }
}
