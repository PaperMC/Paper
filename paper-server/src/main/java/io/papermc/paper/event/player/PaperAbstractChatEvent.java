package io.papermc.paper.event.player;

import io.papermc.paper.chat.ChatRenderer;
import java.util.Set;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;

import static java.util.Objects.requireNonNull;

public sealed abstract class PaperAbstractChatEvent extends CraftPlayerEvent implements AbstractChatEvent permits PaperAsyncChatEvent, PaperChatEvent {

    private final Set<Audience> viewers;
    private final Component originalMessage;
    private final SignedMessage signedMessage;
    private ChatRenderer renderer;
    private Component message;

    private boolean cancelled;

    PaperAbstractChatEvent(final boolean async, final Player player, final Set<Audience> viewers, final ChatRenderer renderer, final Component message, final Component originalMessage, final SignedMessage signedMessage) {
        super(player, async);
        this.viewers = viewers;
        this.renderer = renderer;
        this.message = message;
        this.originalMessage = originalMessage;
        this.signedMessage = signedMessage;
    }

    @Override
    public final Set<Audience> viewers() {
        return this.viewers;
    }

    @Override
    public final void renderer(final ChatRenderer renderer) {
        this.renderer = requireNonNull(renderer, "renderer");
    }

    @Override
    public final ChatRenderer renderer() {
        return this.renderer;
    }

    @Override
    public final Component message() {
        return this.message;
    }

    @Override
    public final void message(final Component message) {
        this.message = requireNonNull(message, "message");
    }

    @Override
    public final Component originalMessage() {
        return this.originalMessage;
    }

    @Override
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
