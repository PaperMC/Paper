package net.minecraft.server;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public abstract class EntityMonster extends EntityCreature implements IMonster {

    public EntityMonster(World world) {
        super(world);
        this.bc = 5;
    }

    public void c() {
        this.bo();
        float f = this.c(1.0F);

        if (f > 0.5F) {
            this.bA += 2;
        }

        super.c();
    }

    public void j_() {
        super.j_();
        if (!this.world.isStatic && this.world.difficulty == 0) {
            this.die();
        }
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);

        return entityhuman != null && this.n(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.isInvulnerable()) {
            return false;
        } else if (super.damageEntity(damagesource, i)) {
            Entity entity = damagesource.getEntity();

            if (this.passenger != entity && this.vehicle != entity) {
                if (entity != this) {
                    // CraftBukkit start - we still need to call events for entities without goals
                    if (entity != this.target && (this instanceof EntityBlaze || this instanceof EntityEnderman || this instanceof EntitySpider || this instanceof EntityGiantZombie || this instanceof EntitySilverfish)) {
                        EntityTargetEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTargetEvent(this, entity, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);

                        if (!event.isCancelled()) {
                            if (event.getTarget() == null) {
                                this.target = null;
                            } else {
                                this.target = ((org.bukkit.craftbukkit.entity.CraftEntity) event.getTarget()).getHandle();
                            }
                        }
                    } else {
                        this.target = entity;
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

    public boolean m(Entity entity) {
        int i = this.c(entity);

        if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
            i += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
        }

        if (this.hasEffect(MobEffectList.WEAKNESS)) {
            i -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
        }

        int j = 0;

        if (entity instanceof EntityLiving) {
            i += EnchantmentManager.a((EntityLiving) this, (EntityLiving) entity);
            j += EnchantmentManager.getKnockbackEnchantmentLevel(this, (EntityLiving) entity);
        }

        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), i);

        if (flag) {
            if (j > 0) {
                entity.g((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F));
                this.motX *= 0.6D;
                this.motZ *= 0.6D;
            }

            int k = EnchantmentManager.getFireAspectEnchantmentLevel(this);

            if (k > 0) {
                entity.setOnFire(k * 4);
            }
        }

        return flag;
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 2.0F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            this.m(entity);
        }
    }

    public float a(int i, int j, int k) {
        return 0.5F - this.world.p(i, j, k);
    }

    protected boolean i_() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        if (this.world.b(EnumSkyBlock.SKY, i, j, k) > this.random.nextInt(32)) {
            return false;
        } else {
            int l = this.world.getLightLevel(i, j, k);

            if (this.world.M()) {
                int i1 = this.world.j;

                this.world.j = 10;
                l = this.world.getLightLevel(i, j, k);
                this.world.j = i1;
            }

            return l <= this.random.nextInt(8);
        }
    }

    public boolean canSpawn() {
        return this.i_() && super.canSpawn();
    }

    public int c(Entity entity) {
        return 2;
    }
}
