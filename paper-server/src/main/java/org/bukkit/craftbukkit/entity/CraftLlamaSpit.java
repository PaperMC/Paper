package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.projectiles.ProjectileSource;

public class CraftLlamaSpit extends AbstractProjectile implements LlamaSpit {

    public CraftLlamaSpit(CraftServer server, net.minecraft.world.entity.projectile.LlamaSpit entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.projectile.LlamaSpit getHandle() {
        return (net.minecraft.world.entity.projectile.LlamaSpit) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftLlamaSpit";
    }

    @Override
    public ProjectileSource getShooter() {
        return (this.getHandle().getOwner() != null) ? (ProjectileSource) this.getHandle().getOwner().getBukkitEntity() : null;
    }

    @Override
    public void setShooter(ProjectileSource source) {
        this.getHandle().setOwner((source != null) ? ((CraftLivingEntity) source).getHandle() : null);
    }
}
