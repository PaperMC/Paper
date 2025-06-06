package io.papermc.paper.event.dialog;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CustomClickActionEvent extends PlayerEvent {
    Key id;
    @Nullable String payload;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public CustomClickActionEvent(final Player player, final Key id, final @Nullable String payload) {
        super(player);
        this.id = id;
        this.payload = payload;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    public Key id() {
        return this.id;
    }

    public @Nullable String payload() {
        return this.payload;
    }
}
