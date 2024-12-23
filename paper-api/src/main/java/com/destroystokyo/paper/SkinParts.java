package com.destroystokyo.paper;

import org.jspecify.annotations.NullMarked;

public interface SkinParts {

    boolean hasCapeEnabled();

    boolean hasJacketEnabled();

    boolean hasLeftSleeveEnabled();

    boolean hasRightSleeveEnabled();

    boolean hasLeftPantsEnabled();

    boolean hasRightPantsEnabled();

    boolean hasHatsEnabled();

    int getRaw();

    @NullMarked
    interface Builder {
        Builder withCape(boolean cape);
        Builder withJacket(boolean jacket);
        Builder withLeftSleeve(boolean leftSleeve);
        Builder withRightSleeve(boolean rightSleeve);
        Builder withLeftPants(boolean leftPants);
        Builder withRightPants(boolean rightPants);
        Builder withHat(boolean hat);
        SkinParts build();
    }
}
