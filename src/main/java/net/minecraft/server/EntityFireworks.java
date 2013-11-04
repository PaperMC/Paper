package net.minecraft.server;

public class EntityFireworks extends Entity {

    private int ticksFlown;
    public int expectedLifespan; // CraftBukkit - private -> public

    public EntityFireworks(World world) {
        super(world);
        this.a(0.25F, 0.25F);
    }

    protected void c() {
        this.datawatcher.a(8, 5);
    }

    public EntityFireworks(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world);
        this.ticksFlown = 0;
        this.a(0.25F, 0.25F);
        this.setPosition(d0, d1, d2);
        this.height = 0.0F;
        int i = 1;

        if (itemstack != null && itemstack.hasTag()) {
            this.datawatcher.watch(8, itemstack);
            NBTTagCompound nbttagcompound = itemstack.getTag();
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("Fireworks");

            if (nbttagcompound1 != null) {
                i += nbttagcompound1.getByte("Flight");
            }
        }

        this.motX = this.random.nextGaussian() * 0.001D;
        this.motZ = this.random.nextGaussian() * 0.001D;
        this.motY = 0.05D;
        this.expectedLifespan = 10 * i + this.random.nextInt(6) + this.random.nextInt(7);
    }

    public void h() {
        this.T = this.locX;
        this.U = this.locY;
        this.V = this.locZ;
        super.h();
        this.motX *= 1.15D;
        this.motZ *= 1.15D;
        this.motY += 0.04D;
        this.move(this.motX, this.motY, this.motZ);
        float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);

        this.yaw = (float) (Math.atan2(this.motX, this.motZ) * 180.0D / 3.1415927410125732D);

        for (this.pitch = (float) (Math.atan2(this.motY, (double) f) * 180.0D / 3.1415927410125732D); this.pitch - this.lastPitch < -180.0F; this.lastPitch -= 360.0F) {
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
        if (this.ticksFlown == 0) {
            this.world.makeSound(this, "fireworks.launch", 3.0F, 1.0F);
        }

        ++this.ticksFlown;
        if (this.world.isStatic && this.ticksFlown % 2 < 2) {
            this.world.addParticle("fireworksSpark", this.locX, this.locY - 0.3D, this.locZ, this.random.nextGaussian() * 0.05D, -this.motY * 0.5D, this.random.nextGaussian() * 0.05D);
        }

        if (!this.world.isStatic && this.ticksFlown > this.expectedLifespan) {
            this.world.broadcastEntityEffect(this, (byte) 17);
            this.die();
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInt("Life", this.ticksFlown);
        nbttagcompound.setInt("LifeTime", this.expectedLifespan);
        ItemStack itemstack = this.datawatcher.getItemStack(8);

        if (itemstack != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();

            itemstack.save(nbttagcompound1);
            nbttagcompound.set("FireworksItem", nbttagcompound1);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.ticksFlown = nbttagcompound.getInt("Life");
        this.expectedLifespan = nbttagcompound.getInt("LifeTime");
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompound("FireworksItem");

        if (nbttagcompound1 != null) {
            ItemStack itemstack = ItemStack.createStack(nbttagcompound1);

            if (itemstack != null) {
                this.datawatcher.watch(8, itemstack);
            }
        }
    }

    public float d(float f) {
        return super.d(f);
    }

    public boolean av() {
        return false;
    }
}
