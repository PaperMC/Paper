package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.entity.Painting;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
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
        this.e = 0;
        this.direction = 0;
        this.height = 0.0F;
        this.a(0.5F, 0.5F);
    }

    public EntityHanging(World world, int i, int j, int k, int l) {
        this(world);
        this.x = i;
        this.y = j;
        this.z = k;
    }

    protected void a() {}

    public void setDirection(int i) {
        this.direction = i;
        this.lastYaw = this.yaw = (float) (i * 90);
        float f = (float) this.d();
        float f1 = (float) this.g();
        float f2 = (float) this.d();

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
            f3 -= this.g(this.d());
        }

        if (i == 1) {
            f5 += this.g(this.d());
        }

        if (i == 0) {
            f3 += this.g(this.d());
        }

        if (i == 3) {
            f5 -= this.g(this.d());
        }

        f4 += this.g(this.g());
        this.setPosition((double) f3, (double) f4, (double) f5);
        float f7 = -0.03125F;

        this.boundingBox.b((double) (f3 - f - f7), (double) (f4 - f1 - f7), (double) (f5 - f2 - f7), (double) (f3 + f + f7), (double) (f4 + f1 + f7), (double) (f5 + f2 + f7));
    }

    private float g(int i) {
        return i == 32 ? 0.5F : (i == 64 ? 0.5F : 0.0F);
    }

    public void j_() {
        if (this.e++ == 100 && !this.world.isStatic) {
            this.e = 0;
            if (!this.dead && !this.survives()) {
                // CraftBukkit start
                if (this instanceof EntityPainting) {
                    Material material = this.world.getMaterial((int) this.locX, (int) this.locY, (int) this.locZ);
                    RemoveCause cause;

                    if (material.equals(Material.WATER)) {
                        cause = RemoveCause.WATER;
                    } else if (!material.equals(Material.AIR)) {
                        // TODO: This feels insufficient to catch 100% of suffocation cases
                        cause = RemoveCause.OBSTRUCTION;
                    } else {
                        cause = RemoveCause.PHYSICS;
                    }

                    PaintingBreakEvent event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), cause);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled() || dead) {
                        return;
                    }
                }
                // CraftBukkit end

                this.die();
                this.h();
            }
        }
    }

    public boolean survives() {
        if (!this.world.getCubes(this, this.boundingBox).isEmpty()) {
            return false;
        } else {
            int i = Math.max(1, this.d() / 16);
            int j = Math.max(1, this.g() / 16);
            int k = this.x;
            int l = this.y;
            int i1 = this.z;

            if (this.direction == 2) {
                k = MathHelper.floor(this.locX - (double) ((float) this.d() / 32.0F));
            }

            if (this.direction == 1) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.d() / 32.0F));
            }

            if (this.direction == 0) {
                k = MathHelper.floor(this.locX - (double) ((float) this.d() / 32.0F));
            }

            if (this.direction == 3) {
                i1 = MathHelper.floor(this.locZ - (double) ((float) this.d() / 32.0F));
            }

            l = MathHelper.floor(this.locY - (double) ((float) this.g() / 32.0F));

            for (int j1 = 0; j1 < i; ++j1) {
                for (int k1 = 0; k1 < j; ++k1) {
                    Material material;

                    if (this.direction != 2 && this.direction != 0) {
                        material = this.world.getMaterial(this.x, l + k1, i1 + j1);
                    } else {
                        material = this.world.getMaterial(k + j1, l + k1, this.z);
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

    public boolean L() {
        return true;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (!this.dead && !this.world.isStatic) {
            // CraftBukkit start
            if (this instanceof EntityPainting) {
                PaintingBreakEvent event = null;
                if (damagesource.getEntity() != null) {
                    event = new org.bukkit.event.painting.PaintingBreakByEntityEvent((Painting) this.getBukkitEntity(), damagesource.getEntity() == null ? null : damagesource.getEntity().getBukkitEntity());
                } else {
                    if (damagesource == DamageSource.FIRE) {
                        event = new PaintingBreakEvent((Painting) this.getBukkitEntity(), RemoveCause.FIRE);
                    }
                    // TODO: Could put other stuff here?
                }

                if (event != null) {
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return true;
                    }
                }

                if (this.dead) {
                    return true;
                }
            }
            // CraftBukkit end

            this.die();
            this.K();
            EntityHuman entityhuman = null;

            if (damagesource.getEntity() instanceof EntityHuman) {
                entityhuman = (EntityHuman) damagesource.getEntity();
            }

            if (entityhuman != null && entityhuman.abilities.canInstantlyBuild) {
                return true;
            }

            this.h();
        }

        return true;
    }

    public void move(double d0, double d1, double d2) {
        if (!this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) {
            if (dead) return; // CraftBukkit

            this.die();
            this.h();
        }
    }

    public void g(double d0, double d1, double d2) {
        if (false && !this.world.isStatic && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.die();
            this.h();
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
        if (nbttagcompound.hasKey("Direction")) {
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

    public abstract int d();

    public abstract int g();

    public abstract void h();
}
