package org.bukkit.craftbukkit.command;

import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.ICommandListener;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftFunctionCommandSender extends ServerCommandSender {

    private final ICommandListener handle;

    public CraftFunctionCommandSender(ICommandListener handle) {
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        for (IChatBaseComponent component : CraftChatMessage.fromString(message)) {
            handle.sendMessage(component);
        }
    }

    @Override
    public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server function sender");
    }

    public ICommandListener getHandle() {
        return handle;
    }
}
