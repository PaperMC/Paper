package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.event.entity.EntityTameEvent;
// CraftBukkit end

public class EntityWolf extends EntityAnimal {

    private boolean a = false;
    private float b;
    private float c;
    private boolean f;
    private boolean g;
    private float h;
    private float i;

    public EntityWolf(World world) {
        super(world);
        this.texture = "/mob/wolf.png";
        this.b(0.8F, 0.8F);
        this.aE = 1.1F;
        this.health = 8;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, "");
        this.datawatcher.a(18, new Integer(this.health));
    }

    protected boolean n() {
        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Angry", this.isAngry());
        nbttagcompound.a("Sitting", this.isSitting());
        if (this.x() == null) {
            nbttagcompound.setString("Owner", "");
        } else {
            nbttagcompound.setString("Owner", this.x());
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAngry(nbttagcompound.m("Angry"));
        this.setSitting(nbttagcompound.m("Sitting"));
        String s = nbttagcompound.getString("Owner");

        if (s.length() > 0) {
            this.a(s);
            this.d(true);
        }
    }

    protected boolean l_() {
        return !this.A();
    }

    protected String g() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.A() && this.datawatcher.b(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String h() {
        return "mob.wolf.hurt";
    }

    protected String i() {
        return "mob.wolf.death";
    }

    protected float k() {
        return 0.4F;
    }

    protected int j() {
        return -1;
    }

    protected void c_() {
        super.c_();
        if (!this.e && !this.B() && this.A() && this.vehicle == null) {
            EntityHuman entityhuman = this.world.a(this.x());

            if (entityhuman != null) {
                float f = entityhuman.f(this);

                if (f > 5.0F) {
                    this.c(entityhuman, f);
                }
            } else if (!this.ac()) {
                this.setSitting(true);
            }
        } else if (this.target == null && !this.B() && !this.A() && this.world.random.nextInt(100) == 0) {
            List list = this.world.a(EntitySheep.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));

            if (!list.isEmpty()) {
                this.c((Entity) list.get(this.world.random.nextInt(list.size())));
            }
        }

        if (this.ac()) {
            this.setSitting(false);
        }

        if (!this.world.isStatic) {
            this.datawatcher.b(18, Integer.valueOf(this.health));
        }
    }

    public void u() {
        super.u();
        this.a = false;
        if (this.U() && !this.B() && !this.isAngry()) {
            Entity entity = this.V();

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;
                ItemStack itemstack = entityhuman.inventory.getItemInHand();

                if (itemstack != null) {
                    if (!this.A() && itemstack.id == Item.BONE.id) {
                        this.a = true;
                    } else if (this.A() && Item.byId[itemstack.id] instanceof ItemFood) {
                        this.a = ((ItemFood) Item.byId[itemstack.id]).l();
                    }
                }
            }
        }

        if (!this.Y && this.f && !this.g && !this.B() && this.onGround) {
            this.g = true;
            this.h = 0.0F;
            this.i = 0.0F;
            this.world.a(this, (byte) 8);
        }
    }

    public void o_() {
        super.o_();
        this.c = this.b;
        if (this.a) {
            this.b += (1.0F - this.b) * 0.4F;
        } else {
            this.b += (0.0F - this.b) * 0.4F;
        }

        if (this.a) {
            this.aF = 10;
        }

        if (this.ab()) {
            this.f = true;
            this.g = false;
            this.h = 0.0F;
            this.i = 0.0F;
        } else if ((this.f || this.g) && this.g) {
            if (this.h == 0.0F) {
                this.world.makeSound(this, "mob.wolf.shake", this.k(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.i = this.h;
            this.h += 0.05F;
            if (this.i >= 2.0F) {
                this.f = false;
                this.g = false;
                this.i = 0.0F;
                this.h = 0.0F;
            }

            if (this.h > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.h - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.length * 0.5F;

                    this.world.a("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    public float s() {
        return this.width * 0.8F;
    }

    protected int v() {
        return this.isSitting() ? 20 : super.v();
    }

    private void c(Entity entity, float f) {
        PathEntity pathentity = this.world.findPath(this, entity, 16.0F);

        if (pathentity == null && f > 12.0F) {
            int i = MathHelper.floor(entity.locX) - 2;
            int j = MathHelper.floor(entity.locZ) - 2;
            int k = MathHelper.floor(entity.boundingBox.b);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.world.d(i + l, k - 1, j + i1) && !this.world.d(i + l, k, j + i1) && !this.world.d(i + l, k + 1, j + i1)) {
                        this.setPositionRotation((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.yaw, this.pitch);
                        return;
                    }
                }
            }
        } else {
            this.a(pathentity);
        }
    }

    protected boolean w() {
        return this.isSitting() || this.g;
    }

    public boolean damageEntity(Entity entity, int i) {
        this.setSitting(false);
        if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow)) {
            i = (i + 1) / 2;
        }

        if (!super.damageEntity((Entity) entity, i)) {
            return false;
        } else {
            if (!this.A() && !this.isAngry()) {
                if (entity instanceof EntityHuman) {
                    // CraftBukkit start
                    CraftServer server = this.world.getServer();
                    org.bukkit.entity.Entity bukkitTarget = null;
                    if (entity != null) {
                        bukkitTarget = entity.getBukkitEntity();
                    }

                    EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, TargetReason.TARGET_ATTACKED_ENTITY);
                    server.getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        if (event.getTarget() == null) {
                            this.target = null;
                        } else {
                            this.setAngry(true);
                            this.target = ((CraftEntity) event.getTarget()).getHandle();
                        }
                    }
                    // CraftBukkit end
                }

                if (entity instanceof EntityArrow && ((EntityArrow) entity).shooter != null) {
                    entity = ((EntityArrow) entity).shooter;
                }

                if (entity instanceof EntityLiving) {
                    List list = this.world.a(EntityWolf.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));
                    Iterator iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity1 = (Entity) iterator.next();
                        EntityWolf entitywolf = (EntityWolf) entity1;

                        if (!entitywolf.A() && entitywolf.target == null) {
                            // CraftBukkit start
                            CraftServer server = this.world.getServer();
                            org.bukkit.entity.Entity bukkitTarget = null;
                            if (entity != null) {
                                bukkitTarget = entity.getBukkitEntity();
                            }

                            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, TargetReason.TARGET_ATTACKED_ENTITY);
                            server.getPluginManager().callEvent(event);

                            if (!event.isCancelled()) {
                                if (event.getTarget() == null) {
                                    this.target = null;
                                } else {
                                    entitywolf.target = (Entity) entity;
                                    if (entity instanceof EntityHuman) {
                                        entitywolf.setAngry(true);
                                    }
                                }
                            }
                            // CraftBukkit end
                        }
                    }
                }
            } else if (entity != this && entity != null) {
                if (this.A() && entity instanceof EntityHuman && ((EntityHuman) entity).name.equalsIgnoreCase(this.x())) {
                    return true;
                }

                this.target = (Entity) entity;
            }

            return true;
        }
    }

    protected Entity findTarget() {
        return this.isAngry() ? this.world.a(this, 16.0D) : null;
    }

    protected void a(Entity entity, float f) {
        if (f > 2.0F && f < 6.0F && this.random.nextInt(10) == 0) {
            if (this.onGround) {
                double d0 = entity.locX - this.locX;
                double d1 = entity.locZ - this.locZ;
                float f1 = MathHelper.a(d0 * d0 + d1 * d1);

                this.motX = d0 / (double) f1 * 0.5D * 0.800000011920929D + this.motX * 0.20000000298023224D;
                this.motZ = d1 / (double) f1 * 0.5D * 0.800000011920929D + this.motZ * 0.20000000298023224D;
                this.motY = 0.4000000059604645D;
            }
        } else if ((double) f < 1.5D && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            byte b0 = 2;

            if (this.A()) {
                b0 = 4;
            }
            // CraftBukkit start
            CraftServer server = this.world.getServer();
            org.bukkit.entity.Entity damager = this.getBukkitEntity();
            org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
            DamageCause damageCause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageCause, b0);
            server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                entity.damageEntity(this, b0);
            }
            // CraftBukkit end
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (!this.A()) {
            if (itemstack != null && itemstack.id == Item.BONE.id && !this.isAngry()) {
                --itemstack.count;
                if (itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                if (!this.world.isStatic) {
                    // CraftBukkit start
                    if (this.random.nextInt(3) == 0) {
                        EntityTameEvent event = CraftEventFactory.callEntityTameEvent(this, entityhuman);

                        if (!event.isCancelled()) {
                        // CraftBukkit end
                            this.d(true);
                            this.a((PathEntity) null);
                            this.setSitting(true);
                            this.health = 20;
                            this.a(entityhuman.name);
                            this.a(true);
                            this.world.a(this, (byte) 7);
                        } else {
                            this.a(false);
                            this.world.a(this, (byte) 6);
                        }
                    }
                }

                return true;
            }
        } else {
            if (itemstack != null && Item.byId[itemstack.id] instanceof ItemFood) {
                ItemFood itemfood = (ItemFood) Item.byId[itemstack.id];

                if (itemfood.l() && this.datawatcher.b(18) < 20) {
                    --itemstack.count;
                    if (itemstack.count <= 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                    }

                    this.b(((ItemFood) Item.PORK).k());
                    return true;
                }
            }

            if (entityhuman.name.equalsIgnoreCase(this.x())) {
                if (!this.world.isStatic) {
                    this.setSitting(!this.isSitting());
                    this.aC = false;
                    this.a((PathEntity) null);
                }

                return true;
            }
        }

        return false;
    }

    void a(boolean flag) {
        String s = "heart";

        if (!flag) {
            s = "smoke";
        }

        for (int i = 0; i < 7; ++i) {
            double d0 = this.random.nextGaussian() * 0.02D;
            double d1 = this.random.nextGaussian() * 0.02D;
            double d2 = this.random.nextGaussian() * 0.02D;

            this.world.a(s, this.locX + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, this.locY + 0.5D + (double) (this.random.nextFloat() * this.width), this.locZ + (double) (this.random.nextFloat() * this.length * 2.0F) - (double) this.length, d0, d1, d2);
        }
    }

    public int l() {
        return 8;
    }

    public String x() {
        return this.datawatcher.c(17);
    }

    public void a(String s) {
        this.datawatcher.b(17, s);
    }

    public boolean isSitting() {
        return (this.datawatcher.a(16) & 1) != 0;
    }

    public void setSitting(boolean flag) {
        byte b0 = this.datawatcher.a(16);

        if (flag) {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public boolean isAngry() {
        return (this.datawatcher.a(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = this.datawatcher.a(16);

        if (flag) {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public boolean A() {
        return (this.datawatcher.a(16) & 4) != 0;
    }

    public void d(boolean flag) {
        byte b0 = this.datawatcher.a(16);

        if (flag) {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.datawatcher.b(16, Byte.valueOf((byte) (b0 & -5)));
        }
    }
}
