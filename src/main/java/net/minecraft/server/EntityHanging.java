package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Painting;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
// CraftBukkit end

public abstract class EntityHanging extends Entity {

    private int e;
    public int direction;
    public int x;
    public int y;
    public int z;

    public EntityHanging(World world) {
        super(world);
        this.height = 0.0F;
        this.a(0.5F, 0.5F);
    }

    public EntityHanging(World world, int i, int j, int k, int l) {
        this(world);
        this.x = i;
        this.y = j;
        this.z = k;
    }

    protected void c() {}

    public void setDirection(int i) {
        this.direction = i;
        this.lastYaw = this.yaw = (float) (i * 90);
        float f = (float) this.f();
        float f1 = (float) this.i();
        float f2 = (float) this.f();

        if (i != 2 && i != 0) {
            f = 0.5F;
        } else {
            f2 = 0.5F;
            this.yaw = this.lastYaw = (float) (Direction.f[i] * 90);
        }

        f /= 32.0F;
        f1 /= 32.0F;
        f2 /= 32.0F;
        float f3 = (float) this.x + 0.5F;
        float f4 = (float) this.y + 0.5F;
        float f5 = (float) this.z + 0.5F;
        float f6 = 0.5625F;

        if (i == 2) {
            f5 -= f6;
        }

        if (i == 1) {
            f3 -= f6;
        }

        if (i == 0) {
            f5 += f6;
        }

        if (i == 3) {
            f3 += f6;
        }

        if (i == 2) {
            f3 -= this.c(this.f());
        }

        if (i == 1) {
            f5 += this.c(this.f());
        }

        if (i == 0) {
            f3 += this.c(this.f());
        }

        if (i == 3) {
            f5 -= this.c(this.f());
        }

        f4 += this.c(this.i());
        this.setPosition((double) f3, (double) f4, (double) f5);
        float f7 = -0.03125F;

        this.boundingBox.b((double) (f3 - f - f7), (double) (f4 - f1 - f7), (double) (f5 - f2 - f7), (double) (f3 + f + f7), (double) (f4 + f1 + f7), (double) (f5 + f2 + f7));
    }

    private float c(int i) {
        return i == 32 ? 0.5F : (i == 64 ? 0.5F : 0.0F);
    }

    public void h() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        if (this.e++ == 100 && !this.world.isStatic) {
            this.e = 0;
            if (!this.dead && !this.survives()) {
                // CraftBukkit start
                Material material = this.world.getType((int) this.locX, (int) this.locY, (int) this.locZ).getMaterial();
                HangingBreakEvent.RemoveCause cause;

                if (!material.equals(Material.AIR)) {
                    // TODO: This feels insufficient to catch 100% of suffocation cases
                    cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
                } else {
                    cause = HangingBreakEvent.RemoveCause.PHYSICS;
                }

                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), cause);
                this.world.getServer().getPluginManager().callEvent(event);

                PaintingBreakEvent paintingEvent = null;
                if (this instanceof EntityPainting) {
                    // Fire old painting event until it can be removed
                    paintingEvent = new PaintingBreakEvent((Painting) this.getBukkitEntity(), PaintingBreakEvent.RemoveCause.valueOf(cause.name()));
                    paintingEvent.setCancelled(event.isCancelled());
                    this.world.getServer().getPluginManager().callEvent(paintingEvent);
                }

                if (dead || event.isCancelled() || (paintingEvent != null && paintingEvent.isCancelled())) {
                    return;
                }
                // CraftBukkit end

