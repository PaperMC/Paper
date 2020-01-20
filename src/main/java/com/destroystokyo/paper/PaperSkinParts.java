package com.destroystokyo.paper;

import com.google.common.base.Objects;

import java.util.StringJoiner;

public class PaperSkinParts implements SkinParts {

    private final int raw;

    public PaperSkinParts(int raw) {
        this.raw = raw;
    }

    public boolean hasCapeEnabled() {
        return (raw & 1) == 1;
    }

    public boolean hasJacketEnabled() {
        return (raw >> 1 & 1) == 1;
    }

    public boolean hasLeftSleeveEnabled() {
        return (raw >> 2 & 1) == 1;
    }

    public boolean hasRightSleeveEnabled() {
        return (raw >> 3 & 1) == 1;
    }

    public boolean hasLeftPantsEnabled() {
        return (raw >> 4 & 1) == 1;
    }

    public boolean hasRightPantsEnabled() {
        return (raw >> 5 & 1) == 1;
    }

    public boolean hasHatsEnabled() {
        return (raw >> 6 & 1) == 1;
    }

    @Override
    public int getRaw() {
        return raw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperSkinParts that = (PaperSkinParts) o;
        return raw == that.raw;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(raw);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PaperSkinParts.class.getSimpleName() + "[", "]")
            .add("raw=" + raw)
            .add("cape=" + hasCapeEnabled())
            .add("jacket=" + hasJacketEnabled())
            .add("leftSleeve=" + hasLeftSleeveEnabled())
            .add("rightSleeve=" + hasRightSleeveEnabled())
            .add("leftPants=" + hasLeftPantsEnabled())
            .add("rightPants=" + hasRightPantsEnabled())
            .add("hats=" + hasHatsEnabled())
            .toString();
    }
}
