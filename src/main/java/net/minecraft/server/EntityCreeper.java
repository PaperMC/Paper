package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
// CraftBukkit end

public class EntityCreeper extends EntityMonster {

    int fuseTicks;
    int b;

    public EntityCreeper(World world) {
        super(world);
        this.texture = "/mob/creeper.png";
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.datawatcher.a(17) == 1) {
            nbttagcompound.a("powered", true);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.datawatcher.watch(17, Byte.valueOf((byte) (nbttagcompound.m("powered") ? 1 : 0)));
    }

    protected void b(Entity entity, float f) {
        if (!this.world.isStatic) {
            if (this.fuseTicks > 0) {
                this.e(-1);
                --this.fuseTicks;
                if (this.fuseTicks < 0) {
                    this.fuseTicks = 0;
                }
            }
        }
    }

    public void o_() {
        this.b = this.fuseTicks;
        if (this.world.isStatic) {
            int i = this.x();

            if (i > 0 && this.fuseTicks == 0) {
                this.world.makeSound(this, "random.fuse", 1.0F, 0.5F);
            }

            this.fuseTicks += i;
            if (this.fuseTicks < 0) {
                this.fuseTicks = 0;
            }

            if (this.fuseTicks >= 30) {
                this.fuseTicks = 30;
            }
        }

        super.o_();
        if (this.target == null && this.fuseTicks > 0) {
            this.e(-1);
            --this.fuseTicks;
            if (this.fuseTicks < 0) {
                this.fuseTicks = 0;
            }
        }
    }

    protected String h() {
        return "mob.creeper";
    }

    protected String i() {
        return "mob.creeperdeath";
    }

    public void die(Entity entity) {
        super.die(entity);
        if (entity instanceof EntitySkeleton) {
            this.b(Item.GOLD_RECORD.id + this.random.nextInt(2), 1);
        }
    }

    protected void a(Entity entity, float f) {
        if (!this.world.isStatic) {
            int i = this.x();

            if ((i > 0 || f >= 3.0F) && (i <= 0 || f >= 7.0F)) {
                this.e(-1);
                --this.fuseTicks;
                if (this.fuseTicks < 0) {
                    this.fuseTicks = 0;
                }
            } else {
                if (this.fuseTicks == 0) {
                    this.world.makeSound(this, "random.fuse", 1.0F, 0.5F);
                }

                this.e(1);
                ++this.fuseTicks;
                if (this.fuseTicks >= 30) {
                    // CraftBukkit start
                    float radius = this.isPowered() ? 6.0F : 3.0F;

                    ExplosionPrimeEvent event = new ExplosionPrimeEvent(CraftEntity.getEntity(this.world.getServer(), this), radius, false);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
                        this.die();
                    } else {
                        this.fuseTicks = 0;
                    }
                    // CraftBukkit end
                }

                this.e = true;
            }
        }
    }

    public boolean isPowered() {
        return this.datawatcher.a(17) == 1;
    }

    protected int j() {
        return Item.SULPHUR.id;
    }

    private int x() {
        return this.datawatcher.a(16);
    }

    private void e(int i) {
        this.datawatcher.watch(16, Byte.valueOf((byte) i));
    }

    public void a(EntityWeatherStorm entityweatherstorm) {
        super.a(entityweatherstorm);

        // CraftBukkit start
        CreeperPowerEvent event = new CreeperPowerEvent(this.getBukkitEntity(), entityweatherstorm.getBukkitEntity(), CreeperPowerEvent.PowerCause.LIGHTNING);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        this.setPowered(true);
    }

    public void setPowered(boolean powered) {
        if (!powered) {
            this.datawatcher.watch(17, Byte.valueOf((byte) 0));
        } else
        // CraftBukkit end
        this.datawatcher.watch(17, Byte.valueOf((byte) 1));
    }
}
