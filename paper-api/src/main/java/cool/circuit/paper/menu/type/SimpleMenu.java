package cool.circuit.paper.menu.type;

import cool.circuit.paper.menu.Menu;
import cool.circuit.paper.menu.button.Button;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;

/**
 * An abstract implementation of the Menu interface for creating simple menus.
 * This class handles the inventory and button placement for the menu.
 */
public abstract class SimpleMenu implements Menu, InventoryHolder {

    private final String title;
    private final int slots;
    private final Inventory inv;
    private final HashMap<Integer, ItemStack> items = new HashMap<>();

    /**
     * Constructs a new SimpleMenu.
     *
     * @param title The title of the menu.
     * @param slots The number of slots in the menu.
     */
    public SimpleMenu(@NotNull final String title, final int slots) {
        this.title = title;
        this.slots = slots;
        this.inv = Bukkit.createInventory(this, slots, title);
    }

    /**
     * Gets the title of the menu.
     *
     * @return The title of the menu.
     */
    @Override
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * Gets the number of slots in the menu.
     *
     * @return The number of slots in the menu.
     */
    @Override
    public int getSlots() {
        return slots;
    }

    /**
     * Gets the inventory of the menu.
     *
     * @return The inventory of the menu.
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }

    /**
     * Sets the items in the menu.
     * This method places items into the inventory at the specified slots.
     */
    @Override
    public void setMenuItems() {
        for (int slot : items.keySet()) {
            inv.setItem(slot, items.get(slot));
        }
    }

    /**
     * Opens the menu for the given player.
     *
     * @param player The player who will open the menu.
     */
    @Override
    public void open(@NotNull final Player player) {
        setMenuItems();
        player.openInventory(inv);
    }

    /**
     * Sets the items to be placed in the menu.
     *
     * @param items A map of slot numbers to ItemStacks that represent the items to be set.
     */
    @Override
    public void setItems(@NotNull final HashMap<Integer, ItemStack> items) {
        this.items.putAll(items);
        setMenuItems();
    }

    /**
     * Closes the menu for the given player.
     *
     * @param player The player who will close the menu.
     */
    @Override
    public void close(@NotNull final Player player) {
        player.closeInventory();
    }

    /**
     * Adds a button to the menu at the specified slot.
     *
     * @param button The button to be added to the menu.
     */
    @Override
    public void addButton(@NotNull final Button button) {
        ItemStack item = button.getItem();
        int slot = button.getSlot();

        inv.setItem(slot, item);
    }
}
