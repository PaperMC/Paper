package net.minecraft.server;

import java.util.EnumSet;
import javax.annotation.Nullable;

public class EntityLlamaTrader extends EntityLlama {

    private int bw = 47999;

    public EntityLlamaTrader(EntityTypes<? extends EntityLlamaTrader> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected EntityLlama fz() {
        return (EntityLlama) EntityTypes.TRADER_LLAMA.a(this.world);
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("DespawnDelay", this.bw);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("DespawnDelay", 99)) {
            this.bw = nbttagcompound.getInt("DespawnDelay");
        }

    }

    @Override
    protected void initPathfinder() {
        super.initPathfinder();
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 2.0D));
        this.targetSelector.a(1, new EntityLlamaTrader.a(this));
    }

    @Override
    protected void h(EntityHuman entityhuman) {
        Entity entity = this.getLeashHolder();

        if (!(entity instanceof EntityVillagerTrader)) {
            super.h(entityhuman);
        }
    }

    @Override
    public void movementTick() {
        super.movementTick();
        if (!this.world.isClientSide) {
            this.fE();
        }

    }

    private void fE() {
        if (this.fF()) {
            this.bw = this.fG() ? ((EntityVillagerTrader) this.getLeashHolder()).eX() - 1 : this.bw - 1;
            if (this.bw <= 0) {
                this.unleash(true, false);
                this.die();
            }

        }
    }

    private boolean fF() {
        return !this.isTamed() && !this.fH() && !this.hasSinglePlayerPassenger();
    }

    private boolean fG() {
        return this.getLeashHolder() instanceof EntityVillagerTrader;
    }

    private boolean fH() {
        return this.isLeashed() && !this.fG();
    }

    @Nullable
    @Override
    public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        if (enummobspawn == EnumMobSpawn.EVENT) {
            this.setAgeRaw(0);
        }

        if (groupdataentity == null) {
            groupdataentity = new EntityAgeable.a(false);
        }

        return super.prepare(worldaccess, difficultydamagescaler, enummobspawn, (GroupDataEntity) groupdataentity, nbttagcompound);
    }

    public class a extends PathfinderGoalTarget {

        private final EntityLlama b;
        private EntityLiving c;
        private int d;

        public a(EntityLlama entityllama) {
            super(entityllama, false);
            this.b = entityllama;
            this.a(EnumSet.of(PathfinderGoal.Type.TARGET));
        }

        @Override
        public boolean a() {
            if (!this.b.isLeashed()) {
                return false;
            } else {
                Entity entity = this.b.getLeashHolder();

                if (!(entity instanceof EntityVillagerTrader)) {
                    return false;
                } else {
                    EntityVillagerTrader entityvillagertrader = (EntityVillagerTrader) entity;

                    this.c = entityvillagertrader.getLastDamager();
                    int i = entityvillagertrader.cZ();

                    return i != this.d && this.a(this.c, PathfinderTargetCondition.a);
                }
            }
        }

        @Override
        public void c() {
            this.e.setGoalTarget(this.c, org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER, true); // CraftBukkit
            Entity entity = this.b.getLeashHolder();

            if (entity instanceof EntityVillagerTrader) {
                this.d = ((EntityVillagerTrader) entity).cZ();
            }

            super.c();
        }
    }
}
