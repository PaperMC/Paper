package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatRenderer;
import java.util.Set;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

/**
 * An abstract implementation of a chat event, handling shared logic.
 */
@ApiStatus.NonExtendable
@NullMarked
public abstract class AbstractChatEvent extends PlayerEvent implements Cancellable {

    private final Set<Audience> viewers;
    private final Component originalMessage;
    private final SignedMessage signedMessage;
    private ChatRenderer renderer;
    private Component message;

    private boolean cancelled;

    AbstractChatEvent(final boolean async, final Player player, final Set<Audience> viewers, final ChatRenderer renderer, final Component message, final Component originalMessage, final SignedMessage signedMessage) {
        super(player, async);
        this.viewers = viewers;
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = originalMessage;
        this.signedMessage = signedMessage;
    }

    /**
     * Gets a set of {@link Audience audiences} that this chat message will be displayed to.
     * <p>
     * The set returned may auto-populate on access. Any listener accessing the returned set should be aware that
     * it may reduce performance for a lazy set implementation.
     *
     * @return a mutable set of {@link Audience audiences} who will receive the chat message
     */
    public final Set<Audience> viewers() {
        return this.viewers;
    }

    /**
     * Sets the chat renderer.
     *
     * @param renderer the chat renderer
     * @throws NullPointerException if {@code renderer} is {@code null}
     */
    public final void renderer(final ChatRenderer renderer) {
        this.renderer = requireNonNull(renderer, "renderer");
    }

    /**
     * Gets the chat renderer.
     *
     * @return the chat renderer
     */
    public final ChatRenderer renderer() {
        return this.renderer;
    }

    /**
     * Gets the user-supplied message.
     * The return value will reflect changes made using {@link #message(Component)}.
     *
     * @return the user-supplied message
     */
    public final Component message() {
        return this.message;
    }

    /**
     * Sets the user-supplied message.
     *
     * @param message the user-supplied message
     * @throws NullPointerException if {@code message} is {@code null}
     */
    public final void message(final Component message) {
        this.message = requireNonNull(message, "message");
    }

    /**
     * Gets the original and unmodified user-supplied message.
     * The return value will <b>not</b> reflect changes made using
     * {@link #message(Component)}.
     *
     * @return the original user-supplied message
     */
    public final Component originalMessage() {
        return this.originalMessage;
    }

    /**
     * Gets the signed message.
     * Changes made in this event will <b>not</b> update
     * the signed message.
     *
     * @return the signed message
     */
    public final SignedMessage signedMessage() {
        return this.signedMessage;
    }

    @Override
    public final boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public final void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
