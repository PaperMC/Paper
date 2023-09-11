package org.bukkit;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LastChatColorTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {String.format("%c%ctest%c%ctest%c", ChatColor.COLOR_CHAR, ChatColor.RED.getChar(), ChatColor.COLOR_CHAR, ChatColor.ITALIC.getChar(), ChatColor.COLOR_CHAR), ChatColor.RED.toString() + ChatColor.ITALIC},
            {String.format("%c%ctest%c%ctest", ChatColor.COLOR_CHAR, ChatColor.RED.getChar(), ChatColor.COLOR_CHAR, ChatColor.BLUE.getChar()), ChatColor.BLUE.toString()},
            {"§x§1§2§3§4§5§6", "§x§1§2§3§4§5§6"},
            {"§y§1§2§3§4§5§6", "§6"},
            {"§3§4§5§6", "§6"},
            {"Test2§x§1§f§3§4§F§6test§l", "§x§1§f§3§4§F§6§l"},
            {"Test2§x§P§f§3§4§F§6test§l", "§6§l"},
            {"Test2§x§fxf§3§4§F§6test§l", "§6§l"},
            {"Test2§x§1§4§F§6test§l", "§6§l"}
        });
    }

    private final String input;
    private final String expected;

    public LastChatColorTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void testGetLastColors() {
        assertEquals(expected, ChatColor.getLastColors(input));
    }
}
