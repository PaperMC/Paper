package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.item.PrimedTnt;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.TNTPrimed;

public class CraftTNTPrimed extends CraftEntity implements TNTPrimed {

    public CraftTNTPrimed(CraftServer server, PrimedTnt entity) {
        super(server, entity);
    }

    @Override
    public float getYield() {
        return this.getHandle().explosionPower;
    }

    @Override
    public boolean isIncendiary() {
        return this.getHandle().isIncendiary;
    }

    @Override
    public void setIsIncendiary(boolean isIncendiary) {
        this.getHandle().isIncendiary = isIncendiary;
    }

    @Override
    public void setYield(float yield) {
        this.getHandle().explosionPower = yield;
    }

    @Override
    public int getFuseTicks() {
        return this.getHandle().getFuse();
    }

    @Override
    public void setFuseTicks(int fuseTicks) {
        this.getHandle().setFuse(fuseTicks);
    }

    @Override
    public PrimedTnt getHandle() {
        return (PrimedTnt) this.entity;
    }

    @Override
    public String toString() {
        return "CraftTNTPrimed";
    }

    @Override
    public Entity getSource() {
        net.minecraft.world.entity.LivingEntity source = this.getHandle().getOwner();

        return (source != null) ? source.getBukkitEntity() : null;
    }

    @Override
    public void setSource(Entity source) {
        if (source instanceof LivingEntity) {
            this.getHandle().owner = ((CraftLivingEntity) source).getHandle();
        } else {
            this.getHandle().owner = null;
        }
    }
}
