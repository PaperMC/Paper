package org.bukkit.event.block;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.Nullable;

/**
 * Called when a block dispenses loot from its designated LootTable.
 * <p>
 * This is not to be confused with events like {@link BlockDispenseEvent} which fires when a
 * singular item is dispensed from its inventory container.
 * <br>
 * Example: A player unlocks a trial chamber vault and the vault block dispenses
 * its loot.
 */
public interface BlockDispenseLootEvent extends BlockEventNew, Cancellable {

    /**
     * Gets the player associated with this event.
     * <br>
     * <b>Warning:</b> Some event instances like a
     * {@link org.bukkit.block.TrialSpawner} dispensing its reward loot may not
     * have a player associated with them and will return {@code null}.
     *
     * @return the player who unlocked the vault
     */
    @Nullable Player getPlayer();

    /**
     * Gets the loot that will be dispensed.
     *
     * @return the loot that will be dispensed
     */
    List<ItemStack> getDispensedLoot();

    /**
     * Sets the loot that will be dispensed.
     *
     * @param dispensedLoot new loot to dispense
     */
    void setDispensedLoot(@Nullable List<ItemStack> dispensedLoot);

    /**
     * Gets the loot table used to generate the initial loot to dispense.
     *
     * @return the loot table used to generate the initial loot to dispense
     */
    LootTable getLootTable();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
