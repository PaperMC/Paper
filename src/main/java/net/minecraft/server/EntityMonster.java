package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public abstract class EntityMonster extends EntityCreature implements IMonster {

    protected int damage = 2;

    public EntityMonster(World world) {
        super(world);
        this.aA = 5;
    }

    public void e() {
        float f = this.b(1.0F);

        if (f > 0.5F) {
            this.aV += 2;
        }

        super.e();
    }

    public void G_() {
        super.G_();
        if (!this.world.isStatic && this.world.difficulty == 0) {
            this.die();
        }
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);

        return entityhuman != null && this.h(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (super.damageEntity(damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (this.passenger != entity && this.vehicle != entity) {
                if (entity != this) {
                    // CraftBukkit start
                    org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

                    EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
                    Bukkit.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        if (event.getTarget() == null) {
                            this.target = null;
                            this.lastDamager = null;
                        } else {
                            this.target = ((CraftEntity) event.getTarget()).getHandle();
                            this.lastDamager = this.target instanceof EntityLiving ? (EntityLiving) this.target : null;
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

    public boolean a(Entity entity) {
        int i = this.damage;

        if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
            i += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
        }

        if (this.hasEffect(MobEffectList.WEAKNESS)) {
            i -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
        }

        // CraftBukkit start - this is still duplicated here and EntityHuman because it's possible for lastDamage EntityMonster
        // to damage another EntityMonster, and we want to catch those events.
        // This does not fire events for slime attacks, av they're not lastDamage EntityMonster.
        if (entity instanceof EntityLiving && !(entity instanceof EntityHuman)) {
            org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();

            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(this.getBukkitEntity(), damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, this.damage);
            Bukkit.getPluginManager().callEvent(event);
            i = event.getDamage();

            if (!event.isCancelled()) {
                return entity.damageEntity(DamageSource.mobAttack(this), i);
            }

            return false;
        }
        // CraftBukkit end

        return entity.damageEntity(DamageSource.mobAttack(this), i);
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 2.0F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            this.a(entity);
        }
    }

    public float a(int i, int j, int k) {
        return 0.5F - this.world.p(i, j, k);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected boolean D() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        if (this.world.a(EnumSkyBlock.SKY, i, j, k) > this.random.nextInt(32)) {
            return false;
        } else {
            int l = this.world.getLightLevel(i, j, k);

            if (this.world.w()) {
                int i1 = this.world.f;

                this.world.f = 10;
                l = this.world.getLightLevel(i, j, k);
                this.world.f = i1;
            }

            return l <= this.random.nextInt(8);
        }
    }

    public boolean canSpawn() {
        return this.D() && super.canSpawn();
    }
}
