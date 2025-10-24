package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import io.papermc.paper.util.Tick;
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
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long time) {
        this.setSize(Math.min(this.getMaxSize(), Math.max(1.0D, newSize)), TimeUnit.SECONDS, Math.clamp(time, 0L, Integer.MAX_VALUE));
    }

    @Override
    public void setSize(double newSize, TimeUnit unit, long time) {
        Preconditions.checkArgument(unit != null, "TimeUnit cannot be null.");
        Preconditions.checkArgument(time >= 0, "time cannot be lower than 0");
        Preconditions.checkArgument(newSize >= 1.0D && newSize <= this.getMaxSize(), "newSize must be between 1.0D and %s", this.getMaxSize());

        if (time > 0L) {
            final long startTime = (this.getWorld() != null) ? this.getWorld().getGameTime() : 0; // Virtual Borders don't have a World
            this.handle.lerpSizeBetween(this.handle.getSize(), newSize, Tick.tick().fromDuration(Duration.of(time, unit.toChronoUnit())), startTime);
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
    public int getWarningTime() {
        return this.handle.getWarningTime();
    }

    @Override
    public void setWarningTime(int time) {
        this.handle.setWarningTime(time);
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
