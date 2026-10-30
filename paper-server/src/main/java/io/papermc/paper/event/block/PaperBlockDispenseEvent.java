package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jspecify.annotations.NullMarked;

/**
 * Server side subtype of the block dispense event used to track more state through the call chain.
 */
@NullMarked
public class PaperBlockDispenseEvent extends BlockDispenseEvent {

    public enum Result {
        CANCELLED,
        ITEM_CHANGE_REROUTED,
        ITEM_CHANGE_NO_REROUTE,
        NO_REROUTED,
        ;

        public boolean preventBehaviour() {
            return switch (this) {
                case CANCELLED, ITEM_CHANGE_REROUTED -> true;
                case ITEM_CHANGE_NO_REROUTE, NO_REROUTED -> false;
            };
        }
    }

    public Result result = Result.NO_REROUTED;

    public PaperBlockDispenseEvent(final Block block, final ItemStack item, final Vector velocity) {
        super(block, item, velocity);
    }

    public PaperBlockDispenseEvent trackResult(final Result result) {
        this.result = result;
        return this;
    }
}
