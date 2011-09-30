package net.minecraft.server;

public class EntitySlime extends EntityLiving implements IMonster {

    public float a;
    public float b;
    private int size = 0;

    public EntitySlime(World world) {
        super(world);
        this.texture = "/mob/slime.png";
        int i = 1 << this.random.nextInt(3);

        this.height = 0.0F;
        this.size = this.random.nextInt(20) + 10;
        this.setSize(i);
        this.ax = i;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, new Byte((byte) 1));
    }

    public void setSize(int i) {
        this.datawatcher.watch(16, new Byte((byte) i));
        this.b(0.6F * (float) i, 0.6F * (float) i);
        this.health = i * i;
        this.setPosition(this.locX, this.locY, this.locZ);
    }

    public int getSize() {
        return this.datawatcher.getByte(16);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Size", this.getSize() - 1);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setSize(nbttagcompound.e("Size") + 1);
    }

    public void s_() {
        this.b = this.a;
        boolean flag = this.onGround;

        super.s_();
        if (this.onGround && !flag) {
            int i = this.getSize();

            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * 3.1415927F * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;

                this.world.a("slime", this.locX + (double) f2, this.boundingBox.b, this.locZ + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            if (i > 2) {
                this.world.makeSound(this, "mob.slime", this.l(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.a = -0.5F;
        }

        this.a *= 0.6F;
    }

    protected void c_() {
        this.ad();
        EntityHuman entityhuman = this.world.findNearbyPlayer(this, 16.0D);

        if (entityhuman != null) {
            this.a(entityhuman, 10.0F, 20.0F);
        }

        if (this.onGround && this.size-- <= 0) {
            this.size = this.random.nextInt(20) + 10;
            if (entityhuman != null) {
                this.size /= 3;
            }

            this.aS = true;
            if (this.getSize() > 1) {
                this.world.makeSound(this, "mob.slime", this.l(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.a = 1.0F;
            this.aP = 1.0F - this.random.nextFloat() * 2.0F;
            this.aQ = (float) (1 * this.getSize());
        } else {
            this.aS = false;
            if (this.onGround) {
                this.aP = this.aQ = 0.0F;
            }
        }
    }

    public void die() {
        int i = this.getSize();

        if (!this.world.isStatic && i > 1 && this.health == 0) {
            // CraftBukkit start
            org.bukkit.event.entity.SlimeSplitEvent event = new org.bukkit.event.entity.SlimeSplitEvent(this.getBukkitEntity(), 4);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled() && event.getCount() > 0) {
                for (int j = 0; j < event.getCount(); ++j) {
                    float f = ((float) (j % 2) - 0.5F) * (float) i / 4.0F;
                    float f1 = ((float) (j / 2) - 0.5F) * (float) i / 4.0F;
                    EntitySlime entityslime = new EntitySlime(this.world);

                    entityslime.setSize(i / 2);
                    entityslime.setPositionRotation(this.locX + (double) f, this.locY + 0.5D, this.locZ + (double) f1, this.random.nextFloat() * 360.0F, 0.0F);
                    this.world.addEntity(entityslime);
                }
            }
            // CraftBukkit end
        }

        super.die();
    }

    public void a_(EntityHuman entityhuman) {
        int i = this.getSize();

        if (i > 1 && this.f(entityhuman) && (double) this.g(entityhuman) < 0.6D * (double) i && entityhuman.damageEntity(DamageSource.mobAttack(this), i)) {
            this.world.makeSound(this, "mob.slimeattack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String i() {
        return "mob.slime";
    }

    protected String j() {
        return "mob.slime";
    }

    protected int k() {
        return this.getSize() == 1 ? Item.SLIME_BALL.id : 0;
    }

    public boolean d() {
        Chunk chunk = this.world.getChunkAtWorldCoords(MathHelper.floor(this.locX), MathHelper.floor(this.locZ));

        return (this.getSize() == 1 || this.world.difficulty > 0) && this.random.nextInt(10) == 0 && chunk.a(987234911L).nextInt(10) == 0 && this.locY < 16.0D;
    }

    protected float l() {
        return 0.6F;
    }
}
