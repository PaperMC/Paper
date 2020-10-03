package net.minecraft.server;

import java.util.EnumSet;
import java.util.Random;

public class EntitySilverfish extends EntityMonster {

    private EntitySilverfish.PathfinderGoalSilverfishWakeOthers b;

    public EntitySilverfish(EntityTypes<? extends EntitySilverfish> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void initPathfinder() {
        this.b = new EntitySilverfish.PathfinderGoalSilverfishWakeOthers(this);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(3, this.b);
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        this.goalSelector.a(5, new EntitySilverfish.PathfinderGoalSilverfishHideInBlock(this));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[0])).a(new Class[0])); // CraftBukkit - decompile error
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
    }

    @Override
    public double ba() {
        return 0.1D;
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.13F;
    }

    public static AttributeProvider.Builder m() {
        return EntityMonster.eR().a(GenericAttributes.MAX_HEALTH, 8.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.25D).a(GenericAttributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected SoundEffect getSoundAmbient() {
        return SoundEffects.ENTITY_SILVERFISH_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_SILVERFISH_HURT;
    }

    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_SILVERFISH_DEATH;
    }

    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {
        this.playSound(SoundEffects.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            if ((damagesource instanceof EntityDamageSource || damagesource == DamageSource.MAGIC) && this.b != null) {
                this.b.g();
            }

            return super.damageEntity(damagesource, f);
        }
    }

    @Override
    public void tick() {
        this.aA = this.yaw;
        super.tick();
    }

    @Override
    public void n(float f) {
        this.yaw = f;
        super.n(f);
    }

    @Override
    public float a(BlockPosition blockposition, IWorldReader iworldreader) {
        return BlockMonsterEggs.h(iworldreader.getType(blockposition.down())) ? 10.0F : super.a(blockposition, iworldreader);
    }

    public static boolean b(EntityTypes<EntitySilverfish> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        if (c(entitytypes, generatoraccess, enummobspawn, blockposition, random)) {
            EntityHuman entityhuman = generatoraccess.a((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D, 5.0D, true);

            return entityhuman == null;
        } else {
            return false;
        }
    }

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }

    static class PathfinderGoalSilverfishHideInBlock extends PathfinderGoalRandomStroll {

        private EnumDirection h;
        private boolean i;

        public PathfinderGoalSilverfishHideInBlock(EntitySilverfish entitysilverfish) {
            super(entitysilverfish, 1.0D, 10);
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE));
        }

        @Override
        public boolean a() {
            if (this.a.getGoalTarget() != null) {
                return false;
            } else if (!this.a.getNavigation().m()) {
                return false;
            } else {
                Random random = this.a.getRandom();

                if (this.a.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && random.nextInt(10) == 0) {
                    this.h = EnumDirection.a(random);
                    BlockPosition blockposition = (new BlockPosition(this.a.locX(), this.a.locY() + 0.5D, this.a.locZ())).shift(this.h);
                    IBlockData iblockdata = this.a.world.getType(blockposition);

                    if (BlockMonsterEggs.h(iblockdata)) {
                        this.i = true;
                        return true;
                    }
                }

                this.i = false;
                return super.a();
            }
        }

        @Override
        public boolean b() {
            return this.i ? false : super.b();
        }

        @Override
        public void c() {
            if (!this.i) {
                super.c();
            } else {
                World world = this.a.world;
                BlockPosition blockposition = (new BlockPosition(this.a.locX(), this.a.locY() + 0.5D, this.a.locZ())).shift(this.h);
                IBlockData iblockdata = world.getType(blockposition);

                if (BlockMonsterEggs.h(iblockdata)) {
                    // CraftBukkit start
                    if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.a, blockposition, BlockMonsterEggs.c(iblockdata.getBlock())).isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    world.setTypeAndData(blockposition, BlockMonsterEggs.c(iblockdata.getBlock()), 3);
                    this.a.doSpawnEffect();
                    this.a.die();
                }

            }
        }
    }

    static class PathfinderGoalSilverfishWakeOthers extends PathfinderGoal {

        private final EntitySilverfish silverfish;
        private int b;

        public PathfinderGoalSilverfishWakeOthers(EntitySilverfish entitysilverfish) {
            this.silverfish = entitysilverfish;
        }

        public void g() {
            if (this.b == 0) {
                this.b = 20;
            }

        }

        @Override
        public boolean a() {
            return this.b > 0;
        }

        @Override
        public void e() {
            --this.b;
            if (this.b <= 0) {
                World world = this.silverfish.world;
                Random random = this.silverfish.getRandom();
                BlockPosition blockposition = this.silverfish.getChunkCoordinates();

                for (int i = 0; i <= 5 && i >= -5; i = (i <= 0 ? 1 : 0) - i) {
                    for (int j = 0; j <= 10 && j >= -10; j = (j <= 0 ? 1 : 0) - j) {
                        for (int k = 0; k <= 10 && k >= -10; k = (k <= 0 ? 1 : 0) - k) {
                            BlockPosition blockposition1 = blockposition.b(j, i, k);
                            IBlockData iblockdata = world.getType(blockposition1);
                            Block block = iblockdata.getBlock();

                            if (block instanceof BlockMonsterEggs) {
                                // CraftBukkit start
                                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(this.silverfish, blockposition1, Blocks.AIR.getBlockData()).isCancelled()) {
                                    continue;
                                }
                                // CraftBukkit end
                                if (world.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
                                    world.a(blockposition1, true, this.silverfish);
                                } else {
                                    world.setTypeAndData(blockposition1, ((BlockMonsterEggs) block).c().getBlockData(), 3);
                                }

                                if (random.nextBoolean()) {
                                    return;
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}
