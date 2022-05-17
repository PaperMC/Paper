package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.EntityWither;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither {

    private BossBar bossBar;

    public CraftWither(CraftServer server, EntityWither entity) {
        super(server, entity);

        if (entity.bossEvent != null) {
            this.bossBar = new CraftBossBar(entity.bossEvent);
        }
    }

    @Override
    public EntityWither getHandle() {
        return (EntityWither) entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public EntityType getType() {
        return EntityType.WITHER;
    }

    @Override
    public BossBar getBossBar() {
        return bossBar;
    }

    @Override
    public void setTarget(Head head, LivingEntity livingEntity) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = (livingEntity != null) ? livingEntity.getEntityId() : 0;
        getHandle().setAlternativeTarget(head.ordinal(), entityId);
    }

    @Override
    public LivingEntity getTarget(Head head) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = getHandle().getAlternativeTarget(head.ordinal());
        if (entityId == 0) {
            return null;
        }
        Entity target = getHandle().getLevel().getEntity(entityId);
        return (target != null) ? (LivingEntity) target.getBukkitEntity() : null;
    }
}
