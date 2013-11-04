package net.minecraft.server;

import org.bukkit.event.entity.EntityTargetEvent; // CraftBukkit

public abstract class EntityMonster extends EntityCreature implements IMonster {

    public EntityMonster(World world) {
        super(world);
        this.b = 5;
    }

    public void e() {
        this.bb();
        float f = this.d(1.0F);

        if (f > 0.5F) {
            this.aV += 2;
        }

        super.e();
    }

    public void h() {
        super.h();
        if (!this.world.isStatic && this.world.difficulty == EnumDifficulty.PEACEFUL) {
            this.die();
        }
    }

    protected String H() {
        return "game.hostile.swim";
    }

    protected String O() {
        return "game.hostile.swim.splash";
    }

    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D);

        return entityhuman != null && this.o(entityhuman) ? entityhuman : null;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (super.damageEntity(damagesource, f)) {
            Entity entity = damagesource.getEntity();

            if (this.passenger != entity && this.vehicle != entity) {
                if (entity != this) {
                    // CraftBukkit start - We still need to call events for entities without goals
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

    protected String aT() {
        return "game.hostile.hurt";
    }

    protected String aU() {
        return "game.hostile.die";
    }

    protected String o(int i) {
        return i > 4 ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }

    public boolean m(Entity entity) {
        float f = (float) this.getAttributeInstance(GenericAttributes.e).getValue();
        int i = 0;

        if (entity instanceof EntityLiving) {
            f += EnchantmentManager.a((EntityLiving) this, (EntityLiving) entity);
            i += EnchantmentManager.getKnockbackEnchantmentLevel(this, (EntityLiving) entity);
        }

        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), f);

        if (flag) {
            if (i > 0) {
                entity.g((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F));
                this.motX *= 0.6D;
                this.motZ *= 0.6D;
            }

            int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);

            if (j > 0) {
                entity.setOnFire(j * 4);
            }

            if (entity instanceof EntityLiving) {
                EnchantmentManager.a((EntityLiving) entity, (Entity) this);
            }

            EnchantmentManager.b(this, entity);
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
        return 0.5F - this.world.n(i, j, k);
    }

    protected boolean j_() {
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.boundingBox.b);
        int k = MathHelper.floor(this.locZ);

        if (this.world.b(EnumSkyBlock.SKY, i, j, k) > this.random.nextInt(32)) {
            return false;
        } else {
            int l = this.world.getLightLevel(i, j, k);

            if (this.world.O()) {
                int i1 = this.world.j;

                this.world.j = 10;
                l = this.world.getLightLevel(i, j, k);
                this.world.j = i1;
            }

            return l <= this.random.nextInt(8);
        }
    }

    public boolean canSpawn() {
        return this.world.difficulty != EnumDifficulty.PEACEFUL && this.j_() && super.canSpawn();
    }

    protected void aD() {
        super.aD();
        this.bc().b(GenericAttributes.e);
    }

    protected boolean aG() {
        return true;
    }
}
