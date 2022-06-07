package org.bukkit.craftbukkit.command;

import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.rcon.RemoteControlCommandListener;
import org.bukkit.command.RemoteConsoleCommandSender;

public class CraftRemoteConsoleCommandSender extends ServerCommandSender implements RemoteConsoleCommandSender {

    private final RemoteControlCommandListener listener;

    public CraftRemoteConsoleCommandSender(RemoteControlCommandListener listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(String message) {
        listener.sendSystemMessage(IChatBaseComponent.literal(message + "\n")); // Send a newline after each message, to preserve formatting.
    }

    @Override
    public void sendMessage(String... messages) {
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
