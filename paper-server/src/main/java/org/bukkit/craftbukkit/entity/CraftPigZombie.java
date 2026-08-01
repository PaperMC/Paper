package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.PigZombie;

public class CraftPigZombie extends CraftZombie implements PigZombie {

    public CraftPigZombie(CraftServer server, ZombifiedPiglin entity) {
        super(server, entity);
    }

    @Override
    public ZombifiedPiglin getHandle() {
        return (ZombifiedPiglin) this.entity;
    }

    @Override
    public int getAnger() {
        return (int) Math.max(this.getHandle().getPersistentAngerEndTime() - this.getHandle().level().getGameTime(), 0);
    }

    @Override
    public void setAnger(int level) {
        this.getHandle().setTimeToRemainAngry(level);
    }

    @Override
    public void setAngry(boolean angry) {
        this.setAnger(angry ? 400 : 0);
    }

    @Override
    public boolean isAngry() {
        return this.getAnger() > 0;
    }

    @Override
    public boolean isConverting() {
        return false;
    }

    @Override
    public int getConversionTime() {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }

    @Override
    public void setConversionTime(int time) {
        throw new UnsupportedOperationException("Not supported by this Entity.");
    }
}
