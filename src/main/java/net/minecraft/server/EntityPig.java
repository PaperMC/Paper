package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.PigZapEvent;
// CraftBukkit end

public class EntityPig extends EntityAnimal {

    public EntityPig(World world) {
        super(world);
        this.texture = "/mob/pig.png";
        this.b(0.9F, 0.9F);
    }

    public int getMaxHealth() {
        return 10;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Saddle", this.hasSaddle());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSaddle(nbttagcompound.getBoolean("Saddle"));
    }

    protected String c_() {
        return "mob.pig";
    }

    protected String m() {
        return "mob.pig";
    }

    protected String n() {
        return "mob.pigdeath";
    }

    public boolean b(EntityHuman entityhuman) {
        if (super.b(entityhuman)) {
            return true;
        } else if (this.hasSaddle() && !this.world.isStatic && (this.passenger == null || this.passenger == entityhuman)) {
            entityhuman.mount(this);
            return true;
        } else {
            return false;
        }
    }

    protected int e() {
        return this.z() ? Item.GRILLED_PORK.id : Item.PORK.id;
    }

    public boolean hasSaddle() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSaddle(boolean flag) {
        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) 0));
        }
    }

    public void a(EntityWeatherLighting entityweatherlighting) {
        if (!this.world.isStatic) {
            EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);

            // CraftBukkit start
            PigZapEvent event = new PigZapEvent(this.getBukkitEntity(), entityweatherlighting.getBukkitEntity(), entitypigzombie.getBukkitEntity());
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            entitypigzombie.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
             // CraftBukkit - added a reason for spawning this creature
            this.world.addEntity(entitypigzombie, SpawnReason.LIGHTNING);
            this.die();
        }
    }

    protected void b(float f) {
        super.b(f);
        if (f > 5.0F && this.passenger instanceof EntityHuman) {
            ((EntityHuman) this.passenger).a((Statistic) AchievementList.u);
        }
    }

    protected EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityPig(this.world);
    }
}
