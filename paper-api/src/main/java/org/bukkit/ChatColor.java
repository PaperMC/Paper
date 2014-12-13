package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * All supported color values for chat
 */
public enum ChatColor {
    /**
     * Represents black
     */
    BLACK('0', 0x00) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.BLACK;
        }
    },
    /**
     * Represents dark blue
     */
    DARK_BLUE('1', 0x1) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_BLUE;
        }
    },
    /**
     * Represents dark green
     */
    DARK_GREEN('2', 0x2) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_GREEN;
        }
    },
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA('3', 0x3) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_AQUA;
        }
    },
    /**
     * Represents dark red
     */
    DARK_RED('4', 0x4) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_RED;
        }
    },
    /**
     * Represents dark purple
     */
    DARK_PURPLE('5', 0x5) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_PURPLE;
        }
    },
    /**
     * Represents gold
     */
    GOLD('6', 0x6) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.GOLD;
        }
    },
    /**
     * Represents gray
     */
    GRAY('7', 0x7) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.GRAY;
        }
    },
    /**
     * Represents dark gray
     */
    DARK_GRAY('8', 0x8) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.DARK_GRAY;
        }
    },
    /**
     * Represents blue
     */
    BLUE('9', 0x9) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.BLUE;
        }
    },
    /**
     * Represents green
     */
    GREEN('a', 0xA) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.GREEN;
        }
    },
    /**
     * Represents aqua
     */
    AQUA('b', 0xB) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.AQUA;
        }
    },
    /**
     * Represents red
     */
    RED('c', 0xC) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.RED;
        }
    },
    /**
     * Represents light purple
     */
    LIGHT_PURPLE('d', 0xD) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.LIGHT_PURPLE;
        }
    },
    /**
     * Represents yellow
     */
    YELLOW('e', 0xE) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.YELLOW;
        }
    },
    /**
     * Represents white
     */
    WHITE('f', 0xF) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.WHITE;
        }
    },
    /**
     * Represents magical characters that change around randomly
     */
    MAGIC('k', 0x10, true) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.MAGIC;
        }
    },
    /**
     * Makes the text bold.
     */
    BOLD('l', 0x11, true) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.BOLD;
        }
    },
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH('m', 0x12, true) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.STRIKETHROUGH;
        }
    },
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE('n', 0x13, true) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.UNDERLINE;
        }
    },
    /**
     * Makes the text italic.
     */
    ITALIC('o', 0x14, true) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.ITALIC;
        }
    },
    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r', 0x15) {
        @NotNull
        @Override
        public net.md_5.bungee.api.ChatColor asBungee() {
            return net.md_5.bungee.api.ChatColor.RESET;
        }
    };

    /**
     * The special character which prefixes all chat colour codes. Use this if
     * you need to dynamically convert colour codes from your custom format.
     */
    public static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-ORX]");

    private final int intCode;
    private final char code;
    private final boolean isFormat;
    private final String toString;
    private static final Map<Integer, ChatColor> BY_ID = Maps.newHashMap();
    private static final Map<Character, ChatColor> BY_CHAR = Maps.newHashMap();

    private ChatColor(char code, int intCode) {
        this(code, intCode, false);
    }

    private ChatColor(char code, int intCode, boolean isFormat) {
        this.code = code;
        this.intCode = intCode;
        this.isFormat = isFormat;
        this.toString = new String(new char[] {COLOR_CHAR, code});
    }

    @NotNull
    public net.md_5.bungee.api.ChatColor asBungee() {
        return net.md_5.bungee.api.ChatColor.RESET;
    };

    /**
     * Gets the char value associated with this color
     *
     * @return A char value of this color code
     */
    public char getChar() {
        return code;
    }

    @NotNull
    @Override
    public String toString() {
        return toString;
    }

    /**
     * Checks if this code is a format code as opposed to a color code.
     *
     * @return whether this ChatColor is a format code
     */
    public boolean isFormat() {
        return isFormat;
    }

    /**
     * Checks if this code is a color code as opposed to a format code.
     *
     * @return whether this ChatColor is a color code
     */
    public boolean isColor() {
        return !isFormat && this != RESET;
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code,
     *     or null if it doesn't exist
     */
    @Nullable
    public static ChatColor getByChar(char code) {
        return BY_CHAR.get(code);
    }

    /**
     * Gets the color represented by the specified color code
     *
     * @param code Code to check
     * @return Associative {@link org.bukkit.ChatColor} with the given code,
     *     or null if it doesn't exist
     */
    @Nullable
    public static ChatColor getByChar(@NotNull String code) {
        Preconditions.checkArgument(code != null, "Code cannot be null");
        Preconditions.checkArgument(code.length() > 0, "Code must have at least one char");

        return BY_CHAR.get(code.charAt(0));
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    @Contract("!null -> !null; null -> null")
    @Nullable
    public static String stripColor(@Nullable final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    /**
     * Translates a string using an alternate color code character into a
     * string that uses the internal ChatColor.COLOR_CODE color code
     * character. The alternate color code character will only be replaced if
     * it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
     *
     * @param altColorChar The alternate color code character to replace. Ex: {@literal &}
     * @param textToTranslate Text containing the alternate color code character.
     * @return Text containing the ChatColor.COLOR_CODE color code character.
     */
    @NotNull
    public static String translateAlternateColorCodes(char altColorChar, @NotNull String textToTranslate) {
        Preconditions.checkArgument(textToTranslate != null, "Cannot translate null text");

        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Gets the ChatColors used at the end of the given input string.
     *
     * @param input Input string to retrieve the colors from.
     * @return Any remaining ChatColors to pass onto the next line.
     */
    @NotNull
    public static String getLastColors(@NotNull String input) {
        Preconditions.checkArgument(input != null, "Cannot get last colors from null text");

        String result = "";
        int length = input.length();

        // Search backwards from the end as it is faster
        for (int index = length - 1; index > -1; index--) {
            char section = input.charAt(index);
            if (section == COLOR_CHAR && index < length - 1) {

                String hexColor = getHexColor(input, index);
                if (hexColor != null) {
                    // We got a hex color
                    result = hexColor + result;
                    break;
                }

                // It is not a hex color, check normal color
                char c = input.charAt(index + 1);
                ChatColor color = getByChar(c);

                if (color != null) {
                    result = color.toString() + result;

                    // Once we find a color or reset we can stop searching
                    if (color.isColor() || color.equals(RESET)) {
                        break;
                    }
                }
            }
        }

        return result;
    }

    @Nullable
    private static String getHexColor(@NotNull String input, int index) {
        // Check for hex color with the format '§x§1§2§3§4§5§6'
        // Our index is currently on the last '§' which means to have a potential hex color
        // The index - 11 must be an 'x' and index - 12 must be a '§'
        // But first check if the string is long enough
        if (index < 12) {
            return null;
        }

        if (input.charAt(index - 11) != 'x' || input.charAt(index - 12) != COLOR_CHAR) {
            return null;
        }

        // We got a potential hex color
        // Now check if every the chars switches between '§' and a hex number
        // First check '§'
        for (int i = index - 10; i <= index; i += 2) {
            if (input.charAt(i) != COLOR_CHAR) {
                return null;
            }
        }

        for (int i = index - 9; i <= (index + 1); i += 2) {
            char toCheck = input.charAt(i);
            if (toCheck < '0' || toCheck > 'f') {
                return null;
            }

            if (toCheck > '9' && toCheck < 'A') {
                return null;
            }

            if (toCheck > 'F' && toCheck < 'a') {
                return null;
            }
        }

        // We got a hex color return it
        return input.substring(index - 12, index + 2);
    }

    static {
        for (ChatColor color : values()) {
            BY_ID.put(color.intCode, color);
            BY_CHAR.put(color.code, color);
        }
    }
}
