package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatRenderer;
import java.util.Set;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEventNew;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * An abstract implementation of a chat event, handling shared logic.
 */
@NullMarked
@ApiStatus.NonExtendable
public interface AbstractChatEvent extends PlayerEventNew, Cancellable {

    /**
     * Gets a set of {@link Audience audiences} that this chat message will be displayed to.
     * <p>
     * The set returned may auto-populate on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.
     *
     * @return a mutable set of {@link Audience audiences} who will receive the chat message
     */
    Set<Audience> viewers();

    /**
     * Sets the chat renderer.
     *
     * @param renderer the chat renderer
     * @throws NullPointerException if {@code renderer} is {@code null}
     */
    void renderer(ChatRenderer renderer);

    /**
     * Gets the chat renderer.
     *
     * @return the chat renderer
     */
    ChatRenderer renderer();

    /**
     * Gets the user-supplied message.
     * The return value will reflect changes made using {@link #message(Component)}.
     *
     * @return the user-supplied message
     */
    Component message();

    /**
     * Sets the user-supplied message.
     *
     * @param message the user-supplied message
     * @throws NullPointerException if {@code message} is {@code null}
     */
    void message(Component message);

    /**
     * Gets the original and unmodified user-supplied message.
     * The return value will <b>not</b> reflect changes made using
     * {@link #message(Component)}.
     *
     * @return the original user-supplied message
     */
    Component originalMessage();

    /**
     * Gets the signed message.
     * Changes made in this event will <b>not</b> update
     * the signed message.
     *
     * @return the signed message
     */
    SignedMessage signedMessage();
}
