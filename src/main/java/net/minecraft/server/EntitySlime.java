package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
// CraftBukkit end

public class EntitySlime extends EntityInsentient implements IMonster {

    public float h;
    public float i;
    public float j;
    private int jumpDelay;
    private Entity lastTarget; // CraftBukkit

    public EntitySlime(World world) {
        super(world);
        int i = 1 << this.random.nextInt(3);

        this.height = 0.0F;
        this.jumpDelay = this.random.nextInt(20) + 10;
        this.setSize(i);
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    // CraftBukkit - protected -> public
    public void setSize(int i) {
        boolean updateMaxHealth = this.getMaxHealth() == this.maxHealth; // CraftBukkit
        this.datawatcher.watch(16, new Byte((byte) i));
        this.a(0.6F * (float) i, 0.6F * (float) i);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.a(GenericAttributes.a).a((double) (i * i));
        // CraftBukkit start
        if (updateMaxHealth) {
            this.maxHealth = this.getMaxHealth();
        }
        this.setHealth(this.maxHealth);
        // CraftBukkit end
        this.b = i;
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("Size", this.getSize() - 1);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSize(nbttagcompound.getInt("Size") + 1);
    }

    protected String bF() {
        return "slime";
    }

    protected String bL() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    public void l_() {
        if (!this.world.isStatic && this.world.difficulty == 0 && this.getSize() > 0) {
            this.dead = true;
        }

        this.i += (this.h - this.i) * 0.5F;
        this.j = this.i;
        boolean flag = this.onGround;

        super.l_();
        int i;

        if (this.onGround && !flag) {
            i = this.getSize();

            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * 3.1415927F * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;

                this.world.addParticle(this.bF(), this.locX + (double) f2, this.boundingBox.b, this.locZ + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            if (this.bM()) {
                this.makeSound(this.bL(), this.aW(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.h = -0.5F;
        } else if (!this.onGround && flag) {
            this.h = 1.0F;
        }

        this.bI();
        if (this.world.isStatic) {
            i = this.getSize();
            this.a(0.6F * (float) i, 0.6F * (float) i);
        }
    }

    protected void bh() {
        this.bk();
        // CraftBukkit start
        Entity entityhuman = this.world.findNearbyVulnerablePlayer(this, 16.0D); // EntityHuman -> Entity
        EntityTargetEvent event = null;

        if (entityhuman != null && !entityhuman.equals(lastTarget)) {
            event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.CLOSEST_PLAYER);
        } else if (lastTarget != null && entityhuman == null) {
            event = CraftEventFactory.callEntityTargetEvent(this, entityhuman, EntityTargetEvent.TargetReason.FORGOT_TARGET);
        }

        if (event != null && !event.isCancelled()) {
            entityhuman = event.getTarget() == null ? null : ((CraftEntity) event.getTarget()).getHandle();
        }

        this.lastTarget = entityhuman;
        // CraftBukkit end

        if (entityhuman != null) {
            this.a(entityhuman, 10.0F, 20.0F);
        }

        if (this.onGround && this.jumpDelay-- <= 0) {
            this.jumpDelay = this.bH();
            if (entityhuman != null) {
                this.jumpDelay /= 3;
            }

            this.bd = true;
            if (this.bO()) {
                this.makeSound(this.bL(), this.aW(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.be = 1.0F - this.random.nextFloat() * 2.0F;
            this.bf = (float) (1 * this.getSize());
        } else {
            this.bd = false;
            if (this.onGround) {
                this.be = this.bf = 0.0F;
            }
        }
    }

    protected void bI() {
        this.h *= 0.6F;
    }

    protected int bH() {
        return this.random.nextInt(20) + 10;
    }

    protected EntitySlime bG() {
        return new EntitySlime(this.world);
    }

    public void die() {
        int i = this.getSize();

        if (!this.world.isStatic && i > 1 && this.getHealth() <= 0.0F) {
            int j = 2 + this.random.nextInt(3);

            // CraftBukkit start
            SlimeSplitEvent event = new SlimeSplitEvent((org.bukkit.entity.Slime) this.getBukkitEntity(), j);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getCount() > 0) {
                j = event.getCount();
            } else {
                super.die();
                return;
            }
            // CraftBukkit end

            for (int k = 0; k < j; ++k) {
                float f = ((float) (k % 2) - 0.5F) * (float) i / 4.0F;
                float f1 = ((float) (k / 2) - 0.5F) * (float) i / 4.0F;
                EntitySlime entityslime = this.bG();

                entityslime.setSize(i / 2);
                entityslime.setPositionRotation(this.locX + (double) f, this.locY + 0.5D, this.locZ + (double) f1, this.random.nextFloat() * 360.0F, 0.0F);
                this.world.addEntity(entityslime, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.SLIME_SPLIT); // CraftBukkit - SpawnReason
            }
        }

        super.die();
    }

    public void b_(EntityHuman entityhuman) {
        if (this.bJ()) {
            int i = this.getSize();

            if (this.o(entityhuman) && this.e(entityhuman) < 0.6D * (double) i * 0.6D * (double) i && entityhuman.damageEntity(DamageSource.mobAttack(this), (float) this.bK())) {
                this.makeSound("mob.attack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    protected boolean bJ() {
        return this.getSize() > 1;
    }

    protected int bK() {
        return this.getSize();
    }

    protected String aK() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected String aL() {
        return "mob.slime." + (this.getSize() > 1 ? "big" : "small");
    }

    protected int getLootId() {
        return this.getSize() == 1 ? Item.SLIME_BALL.id : 0;
    }

    public boolean canSpawn() {
        Chunk chunk = this.world.getChunkAtWorldCoords(MathHelper.floor(this.locX), MathHelper.floor(this.locZ));

        if (this.world.getWorldData().getType() == WorldType.FLAT && this.random.nextInt(4) != 1) {
            return false;
        } else {
            if (this.getSize() == 1 || this.world.difficulty > 0) {
                BiomeBase biomebase = this.world.getBiome(MathHelper.floor(this.locX), MathHelper.floor(this.locZ));

                if (biomebase == BiomeBase.SWAMPLAND && this.locY > 50.0D && this.locY < 70.0D && this.random.nextFloat() < 0.5F && this.random.nextFloat() < this.world.x() && this.world.getLightLevel(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) <= this.random.nextInt(8)) {
                    return super.canSpawn();
                }

                if (this.random.nextInt(10) == 0 && chunk.a(987234911L).nextInt(10) == 0 && this.locY < 40.0D) {
                    return super.canSpawn();
                }
            }

            return false;
        }
    }

    protected float aW() {
        return 0.4F * (float) this.getSize();
    }

    public int bl() {
        return 0;
    }

    protected boolean bO() {
        return this.getSize() > 0;
    }

    protected boolean bM() {
        return this.getSize() > 2;
    }
}
