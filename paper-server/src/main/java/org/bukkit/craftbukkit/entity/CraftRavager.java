package org.bukkit.craftbukkit.entity;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Ravager;

public class CraftRavager extends CraftRaider implements Ravager {

    public CraftRavager(CraftServer server, net.minecraft.world.entity.monster.Ravager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.monster.Ravager getHandle() {
        return (net.minecraft.world.entity.monster.Ravager) this.entity;
    }

    @Override
    public int getAttackTicks() {
        return this.getHandle().getAttackTick();
    }

    @Override
    public void setAttackTicks(int ticks) {
        this.getHandle().attackTick = ticks;
    }

    @Override
    public int getStunnedTicks() {
        return this.getHandle().getStunnedTick();
    }

    @Override
    public void setStunnedTicks(int ticks) {
        this.getHandle().stunnedTick = ticks;
    }

    @Override
    public int getRoarTicks() {
        return this.getHandle().getRoarTick();
    }

    @Override
    public void setRoarTicks(int ticks) {
        this.getHandle().roarTick = ticks;
    }
}
