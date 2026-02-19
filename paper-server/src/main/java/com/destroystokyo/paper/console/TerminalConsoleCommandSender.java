package com.destroystokyo.paper.console;

import net.kyori.adventure.chat.ChatType;
import net.kyori.adventure.chat.SignedMessage;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.logging.log4j.LogManager;
import org.bukkit.craftbukkit.command.CraftConsoleCommandSender;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class TerminalConsoleCommandSender extends CraftConsoleCommandSender {

    private static final ComponentLogger LOGGER = ComponentLogger.logger(LogManager.getRootLogger().getName());

    @Override
    public void sendRawMessage(String message) {
        final Component msg = LegacyComponentSerializer.legacySection().deserialize(message);
        this.sendMessage(msg);
    }

    @Override
    public void sendMessage(Component message) {
        LOGGER.info(message);
    }

    @Override
    public void sendMessage(final Component message, final ChatType.Bound boundChatType) {
        this.sendMessage(message);
    }

    @Override
    public void sendMessage(final SignedMessage signedMessage, final ChatType.Bound boundChatType) {
        if (signedMessage.unsignedContent() != null) {
            this.sendMessage(signedMessage.unsignedContent());
        } else {
            LOGGER.info(signedMessage.message());
        }
    }
}
