package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

public class EntityEvokerFangs extends Entity {

    private int b;
    private boolean c;
    private int d;
    private boolean e;
    private EntityLiving f;
    private UUID g;

    public EntityEvokerFangs(EntityTypes<? extends EntityEvokerFangs> entitytypes, World world) {
        super(entitytypes, world);
        this.d = 22;
    }

    public EntityEvokerFangs(World world, double d0, double d1, double d2, float f, int i, EntityLiving entityliving) {
        this(EntityTypes.EVOKER_FANGS, world);
        this.b = i;
        this.a(entityliving);
        this.yaw = f * 57.295776F;
        this.setPosition(d0, d1, d2);
    }

    @Override
    protected void initDatawatcher() {}

    public void a(@Nullable EntityLiving entityliving) {
        this.f = entityliving;
        this.g = entityliving == null ? null : entityliving.getUniqueID();
    }

    @Nullable
    public EntityLiving getOwner() {
        if (this.f == null && this.g != null && this.world instanceof WorldServer) {
            Entity entity = ((WorldServer) this.world).getEntity(this.g);

            if (entity instanceof EntityLiving) {
                this.f = (EntityLiving) entity;
            }
        }

        return this.f;
    }

    @Override
    protected void loadData(NBTTagCompound nbttagcompound) {
        this.b = nbttagcompound.getInt("Warmup");
        if (nbttagcompound.b("Owner")) {
            this.g = nbttagcompound.a("Owner");
        }

    }

    @Override
    protected void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Warmup", this.b);
        if (this.g != null) {
            nbttagcompound.a("Owner", this.g);
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide) {
            if (this.e) {
                --this.d;
                if (this.d == 14) {
                    for (int i = 0; i < 12; ++i) {
                        double d0 = this.locX() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                        double d1 = this.locY() + 0.05D + this.random.nextDouble();
                        double d2 = this.locZ() + (this.random.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                        double d3 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.random.nextDouble() * 0.3D;
                        double d5 = (this.random.nextDouble() * 2.0D - 1.0D) * 0.3D;

                        this.world.addParticle(Particles.CRIT, d0, d1 + 1.0D, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.b < 0) {
            if (this.b == -8) {
                List<EntityLiving> list = this.world.a(EntityLiving.class, this.getBoundingBox().grow(0.2D, 0.0D, 0.2D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    EntityLiving entityliving = (EntityLiving) iterator.next();

                    this.c(entityliving);
                }
            }

            if (!this.c) {
                this.world.broadcastEntityEffect(this, (byte) 4);
                this.c = true;
            }

            if (--this.d < 0) {
                this.die();
            }
        }

    }

    private void c(EntityLiving entityliving) {
        EntityLiving entityliving1 = this.getOwner();

        if (entityliving.isAlive() && !entityliving.isInvulnerable() && entityliving != entityliving1) {
            if (entityliving1 == null) {
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = this; // CraftBukkit
                entityliving.damageEntity(DamageSource.MAGIC, 6.0F);
                org.bukkit.craftbukkit.event.CraftEventFactory.entityDamage = null; // CraftBukkit
            } else {
                if (entityliving1.r(entityliving)) {
                    return;
                }

                entityliving.damageEntity(DamageSource.c(this, entityliving1), 6.0F);
            }

        }
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntity(this);
    }
}
