package net.minecraft.server;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
// CraftBukkit end

public abstract class EntityArrow extends IProjectile {

    private static final DataWatcherObject<Byte> f = DataWatcher.a(EntityArrow.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Byte> g = DataWatcher.a(EntityArrow.class, DataWatcherRegistry.a);
    @Nullable
    private IBlockData ag;
    public boolean inGround;
    protected int c;
    public EntityArrow.PickupStatus fromPlayer;
    public int shake;
    public int despawnCounter;
    private double damage;
    public int knockbackStrength;
    private SoundEffect ak;
    private IntOpenHashSet al;
    private List<Entity> am;

    protected EntityArrow(EntityTypes<? extends EntityArrow> entitytypes, World world) {
        super(entitytypes, world);
        this.fromPlayer = EntityArrow.PickupStatus.DISALLOWED;
        this.damage = 2.0D;
        this.ak = this.i();
    }

    protected EntityArrow(EntityTypes<? extends EntityArrow> entitytypes, double d0, double d1, double d2, World world) {
        this(entitytypes, world);
        this.setPosition(d0, d1, d2);
    }

    protected EntityArrow(EntityTypes<? extends EntityArrow> entitytypes, EntityLiving entityliving, World world) {
        this(entitytypes, entityliving.locX(), entityliving.getHeadY() - 0.10000000149011612D, entityliving.locZ(), world);
        this.setShooter(entityliving);
        if (entityliving instanceof EntityHuman) {
            this.fromPlayer = EntityArrow.PickupStatus.ALLOWED;
        }

    }

    public void a(SoundEffect soundeffect) {
        this.ak = soundeffect;
    }

    @Override
    protected void initDatawatcher() {
        this.datawatcher.register(EntityArrow.f, (byte) 0);
        this.datawatcher.register(EntityArrow.g, (byte) 0);
    }

    @Override
    public void shoot(double d0, double d1, double d2, float f, float f1) {
        super.shoot(d0, d1, d2, f, f1);
        this.despawnCounter = 0;
    }

    @Override
    public void tick() {
        super.tick();
        boolean flag = this.t();
        Vec3D vec3d = this.getMot();

        if (this.lastPitch == 0.0F && this.lastYaw == 0.0F) {
            float f = MathHelper.sqrt(c(vec3d));

            this.yaw = (float) (MathHelper.d(vec3d.x, vec3d.z) * 57.2957763671875D);
            this.pitch = (float) (MathHelper.d(vec3d.y, (double) f) * 57.2957763671875D);
            this.lastYaw = this.yaw;
            this.lastPitch = this.pitch;
        }

        BlockPosition blockposition = this.getChunkCoordinates();
        IBlockData iblockdata = this.world.getType(blockposition);
        Vec3D vec3d1;

        if (!iblockdata.isAir() && !flag) {
            VoxelShape voxelshape = iblockdata.getCollisionShape(this.world, blockposition);

            if (!voxelshape.isEmpty()) {
                vec3d1 = this.getPositionVector();
                Iterator iterator = voxelshape.d().iterator();

                while (iterator.hasNext()) {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB) iterator.next();

                    if (axisalignedbb.a(blockposition).d(vec3d1)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }

        if (this.shake > 0) {
            --this.shake;
        }

        if (this.isInWaterOrRain()) {
            this.extinguish();
        }

        if (this.inGround && !flag) {
            if (this.ag != iblockdata && this.u()) {
                this.z();
            } else if (!this.world.isClientSide) {
                this.h();
            }

            ++this.c;
        } else {
            this.c = 0;
            Vec3D vec3d2 = this.getPositionVector();

            vec3d1 = vec3d2.e(vec3d);
            Object object = this.world.rayTrace(new RayTrace(vec3d2, vec3d1, RayTrace.BlockCollisionOption.COLLIDER, RayTrace.FluidCollisionOption.NONE, this));

            if (((MovingObjectPosition) object).getType() != MovingObjectPosition.EnumMovingObjectType.MISS) {
                vec3d1 = ((MovingObjectPosition) object).getPos();
            }

            while (!this.dead) {
                MovingObjectPositionEntity movingobjectpositionentity = this.a(vec3d2, vec3d1);

                if (movingobjectpositionentity != null) {
                    object = movingobjectpositionentity;
                }

                if (object != null && ((MovingObjectPosition) object).getType() == MovingObjectPosition.EnumMovingObjectType.ENTITY) {
                    Entity entity = ((MovingObjectPositionEntity) object).getEntity();
                    Entity entity1 = this.getShooter();

                    if (entity instanceof EntityHuman && entity1 instanceof EntityHuman && !((EntityHuman) entity1).a((EntityHuman) entity)) {
                        object = null;
                        movingobjectpositionentity = null;
                    }
                }

                if (object != null && !flag) {
                    this.a((MovingObjectPosition) object);
                    this.impulse = true;
                }

                if (movingobjectpositionentity == null || this.getPierceLevel() <= 0) {
                    break;
                }

                object = null;
            }

            vec3d = this.getMot();
            double d0 = vec3d.x;
            double d1 = vec3d.y;
            double d2 = vec3d.z;

            if (this.isCritical()) {
                for (int i = 0; i < 4; ++i) {
                    this.world.addParticle(Particles.CRIT, this.locX() + d0 * (double) i / 4.0D, this.locY() + d1 * (double) i / 4.0D, this.locZ() + d2 * (double) i / 4.0D, -d0, -d1 + 0.2D, -d2);
                }
            }

            double d3 = this.locX() + d0;
            double d4 = this.locY() + d1;
            double d5 = this.locZ() + d2;
            float f1 = MathHelper.sqrt(c(vec3d));

            if (flag) {
                this.yaw = (float) (MathHelper.d(-d0, -d2) * 57.2957763671875D);
            } else {
                this.yaw = (float) (MathHelper.d(d0, d2) * 57.2957763671875D);
            }

            this.pitch = (float) (MathHelper.d(d1, (double) f1) * 57.2957763671875D);
            this.pitch = e(this.lastPitch, this.pitch);
            this.yaw = e(this.lastYaw, this.yaw);
            float f2 = 0.99F;
            float f3 = 0.05F;

            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f4 = 0.25F;

                    this.world.addParticle(Particles.BUBBLE, d3 - d0 * 0.25D, d4 - d1 * 0.25D, d5 - d2 * 0.25D, d0, d1, d2);
                }

                f2 = this.s();
            }

            this.setMot(vec3d.a((double) f2));
            if (!this.isNoGravity() && !flag) {
                Vec3D vec3d3 = this.getMot();

                this.setMot(vec3d3.x, vec3d3.y - 0.05000000074505806D, vec3d3.z);
            }

            this.setPosition(d3, d4, d5);
            this.checkBlockCollisions();
        }
    }

    private boolean u() {
        return this.inGround && this.world.b((new AxisAlignedBB(this.getPositionVector(), this.getPositionVector())).g(0.06D));
    }

    private void z() {
        this.inGround = false;
        Vec3D vec3d = this.getMot();

        this.setMot(vec3d.d((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
        this.despawnCounter = 0;
    }

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        super.move(enummovetype, vec3d);
        if (enummovetype != EnumMoveType.SELF && this.u()) {
            this.z();
        }

    }

    protected void h() {
        ++this.despawnCounter;
        if (this.despawnCounter >= 1200) {
            this.die();
        }

    }

    private void A() {
        if (this.am != null) {
            this.am.clear();
        }

        if (this.al != null) {
            this.al.clear();
        }

    }

    @Override
    protected void a(MovingObjectPositionEntity movingobjectpositionentity) {
        super.a(movingobjectpositionentity);
        Entity entity = movingobjectpositionentity.getEntity();
        float f = (float) this.getMot().f();
        int i = MathHelper.f(MathHelper.a((double) f * this.damage, 0.0D, 2.147483647E9D));

        if (this.getPierceLevel() > 0) {
            if (this.al == null) {
                this.al = new IntOpenHashSet(5);
            }

            if (this.am == null) {
                this.am = Lists.newArrayListWithCapacity(5);
            }

            if (this.al.size() >= this.getPierceLevel() + 1) {
                this.die();
                return;
            }

            this.al.add(entity.getId());
        }

        if (this.isCritical()) {
            long j = (long) this.random.nextInt(i / 2 + 2);

            i = (int) Math.min(j + (long) i, 2147483647L);
        }

        Entity entity1 = this.getShooter();
        DamageSource damagesource;

        if (entity1 == null) {
            damagesource = DamageSource.arrow(this, this);
        } else {
            damagesource = DamageSource.arrow(this, entity1);
            if (entity1 instanceof EntityLiving) {
                ((EntityLiving) entity1).z(entity);
            }
        }

        boolean flag = entity.getEntityType() == EntityTypes.ENDERMAN;
        int k = entity.getFireTicks();

        if (this.isBurning() && !flag) {
            // CraftBukkit start
            EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), 5);
            org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);
            if (!combustEvent.isCancelled()) {
                entity.setOnFire(combustEvent.getDuration(), false);
            }
            // CraftBukkit end
        }

        if (entity.damageEntity(damagesource, (float) i)) {
            if (flag) {
                return;
            }

            if (entity instanceof EntityLiving) {
                EntityLiving entityliving = (EntityLiving) entity;

                if (!this.world.isClientSide && this.getPierceLevel() <= 0) {
                    entityliving.setArrowCount(entityliving.getArrowCount() + 1);
                }

                if (this.knockbackStrength > 0) {
                    Vec3D vec3d = this.getMot().d(1.0D, 0.0D, 1.0D).d().a((double) this.knockbackStrength * 0.6D);

                    if (vec3d.g() > 0.0D) {
                        entityliving.i(vec3d.x, 0.1D, vec3d.z);
                    }
                }

                if (!this.world.isClientSide && entity1 instanceof EntityLiving) {
                    EnchantmentManager.a(entityliving, entity1);
                    EnchantmentManager.b((EntityLiving) entity1, (Entity) entityliving);
                }

                this.a(entityliving);
                if (entity1 != null && entityliving != entity1 && entityliving instanceof EntityHuman && entity1 instanceof EntityPlayer && !this.isSilent()) {
                    ((EntityPlayer) entity1).playerConnection.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.g, 0.0F));
                }

                if (!entity.isAlive() && this.am != null) {
                    this.am.add(entityliving);
                }

                if (!this.world.isClientSide && entity1 instanceof EntityPlayer) {
                    EntityPlayer entityplayer = (EntityPlayer) entity1;

                    if (this.am != null && this.isShotFromCrossbow()) {
                        CriterionTriggers.G.a(entityplayer, (Collection) this.am);
                    } else if (!entity.isAlive() && this.isShotFromCrossbow()) {
                        CriterionTriggers.G.a(entityplayer, (Collection) Arrays.asList(entity));
                    }
                }
            }

