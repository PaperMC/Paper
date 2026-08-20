package io.papermc.paper.event.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEventNew;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a block tries to dispense an item, but its inventory is empty.
 */
@NullMarked
public interface BlockFailedDispenseEvent extends BlockEventNew {

    /**
     * @return if the effect should be played
     */
    boolean shouldPlayEffect();

    /**
     * Sets if the effect for empty dispensers should be played
     *
     * @param playEffect if the effect should be played
     */
    void shouldPlayEffect(boolean playEffect);

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
