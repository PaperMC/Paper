package net.minecraft.server;

import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;

public abstract class EntityAnimal extends EntityAgeable {

    public int loveTicks;
    public UUID breedCause;

    protected EntityAnimal(EntityTypes<? extends EntityAnimal> entitytypes, World world) {
        super(entitytypes, world);
        this.a(PathType.DANGER_FIRE, 16.0F);
        this.a(PathType.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected void mobTick() {
        if (this.getAge() != 0) {
            this.loveTicks = 0;
        }

        super.mobTick();
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (this.getAge() != 0) {
            this.loveTicks = 0;
        }

        if (this.loveTicks > 0) {
            --this.loveTicks;
            if (this.loveTicks % 10 == 0) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;

                this.world.addParticle(Particles.HEART, this.d(1.0D), this.cE() + 0.5D, this.g(1.0D), d0, d1, d2);
            }
        }

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.loveTicks = 0;
            return super.damageEntity(damagesource, f);
        }
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return iworldreader.getType(blockposition.down()).a(Blocks.GRASS_BLOCK) ? 10.0F : iworldreader.y(blockposition) - 0.5F;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("InLove", this.loveTicks);
        if (this.breedCause != null) {
            nbttagcompound.a("LoveCause", this.breedCause);
        }

    }

    @Override
    public double ba() {
        return 0.14D;
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        this.loveTicks = nbttagcompound.getInt("InLove");
        this.breedCause = nbttagcompound.b("LoveCause") ? nbttagcompound.a("LoveCause") : null;
    }

    public static boolean b(EntityTypes<? extends EntityAnimal> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return generatoraccess.getType(blockposition.down()).a(Blocks.GRASS_BLOCK) && generatoraccess.getLightLevel(blockposition, 0) > 8;
    }

    @Override
    public int D() {
        return 120;
    }

    @Override
    public boolean isTypeNotPersistent(double d0) {
        return false;
    }

    @Override
    protected int getExpValue(EntityHuman entityhuman) {
        return 1 + this.world.random.nextInt(3);
    }

    public boolean k(ItemStack itemstack) {
        return itemstack.getItem() == Items.WHEAT;
    }

    @Override
    public EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (this.k(itemstack)) {
            int i = this.getAge();

            if (!this.world.isClientSide && i == 0 && this.eP()) {
                this.a(entityhuman, itemstack);
                this.g(entityhuman);
                return EnumInteractionResult.SUCCESS;
            }

            if (this.isBaby()) {
                this.a(entityhuman, itemstack);
                this.setAge((int) ((float) (-i / 20) * 0.1F), true);
                return EnumInteractionResult.a(this.world.isClientSide);
            }

            if (this.world.isClientSide) {
                return EnumInteractionResult.CONSUME;
            }
        }

        return super.b(entityhuman, enumhand);
    }

    protected void a(EntityHuman entityhuman, ItemStack itemstack) {
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

    }

    public boolean eP() {
        return this.loveTicks <= 0;
    }

    public void g(@Nullable EntityHuman entityhuman) {
        this.loveTicks = 600;
        if (entityhuman != null) {
            this.breedCause = entityhuman.getUniqueID();
        }

        this.world.broadcastEntityEffect(this, (byte) 18);
    }

    public void setLoveTicks(int i) {
        this.loveTicks = i;
    }

    public int eQ() {
        return this.loveTicks;
    }

    @Nullable
    public EntityPlayer getBreedCause() {
        if (this.breedCause == null) {
            return null;
        } else {
            EntityHuman entityhuman = this.world.b(this.breedCause);

            return entityhuman instanceof EntityPlayer ? (EntityPlayer) entityhuman : null;
        }
    }

    public boolean isInLove() {
        return this.loveTicks > 0;
    }

    public void resetLove() {
        this.loveTicks = 0;
    }

    public boolean mate(EntityAnimal entityanimal) {
        return entityanimal == this ? false : (entityanimal.getClass() != this.getClass() ? false : this.isInLove() && entityanimal.isInLove());
    }

    public void a(WorldServer worldserver, EntityAnimal entityanimal) {
        EntityAgeable entityageable = this.createChild(worldserver, entityanimal);

        if (entityageable != null) {
            EntityPlayer entityplayer = this.getBreedCause();

            if (entityplayer == null && entityanimal.getBreedCause() != null) {
                entityplayer = entityanimal.getBreedCause();
            }

            if (entityplayer != null) {
                entityplayer.a(StatisticList.ANIMALS_BRED);
                CriterionTriggers.o.a(entityplayer, this, entityanimal, entityageable);
            }

            this.setAgeRaw(6000);
            entityanimal.setAgeRaw(6000);
            this.resetLove();
            entityanimal.resetLove();
            entityageable.setBaby(true);
            entityageable.setPositionRotation(this.locX(), this.locY(), this.locZ(), 0.0F, 0.0F);
            worldserver.addAllEntities(entityageable);
            worldserver.broadcastEntityEffect(this, (byte) 18);
            if (worldserver.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                worldserver.addEntity(new EntityExperienceOrb(worldserver, this.locX(), this.locY(), this.locZ(), this.getRandom().nextInt(7) + 1));
            }

        }
    }
}
