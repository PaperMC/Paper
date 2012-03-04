package org.bukkit.conversations;

import org.bukkit.Server;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.Set;

/**
 */
public class FakeConversable implements Conversable {
    public String lastSentMessage;
    public Conversation begunConversation;
    public Conversation abandonedConverstion;
    public ConversationAbandonedEvent abandonedConversationEvent;
    
    public boolean isConversing() {
        return false;   
    }

    public void acceptConversationInput(String input) {
         
    }

    public boolean beginConversation(Conversation conversation) {
        begunConversation = conversation;
        conversation.outputNextPrompt();
        return true;
    }

    public void abandonConversation(Conversation conversation) {
        abandonedConverstion = conversation;
    }

    public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        abandonedConverstion = conversation;
        abandonedConversationEvent = details;
    }

    public void sendRawMessage(String message) {
        lastSentMessage = message;     
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
