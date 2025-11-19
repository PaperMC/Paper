package com.destroystokyo.paper;

import com.google.common.base.Objects;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.jspecify.annotations.NullMarked;

import java.util.StringJoiner;

@NullMarked
public final class PaperSkinParts implements SkinParts {

    private final int raw;

    public PaperSkinParts(final int raw) {
        this.raw = raw;
    }

    private static boolean isPartShown(final int raw, final PlayerModelPart part) {
        return ((byte) raw & part.getMask()) == part.getMask();
    }

    private static int setPartShown(int raw, final PlayerModelPart part, final boolean shown) {
        if (shown) {
            raw |= part.getMask();
        } else {
            raw &= ~part.getMask();
        }
        return raw;
    }

    @Override
    public boolean hasCapeEnabled() {
        return isPartShown(this.raw, PlayerModelPart.CAPE);
    }

    @Override
    public boolean hasJacketEnabled() {
        return isPartShown(this.raw, PlayerModelPart.JACKET);
    }

    @Override
    public boolean hasLeftSleeveEnabled() {
        return isPartShown(this.raw, PlayerModelPart.LEFT_SLEEVE);
    }

    @Override
    public boolean hasRightSleeveEnabled() {
        return isPartShown(this.raw, PlayerModelPart.RIGHT_SLEEVE);
    }

    @Override
    public boolean hasLeftPantsEnabled() {
        return isPartShown(this.raw, PlayerModelPart.LEFT_PANTS_LEG);
    }

    @Override
    public boolean hasRightPantsEnabled() {
        return isPartShown(this.raw, PlayerModelPart.RIGHT_PANTS_LEG);
    }

    @Override
    public boolean hasHatsEnabled() {
        return isPartShown(this.raw, PlayerModelPart.HAT);
    }

    @Override
    public int getRaw() {
        return this.raw;
    }

    @Override
    public SkinParts.Mutable mutableCopy() {
        return new Mutable(this.raw);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperSkinParts that = (PaperSkinParts) o;
        return this.raw == that.raw;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.raw);
    }

    @Override
    public String toString() {
        return toString(this, "PaperSkinParts");
    }

    private static String toString(final SkinParts parts, final String name) {
        return new StringJoiner(", ", name + "[", "]")
            .add("raw=" + parts.getRaw())
            .add("cape=" + parts.hasCapeEnabled())
            .add("jacket=" + parts.hasJacketEnabled())
            .add("leftSleeve=" + parts.hasLeftSleeveEnabled())
            .add("rightSleeve=" + parts.hasRightSleeveEnabled())
            .add("leftPants=" + parts.hasLeftPantsEnabled())
            .add("rightPants=" + parts.hasRightPantsEnabled())
            .add("hats=" + parts.hasHatsEnabled())
            .toString();
    }

    public static final class Mutable implements SkinParts.Mutable {
        private int raw;

        public Mutable(final int raw) {
            this.raw = raw;
        }

        @Override
        public void setCapeEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.CAPE, enabled);
        }

        @Override
        public void setJacketEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.JACKET, enabled);
        }

        @Override
        public void setLeftSleeveEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.LEFT_SLEEVE, enabled);
        }

        @Override
        public void setRightSleeveEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.RIGHT_SLEEVE, enabled);
        }

        @Override
        public void setLeftPantsEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.LEFT_PANTS_LEG, enabled);
        }

        @Override
        public void setRightPantsEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.RIGHT_PANTS_LEG, enabled);
        }

        @Override
        public void setHatsEnabled(final boolean enabled) {
            this.raw = setPartShown(this.raw, PlayerModelPart.HAT, enabled);
        }

        @Override
        public boolean hasCapeEnabled() {
            return isPartShown(this.raw, PlayerModelPart.CAPE);
        }

        @Override
        public boolean hasJacketEnabled() {
            return isPartShown(this.raw, PlayerModelPart.JACKET);
        }

        @Override
        public boolean hasLeftSleeveEnabled() {
            return isPartShown(this.raw, PlayerModelPart.LEFT_SLEEVE);
        }

        @Override
        public boolean hasRightSleeveEnabled() {
            return isPartShown(this.raw, PlayerModelPart.RIGHT_SLEEVE);
        }

        @Override
        public boolean hasLeftPantsEnabled() {
            return isPartShown(this.raw, PlayerModelPart.LEFT_PANTS_LEG);
        }

        @Override
        public boolean hasRightPantsEnabled() {
            return isPartShown(this.raw, PlayerModelPart.RIGHT_PANTS_LEG);
        }

        @Override
        public boolean hasHatsEnabled() {
            return isPartShown(this.raw, PlayerModelPart.HAT);
        }

        @Override
        public int getRaw() {
            return this.raw;
        }

        @Override
        public SkinParts immutableCopy() {
            return new PaperSkinParts(this.raw);
        }

        @Override
        public Mutable mutableCopy() {
            return new PaperSkinParts.Mutable(this.raw);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PaperSkinParts.Mutable that = (PaperSkinParts.Mutable) o;
            return this.raw == that.raw;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(this.raw);
        }

        @Override
        public String toString() {
            return PaperSkinParts.toString(this, "PaperSkinParts.Mutable");
        }
    }
}
