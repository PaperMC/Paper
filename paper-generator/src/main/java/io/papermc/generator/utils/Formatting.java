package io.papermc.generator.utils;

import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.Nullable;

public final class Formatting {

    private static final Pattern ILLEGAL_FIELD_CHARACTERS = Pattern.compile("[.-/]");

    public static String formatKeyAsField(String path) {
        return ILLEGAL_FIELD_CHARACTERS.matcher(path.toUpperCase(Locale.ENGLISH)).replaceAll("_");
    }

    @ApiStatus.Obsolete
    public static String formatTagFieldPrefix(String name, ResourceKey<? extends Registry<?>> registryKey) {
        if (registryKey == Registries.BLOCK) {
            return "";
        }
        if (registryKey == Registries.GAME_EVENT) {
            return "GAME_EVENT_"; // Paper doesn't follow the format (should be GAME_EVENTS_) (pre 1.21)
        }
        return name.toUpperCase(Locale.ENGLISH) + "_";
    }

    public static Optional<String> findTagKeyPath(String tagDir, String resourcePath) {
        int tagsIndex = resourcePath.indexOf(tagDir);
        int dotIndex = resourcePath.lastIndexOf('.');
        if (tagsIndex == -1 || dotIndex == -1) {
            return Optional.empty();
        }
        return Optional.of(resourcePath.substring(tagsIndex + tagDir.length() + 1, dotIndex)); // namespace/tags/registry_key/[tag_key_path].json
    }

    public static String quoted(String value) {
        return "\"" + value + "\"";
    }

    public static String stripInitialWord(String name, String word) { // both ends
        if (name.startsWith(word)) {
            return name.substring(word.length());
        }

        if (name.endsWith(word)) {
            return name.substring(0, name.length() - word.length());
        }

        return name;
    }

    public static String stripInitialWord(String name, String word, boolean fromEnd) {
        if (fromEnd) {
            if (name.endsWith(word)) {
                return name.substring(0, name.length() - word.length());
            }
        } else {
            if (name.startsWith(word)) {
                return name.substring(word.length());
            }
        }
        return name;
    }

    public static final Comparator<Holder.Reference<?>> HOLDER_ORDER = alphabeticKeyOrder(reference -> reference.key().location().getPath());
    public static final Comparator<TagKey<?>> TAG_ORDER = alphabeticKeyOrder(tagKey -> tagKey.location().getPath());

    public static <T> Comparator<T> alphabeticKeyOrder(Function<T, String> pathConverter) {
        return Comparator.comparing(pathConverter, (path1, path2) -> {
            TrailingInt trailingInt1 = tryParseTrailingInt(path1);
            TrailingInt trailingInt2 = tryParseTrailingInt(path2);

            if (trailingInt1 != null && trailingInt2 != null &&
                trailingInt1.prefix().equals(trailingInt2.prefix())) {
                return Integer.compareUnsigned(trailingInt1.value(), trailingInt2.value());
            }

            return path1.compareTo(path2);
        });
    }

    private static @Nullable TrailingInt tryParseTrailingInt(String path) {
        int delimiterIndex = path.lastIndexOf('_');
        if (delimiterIndex != -1) {
            String value = path.substring(delimiterIndex + 1);
            if (NumberUtils.isDigits(value)) {
                String prefix = path.substring(0, delimiterIndex);
                return new TrailingInt(prefix, Integer.parseInt(value));
            }
        }
        return null;
    }

    private record TrailingInt(String prefix, int value) {
    }

    private Formatting() {
    }
}
