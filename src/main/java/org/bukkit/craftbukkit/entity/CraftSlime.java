package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntitySlime;

import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Slime;

public class CraftSlime extends CraftLivingEntity implements Slime {

    public CraftSlime(CraftServer server, EntitySlime entity) {
        super(server, entity);
    }

    public int getSize() {
        return getHandle().getSize();
    }

    public void setSize(int size) {
        getHandle().setSize(size, true);
    }

    @Override
    public void setTarget(LivingEntity target) {
        if (target == null) {
            getHandle().setGoalTarget(null, null, false);
        } else if (target instanceof CraftLivingEntity) {
            getHandle().setGoalTarget(((CraftLivingEntity) target).getHandle(), null, false);
        }
    }

    @Override
    public LivingEntity getTarget() {
        return getHandle().getGoalTarget() == null ? null : (LivingEntity)getHandle().getGoalTarget().getBukkitEntity();
    }

    @Override
    public EntitySlime getHandle() {
        return (EntitySlime) entity;
    }

    @Override
    public String toString() {
        return "CraftSlime";
    }

    public EntityType getType() {
        return EntityType.SLIME;
    }
}
