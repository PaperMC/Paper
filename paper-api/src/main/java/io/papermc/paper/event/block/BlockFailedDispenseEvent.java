package io.papermc.paper.event.block;

import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Called when a block tries to dispense an item, but its inventory is empty.
 */
@NullMarked
public class BlockFailedDispenseEvent extends BlockEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private boolean shouldPlayEffect = true;

    @ApiStatus.Internal
    public BlockFailedDispenseEvent(final Block block) {
        super(block);
    }

    /**
     * @return if the effect should be played
     */
    public boolean shouldPlayEffect() {
        return this.shouldPlayEffect;
    }

    /**
     * Sets if the effect for empty dispensers should be played
     *
     * @param playEffect if the effect should be played
     */
    public void shouldPlayEffect(final boolean playEffect) {
        this.shouldPlayEffect = playEffect;
    }

    /**
     * @return {@link #shouldPlayEffect()}
     */
    @Override
    public boolean callEvent() {
        super.callEvent();
        return this.shouldPlayEffect();
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
