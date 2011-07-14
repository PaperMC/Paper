package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
// CraftBukkit end

public class EntityEgg extends Entity {

    private int b = -1;
    private int c = -1;
    private int d = -1;
    private int e = 0;
    private boolean f = false;
    public int a = 0;
    public EntityLiving thrower; // CraftBukkit - private -> public
    private int h;
    private int i = 0;

    public EntityEgg(World world) {
        super(world);
        this.b(0.25F, 0.25F);
    }

    protected void b() {}

    public EntityEgg(World world, EntityLiving entityliving) {
        super(world);
        this.thrower = entityliving;
        this.b(0.25F, 0.25F);
        this.setPositionRotation(entityliving.locX, entityliving.locY + (double) entityliving.t(), entityliving.locZ, entityliving.yaw, entityliving.pitch);
        this.locX -= (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.locY -= 0.10000000149011612D;
        this.locZ -= (double) (MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * 0.16F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.height = 0.0F;
        float f = 0.4F;

        this.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
        this.motY = (double) (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f);
        this.a(this.motX, this.motY, this.motZ, 1.5F, 1.0F);
    }

    public EntityEgg(World world, double d0, double d1, double d2) {
        super(world);
        this.h = 0;
        this.b(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
    }

    public void a(double d0, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2);

        d0 /= (double) f2;
        d1 /= (double) f2;
        d2 /= (double) f2;
        d0 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d1 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d2 += this.random.nextGaussian() * 0.007499999832361937D * (double) f1;
        d0 *= (double) f;
        d1 *= (double) f;
        d2 *= (double) f;
        this.motX = d0;
        this.motY = d1;
        this.motZ = d2;
        float f3 = MathHelper.a(d0 * d0 + d2 * d2);

        this.lastYaw = this.yaw = (float) (Math.atan2(d0, d2) * 180.0D / 3.1415927410125732D);
        this.lastPitch = this.pitch = (float) (Math.atan2(d1, (double) f3) * 180.0D / 3.1415927410125732D);
        this.h = 0;
    }

    public void m_() {
        this.bo = this.locX;
        this.bp = this.locY;
        this.bq = this.locZ;
        super.m_();
        if (this.a > 0) {
            --this.a;
        }

        if (this.f) {
            int i = this.world.getTypeId(this.b, this.c, this.d);

            if (i == this.e) {
                ++this.h;
                if (this.h == 1200) {
                    this.die();
                }

                return;
            }

            this.f = false;
            this.motX *= (double) (this.random.nextFloat() * 0.2F);
            this.motY *= (double) (this.random.nextFloat() * 0.2F);
            this.motZ *= (double) (this.random.nextFloat() * 0.2F);
            this.h = 0;
            this.i = 0;
        } else {
            ++this.i;
        }

        Vec3D vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
        Vec3D vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        MovingObjectPosition movingobjectposition = this.world.a(vec3d, vec3d1);

        vec3d = Vec3D.create(this.locX, this.locY, this.locZ);
        vec3d1 = Vec3D.create(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.create(movingobjectposition.f.a, movingobjectposition.f.b, movingobjectposition.f.c);
        }

        if (!this.world.isStatic) {
            Entity entity = null;
            List list = this.world.b((Entity) this, this.boundingBox.a(this.motX, this.motY, this.motZ).b(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1.l_() && (entity1 != this.thrower || this.i >= 5)) {
                    float f = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.b((double) f, (double) f, (double) f);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.a(vec3d, vec3d1);

                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.a(movingobjectposition1.f);

                        if (d1 < d0 || d0 == 0.0D) {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null) {
            // CraftBukkit start
            ProjectileHitEvent phe = new ProjectileHitEvent((Projectile) this.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(phe);

            if (movingobjectposition.entity != null) {
                boolean stick;
                if (movingobjectposition.entity instanceof EntityLiving) {
                    org.bukkit.entity.Entity damagee = movingobjectposition.entity.getBukkitEntity();
                    Projectile projectile = (Projectile) this.getBukkitEntity();

                    // TODO @see EntityArrow#162
                    EntityDamageByProjectileEvent event = new EntityDamageByProjectileEvent(damagee, projectile, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 0);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        stick = !event.getBounce();
                    } else {
                        // this function returns if the egg should stick in or not, i.e. !bounce
                        stick = movingobjectposition.entity.damageEntity(this.thrower, event.getDamage());
                    }
                } else {
                    stick = movingobjectposition.entity.damageEntity(this.thrower, 0);
                }

                if (stick) {
                    ; // Original code does nothing *yet*
                }
            }

            boolean hatching = !this.world.isStatic && this.random.nextInt(8) == 0;
            int numHatching = (this.random.nextInt(32) == 0) ? 4 : 1;
            if (!hatching) {
                numHatching = 0;
            }

            CreatureType hatchingType = CreatureType.CHICKEN;

            if (this.thrower instanceof EntityPlayer) {
                org.bukkit.entity.Player player = (this.thrower == null) ? null : (org.bukkit.entity.Player) this.thrower.getBukkitEntity();

                PlayerEggThrowEvent event = new PlayerEggThrowEvent(player, (org.bukkit.entity.Egg) this.getBukkitEntity(), hatching, (byte) numHatching, hatchingType);
                this.world.getServer().getPluginManager().callEvent(event);

                hatching = event.isHatching();
                numHatching = event.getNumHatches();
                hatchingType = event.getHatchType();
            }

            if (hatching) {
                for (int k = 0; k < numHatching; k++) {
                    Entity entity = null;
                    switch (hatchingType) {
                        case CHICKEN:
                            entity = new EntityChicken(this.world);
                            break;
                        case COW:
                            entity = new EntityCow(this.world);
                            break;
                        case CREEPER:
                            entity = new EntityCreeper(this.world);
                            break;
                        case GHAST:
                            entity = new EntityGhast(this.world);
                            break;
                        case GIANT:
                            entity = new EntityGiantZombie(this.world);
                            break;
                        case PIG:
                            entity = new EntityPig(this.world);
                            break;
                        case PIG_ZOMBIE:
                            entity = new EntityPigZombie(this.world);
                            break;
                        case SHEEP:
                            entity = new EntitySheep(this.world);
                            break;
                        case SKELETON:
                            entity = new EntitySkeleton(this.world);
                            break;
                        case SPIDER:
                            entity = new EntitySpider(this.world);
                            break;
                        case ZOMBIE:
                            entity = new EntityZombie(this.world);
                            break;
                        case SQUID:
                            entity = new EntitySquid(this.world);
                            break;
                        case SLIME:
                            entity = new EntitySlime(this.world);
                            break;
                        case WOLF:
                            entity = new EntityWolf(this.world);
                            break;
                        case MONSTER:
                            entity = new EntityMonster(this.world);
                            break;
                        default:
                            entity = new EntityChicken(this.world);
                            break;
                    }

                    // The world we're spawning in accepts this creature
                    boolean isAnimal = entity instanceof EntityAnimal || entity instanceof EntityWaterAnimal;
                    if ((isAnimal && this.world.allowAnimals) || (!isAnimal && this.world.allowMonsters)) {
                        entity.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
                        this.world.addEntity(entity, SpawnReason.EGG);
                    }
                    // CraftBukkit end
                }
            }

            for (int l = 0; l < 8; ++l) {
                this.world.a("snowballpoof", this.locX, this.locY, this.locZ, 0.0D, 0.0D, 0.0D);
            }

            this.die();
        }

        this.locX += this.motX;
        this.locY += this.motY;
        this.locZ += this.motZ;
        float f1 = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);

        this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

        for (this.pitch = (float) (Math.atan2(this.motY, (double) f1) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
            ;
        }

        while (this.pitch - this.lastPitch >= 180.0F) {
            this.lastPitch += 360.0F;
        }

        while (this.yaw - this.lastYaw < -180.0F) {
            this.lastYaw -= 360.0F;
        }

        while (this.yaw - this.lastYaw >= 180.0F) {
            this.lastYaw += 360.0F;
        }

        this.pitch = this.lastPitch + (this.pitch - this.lastPitch) * 0.2F;
        this.yaw = this.lastYaw + (this.yaw - this.lastYaw) * 0.2F;
        float f2 = 0.99F;
        float f3 = 0.03F;

        if (this.ad()) {
            for (int i1 = 0; i1 < 4; ++i1) {
                float f4 = 0.25F;

                this.world.a("bubble", this.locX - this.motX * (double) f4, this.locY - this.motY * (double) f4, this.locZ - this.motZ * (double) f4, this.motX, this.motY, this.motZ);
            }

            f2 = 0.8F;
        }

        this.motX *= (double) f2;
        this.motY *= (double) f2;
        this.motZ *= (double) f2;
        this.motY -= (double) f3;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("xTile", (short) this.b);
        nbttagcompound.a("yTile", (short) this.c);
        nbttagcompound.a("zTile", (short) this.d);
        nbttagcompound.a("inTile", (byte) this.e);
        nbttagcompound.a("shake", (byte) this.a);
        nbttagcompound.a("inGround", (byte) (this.f ? 1 : 0));
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.b = nbttagcompound.d("xTile");
        this.c = nbttagcompound.d("yTile");
        this.d = nbttagcompound.d("zTile");
        this.e = nbttagcompound.c("inTile") & 255;
        this.a = nbttagcompound.c("shake") & 255;
        this.f = nbttagcompound.c("inGround") == 1;
    }

    public void b(EntityHuman entityhuman) {
        if (this.f && this.thrower == entityhuman && this.a <= 0 && entityhuman.inventory.pickup(new ItemStack(Item.ARROW, 1))) {
            this.world.makeSound(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityhuman.receive(this, 1);
            this.die();
        }
    }
}
