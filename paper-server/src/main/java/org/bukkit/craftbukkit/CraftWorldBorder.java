package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder {

    private final World world;
    private final net.minecraft.world.level.border.WorldBorder handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().getWorldBorder();
    }

    public CraftWorldBorder(net.minecraft.world.level.border.WorldBorder handle) {
        this.world = null;
        this.handle = handle;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void reset() {
        this.getHandle().applySettings(net.minecraft.world.level.border.WorldBorder.Settings.DEFAULT);
    }

    @Override
    public double getSize() {
        return this.handle.getSize();
    }

    @Override
    public void setSize(double newSize) {
        Preconditions.checkArgument(newSize >= 1.0D && newSize <= this.getMaxSize(), "newSize must be between 1.0D and %s", this.getMaxSize());
        this.handle.setSize(newSize);
    }

    @Override
    public void changeSize(double newSize, long ticks) {
        Preconditions.checkArgument(ticks >= 0 && ticks <= Integer.MAX_VALUE, "ticks must be between 0-%s", Integer.MAX_VALUE);
        Preconditions.checkArgument(newSize >= 1.0D && newSize <= this.getMaxSize(), "newSize must be between 1.0D and %s", this.getMaxSize());

        if (ticks > 0L) {
            final long startTime = (this.getWorld() != null) ? this.getWorld().getGameTime() : 0; // Virtual Borders don't have a World
            this.handle.lerpSizeBetween(this.handle.getSize(), newSize, ticks, startTime);
        } else {
            this.handle.setSize(newSize);
        }
    }

    @Override
    public Location getCenter() {
        double x = this.handle.getCenterX();
        double z = this.handle.getCenterZ();

        return new Location(this.world, x, 0, z);
    }

    @Override
    public void setCenter(double x, double z) {
        Preconditions.checkArgument(Math.abs(x) <= this.getMaxCenterCoordinate(), "x coordinate cannot be outside +- %s", this.getMaxCenterCoordinate());
        Preconditions.checkArgument(Math.abs(z) <= this.getMaxCenterCoordinate(), "z coordinate cannot be outside +- %s", this.getMaxCenterCoordinate());

        this.handle.setCenter(x, z);
    }

    @Override
    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return this.handle.getSafeZone();
    }

    @Override
    public void setDamageBuffer(double blocks) {
        this.handle.setSafeZone(blocks);
    }

    @Override
    public double getDamageAmount() {
        return this.handle.getDamagePerBlock();
    }

    @Override
    public void setDamageAmount(double damage) {
        this.handle.setDamagePerBlock(damage);
    }

    @Override
    public int getWarningTimeTicks() {
        return this.handle.getWarningTime();
    }

    @Override
    public void setWarningTimeTicks(final int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks cannot be lower than 0");

        this.handle.setWarningTime(ticks);
    }

    @Override
    public int getWarningDistance() {
        return this.handle.getWarningBlocks();
    }

    @Override
    public void setWarningDistance(int distance) {
        this.handle.setWarningBlocks(distance);
    }

    @Override
    public boolean isInside(Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");

        return (this.world == null || location.getWorld().equals(this.world)) && this.handle.isWithinBounds(BlockPos.containing(location.getX(), location.getY(), location.getZ()));
    }

    @Override
    public double getMaxSize() {
        return net.minecraft.world.level.border.WorldBorder.MAX_SIZE;
    }

    @Override
    public double getMaxCenterCoordinate() {
        return net.minecraft.world.level.border.WorldBorder.MAX_CENTER_COORDINATE;
    }

    public net.minecraft.world.level.border.WorldBorder getHandle() {
        return this.handle;
    }

    public boolean isVirtual() {
        return this.world == null;
    }
}
