package io.papermc.generator.utils;

import java.util.Optional;
import org.apache.commons.lang3.math.NumberUtils;
import java.util.Comparator;
import java.util.Locale;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Formatting {

    private static final Pattern ILLEGAL_FIELD_CHARACTERS = Pattern.compile("[.-/]");

    public static String formatKeyAsField(String path) {
        return ILLEGAL_FIELD_CHARACTERS.matcher(path.toUpperCase(Locale.ENGLISH)).replaceAll("_");
    }

    public static String formatTagFieldPrefix(String name, ResourceKey<? extends Registry<?>> registryKey) {
        if (registryKey == Registries.BLOCK) {
            return "";
        }
        if (registryKey == Registries.GAME_EVENT) {
            return "GAME_EVENT_"; // Paper doesn't follow the format (should be GAME_EVENTS_) (pre 1.21)
        }
        return name.toUpperCase(Locale.ENGLISH) + "_";
    }

    public static Optional<String> formatTagKey(String tagDir, String resourcePath) {
        int tagsIndex = resourcePath.indexOf(tagDir);
        int dotIndex = resourcePath.lastIndexOf('.');
        if (tagsIndex == -1 || dotIndex == -1) {
            return Optional.empty();
        }
        return Optional.of(resourcePath.substring(tagsIndex + tagDir.length() + 1, dotIndex)); // namespace/tags/registry_key/[tag_key].json
    }

    public static String quoted(String value) {
        return "\"" + value + "\"";
    }

    public static String[] asCode(int... values) {
        return IntStream.of(values).mapToObj(Integer::toString).toArray(String[]::new);
    }

    /**
     * Strips a word from a camelCase name, ensuring the word is properly part of the camelCase structure.
     * This implementation avoids creating unnecessary intermediate strings by using StringBuilder.
     *
     * @param name The original name in camelCase
     * @param word The word to strip
     * @param onlyOnce True to strip only the first occurrence, false to strip all occurrences
     * @return The name with the specified word removed
     */
    public static String stripWordOfCamelCaseName(String name, String word, boolean onlyOnce) {
        StringBuilder result = new StringBuilder(name.length());
        int startIndex = 0;
        int currentIndex = 0;

        while ((currentIndex = name.indexOf(word, startIndex)) != -1) {
            // Check if this occurrence meets our criteria
            boolean validOccurrence = true;

            if (currentIndex > 0 && !Character.isLowerCase(name.charAt(currentIndex - 1))) {
                validOccurrence = false;
            }

            if (currentIndex + word.length() < name.length() &&
                !Character.isUpperCase(name.charAt(currentIndex + word.length()))) {
                validOccurrence = false;
            }

            if (validOccurrence) {
                // Append text before this occurrence
                result.append(name, startIndex, currentIndex);
                // Skip the word
                startIndex = currentIndex + word.length();

                if (onlyOnce) {
                    break;
                }
            } else {
                // Move past this occurrence
                startIndex = currentIndex + 1;
            }
        }

        // Append any remaining text
        if (startIndex < name.length()) {
            result.append(name, startIndex, name.length());
        }

        return result.toString();
    }

    public static final Comparator<String> ALPHABETIC_KEY_ORDER = alphabeticKeyOrder(path -> path);

    public static <T> Comparator<T> alphabeticKeyOrder(Function<T, String> mapper) {
        return (o1, o2) -> {
            String path1 = mapper.apply(o1);
            String path2 = mapper.apply(o2);

            OptionalInt trailingInt1 = tryParseTrailingInt(path1);
            OptionalInt trailingInt2 = tryParseTrailingInt(path2);

            if (trailingInt1.isPresent() && trailingInt2.isPresent()) {
                return Integer.compare(trailingInt1.getAsInt(), trailingInt2.getAsInt());
            }

            return path1.compareTo(path2);
        };
    }

    private static OptionalInt tryParseTrailingInt(String path) {
        int delimiterIndex = path.lastIndexOf('_');
        if (delimiterIndex != -1) {
            String score = path.substring(delimiterIndex + 1);
            if (NumberUtils.isDigits(score)) {
                return OptionalInt.of(Integer.parseInt(score));
            }
        }
        return OptionalInt.empty();
    }

    private Formatting() {
    }
}
