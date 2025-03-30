package cool.circuit.paper.menu;

import cool.circuit.paper.menu.button.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface Menu {

    Inventory getInventory();

    String getTitle();

    int getSlots();

    HashMap<Integer, ItemStack> getItems();

    void setItems(@NotNull HashMap<Integer, ItemStack> items);

    void open(@NotNull Player player);

    void close(@NotNull Player player);

    void setMenuItems();

    void addButton(@NotNull Button button);
}
