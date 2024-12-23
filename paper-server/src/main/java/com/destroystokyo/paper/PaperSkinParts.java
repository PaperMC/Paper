package com.destroystokyo.paper;

import com.google.common.base.Objects;

import java.util.StringJoiner;
import net.minecraft.world.entity.player.PlayerModelPart;

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

    public static SkinParts.Builder builder(){
        return new Builder();
    }

    public static class Builder implements SkinParts.Builder {

        private boolean cape;
        private boolean jacket;
        private boolean leftSleeve;
        private boolean rightSleeve;
        private boolean leftPants;
        private boolean rightPants;
        private boolean hats;

        private static final int CAPE = PlayerModelPart.CAPE.getMask();
        private static final int JACKET = PlayerModelPart.JACKET.getMask();
        private static final int LEFT_SLEEVE = PlayerModelPart.LEFT_SLEEVE.getMask();
        private static final int RIGHT_SLEEVE = PlayerModelPart.RIGHT_SLEEVE.getMask();
        private static final int LEFT_PANTS = PlayerModelPart.LEFT_PANTS_LEG.getMask();
        private static final int RIGHT_PANTS = PlayerModelPart.RIGHT_PANTS_LEG.getMask();
        private static final int HAT = PlayerModelPart.HAT.getMask();

        @Override
        public @org.jetbrains.annotations.NotNull Builder withCape(boolean cape) {
            this.cape = cape;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withJacket(boolean jacket) {
            this.jacket = jacket;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withLeftSleeve(boolean leftSleeve) {
            this.leftSleeve = leftSleeve;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withRightSleeve(boolean rightSleeve) {
            this.rightSleeve = rightSleeve;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withLeftPants(boolean leftPants) {
            this.leftPants = leftPants;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withRightPants(boolean rightPants) {
            this.rightPants = rightPants;
            return this;
        }

        @Override
        public @org.jetbrains.annotations.NotNull Builder withHat(boolean hat) {
            this.hats = hat;
            return this;
        }

        public @org.jetbrains.annotations.NotNull SkinParts build() {
            int raw = 0;
            if (cape) raw |= CAPE;
            if (jacket) raw |= JACKET;
            if (leftSleeve) raw |= LEFT_SLEEVE;
            if (rightSleeve) raw |= RIGHT_SLEEVE;
            if (leftPants) raw |= LEFT_PANTS;
            if (rightPants) raw |= RIGHT_PANTS;
            if (hats) raw |= HAT;
            return new PaperSkinParts(raw);
        }
    }
}
