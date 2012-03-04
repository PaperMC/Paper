package org.bukkit.craftbukkit.conversations;

import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;

import java.util.LinkedList;

/**
 */
public class ConversationTracker {

    private LinkedList<Conversation> conversationQueue = new LinkedList<Conversation>();

    public synchronized boolean beginConversation(Conversation conversation) {
        if (!conversationQueue.contains(conversation)) {
            conversationQueue.addLast(conversation);
            if (conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }
        return true;
    }

    public synchronized void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (!conversationQueue.isEmpty()) {
            if (conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }
            if (conversationQueue.contains(conversation)) {
                conversationQueue.remove(conversation);
            }
            if (!conversationQueue.isEmpty()) {
                conversationQueue.getFirst().outputNextPrompt();
            }
        }
    }

    public synchronized void abandonAllConversations() {

        LinkedList<Conversation> oldQueue = conversationQueue;
        conversationQueue = new LinkedList<Conversation>();
        for(Conversation conversation : oldQueue) {
            conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
        }
    }

    public synchronized void acceptConversationInput(String input) {
        if (isConversing()) {
            conversationQueue.getFirst().acceptInput(input);
        }
    }

    public synchronized boolean isConversing() {
        return !conversationQueue.isEmpty();
    }

    public synchronized boolean isConversingModaly() {
        return isConversing() && conversationQueue.getFirst().isModal();
    }
}
