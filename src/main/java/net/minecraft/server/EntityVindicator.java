package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityVindicator extends EntityIllagerAbstract {

    private static final Predicate<EnumDifficulty> b = (enumdifficulty) -> {
        return enumdifficulty == EnumDifficulty.NORMAL || enumdifficulty == EnumDifficulty.HARD;
    };
    private boolean bo;

    public EntityVindicator(EntityTypes<? extends EntityVindicator> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityVindicator.a(this));
        this.goalSelector.a(2, new EntityIllagerAbstract.b(this));
        this.goalSelector.a(3, new EntityRaider.a(this, 10.0F));
        this.goalSelector.a(4, new EntityVindicator.c(this));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a());
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, true));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
        this.targetSelector.a(4, new EntityVindicator.b(this));
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
    }

    @Override
    protected void mobTick() {
        if (!this.isNoAI() && PathfinderGoalUtil.a(this)) {
            boolean flag = ((WorldServer) this.world).c_(this.getChunkCoordinates());

            ((Navigation) this.getNavigation()).a(flag);
        }

        super.mobTick();
    }

    public static AttributeProvider.Builder eK() {
        return EntityMonster.eR().a(GenericAttributes.MOVEMENT_SPEED, 0.3499999940395355D).a(GenericAttributes.FOLLOW_RANGE, 12.0D).a(GenericAttributes.MAX_HEALTH, 24.0D).a(GenericAttributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        if (this.bo) {
            nbttagcompound.setBoolean("Johnny", true);
        }

    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("Johnny", 99)) {
            this.bo = nbttagcompound.getBoolean("Johnny");
        }

    }

    @Override
    public SoundEffect eL() {
        return SoundEffects.ENTITY_VINDICATOR_CELEBRATE;
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        GroupDataEntity groupdataentity1 = super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);

        ((Navigation) this.getNavigation()).a(true);
        this.a(difficultydamagescaler);
        this.b(difficultydamagescaler);
        return groupdataentity1;
    }

    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        if (this.fa() == null) {
            this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_AXE));
        }

    }

    @Override
    public boolean r(Entity entity) {
        return super.r(entity) ? true : (entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == EnumMonsterType.ILLAGER ? this.getScoreboardTeam() == null && entity.getScoreboardTeam() == null : false);
    }

    @Override
    public void setCustomName(@Nullable IChatBaseComponent ichatbasecomponent) {
        super.setCustomName(ichatbasecomponent);
        if (!this.bo && ichatbasecomponent != null && ichatbasecomponent.getString().equals("Johnny")) {
            this.bo = true;
        }

    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_VINDICATOR_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_VINDICATOR_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_VINDICATOR_HURT;
    }

    @Override
    public void a(int i, boolean flag) {
        ItemStack itemstack = new ItemStack(Items.IRON_AXE);
        Raid raid = this.fa();
        byte b0 = 1;

        if (i > raid.a(EnumDifficulty.NORMAL)) {
            b0 = 2;
        }

        boolean flag1 = this.random.nextFloat() <= raid.w();

        if (flag1) {
            Map<Enchantment, Integer> map = Maps.newHashMap();

            map.put(Enchantments.DAMAGE_ALL, Integer.valueOf(b0));
            EnchantmentManager.a((Map) map, itemstack);
        }

        this.setSlot(EnumItemSlot.MAINHAND, itemstack);
    }

    static class b extends PathfinderGoalNearestAttackableTarget<EntityLiving> {

        public b(EntityVindicator entityvindicator) {
            super(entityvindicator, EntityLiving.class, 0, true, true, EntityLiving::eh);
        }

        @Override
        public boolean a() {
            return ((EntityVindicator) this.e).bo && super.a();
        }

        @Override
        public void c() {
            super.c();
            this.e.n(0);
        }
    }

    static class a extends PathfinderGoalBreakDoor {

        public a(EntityInsentient entityinsentient) {
            super(entityinsentient, 6, EntityVindicator.b);
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean b() {
            EntityVindicator entityvindicator = (EntityVindicator) this.entity;

            return entityvindicator.fb() && super.b();
        }

        @Override
        public boolean a() {
            EntityVindicator entityvindicator = (EntityVindicator) this.entity;

            return entityvindicator.fb() && entityvindicator.random.nextInt(10) == 0 && super.a();
        }

        @Override
        public void c() {
            super.c();
            this.entity.n(0);
        }
    }

    class c extends PathfinderGoalMeleeAttack {

        public c(EntityVindicator entityvindicator) {
            super(entityvindicator, 1.0D, false);
        }

        @Override
        protected double a(EntityLiving entityliving) {
            if (this.a.getVehicle() instanceof EntityRavager) {
                float f = this.a.getVehicle().getWidth() - 0.1F;

                return (double) (f * 2.0F * f * 2.0F + entityliving.getWidth());
            } else {
                return super.a(entityliving);
            }
        }
    }
}
