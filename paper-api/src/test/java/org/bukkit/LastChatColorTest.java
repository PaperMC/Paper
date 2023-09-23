package org.bukkit;

import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class LastChatColorTest {

    public static Stream<Arguments> data() {
        return Stream.of(
            Arguments.of(String.format("%c%ctest%c%ctest%c", ChatColor.COLOR_CHAR, ChatColor.RED.getChar(), ChatColor.COLOR_CHAR, ChatColor.ITALIC.getChar(), ChatColor.COLOR_CHAR), ChatColor.RED.toString() + ChatColor.ITALIC),
            Arguments.of(String.format("%c%ctest%c%ctest", ChatColor.COLOR_CHAR, ChatColor.RED.getChar(), ChatColor.COLOR_CHAR, ChatColor.BLUE.getChar()), ChatColor.BLUE.toString()),
            Arguments.of("§x§1§2§3§4§5§6", "§x§1§2§3§4§5§6"),
            Arguments.of("§y§1§2§3§4§5§6", "§6"),
            Arguments.of("§3§4§5§6", "§6"),
            Arguments.of("Test2§x§1§f§3§4§F§6test§l", "§x§1§f§3§4§F§6§l"),
            Arguments.of("Test2§x§P§f§3§4§F§6test§l", "§6§l"),
            Arguments.of("Test2§x§fxf§3§4§F§6test§l", "§6§l"),
            Arguments.of("Test2§x§1§4§F§6test§l", "§6§l")
        );
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testGetLastColors(String input, String expected) {
        assertEquals(expected, ChatColor.getLastColors(input));
    }
}