            this.playSound(this.ak, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
            if (this.getPierceLevel() <= 0) {
                this.die();
            }
        } else {
            entity.setFireTicks(k);
            this.setMot(this.getMot().a(-0.1D));
            this.yaw += 180.0F;
            this.lastYaw += 180.0F;
            if (!this.world.isClientSide && this.getMot().g() < 1.0E-7D) {
                if (this.fromPlayer == EntityArrow.PickupStatus.ALLOWED) {
                    this.a(this.getItemStack(), 0.1F);
                }

                this.die();
            }
        }

    }

    @Override
    protected void a(MovingObjectPositionBlock movingobjectpositionblock) {
        this.ag = this.world.getType(movingobjectpositionblock.getBlockPosition());
        super.a(movingobjectpositionblock);
        Vec3D vec3d = movingobjectpositionblock.getPos().a(this.locX(), this.locY(), this.locZ());

        this.setMot(vec3d);
        Vec3D vec3d1 = vec3d.d().a(0.05000000074505806D);

        this.setPositionRaw(this.locX() - vec3d1.x, this.locY() - vec3d1.y, this.locZ() - vec3d1.z);
        this.playSound(this.getSoundHit(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.shake = 7;
        this.setCritical(false);
        this.setPierceLevel((byte) 0);
        this.a(SoundEffects.ENTITY_ARROW_HIT);
        this.setShotFromCrossbow(false);
        this.A();
    }

    protected SoundEffect i() {
        return SoundEffects.ENTITY_ARROW_HIT;
    }

    protected final SoundEffect getSoundHit() {
        return this.ak;
    }

    protected void a(EntityLiving entityliving) {}

    @Nullable
    protected MovingObjectPositionEntity a(Vec3D vec3d, Vec3D vec3d1) {
        return ProjectileHelper.a(this.world, this, vec3d, vec3d1, this.getBoundingBox().b(this.getMot()).g(1.0D), this::a);
    }

    @Override
    protected boolean a(Entity entity) {
        return super.a(entity) && (this.al == null || !this.al.contains(entity.getId()));
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setShort("life", (short) this.despawnCounter);
        if (this.ag != null) {
            nbttagcompound.set("inBlockState", GameProfileSerializer.a(this.ag));
        }

        nbttagcompound.setByte("shake", (byte) this.shake);
        nbttagcompound.setBoolean("inGround", this.inGround);
        nbttagcompound.setByte("pickup", (byte) this.fromPlayer.ordinal());
        nbttagcompound.setDouble("damage", this.damage);
        nbttagcompound.setBoolean("crit", this.isCritical());
        nbttagcompound.setByte("PierceLevel", this.getPierceLevel());
        nbttagcompound.setString("SoundEvent", IRegistry.SOUND_EVENT.getKey(this.ak).toString());
        nbttagcompound.setBoolean("ShotFromCrossbow", this.isShotFromCrossbow());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.despawnCounter = nbttagcompound.getShort("life");
        if (nbttagcompound.hasKeyOfType("inBlockState", 10)) {
            this.ag = GameProfileSerializer.c(nbttagcompound.getCompound("inBlockState"));
        }

        this.shake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getBoolean("inGround");
        if (nbttagcompound.hasKeyOfType("damage", 99)) {
            this.damage = nbttagcompound.getDouble("damage");
        }

        if (nbttagcompound.hasKeyOfType("pickup", 99)) {
            this.fromPlayer = EntityArrow.PickupStatus.a(nbttagcompound.getByte("pickup"));
        } else if (nbttagcompound.hasKeyOfType("player", 99)) {
            this.fromPlayer = nbttagcompound.getBoolean("player") ? EntityArrow.PickupStatus.ALLOWED : EntityArrow.PickupStatus.DISALLOWED;
        }

        this.setCritical(nbttagcompound.getBoolean("crit"));
        this.setPierceLevel(nbttagcompound.getByte("PierceLevel"));
        if (nbttagcompound.hasKeyOfType("SoundEvent", 8)) {
            this.ak = (SoundEffect) IRegistry.SOUND_EVENT.getOptional(new MinecraftKey(nbttagcompound.getString("SoundEvent"))).orElse(this.i());
        }

        this.setShotFromCrossbow(nbttagcompound.getBoolean("ShotFromCrossbow"));
    }

    @Override
    public void setShooter(@Nullable Entity entity) {
        super.setShooter(entity);
        if (entity instanceof EntityHuman) {
            this.fromPlayer = ((EntityHuman) entity).abilities.canInstantlyBuild ? EntityArrow.PickupStatus.CREATIVE_ONLY : EntityArrow.PickupStatus.ALLOWED;
        }

    }

    @Override
    public void pickup(EntityHuman entityhuman) {
        if (!this.world.isClientSide && (this.inGround || this.t()) && this.shake <= 0) {
            // CraftBukkit start
            ItemStack itemstack = this.getItemStack();
            if (this.fromPlayer == PickupStatus.ALLOWED && !itemstack.isEmpty() && entityhuman.inventory.canHold(itemstack) > 0) {
                EntityItem item = new EntityItem(this.world, this.locX(), this.locY(), this.locZ(), itemstack);
                PlayerPickupArrowEvent event = new PlayerPickupArrowEvent((org.bukkit.entity.Player) entityhuman.getBukkitEntity(), new org.bukkit.craftbukkit.entity.CraftItem(this.world.getServer(), this, item), (org.bukkit.entity.AbstractArrow) this.getBukkitEntity());
                // event.setCancelled(!entityhuman.canPickUpLoot); TODO
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }
                itemstack = item.getItemStack();
            }
            boolean flag = this.fromPlayer == EntityArrow.PickupStatus.ALLOWED || this.fromPlayer == EntityArrow.PickupStatus.CREATIVE_ONLY && entityhuman.abilities.canInstantlyBuild || this.t() && this.getShooter().getUniqueID() == entityhuman.getUniqueID();

            if (this.fromPlayer == EntityArrow.PickupStatus.ALLOWED && !entityhuman.inventory.pickup(itemstack)) {
                // CraftBukkit end
                flag = false;
            }

            if (flag) {
                entityhuman.receive(this, 1);
                this.die();
            }

        }
    }

    protected abstract ItemStack getItemStack();

    @Override
    protected boolean playStepSound() {
        return false;
    }

    public void setDamage(double d0) {
        this.damage = d0;
    }

    public double getDamage() {
        return this.damage;
    }

    public void setKnockbackStrength(int i) {
        this.knockbackStrength = i;
    }

    @Override
    public boolean bK() {
        return false;
    }

    @Override
    protected float getHeadHeight(EntityPose entitypose, EntitySize entitysize) {
        return 0.13F;
    }

    public void setCritical(boolean flag) {
        this.a(1, flag);
    }

    public void setPierceLevel(byte b0) {
        this.datawatcher.set(EntityArrow.g, b0);
    }

    private void a(int i, boolean flag) {
        byte b0 = (Byte) this.datawatcher.get(EntityArrow.f);

        if (flag) {
            this.datawatcher.set(EntityArrow.f, (byte) (b0 | i));
        } else {
            this.datawatcher.set(EntityArrow.f, (byte) (b0 & ~i));
        }

    }

    public boolean isCritical() {
        byte b0 = (Byte) this.datawatcher.get(EntityArrow.f);

        return (b0 & 1) != 0;
    }

    public boolean isShotFromCrossbow() {
        byte b0 = (Byte) this.datawatcher.get(EntityArrow.f);

        return (b0 & 4) != 0;
    }

    public byte getPierceLevel() {
        return (Byte) this.datawatcher.get(EntityArrow.g);
    }

    public void a(EntityLiving entityliving, float f) {
        int i = EnchantmentManager.a(Enchantments.ARROW_DAMAGE, entityliving);
        int j = EnchantmentManager.a(Enchantments.ARROW_KNOCKBACK, entityliving);

        this.setDamage((double) (f * 2.0F) + this.random.nextGaussian() * 0.25D + (double) ((float) this.world.getDifficulty().a() * 0.11F));
        if (i > 0) {
            this.setDamage(this.getDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockbackStrength(j);
        }

        if (EnchantmentManager.a(Enchantments.ARROW_FIRE, entityliving) > 0) {
            this.setOnFire(100);
        }

    }

    protected float s() {
        return 0.6F;
    }

    public void o(boolean flag) {
        this.noclip = flag;
        this.a(2, flag);
    }

    public boolean t() {
        return !this.world.isClientSide ? this.noclip : ((Byte) this.datawatcher.get(EntityArrow.f) & 2) != 0;
    }

    public void setShotFromCrossbow(boolean flag) {
        this.a(4, flag);
    }

    @Override
    public Packet<?> P() {
        Entity entity = this.getShooter();

        return new PacketPlayOutSpawnEntity(this, entity == null ? 0 : entity.getId());
    }

    public static enum PickupStatus {

        DISALLOWED, ALLOWED, CREATIVE_ONLY;

        private PickupStatus() {}

        public static EntityArrow.PickupStatus a(int i) {
            if (i < 0 || i > values().length) {
                i = 0;
            }

            return values()[i];
        }
    }
}
