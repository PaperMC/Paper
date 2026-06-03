package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.AbstractCubeMob;

public abstract class CraftAbstractCubeMob extends CraftMob implements AbstractCubeMob {
    public CraftAbstractCubeMob(final CraftServer server, final net.minecraft.world.entity.monster.cubemob.AbstractCubeMob entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.AbstractCubeMob getHandle() {
        return (net.minecraft.world.entity.monster.cubemob.AbstractCubeMob) this.entity;
    }

    @Override
    public int getSize() {
        return this.getHandle().getSize();
    }

    @Override
    public void setSize(int size) {
        this.getHandle().setSize(size, /* true */ getHandle().isAlive()); // Paper - fix dead slime setSize invincibility
    }

    @Override
    public boolean canWander() {
        return this.getHandle().canWander();
    }

    @Override
    public void setWander(boolean canWander) {
        this.getHandle().setWander(canWander);
    }
}
