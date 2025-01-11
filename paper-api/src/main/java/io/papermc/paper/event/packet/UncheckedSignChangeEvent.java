package io.papermc.paper.event.packet;

import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.text.Component;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;
import java.util.List;

/**
 * Called when a sign edit packet has been received, but the location at which the sign should be edited
 * has not yet been checked for the existence of a real sign.
 * <p>
 * Cancelling this event will prevent further processing of the sign change, but needs further handling
 * by the plugin because the local world might be in an inconsistent state.
 */
@NullMarked
public class UncheckedSignChangeEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean cancel = false;
    private final BlockPosition editedBlockPosition;
    private final Side side;
    private final List<Component> lines;

    @ApiStatus.Internal
    public UncheckedSignChangeEvent(final Player editor, final BlockPosition editedBlockPosition, final Side side, final List<Component> lines) {
        super(editor);
        this.editedBlockPosition = editedBlockPosition;
        this.side = side;
        this.lines = lines;
    }

    /**
     * Gets the location at which a potential sign was edited.
     *
     * @return location where the change happened
     */
    public BlockPosition getEditedBlockPosition() {
        return editedBlockPosition;
    }

    /**
     * Gets which side of the sign was edited.
     *
     * @return {@link Side} that was edited
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets the lines that the player has entered.
     *
     * @return the lines
     */
    public @Unmodifiable List<Component> lines() {
        return lines;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
