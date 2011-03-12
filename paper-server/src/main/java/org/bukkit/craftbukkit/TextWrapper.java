package org.bukkit.craftbukkit;

import java.util.regex.Pattern;

public class TextWrapper {
    private static final int[] characterWidths = new int[] {
        1, 9, 9, 8, 8, 8, 8, 7, 9, 8, 9, 9, 8, 9, 9, 9,
        8, 8, 8, 8, 9, 9, 8, 9, 8, 8, 8, 8, 8, 9, 9, 9,
        4, 2, 5, 6, 6, 6, 6, 3, 5, 5, 5, 6, 2, 6, 2, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 5, 6, 5, 6,
        7, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6,
        3, 6, 6, 6, 6, 6, 5, 6, 6, 2, 6, 5, 3, 6, 6, 6,
        6, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 5, 2, 5, 7, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 3, 6, 6,
        6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6,
        6, 3, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 2, 6, 6,
        8, 9, 9, 6, 6, 6, 8, 8, 6, 8, 8, 8, 8, 8, 6, 6,
        9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
        9, 9, 9, 9, 9, 9, 9, 9, 9, 6, 9, 9, 9, 5, 9, 9,
        8, 7, 7, 8, 7, 8, 8, 8, 7, 8, 8, 7, 9, 9, 6, 7,
        7, 7, 7, 7, 9, 6, 7, 8, 7, 6, 6, 9, 7, 6, 7, 1
    };
    private static final int CHAT_WINDOW_WIDTH = 320;
    private static final Pattern pattern = Pattern.compile("\n.*\u00A7", Pattern.DOTALL);

    public static String[] wrapText(final String text) {
        final StringBuilder out = new StringBuilder();
        char colorChar = 'f';
        int lineWidth = 0;
        int lineCount = 0;
        boolean hasColored = true;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '\u00A7' && i < text.length() - 1) {
                colorChar = text.charAt(++i);
                hasColored = false;
                continue;
            } else if (ch >= characterWidths.length) {
                ch = (char) (characterWidths.length - 1);
            }
            final int width = characterWidths[(int) ch];
            if (lineWidth + width >= CHAT_WINDOW_WIDTH) {
                out.append('\n');
                lineCount++;
                if (colorChar != 'f') {
                    out.append('\u00A7');
                    out.append(colorChar);
                }
                out.append(ch);
                lineWidth = width;
            } else {
                if (!hasColored) {
                    out.append('\u00A7');
                    out.append(colorChar);
                    hasColored = true;
                }
                out.append(ch);
                lineWidth += width;
            }
        }

        // See if we need to split the string
        String result = out.toString();
        if (pattern.matcher(result).find()) return result.split("\n");

        if (lineCount > 0) result.replace("\n", "");
        return new String[] {result};
    }
}
