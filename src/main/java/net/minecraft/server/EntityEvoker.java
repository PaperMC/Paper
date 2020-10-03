package net.minecraft.server;

import java.util.List;
import javax.annotation.Nullable;

public class EntityEvoker extends EntityIllagerWizard {

    private EntitySheep bo;

    public EntityEvoker(EntityTypes<? extends EntityEvoker> entitytypes, World world) {
        super(entitytypes, world);
        this.f = 10;
    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new EntityEvoker.b());
        this.goalSelector.a(2, new PathfinderGoalAvoidTarget<>(this, EntityHuman.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.a(4, new EntityEvoker.c());
        this.goalSelector.a(5, new EntityEvoker.a());
        this.goalSelector.a(6, new EntityEvoker.d());
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 3.0F, 1.0F));
        this.goalSelector.a(10, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 8.0F));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityRaider.class})).a());
        this.targetSelector.a(2, (new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true)).a(300));
        this.targetSelector.a(3, (new PathfinderGoalNearestAttackableTarget<>(this, EntityVillagerAbstract.class, false)).a(300));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, false));
    }

    public static AttributeProvider.Builder eK() {
        return EntityMonster.eR().a(GenericAttributes.MOVEMENT_SPEED, 0.5D).a(GenericAttributes.FOLLOW_RANGE, 12.0D).a(GenericAttributes.MAX_HEALTH, 24.0D);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
    }

    @Override
    public SoundEffect eL() {
        return SoundEffects.ENTITY_EVOKER_CELEBRATE;
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
    }

    @Override
    protected void mobTick() {
        super.mobTick();
    }

    @Override
    public boolean r(Entity entity) {
        return entity == null ? false : (entity == this ? true : (super.r(entity) ? true : (entity instanceof EntityVex ? this.r(((EntityVex) entity).eK()) : (entity instanceof EntityLiving && ((EntityLiving) entity).getMonsterType() == EnumMonsterType.ILLAGER ? this.getScoreboardTeam() == null && entity.getScoreboardTeam() == null : false))));
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_EVOKER_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_EVOKER_DEATH;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_EVOKER_HURT;
    }

    private void a(@Nullable EntitySheep entitysheep) {
        this.bo = entitysheep;
    }

    @Nullable
    private EntitySheep fg() {
        return this.bo;
    }

    @Override
    protected SoundEffect getSoundCastSpell() {
        return SoundEffects.ENTITY_EVOKER_CAST_SPELL;
    }

    @Override
    public void a(int i, boolean flag) {}

    public class d extends EntityIllagerWizard.PathfinderGoalCastSpell {

        private final PathfinderTargetCondition e = (new PathfinderTargetCondition()).a(16.0D).a().a((entityliving) -> {
            return ((EntitySheep) entityliving).getColor() == EnumColor.BLUE;
        });

        public d() {
            super();
        }

        @Override
        public boolean a() {
            if (EntityEvoker.this.getGoalTarget() != null) {
                return false;
            } else if (EntityEvoker.this.eW()) {
                return false;
            } else if (EntityEvoker.this.ticksLived < this.c) {
                return false;
            } else if (!EntityEvoker.this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                return false;
            } else {
                List<EntitySheep> list = EntityEvoker.this.world.a(EntitySheep.class, this.e, EntityEvoker.this, EntityEvoker.this.getBoundingBox().grow(16.0D, 4.0D, 16.0D));

                if (list.isEmpty()) {
                    return false;
                } else {
                    EntityEvoker.this.a((EntitySheep) list.get(EntityEvoker.this.random.nextInt(list.size())));
                    return true;
                }
            }
        }

        @Override
        public boolean b() {
            return EntityEvoker.this.fg() != null && this.b > 0;
        }

        @Override
        public void d() {
            super.d();
            EntityEvoker.this.a((EntitySheep) null);
        }

        @Override
        protected void j() {
            EntitySheep entitysheep = EntityEvoker.this.fg();

            if (entitysheep != null && entitysheep.isAlive()) {
                entitysheep.setColor(EnumColor.RED);
            }

        }

        @Override
        protected int m() {
            return 40;
        }

        @Override
        protected int g() {
            return 60;
        }

        @Override
        protected int h() {
            return 140;
        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.ENTITY_EVOKER_PREPARE_WOLOLO;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.WOLOLO;
        }
    }

    class c extends EntityIllagerWizard.PathfinderGoalCastSpell {

        private final PathfinderTargetCondition e;

        private c() {
            super();
            this.e = (new PathfinderTargetCondition()).a(16.0D).c().e().a().b();
        }

        @Override
        public boolean a() {
            if (!super.a()) {
                return false;
            } else {
                int i = EntityEvoker.this.world.a(EntityVex.class, this.e, EntityEvoker.this, EntityEvoker.this.getBoundingBox().g(16.0D)).size();

                return EntityEvoker.this.random.nextInt(8) + 1 > i;
            }
        }

        @Override
        protected int g() {
            return 100;
        }

        @Override
        protected int h() {
            return 340;
        }

        @Override
        protected void j() {
            WorldServer worldserver = (WorldServer) EntityEvoker.this.world;

            for (int i = 0; i < 3; ++i) {
                BlockPosition blockposition = EntityEvoker.this.getChunkCoordinates().b(-2 + EntityEvoker.this.random.nextInt(5), 1, -2 + EntityEvoker.this.random.nextInt(5));
                EntityVex entityvex = (EntityVex) EntityTypes.VEX.a(EntityEvoker.this.world);

                entityvex.setPositionRotation(blockposition, 0.0F, 0.0F);
                entityvex.prepare(worldserver, EntityEvoker.this.world.getDamageScaler(blockposition), EnumMobSpawn.MOB_SUMMONED, (GroupDataEntity) null, (NBTTagCompound) null);
                entityvex.a((EntityInsentient) EntityEvoker.this);
                entityvex.g(blockposition);
                entityvex.a(20 * (30 + EntityEvoker.this.random.nextInt(90)));
                worldserver.addAllEntities(entityvex);
            }

        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.ENTITY_EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.SUMMON_VEX;
        }
    }

    class a extends EntityIllagerWizard.PathfinderGoalCastSpell {

        private a() {
            super();
        }

        @Override
        protected int g() {
            return 40;
        }

        @Override
        protected int h() {
            return 100;
        }

        @Override
        protected void j() {
            EntityLiving entityliving = EntityEvoker.this.getGoalTarget();
            double d0 = Math.min(entityliving.locY(), EntityEvoker.this.locY());
            double d1 = Math.max(entityliving.locY(), EntityEvoker.this.locY()) + 1.0D;
            float f = (float) MathHelper.d(entityliving.locZ() - EntityEvoker.this.locZ(), entityliving.locX() - EntityEvoker.this.locX());
            int i;

            if (EntityEvoker.this.h((Entity) entityliving) < 9.0D) {
                float f1;

                for (i = 0; i < 5; ++i) {
                    f1 = f + (float) i * 3.1415927F * 0.4F;
                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f1) * 1.5D, EntityEvoker.this.locZ() + (double) MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (i = 0; i < 8; ++i) {
                    f1 = f + (float) i * 3.1415927F * 2.0F / 8.0F + 1.2566371F;
                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f1) * 2.5D, EntityEvoker.this.locZ() + (double) MathHelper.sin(f1) * 2.5D, d0, d1, f1, 3);
                }
            } else {
                for (i = 0; i < 16; ++i) {
                    double d2 = 1.25D * (double) (i + 1);
                    int j = 1 * i;

                    this.a(EntityEvoker.this.locX() + (double) MathHelper.cos(f) * d2, EntityEvoker.this.locZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }

        }

        private void a(double d0, double d1, double d2, double d3, float f, int i) {
            BlockPosition blockposition = new BlockPosition(d0, d3, d1);
            boolean flag = false;
            double d4 = 0.0D;

            do {
                BlockPosition blockposition1 = blockposition.down();
                IBlockData iblockdata = EntityEvoker.this.world.getType(blockposition1);

                if (iblockdata.d(EntityEvoker.this.world, blockposition1, EnumDirection.UP)) {
                    if (!EntityEvoker.this.world.isEmpty(blockposition)) {
                        IBlockData iblockdata1 = EntityEvoker.this.world.getType(blockposition);
                        VoxelShape voxelshape = iblockdata1.getCollisionShape(EntityEvoker.this.world, blockposition);

                        if (!voxelshape.isEmpty()) {
                            d4 = voxelshape.c(EnumDirection.EnumAxis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockposition = blockposition.down();
            } while (blockposition.getY() >= MathHelper.floor(d2) - 1);

            if (flag) {
                EntityEvoker.this.world.addEntity(new EntityEvokerFangs(EntityEvoker.this.world, d0, (double) blockposition.getY() + d4, d1, f, i, EntityEvoker.this));
            }

        }

        @Override
        protected SoundEffect k() {
            return SoundEffects.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected EntityIllagerWizard.Spell getCastSpell() {
            return EntityIllagerWizard.Spell.FANGS;
        }
    }

    class b extends EntityIllagerWizard.b {

        private b() {
            super();
        }

        @Override
        public void e() {
            if (EntityEvoker.this.getGoalTarget() != null) {
                EntityEvoker.this.getControllerLook().a(EntityEvoker.this.getGoalTarget(), (float) EntityEvoker.this.eo(), (float) EntityEvoker.this.O());
            } else if (EntityEvoker.this.fg() != null) {
                EntityEvoker.this.getControllerLook().a(EntityEvoker.this.fg(), (float) EntityEvoker.this.eo(), (float) EntityEvoker.this.O());
            }

        }
    }
}
