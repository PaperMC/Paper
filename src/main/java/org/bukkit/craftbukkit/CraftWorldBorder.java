package org.bukkit.craftbukkit;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class CraftWorldBorder implements WorldBorder {

    private final World world;
    private final net.minecraft.server.WorldBorder handle;

    public CraftWorldBorder(CraftWorld world) {
        this.world = world;
        this.handle = world.getHandle().af(); // PAIL: Rename
    }

    @Override
    public void reset() {
        this.setSize(6.0E7D);
        this.setDamageAmount(0.2D);
        this.setDamageBuffer(5.0D);
        this.setWarningDistance(5);
        this.setWarningTime(15);
        this.setCenter(0, 0);
    }

    @Override
    public double getSize() {
        return this.handle.h(); // PAIL: Rename
    }

    @Override
    public void setSize(double newSize) {
        this.setSize(newSize, 0L);
    }

    @Override
    public void setSize(double newSize, long time) {
        // PAIL: TODO: Magic Values
        newSize = Math.min(6.0E7D, Math.max(1.0D, newSize));
        time = Math.min(9223372036854775L, Math.max(0L, time));

        if (time > 0L) {
            this.handle.a(this.handle.h(), newSize, time * 1000L); // PAIL: Rename
        } else {
            this.handle.a(newSize); // PAIL: Rename
        }
    }

    @Override
    public Location getCenter() {
        double x = this.handle.f(); // PAIL: Rename
        double z = this.handle.g(); // PAIL: Rename

        return new Location(this.world, x, 0, z);
    }

    @Override
    public void setCenter(double x, double z) {
        // PAIL: TODO: Magic Values
        x = Math.min(3.0E7D, Math.max(-3.0E7D, x));
        z = Math.min(3.0E7D, Math.max(-3.0E7D, z));

        this.handle.c(x, z); // PAIL: Rename
    }

    @Override
    public void setCenter(Location location) {
        this.setCenter(location.getX(), location.getZ());
    }

    @Override
    public double getDamageBuffer() {
        return this.handle.m(); // PAIL: Rename
    }

    @Override
    public void setDamageBuffer(double blocks) {
        this.handle.b(blocks); // PAIL: Rename
    }

    @Override
    public double getDamageAmount() {
        return this.handle.n(); // PAIL: Rename
    }

    @Override
    public void setDamageAmount(double damage) {
        this.handle.c(damage); // PAIL: Rename
    }

    @Override
    public int getWarningTime() {
        return this.handle.p(); // PAIL: Rename
    }

    @Override
    public void setWarningTime(int time) {
        this.handle.b(time); // PAIL: Rename
    }

    @Override
    public int getWarningDistance() {
        return this.handle.q(); // PAIL: Rename
    }

    @Override
    public void setWarningDistance(int distance) {
        this.handle.c(distance); // PAIL: Rename
    }
}
