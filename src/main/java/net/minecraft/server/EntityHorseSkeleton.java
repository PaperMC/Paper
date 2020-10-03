package net.minecraft.server;

import javax.annotation.Nullable;

public class EntityHorseSkeleton extends EntityHorseAbstract {

    private final PathfinderGoalHorseTrap bw = new PathfinderGoalHorseTrap(this);
    private boolean bx;
    private int by;

    public EntityHorseSkeleton(EntityTypes<? extends EntityHorseSkeleton> entitytypes, World world) {
        super(entitytypes, world);
    }

    public static AttributeProvider.Builder eL() {
        return fi().a(GenericAttributes.MAX_HEALTH, 15.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Override
    protected void eK() {
        this.getAttributeInstance(GenericAttributes.JUMP_STRENGTH).setValue(this.fq());
    }

    @Override
    protected void eV() {}

    @Override
    protected SoundEffect getSoundAmbient() {
        super.getSoundAmbient();
        return this.a((Tag) TagsFluid.WATER) ? SoundEffects.ENTITY_SKELETON_HORSE_AMBIENT_WATER : SoundEffects.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        super.getSoundDeath();
        return SoundEffects.ENTITY_SKELETON_HORSE_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        super.getSoundHurt(damagesource);
        return SoundEffects.ENTITY_SKELETON_HORSE_HURT;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        if (this.onGround) {
            if (!this.isVehicle()) {
                return SoundEffects.ENTITY_SKELETON_HORSE_STEP_WATER;
            }

            ++this.bv;
            if (this.bv > 5 && this.bv % 3 == 0) {
                return SoundEffects.ENTITY_SKELETON_HORSE_GALLOP_WATER;
            }

            if (this.bv <= 5) {
                return SoundEffects.ENTITY_SKELETON_HORSE_STEP_WATER;
            }
        }

        return SoundEffects.ENTITY_SKELETON_HORSE_SWIM;
    }

    @Override
    protected void d(float f) {
        if (this.onGround) {
            super.d(0.3F);
        } else {
            super.d(Math.min(0.1F, f * 25.0F));
        }

    }

    @Override
    protected void fn() {
        if (this.isInWater()) {
            this.playSound(SoundEffects.ENTITY_SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
        } else {
            super.fn();
        }

    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.UNDEAD;
    }

    @Override
    public double bb() {
        return super.bb() - 0.1875D;
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.eM() && this.by++ >= 18000) {
            this.die();
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setBoolean("SkeletonTrap", this.eM());
        nbttagcompound.setInt("SkeletonTrapTime", this.by);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.t(nbttagcompound.getBoolean("SkeletonTrap"));
        this.by = nbttagcompound.getInt("SkeletonTrapTime");
    }

    @Override
    public boolean bs() {
        return true;
    }

    @Override
    protected float dL() {
        return 0.96F;
    }

    public boolean eM() {
        return this.bx;
    }

    public void t(boolean flag) {
        if (flag != this.bx) {
            this.bx = flag;
            if (flag) {
                this.goalSelector.a(1, this.bw);
            } else {
                this.goalSelector.a((PathfinderGoal) this.bw);
            }

        }
    }

    @Nullable
    @Override
    public EntityAgeable createChild(WorldServer worldserver, EntityAgeable entityageable) {
        return (EntityAgeable) EntityTypes.SKELETON_HORSE.a((World) worldserver);
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!this.isTamed()) {
            return EnumInteractionResult.PASS;
        } else if (this.isBaby()) {
            return super.b(entityhuman, enumhand);
        } else if (entityhuman.ep()) {
            this.f(entityhuman);
            return EnumInteractionResult.a(this.world.isClientSide);
        } else if (this.isVehicle()) {
            return super.b(entityhuman, enumhand);
        } else {
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() == Items.SADDLE && !this.hasSaddle()) {
                    this.f(entityhuman);
                    return EnumInteractionResult.a(this.world.isClientSide);
                }

                EnumInteractionResult enuminteractionresult = itemstack.a(entityhuman, (EntityLiving) this, enumhand);

                if (enuminteractionresult.a()) {
                    return enuminteractionresult;
                }
            }

            this.h(entityhuman);
            return EnumInteractionResult.a(this.world.isClientSide);
        }
    }
}
