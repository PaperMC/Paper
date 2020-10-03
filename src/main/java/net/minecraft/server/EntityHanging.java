package net.minecraft.server;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import org.apache.commons.lang3.Validate;

// CraftBukkit start
import org.bukkit.entity.Hanging;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
// CraftBukkit end

public abstract class EntityHanging extends Entity {

    protected static final Predicate<Entity> b = (entity) -> {
        return entity instanceof EntityHanging;
    };
    private int e;
    public BlockPosition blockPosition;
    protected EnumDirection direction;

    protected EntityHanging(EntityTypes<? extends EntityHanging> entitytypes, World world) {
        super(entitytypes, world);
        this.direction = EnumDirection.SOUTH;
    }

    protected EntityHanging(EntityTypes<? extends EntityHanging> entitytypes, World world, BlockPosition blockposition) {
        this(entitytypes, world);
        this.blockPosition = blockposition;
    }

    @Override
    protected void initDatawatcher() {}

    public void setDirection(EnumDirection enumdirection) {
        Validate.notNull(enumdirection);
        Validate.isTrue(enumdirection.n().d());
        this.direction = enumdirection;
        this.yaw = (float) (this.direction.get2DRotationValue() * 90);
        this.lastYaw = this.yaw;
        this.updateBoundingBox();
    }

    protected void updateBoundingBox() {
        if (this.direction != null) {
            // CraftBukkit start code moved in to calculateBoundingBox
            this.a(calculateBoundingBox(this, this.blockPosition, this.direction, this.getHangingWidth(), this.getHangingHeight()));
            // CraftBukkit end
        }
    }

    // CraftBukkit start - break out BB calc into own method
    public static AxisAlignedBB calculateBoundingBox(@Nullable Entity entity, BlockPosition blockPosition, EnumDirection direction, int width, int height) {
        {
            double d0 = (double) blockPosition.getX() + 0.5D;
            double d1 = (double) blockPosition.getY() + 0.5D;
            double d2 = (double) blockPosition.getZ() + 0.5D;
            double d3 = 0.46875D;
            double d4 = a(width);
            double d5 = a(height);

            d0 -= (double) direction.getAdjacentX() * 0.46875D;
            d2 -= (double) direction.getAdjacentZ() * 0.46875D;
            d1 += d5;
            EnumDirection enumdirection = direction.h();

            d0 += d4 * (double) enumdirection.getAdjacentX();
            d2 += d4 * (double) enumdirection.getAdjacentZ();
            if (entity != null) {
                entity.setPositionRaw(d0, d1, d2);
            }
            double d6 = (double) width;
            double d7 = (double) height;
            double d8 = (double) width;

            if (direction.n() == EnumDirection.EnumAxis.Z) {
                d8 = 1.0D;
            } else {
                d6 = 1.0D;
            }

            d6 /= 32.0D;
            d7 /= 32.0D;
            d8 /= 32.0D;
            return new AxisAlignedBB(d0 - d6, d1 - d7, d2 - d8, d0 + d6, d1 + d7, d2 + d8);
        }
    }
    // CraftBukkit end

    private static double a(int i) { // CraftBukkit - static
        return i % 32 == 0 ? 0.5D : 0.0D;
    }

