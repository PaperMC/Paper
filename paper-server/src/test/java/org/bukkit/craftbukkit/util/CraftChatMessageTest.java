package org.bukkit.craftbukkit.util;

import static org.junit.Assert.*;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import org.junit.Test;

public class CraftChatMessageTest {

    @Test
    public void testSimpleStrings() {
        // These should be able to go from legacy to comp to legacy back without data changing
        testString("§fFoo");
        testString("§fFoo§f§l"); // Keeps empty format at end
        testString("Foo");
        // testString("§r§oFoo"); // Retains reset at start (item names can use this to get rid of italics)
        testString("Foo§bBar");
        testString("F§loo§b§oBa§b§lr"); // any non color formatting code implies previous color code.
        // So §l at start has no inherited color code, so that's fine, but the one at the end,
        // while Ba§l would work visually, serializing back will include the implied color

        testString("F§loo§rBa§lr"); // But if reset was used before.... then it can be standalone
        testString("§fFoo§bBar");
        testString("§fFoo§bBar§rBaz");
    }

    @Test
    public void testNewLineBehavior() {
        // new line retain should stay as 1 comp
        testString("Hello§0\n§rFoo\n§5Test", true);
        testString("§0Foo!\n", true);
        testString("§0Foo!§0\\n§0\\n§0Bar\n", true);

        // dont retain line returns multiple components
        IChatBaseComponent[] components = CraftChatMessage.fromString("Hello§0\n§rFoo\n§5Test");
        assertEquals("Has 3 components", 3, components.length);
        assertEquals("Hello§0", CraftChatMessage.fromComponent(components[0]));
        assertEquals(/*§r*/"Foo", CraftChatMessage.fromComponent(components[1]));
        assertEquals("§5Test", CraftChatMessage.fromComponent(components[2]));
    }

    @Test
    public void testComponents() {
        testComponent("Foo§bBar§rBaz", create("Foo", "§bBar", "Baz"));
        testComponent("§fFoo§bBar§rBaz", create("", "§fFoo", "§bBar", "Baz"));
        testComponent("§fFoo§bBar§rBaz", create("", "§fFoo", "§bBar", "", "Baz"));
        testComponent("§fFoo§bBar§rBaz", create("§fFoo", "§bBar", "Baz"));
        testComponent("Foo§bBar§rBaz", create("", "Foo", "§bBar", "Baz"));
        testComponent("§fFoo§bBar§rBaz", create("§fFoo", "§bBar", "Baz"));
        testComponent("F§foo§bBar§rBaz", create("F§foo", "§bBar", "Baz"));
    }

    @Test
    public void testPlainText() {
        testPlainString("");
        testPlainString("Foo§f§mBar§0");
        testPlainString("Link to https://www.spigotmc.org/ ...");
        testPlainString("Link to http://www.spigotmc.org/ ...");
        testPlainString("Link to www.spigotmc.org ...");
    }

    private IChatBaseComponent create(String txt, String... rest) {
        IChatMutableComponent cmp = CraftChatMessage.fromString(txt, false)[0].mutableCopy();
        for (String s : rest) {
            cmp.addSibling(CraftChatMessage.fromString(s, true)[0]);
        }

        return cmp;
    }

    private void testString(String expected) {
        testString(expected, false);
    }

    private void testString(String expected, boolean keepNewLines) {
        testString(expected, expected, keepNewLines);
    }

    private void testString(String input, String expected) {
        testString(input, expected, false);
    }

    private void testString(String input, String expected, boolean keepNewLines) {
        IChatBaseComponent cmp = CraftChatMessage.fromString(input, keepNewLines)[0];
        String actual = CraftChatMessage.fromComponent(cmp);
        assertEquals("\nComponent: " + cmp + "\n", expected, actual);
    }

    private void testPlainString(String expected) {
        IChatBaseComponent component = CraftChatMessage.fromString(expected, false, true)[0];
        String actual = CraftChatMessage.fromComponent(component);
        assertEquals("fromComponent does not match input: " + component, expected, actual);
        assertTrue("Non-plain component: " + component, !containsNonPlainComponent(component));
    }

    private boolean containsNonPlainComponent(IChatBaseComponent component) {
        for (IChatBaseComponent c : component) {
            if (!(c instanceof ChatComponentText)) {
                return true;
            }
        }
        return false;
    }

    private void testComponent(String expected, IChatBaseComponent cmp) {
        String actual = CraftChatMessage.fromComponent(cmp);
        assertEquals("\nComponent: " + cmp + "\n", expected, actual);

        IChatBaseComponent expectedCmp = CraftChatMessage.fromString(expected, true)[0];
        String actualExpectedCmp = CraftChatMessage.fromComponent(expectedCmp);
        assertEquals("\nComponent: " + expectedCmp + "\n", expected, actualExpectedCmp);
    }
}
