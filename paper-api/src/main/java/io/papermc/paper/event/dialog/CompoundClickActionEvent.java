package io.papermc.paper.event.dialog;

import io.papermc.paper.dialog.InputData;
import net.kyori.adventure.key.Key;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CompoundClickActionEvent extends PlayerEvent {
    Key id;
    InputData payload;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    @ApiStatus.Internal
    public CompoundClickActionEvent(final Player player, final Key id, final InputData payload) {
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

    public InputData payload() {
        return this.payload;
    }
}
