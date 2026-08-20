package org.bukkit.event.block;

import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jspecify.annotations.Nullable;

/**
 * Called when a sign is changed by a player.
 * <p>
 * If this event is cancelled, the sign will not be changed.
 */
public interface SignChangeEvent extends BlockEvent, Cancellable {

    /**
     * Gets the player changing the sign involved in this event.
     *
     * @return the Player involved in this event
     */
    Player getPlayer();

    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     */
    List<Component> lines();

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return the String containing the line of text associated with the
     *     provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    @Nullable Component line(int index);

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     */
    void line(int index, @Nullable Component line);

    /**
     * Gets all of the lines of text from the sign involved in this event.
     *
     * @return the String array for the sign's lines new text
     * @deprecated in favour of {@link #lines()}
     */
    @Deprecated
    String[] getLines();

    /**
     * Gets a single line of text from the sign involved in this event.
     *
     * @param index index of the line to get
     * @return the String containing the line of text associated with the
     *     provided index
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     * @deprecated in favour of {@link #line(int)}
     */
    @Deprecated
    @Nullable String getLine(int index);

    /**
     * Sets a single line for the sign involved in this event
     *
     * @param index index of the line to set
     * @param line text to set
     * @throws IndexOutOfBoundsException thrown when the provided index is {@literal > 3
     *     or < 0}
     * @deprecated in favour of {@link #line(int, Component)}
     */
    @Deprecated
    void setLine(int index, @Nullable String line);

    /**
     * Returns which side is changed.
     *
     * @return the affected side of the sign
     */
    Side getSide();

    static HandlerList getHandlerList() {
        final class Holder {
            private static final HandlerList HANDLER_LIST = new HandlerList();
        }
        return Holder.HANDLER_LIST;
    }
}