    @Override
    public void tick() {
        if (!this.world.isClientSide) {
            if (this.locY() < -64.0D) {
                this.am();
            }

            if (this.e++ == 100) {
                this.e = 0;
                if (!this.dead && !this.survives()) {
                    // CraftBukkit start - fire break events
                    Material material = this.world.getType(this.getChunkCoordinates()).getMaterial();
                    HangingBreakEvent.RemoveCause cause;

                    if (!material.equals(Material.AIR)) {
                        // TODO: This feels insufficient to catch 100% of suffocation cases
                        cause = HangingBreakEvent.RemoveCause.OBSTRUCTION;
                    } else {
                        cause = HangingBreakEvent.RemoveCause.PHYSICS;
                    }

                    HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), cause);
                    this.world.getServer().getPluginManager().callEvent(event);

                    if (dead || event.isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.die();
                    this.a((Entity) null);
                }
            }
        }

    }

    public boolean survives() {
        if (!this.world.getCubes(this)) {
            return false;
        } else {
            int i = Math.max(1, this.getHangingWidth() / 16);
            int j = Math.max(1, this.getHangingHeight() / 16);
            BlockPosition blockposition = this.blockPosition.shift(this.direction.opposite());
            EnumDirection enumdirection = this.direction.h();
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();

            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < j; ++l) {
                    int i1 = (i - 1) / -2;
                    int j1 = (j - 1) / -2;

                    blockposition_mutableblockposition.g(blockposition).c(enumdirection, k + i1).c(EnumDirection.UP, l + j1);
                    IBlockData iblockdata = this.world.getType(blockposition_mutableblockposition);

                    if (!iblockdata.getMaterial().isBuildable() && !BlockDiodeAbstract.isDiode(iblockdata)) {
                        return false;
                    }
                }
            }

            return this.world.getEntities(this, this.getBoundingBox(), EntityHanging.b).isEmpty();
        }
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public boolean t(Entity entity) {
        if (entity instanceof EntityHuman) {
            EntityHuman entityhuman = (EntityHuman) entity;

            return !this.world.a(entityhuman, this.blockPosition) ? true : this.damageEntity(DamageSource.playerAttack(entityhuman), 0.0F);
        } else {
            return false;
        }
    }

    @Override
    public EnumDirection getDirection() {
        return this.direction;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            if (!this.dead && !this.world.isClientSide) {
                // CraftBukkit start - fire break events
                Entity damager = (damagesource instanceof EntityDamageSourceIndirect) ? ((EntityDamageSourceIndirect) damagesource).getProximateDamageSource() : damagesource.getEntity();
                HangingBreakEvent event;
                if (damager != null) {
                    event = new HangingBreakByEntityEvent((Hanging) this.getBukkitEntity(), damager.getBukkitEntity(), damagesource.isExplosion() ? HangingBreakEvent.RemoveCause.EXPLOSION : HangingBreakEvent.RemoveCause.ENTITY);
                } else {
                    event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), damagesource.isExplosion() ? HangingBreakEvent.RemoveCause.EXPLOSION : HangingBreakEvent.RemoveCause.DEFAULT);
                }

                this.world.getServer().getPluginManager().callEvent(event);

                if (this.dead || event.isCancelled()) {
                    return true;
                }
                // CraftBukkit end

                this.die();
                this.velocityChanged();
                this.a(damagesource.getEntity());
            }

            return true;
        }
    }

    @Override
    public void move(EnumMoveType enummovetype, Vec3D vec3d) {
        if (!this.world.isClientSide && !this.dead && vec3d.g() > 0.0D) {
            if (this.dead) return; // CraftBukkit

            // CraftBukkit start - fire break events
            // TODO - Does this need its own cause? Seems to only be triggered by pistons
            HangingBreakEvent event = new HangingBreakEvent((Hanging) this.getBukkitEntity(), HangingBreakEvent.RemoveCause.PHYSICS);
            this.world.getServer().getPluginManager().callEvent(event);

            if (this.dead || event.isCancelled()) {
                return;
            }
            // CraftBukkit end

            this.die();
            this.a((Entity) null);
        }

    }

    @Override
    public void i(double d0, double d1, double d2) {
        if (false && !this.world.isClientSide && !this.dead && d0 * d0 + d1 * d1 + d2 * d2 > 0.0D) { // CraftBukkit - not needed
            this.die();
            this.a((Entity) null);
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        BlockPosition blockposition = this.getBlockPosition();

        nbttagcompound.setInt("TileX", blockposition.getX());
        nbttagcompound.setInt("TileY", blockposition.getY());
        nbttagcompound.setInt("TileZ", blockposition.getZ());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.blockPosition = new BlockPosition(nbttagcompound.getInt("TileX"), nbttagcompound.getInt("TileY"), nbttagcompound.getInt("TileZ"));
    }

    public abstract int getHangingWidth();

    public abstract int getHangingHeight();

    public abstract void a(@Nullable Entity entity);

    public abstract void playPlaceSound();

    @Override
    public EntityItem a(ItemStack itemstack, float f) {
        EntityItem entityitem = new EntityItem(this.world, this.locX() + (double) ((float) this.direction.getAdjacentX() * 0.15F), this.locY() + (double) f, this.locZ() + (double) ((float) this.direction.getAdjacentZ() * 0.15F), itemstack);

        entityitem.defaultPickupDelay();
        this.world.addEntity(entityitem);
        return entityitem;
    }

    @Override
    protected boolean aU() {
        return false;
    }

    @Override
    public void setPosition(double d0, double d1, double d2) {
        this.blockPosition = new BlockPosition(d0, d1, d2);
        this.updateBoundingBox();
        this.impulse = true;
    }

    public BlockPosition getBlockPosition() {
        return this.blockPosition;
    }

    @Override
    public float a(EnumBlockRotation enumblockrotation) {
        if (this.direction.n() != EnumDirection.EnumAxis.Y) {
            switch (enumblockrotation) {
                case CLOCKWISE_180:
                    this.direction = this.direction.opposite();
                    break;
                case COUNTERCLOCKWISE_90:
                    this.direction = this.direction.h();
                    break;
                case CLOCKWISE_90:
                    this.direction = this.direction.g();
            }
        }

        float f = MathHelper.g(this.yaw);

        switch (enumblockrotation) {
            case CLOCKWISE_180:
                return f + 180.0F;
            case COUNTERCLOCKWISE_90:
                return f + 90.0F;
            case CLOCKWISE_90:
                return f + 270.0F;
            default:
                return f;
        }
    }

    @Override
    public float a(EnumBlockMirror enumblockmirror) {
        return this.a(enumblockmirror.a(this.direction));
    }

    @Override
    public void onLightningStrike(WorldServer worldserver, EntityLightning entitylightning) {}

    @Override
    public void updateSize() {}
}
