package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityCreeper extends EntityMonster {

    int fuseTicks;
    int b;
    private int record = -1; // CraftBukkit

    public EntityCreeper(World world) {
        super(world);
        this.texture = "/mob/creeper.png";
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalSwell(this));
        this.goalSelector.a(3, new PathfinderGoalAvoidPlayer(this, EntityOcelot.class, 6.0F, 0.25F, 0.3F));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 0.25F, false));
        this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 0.2F));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
    }

    public boolean c_() {
        return true;
    }

    public int getMaxHealth() {
        return 20;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) -1));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        if (this.datawatcher.getByte(17) == 1) {
            nbttagcompound.setBoolean("powered", true);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.datawatcher.watch(17, Byte.valueOf((byte) (nbttagcompound.getBoolean("powered") ? 1 : 0)));
    }

    public void F_() {
        if (this.isAlive()) {
            this.b = this.fuseTicks;
            int i = this.A();

            if (i > 0 && this.fuseTicks == 0) {
                this.world.makeSound(this, "random.fuse", 1.0F, 0.5F);
            }

            this.fuseTicks += i;
            if (this.fuseTicks < 0) {
                this.fuseTicks = 0;
            }

            if (this.fuseTicks >= 30) {
                this.fuseTicks = 30;
                // CraftBukkit start
                float radius = this.isPowered() ? 6.0F : 3.0F;

                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), radius, false);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
                    this.die();
                } else {
                    this.fuseTicks = 0;
                }
                // CraftBukkit end
            }
        }

        super.F_();
    }

    protected String j() {
        return "mob.creeper";
    }

    protected String k() {
        return "mob.creeperdeath";
    }

    public void die(DamageSource damagesource) {
        // CraftBukkit start - rearranged the method (super call to end, drop to dropDeathLoot)
        if (damagesource.getEntity() instanceof EntitySkeleton) {
            // this.b(Item.RECORD_1.id + this.random.nextInt(10), 1); // CraftBukkit
            this.record = Item.RECORD_1.id + this.random.nextInt(10);
        }
        super.die(damagesource);
        // CraftBukkit end
    }

    // CraftBukkit start - whole method
    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.getLootId();

        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        if (j > 0) {
            int k = this.random.nextInt(3);

            if (i > 0) {
                k += this.random.nextInt(i + 1);
            }

            if (k > 0) {
                loot.add(new org.bukkit.inventory.ItemStack(j, k));
            }
        }

        // Drop a music disc?
        if (this.record != -1) {
            loot.add(new org.bukkit.inventory.ItemStack(this.record, 1));
            this.record = -1;
        }

        CraftEventFactory.callEntityDeathEvent(this, loot); // raise event even for those times when the entity does not drop loot
    }
    // CraftBukkit end

    public boolean a(Entity entity) {
        return true;
    }

    public boolean isPowered() {
        return this.datawatcher.getByte(17) == 1;
    }

    protected int getLootId() {
        return Item.SULPHUR.id;
    }

    public int A() {
        return this.datawatcher.getByte(16);
    }

    public void c(int i) {
        this.datawatcher.watch(16, Byte.valueOf((byte) i));
    }

    public void a(EntityWeatherLighting entityweatherlighting) {
        super.a(entityweatherlighting);

        // CraftBukkit start
        if (CraftEventFactory.callCreeperPowerEvent(this, entityweatherlighting, org.bukkit.event.entity.CreeperPowerEvent.PowerCause.LIGHTNING).isCancelled()) {
            return;
        }

        this.setPowered(true);
    }

    public void setPowered(boolean powered) {
        if (!powered) {
            this.datawatcher.watch(17, Byte.valueOf((byte) 0));
        } else {
            this.datawatcher.watch(17, Byte.valueOf((byte) 1));
        }
        // CraftBukkit end
    }
}
