package org.bukkit.craftbukkit.command;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.RemoteControlCommandListener;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {
    public CraftRemoteConsoleCommandSender() {
        super();
    }

    public void sendMessage(String message) {
        RemoteControlCommandListener.instance.sendMessage(ChatMessage.d(message + "\n")); // Send a newline after each message, to preserve formatting.
    }

    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    public String getName() {
        return "Rcon";
    }

    public boolean isOp() {
        return true;
    }

    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of remote controller.");
    }
}
