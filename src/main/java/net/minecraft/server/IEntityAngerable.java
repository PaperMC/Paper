package net.minecraft.server;

import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;

public interface IEntityAngerable {

    int getAnger();

    void setAnger(int i);

    @Nullable
    UUID getAngerTarget();

    void setAngerTarget(@Nullable UUID uuid);

    void anger();

    default void c(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("AngerTime", this.getAnger());
        if (this.getAngerTarget() != null) {
            nbttagcompound.a("AngryAt", this.getAngerTarget());
        }

    }

    default void a(WorldServer worldserver, NBTTagCompound nbttagcompound) {
        this.setAnger(nbttagcompound.getInt("AngerTime"));
        if (!nbttagcompound.b("AngryAt")) {
            this.setAngerTarget((UUID) null);
        } else {
            UUID uuid = nbttagcompound.a("AngryAt");

            this.setAngerTarget(uuid);
            Entity entity = worldserver.getEntity(uuid);

            if (entity != null) {
                if (entity instanceof EntityInsentient) {
                    this.setLastDamager((EntityInsentient) entity);
                }

                if (entity.getEntityType() == EntityTypes.PLAYER) {
                    this.e((EntityHuman) entity);
                }

            }
        }
    }

    default void a(WorldServer worldserver, boolean flag) {
        EntityLiving entityliving = this.getGoalTarget();
        UUID uuid = this.getAngerTarget();

        if ((entityliving == null || entityliving.dk()) && uuid != null && worldserver.getEntity(uuid) instanceof EntityInsentient) {
            this.pacify();
        } else {
            if (entityliving != null && !Objects.equals(uuid, entityliving.getUniqueID())) {
                this.setAngerTarget(entityliving.getUniqueID());
                this.anger();
            }

            if (this.getAnger() > 0 && (entityliving == null || entityliving.getEntityType() != EntityTypes.PLAYER || !flag)) {
                this.setAnger(this.getAnger() - 1);
                if (this.getAnger() == 0) {
                    this.pacify();
                }
            }

        }
    }

    default boolean a_(EntityLiving entityliving) {
        return !IEntitySelector.f.test(entityliving) ? false : (entityliving.getEntityType() == EntityTypes.PLAYER && this.a(entityliving.world) ? true : entityliving.getUniqueID().equals(this.getAngerTarget()));
    }

    default boolean a(World world) {
        return world.getGameRules().getBoolean(GameRules.UNIVERSAL_ANGER) && this.isAngry() && this.getAngerTarget() == null;
    }

    default boolean isAngry() {
        return this.getAnger() > 0;
    }

    default void b(EntityHuman entityhuman) {
        if (entityhuman.world.getGameRules().getBoolean(GameRules.FORGIVE_DEAD_PLAYERS)) {
            if (entityhuman.getUniqueID().equals(this.getAngerTarget())) {
                this.pacify();
            }
        }
    }

    default void I_() {
        this.pacify();
        this.anger();
    }

    default void pacify() {
        this.setLastDamager((EntityLiving) null);
        this.setAngerTarget((UUID) null);
        this.setGoalTarget((EntityLiving) null, org.bukkit.event.entity.EntityTargetEvent.TargetReason.FORGOT_TARGET, true); // CraftBukkit
        this.setAnger(0);
    }

    void setLastDamager(@Nullable EntityLiving entityliving);

    void e(@Nullable EntityHuman entityhuman);

    void setGoalTarget(@Nullable EntityLiving entityliving);

    boolean setGoalTarget(EntityLiving entityliving, org.bukkit.event.entity.EntityTargetEvent.TargetReason reason, boolean fireEvent); // CraftBukkit

    @Nullable
    EntityLiving getGoalTarget();
}
