package org.bukkit.conversations;

import java.util.Set;
import java.util.UUID;
import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 */
public class FakeConversable implements Conversable {
    public String lastSentMessage;
    public Conversation begunConversation;
    public Conversation abandonedConversation;
    public ConversationAbandonedEvent abandonedConversationEvent;

    @Override
    public boolean isConversing() {
        return false;
    }

    @Override
    public void acceptConversationInput(String input) {

    }

    @Override
    public boolean beginConversation(Conversation conversation) {
        begunConversation = conversation;
        conversation.outputNextPrompt();
        return true;
    }

    @Override
    public void abandonConversation(Conversation conversation) {
        abandonedConversation = conversation;
    }

    @Override
    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        abandonedConversation = conversation;
        abandonedConversationEvent = details;
    }

    @Override
    public void sendRawMessage(String message) {
        lastSentMessage = message;
    }

    @Override
    public void sendRawMessage(@Nullable UUID sender, @NotNull String message) {
        this.sendRawMessage(message);
    }

    public Server getServer() {
        return null;
    }

    public String getName() {
        return null;
    }

    public boolean isPermissionSet(String name) {
        return false;
    }

    public boolean isPermissionSet(Permission perm) {
        return false;
    }

    public boolean hasPermission(String name) {
        return false;
    }

    public boolean hasPermission(Permission perm) {
        return false;
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return null;
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return null;
    }

    public void removeAttachment(PermissionAttachment attachment) {

    }

    public void recalculatePermissions() {

    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return null;
    }

    public boolean isOp() {
        return false;
    }

    public void setOp(boolean value) {

    }
}
