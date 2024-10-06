package org.bukkit.inventory.view;

import java.util.List;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.LoomInventory;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of {@link InventoryView} which provides extra methods related to
 * loom view data.
 */
public interface LoomView extends InventoryView {

    @NotNull
    @Override
    LoomInventory getTopInventory();

    /**
     * Gets a list of all selectable to the player.
     *
     * @return A copy of the {@link PatternType}'s currently selectable by the
     * player
     */
    @NotNull
    List<PatternType> getSelectablePatterns();

    /**
     * Gets an index of the selected pattern.
     *
     * @return Index of the selected pattern
     */
    int getSelectedPatternIndex();
}
