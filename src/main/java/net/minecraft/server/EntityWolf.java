package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityWolf extends EntityAnimal {

    private boolean a = false;
    private float b;
    private float c;
    private boolean g;
    private boolean h;
    private float i;
    private float j;

    public EntityWolf(World world) {
        super(world);
        this.texture = "/mob/wolf.png";
        this.b(0.8F, 0.8F);
        this.aY = 1.1F;
    }

    public int getMaxHealth() {
        return this.isTamed() ? 20 : 8;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, "");
        this.datawatcher.a(18, new Integer(this.getHealth()));
    }

    protected boolean g_() {
        return false;
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
        nbttagcompound.setBoolean("Sitting", this.isSitting());
        if (this.getOwnerName() == null) {
            nbttagcompound.setString("Owner", "");
        } else {
            nbttagcompound.setString("Owner", this.getOwnerName());
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
        this.setSitting(nbttagcompound.getBoolean("Sitting"));
        String s = nbttagcompound.getString("Owner");

        if (s.length() > 0) {
            this.setOwnerName(s);
            this.setTamed(true);
        }
    }

    protected boolean d_() {
        return this.isAngry();
    }

    protected String c_() {
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String m() {
        return "mob.wolf.hurt";
    }

    protected String n() {
        return "mob.wolf.death";
    }

    protected float o() {
        return 0.4F;
    }

    protected int e() {
        return -1;
    }

    protected void m_() {
        super.m_();
        if (!this.e && !this.D() && this.isTamed() && this.vehicle == null) {
            EntityHuman entityhuman = this.world.a(this.getOwnerName());

            if (entityhuman != null) {
                float f = entityhuman.h(this);

                if (f > 5.0F) {
                    this.c(entityhuman, f);
                }
            } else if (!this.az()) {
                this.setSitting(true);
            }
        } else if (this.target == null && !this.D() && !this.isTamed() && this.world.random.nextInt(100) == 0) {
            List list = this.world.a(EntitySheep.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));

            if (!list.isEmpty()) {
                // CraftBukkit start
                Entity entity = (Entity) list.get(this.world.random.nextInt(list.size()));
                org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

                EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.RANDOM_TARGET);
                this.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled() || event.getTarget() != null ) {
                    this.setTarget(entity);
                }
                // CraftBukkit end
            }
        }

        if (this.az()) {
            this.setSitting(false);
        }

        if (!this.world.isStatic) {
            this.datawatcher.watch(18, Integer.valueOf(this.getHealth()));
        }
    }

    public void d() {
        super.d();
        this.a = false;
        if (this.al() && !this.D() && !this.isAngry()) {
            Entity entity = this.am();

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;
                ItemStack itemstack = entityhuman.inventory.getItemInHand();

                if (itemstack != null) {
                    if (!this.isTamed() && itemstack.id == Item.BONE.id) {
                        this.a = true;
                    } else if (this.isTamed() && Item.byId[itemstack.id] instanceof ItemFood) {
                        this.a = ((ItemFood) Item.byId[itemstack.id]).p();
                    }
                }
            }
        }

        if (!this.aj && this.g && !this.h && !this.D() && this.onGround) {
            this.h = true;
            this.i = 0.0F;
            this.j = 0.0F;
            this.world.a(this, (byte) 8);
        }
    }

    public void w_() {
        super.w_();
        this.c = this.b;
        if (this.a) {
            this.b += (1.0F - this.b) * 0.4F;
        } else {
            this.b += (0.0F - this.b) * 0.4F;
        }

        if (this.a) {
            this.aZ = 10;
        }

        if (this.ay()) {
            this.g = true;
            this.h = false;
            this.i = 0.0F;
            this.j = 0.0F;
        } else if ((this.g || this.h) && this.h) {
            if (this.i == 0.0F) {
                this.world.makeSound(this, "mob.wolf.shake", this.o(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.j = this.i;
            this.i += 0.05F;
            if (this.j >= 2.0F) {
                this.g = false;
                this.h = false;
                this.j = 0.0F;
                this.i = 0.0F;
            }

            if (this.i > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.i - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.a("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    public float x() {
        return this.length * 0.8F;
    }

    protected int q_() {
        return this.isSitting() ? 20 : super.q_();
    }

    private void c(Entity entity, float f) {
        PathEntity pathentity = this.world.findPath(this, entity, 16.0F);

        if (pathentity == null && f > 12.0F) {
            int i = MathHelper.floor(entity.locX) - 2;
            int j = MathHelper.floor(entity.locZ) - 2;
            int k = MathHelper.floor(entity.boundingBox.b);

            for (int l = 0; l <= 4; ++l) {
                for (int i1 = 0; i1 <= 4; ++i1) {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.world.e(i + l, k - 1, j + i1) && !this.world.e(i + l, k, j + i1) && !this.world.e(i + l, k + 1, j + i1)) {
                        this.setPositionRotation((double) ((float) (i + l) + 0.5F), (double) k, (double) ((float) (j + i1) + 0.5F), this.yaw, this.pitch);
                        return;
                    }
                }
            }
        } else {
            this.setPathEntity(pathentity);
        }
    }

    protected boolean w() {
        return this.isSitting() || this.h;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        this.setSitting(false);
        if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow)) {
            i = (i + 1) / 2;
        }

        if (!super.damageEntity(damagesource, i)) {
            return false;
        } else {
            if (!this.isTamed() && !this.isAngry()) {
                if (entity instanceof EntityHuman) {
                    // CraftBukkit start
                    org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

                    EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
                    this.world.getServer().getPluginManager().callEvent(event);

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

                        if (!entitywolf.isTamed() && entitywolf.target == null) {
                            // CraftBukkit start
                            org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

                            EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY);
                            this.world.getServer().getPluginManager().callEvent(event);

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
                if (this.isTamed() && entity instanceof EntityHuman && ((EntityHuman) entity).name.equalsIgnoreCase(this.getOwnerName())) {
                    return true;
                }

                this.target = entity;
            }

            return true;
        }
    }

    protected Entity findTarget() {
        return this.isAngry() ? this.world.findNearbyPlayer(this, 16.0D) : null;
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

            if (this.isTamed()) {
                b0 = 4;
            }
            // CraftBukkit start
            org.bukkit.entity.Entity damager = this.getBukkitEntity();
            org.bukkit.entity.Entity damagee = entity == null ? null : entity.getBukkitEntity();

            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, b0);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            entity.damageEntity(DamageSource.mobAttack(this), b0);
        }
    }

    public boolean b(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (!this.isTamed()) {
            if (itemstack != null && itemstack.id == Item.BONE.id && !this.isAngry()) {
                --itemstack.count;
                if (itemstack.count <= 0) {
                    entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                }

                if (!this.world.isStatic) {
                    // CraftBukkit - added event call and isCancelled check.
                    if (this.random.nextInt(3) == 0 && !CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                        // CraftBukkit end
                        this.setTamed(true);
                        this.setPathEntity((PathEntity) null);
                        this.setSitting(true);
                        this.setHealth(20);
                        this.setOwnerName(entityhuman.name);
                        this.a(true);
                        this.world.a(this, (byte) 7);
                    } else {
                        this.a(false);
                        this.world.a(this, (byte) 6);
                    }
                }

                return true;
            }
        } else {
            if (itemstack != null && Item.byId[itemstack.id] instanceof ItemFood) {
                ItemFood itemfood = (ItemFood) Item.byId[itemstack.id];

                if (itemfood.p() && this.datawatcher.getInt(18) < 20) {
                    --itemstack.count;
                    this.d(itemfood.n(), RegainReason.EATING); // Craftbukkit
                    if (itemstack.count <= 0) {
                        entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                    }

                    return true;
                }
            }

            if (entityhuman.name.equalsIgnoreCase(this.getOwnerName())) {
                if (!this.world.isStatic) {
                    this.setSitting(!this.isSitting());
                    this.aW = false;
                    this.setPathEntity((PathEntity) null);
                }

                return true;
            }
        }

        return super.b(entityhuman);
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

            this.world.a(s, this.locX + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, this.locY + 0.5D + (double) (this.random.nextFloat() * this.length), this.locZ + (double) (this.random.nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
        }
    }

    public int p() {
        return 8;
    }

    public String getOwnerName() {
        return this.datawatcher.getString(17);
    }

    public void setOwnerName(String s) {
        this.datawatcher.watch(17, s);
    }

    public boolean isSitting() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setSitting(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public boolean isTamed() {
        return (this.datawatcher.getByte(16) & 4) != 0;
    }

    public void setTamed(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 4)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -5)));
        }
    }

    protected EntityAnimal createChild(EntityAnimal entityanimal) {
        return new EntityWolf(this.world);
    }
}
