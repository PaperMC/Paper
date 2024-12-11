package io.papermc.paper.plugin.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class NamespaceChecker {

    private static final String[] QUICK_INVALID_NAMESPACES = {
        "net.minecraft.",
        "org.bukkit.",
        "io.papermc.paper.",
        "com.destroystokoyo.paper."
    };

    /**
     * Used for a variety of namespaces that shouldn't be resolved and should instead be moved to
     * other classloaders. We can assume this because only plugins should be using this classloader.
     *
     * @param name namespace
     */
    public static void validateNameSpaceForClassloading(@NotNull String name) throws ClassNotFoundException {
        if (!isValidNameSpace(name)) {
            throw new ClassNotFoundException(name);
        }
    }

    public static boolean isValidNameSpace(@NotNull String name) {
        for (String string : QUICK_INVALID_NAMESPACES) {
            if (name.startsWith(string)) {
                return false;
            }
        }

        return true;
    }
}
