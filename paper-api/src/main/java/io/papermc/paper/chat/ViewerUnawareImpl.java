package io.papermc.paper.chat;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@ApiStatus.Internal
@NullMarked
sealed class ViewerUnawareImpl implements ChatRenderer, ChatRenderer.ViewerUnaware permits ViewerUnawareImpl.Default {
    private final ViewerUnaware unaware;

    ViewerUnawareImpl(final ViewerUnaware unaware) {
        this.unaware = unaware;
    }

    @Override
    public Component render(final Player source, final Component sourceDisplayName, final Component message, final Audience viewer) {
        return this.render(source, sourceDisplayName, message);
    }

    @Override
    public Component render(final Player source, final Component sourceDisplayName, final Component message) {
        return this.unaware.render(source, sourceDisplayName, message);
    }

    static final class Default extends ViewerUnawareImpl implements ChatRenderer.Default {
        Default(final ViewerUnaware unaware) {
            super(unaware);
        }
    }
}
