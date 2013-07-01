package org.bukkit.craftbukkit.command;

import java.lang.reflect.Method;

import net.minecraft.server.ChatMessage;
import net.minecraft.server.ChunkCoordinates;
import net.minecraft.server.ICommandListener;
import net.minecraft.server.LocaleLanguage;
import net.minecraft.server.World;

import org.bukkit.command.CommandSender;

public class ServerCommandListener implements ICommandListener {
    private final CommandSender commandSender;
    private final String prefix;

    public ServerCommandListener(CommandSender commandSender) {
        this.commandSender = commandSender;
        String[] parts = commandSender.getClass().getName().split("\\.");
        this.prefix = parts[parts.length - 1];
    }

    public void sendMessage(ChatMessage chatmessage) {
        this.commandSender.sendMessage(chatmessage.toString());
    }

    public CommandSender getSender() {
        return commandSender;
    }

    public String getName() {
        try {
            Method getName = commandSender.getClass().getMethod("getName");

            return (String) getName.invoke(commandSender);
        } catch (Exception e) {}

        return this.prefix;
    }

    public boolean a(int i, String s) {
        return true;
    }

    public ChunkCoordinates b() {
        return new ChunkCoordinates(0, 0, 0);
    }

    public World f_() {
        return null;
    }
}
