package org.bukkit.conversations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class ConversationContextTest {
    @Test
    public void TestFromWhom() {
        Conversable conversable = new FakeConversable();
        ConversationContext context = new ConversationContext(null, conversable, new HashMap<Object, Object>());
        assertEquals(conversable, context.getForWhom());
    }

    @Test
    public void TestPlugin() {
        Conversable conversable = new FakeConversable();
        ConversationContext context = new ConversationContext(null, conversable, new HashMap<Object, Object>());
        assertEquals(null, context.getPlugin());
    }

    @Test
    public void TestSessionData() {
        Conversable conversable = new FakeConversable();
        Map session = new HashMap();
        session.put("key", "value");
        ConversationContext context = new ConversationContext(null, conversable, session);
        assertEquals("value", context.getSessionData("key"));
    }
}
