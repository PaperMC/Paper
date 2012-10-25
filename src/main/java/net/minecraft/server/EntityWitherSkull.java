package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityWitherSkull extends EntityFireball {

    public EntityWitherSkull(World world) {
        super(world);
        this.a(0.3125F, 0.3125F);
    }

    public EntityWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.a(0.3125F, 0.3125F);
    }

    protected float c() {
        return this.d() ? 0.73F : super.c();
    }

    public boolean isBurning() {
        return false;
    }

    public float a(Explosion explosion, Block block, int i, int j, int k) {
        float f = super.a(explosion, block, i, j, k);

        if (this.d() && block != Block.BEDROCK) {
            f = Math.min(0.8F, f);
        }

        return f;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null) {
                if (this.shooter != null) {
                    if (movingobjectposition.entity.damageEntity(DamageSource.mobAttack(this.shooter), 8) && !movingobjectposition.entity.isAlive()) {
                        this.shooter.heal(5, EntityRegainHealthEvent.RegainReason.WITHER); // CraftBukkit
                    }
                } else {
                    movingobjectposition.entity.damageEntity(DamageSource.MAGIC, 5);
                }

                if (movingobjectposition.entity instanceof EntityLiving) {
                    byte b0 = 0;

                    if (this.world.difficulty > 1) {
                        if (this.world.difficulty == 2) {
                            b0 = 10;
                        } else if (this.world.difficulty == 3) {
                            b0 = 40;
                        }
                    }

                    if (b0 > 0) {
                        ((EntityLiving) movingobjectposition.entity).addEffect(new MobEffect(MobEffectList.WITHER.id, 20 * b0, 1));
                    }
                }
            }

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(), this.world.getGameRules().getBoolean("mobGriefing"));
            }
            // CraftBukkit end

            this.die();
        }
    }

    public boolean L() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    protected void a() {
        this.datawatcher.a(10, Byte.valueOf((byte) 0));
    }

    public boolean d() {
        return this.datawatcher.getByte(10) == 1;
    }

    public void e(boolean flag) {
        this.datawatcher.watch(10, Byte.valueOf((byte) (flag ? 1 : 0)));
    }
}
