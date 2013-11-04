package org.bukkit.craftbukkit.command;

import net.minecraft.server.RemoteControlCommandListener;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {
    public CraftRemoteConsoleCommandSender() {
        super();
    }

    @Override
    public void sendMessage(String message) {
        RemoteControlCommandListener.instance.sendMessage(message + "\n"); // Send a newline after each message, to preserve formatting.
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return "Rcon";
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