                this.die();
                this.b((Entity) null);
            }
        }
    }

    public boolean survives() {
        if (!this.world.getCubes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
            int i = Math.max(1, this.f() / 16);
            int j = Math.max(1, this.i() / 16);
            int k = this.x;
            int l = this.y;
            int i1 = this.z;

            if (this.direction == 2) {
                k = MathHelper.floor(this.locX - (double) ((float) this.f() / 32.0F));
            }

            if (this.direction == 1) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.f() / 32.0F));
            }

            if (this.direction == 0) {
                k = MathHelper.floor(this.locX - (double) ((float) this.f() / 32.0F));
            }

            if (this.direction == 3) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.f() / 32.0F));
            }

            l = MathHelper.floor(this.locY - (double) ((float) this.i() / 32.0F));

            for (int j1 = 0; j1 < i; ++j1) {
                for (int k1 = 0; k1 < j; ++k1) {
                    Material material;

                    if (this.direction != 2 && this.direction != 0) {
                        material = this.world.getType(this.x, l + k1, i1 + j1).getMaterial();
                    } else {
                        material = this.world.getType(k + j1, l + k1, this.z).getMaterial();
                    }

                    if (!material.isBuildable()) {
                        return false;
                    }
                }
            }

            List list = this.world.getEntities(this, this.boundingBox);
            Iterator iterator = list.iterator();

            Entity entity;

            do {
                if (!iterator.hasNext()) {
                    return true;
                }

                entity = (Entity) iterator.next();
            } while (!(entity instanceof EntityHanging));

            return false;
        }
    }

    public boolean R() {
        return true;
    }

    public boolean i(Entity entity) {
        return entity instanceof EntityHuman ? this.damageEntity(DamageSource.playerAttack((EntityHuman) entity), 0.0F) : false;
    }

    public void i(int i) {
        this.world.X();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            if (!this.dead && !this.world.isStatic) {
                // CraftBukkit start
                HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.DEFAULT);
                PaintingBreakEvent paintingEvent = null;
                if (damagesource.getEntity() != null) {
                    event = new org.bukkit.event.hanging.HangingBreakByEntityEvent((Hanging) this.getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());

                    if (this instanceof EntityPainting) {
                        // Fire old painting event until it can be removed
                        paintingEvent = new org.bukkit.event.painting.PaintingBreakByEntityEvent((Painting) this.getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());
                    }
                } else if (damagesource.c()) {
                    event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.EXPLOSION);
                }

                this.world.getServer().getPluginManager().callEvent(event);

                if (paintingEvent != null) {
                    paintingEvent.setCancelled(event.isCancelled());
                    this.world.getServer().getPluginManager().callEvent(paintingEvent);
                }

                if (dead || event.isCancelled() || (paintingEvent != null && paintingEvent.isCancelled())) {
                    return true;
                }
                // CraftBukkit end

                this.die();
                this.Q();
                this.b(damagesource.getEntity());
            }

            return true;
        }
    }

    public void move(double d0, double d1, double d2) {
        if (!this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (dead) return; // CraftBukkit

            this.die();
            this.b((Entity) null);
        }
    }

    public void g(double d0, double d1, double d2) {
        if (false && !this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.die();
            this.b((Entity) null);
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Direction", (byte) this.direction);
        nbttagcompound.setInt("TileX", this.x);
        nbttagcompound.setInt("TileY", this.y);
        nbttagcompound.setInt("TileZ", this.z);
        switch (this.direction) {
        case 0:
            nbttagcompound.setByte("Dir", (byte) 2);
            break;

        case 1:
            nbttagcompound.setByte("Dir", (byte) 1);
            break;

        case 2:
            nbttagcompound.setByte("Dir", (byte) 0);
            break;

        case 3:
            nbttagcompound.setByte("Dir", (byte) 3);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("Direction", 99)) {
            this.direction = nbttagcompound.getByte("Direction");
        } else {
            switch (nbttagcompound.getByte("Dir")) {
            case 0:
                this.direction = 2;
                break;

            case 1:
                this.direction = 1;
                break;

            case 2:
                this.direction = 0;
                break;

            case 3:
                this.direction = 3;
            }
        }

        this.x = nbttagcompound.getInt("TileX");
        this.y = nbttagcompound.getInt("TileY");
        this.z = nbttagcompound.getInt("TileZ");
        this.setDirection(this.direction);
    }

    public abstract int f();

    public abstract int i();

    public abstract void b(Entity entity);

    protected boolean V() {
        return false;
    }
}
