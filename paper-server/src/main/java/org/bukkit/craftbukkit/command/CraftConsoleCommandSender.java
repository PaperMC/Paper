package org.bukkit.craftbukkit.command;

import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.craftbukkit.conversations.ConversationTracker;

/**
 * Represents CLI input from a console
 */
public class CraftConsoleCommandSender extends ServerCommandSender implements ConsoleCommandSender {

    protected final ConversationTracker conversationTracker = new ConversationTracker();

    protected CraftConsoleCommandSender() {
        super();
    }

    @Override
    public void sendMessage(String message) {
        this.sendRawMessage(message);
    }

    @Override
    public void sendRawMessage(String message) {
        System.out.println(ChatColor.stripColor(message));
    }

    @Override
    public void sendRawMessage(UUID sender, String message) {
      this.sendRawMessage(message); // Console doesn't know of senders
    }

    @Override
    public void sendMessage(String... messages) {
        for (String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public String getName() {
        return "CONSOLE";
    }

    // Paper start
    @Override
    public net.kyori.adventure.text.Component name() {
        return net.kyori.adventure.text.Component.text(this.getName());
    }
    // Paper end

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        return this.conversationTracker.beginConversation(conversation);
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        this.conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        this.conversationTracker.abandonConversation(conversation, details);
    }

    @Override
    public void acceptConversationInput(String input) {
        this.conversationTracker.acceptConversationInput(input);
    }

    @Override
    public boolean isConversing() {
        return this.conversationTracker.isConversing();
    }

    // Paper start
    @Override
    public void sendMessage(final net.kyori.adventure.identity.Identity identity, final net.kyori.adventure.text.Component message, final net.kyori.adventure.audience.MessageType type) {
        this.sendRawMessage(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(message));
    }

    @Override
    public boolean hasPermission(String name) {
        return io.papermc.paper.configuration.GlobalConfiguration.get().console.hasAllPermissions || super.hasPermission(name);
    }

    @Override
    public boolean hasPermission(org.bukkit.permissions.Permission perm) {
        return io.papermc.paper.configuration.GlobalConfiguration.get().console.hasAllPermissions || super.hasPermission(perm);
    }
    // Paper end
}
