package org.bukkit.craftbukkit.entity;

import io.papermc.paper.entity.PaperBucketable;
import io.papermc.paper.entity.PaperShearable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.SulfurCube;

public class CraftSulfurCube extends CraftAbstractCubeMob implements SulfurCube, PaperShearable, PaperBucketable {
    public CraftSulfurCube(final CraftServer server, final net.minecraft.world.entity.monster.cubemob.SulfurCube entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.cubemob.SulfurCube getHandle() {
        return (net.minecraft.world.entity.monster.cubemob.SulfurCube) this.entity;
    }

    @Override
    public int getAge() {
        return this.getHandle().getAge();
    }

    @Override
    public void setAge(final int age) {
        this.getHandle().setAge(age);
    }

    @Override
    public void setAgeLock(final boolean lock) {
        this.getHandle().setAgeLocked(lock);
    }

    @Override
    public boolean getAgeLock() {
        return this.getHandle().isAgeLocked();
    }

    @Override
    public void setBaby() {
        CraftAgeable.setBaby(this.getHandle(), true);
    }

    @Override
    public void setAdult() {
        CraftAgeable.setBaby(this.getHandle(), false);
    }

    @Override
    public boolean isAdult() {
        return !this.getHandle().isBaby();
    }

    @Override
    public boolean canBreed() {
        return false;
    }

    @Override
    public void setBreed(final boolean breed) {
    }
}
