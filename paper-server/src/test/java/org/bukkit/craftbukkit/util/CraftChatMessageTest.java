package org.bukkit.craftbukkit.util;

import static org.junit.jupiter.api.Assertions.*;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

@Normal
public class CraftChatMessageTest {

    @Test
    public void testSimpleStrings() {
        // These should be able to go from legacy to comp to legacy back without data changing
        this.testString("§fFoo");
        this.testString("§fFoo§f§l"); // Keeps empty format at end
        this.testString("Foo");
        // testString("§r§oFoo"); // Retains reset at start (item names can use this to get rid of italics)
        this.testString("Foo§bBar");
        this.testString("F§loo§b§oBa§b§lr"); // any non color formatting code implies previous color code.
        // So §l at start has no inherited color code, so that's fine, but the one at the end,
        // while Ba§l would work visually, serializing back will include the implied color

        this.testString("F§loo§rBa§lr"); // But if reset was used before.... then it can be standalone
        this.testString("§fFoo§bBar");
        this.testString("§fFoo§bBar§rBaz");
    }

    @Test
    public void testNewLineBehavior() {
        // new line retain should stay as 1 comp
        this.testString("Hello§0\n§rFoo\n§5Test", true);
        this.testString("§0Foo!\n", true);
        this.testString("§0Foo!§0\\n§0\\n§0Bar\n", true);

        // dont retain line returns multiple components
        Component[] components = CraftChatMessage.fromString("Hello§0\n§rFoo\n§5Test");
        assertEquals(3, components.length, "Has 3 components");
        assertEquals("Hello§0", CraftChatMessage.fromComponent(components[0]));
        assertEquals(/*§r*/"Foo", CraftChatMessage.fromComponent(components[1]));
        assertEquals("§5Test", CraftChatMessage.fromComponent(components[2]));
    }

    @Test
    public void testComponents() {
        this.testComponent("Foo§bBar§rBaz", this.create("Foo", "§bBar", "Baz"));
        this.testComponent("§fFoo§bBar§rBaz", this.create("", "§fFoo", "§bBar", "Baz"));
        this.testComponent("§fFoo§bBar§rBaz", this.create("", "§fFoo", "§bBar", "", "Baz"));
        this.testComponent("§fFoo§bBar§rBaz", this.create("§fFoo", "§bBar", "Baz"));
        this.testComponent("Foo§bBar§rBaz", this.create("", "Foo", "§bBar", "Baz"));
        this.testComponent("§fFoo§bBar§rBaz", this.create("§fFoo", "§bBar", "Baz"));
        this.testComponent("F§foo§bBar§rBaz", this.create("F§foo", "§bBar", "Baz"));
    }

    @Test
    public void testPlainText() {
        this.testPlainString("");
        this.testPlainString("Foo§f§mBar§0");
        this.testPlainString("Link to https://www.spigotmc.org/ ...");
        this.testPlainString("Link to http://www.spigotmc.org/ ...");
        this.testPlainString("Link to www.spigotmc.org ...");
    }

    private Component create(String txt, String... rest) {
        MutableComponent cmp = CraftChatMessage.fromString(txt, false)[0].copy();
        for (String s : rest) {
            cmp.append(CraftChatMessage.fromString(s, true)[0]);
        }

        return cmp;
    }

    private void testString(String expected) {
        this.testString(expected, false);
    }

    private void testString(String expected, boolean keepNewLines) {
        this.testString(expected, expected, keepNewLines);
    }

    private void testString(String input, String expected) {
        this.testString(input, expected, false);
    }

    private void testString(String input, String expected, boolean keepNewLines) {
        Component cmp = CraftChatMessage.fromString(input, keepNewLines)[0];
        String actual = CraftChatMessage.fromComponent(cmp);
        assertEquals(expected, actual, "\nComponent: " + cmp + "\n");
    }

    private void testPlainString(String expected) {
        Component component = CraftChatMessage.fromString(expected, false, true)[0];
        String actual = CraftChatMessage.fromComponent(component);
        assertEquals(expected, actual, "fromComponent does not match input: " + component);
        assertFalse(this.containsNonPlainComponent(component), "Non-plain component: " + component);
    }

    private boolean containsNonPlainComponent(Component component) {
        for (Component c : component) {
            if (c.getContents() != PlainTextContents.EMPTY && !(c.getContents() instanceof PlainTextContents)) {
                return true;
            }
        }
        return false;
    }

    private void testComponent(String expected, Component cmp) {
        String actual = CraftChatMessage.fromComponent(cmp);
        assertEquals(expected, actual, "\nComponent: " + cmp + "\n");

        Component expectedCmp = CraftChatMessage.fromString(expected, true)[0];
        String actualExpectedCmp = CraftChatMessage.fromComponent(expectedCmp);
        assertEquals(expected, actualExpectedCmp, "\nComponent: " + expectedCmp + "\n");
    }
}
