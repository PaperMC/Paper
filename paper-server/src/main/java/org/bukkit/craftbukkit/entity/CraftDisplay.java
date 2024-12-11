package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Color;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Display;
import org.bukkit.util.Transformation;

public class CraftDisplay extends CraftEntity implements Display {

    public CraftDisplay(CraftServer server, net.minecraft.world.entity.Display entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.Display getHandle() {
        return (net.minecraft.world.entity.Display) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftDisplay";
    }

    @Override
    public Transformation getTransformation() {
        com.mojang.math.Transformation nms = net.minecraft.world.entity.Display.createTransformation(this.getHandle().getEntityData());

        return new Transformation(nms.getTranslation(), nms.getLeftRotation(), nms.getScale(), nms.getRightRotation());
    }

    @Override
    public void setTransformation(Transformation transformation) {
        Preconditions.checkArgument(transformation != null, "Transformation cannot be null");

        this.getHandle().setTransformation(new com.mojang.math.Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation()));
    }

    @Override
    public void setTransformationMatrix(org.joml.Matrix4f transformationMatrix) {
        Preconditions.checkArgument(transformationMatrix != null, "Transformation matrix cannot be null");

        this.getHandle().setTransformation(new com.mojang.math.Transformation(transformationMatrix));
    }

    @Override
    public int getInterpolationDuration() {
        return this.getHandle().getTransformationInterpolationDuration();
    }

    @Override
    public void setInterpolationDuration(int duration) {
        this.getHandle().setTransformationInterpolationDuration(duration);
    }

    @Override
    public int getTeleportDuration() {
        return this.getHandle().getEntityData().get(net.minecraft.world.entity.Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID);
    }

    @Override
    public void setTeleportDuration(int duration) {
        Preconditions.checkArgument(duration >= 0 && duration <= 59, "duration (%s) cannot be lower than 0 or higher than 59", duration);
        this.getHandle().getEntityData().set(net.minecraft.world.entity.Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, duration);
    }

    @Override
    public float getViewRange() {
        return this.getHandle().getViewRange();
    }

    @Override
    public void setViewRange(float range) {
        this.getHandle().setViewRange(range);
    }

    @Override
    public float getShadowRadius() {
        return this.getHandle().getShadowRadius();
    }

    @Override
    public void setShadowRadius(float radius) {
        this.getHandle().setShadowRadius(radius);
    }

    @Override
    public float getShadowStrength() {
        return this.getHandle().getShadowStrength();
    }

    @Override
    public void setShadowStrength(float strength) {
        this.getHandle().setShadowStrength(strength);
    }

    @Override
    public float getDisplayWidth() {
        return this.getHandle().getWidth();
    }

    @Override
    public void setDisplayWidth(float width) {
        this.getHandle().setWidth(width);
    }

    @Override
    public float getDisplayHeight() {
        return this.getHandle().getHeight();
    }

    @Override
    public void setDisplayHeight(float height) {
        this.getHandle().setHeight(height);
    }

    @Override
    public int getInterpolationDelay() {
        return this.getHandle().getTransformationInterpolationDelay();
    }

    @Override
    public void setInterpolationDelay(int ticks) {
        this.getHandle().setTransformationInterpolationDelay(ticks);
    }

    @Override
    public Billboard getBillboard() {
        return Billboard.valueOf(this.getHandle().getBillboardConstraints().name());
    }

    @Override
    public void setBillboard(Billboard billboard) {
        Preconditions.checkArgument(billboard != null, "Billboard cannot be null");

        this.getHandle().setBillboardConstraints(net.minecraft.world.entity.Display.BillboardConstraints.valueOf(billboard.name()));
    }

    @Override
    public Color getGlowColorOverride() {
        int color = this.getHandle().getGlowColorOverride();

        return (color == -1) ? null : Color.fromARGB(color);
    }

    @Override
    public void setGlowColorOverride(Color color) {
        if (color == null) {
            this.getHandle().setGlowColorOverride(-1);
        } else {
            this.getHandle().setGlowColorOverride(color.asARGB());
        }
    }

    @Override
    public Brightness getBrightness() {
        net.minecraft.util.Brightness nms = this.getHandle().getBrightnessOverride();

        return (nms != null) ? new Brightness(nms.block(), nms.sky()) : null;
    }

    @Override
    public void setBrightness(Brightness brightness) {
        if (brightness != null) {
            this.getHandle().setBrightnessOverride(new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()));
        } else {
            this.getHandle().setBrightnessOverride(null);
        }
    }
}
