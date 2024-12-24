package com.destroystokyo.paper;

public interface SkinParts {

    boolean hasCapeEnabled();

    boolean hasJacketEnabled();

    boolean hasLeftSleeveEnabled();

    boolean hasRightSleeveEnabled();

    boolean hasLeftPantsEnabled();

    boolean hasRightPantsEnabled();

    boolean hasHatsEnabled();

    int getRaw();

    interface Builder {
        @org.jetbrains.annotations.NotNull Builder withCape(boolean cape);
        @org.jetbrains.annotations.NotNull Builder withJacket(boolean jacket);
        @org.jetbrains.annotations.NotNull Builder withLeftSleeve(boolean leftSleeve);
        @org.jetbrains.annotations.NotNull Builder withRightSleeve(boolean rightSleeve);
        @org.jetbrains.annotations.NotNull Builder withLeftPants(boolean leftPants);
        @org.jetbrains.annotations.NotNull Builder withRightPants(boolean rightPants);
        @org.jetbrains.annotations.NotNull Builder withHat(boolean hat);
        @org.jetbrains.annotations.NotNull SkinParts build();
    }
}
