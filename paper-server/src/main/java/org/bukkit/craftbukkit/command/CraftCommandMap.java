package org.bukkit.craftbukkit.command;

import com.destroystokyo.paper.exception.ServerCommandException;
import io.papermc.paper.event.server.PaperServerExceptionEvent;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

public class CraftCommandMap extends SimpleCommandMap {

    public CraftCommandMap(Server server) {
        super(server, io.papermc.paper.command.brigadier.bukkit.BukkitBrigForwardingMap.INSTANCE);
    }

    @Override
    public Map<String, Command> getKnownCommands() {
        return this.knownCommands;
    }

    @Override
    protected void onServerException(ServerCommandException exception) {
        new PaperServerExceptionEvent(exception).callEvent();
    }
}
