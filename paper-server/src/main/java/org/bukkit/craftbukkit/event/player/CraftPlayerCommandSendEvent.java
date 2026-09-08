package org.bukkit.craftbukkit.event.player;

import java.util.Collection;
import java.util.LinkedHashSet;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class CraftPlayerCommandSendEvent extends CraftPlayerEvent implements PlayerCommandSendEvent {

    private final Collection<String> commands;

    public CraftPlayerCommandSendEvent(final Player player, final Collection<String> commands) {
        super(player);
        this.commands = new LinkedHashSet<>(commands);
    }

    public CraftPlayerCommandSendEvent(final ServerPlayer player, final Collection<String> commands) {
        this(player.getBukkitEntity(), commands);
    }

    @Override
    public Collection<String> getCommands() {
        return this.commands;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerCommandSendEvent.getHandlerList();
    }
}
