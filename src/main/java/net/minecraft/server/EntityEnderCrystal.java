package net.minecraft.server;

public class EntityEnderCrystal extends Entity {

    public int a = 0;
    public int b;

    public EntityEnderCrystal(World world) {
        super(world);
        this.m = true;
        this.a(2.0F, 2.0F);
        this.height = this.length / 2.0F;
        this.b = 5;
        this.a = this.random.nextInt(100000);
    }

    protected boolean e_() {
        return false;
    }

    protected void a() {
        this.datawatcher.a(8, Integer.valueOf(this.b));
    }

    public void h_() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        ++this.a;
        this.datawatcher.watch(8, Integer.valueOf(this.b));
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);

        if (this.world.getTypeId(i, j, k) != Block.FIRE.id) {
            this.world.setTypeId(i, j, k, Block.FIRE.id);
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

    public boolean L() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.dead && !this.world.isStatic) {
            // CraftBukkit start - All non-living entities need this
            if (org.bukkit.craftbukkit.event.CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, i)) {
                return false;
            }
            // CraftBukkit end

            this.b = 0;
            if (this.b <= 0) {
                this.die();
                if (!this.world.isStatic) {
                    this.world.explode(this, this.locX, this.locY, this.locZ, 6.0F); // CraftBukkit - (Entity) null -> this
                }
            }
        }

        return true;
    }
}
