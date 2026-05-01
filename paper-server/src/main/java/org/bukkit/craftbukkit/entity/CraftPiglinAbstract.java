package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PiglinAbstract;

public class CraftPiglinAbstract extends CraftMonster implements PiglinAbstract {

    public CraftPiglinAbstract(CraftServer server, AbstractPiglin entity) {
        super(server, entity);
    }

    @Override
    public AbstractPiglin getHandle() {
        return (AbstractPiglin) this.entity;
    }

    @Override
    public boolean isImmuneToZombification() {
        return this.getHandle().isImmuneToZombification();
    }

    @Override
    public void setImmuneToZombification(boolean flag) {
        this.getHandle().setImmuneToZombification(flag);
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity not converting");
        return this.getHandle().timeInOverworld;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            this.getHandle().timeInOverworld = -1;
            this.getHandle().setImmuneToZombification(false);
        } else {
            this.getHandle().timeInOverworld = time;
        }
    }

    @Override
    public boolean isConverting() {
        return this.getHandle().isConverting();
    }

    @Override
    public boolean isBaby() {
        return this.getHandle().isBaby();
    }

    @Override
    public void setBaby(boolean baby) {
        CraftAgeable.setBaby(this.getHandle(), baby);
    }

    @Override
    public int getAge() {
        return this.getHandle().isBaby() ? -1 : 0;
    }

    @Override
    public void setAge(int age) {
        this.getHandle().setBaby(age < 0);
    }

    @Override
    public void setAgeLock(boolean lock) {
    }

    @Override
    public boolean getAgeLock() {
        return false;
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
    public void setBreed(boolean b) {
    }
}
