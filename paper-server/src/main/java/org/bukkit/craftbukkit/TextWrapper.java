package org.bukkit.craftbukkit;

import org.bukkit.ChatColor;

public class TextWrapper {
    private static final int CHAT_STRING_LENGTH = 119;

    public static String[] wrapText(final String text) {
        return insertLineBreaks(text).split("\n");
    }

    public static String insertLineBreaks(String input) {
        if (input.length() <= CHAT_STRING_LENGTH) return input;

        String head = input.substring(0, CHAT_STRING_LENGTH);
        String tail = ChatColor.getLastColors(head) + input.substring(CHAT_STRING_LENGTH + (input.charAt(CHAT_STRING_LENGTH) == ' ' ? 1 : 0));

        return head + "\n" + insertLineBreaks(tail);
    }
}
