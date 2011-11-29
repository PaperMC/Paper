package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntitySpider extends EntityMonster {

    public EntitySpider(World world) {
        super(world);
        this.texture = "/mob/spider.png";
        this.b(1.4F, 0.9F);
        this.aY = 0.8F;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, new Byte((byte) 0));
    }

    public void d() {
        super.d();
    }

    public void w_() {
        super.w_();
        if (!this.world.isStatic) {
            this.a(this.positionChanged);
        }
    }

    public int getMaxHealth() {
        return 16;
    }

    public double q() {
        return (double) this.length * 0.75D - 0.5D;
    }

    protected boolean g_() {
        return false;
    }

    protected Entity findTarget() {
        float f = this.a(1.0F);

        if (f < 0.5F) {
            double d0 = 16.0D;

            return this.world.findNearbyVulnerablePlayer(this, d0);
        } else {
            return null;
        }
    }

    protected String c_() {
        return "mob.spider";
    }

    protected String m() {
        return "mob.spider";
    }

    protected String n() {
        return "mob.spiderdeath";
    }

    protected void a(Entity entity, float f) {
        float f1 = this.a(1.0F);

        if (f1 > 0.5F && this.random.nextInt(100) == 0) {
            // CraftBukkit start
            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), null, EntityTargetEvent.TargetReason.FORGOT_TARGET);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.target = null;
                } else {
                    this.target = ((CraftEntity) event.getTarget()).getHandle();
                }
                return;
            }
            // CraftBukkit end
        } else {
            if (f > 2.0F && f < 6.0F && this.random.nextInt(10) == 0) {
                if (this.onGround) {
                    double d0 = entity.locX - this.locX;
                    double d1 = entity.locZ - this.locZ;
                    float f2 = MathHelper.a(d0 * d0 + d1 * d1);

                    this.motX = d0 / (double) f2 * 0.5D * 0.800000011920929D + this.motX * 0.20000000298023224D;
                    this.motZ = d1 / (double) f2 * 0.5D * 0.800000011920929D + this.motZ * 0.20000000298023224D;
                    this.motY = 0.4000000059604645D;
                }
            } else {
                super.a(entity, f);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int e() {
        return Item.STRING.id;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        super.dropDeathLoot(flag, i);
        if (flag && (this.random.nextInt(3) == 0 || this.random.nextInt(1 + i) > 0)) {
            this.b(Item.SPIDER_EYE.id, 1);
        }
    }

    public boolean r() {
        return this.o_();
    }

    public void s() {}

    public MonsterType getMonsterType() {
        return MonsterType.ARTHROPOD;
    }

    public boolean a(MobEffect mobeffect) {
        return mobeffect.getEffectId() == MobEffectList.POISON.id ? false : super.a(mobeffect);
    }

    public boolean o_() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void a(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.datawatcher.watch(16, Byte.valueOf(b0));
    }
}
