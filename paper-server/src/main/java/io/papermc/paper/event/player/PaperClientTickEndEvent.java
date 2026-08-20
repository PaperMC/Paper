package io.papermc.paper.event.player;

import io.papermc.paper.event.packet.ClientTickEndEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperClientTickEndEvent extends CraftPlayerEvent implements ClientTickEndEvent {

    public PaperClientTickEndEvent(final Player player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return ClientTickEndEvent.getHandlerList();
    }
}
