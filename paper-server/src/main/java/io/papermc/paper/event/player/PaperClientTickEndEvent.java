package io.papermc.paper.event.player;

import io.papermc.paper.event.packet.ClientTickEndEvent;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.event.HandlerList;

public class PaperClientTickEndEvent extends CraftPlayerEvent implements ClientTickEndEvent {

    public PaperClientTickEndEvent(final ServerPlayer player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return ClientTickEndEvent.getHandlerList();
    }
}
