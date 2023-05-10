package org.bukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

/**
 * Represents a display entity which is designed to only have a visual function.
 */
public interface Display extends Entity {

    /**
     * Gets the transformation applied to this display.
     *
     * @return the transformation
     */
    @NotNull
    public Transformation getTransformation();

    /**
     * Sets the transformation applied to this display
     *
     * @param transformation the new transformation
     */
    public void setTransformation(@NotNull Transformation transformation);

    /**
     * Sets the raw transformation matrix applied to this display
     *
     * @param transformationMatrix the transformation matrix
     */
    public void setTransformationMatrix(@NotNull Matrix4f transformationMatrix);

    /**
     * Gets the interpolation duration of this display.
     *
     * @return interpolation duration
     */
    public int getInterpolationDuration();

    /**
     * Sets the interpolation duration of this display.
     *
     * @param duration new duration
     */
    public void setInterpolationDuration(int duration);

    /**
     * Gets the view distance/range of this display.
     *
     * @return view range
     */
    public float getViewRange();

    /**
     * Sets the view distance/range of this display.
     *
     * @param range new range
     */
    public void setViewRange(float range);

    /**
     * Gets the shadow radius of this display.
     *
     * @return radius
     */
    public float getShadowRadius();

    /**
     * Sets the shadow radius of this display.
     *
     * @param radius new radius
     */
    public void setShadowRadius(float radius);

    /**
     * Gets the shadow strength of this display.
     *
     * @return shadow strength
     */
    public float getShadowStrength();

    /**
     * Sets the shadow strength of this display.
     *
     * @param strength new strength
     */
    public void setShadowStrength(float strength);

    /**
     * Gets the width of this display.
     *
     * @return width
     */
    public float getDisplayWidth();

    /**
     * Sets the width of this display.
     *
     * @param width new width
     */
    public void setDisplayWidth(float width);

    /**
     * Gets the height of this display.
     *
     * @return height
     */
    public float getDisplayHeight();

    /**
     * Sets the height if this display.
     *
     * @param height new height
     */
    public void setDisplayHeight(float height);

    /**
     * Gets the amount of ticks before client-side interpolation will commence.
     *
     * @return interpolation delay ticks
     */
    public int getInterpolationDelay();

    /**
     * Sets the amount of ticks before client-side interpolation will commence.
     *
     * @param ticks interpolation delay ticks
     */
    public void setInterpolationDelay(int ticks);

    /**
     * Gets the billboard setting of this entity.
     *
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @return billboard setting
     */
    @NotNull
    public Billboard getBillboard();

    /**
     * Sets the billboard setting of this entity.
     *
     * The billboard setting controls the automatic rotation of the entity to
     * face the player.
     *
     * @param billboard new setting
     */
    public void setBillboard(@NotNull Billboard billboard);

    /**
     * Gets the scoreboard team overridden glow color of this display.
     *
     * @return glow color
     */
    @Nullable
    public Color getGlowColorOverride();

    /**
     * Sets the scoreboard team overridden glow color of this display.
     *
     * @param color new color
     */
    public void setGlowColorOverride(@Nullable Color color);

    /**
     * Gets the brightness override of the entity.
     *
     * @return brightness override, if set
     */
    @Nullable
    public Brightness getBrightness();

    /**
     * Sets the brightness override of the entity.
     *
     * @param brightness new brightness override
     */
    public void setBrightness(@Nullable Brightness brightness);

    /**
     * Describes the axes/points around which the entity can pivot.
     */
    public enum Billboard {

        /**
         * No rotation (default).
         */
        FIXED,
        /**
         * Can pivot around vertical axis.
         */
        VERTICAL,
        /**
         * Can pivot around horizontal axis.
         */
        HORIZONTAL,
        /**
         * Can pivot around center point.
         */
        CENTER;
    }

    /**
     * Represents the brightness rendering parameters of the entity.
     */
    public static class Brightness {

        private final int blockLight;
        private final int skyLight;

        public Brightness(int blockLight, int skyLight) {
            Preconditions.checkArgument(0 <= blockLight && blockLight <= 15, "Block brightness out of range: %s", blockLight);
            Preconditions.checkArgument(0 <= skyLight && skyLight <= 15, "Sky brightness out of range: %s", skyLight);

            this.blockLight = blockLight;
            this.skyLight = skyLight;
        }

        /**
         * Gets the block lighting component of this brightness.
         *
         * @return block light, between 0-15
         */
        public int getBlockLight() {
            return this.blockLight;
        }

        /**
         * Gets the sky lighting component of this brightness.
         *
         * @return sky light, between 0-15
         */
        public int getSkyLight() {
            return this.skyLight;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + this.blockLight;
            hash = 47 * hash + this.skyLight;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Brightness other = (Brightness) obj;
            if (this.blockLight != other.blockLight) {
                return false;
            }
            return this.skyLight == other.skyLight;
        }

        @Override
        public String toString() {
            return "Brightness{" + "blockLight=" + this.blockLight + ", skyLight=" + this.skyLight + '}';
        }
    }
}
