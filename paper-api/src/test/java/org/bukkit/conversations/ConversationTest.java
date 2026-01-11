package org.bukkit.conversations;

import static org.junit.jupiter.api.Assertions.*;
import org.bukkit.plugin.BukkitTestPlugin;
import org.junit.jupiter.api.Test;

/**
 */
public class ConversationTest {

    @Test
    public void testBaseConversationFlow() {
        FakeConversable forWhom = new FakeConversable();
        Conversation conversation = new Conversation(null, forWhom, new FirstPrompt());

        // Conversation not yet begun
        assertNull(forWhom.lastSentMessage);
        assertEquals(conversation.getForWhom(), forWhom);
        assertTrue(conversation.isModal());

        // Begin the conversation
        conversation.begin();
        assertEquals("FirstPrompt", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.begunConversation);

        // Send the first input
        conversation.acceptInput("FirstInput");
        assertEquals("SecondPrompt", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.abandonedConversation);
    }

    @Test
    public void testConversationFactory() {
        FakeConversable forWhom = new FakeConversable();
        NullConversationPrefix prefix = new NullConversationPrefix();
        ConversationFactory factory = new ConversationFactory(new BukkitTestPlugin("Test"))
                .withFirstPrompt(new FirstPrompt())
                .withModality(false)
                .withPrefix(prefix);
        Conversation conversation = factory.buildConversation(forWhom);

        // Conversation not yet begun
        assertNull(forWhom.lastSentMessage);
        assertEquals(conversation.getForWhom(), forWhom);
        assertFalse(conversation.isModal());
        assertEquals(conversation.getPrefix(), prefix);

        // Begin the conversation
        conversation.begin();
        assertEquals("FirstPrompt", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.begunConversation);

        // Send the first input
        conversation.acceptInput("FirstInput");
        assertEquals("SecondPrompt", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.abandonedConversation);
    }

    @Test
    public void testEscapeSequence() {
        FakeConversable forWhom = new FakeConversable();
        Conversation conversation = new Conversation(null, forWhom, new FirstPrompt());
        conversation.addConversationCanceller(new ExactMatchConversationCanceller("bananas"));

        // Begin the conversation
        conversation.begin();
        assertEquals("FirstPrompt", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.begunConversation);

        // Send the first input
        conversation.acceptInput("bananas");
        assertEquals("bananas", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.abandonedConversation);
    }

    @Test
    public void testNotPlayer() {
        FakeConversable forWhom = new FakeConversable();
        ConversationFactory factory = new ConversationFactory(new BukkitTestPlugin("Test"))
                .thatExcludesNonPlayersWithMessage("bye");
        Conversation conversation = factory.buildConversation(forWhom);

        // Begin the conversation
        conversation.begin();
        assertEquals("bye", forWhom.lastSentMessage);
        assertEquals(conversation, forWhom.begunConversation);
        assertEquals(conversation, forWhom.abandonedConversation);
    }

    private class FirstPrompt extends StringPrompt {

        @Override
        public String getPromptText(ConversationContext context) {
            return "FirstPrompt";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String input) {
            assertEquals("FirstInput", input);
            context.setSessionData("data", 10);
            return new SecondPrompt();
        }
    }

    private class SecondPrompt extends MessagePrompt {

        @Override
        protected Prompt getNextPrompt(ConversationContext context) {
            return Prompt.END_OF_CONVERSATION;
        }

        @Override
        public String getPromptText(ConversationContext context) {
            // Assert that session data passes from one prompt to the next
            assertEquals(context.getSessionData("data"), 10);
            return "SecondPrompt";
        }
    }
}
