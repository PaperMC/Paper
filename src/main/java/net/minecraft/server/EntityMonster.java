package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityMonster extends EntityCreature implements IMonster {

    protected int damage = 2;

    public EntityMonster(World world) {
        super(world);
        this.health = 20;
    }

    public void u() {
        float f = this.c(1.0F);

        if (f > 0.5F) {
            this.ay += 2;
        }

        super.u();
    }

    public void o_() {
        super.o_();
        if (!this.world.isStatic && this.world.spawnMonsters == 0) {
            this.die();
        }
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.a(this, 16.0D);

        return entityhuman != null && this.e(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(Entity entity, int i) {
        if (super.damageEntity(entity, i)) {
            if (this.passenger != entity && this.vehicle != entity) {
                if (entity != this) {
                    // CraftBukkit start
                    CraftServer server = ((WorldServer) this.world).getServer();
                    org.bukkit.entity.Entity bukkitTarget = null;
                    if (entity != null) {
                        bukkitTarget = entity.getBukkitEntity();
                    }

                    EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, TargetReason.TARGET_ATTACKED_ENTITY);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        if (event.getTarget() == null) {
                            this.target = null;
                        } else {
                            this.target = ((CraftEntity) event.getTarget()).getHandle();
                        }
                    }
                    // CraftBukkit end
                }

                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 2.0F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            // CraftBukkit start - this is still duplicated here and EntityHuman because it's possible for lastDamage EntityMonster
            // to damage another EntityMonster, and we want to catch those events.
            // This does not fire events for slime attacks, av they're not lastDamage EntityMonster.
            if (entity instanceof EntityLiving && !(entity instanceof EntityHuman)) {
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damager = this.getBukkitEntity();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, this.damage);
                server.getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    entity.damageEntity(this, event.getDamage());
                }
                return;
            }
            // CraftBukkit end

            entity.damageEntity(this, this.damage);
        }
    }

    protected float a(int i, int j, int k) {
        return 0.5F - this.world.m(i, j, k);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public boolean d() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        if (this.world.a(EnumSkyBlock.SKY, i, j, k) > this.random.nextInt(32)) {
            return false;
        } else {
            int l = this.world.getLightLevel(i, j, k);

            if (this.world.u()) {
                int i1 = this.world.f;

                this.world.f = 10;
                l = this.world.getLightLevel(i, j, k);
                this.world.f = i1;
            }

            return l <= this.random.nextInt(8) && super.d();
        }
    }
}
