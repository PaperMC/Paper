package org.bukkit.event.block;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a block dispenses loot from its designated LootTable. This is not
 * to be confused with events like {@link BlockDispenseEvent} which fires when a
 * singular item is dispensed from its inventory container.
 * <br><br>
 * Example: A player unlocks a trial chamber vault and the vault block dispenses
 * its loot.
 */
@ApiStatus.Experimental
public class BlockDispenseLootEvent extends BlockEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private List<ItemStack> dispensedLoot;
    private boolean cancelled;

    public BlockDispenseLootEvent(@Nullable Player player, @NotNull Block theBlock, @NotNull List<ItemStack> dispensedLoot) {
        super(theBlock);
        this.player = player;
        this.block = theBlock;
        this.dispensedLoot = dispensedLoot;
    }

    /**
     * Gets the loot that will be dispensed.
     *
     * @return the loot that will be dispensed
     */
    @NotNull
    public List<ItemStack> getDispensedLoot() {
        return dispensedLoot;
    }

    /**
     * Sets the loot that will be dispensed.
     *
     * @param dispensedLoot new loot to dispense
     */
    public void setDispensedLoot(@Nullable List<ItemStack> dispensedLoot) {
        this.dispensedLoot = (dispensedLoot == null) ? new ArrayList<>() : dispensedLoot;
    }

    /**
     * Gets the player associated with this event.
     * <br>
     * <b>Warning:</b> Some event instances like a
     * {@link org.bukkit.block.TrialSpawner} dispensing its reward loot may not
     * have a player associated with them and will return null.
     *
     * @return the player who unlocked the vault
     */
    @Nullable
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
