package cool.circuit.paper.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;


public class PlayerShowTitleEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final Player player;
    private final String title;

    public PlayerShowTitleEvent(@NotNull final Player player, @NotNull final String title) {
        this.player = player;
        this.title = title;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLERS;
    }
}
