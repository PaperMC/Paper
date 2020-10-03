package net.minecraft.server;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;

public class EntityDolphin extends EntityWaterAnimal {

    private static final DataWatcherObject<BlockPosition> c = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.l);
    private static final DataWatcherObject<Boolean> d = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.i);
    private static final DataWatcherObject<Integer> bo = DataWatcher.a(EntityDolphin.class, DataWatcherRegistry.b);
    private static final PathfinderTargetCondition bp = (new PathfinderTargetCondition()).a(10.0D).b().a().c();
    public static final Predicate<EntityItem> b = (entityitem) -> {
        return !entityitem.p() && entityitem.isAlive() && entityitem.isInWater();
    };

    public EntityDolphin(EntityTypes<? extends EntityDolphin> entitytypes, World world) {
        super(entitytypes, world);
        this.moveController = new EntityDolphin.a(this);
        this.lookController = new ControllerLookDolphin(this, 10);
        this.setCanPickupLoot(true);
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        this.setAirTicks(this.bG());
        this.pitch = 0.0F;
        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    @Override
    public boolean cL() {
        return false;
    }

    @Override
    protected void a(int i) {}

    public void setTreasurePos(BlockPosition blockposition) {
        this.datawatcher.set(EntityDolphin.c, blockposition);
    }

    public BlockPosition getTreasurePos() {
        return (BlockPosition) this.datawatcher.get(EntityDolphin.c);
    }

    public boolean gotFish() {
        return (Boolean) this.datawatcher.get(EntityDolphin.d);
    }

    public void setGotFish(boolean flag) {
        this.datawatcher.set(EntityDolphin.d, flag);
    }

    public int getMoistness() {
        return (Integer) this.datawatcher.get(EntityDolphin.bo);
    }

    public void setMoistness(int i) {
        this.datawatcher.set(EntityDolphin.bo, i);
    }

    @Override
    protected void initDatawatcher() {
        super.initDatawatcher();
        this.datawatcher.register(EntityDolphin.c, BlockPosition.ZERO);
        this.datawatcher.register(EntityDolphin.d, false);
        this.datawatcher.register(EntityDolphin.bo, 2400);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("TreasurePosX", this.getTreasurePos().getX());
        nbttagcompound.setInt("TreasurePosY", this.getTreasurePos().getY());
        nbttagcompound.setInt("TreasurePosZ", this.getTreasurePos().getZ());
        nbttagcompound.setBoolean("GotFish", this.gotFish());
        nbttagcompound.setInt("Moistness", this.getMoistness());
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        int i = nbttagcompound.getInt("TreasurePosX");
        int j = nbttagcompound.getInt("TreasurePosY");
        int k = nbttagcompound.getInt("TreasurePosZ");

        this.setTreasurePos(new BlockPosition(i, j, k));
        super.loadData(nbttagcompound);
        this.setGotFish(nbttagcompound.getBoolean("GotFish"));
        this.setMoistness(nbttagcompound.getInt("Moistness"));
    }

    @Override
    protected void initPathfinder() {
        this.goalSelector.a(0, new PathfinderGoalBreath(this));
        this.goalSelector.a(0, new PathfinderGoalWater(this));
        this.goalSelector.a(1, new EntityDolphin.b(this));
        this.goalSelector.a(2, new EntityDolphin.c(this, 4.0D));
        this.goalSelector.a(4, new PathfinderGoalRandomSwim(this, 1.0D, 10));
        this.goalSelector.a(4, new PathfinderGoalRandomLookaround(this));
        this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(5, new PathfinderGoalWaterJump(this, 10));
        this.goalSelector.a(6, new PathfinderGoalMeleeAttack(this, 1.2000000476837158D, true));
        this.goalSelector.a(8, new EntityDolphin.d());
        this.goalSelector.a(8, new PathfinderGoalFollowBoat(this));
        this.goalSelector.a(9, new PathfinderGoalAvoidTarget<>(this, EntityGuardian.class, 8.0F, 1.0D, 1.0D));
        this.targetSelector.a(1, (new PathfinderGoalHurtByTarget(this, new Class[]{EntityGuardian.class})).a(new Class[0])); // CraftBukkit - decompile error
    }

    public static AttributeProvider.Builder eM() {
        return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 1.2000000476837158D).a(GenericAttributes.ATTACK_DAMAGE, 3.0D);
    }

    @Override
    protected NavigationAbstract b(World world) {
        return new NavigationGuardian(this, world);
    }

    @Override
    public boolean attackEntity(Entity entity) {
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (float) ((int) this.b(GenericAttributes.ATTACK_DAMAGE)));

        if (flag) {
            this.a((EntityLiving) this, entity);
            this.playSound(SoundEffects.ENTITY_DOLPHIN_ATTACK, 1.0F, 1.0F);
        }

        return flag;
    }

    @Override
    public int bG() {
        return 4800;
    }

    @Override
    protected int m(int i) {
        return this.bG();
    }

    @Override
    protected float b(EntityPose entitypose, EntitySize entitysize) {
        return 0.3F;
    }

    @Override
    public int O() {
        return 1;
    }

    @Override
    public int eo() {
        return 1;
    }

    @Override
    protected boolean n(Entity entity) {
        return true;
    }

    @Override
    public boolean e(ItemStack itemstack) {
        EnumItemSlot enumitemslot = EntityInsentient.j(itemstack);

        return !this.getEquipment(enumitemslot).isEmpty() ? false : enumitemslot == EnumItemSlot.MAINHAND && super.e(itemstack);
    }

    @Override
    protected void b(EntityItem entityitem) {
        if (this.getEquipment(EnumItemSlot.MAINHAND).isEmpty()) {
            ItemStack itemstack = entityitem.getItemStack();

            if (this.canPickup(itemstack)) {
                // CraftBukkit start - call EntityPickupItemEvent
                if (org.bukkit.craftbukkit.event.CraftEventFactory.callEntityPickupItemEvent(this, entityitem, 0, false).isCancelled()) {
                    return;
                }
                itemstack = entityitem.getItemStack(); // update ItemStack from event
                // CraftBukkit end
                this.a(entityitem);
                this.setSlot(EnumItemSlot.MAINHAND, itemstack);
                this.dropChanceHand[EnumItemSlot.MAINHAND.b()] = 2.0F;
                this.receive(entityitem, itemstack.getCount());
                entityitem.die();
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (this.isNoAI()) {
            this.setAirTicks(this.bG());
        } else {
            if (this.aF()) {
                this.setMoistness(2400);
            } else {
                this.setMoistness(this.getMoistness() - 1);
                if (this.getMoistness() <= 0) {
                    this.damageEntity(DamageSource.DRYOUT, 1.0F);
                }

                if (this.onGround) {
                    this.setMot(this.getMot().add((double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, (double) ((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F)));
                    this.yaw = this.random.nextFloat() * 360.0F;
                    this.onGround = false;
                    this.impulse = true;
                }
            }

            if (this.world.isClientSide && this.isInWater() && this.getMot().g() > 0.03D) {
                Vec3D vec3d = this.f(0.0F);
                float f = MathHelper.cos(this.yaw * 0.017453292F) * 0.3F;
                float f1 = MathHelper.sin(this.yaw * 0.017453292F) * 0.3F;
                float f2 = 1.2F - this.random.nextFloat() * 0.7F;

                for (int i = 0; i < 2; ++i) {
                    this.world.addParticle(Particles.DOLPHIN, this.locX() - vec3d.x * (double) f2 + (double) f, this.locY() - vec3d.y, this.locZ() - vec3d.z * (double) f2 + (double) f1, 0.0D, 0.0D, 0.0D);
                    this.world.addParticle(Particles.DOLPHIN, this.locX() - vec3d.x * (double) f2 - (double) f, this.locY() - vec3d.y, this.locZ() - vec3d.z * (double) f2 - (double) f1, 0.0D, 0.0D, 0.0D);
                }
            }

        }
    }

    @Override
    protected EnumInteractionResult b(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);

        if (!itemstack.isEmpty() && itemstack.getItem().a((Tag) TagsItem.FISHES)) {
            if (!this.world.isClientSide) {
                this.playSound(SoundEffects.ENTITY_DOLPHIN_EAT, 1.0F, 1.0F);
            }

            this.setGotFish(true);
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }

            return EnumInteractionResult.a(this.world.isClientSide);
        } else {
            return super.b(entityhuman, enumhand);
        }
    }

    public static boolean b(EntityTypes<EntityDolphin> entitytypes, GeneratorAccess generatoraccess, EnumMobSpawn enummobspawn, BlockPosition blockposition, Random random) {
        if (blockposition.getY() > 45 && blockposition.getY() < generatoraccess.getSeaLevel()) {
            Optional<ResourceKey<BiomeBase>> optional = generatoraccess.i(blockposition);

            return (!Objects.equals(optional, Optional.of(Biomes.OCEAN)) || !Objects.equals(optional, Optional.of(Biomes.DEEP_OCEAN))) && generatoraccess.getFluid(blockposition).a((Tag) TagsFluid.WATER);
        } else {
            return false;
        }
    }

    @Override
    protected SoundEffect getSoundHurt(DamageSource damagesource) {
        return SoundEffects.ENTITY_DOLPHIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundDeath() {
        return SoundEffects.ENTITY_DOLPHIN_DEATH;
    }

    @Nullable
    @Override
    protected SoundEffect getSoundAmbient() {
        return this.isInWater() ? SoundEffects.ENTITY_DOLPHIN_AMBIENT_WATER : SoundEffects.ENTITY_DOLPHIN_AMBIENT;
    }

    @Override
    protected SoundEffect getSoundSplash() {
        return SoundEffects.ENTITY_DOLPHIN_SPLASH;
    }

    @Override
    protected SoundEffect getSoundSwim() {
        return SoundEffects.ENTITY_DOLPHIN_SWIM;
    }

    protected boolean eN() {
        BlockPosition blockposition = this.getNavigation().h();

        return blockposition != null ? blockposition.a((IPosition) this.getPositionVector(), 12.0D) : false;
    }

    @Override
    public void g(Vec3D vec3d) {
        if (this.doAITick() && this.isInWater()) {
            this.a(this.dM(), vec3d);
            this.move(EnumMoveType.SELF, this.getMot());
            this.setMot(this.getMot().a(0.9D));
            if (this.getGoalTarget() == null) {
                this.setMot(this.getMot().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.g(vec3d);
        }

    }

    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }

    static class b extends PathfinderGoal {

        private final EntityDolphin a;
        private boolean b;

        b(EntityDolphin entitydolphin) {
            this.a = entitydolphin;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean C_() {
            return false;
        }

        @Override
        public boolean a() {
            return this.a.gotFish() && this.a.getAirTicks() >= 100 && this.a.world.getWorld().canGenerateStructures(); // MC-151364, SPIGOT-5494: hangs if generate-structures=false
        }

        @Override
        public boolean b() {
            BlockPosition blockposition = this.a.getTreasurePos();

            return !(new BlockPosition((double) blockposition.getX(), this.a.locY(), (double) blockposition.getZ())).a((IPosition) this.a.getPositionVector(), 4.0D) && !this.b && this.a.getAirTicks() >= 100;
        }

        @Override
        public void c() {
            if (this.a.world instanceof WorldServer) {
                WorldServer worldserver = (WorldServer) this.a.world;

                this.b = false;
                this.a.getNavigation().o();
                BlockPosition blockposition = this.a.getChunkCoordinates();
                StructureGenerator<?> structuregenerator = (double) worldserver.random.nextFloat() >= 0.5D ? StructureGenerator.OCEAN_RUIN : StructureGenerator.SHIPWRECK;
                BlockPosition blockposition1 = worldserver.a(structuregenerator, blockposition, 50, false);

                if (blockposition1 == null) {
                    StructureGenerator<?> structuregenerator1 = structuregenerator.equals(StructureGenerator.OCEAN_RUIN) ? StructureGenerator.SHIPWRECK : StructureGenerator.OCEAN_RUIN;
                    BlockPosition blockposition2 = worldserver.a(structuregenerator1, blockposition, 50, false);

                    if (blockposition2 == null) {
                        this.b = true;
                        return;
                    }

                    this.a.setTreasurePos(blockposition2);
                } else {
                    this.a.setTreasurePos(blockposition1);
                }

                worldserver.broadcastEntityEffect(this.a, (byte) 38);
            }
        }

        @Override
        public void d() {
            BlockPosition blockposition = this.a.getTreasurePos();

            if ((new BlockPosition((double) blockposition.getX(), this.a.locY(), (double) blockposition.getZ())).a((IPosition) this.a.getPositionVector(), 4.0D) || this.b) {
                this.a.setGotFish(false);
            }

        }

        @Override
        public void e() {
            World world = this.a.world;

            if (this.a.eN() || this.a.getNavigation().m()) {
                Vec3D vec3d = Vec3D.a((BaseBlockPosition) this.a.getTreasurePos());
                Vec3D vec3d1 = RandomPositionGenerator.a(this.a, 16, 1, vec3d, 0.39269909262657166D);

                if (vec3d1 == null) {
                    vec3d1 = RandomPositionGenerator.b(this.a, 8, 4, vec3d);
                }

                if (vec3d1 != null) {
                    BlockPosition blockposition = new BlockPosition(vec3d1);

                    if (!world.getFluid(blockposition).a((Tag) TagsFluid.WATER) || !world.getType(blockposition).a((IBlockAccess) world, blockposition, PathMode.WATER)) {
                        vec3d1 = RandomPositionGenerator.b(this.a, 8, 5, vec3d);
                    }
                }

                if (vec3d1 == null) {
                    this.b = true;
                    return;
                }

                this.a.getControllerLook().a(vec3d1.x, vec3d1.y, vec3d1.z, (float) (this.a.eo() + 20), (float) this.a.O());
                this.a.getNavigation().a(vec3d1.x, vec3d1.y, vec3d1.z, 1.3D);
                if (world.random.nextInt(80) == 0) {
                    world.broadcastEntityEffect(this.a, (byte) 38);
                }
            }

        }
    }

    static class c extends PathfinderGoal {

        private final EntityDolphin a;
        private final double b;
        private EntityHuman c;

        c(EntityDolphin entitydolphin, double d0) {
            this.a = entitydolphin;
            this.b = d0;
            this.a(EnumSet.of(PathfinderGoal.Type.MOVE, PathfinderGoal.Type.LOOK));
        }

        @Override
        public boolean a() {
            this.c = this.a.world.a(EntityDolphin.bp, (EntityLiving) this.a);
            return this.c == null ? false : this.c.isSwimming() && this.a.getGoalTarget() != this.c;
        }

        @Override
        public boolean b() {
            return this.c != null && this.c.isSwimming() && this.a.h((Entity) this.c) < 256.0D;
        }

        @Override
        public void c() {
            this.c.addEffect(new MobEffect(MobEffects.DOLPHINS_GRACE, 100), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.DOLPHIN); // CraftBukkit
        }

        @Override
        public void d() {
            this.c = null;
            this.a.getNavigation().o();
        }

        @Override
        public void e() {
            this.a.getControllerLook().a(this.c, (float) (this.a.eo() + 20), (float) this.a.O());
            if (this.a.h((Entity) this.c) < 6.25D) {
                this.a.getNavigation().o();
            } else {
                this.a.getNavigation().a((Entity) this.c, this.b);
            }

            if (this.c.isSwimming() && this.c.world.random.nextInt(6) == 0) {
                this.c.addEffect(new MobEffect(MobEffects.DOLPHINS_GRACE, 100), org.bukkit.event.entity.EntityPotionEffectEvent.Cause.DOLPHIN); // CraftBukkit
            }

        }
    }

    class d extends PathfinderGoal {

        private int b;

        private d() {}

        @Override
        public boolean a() {
            if (this.b > EntityDolphin.this.ticksLived) {
                return false;
            } else {
                List<EntityItem> list = EntityDolphin.this.world.a(EntityItem.class, EntityDolphin.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.b);

                return !list.isEmpty() || !EntityDolphin.this.getEquipment(EnumItemSlot.MAINHAND).isEmpty();
            }
        }

        @Override
        public void c() {
            List<EntityItem> list = EntityDolphin.this.world.a(EntityItem.class, EntityDolphin.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.b);

            if (!list.isEmpty()) {
                EntityDolphin.this.getNavigation().a((Entity) list.get(0), 1.2000000476837158D);
                EntityDolphin.this.playSound(SoundEffects.ENTITY_DOLPHIN_PLAY, 1.0F, 1.0F);
            }

            this.b = 0;
        }

        @Override
        public void d() {
            ItemStack itemstack = EntityDolphin.this.getEquipment(EnumItemSlot.MAINHAND);

            if (!itemstack.isEmpty()) {
                this.a(itemstack);
                EntityDolphin.this.setSlot(EnumItemSlot.MAINHAND, ItemStack.b);
                this.b = EntityDolphin.this.ticksLived + EntityDolphin.this.random.nextInt(100);
            }

        }

        @Override
        public void e() {
            List<EntityItem> list = EntityDolphin.this.world.a(EntityItem.class, EntityDolphin.this.getBoundingBox().grow(8.0D, 8.0D, 8.0D), EntityDolphin.b);
            ItemStack itemstack = EntityDolphin.this.getEquipment(EnumItemSlot.MAINHAND);

            if (!itemstack.isEmpty()) {
                this.a(itemstack);
                EntityDolphin.this.setSlot(EnumItemSlot.MAINHAND, ItemStack.b);
            } else if (!list.isEmpty()) {
                EntityDolphin.this.getNavigation().a((Entity) list.get(0), 1.2000000476837158D);
            }

        }

        private void a(ItemStack itemstack) {
            if (!itemstack.isEmpty()) {
                double d0 = EntityDolphin.this.getHeadY() - 0.30000001192092896D;
                EntityItem entityitem = new EntityItem(EntityDolphin.this.world, EntityDolphin.this.locX(), d0, EntityDolphin.this.locZ(), itemstack);

                entityitem.setPickupDelay(40);
                entityitem.setThrower(EntityDolphin.this.getUniqueID());
                float f = 0.3F;
                float f1 = EntityDolphin.this.random.nextFloat() * 6.2831855F;
                float f2 = 0.02F * EntityDolphin.this.random.nextFloat();

                entityitem.setMot((double) (0.3F * -MathHelper.sin(EntityDolphin.this.yaw * 0.017453292F) * MathHelper.cos(EntityDolphin.this.pitch * 0.017453292F) + MathHelper.cos(f1) * f2), (double) (0.3F * MathHelper.sin(EntityDolphin.this.pitch * 0.017453292F) * 1.5F), (double) (0.3F * MathHelper.cos(EntityDolphin.this.yaw * 0.017453292F) * MathHelper.cos(EntityDolphin.this.pitch * 0.017453292F) + MathHelper.sin(f1) * f2));
                EntityDolphin.this.world.addEntity(entityitem);
            }
        }
    }

    static class a extends ControllerMove {

        private final EntityDolphin i;

        public a(EntityDolphin entitydolphin) {
            super(entitydolphin);
            this.i = entitydolphin;
        }

        @Override
        public void a() {
            if (this.i.isInWater()) {
                this.i.setMot(this.i.getMot().add(0.0D, 0.005D, 0.0D));
            }

            if (this.h == ControllerMove.Operation.MOVE_TO && !this.i.getNavigation().m()) {
                double d0 = this.b - this.i.locX();
                double d1 = this.c - this.i.locY();
                double d2 = this.d - this.i.locZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 < 2.500000277905201E-7D) {
                    this.a.t(0.0F);
                } else {
                    float f = (float) (MathHelper.d(d2, d0) * 57.2957763671875D) - 90.0F;

                    this.i.yaw = this.a(this.i.yaw, f, 10.0F);
                    this.i.aA = this.i.yaw;
                    this.i.aC = this.i.yaw;
                    float f1 = (float) (this.e * this.i.b(GenericAttributes.MOVEMENT_SPEED));

                    if (this.i.isInWater()) {
                        this.i.q(f1 * 0.02F);
                        float f2 = -((float) (MathHelper.d(d1, (double) MathHelper.sqrt(d0 * d0 + d2 * d2)) * 57.2957763671875D));

                        f2 = MathHelper.a(MathHelper.g(f2), -85.0F, 85.0F);
                        this.i.pitch = this.a(this.i.pitch, f2, 5.0F);
                        float f3 = MathHelper.cos(this.i.pitch * 0.017453292F);
                        float f4 = MathHelper.sin(this.i.pitch * 0.017453292F);

                        this.i.aT = f3 * f1;
                        this.i.aS = -f4 * f1;
                    } else {
                        this.i.q(f1 * 0.1F);
                    }

                }
            } else {
                this.i.q(0.0F);
                this.i.v(0.0F);
                this.i.u(0.0F);
                this.i.t(0.0F);
            }
        }
    }
}
