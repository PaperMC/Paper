package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntityEnderCrystal extends Entity {

    public int a;
    public int b;

    public EntityEnderCrystal(World world) {
        super(world);
        this.l = true;
        this.a(2.0F, 2.0F);
        this.height = this.length / 2.0F;
        this.b = 5;
        this.a = this.random.nextInt(100000);
    }

    protected boolean g_() {
        return false;
    }

    protected void c() {
        this.datawatcher.a(8, Integer.valueOf(this.b));
    }

    public void h() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        ++this.a;
        this.datawatcher.watch(8, Integer.valueOf(this.b));
        int i = MathHelper.floor(this.locX);
        int j = MathHelper.floor(this.locY);
        int k = MathHelper.floor(this.locZ);

        if (this.world.worldProvider instanceof WorldProviderTheEnd && this.world.getType(i, j, k) != Blocks.FIRE) {
            // CraftBukkit start
            if (!CraftEventFactory.callBlockIgniteEvent(this.world, i, j, k, this).isCancelled()) {
                this.world.setTypeUpdate(i, j, k, Blocks.FIRE);
            }
            // CraftBukkit end
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {}

    protected void a(NBTTagCompound nbttagcompound) {}

    public boolean R() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            if (!this.dead && !this.world.isStatic) {
                // CraftBukkit start - All non-living entities need this
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end

                this.b = 0;
                if (this.b <= 0) {
                    this.die();
                    if (!this.world.isStatic) {
                        this.world.explode(this, this.locX, this.locY, this.locZ, 6.0F, true); // CraftBukkit - (Entity) null -> this
                    }
                }
            }

            return true;
        }
    }
}
