package net.minecraft.server;

import java.util.Random;
import java.util.function.Predicate;

public abstract class EntityMonster extends EntityCreature implements IMonster {

    protected EntityMonster(EntityTypes<? extends EntityMonster> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 5;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    public void movementTick() {
        this.dz();
        this.eQ();
        super.movementTick();
    }

    protected void eQ() {
        float f = this.aQ();

        if (f > 0.5F) {
            this.ticksFarFromPlayer += 2;
        }

    }

    @Override
    protected boolean L() {
        return true;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_HOSTILE_SWIM;
    }

    @Override
    protected SoundEffect getSoundSplash() {
        return SoundEffects.ENTITY_HOSTILE_SPLASH;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return this.isInvulnerable(damagesource) ? false : super.damageEntity(damagesource, f);
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_HOSTILE_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_HOSTILE_DEATH;
    }

    @Override
    protected SoundEffect getSoundFall(int i) {
        return i > 4 ? SoundEffects.ENTITY_HOSTILE_BIG_FALL : SoundEffects.ENTITY_HOSTILE_SMALL_FALL;
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return 0.5F - iworldreader.y(blockposition);
    }

    public static boolean a(WorldAccess worldaccess, BlockPosition blockposition, Random random) {
        if (worldaccess.getBrightness(EnumSkyBlock.SKY, blockposition) > random.nextInt(32)) {
            return false;
        } else {
            int i = worldaccess.getMinecraftWorld().V() ? worldaccess.c(blockposition, 10) : worldaccess.getLightLevel(blockposition);

            return i <= random.nextInt(8);
        }
    }

    public static boolean b(EntityTypes<? extends EntityMonster> entitytypes, WorldAccess worldaccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return worldaccess.getDifficulty() != EnumDifficulty.PEACEFUL && a(worldaccess, blockposition, random) && a(entitytypes, (GeneratorAccess) worldaccess, enummobspawn, blockposition, random);
    }

    public static boolean c(EntityTypes<? extends EntityMonster> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        return generatoraccess.getDifficulty() != EnumDifficulty.PEACEFUL && a(entitytypes, generatoraccess, enummobspawn, blockposition, random);
    }

    public static AttributeProvider.Builder eR() {
        return EntityInsentient.p().a(GenericAttributes.ATTACK_DAMAGE);
    }

    @Override
    protected boolean isDropExperience() {
        return true;
    }

    @Override
    protected boolean cV() {
        return true;
    }

    public boolean f(EntityHuman entityhuman) {
        return true;
    }

    @Override
    public ItemStack f(ItemStack itemstack) {
        if (itemstack.getItem() instanceof ItemProjectileWeapon) {
            Predicate<ItemStack> predicate = ((ItemProjectileWeapon) itemstack.getItem()).e();
            ItemStack itemstack1 = ItemProjectileWeapon.a((EntityLiving) this, predicate);

            return itemstack1.isEmpty() ? new ItemStack(Items.ARROW) : itemstack1;
        } else {
            return ItemStack.b;
        }
    }
}
