package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.monster.zombie.ZombieVillager;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Zombie;

public class CraftZombie extends CraftMonster implements Zombie {

    public CraftZombie(CraftServer server, net.minecraft.world.entity.monster.zombie.Zombie entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.zombie.Zombie getHandle() {
        return (net.minecraft.world.entity.monster.zombie.Zombie) this.entity;
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
    public boolean isVillager() {
        return this.getHandle() instanceof ZombieVillager;
    }

    @Override
    public void setVillager(boolean flag) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void setVillagerProfession(Villager.Profession profession) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Villager.Profession getVillagerProfession() {
        return null;
    }

    @Override
    public boolean isConverting() {
        return this.getHandle().isUnderWaterConverting();
    }

    @Override
    public int getConversionTime() {
        Preconditions.checkState(this.isConverting(), "Entity not converting");

        return this.getHandle().conversionTime;
    }

    @Override
    public void setConversionTime(int time) {
        if (time < 0) {
            this.getHandle().conversionTime = -1;
            this.getHandle().getEntityData().set(net.minecraft.world.entity.monster.zombie.Zombie.DATA_DROWNED_CONVERSION_ID, false);
        } else {
            this.getHandle().startUnderWaterConversion(time);
        }
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
    public boolean isDrowning() {
        return this.getHandle().isUnderWaterConverting();
    }

    @Override
    public void startDrowning(int drownedConversionTime) {
        this.getHandle().startUnderWaterConversion(drownedConversionTime);
    }

    @Override
    public void stopDrowning() {
        this.getHandle().stopDrowning();
    }

    @Override
    public boolean shouldBurnInDay() {
        return this.getHandle().isSunSensitive();
    }

    @Override
    public boolean isArmsRaised() {
        return this.getHandle().isAggressive();
    }

    @Override
    public void setArmsRaised(final boolean raised) {
        this.getHandle().setAggressive(raised);
    }

    @Override
    public void setShouldBurnInDay(boolean shouldBurnInDay) {
        this.getHandle().setShouldBurnInDay(shouldBurnInDay);
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

    @Override
    public boolean canBreakDoors() {
        return this.getHandle().canBreakDoors();
    }

    @Override
    public void setCanBreakDoors(boolean flag) {
        this.getHandle().setCanBreakDoors(flag);
    }
}
