package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.bukkit.boss.BossBar;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.boss.CraftBossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wither;

public class CraftWither extends CraftMonster implements Wither, com.destroystokyo.paper.entity.CraftRangedEntity<WitherBoss> { // Paper

    private BossBar bossBar;

    public CraftWither(CraftServer server, WitherBoss entity) {
        super(server, entity);

        if (entity.bossEvent != null) {
            this.bossBar = new CraftBossBar(entity.bossEvent);
        }
    }

    @Override
    public WitherBoss getHandle() {
        return (WitherBoss) this.entity;
    }

    @Override
    public String toString() {
        return "CraftWither";
    }

    @Override
    public BossBar getBossBar() {
        return this.bossBar;
    }

    @Override
    public void setTarget(Head head, LivingEntity livingEntity) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = (livingEntity != null) ? livingEntity.getEntityId() : 0;
        this.getHandle().setAlternativeTarget(head.ordinal(), entityId);
    }

    @Override
    public LivingEntity getTarget(Head head) {
        Preconditions.checkArgument(head != null, "head cannot be null");

        int entityId = this.getHandle().getAlternativeTarget(head.ordinal());
        if (entityId == 0) {
            return null;
        }
        Entity target = this.getHandle().level().getEntity(entityId);
        return (target != null) ? (LivingEntity) target.getBukkitEntity() : null;
    }

    @Override
    public int getInvulnerabilityTicks() {
        return this.getHandle().getInvulnerableTicks();
    }

    @Override
    public void setInvulnerabilityTicks(int ticks) {
        Preconditions.checkArgument(ticks >= 0, "ticks must be >=0");

        this.getHandle().setInvulnerableTicks(ticks);
    }
}
