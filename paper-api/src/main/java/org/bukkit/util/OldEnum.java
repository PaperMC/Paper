package org.bukkit.util;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Class which holds common methods which are present in an enum.
 *
 * @param <T> the type of the old enum.
 * @deprecated only for backwards compatibility.
 */
@ApiStatus.Internal
@Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
public interface OldEnum<T extends OldEnum<T>> extends Comparable<T> {

    /**
     * @param other to compare to.
     * @return negative if this old enum is lower, zero if equal and positive if
     * higher than the given old enum.
     * @deprecated only for backwards compatibility, old enums can not be
     * compared.
     */
    @Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    @Override
    int compareTo(@NotNull T other);

    /**
     * @return the name of the old enum.
     * @deprecated only for backwards compatibility.
     */
    @NotNull
    @Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    String name();

    /**
     * @return the ordinal of the old enum.
     * @deprecated only for backwards compatibility, it is not guaranteed that
     * an old enum always has the same ordinal.
     */
    @Deprecated(since = "1.21", forRemoval = true) @ApiStatus.ScheduledForRemoval(inVersion = "1.22") // Paper - will be removed via asm-utils
    int ordinal();
}
