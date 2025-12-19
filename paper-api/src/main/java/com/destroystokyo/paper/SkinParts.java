package com.destroystokyo.paper;

import io.papermc.paper.InternalAPIBridge;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Represents the different parts of a player's skin that can be toggled on or off.
 */
@NullMarked
public interface SkinParts {

    /**
     * Creates a new instance of {@link SkinParts} with all parts enabled.
     *
     * @return a new {@link SkinParts} instance
     */
    @Contract(value = "-> new", pure = true)
    static SkinParts.Mutable allParts() {
        return InternalAPIBridge.get().allSkinParts();
    }

    boolean hasCapeEnabled();

    boolean hasJacketEnabled();

    boolean hasLeftSleeveEnabled();

    boolean hasRightSleeveEnabled();

    boolean hasLeftPantsEnabled();

    boolean hasRightPantsEnabled();

    boolean hasHatsEnabled();

    int getRaw();

    Mutable mutableCopy();

    interface Mutable extends SkinParts {

        void setCapeEnabled(boolean enabled);

        void setJacketEnabled(boolean enabled);

        void setLeftSleeveEnabled(boolean enabled);

        void setRightSleeveEnabled(boolean enabled);

        void setLeftPantsEnabled(boolean enabled);

        void setRightPantsEnabled(boolean enabled);

        void setHatsEnabled(boolean enabled);

        SkinParts immutableCopy();
    }
}
