package org.bukkit.conversations;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class ValidatingPromptTest {

    @Test
    public void TestBooleanPrompt() {
        TestBooleanPrompt prompt = new TestBooleanPrompt();
        assertTrue(prompt.isInputValid(null, "true"));
        assertFalse(prompt.isInputValid(null, "bananas"));
        prompt.acceptInput(null, "true");
        assertTrue(prompt.result);
        prompt.acceptInput(null, "no");
        assertFalse(prompt.result);
    }
    
    @Test
    public void TestFixedSetPrompt() {
        TestFixedSetPrompt prompt = new TestFixedSetPrompt("foo", "bar");
        assertTrue(prompt.isInputValid(null, "foo"));
        assertFalse(prompt.isInputValid(null, "cheese"));
        prompt.acceptInput(null, "foo");
        assertEquals("foo", prompt.result);
    }
    
    @Test
    public void TestNumericPrompt() {
        TestNumericPrompt prompt = new TestNumericPrompt();
        assertTrue(prompt.isInputValid(null, "1010220"));
        assertFalse(prompt.isInputValid(null, "tomato"));
        prompt.acceptInput(null, "1010220");
        assertEquals(1010220, prompt.result);
    }
    
    @Test
    public void TestRegexPrompt() {
        TestRegexPrompt prompt = new TestRegexPrompt("a.c");
        assertTrue(prompt.isInputValid(null, "abc"));
        assertTrue(prompt.isInputValid(null, "axc"));
        assertFalse(prompt.isInputValid(null, "xyz"));
        prompt.acceptInput(null, "abc");
        assertEquals("abc", prompt.result);
    }

    //TODO: TestPlayerNamePrompt()
    
    private class TestBooleanPrompt extends BooleanPrompt {
        public boolean result;
        
        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, boolean input) {
            result = input;
            return null;
        }

        public String getPromptText(ConversationContext context) {
            return null;
        }
    }
    
    private class TestFixedSetPrompt extends FixedSetPrompt {
        public String result;

        public TestFixedSetPrompt(String... fixedSet) {
            super(fixedSet);
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            result = input;
            return null;
        }

        public String getPromptText(ConversationContext context) {
            return null;
        }
    }
    
    private class TestNumericPrompt extends NumericPrompt {
        public Number result;
        
        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
            result = input;
            return null;
        }

        public String getPromptText(ConversationContext context) {
            return null;
        }
    }

    private class TestRegexPrompt extends RegexPrompt {
        public String result;

        public TestRegexPrompt(String pattern) {
            super(pattern);
        }

        @Override
        protected Prompt acceptValidatedInput(ConversationContext context, String input) {
            result = input;
            return null;
        }

        public String getPromptText(ConversationContext context) {
            return null;
        }
    }
}
