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
        com.mojang.math.Transformation nms = net.minecraft.world.entity.Display.createTransformation(getHandle().getEntityData());

        return new Transformation(nms.getTranslation(), nms.getLeftRotation(), nms.getScale(), nms.getRightRotation());
    }

    @Override
    public void setTransformation(Transformation transformation) {
        Preconditions.checkArgument(transformation != null, "Transformation cannot be null");

        getHandle().setTransformation(new com.mojang.math.Transformation(transformation.getTranslation(), transformation.getLeftRotation(), transformation.getScale(), transformation.getRightRotation()));
    }

    @Override
    public void setTransformationMatrix(org.joml.Matrix4f transformationMatrix) {
        Preconditions.checkArgument(transformationMatrix != null, "Transformation matrix cannot be null");

        getHandle().setTransformation(new com.mojang.math.Transformation(transformationMatrix));
    }

    @Override
    public int getInterpolationDuration() {
        return getHandle().getInterpolationDuration();
    }

    @Override
    public void setInterpolationDuration(int duration) {
        getHandle().setInterpolationDuration(duration);
    }

    @Override
    public float getViewRange() {
        return getHandle().getViewRange();
    }

    @Override
    public void setViewRange(float range) {
        getHandle().setViewRange(range);
    }

    @Override
    public float getShadowRadius() {
        return getHandle().getShadowRadius();
    }

    @Override
    public void setShadowRadius(float radius) {
        getHandle().setShadowRadius(radius);
    }

    @Override
    public float getShadowStrength() {
        return getHandle().getShadowStrength();
    }

    @Override
    public void setShadowStrength(float strength) {
        getHandle().setShadowStrength(strength);
    }

    @Override
    public float getDisplayWidth() {
        return getHandle().getWidth();
    }

    @Override
    public void setDisplayWidth(float width) {
        getHandle().setWidth(width);
    }

    @Override
    public float getDisplayHeight() {
        return getHandle().getHeight();
    }

    @Override
    public void setDisplayHeight(float height) {
        getHandle().setHeight(height);
    }

    @Override
    public int getInterpolationDelay() {
        return getHandle().getInterpolationDelay();
    }

    @Override
    public void setInterpolationDelay(int ticks) {
        getHandle().setInterpolationDelay(ticks);
    }

    @Override
    public Billboard getBillboard() {
        return Billboard.valueOf(getHandle().getBillboardConstraints().name());
    }

    @Override
    public void setBillboard(Billboard billboard) {
        Preconditions.checkArgument(billboard != null, "Billboard cannot be null");

        getHandle().setBillboardConstraints(net.minecraft.world.entity.Display.BillboardConstraints.valueOf(billboard.name()));
    }

    @Override
    public Color getGlowColorOverride() {
        int color = getHandle().getGlowColorOverride();

        return (color == -1) ? null : Color.fromARGB(color);
    }

    @Override
    public void setGlowColorOverride(Color color) {
        if (color == null) {
            getHandle().setGlowColorOverride(-1);
        } else {
            getHandle().setGlowColorOverride(color.asARGB());
        }
    }

    @Override
    public Brightness getBrightness() {
        net.minecraft.util.Brightness nms = getHandle().getBrightnessOverride();

        return (nms != null) ? new Brightness(nms.block(), nms.sky()) : null;
    }

    @Override
    public void setBrightness(Brightness brightness) {
        if (brightness != null) {
            getHandle().setBrightnessOverride(new net.minecraft.util.Brightness(brightness.getBlockLight(), brightness.getSkyLight()));
        } else {
            getHandle().setBrightnessOverride(null);
        }
    }
}
