package io.papermc.paper.event.player;

import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PaperPlayerClientLoadedWorldEvent extends CraftPlayerEvent implements PlayerClientLoadedWorldEvent {

    private final boolean timeout;

    public PaperPlayerClientLoadedWorldEvent(final Player player, final boolean timeout) {
        super(player);
        this.timeout = timeout;
    }

    public PaperPlayerClientLoadedWorldEvent(final ServerPlayer player, final boolean timeout) {
        this(player.getBukkitEntity(), timeout);
    }

    @Override
    public boolean isTimeout() {
        return this.timeout;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerClientLoadedWorldEvent.getHandlerList();
    }
}
